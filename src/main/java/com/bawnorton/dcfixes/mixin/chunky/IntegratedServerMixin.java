package com.bawnorton.dcfixes.mixin.chunky;

import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import net.minecraft.client.server.IntegratedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@IfModLoaded("chunky")
@Mixin(IntegratedServer.class)
abstract class IntegratedServerMixin extends MinecraftServerMixin {
    @Inject(
            method = "tickServer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/server/IntegratedServer;tickPaused()V"
            )
    )
    private void onTickPaused(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        this.dcfixes$runChunkSystemHousekeeping(hasTimeLeft);
    }
}
