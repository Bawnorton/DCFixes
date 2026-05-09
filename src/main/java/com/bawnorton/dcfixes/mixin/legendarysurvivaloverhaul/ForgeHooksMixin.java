package com.bawnorton.dcfixes.mixin.legendarysurvivaloverhaul;

import com.bawnorton.dcfixes.DeceasedCraftFixes;
import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@IfModLoaded("legendarysurvivaloverhaul")
@Mixin(value = ForgeHooks.class, remap = false)
abstract class ForgeHooksMixin {
    @ModifyReturnValue(
            method = "onLivingDamage",
            at = @At("RETURN")
    )
    private static float applyImprovedLSODamage(float original, LivingEntity entity, DamageSource src) {
        if(original == 0) return original;

        return DeceasedCraftFixes.getCompat()
                .getLSOCompat()
                .orElseThrow()
                .simulateLimbDamage(entity, src, original);
    }
}
