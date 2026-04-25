package com.bawnorton.dcfixes.client.mixin.iris;

import com.bawnorton.dcfixes.config.DCFixesConfig;
import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.irisshaders.iris.mixin.LevelRendererAccessor;
import net.irisshaders.iris.shadows.ShadowRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@IfModLoaded("oculus")
@MixinEnvironment("client")
@Mixin(value = ShadowRenderer.class, remap = false)
abstract class ShadowRendererMixin {
    @WrapMethod(
            method = "renderPlayerEntity"
    )
    private int useConfig(LevelRendererAccessor levelRenderer, EntityRenderDispatcher dispatcher, MultiBufferSource.BufferSource bufferSource, PoseStack modelView, float tickDelta, Frustum frustum, double cameraX, double cameraY, double cameraZ, Operation<Integer> original) {
        if(DCFixesConfig.get().renderPlayerShadows) {
            return original.call(levelRenderer, dispatcher, bufferSource, modelView, tickDelta, frustum, cameraX, cameraY, cameraZ);
        }
        return 0;
    }

    @WrapOperation(
            method = "renderEntities",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;shouldRender(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/culling/Frustum;DDD)Z",
                    remap = true
            )
    )
    private <E extends Entity> boolean useConfigForPlayers(EntityRenderDispatcher instance, E entity, Frustum frustum, double camX, double camY, double camZ, Operation<Boolean> original) {
        if(DCFixesConfig.get().renderPlayerShadows) {
            return original.call(instance, entity, frustum, camX, camY, camZ);
        }

        return !(entity instanceof Player) && original.call(instance, entity, frustum, camX, camY, camZ);
    }
}
