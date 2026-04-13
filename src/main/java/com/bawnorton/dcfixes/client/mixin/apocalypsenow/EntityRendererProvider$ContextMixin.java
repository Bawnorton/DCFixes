package com.bawnorton.dcfixes.client.mixin.apocalypsenow;

import com.bawnorton.dcfixes.client.extend.ModelPartExtender;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@MixinEnvironment("client")
@Mixin(EntityRendererProvider.Context.class)
abstract class EntityRendererProvider$ContextMixin {
    @WrapOperation(
            method = "bakeLayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/model/geom/EntityModelSet;bakeLayer(Lnet/minecraft/client/model/geom/ModelLayerLocation;)Lnet/minecraft/client/model/geom/ModelPart;"
            )
    )
    private ModelPart attachLocation(EntityModelSet instance, ModelLayerLocation modelLayerLocation, Operation<ModelPart> original) {
        ModelPart result = original.call(instance, modelLayerLocation);
        ((ModelPartExtender) result).dcfixes$setLocation(modelLayerLocation);
        return result;
    }
}
