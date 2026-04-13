/*

package com.bawnorton.dcfixes.client.mixin.emf;

import com.bawnorton.dcfixes.client.DeceasedCraftFixesClient;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.diebuddies.physics.PhysicsMod;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import traben.entity_model_features.models.parts.EMFModelPart;

@MixinEnvironment("client")
@Mixin(EMFModelPart.class)
abstract class EMFModelPartMixin {
    @WrapMethod(
            method = "renderLikeVanilla",
            remap = false
    )
    private void applyMatrix(PoseStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha, Operation<Void> original) {
        if (PhysicsMod.getCurrentInstance() != null && PhysicsMod.getCurrentInstance().blockify && ((ModelPart) (Object) this).visible) {
            PhysicsMod.getCurrentInstance().localPivotMatrix.pushPose();
            ((ModelPart) (Object) this).translateAndRotate(PhysicsMod.getCurrentInstance().localPivotMatrix);
            original.call(matrices, vertices, light, overlay, red, green, blue, alpha);
            PhysicsMod.getCurrentInstance().localPivotMatrix.popPose();
        } else {
            original.call(matrices, vertices, light, overlay, red, green, blue, alpha);
        }
    }

    @Inject(
        method = "compile",
        at = @At("HEAD")
    )
    private void createParticles(PoseStack.Pose pose, VertexConsumer vertexConsumer, int i, int j, float red, float green, float blue, float alpha, CallbackInfo ci) {
        if (PhysicsMod.getCurrentInstance() != null && PhysicsMod.getCurrentInstance().blockify && ((ModelPart) (Object) this).visible) {
            DeceasedCraftFixesClient.getCompat().getPhysicsModCompat().createParticlesFromEmfCuboids(
                    pose,
                    PhysicsMod.getCurrentInstance().localPivotMatrix,
                    ((ModelPart) (Object) this).cubes,
                    (ModelPart) (Object) this,
                    PhysicsMod.getCurrentInstance().cubifyEntity,
                    PhysicsMod.getCurrentInstance().blockifyFeature,
                    red,
                    green,
                    blue
            );
        }
    }
}
*/
