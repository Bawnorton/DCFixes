package com.bawnorton.dcfixes.mixin.hazardous;

import com.bawnorton.dcfixes.mixin.minecraft.accessor.ServerChunkCacheAccessor;
import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mcjty.hazardous.data.HazardManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkStatus;
import org.spongepowered.asm.mixin.Mixin;

@IfModLoaded("hazardous")
@Mixin(HazardManager.class)
abstract class HazardManagerMixin {
    @WrapMethod(
            method = "isChunkLoaded"
    )
    private static boolean directlyQueryFutureInstead(Level level, BlockPos pos, Operation<Boolean> original) {
        if (level instanceof ServerLevel serverLevel) {
            ServerChunkCache chunkSource = serverLevel.getChunkSource();
            int x = SectionPos.blockToSectionCoord(pos.getX());
            int z = SectionPos.blockToSectionCoord(pos.getZ());

            if (((ServerChunkCacheAccessor) chunkSource).dcfixes$mainThread() != Thread.currentThread()) return chunkSource.hasChunk(x, z);
            return ((ServerChunkCacheAccessor) chunkSource).dcfixes$getChunkFutureMainThread(x, z, ChunkStatus.FULL, false).isDone();
        } else if (level.isClientSide()) {
            return level.isLoaded(pos);
        }
        return original.call(level, pos);
    }
}
