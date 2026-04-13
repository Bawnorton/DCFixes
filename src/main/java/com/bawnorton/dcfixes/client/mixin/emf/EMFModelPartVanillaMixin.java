/*
package com.bawnorton.dcfixes.client.mixin.emf;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.diebuddies.physics.verlet.ModelPartParent;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import traben.entity_model_features.models.parts.EMFModelPartVanilla;

import java.util.Collection;
import java.util.Map;

@MixinEnvironment("client")
@Mixin(EMFModelPartVanilla.class)
abstract class EMFModelPartVanillaMixin {
    @Inject(
        method = "<init>",
        at = @At("TAIL"),
        remap = false
    )
    private void physicsmod$setParentInConstructor(String name, ModelPart vanillaPart, Collection<String> optifinePartNames, Map allVanillaParts, CallbackInfo info) {
        if (vanillaPart != null) {
            ((ModelPartParent)this).physicsmod$setParent(vanillaPart);
        }

        ((ModelPartParent)this).physicsmod$setName(name);
    }
}
*/
