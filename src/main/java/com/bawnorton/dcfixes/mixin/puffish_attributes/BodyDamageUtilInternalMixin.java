package com.bawnorton.dcfixes.mixin.puffish_attributes;

import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.player.Player;
import net.puffish.attributesmod.api.DynamicModification;
import net.puffish.attributesmod.api.PuffishAttributes;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import sfiomn.legendarysurvivaloverhaul.util.internal.BodyDamageUtilInternal;

@IfModLoaded({"legendarysurvivaloverhaul", "puffish_attributes"})
@Mixin(value = BodyDamageUtilInternal.class, remap = false)
abstract class BodyDamageUtilInternalMixin {
    @ModifyExpressionValue(
            method = "applyConsumableHealing",
            at = @At(
                    value = "FIELD",
                    target = "Lsfiomn/legendarysurvivaloverhaul/api/data/json/JsonHealingConsumable;healingValue:F",
                    opcode = Opcodes.GETFIELD
            )
    )
    private static float applyHealingAttribute(float original, Player player) {
        return DynamicModification.create()
                .withPositive(PuffishAttributes.HEALING, player)
                .applyTo(original);
    }
}
