package com.bawnorton.dcfixes.client.mixin.physics_mod;

import com.bawnorton.dcfixes.client.extend.PhysicsEntityExtender;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.diebuddies.physics.IRigidBody;
import net.diebuddies.physics.PhysicsEntity;
import net.diebuddies.physics.PhysicsWorld;
import net.diebuddies.render.MainRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@MixinEnvironment("client")
@Mixin(MainRenderer.class)
abstract class MainRendererMixin {
    @Shadow(remap = false)
    private Matrix4f transformation;

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;getShader()Lnet/minecraft/client/renderer/ShaderInstance;"
            )
    )
    private void renderRagdollIndices(PhysicsWorld physics, ClientLevel level, PoseStack matrixStackIn, Vec3 view, double offsetX, double offsetY, double offsetZ, IRigidBody body, PhysicsEntity particle, boolean renderTransparency, CallbackInfo ci) {
        int i = ((PhysicsEntityExtender) particle).dcfixes$ragdollIndex();
        dcfixes$renderIndicesOnFaces(matrixStackIn, particle.pivot, i);
    }

    @Unique
    private void dcfixes$renderIndicesOnFaces(PoseStack poseStack, Vector3f pivot, int num) {
        if (num == -1) return;

        String text = String.valueOf(num);
        poseStack.pushPose();
        poseStack.last().pose().mul(transformation);

        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;
        MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();

        float textWidth = (float) (-font.width(text) / 2);
        float textHeight = -4f;
        float offset = 0.2f;
        float textScale = 0.015f;

        RenderSystem.disableDepthTest();
        dcfixes$drawIndexFace(poseStack, buffer, font, text, textWidth, textHeight, 0, 0, offset, 0, 0, 0, textScale, 0xFF00FF00);
        dcfixes$drawIndexFace(poseStack, buffer, font, text, textWidth, textHeight, 0, 0, -offset, 0, 180, 0, textScale, 0xFF00FF00);
        dcfixes$drawIndexFace(poseStack, buffer, font, text, textWidth, textHeight, offset, 0, 0, 0, 90, 0, textScale, 0xFF00FF00);
        dcfixes$drawIndexFace(poseStack, buffer, font, text, textWidth, textHeight, -offset, 0, 0, 0, -90, 0, textScale, 0xFF00FF00);
        dcfixes$drawIndexFace(poseStack, buffer, font, text, textWidth, textHeight, 0, offset, 0, -90, 0, 0, textScale, 0xFF00FF00);
        dcfixes$drawIndexFace(poseStack, buffer, font, text, textWidth, textHeight, 0, -offset, 0, 90, 0, 0, textScale, 0xFF00FF00);

//        buffer.endBatch();
        RenderSystem.enableDepthTest();
        poseStack.popPose();
    }

    @Unique
    private void dcfixes$drawIndexFace(PoseStack stack, MultiBufferSource.BufferSource buffer, Font font, String text, float tx, float ty, float ox, float oy, float oz, float rx, float ry, float rz, float scale, int colour) {
        stack.pushPose();
        stack.translate(ox, oy, oz);
        if (rx != 0) stack.mulPose(new Quaternionf().rotationX(rx * (float) (Math.PI / 180.0)));
        if (ry != 0) stack.mulPose(new Quaternionf().rotationY(ry * (float) (Math.PI / 180.0)));
        if (rz != 0) stack.mulPose(new Quaternionf().rotationZ(rz * (float) (Math.PI / 180.0)));

        stack.scale(scale, -scale, scale);

        font.drawInBatch(
                text,
                tx,
                ty,
                colour,
                false,
                stack.last().pose(),
                buffer,
                Font.DisplayMode.SEE_THROUGH,
                0,
                15728880
        );
        stack.popPose();
    }
}
