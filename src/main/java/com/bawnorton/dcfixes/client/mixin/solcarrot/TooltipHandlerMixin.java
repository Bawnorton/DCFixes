package com.bawnorton.dcfixes.client.mixin.solcarrot;

import com.bawnorton.dcfixes.extend.FoodListExtender;
import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.cazsius.solcarrot.client.TooltipHandler;
import com.cazsius.solcarrot.tracking.FoodList;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings("ConstantValue")
@IfModLoaded("solcarrot")
@MixinEnvironment("client")
@Mixin(value = TooltipHandler.class, remap = false)
abstract class TooltipHandlerMixin {
    @Expression("?('disabled.eaten', ?)")
    @WrapOperation(
            method = "onItemTooltip",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private static MutableComponent disabledConsiderForgotten(String path, ChatFormatting color, Operation<MutableComponent> original, @Local(name = "food") Item food, @Local(name = "foodList") FoodList foodList) {
        if((Object) foodList instanceof FoodListExtender extender) {
            if(extender.dcfixes$hasForgotten(food)) {
                return original.call("disabled.forgotten", ChatFormatting.RED);
            }
        }
        return original.call(path, color);
    }

    @Expression("?('hearty.eaten', ?)")
    @WrapOperation(
            method = "onItemTooltip",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private static MutableComponent heartyConsiderForgotten(String path, ChatFormatting color, Operation<MutableComponent> original, @Local(name = "food") Item food, @Local(name = "foodList") FoodList foodList) {
        if((Object) foodList instanceof FoodListExtender extender) {
            if(extender.dcfixes$hasForgotten(food)) {
                return original.call("hearty.forgotten", ChatFormatting.GOLD);
            }
        }
        return original.call(path, color);
    }

    @Expression("?('cheap.eaten', ?)")
    @WrapOperation(
            method = "onItemTooltip",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private static MutableComponent cheapConsiderForgotten(String path, ChatFormatting color, Operation<MutableComponent> original, @Local(name = "food") Item food, @Local(name = "foodList") FoodList foodList) {
        if((Object) foodList instanceof FoodListExtender extender) {
            if(extender.dcfixes$hasForgotten(food)) {
                return original.call("cheap.forgotten", ChatFormatting.GRAY);
            }
        }
        return original.call(path, color);
    }
}
