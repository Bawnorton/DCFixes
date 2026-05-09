package com.bawnorton.dcfixes.mixin.legendarysurvivaloverhaul;

import com.bawnorton.dcfixes.DeceasedCraftFixes;
import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage.BodyDamageCapability;

import java.util.function.BiConsumer;

@IfModLoaded("legendarysurvivaloverhaul")
@Mixin(value = BodyDamageCapability.class, remap = false)
abstract class BodyDamageCapabilityMixin {
    @WrapMethod(
            method = {"applyCustomHealthRegeneration", "applyProportionalLimbRegeneration"}
    )
    private void usePerLimbDirectHealing(Player player, Operation<Void> original) {
    }

    @WrapOperation(
            method = "tickUpdate",
            at = @At(
                    value = "INVOKE",
                    target = "Lsfiomn/legendarysurvivaloverhaul/common/capabilities/bodydamage/BodyDamageCapability;healWithFoodExhaustion(Lnet/minecraft/world/entity/player/Player;Lsfiomn/legendarysurvivaloverhaul/api/bodydamage/BodyPartEnum;F)V"
            )
    )
    private void thenHealPlayerDirectly(BodyDamageCapability instance, Player player, BodyPartEnum part, float healingValue, Operation<Void> original) {
        original.call(instance, player, part, healingValue);
        DeceasedCraftFixes.getCompat().getLSOCompat().orElseThrow().healPlayerDirectly(player, healingValue);
    }

    @Definition(id = "healingFunction", local = @Local(type = BiConsumer.class, name = "healingFunction"))
    @Expression("healingFunction = @(?)")
    @ModifyExpressionValue(
            method = "healMostDamaged",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private BiConsumer<BodyPartEnum, Float> thenHealPlayerDirectly(BiConsumer<BodyPartEnum, Float> original, Player player) {
        return (part, amount) -> {
            original.accept(part, amount);
            DeceasedCraftFixes.getCompat().getLSOCompat().orElseThrow().healPlayerDirectly(player, amount);
        };
    }

    @WrapOperation(
            method = "tickUpdate",
            at = @At(
                    value = "INVOKE",
                    target = "Lsfiomn/legendarysurvivaloverhaul/common/capabilities/bodydamage/BodyDamageCapability;updateBodyPartDynamicMaxHealth(F)V"
            )
    )
    private void syncPlayerHealth(BodyDamageCapability instance, float maxHealth, Operation<Void> original, Player player) {
        original.call(instance, maxHealth);
        DeceasedCraftFixes.getCompat().getLSOCompat().orElseThrow().syncHealth(player);
    }
}
