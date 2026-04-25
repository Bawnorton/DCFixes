package com.bawnorton.dcfixes.client.mixin.flerovium;

import com.bawnorton.dcfixes.client.DeceasedCraftFixesClient;
import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.moepus.flerovium.functions.FastEntityRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import me.jellysquid.mods.sodium.client.render.vertex.VertexConsumerUtils;
import net.caffeinemc.mods.sodium.api.util.ColorABGR;
import net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter;
import net.diebuddies.physics.verlet.ModelPartParent;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;

@IfModLoaded("flerovium")
@MixinEnvironment("client")
@Mixin(ModelPart.class)
abstract class ModelPartMixin {
    @SuppressWarnings("Convert2MethodRef")
    @WrapMethod(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V"
    )
    private void renderWithFastEntityRendererWhenSafe(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, Operation<Void> original) {
        if (DeceasedCraftFixesClient.getCompat().getPhysicsModCompat().map(compat -> compat.isBlockifyingEntity()).orElse(false)) {
            DeceasedCraftFixesClient.getCompat()
                    .getPhysicsModCompat()
                    .orElseThrow()
                    .setCurrentBoneId(((ModelPartParent) this).physicsmod$getName());
            original.call(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
            DeceasedCraftFixesClient.getCompat()
                    .getPhysicsModCompat()
                    .orElseThrow()
                    .removeCurrentBoneId();
            return;
        }

        if ((Object) this.getClass() != ModelPart.class) {
            original.call(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
            return;
        }

        VertexBufferWriter writer = VertexConsumerUtils.convertOrLog(vertexConsumer);

        if (writer == null) {
            original.call(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
            return;
        }

        FastEntityRenderer.render(poseStack, writer, (ModelPart) (Object) this, packedLight, packedOverlay, ColorABGR.pack(red, green, blue, alpha));
    }
}
