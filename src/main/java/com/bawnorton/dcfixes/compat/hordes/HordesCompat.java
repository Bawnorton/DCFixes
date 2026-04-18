package com.bawnorton.dcfixes.compat.hordes;

import com.bawnorton.dcfixes.config.DCFixesConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.smileycorp.hordes.common.event.HordeFindSpawnPosEvent;

public class HordesCompat {
    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onHordeFindSpawnPos(HordeFindSpawnPosEvent event) {
        BlockPos furthestPos = event.getPos();
        Vec3 direction = event.getDir().reverse();
        Vec3 newPos = Vec3.atCenterOf(furthestPos).add(direction.scale(DCFixesConfig.get().hordeWavesLoadedDistanceBuffer));
        BlockPos newBasePos = BlockPos.containing(newPos);
        ServerLevel level = (ServerLevel) event.getEntityWorld();
        int height = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, newBasePos.getX(), newBasePos.getZ());
        newBasePos.atY(height);
        event.setPos(newBasePos);
    }
}
