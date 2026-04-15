package com.bawnorton.dcfixes.client.mixin.immersive_vehicles;

import com.bawnorton.dcfixes.client.extend.InterfaceRenderExtender;
import com.google.common.collect.Iterators;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import mcinterface1201.InterfaceRender;
import minecrafttransportsimulator.entities.components.AEntityC_Renderable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Iterator;

@MixinEnvironment("client")
@Mixin(value = InterfaceRender.class, remap = false)
abstract class InterfaceRenderMixin implements InterfaceRenderExtender {
    @ModifyExpressionValue(
            method = "doRenderCall",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/concurrent/ConcurrentLinkedQueue;iterator()Ljava/util/Iterator;"
            )
    )
    private static <E> Iterator<E> applyFrustumCulling(Iterator<E> original) {
        return Iterators.filter(original, element -> InterfaceRenderExtender.shouldRenderInFrustum((AEntityC_Renderable) element));
    }
}
