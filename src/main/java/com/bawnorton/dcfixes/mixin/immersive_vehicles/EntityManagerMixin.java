package com.bawnorton.dcfixes.mixin.immersive_vehicles;

import com.bawnorton.dcfixes.DeceasedCraftFixes;
import minecrafttransportsimulator.baseclasses.EntityManager;
import minecrafttransportsimulator.entities.components.AEntityA_Base;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(EntityManager.class)
abstract class EntityManagerMixin {
    @ModifyArg(
            method = "tickAll",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/concurrent/ConcurrentLinkedQueue;forEach(Ljava/util/function/Consumer;)V"
            ),
            remap = false
    )
    private Consumer<AEntityA_Base> filterIfUnloaded(Consumer<AEntityA_Base> action) {
        return DeceasedCraftFixes.getCompat().getImmersiveVehiclesCompat().filterTickingEntities(action);
    }

    @Inject(
            method = "tickAll",
            at = @At("HEAD"),
            remap = false
    )
    private void onTickStart(boolean beforePlayer, CallbackInfo ci) {
        if (beforePlayer) {
            DeceasedCraftFixes.getCompat().getImmersiveVehiclesCompat().onTickStart();
        }
    }
}