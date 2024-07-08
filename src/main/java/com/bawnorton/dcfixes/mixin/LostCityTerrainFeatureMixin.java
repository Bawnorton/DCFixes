package com.bawnorton.dcfixes.mixin;

import com.bawnorton.dcfixes.FasterLostCities;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mcjty.lostcities.worldgen.GlobalTodo;
import mcjty.lostcities.worldgen.LostCityTerrainFeature;
import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import java.util.function.Consumer;

@Mixin(LostCityTerrainFeature.class)
public abstract class LostCityTerrainFeatureMixin {
    @WrapOperation(
            method = "handleTodo",
            at = @At(
                    value = "INVOKE",
                    target = "Lmcjty/lostcities/worldgen/GlobalTodo;addTodo(Lnet/minecraft/util/math/BlockPos;Ljava/util/function/Consumer;)V"
            )
    )
    private void generateTreeWithoutWorld(GlobalTodo instance, BlockPos pos, Consumer<ServerWorld> code, Operation<Void> original, @Local(argsOnly = true) BlockState state) {
        original.call(instance, pos, (Consumer<ServerWorld>) world -> {
            Chunk chunk = FasterLostCities.CHUNK.get();
            BlockState saplingState = state.with(SaplingBlock.STAGE, 1);

            chunk.setBlockState(pos, saplingState, false);
        });
    }
}
