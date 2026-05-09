package com.bawnorton.dcfixes.mixin.legendarysurvivaloverhaul;

import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sfiomn.legendarysurvivaloverhaul.config.Config;

@IfModLoaded("legendarysurvivaloverhaul")
@Mixin(value = Config.Baked.class, remap = false)
abstract class ConfigMixin {
    @Shadow
    public static double headPartHealth;

    @Shadow
    public static double chestPartHealth;

    @Shadow
    public static double armsPartHealth;

    @Shadow
    public static double feetPartHealth;

    @Shadow
    public static double legsPartHealth;

    @Inject(
            method = "bakeCommon",
            at = @At("TAIL")
    )
    private static void normaliseLimbPartHealth(CallbackInfo ci) {
        double total = headPartHealth + chestPartHealth + 2 * (armsPartHealth + legsPartHealth + feetPartHealth);
        headPartHealth = headPartHealth / total;
        chestPartHealth = chestPartHealth / total;
        armsPartHealth = armsPartHealth / total;
        legsPartHealth = legsPartHealth / total;
        feetPartHealth = feetPartHealth / total;
    }
}
