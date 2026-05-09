package com.bawnorton.dcfixes.mixin.legendarysurvivaloverhaul;

import com.bawnorton.dcfixes.DeceasedCraftFixes;
import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.puffish.attributesmod.api.DynamicModification;
import net.puffish.attributesmod.api.PuffishAttributes;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.IBodyDamageCapability;
import sfiomn.legendarysurvivaloverhaul.util.internal.BodyDamageUtilInternal;

import java.util.List;

@IfModLoaded("legendarysurvivaloverhaul")
@Mixin(value = BodyDamageUtilInternal.class, remap = false)
abstract class BodyDamageUtilInternalMixin {
    @Shadow
    @Final
    private static List<MobEffect> passiveLimbRegenerationEffects;

    @WrapOperation(
            method = "healBodyPart",
            at = @At(
                    value = "INVOKE",
                    target = "Lsfiomn/legendarysurvivaloverhaul/api/bodydamage/IBodyDamageCapability;heal(Lsfiomn/legendarysurvivaloverhaul/api/bodydamage/BodyPartEnum;F)V"
            )
    )
    private void thenHealPlayerDirectly(IBodyDamageCapability instance, BodyPartEnum bodyPartEnum, float healingValue, Operation<Void> original, Player player) {
        original.call(instance, bodyPartEnum, healingValue);
        DeceasedCraftFixes.getCompat().getLSOCompat().orElseThrow().healPlayerDirectly(player, healingValue);
    }

    @WrapMethod(
            method = "hurtBodyPart"
    )
    private void distributePainBetter(Player player, BodyPartEnum bodyPartEnum, float damageValue, Operation<Void> original) {
        DeceasedCraftFixes.getCompat().getLSOCompat().orElseThrow().hurtBodyPartAndDistribute(player, bodyPartEnum, damageValue);
    }

    @WrapMethod(
            method = "getPlayerPassiveLimbRegenerationEffect"
    )
    private MobEffectInstance thereAreNoPassiveLimbRegenEffects(Player player, Operation<MobEffectInstance> original) {
        return null;
    }
}
