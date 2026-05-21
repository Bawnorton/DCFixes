package com.bawnorton.dcfixes.client.mixin.create;

import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.platform.Window;
import com.simibubi.create.compat.trainmap.XaeroTrainMap;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@IfModLoaded("create")
@MixinEnvironment("client")
@Mixin(value = XaeroTrainMap.class, remap = false)
abstract class XaeroTrainMapMixin {
    @ModifyVariable(
            method = "onRender",
            at = @At("STORE"),
            name = "scale"
    )
    private static double respectInterfaceScale(double scale, @Local(name = "window") Window window) {
        double interfaceScale = (double) window.getWidth() / window.getScreenWidth();
        return scale / interfaceScale;
    }
}
