package com.bawnorton.dcfixes.mixin.quark;

import com.bawnorton.dcfixes.DeceasedCraftFixes;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import vazkii.quark.api.IQuarkButtonAllowed;
import vazkii.quark.base.client.handler.InventoryButtonHandler;

@Mixin(InventoryButtonHandler.class)
public abstract class InventoryButtonHandlerMixin {
    @Unique
    private static final ThreadLocal<Screen> dcfixes$CURRENT_SCREEN = ThreadLocal.withInitial(() -> null);

    @WrapOperation(
            method = "mouseInputEvent",
            at = @At(
                    value = "INVOKE",
                    target = "Lvazkii/quark/base/handler/GeneralConfig;isScreenAllowed(Ljava/lang/Object;)Z"
            ),
            remap = false
    )
    private static boolean orButtonProvider(Object screen, Operation<Boolean> original) {
        return original.call(screen) || screen instanceof IQuarkButtonAllowed;
    }

    @ModifyExpressionValue(
            method = "initGui",
            at = @At(
                    value = "INVOKE",
                    //? if <=1.18.2 {
                    /*target = "Lnet/minecraftforge/client/event/ScreenEvent$InitScreenEvent$Post;getScreen()Lnet/minecraft/client/gui/screen/Screen;"
                    *///?} else {
                    target = "Lnet/minecraftforge/client/event/ScreenEvent$Init$Post;getScreen()Lnet/minecraft/client/gui/screen/Screen;"
                    //?}
            )
    )
    private static Screen captureScreen(Screen original) {
        dcfixes$CURRENT_SCREEN.set(original);
        return original;
    }

    @ModifyExpressionValue(
            method = "lambda$initGui$1",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=8"
            ),
            remap = false
    )
    private static int changeTargetSlot(int original) {
        Screen screen = dcfixes$CURRENT_SCREEN.get();
        Class<? extends Screen> clazz = screen.getClass();
        return DeceasedCraftFixes.QUARK_BUTTON_SLOT_INDEX.getOrDefault(clazz, original);
    }
}
