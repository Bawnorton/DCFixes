package com.bawnorton.dcfixes;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mod(DeceasedCraftFixes.MOD_ID)
public final class DeceasedCraftFixes {
    public static final String MOD_ID = "dcfixes";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final ThreadLocal<Boolean> READING = ThreadLocal.withInitial(() -> false);

    public DeceasedCraftFixes() {
        LOGGER.info("Fixing DeceasedCraft");
    }

    public static void onChunkLoaded(ServerWorld world, Chunk chunk) {
        FasterLostCities.executeAndClearTodo(world, chunk);
    }
}
