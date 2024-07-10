package com.bawnorton.dcfixes.mixin.lostcities;

import com.bawnorton.dcfixes.FasterLostCities;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WorldChunk.class)
public abstract class WorldChunkMixin {
    @Shadow public abstract void removeBlockEntity(BlockPos pos);

    @WrapOperation(
            method = "setBlockState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockState;onStateReplaced(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Z)V"
            )
    )
    private void handleGeneration(BlockState instance, World world, BlockPos blockPos, BlockState newState, boolean b, Operation<Void> original) {
        if(FasterLostCities.CHUNK.get() != null) {
            if (instance.hasBlockEntity() && (!instance.isOf(newState.getBlock()) || !newState.hasBlockEntity())) {
                removeBlockEntity(blockPos);
            }
        } else {
            original.call(instance, world, blockPos, newState, b);
        }
    }

    @WrapOperation(
            method = "loadBlockEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V",
                    remap = false,
                    ordinal = 1
            )
    )
    private void dontLogOnGen(Logger instance, String message, Object arg1, Object arg2, Operation<Void> original) {
        if(FasterLostCities.CHUNK.get() == null) {
            original.call(instance, message, arg1, arg2);
        }
    }
}
