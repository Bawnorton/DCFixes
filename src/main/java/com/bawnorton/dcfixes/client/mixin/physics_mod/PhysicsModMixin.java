
package com.bawnorton.dcfixes.client.mixin.physics_mod;

import com.bawnorton.dcfixes.client.DeceasedCraftFixesClient;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.diebuddies.physics.PhysicsMod;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
        DeceasedCraftFixesClient.getCompat().getEmfCompat().prepare(entity);
    }

    @WrapOperation(
            method = "blockifyEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;render(Lnet/minecraft/world/entity/Entity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
                    remap = true
            ),
            remap = false
    )
    private static <T extends Entity> void replaceRenderedEntity(EntityRenderer<T> instance, T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, Operation<Void> original) {
        DeceasedCraftFixesClient.getCompat().getPhysicsModCompat().replaceRenderer(instance, entity, entityYaw, partialTick, poseStack, buffer, packedLight, original);
    }
}
