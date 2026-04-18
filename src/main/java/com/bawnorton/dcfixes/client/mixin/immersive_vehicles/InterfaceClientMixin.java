package com.bawnorton.dcfixes.client.mixin.immersive_vehicles;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import mcinterface1201.InterfaceClient;
import minecrafttransportsimulator.mcinterface.IWrapperPlayer;
import minecrafttransportsimulator.systems.LanguageSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@MixinEnvironment("client")
@Mixin(value = InterfaceClient.class, remap = false)
abstract class InterfaceClientMixin {
    @WrapWithCondition(
            method = "onIVClientTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lminecrafttransportsimulator/mcinterface/IWrapperPlayer;displayChatMessage(Lminecrafttransportsimulator/systems/LanguageSystem$LanguageEntry;[Ljava/lang/Object;)V"
            )
    )
    private static boolean shhhhh(IWrapperPlayer instance, LanguageSystem.LanguageEntry languageEntry, Object[] objects) {
        return false;
    }
}
