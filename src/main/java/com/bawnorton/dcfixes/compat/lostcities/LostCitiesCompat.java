package com.bawnorton.dcfixes.compat.lostcities;

import com.bawnorton.dcfixes.mixin.minecraft.accessor.ServerChunkCacheAccessor;
import com.bawnorton.dcfixes.network.DCNetworking;
import com.bawnorton.dcfixes.network.clientbound.ClientboundRebuildModelPacket;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LostCitiesCompat {
    private static final int MAX_INCOMING_PER_TICK = 16384;

    private final Queue<BlockPos> incoming = new ConcurrentLinkedQueue<>();
    private final Map<Long, List<BlockPos>> pendingBuffer = new HashMap<>();
    private final Set<Long> pendingChunks = new HashSet<>();
    private final List<BlockPos> toRebuild = new ArrayList<>();
    private Path pendingDir;

    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void ensureBlockLoadsCorrectly(BlockPos pos) {
        incoming.add(pos);
    }

    @SubscribeEvent
    public void onTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (!(event.level instanceof ServerLevel serverLevel)) return;
        if (!serverLevel.dimension().equals(Level.OVERWORLD)) return;

        int count = 0;
        BlockPos pos;
        while (count++ < MAX_INCOMING_PER_TICK && (pos = incoming.poll()) != null) {
            schedulePosition(serverLevel, pos);
        }

        checkPendingChunks(serverLevel);
        flushPendingBuffer();

        if (!toRebuild.isEmpty()) {
            Map<ChunkPos, List<BlockPos>> chunkMap = new HashMap<>();
            for (BlockPos blockPos : toRebuild) {
                ChunkPos cp = new ChunkPos(blockPos);
                chunkMap.computeIfAbsent(cp, k -> new ArrayList<>()).add(blockPos);
            }
            for (Map.Entry<ChunkPos, List<BlockPos>> entry : chunkMap.entrySet()) {
                DCNetworking.CHANNEL.send(PacketDistributor.ALL.noArg(), new ClientboundRebuildModelPacket(entry.getKey(), entry.getValue()));
            }
            toRebuild.clear();
        }
    }

    @SubscribeEvent
    public void onLevelLoad(LevelEvent.Load event) {
        if (!(event.getLevel() instanceof ServerLevel serverLevel)) return;
        if (!serverLevel.dimension().equals(Level.OVERWORLD)) return;

        pendingDir = serverLevel.getServer()
                .getWorldPath(LevelResource.ROOT)
                .resolve("data/dcfixes_pending");

        try {
            Files.createDirectories(pendingDir);
        } catch (IOException e) {
            pendingDir = null;
        }

        if (pendingDir != null) {
            try (DirectoryStream<Path> dir = Files.newDirectoryStream(pendingDir, "c.*.*")) {
                for (Path file : dir) {
                    String[] parts = file.getFileName().toString().substring(2).split("\\.");
                    if (parts.length == 2) {
                        try {
                            int cx = Integer.parseInt(parts[0]);
                            int cz = Integer.parseInt(parts[1]);
                            pendingChunks.add(ChunkPos.asLong(cx, cz));
                        } catch (NumberFormatException ignored) {}
                    }
                }
            } catch (IOException ignored) {}
        }
    }

    @SubscribeEvent
    public void onLevelUnload(LevelEvent.Unload event) {
        if (!(event.getLevel() instanceof ServerLevel)) return;
        if (!((ServerLevel) event.getLevel()).dimension().equals(Level.OVERWORLD)) return;

        BlockPos pos;
        while ((pos = incoming.poll()) != null) {
            pendingBuffer.computeIfAbsent(chunkKey(pos), k -> new ArrayList<>()).add(pos);
        }
        flushPendingBuffer();

        pendingChunks.clear();
        toRebuild.clear();
        pendingDir = null;
    }

    private void checkPendingChunks(ServerLevel level) {
        if (pendingChunks.isEmpty()) return;

        ServerChunkCache chunkSource = level.getChunkSource();
        List<Long> ready = null;
        for (long key : pendingChunks) {
            ChunkPos cp = new ChunkPos(key);
            if (chunkSource.hasChunk(cp.x, cp.z)) {
                if (ready == null) ready = new ArrayList<>();
                ready.add(key);
            }
        }
        if (ready != null) {
            for (long key : ready) {
                processChunkPending(level, new ChunkPos(key), key);
            }
        }
    }

    private void schedulePosition(ServerLevel level, BlockPos pos) {
        int cx = SectionPos.blockToSectionCoord(pos.getX());
        int cz = SectionPos.blockToSectionCoord(pos.getZ());
        if (level.getChunkSource().hasChunk(cx, cz)) {
            processPosition(level, pos);
        } else {
            long key = ChunkPos.asLong(cx, cz);
            pendingBuffer.computeIfAbsent(key, k -> new ArrayList<>()).add(pos);
            pendingChunks.add(key);
        }
    }

    private void processChunkPending(ServerLevel level, ChunkPos chunkPos, long key) {
        pendingChunks.remove(key);

        List<BlockPos> buffered = pendingBuffer.remove(key);
        if (buffered != null) {
            for (BlockPos pos : buffered) processPosition(level, pos);
        }

        if (pendingDir != null) {
            Path file = pendingDir.resolve("c." + chunkPos.x + "." + chunkPos.z);
            if (Files.exists(file)) {
                for (BlockPos pos : readChunkFile(file)) {
                    processPosition(level, pos);
                }
                try {
                    Files.delete(file);
                } catch (IOException ignored) {}
            }
        }
    }

    private void processPosition(ServerLevel level, BlockPos pos) {
        ServerChunkCache chunkSource = level.getChunkSource();
        int cx = SectionPos.blockToSectionCoord(pos.getX());
        int cz = SectionPos.blockToSectionCoord(pos.getZ());

        CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> future =
                ((ServerChunkCacheAccessor) chunkSource).dcfixes$getChunkFutureMainThread(cx, cz, ChunkStatus.FULL, false);
        if (!future.isDone()) {
            long key = ChunkPos.asLong(cx, cz);
            pendingBuffer.computeIfAbsent(key, k -> new ArrayList<>()).add(pos);
            pendingChunks.add(key);
            return;
        }

        BlockEntity be = level.getBlockEntity(pos);
        if (be == null) return;

        toRebuild.add(pos);
    }

    private void flushPendingBuffer() {
        if (pendingBuffer.isEmpty() || pendingDir == null) return;

        for (Map.Entry<Long, List<BlockPos>> entry : pendingBuffer.entrySet()) {
            ChunkPos cp = new ChunkPos(entry.getKey());
            Path file = pendingDir.resolve("c." + cp.x + "." + cp.z);
            try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
                    Files.newOutputStream(file, StandardOpenOption.CREATE, StandardOpenOption.APPEND)))) {
                for (BlockPos pos : entry.getValue()) {
                    out.writeLong(pos.asLong());
                }
            } catch (IOException ignored) {}
        }
        pendingBuffer.clear();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private List<BlockPos> readChunkFile(Path file) {
        List<BlockPos> positions = new ArrayList<>();
        try (DataInputStream in = new DataInputStream(new BufferedInputStream(Files.newInputStream(file)))) {
            try {
                while (true) {
                    long posLong = in.readLong();
                    positions.add(BlockPos.of(posLong));
                }
            } catch (EOFException ignored) {}
        } catch (IOException ignored) {}
        return positions;
    }

    private long chunkKey(BlockPos pos) {
        return ChunkPos.asLong(
                SectionPos.blockToSectionCoord(pos.getX()),
                SectionPos.blockToSectionCoord(pos.getZ())
        );
    }
}
