
package com.bawnorton.dcfixes.client.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.diebuddies.physics.PhysicsMod;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import traben.entity_texture_features.ETFApi;
import traben.entity_texture_features.features.ETFRenderContext;

@MixinEnvironment("client")
@Mixin(PhysicsMod.class)
abstract class PhysicsModMixin {
    @Inject(
            method = "blockifyEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;render(Lnet/minecraft/world/entity/Entity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
                    remap = true
            ),
            remap = false
    )
    private static void prepareForEMF(Level level, LivingEntity entity, CallbackInfo ci) {
        ETFRenderContext.setCurrentEntity(ETFApi.stateOfEntityOrEntityState(entity));
    }
}
