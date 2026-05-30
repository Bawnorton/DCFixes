package com.bawnorton.dcfixes.mixin.minecraft;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.WrittenBookItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WrittenBookItem.class)
abstract class WrittenBookItemMixin {
    @ModifyExpressionValue(
            method = "makeSureTagIsValid",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=32"
            )
    )
    private static int increaseMaxTitleLength(int original) {
        return 256;
    }

    @WrapOperation(
            method = "getName",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/chat/Component;literal(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;"
            )
    )
    private static MutableComponent useTranslatable(String text, Operation<MutableComponent> original) {
        return Component.translatable(text);
    }
}
