package com.bawnorton.dcfixes.client.mixin.immersive_vehicles;

import com.bawnorton.dcfixes.client.extend.InterfaceRenderExtender;
import com.bawnorton.mixinsquared.TargetHandler;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@MixinEnvironment("client")
@Mixin(value = LevelRenderer.class, priority = 1500)
abstract class LevelRendererMixinSquared {
    @Shadow
    private Frustum cullingFrustum;

    @TargetHandler(
            mixin = "mcinterface1201.mixin.client.LevelRendererMixin",
            name = "inject_renderLevelDataGetter"
    )
    @Inject(
            method = "@MixinSquared:Handler",
            at = @At("TAIL")
    )
    private void alsoCaptureFrustum(CallbackInfo ci) {
        InterfaceRenderExtender.StaticHolder.activeFrustum = cullingFrustum;
    }
}
