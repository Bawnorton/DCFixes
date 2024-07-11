package com.bawnorton.dcfixes.mixin.quark;

import com.bawnorton.dcfixes.DeceasedCraftFixes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import vazkii.quark.base.handler.InventoryTransferHandler;

@Mixin(InventoryTransferHandler.class)
public abstract class InventoryTransfterHandlerMixin {
    @ModifyReturnValue(
            method = "accepts",
            at = @At("RETURN")
    )
    private static boolean allowOtherContainers(boolean original, @Local(argsOnly = true) ScreenHandler screenHandler) {
        return original || DeceasedCraftFixes.QUARK_ALLOWED_HANDLERS.contains(screenHandler.getClass());
    }
}
