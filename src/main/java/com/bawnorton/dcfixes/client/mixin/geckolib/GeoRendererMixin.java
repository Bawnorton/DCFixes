package com.bawnorton.dcfixes.client.mixin.geckolib;

import com.bawnorton.dcfixes.client.DeceasedCraftFixesClient;
import com.bawnorton.dcfixes.client.compat.physics_mod.PhysicsModCompat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.diebuddies.physics.PhysicsMod;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.GeoCube;
import software.bernie.geckolib.cache.object.GeoQuad;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.util.RenderUtils;

@MixinEnvironment("client")
@Mixin(GeoRenderer.class)
public interface GeoRendererMixin {
    @Shadow(remap = false)
    void createVerticesOfQuad(GeoQuad quad, Matrix4f poseState, Vector3f normal, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha);

    /**
     * @author Bawnorton
     * @reason GeckoLib + PhysMod Compat - Mixin 0.8.5 doesn't support interface injectors
     */
    @Overwrite(remap = false)
    default void renderCubesOfBone(PoseStack poseStack, GeoBone bone, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (!bone.isHidden()) {
            PhysicsModCompat.CURRENT_BONE_CAPTURE.set(bone);
            for(GeoCube cube : bone.getCubes()) {
                poseStack.pushPose();
                this.renderCube(poseStack, cube, buffer, packedLight, packedOverlay, red, green, blue, alpha);
                poseStack.popPose();
            }
            PhysicsModCompat.CURRENT_BONE_CAPTURE.remove();
        }
    }

    /**
     * @author Bawnorton
     * @reason GeckoLib + PhysMod Compat - Mixin 0.8.5 doesn't support interface injectors
     */
    @Overwrite(remap = false)
    default void renderCube(PoseStack poseStack, GeoCube cube, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        RenderUtils.translateToPivotPoint(poseStack, cube);
        RenderUtils.rotateMatrixAroundCube(poseStack, cube);
        RenderUtils.translateAwayFromPivotPoint(poseStack, cube);

        if (PhysicsMod.getCurrentInstance() != null && PhysicsMod.getCurrentInstance().blockify) {
            DeceasedCraftFixesClient.getCompat().getPhysicsModCompat().createParticlesFromCuboids(
                    poseStack,
                    PhysicsModCompat.CURRENT_BONE_CAPTURE.get(),
                    cube,
                    PhysicsMod.getCurrentInstance().cubifyEntity,
                    PhysicsMod.getCurrentInstance().blockifyFeature,
                    red,
                    green, blue);
        }

        Matrix3f normalisedPoseState = poseStack.last().normal();
        Matrix4f poseState = poseStack.last().pose();

        for(GeoQuad quad : cube.quads()) {
            if (quad != null) {
                Vector3f normal = normalisedPoseState.transform(new Vector3f(quad.normal()));
                RenderUtils.fixInvertedFlatCube(cube, normal);
                createVerticesOfQuad(quad, poseState, normal, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            }
        }
    }
}
