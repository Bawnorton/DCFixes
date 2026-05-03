package com.bawnorton.dcfixes.mixin.minecraft;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PotionBrewing.class)
abstract class PotionBrewingMixin {
    @Definition(id = "addMix", method = "Lnet/minecraft/world/item/alchemy/PotionBrewing;addMix(Lnet/minecraft/world/item/alchemy/Potion;Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/alchemy/Potion;)V")
    @Definition(id = "INVISIBILITY", field = "Lnet/minecraft/world/item/alchemy/Potions;INVISIBILITY:Lnet/minecraft/world/item/alchemy/Potion;")
    @Definition(id = "LONG_INVISIBILITY", field = "Lnet/minecraft/world/item/alchemy/Potions;LONG_INVISIBILITY:Lnet/minecraft/world/item/alchemy/Potion;")
    @Definition(id = "LONG_NIGHT_VISION", field = "Lnet/minecraft/world/item/alchemy/Potions;LONG_NIGHT_VISION:Lnet/minecraft/world/item/alchemy/Potion;")
    @Expression("addMix(?, ?, INVISIBILITY)")
    @Expression("addMix(LONG_NIGHT_VISION, ?, LONG_INVISIBILITY)")
    @WrapOperation(
            method = "bootStrap",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private static void dontRegisterInvis(Potion potionEntry, Item potionIngredient, Potion potionResult, Operation<Void> original) {

    }
}
