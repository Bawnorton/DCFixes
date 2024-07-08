package com.bawnorton.dcfixes.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import vazkii.quark.api.IQuarkButtonAllowed;
import vazkii.quark.base.client.handler.InventoryButtonHandler;

@Mixin(value = InventoryButtonHandler.class, remap = false)
public abstract class InventoryButtonHandlerMixin {
    @WrapOperation(
            method = "mouseInputEvent",
            at = @At(
                    value = "INVOKE",
                    target = "Lvazkii/quark/base/handler/GeneralConfig;isScreenAllowed(Ljava/lang/Object;)Z"
            )
    )
    private static boolean orButtonProvider(Object screen, Operation<Boolean> original) {
        return original.call(screen) || screen instanceof IQuarkButtonAllowed;
    }
}
