package com.bawnorton.dcfixes.mixin.lostcities;

import com.bawnorton.dcfixes.FasterLostCities;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.entity.BlockEntity;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin {
    @WrapOperation(
            method = "m_58881_",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V"
            ),
            remap = false
    )
    private static void dontLogOnGen(Logger instance, String message, Object arg, Operation<Void> original) {
        if(FasterLostCities.CHUNK.get() == null) {
            original.call(instance, message, arg);
        }
    }
}
