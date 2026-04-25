package com.bawnorton.dcfixes.mixin.immersive_vehicles;

import com.bawnorton.dcfixes.DeceasedCraftFixes;
import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import minecrafttransportsimulator.baseclasses.EntityManager;
import minecrafttransportsimulator.entities.components.AEntityA_Base;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@IfModLoaded("mts")
@Mixin(value = EntityManager.class, remap = false)
abstract class EntityManagerMixin {
    @ModifyArg(
            method = "tickAll",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/concurrent/ConcurrentLinkedQueue;forEach(Ljava/util/function/Consumer;)V"
            )
    )
    private Consumer<AEntityA_Base> filterIfUnloaded(Consumer<AEntityA_Base> action) {
        return DeceasedCraftFixes.getCompat()
                .getImmersiveVehiclesCompat()
                .orElseThrow()
                .filterTickingEntities(action);
    }

    @Inject(
            method = "tickAll",
            at = @At("HEAD")
    )
    private void onTickStart(boolean beforePlayer, CallbackInfo ci) {
        if (beforePlayer) {
            DeceasedCraftFixes.getCompat()
                    .getImmersiveVehiclesCompat()
                    .orElseThrow()
                    .onTickStart();
        }
    }
}