package com.bawnorton.dcfixes.client.mixin.minecraft;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.util.ClassInstanceMultiMap;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Collection;
import java.util.Collections;

@MixinEnvironment("client")
@Mixin(ClassInstanceMultiMap.class)
abstract class ClassInstanceMultiMapMixin {
    @WrapMethod(
            method = "find"
    )
    private <S> Collection<S> findSafely(Class<S> type, Operation<Collection<S>> original) {
        try {
            return original.call(type);
        } catch (IllegalArgumentException e) {
            return Collections.emptyList();
        }
    }
}
