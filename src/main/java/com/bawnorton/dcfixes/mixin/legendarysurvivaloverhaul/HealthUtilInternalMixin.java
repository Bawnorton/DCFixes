package com.bawnorton.dcfixes.mixin.legendarysurvivaloverhaul;

import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import sfiomn.legendarysurvivaloverhaul.util.internal.HealthUtilInternal;

@IfModLoaded("legendarysurvivaloverhaul")
@Mixin(value = HealthUtilInternal.class, remap = false)
abstract class HealthUtilInternalMixin {
    @ModifyArg(
            method = "loseHearth",
            at = @At(
                    value = "INVOKE",
                    target = "Lsfiomn/legendarysurvivaloverhaul/common/capabilities/health/HealthCapability;setAdditionalHealth(F)V"
            )
    )
    private float preventNegativeHealth(float original) {
        return Math.max(0, original);
    }
}
