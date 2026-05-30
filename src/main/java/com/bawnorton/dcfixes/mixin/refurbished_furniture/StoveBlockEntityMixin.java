package com.bawnorton.dcfixes.mixin.refurbished_furniture;

import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mrcrayfish.furniture.refurbished.block.StoveBlock;
import com.mrcrayfish.furniture.refurbished.blockentity.StoveBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@IfModLoaded("refurbished_furniture")
@Mixin(StoveBlockEntity.class)
abstract class StoveBlockEntityMixin {
    @Shadow
    protected int processingTime;
    @Unique
    private boolean dcfixes$powered;

    @Inject(
            method = "<init>(Lnet/minecraft/world/level/block/entity/BlockEntityType;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V",
            at = @At("TAIL"),
            remap = false
    )
    private void setPowered(BlockEntityType<?> type, BlockPos pos, BlockState state, CallbackInfo ci) {
        dcfixes$powered = state.hasProperty(StoveBlock.POWERED) && state.getValue(StoveBlock.POWERED);
    }

    @Definition(id = "processingTime", field = "Lcom/mrcrayfish/furniture/refurbished/blockentity/StoveBlockEntity;processingTime:I")
    @Definition(id = "time", local = @Local(type = int.class, argsOnly = true))
    @Expression("time > @(?.processingTime)")
    @ModifyExpressionValue(
            method = "setProcessingTime",
            at = @At("MIXINEXTRAS:EXPRESSION"),
            remap = false
    )
    private int preventEagerChanges(int original) {
        return 0;
    }

    @Definition(id = "time", local = @Local(type = int.class, argsOnly = true))
    @Definition(id = "processingTime", field = "Lcom/mrcrayfish/furniture/refurbished/blockentity/StoveBlockEntity;processingTime:I")
    @Expression("time < ?.processingTime")
    @ModifyExpressionValue(
            method = "setProcessingTime",
            at = @At("MIXINEXTRAS:EXPRESSION"),
            remap = false
    )
    private boolean preventEagerChanges(boolean original) {
        return processingTime > 0;
    }

    @Definition(id = "processingTime", field = "Lcom/mrcrayfish/furniture/refurbished/blockentity/StoveBlockEntity;processingTime:I")
    @Definition(id = "time", local = @Local(type = int.class, argsOnly = true))
    @Expression("?.processingTime != time")
    @ModifyExpressionValue(
            method = "setProcessingTime",
            at = @At("MIXINEXTRAS:EXPRESSION"),
            remap = false
    )
    private boolean preventEagerChanges0(boolean original, @Local(name = "time") int time) {
        processingTime = time;
        return false;
    }

    @Inject(
            method = "setBlockState",
            at = @At("TAIL")
    )
    private void updatePowered(BlockState state, CallbackInfo ci) {
        dcfixes$powered = state.hasProperty(StoveBlock.POWERED)  && state.getValue(StoveBlock.POWERED);
    }

    /**
     * @author Bawnorton
     * @reason optimisations
     */
    @Overwrite(remap = false)
    public boolean isNodePowered() {
        return this.dcfixes$powered;
    }
}
