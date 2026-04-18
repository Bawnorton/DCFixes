package com.bawnorton.dcfixes.mixin.extreme_reactors;

import com.bawnorton.dcfixes.config.DCFixesConfig;
import it.zerono.mods.extremereactors.gamecontent.multiblock.turbine.variant.TurbineVariant;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(TurbineVariant.class)
abstract class TurbineVariantMixin {
    @ModifyArg(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lit/zerono/mods/extremereactors/gamecontent/multiblock/turbine/variant/TurbineVariant$Builder;setPartEnergyCapacity(I)Lit/zerono/mods/extremereactors/gamecontent/multiblock/turbine/variant/TurbineVariant$Builder;",
                    ordinal = 1
            ),
            remap = false
    )
    private static int configurablePartEnergyCapacity(int original) {
        return DCFixesConfig.get().reinforcedPartEnergyCapacity;
    }

    @ModifyArg(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lit/zerono/mods/extremereactors/gamecontent/multiblock/turbine/variant/TurbineVariant$Builder;setEnergyGenerationEfficiency(F)Lit/zerono/mods/extremereactors/gamecontent/multiblock/turbine/variant/TurbineVariant$Builder;",
                    ordinal = 1
            ),
            remap = false
    )
    private static float configurableEnergyGenerationEfficiency(float original) {
        return DCFixesConfig.get().reinforcedEnergyGenerationEfficiency;
    }

    @ModifyArg(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lit/zerono/mods/extremereactors/gamecontent/multiblock/turbine/variant/TurbineVariant$Builder;setMaxEnergyExtractionRate(D)Lit/zerono/mods/extremereactors/gamecontent/multiblock/turbine/variant/TurbineVariant$Builder;",
                    ordinal = 1
            ),
            remap = false
    )
    private static double configurableMaxEnergyExtractionRate(double original) {
        return DCFixesConfig.get().reinforcedMaxEnergyExtractionRate;
    }

    @ModifyArg(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lit/zerono/mods/extremereactors/gamecontent/multiblock/turbine/variant/TurbineVariant$Builder;setMaxChargerRate(D)Lit/zerono/mods/extremereactors/gamecontent/multiblock/turbine/variant/TurbineVariant$Builder;",
                    ordinal = 1
            ),
            remap = false
    )
    private static double configurableMaxChargerRate(double original) {
        return DCFixesConfig.get().reinforcedMaxChargerRate;
    }

    @ModifyArg(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lit/zerono/mods/extremereactors/gamecontent/multiblock/turbine/variant/TurbineVariant$Builder;setRadiationAttenuation(F)Lit/zerono/mods/extremereactors/gamecontent/multiblock/turbine/variant/TurbineVariant$Builder;",
                    ordinal = 1
            ),
            remap = false
    )
    private static float configurableRadiationAttenuation(float original) {
        return DCFixesConfig.get().reinforcedRadiationAttenuation;
    }

    @ModifyArg(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lit/zerono/mods/extremereactors/gamecontent/multiblock/turbine/variant/TurbineVariant$Builder;setResidualRadiationAttenuation(F)Lit/zerono/mods/extremereactors/gamecontent/multiblock/turbine/variant/TurbineVariant$Builder;",
                    ordinal = 1
            ),
            remap = false
    )
    private static float configurableResidualRadiationAttenuation(float original) {
        return DCFixesConfig.get().reinforcedResidualRadiationAttenuation;
    }

    @ModifyArg(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lit/zerono/mods/extremereactors/gamecontent/multiblock/turbine/variant/TurbineVariant$Builder;setMaxPermittedFlow(I)Lit/zerono/mods/extremereactors/gamecontent/multiblock/turbine/variant/TurbineVariant$Builder;",
                    ordinal = 1
            ),
            remap = false
    )
    private static int configurableMaxPermittedFlow(int original) {
        return DCFixesConfig.get().reinforcedMaxPermittedFlow;
    }

    @ModifyArg(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lit/zerono/mods/extremereactors/gamecontent/multiblock/turbine/variant/TurbineVariant$Builder;setBaseFluidPerBlade(I)Lit/zerono/mods/extremereactors/gamecontent/multiblock/turbine/variant/TurbineVariant$Builder;",
                    ordinal = 1
            ),
            remap = false
    )
    private static int configurableBaseFluidPerBlade(int original) {
        return DCFixesConfig.get().reinforcedBaseFluidPerBlade;
    }

    @ModifyArg(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lit/zerono/mods/extremereactors/gamecontent/multiblock/turbine/variant/TurbineVariant$Builder;setRotorDragCoefficient(F)Lit/zerono/mods/extremereactors/gamecontent/multiblock/turbine/variant/TurbineVariant$Builder;",
                    ordinal = 1
            ),
            remap = false
    )
    private static float configurableRotorDragCoefficient(float original) {
        return DCFixesConfig.get().reinforcedRotorDragCoefficient;
    }

    @ModifyArg(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lit/zerono/mods/extremereactors/gamecontent/multiblock/turbine/variant/TurbineVariant$Builder;setMaxRotorSpeed(F)Lit/zerono/mods/extremereactors/gamecontent/multiblock/turbine/variant/TurbineVariant$Builder;",
                    ordinal = 1
            ),
            remap = false
    )
    private static float configurableMaxRotorSpeed(float original) {
        return DCFixesConfig.get().reinforcedMaxRotorSpeed;
    }

    @ModifyArg(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lit/zerono/mods/extremereactors/gamecontent/multiblock/turbine/variant/TurbineVariant$Builder;setRotorBladeMass(I)Lit/zerono/mods/extremereactors/gamecontent/multiblock/turbine/variant/TurbineVariant$Builder;",
                    ordinal = 1
            ),
            remap = false
    )
    private static int configurableRotorBladeMass(int original) {
        return DCFixesConfig.get().reinforcedRotorBladeMass;
    }

    @ModifyArg(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lit/zerono/mods/extremereactors/gamecontent/multiblock/turbine/variant/TurbineVariant$Builder;setRotorShaftMass(I)Lit/zerono/mods/extremereactors/gamecontent/multiblock/turbine/variant/TurbineVariant$Builder;",
                    ordinal = 1
            ),
            remap = false
    )
    private static int configurableRotorShaftMass(int original) {
        return DCFixesConfig.get().reinforcedRotorShaftMass;
    }

    @ModifyArg(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lit/zerono/mods/extremereactors/gamecontent/multiblock/turbine/variant/TurbineVariant$Builder;setPartFluidCapacity(I)Lit/zerono/mods/extremereactors/gamecontent/multiblock/turbine/variant/TurbineVariant$Builder;",
                    ordinal = 1
            ),
            remap = false
    )
    private static int configurablePartFluidCapacity(int original) {
        return DCFixesConfig.get().reinforcedPartFluidCapacity;
    }

    @ModifyArg(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lit/zerono/mods/extremereactors/gamecontent/multiblock/turbine/variant/TurbineVariant$Builder;setMaxFluidCapacity(I)Lit/zerono/mods/extremereactors/gamecontent/multiblock/turbine/variant/TurbineVariant$Builder;",
                    ordinal = 1
            ),
            remap = false
    )
    private static int configurableMaxFluidCapacity(int original) {
        return DCFixesConfig.get().reinforcedMaxFluidCapacity;
    }
}
