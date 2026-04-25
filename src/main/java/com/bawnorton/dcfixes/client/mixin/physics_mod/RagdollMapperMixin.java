package com.bawnorton.dcfixes.client.mixin.physics_mod;

import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.diebuddies.physics.ragdoll.Ragdoll;
import net.diebuddies.physics.ragdoll.RagdollHook;
import net.diebuddies.physics.ragdoll.RagdollMapper;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@IfModLoaded("physicsmod")
@MixinEnvironment("client")
@Mixin(value = RagdollMapper.class, remap = false)
abstract class RagdollMapperMixin {
    @Definition(id = "vanillaHook", field = "Lnet/diebuddies/physics/ragdoll/RagdollMapper;vanillaHook:Lnet/diebuddies/physics/ragdoll/RagdollHook;")
    @Definition(id = "map", method = "Lnet/diebuddies/physics/ragdoll/RagdollHook;map(Lnet/diebuddies/physics/ragdoll/Ragdoll;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/model/EntityModel;)V")
    @Expression("vanillaHook.map(?, ?, ?)")
    @WrapOperation(
            method = "map",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private static void dontMapIfNoBodies(RagdollHook instance, Ragdoll ragdoll, Entity entity, EntityModel entityModel, Operation<Void> original) {
        if (!ragdoll.bodies.isEmpty()) {
            original.call(instance, ragdoll, entity, entityModel);
        }
    }
}
