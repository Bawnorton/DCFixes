package com.bawnorton.dcfixes.client.mixin.immersive_vehicles;

import com.bawnorton.dcfixes.config.DCFixesConfig;
import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import minecrafttransportsimulator.entities.components.AEntityE_Interactable;
import minecrafttransportsimulator.items.instances.ItemInstrument;
import minecrafttransportsimulator.mcinterface.AWrapperWorld;
import minecrafttransportsimulator.mcinterface.IWrapperNBT;
import minecrafttransportsimulator.mcinterface.IWrapperPlayer;
import minecrafttransportsimulator.mcinterface.InterfaceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@IfModLoaded("mts")
@MixinEnvironment("client")
@Mixin(value = AEntityE_Interactable.class, remap = false)
abstract class AEntityE_InteractableMixin extends AEntityD_DefinableMixin {
    protected AEntityE_InteractableMixin(AWrapperWorld world, IWrapperNBT data) {
        super(world, data);
    }

    @Definition(id = "instrument", local = @Local(type = ItemInstrument.class, name = "instrument"))
    @Expression("instrument != null")
    @ModifyExpressionValue(
            method = "renderModel",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private boolean dontRenderInstrumentsFarAway(boolean original) {
        IWrapperPlayer clientPlayer = InterfaceManager.clientInterface.getClientPlayer();
        if (clientPlayer != null && !position.isDistanceToCloserThan(clientPlayer.getPosition(), DCFixesConfig.get().instrumentRenderDistance)) {
            return false;
        }
        return original;
    }
}
