package com.bawnorton.dcfixes.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import vazkii.quark.base.handler.InventoryTransferHandler;

@Mixin(InventoryTransferHandler.class)
public abstract class InventoryTransfterHandlerMixin {
    @ModifyExpressionValue(
            method = "accepts",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=27"
            )
    )
    private static int allowSmallerContainers(int original) {
        return 5;
    }
}
