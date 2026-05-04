package com.bawnorton.dcfixes.compat.immersive_vehicles;

import com.bawnorton.dcfixes.DeceasedCraftFixes;
import com.bawnorton.dcfixes.config.DCFixesConfig;
import com.bawnorton.dcfixes.mixin.minecraft.accessor.ServerChunkCacheAccessor;
import com.mojang.datafixers.util.Either;
import minecrafttransportsimulator.baseclasses.Point3D;
import minecrafttransportsimulator.entities.components.AEntityF_Multipart;
import minecrafttransportsimulator.jsondefs.JSONVehicle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class IVVehicle {
    private final AEntityF_Multipart<?> root;
    private final boolean isAircraft;

    private long lastTickChecked = -1;
    private boolean lastTickResult = false;

    private IVVehicle(AEntityF_Multipart<?> root, boolean isAircraft) {
        this.root = root;
        this.isAircraft = isAircraft;
    }

    public static IVVehicle create(AEntityF_Multipart<?> root) {
        boolean isAircraft = false;
        if (root.definition instanceof JSONVehicle jsonVehicle) {
            isAircraft = jsonVehicle.motorized.isAircraft;
        }
        return new IVVehicle(root, isAircraft);
    }

    public AEntityF_Multipart<?> getRoot() {
        return root;
    }

    public boolean shouldTickServer(Point3D pos, ServerLevel level, long currentTick) {
        return shouldTick(pos, level, currentTick, blockPos -> {
            ServerChunkCache chunkSource = level.getChunkSource();
            int x = SectionPos.blockToSectionCoord(blockPos.getX());
            int z = SectionPos.blockToSectionCoord(blockPos.getZ());
            CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> chunkFuture = ((ServerChunkCacheAccessor) chunkSource).dcfixes$getChunkFutureMainThread(x, z, ChunkStatus.FULL, false);
            if (!chunkFuture.isDone()) return false;

            return level.isPositionEntityTicking(blockPos);
        });
    }

    public boolean shouldTickClient(Point3D pos, Level level, long currentTick) {
        if (currentTick % 20 == 0) {
            return true;
        }
        return shouldTick(pos, level, currentTick, level::isLoaded);
    }

    private <T extends Level> boolean shouldTick(Point3D pos, T level, long currentTick, Predicate<BlockPos> posPredicate) {
        if (this.lastTickChecked == currentTick) {
            return this.lastTickResult;
        }

        this.lastTickChecked = currentTick;
        this.lastTickResult = false;

        BlockPos blockPos = BlockPos.containing(pos.x, pos.y, pos.z);
        if (!posPredicate.test(blockPos)) {
            return false;
        }

        if (isAircraft && DCFixesConfig.get().aircraftAlwaysTick) {
            return lastTickResult = true;
        }

        Player nearestPlayer = level.getNearestPlayer(pos.x, pos.y, pos.z, DCFixesConfig.get().dormatTickingDistance, false);
        lastTickResult = nearestPlayer != null;
        return lastTickResult;
    }
}