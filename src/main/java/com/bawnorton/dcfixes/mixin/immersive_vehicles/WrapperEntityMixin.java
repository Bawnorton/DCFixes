package com.bawnorton.dcfixes.mixin.immersive_vehicles;

import com.bawnorton.dcfixes.client.extend.EntityExtender;
import com.bawnorton.dcfixes.mixin.accessor.WrapperEntityAccessor;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mcinterface1201.WrapperEntity;
import mcinterface1201.WrapperPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = WrapperEntity.class, remap = false)
abstract class WrapperEntityMixin {
    @WrapMethod(
            method = "getWrapperFor"
    )
    private static WrapperEntity wtfAreWeStoringEverythingInANonLazyParallelMapForDoYouWantAMemoryLeakQuestionMark(Entity entity, Operation<WrapperEntity> original) {
        if (entity instanceof Player player) {
            return WrapperPlayer.getWrapperFor(player);
        }

        EntityExtender extender = (EntityExtender) entity;
        WrapperEntity existingWrapper = (WrapperEntity) extender.dcfixes$getWrapper();
        if (existingWrapper == null || !existingWrapper.isValid() || entity != ((WrapperEntityAccessor) existingWrapper).dcfixes$entity()) {
            WrapperEntity newWrapper = WrapperEntityAccessor.dcfixes$create(entity);
            extender.dcfixes$setWrapper(newWrapper);
            return newWrapper;
        } else {
            return existingWrapper;
        }
    }
}
