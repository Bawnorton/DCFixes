package com.bawnorton.dcfixes.mixin.tacz;

import com.bawnorton.dcfixes.config.DCFixesConfig;
import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.tacz.guns.item.AmmoBoxItem;
import com.tacz.guns.resource.index.CommonAmmoIndex;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@IfModLoaded("tacz")
@Mixin(value = AmmoBoxItem.class, remap = false)
abstract class AmmoBoxItemMixin {
    @Definition(id = "maxSize", local = @Local(type = int.class, name = "maxSize"))
    @Expression("maxSize = @(?)")
    @ModifyExpressionValue(
            method = "lambda$overrideStackedOnOther$1",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private static int overrideMaxSizeValue(int original, @Local(name = "index") CommonAmmoIndex index, @Local(name = "boxLevelMultiplier") int boxLevelMultiplier) {
        return dcfixes$getOverridenStackSize(original, index, boxLevelMultiplier);
    }

    @Definition(id = "totalCount", local = @Local(type = double.class, name = "totalCount"))
    @Expression("totalCount = (double) @(?)")
    @ModifyExpressionValue(
            method = "lambda$getBarWidth$2",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private static int overrideMaxSizeDisplay(int original, @Local(name = "index") CommonAmmoIndex index, @Local(name = "boxLevelMultiplier") int boxLevelMultiplier) {
        return dcfixes$getOverridenStackSize(original, index, boxLevelMultiplier);
    }

    @Unique
    private static int dcfixes$getOverridenStackSize(int original, CommonAmmoIndex index, int boxLevelMultiplier) {
        return switch(boxLevelMultiplier) {
            case 2 -> DCFixesConfig.get().goldAmmoBoxStackCount == -1 ? original : DCFixesConfig.get().goldAmmoBoxStackCount * index.getStackSize();
            case 3 -> DCFixesConfig.get().diamondAmmoBoxStackCount == -1 ? original : DCFixesConfig.get().diamondAmmoBoxStackCount * index.getStackSize();
            default -> DCFixesConfig.get().ironAmmoBoxStackCount == -1 ? original : DCFixesConfig.get().ironAmmoBoxStackCount * index.getStackSize();
        };
    }
}
