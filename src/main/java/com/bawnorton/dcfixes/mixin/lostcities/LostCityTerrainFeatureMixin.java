package com.bawnorton.dcfixes.mixin.lostcities;

import com.bawnorton.dcfixes.DeceasedCraftFixes;
import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mcjty.lostcities.worldgen.ChunkDriver;
import mcjty.lostcities.worldgen.IDimensionInfo;
import mcjty.lostcities.worldgen.LostCityTerrainFeature;
import mcjty.lostcities.worldgen.lost.BuildingInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@IfModLoaded("lostcities")
@Mixin(value = LostCityTerrainFeature.class, remap = false)
abstract class LostCityTerrainFeatureMixin {
    @Shadow
    @Final
    public IDimensionInfo provider;

    @WrapOperation(
            method = "generatePart",
            at = @At(
                    value = "INVOKE",
                    target = "Lmcjty/lostcities/worldgen/ChunkDriver;add(Lnet/minecraft/world/level/block/state/BlockState;)Lmcjty/lostcities/worldgen/ChunkDriver;"
            )
    )
    private ChunkDriver ensureBlockEntitiesLoadCorrectly(
            ChunkDriver instance,
            BlockState state,
            Operation<ChunkDriver> original,
            @Local(argsOnly = true) BuildingInfo info,
            @Local(name = "oy") int oy,
            @Local(name = "rx") int rx,
            @Local(name = "rz") int rz,
            @Local(name = "y") int y
    ) {
        if(state.getBlock() instanceof EntityBlock) {
            BlockPos pos = info.getRelativePos(rx, oy + y, rz);
            DeceasedCraftFixes.getCompat().getLostCitiesCompat().orElseThrow().ensureBlockLoadsCorrectly(pos);
        }
        return original.call(instance, state);
    }
}
