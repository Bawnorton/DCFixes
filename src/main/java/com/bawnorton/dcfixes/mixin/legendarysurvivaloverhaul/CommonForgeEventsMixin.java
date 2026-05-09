package com.bawnorton.dcfixes.mixin.legendarysurvivaloverhaul;

import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import org.spongepowered.asm.mixin.Mixin;
import sfiomn.legendarysurvivaloverhaul.common.events.CommonForgeEvents;

@IfModLoaded("legendarysurvivaloverhaul")
@Mixin(value = CommonForgeEvents.class, remap = false)
abstract class CommonForgeEventsMixin {
    @WrapMethod(
            method = "onEntityHurtDamage"
    )
    private static void useSmarterCalculation(LivingDamageEvent event, Operation<Void> original) {
    }
}
