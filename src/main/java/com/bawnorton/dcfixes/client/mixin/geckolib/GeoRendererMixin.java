package com.bawnorton.dcfixes.client.mixin.geckolib;

import com.bawnorton.dcfixes.client.DeceasedCraftFixesClient;
import com.bawnorton.dcfixes.client.compat.physics_mod.PhysicsModCompat;
import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.diebuddies.physics.PhysicsMod;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.GeoCube;
import software.bernie.geckolib.cache.object.GeoQuad;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;

@IfModLoaded({"geckolib", "physicsmod"})
@MixinEnvironment("client")
@Mixin(value = GeoRenderer.class, priority = 1500, remap = false)
public interface GeoRendererMixin {
    @SuppressWarnings("unchecked")
    @Definition(id = "buffer", local = @Local(type = VertexConsumer.class, argsOnly = true))
    @Expression("buffer == null")
    @ModifyVariable(
            method = "defaultRender",
            at = @At("MIXINEXTRAS:EXPRESSION"),
            argsOnly = true
    )
    private <E extends GeoAnimatable, R extends GeoRenderer<E>> RenderType replaceRenderType(RenderType renderType, @Local(argsOnly = true) E animatable) {
        return DeceasedCraftFixesClient.getCompat()
                .getGeckoLibCompat()
                .orElseThrow()
                .replaceRenderType(animatable, (R) this, renderType);
    }

    @WrapMethod(
            method = "renderCubesOfBone"
    )
    default void attachBone(PoseStack poseStack, GeoBone bone, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, Operation<Void> original) {
        if (!bone.isHidden()) {
            PhysicsModCompat compat = DeceasedCraftFixesClient.getCompat()
                    .getPhysicsModCompat()
                    .orElseThrow();
            compat.setGeoBoneCapture(bone);
            compat.setCurrentBoneId(bone.getName());
            original.call(poseStack, bone, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            compat.removeGeoBoneCapture();
            compat.removeCurrentBoneId();
        }
    }

    @WrapOperation(
            method = "renderCube",
            at = @At(
                    value = "INVOKE",
                    target = "Lsoftware/bernie/geckolib/cache/object/GeoCube;quads()[Lsoftware/bernie/geckolib/cache/object/GeoQuad;"
            )
    )
    private GeoQuad[] createPhysicsModParticles(GeoCube instance, Operation<GeoQuad[]> original, PoseStack poseStack, GeoCube cube, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        GeoQuad[] quads = original.call(instance);
        PhysicsModCompat compat = DeceasedCraftFixesClient.getCompat()
                .getPhysicsModCompat()
                .orElseThrow();
        if (compat.isBlockifyingEntity()) {
            compat.createParticlesFromCuboids(
                    poseStack,
                    cube,
                    PhysicsMod.getCurrentInstance().cubifyEntity,
                    PhysicsMod.getCurrentInstance().blockifyFeature,
                    red,
                    green,
                    blue
            );
        }
        return quads;
    }
}
