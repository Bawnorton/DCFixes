package com.bawnorton.dcfixes.client.compat.lostcities;

import com.bawnorton.dcfixes.DeceasedCraftFixes;
import com.bawnorton.dcfixes.network.clientbound.ClientboundRebuildModelPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LostCitiesClientCompat {
    private static final Path SAVE_FILE = FMLPaths.CONFIGDIR.get().resolve("dcfixes").resolve("lostcities_rebuild.nbt");

    private final Map<ChunkPos, List<BlockPos>> toRebuild = new HashMap<>();

    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void handlePacket(ClientboundRebuildModelPacket packet) {
       ClientLevel level = Minecraft.getInstance().level;
       if (level == null) return;

       ChunkPos pos = packet.chunkPos();
       LevelChunk chunk = level.getChunk(pos.x, pos.z);
       if(chunk.isEmpty()) {
           toRebuild.put(pos, packet.positions());
       } else {
           packet.positions().forEach(level::getBlockEntity);
       }
    }

    @SubscribeEvent
    public void onLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        loadFromDisk();
    }

    @SubscribeEvent
    public void onLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        saveToDisk();
        toRebuild.clear();
    }

    @SubscribeEvent
    public void onChunkLoad(ChunkEvent.Load event) {
        LevelAccessor level = event.getLevel();
        if(level.isClientSide()) {
            ChunkAccess chunk = event.getChunk();
            List<BlockPos> positions = toRebuild.remove(chunk.getPos());
            if(positions == null) return;

            positions.forEach(level::getBlockEntity);
        }
    }

    private void saveToDisk() {
        if (toRebuild.isEmpty()) {
            try {
                Files.deleteIfExists(SAVE_FILE);
            } catch (IOException ignored) {}
            return;
        }
        CompoundTag root = new CompoundTag();
        ListTag chunks = new ListTag();
        for (Map.Entry<ChunkPos, List<BlockPos>> entry : toRebuild.entrySet()) {
            CompoundTag chunkTag = new CompoundTag();
            chunkTag.putLong("chunk", entry.getKey().toLong());
            ListTag posTag = new ListTag();
            for (BlockPos pos : entry.getValue()) {
                posTag.add(LongTag.valueOf(pos.asLong()));
            }
            chunkTag.put("positions", posTag);
            chunks.add(chunkTag);
        }
        root.put("chunks", chunks);
        try {
            Files.createDirectories(SAVE_FILE.getParent());
            NbtIo.write(root, SAVE_FILE.toFile());
        } catch (IOException e) {
            DeceasedCraftFixes.LOGGER.error("Failed to save lostcities rebuild positions", e);
        }
    }

    private void loadFromDisk() {
        CompoundTag root;
        try {
            root = NbtIo.read(SAVE_FILE.toFile());
        } catch (IOException e) {
            DeceasedCraftFixes.LOGGER.error("Failed to load lostcities rebuild positions", e);
            return;
        }
        if (root == null) return;

        ListTag chunks = root.getList("chunks", Tag.TAG_COMPOUND);
        for (Tag t : chunks) {
            CompoundTag chunkTag = (CompoundTag) t;
            ChunkPos chunkPos = new ChunkPos(chunkTag.getLong("chunk"));
            ListTag posTag = chunkTag.getList("positions", Tag.TAG_LONG);
            List<BlockPos> positions = new ArrayList<>(posTag.size());
            for (Tag pt : posTag) {
                positions.add(BlockPos.of(((LongTag) pt).getAsLong()));
            }
            toRebuild.put(chunkPos, positions);
        }
    }
}
