package com.bawnorton.dcfixes.mixin.minecraft;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.server.dedicated.DedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@MixinEnvironment(type = MixinEnvironment.Env.SERVER)
@Mixin(DedicatedServer.class)
abstract class DedicatedServerMixin {
    @ModifyReturnValue(
            method = "isCommandBlockEnabled",
            at = @At("RETURN")
    )
    private boolean forceEnabled(boolean original) {
        return true;
    }
}
