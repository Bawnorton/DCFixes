package com.bawnorton.dcfixes.client.mixin.legendarysurvivaloverhaul;

import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.puffish.attributesmod.api.DynamicModification;
import net.puffish.attributesmod.api.PuffishAttributes;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import sfiomn.legendarysurvivaloverhaul.client.tooltips.TooltipHandler;

@IfModLoaded({"legendarysurvivaloverhaul", "puffish_attributes"})
@MixinEnvironment("client")
@Mixin(value = TooltipHandler.class, remap = false)
abstract class TooltipHandlerMixin {
    @ModifyExpressionValue(
            method = "addHealingText",
            at = @At(
                    value = "FIELD",
                    target = "Lsfiomn/legendarysurvivaloverhaul/api/data/json/JsonHealingConsumable;healingValue:F",
                    opcode = Opcodes.GETFIELD
            )
    )
    private static float applyHealingAttribute(float original) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null) return original;

        return DynamicModification.create()
                .withPositive(PuffishAttributes.HEALING, player)
                .applyTo(original);
    }
}
