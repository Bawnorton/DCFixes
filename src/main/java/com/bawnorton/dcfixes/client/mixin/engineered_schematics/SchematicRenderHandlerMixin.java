package com.bawnorton.dcfixes.client.mixin.engineered_schematics;

import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import tech.muddykat.engineered_schematics.event.SchematicRenderHandler;

@IfModLoaded("engineered_schematics")
@MixinEnvironment("client")
@Mixin(value = SchematicRenderHandler.class, remap = false)
abstract class SchematicRenderHandlerMixin {
    @Definition(id = "secondItem", local = @Local(type = ItemStack.class, name = "secondItem"))
    @Definition(id = "getTag", method = "Lnet/minecraft/world/item/ItemStack;getTag()Lnet/minecraft/nbt/CompoundTag;", remap = true)
    @Expression("secondItem.getTag() != null")
    @ModifyExpressionValue(
            method = "renderMultiblockSchematic",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private static boolean useTheCorrectMethod0(boolean original, @Local(name = "secondItem") ItemStack secondItem) {
        return secondItem.hasTag();
    }

    @Definition(id = "mainItem", local = @Local(type = ItemStack.class, name = "mainItem"))
    @Definition(id = "getTag", method = "Lnet/minecraft/world/item/ItemStack;getTag()Lnet/minecraft/nbt/CompoundTag;", remap = true)
    @Expression("mainItem.getTag() != null")
    @ModifyExpressionValue(
            method = "renderMultiblockSchematic",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private static boolean useTheCorrectMethod1(boolean original, @Local(name = "mainItem") ItemStack mainItem) {
        return mainItem.hasTag();
    }
}
