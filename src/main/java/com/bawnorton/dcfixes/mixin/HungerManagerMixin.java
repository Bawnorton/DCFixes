package com.bawnorton.dcfixes.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HungerManager.class)
public abstract class HungerManagerMixin {
    @Unique
    private final ThreadLocal<Boolean> dcfixes$didHeal = ThreadLocal.withInitial(() -> false);

    @WrapOperation(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;heal(F)V"))
    private void onHeal(PlayerEntity instance, float exhaustion, Operation<Void> original) {
        float preHealth = instance.getHealth();
        original.call(instance, exhaustion);
        float postHealth = instance.getHealth();
        dcfixes$didHeal.set(preHealth < postHealth);
    }

    @WrapOperation(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/HungerManager;addExhaustion(F)V"))
    private void onAddExhaustion(HungerManager instance, float exhaustion, Operation<Void> original) {
        if (dcfixes$didHeal.get()) {
            original.call(instance, exhaustion);
            dcfixes$didHeal.set(false);
        }
    }
}