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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class LostCitiesCompat {
    private final Queue<Loader> incoming = new ConcurrentLinkedQueue<>();
    private List<Loader> pending = new ArrayList<>();
    private List<Loader> retry = new ArrayList<>();
    private final List<BlockPos> toRebuild = new ArrayList<>();
    private LostCitiesSavedData savedData;

    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void ensureBlockLoadsCorrectly(BlockPos pos) {
        incoming.add(new Loader(pos));
    }

    @SubscribeEvent
    public void onTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (event.level instanceof ServerLevel serverLevel) {
                tickLoaders(serverLevel);
                if (!toRebuild.isEmpty()) {
                    Map<ChunkPos, List<BlockPos>> chunksToRebuild = new HashMap<>();
                    for (BlockPos pos : toRebuild) {
                        ChunkPos chunkPos = new ChunkPos(pos);
                        chunksToRebuild.computeIfAbsent(chunkPos, k -> new ArrayList<>()).add(pos);
                    }
                    for (List<BlockPos> entry : chunksToRebuild.values()) {
                        DCNetworking.CHANNEL.send(PacketDistributor.ALL.noArg(), new ClientboundRebuildModelPacket(entry));
                    }
                    toRebuild.clear();
                }
            }
        }
    }

    @SubscribeEvent
    public void onLevelLoad(LevelEvent.Load event) {
        if (!(event.getLevel() instanceof ServerLevel serverLevel)) return;
        if (!serverLevel.dimension().equals(Level.OVERWORLD)) return;

        savedData = serverLevel.getDataStorage().computeIfAbsent(LostCitiesSavedData::load, LostCitiesSavedData::new, LostCitiesSavedData.ID);
        savedData.getPositions().forEach(pos -> incoming.add(new Loader(pos)));
    }

    @SubscribeEvent
    public void onLevelSave(LevelEvent.Save event) {
        if (savedData == null) return;
        if (!(event.getLevel() instanceof ServerLevel serverLevel)) return;
        if (!serverLevel.dimension().equals(Level.OVERWORLD)) return;

        snapshot();
    }

    @SubscribeEvent
    public void onLevelUnload(LevelEvent.Unload event) {
        if (savedData == null) return;
        if (!(event.getLevel() instanceof ServerLevel serverLevel)) return;
        if (!serverLevel.dimension().equals(Level.OVERWORLD)) return;

        snapshot();
        savedData = null;
        pending.clear();
        retry.clear();
        incoming.clear();
        toRebuild.clear();
    }

    private void snapshot() {
        List<BlockPos> all = new ArrayList<>(pending.size() + retry.size());
        for (Loader l : pending) all.add(l.pos);
        for (Loader l : retry) all.add(l.pos);
        savedData.update(all);
    }

    private void tickLoaders(ServerLevel level) {
        Loader loader;
        while ((loader = incoming.poll()) != null) {
            pending.add(loader);
        }
        for (Loader l : pending) {
            l.accept(level);
        }
        pending.clear();
        List<Loader> tmp = pending;
        pending = retry;
        retry = tmp;
    }

    private class Loader implements Consumer<ServerLevel> {
        private final BlockPos pos;

        public Loader(BlockPos pos) {
            this.pos = pos;
        }

        @Override
        public void accept(ServerLevel level) {
            ServerChunkCache chunkSource = level.getChunkSource();
            int x = SectionPos.blockToSectionCoord(pos.getX());
            int z = SectionPos.blockToSectionCoord(pos.getZ());
            if(!chunkSource.hasChunk(x, z)) {
                retry.add(this);
                return;
            }

            CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> chunkFuture = ((ServerChunkCacheAccessor) chunkSource).dcfixes$getChunkFutureMainThread(x, z, ChunkStatus.FULL, false);
            if (!chunkFuture.isDone()) {
                retry.add(this);
                return;
            }

            BlockEntity be = level.getBlockEntity(pos);
            if (be == null) return;

            toRebuild.add(pos);
        }
    }
}
