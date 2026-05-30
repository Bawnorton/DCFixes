package com.bawnorton.dcfixes.mixin.refurbished_furniture;

import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mrcrayfish.furniture.refurbished.blockentity.StoveBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@IfModLoaded("refurbished_furniture")
@Mixin(targets = "com.mrcrayfish.furniture.refurbished.blockentity.StoveBlockEntity$CookingSpace")
abstract class CookingSpaceMixin {
    @WrapWithCondition(
            method = "setProcessingTime",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mrcrayfish/furniture/refurbished/blockentity/StoveBlockEntity;setChanged()V"
            )
    )
    private boolean dontUpdateStateEagerly(StoveBlockEntity instance) {
        return false;
    }
}