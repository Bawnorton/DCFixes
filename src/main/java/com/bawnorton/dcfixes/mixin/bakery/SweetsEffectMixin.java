package com.bawnorton.dcfixes.mixin.bakery;

import com.bawnorton.dcfixes.config.DCFixesConfig;
import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.satisfy.bakery.effect.SweetsEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@IfModLoaded("bakery")
@Mixin(SweetsEffect.class)
abstract class SweetsEffectMixin {
    @Definition(id = "applyModifier", method = "Lnet/satisfy/bakery/effect/SweetsEffect;applyModifier(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/ai/attributes/Attribute;Ljava/util/UUID;D)V", remap = false)
    @Definition(id = "MOVEMENT_SPEED", field = "Lnet/minecraft/world/entity/ai/attributes/Attributes;MOVEMENT_SPEED:Lnet/minecraft/world/entity/ai/attributes/Attribute;")
    @Expression("?.applyModifier(?, MOVEMENT_SPEED, ?, @(?))")
    @ModifyExpressionValue(
            method = "applyEffectTick",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private double modifyMovementSpeedBonus(double original, @Local(name = "increase") double increase) {
        return DCFixesConfig.get().sugarRushMovementSpeedBonus * increase;
    }

    @Definition(id = "applyModifier", method = "Lnet/satisfy/bakery/effect/SweetsEffect;applyModifier(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/ai/attributes/Attribute;Ljava/util/UUID;D)V", remap = false)
    @Definition(id = "ATTACK_SPEED", field = "Lnet/minecraft/world/entity/ai/attributes/Attributes;ATTACK_SPEED:Lnet/minecraft/world/entity/ai/attributes/Attribute;")
    @Expression("?.applyModifier(?, ATTACK_SPEED, ?, @(?))")
    @ModifyExpressionValue(
            method = "applyEffectTick",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private double modifyAttackSpeedBonus(double original, @Local(name = "increase") double increase) {
        return DCFixesConfig.get().sugarRushAttackSpeedBons * increase;
    }

    @Definition(id = "applyModifier", method = "Lnet/satisfy/bakery/effect/SweetsEffect;applyModifier(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/ai/attributes/Attribute;Ljava/util/UUID;D)V", remap = false)
    @Definition(id = "ATTACK_DAMAGE", field = "Lnet/minecraft/world/entity/ai/attributes/Attributes;ATTACK_DAMAGE:Lnet/minecraft/world/entity/ai/attributes/Attribute;")
    @Expression("?.applyModifier(?, ATTACK_DAMAGE, ?, @(?))")
    @ModifyExpressionValue(
            method = "applyEffectTick",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private double modifyAttackDamageBonus(double original, @Local(name = "increase") double increase) {
        return DCFixesConfig.get().sugarRushAttackDamageBonus * increase;
    }
}
