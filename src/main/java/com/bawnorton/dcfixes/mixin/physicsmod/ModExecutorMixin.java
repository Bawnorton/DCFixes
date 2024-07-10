package com.bawnorton.dcfixes.mixin.physicsmod;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.diebuddies.physics.ModExecutor;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ModExecutor.class, remap = false)
public abstract class ModExecutorMixin {
    @WrapOperation(
            method = "lambda$new$0",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/diebuddies/physics/StarterClient;onInitializeClient()V"
            )
    )
    private static void sendInfoOnMacos(Operation<Void> original) {
        if (Util.getOperatingSystem() == Util.OperatingSystem.OSX) {
            try {
                original.call();
            } catch (Throwable throwable) {
                throw new IllegalStateException("""
                    \n
                    PhysicsMod 2.x cannot load on ARM Macs, but a workaround is possible.
                    Follow this guide:
                    https://gist.github.com/Bawnorton/04cbe6dc90347cc13446ae1bb8921067
                """);
            }
        } else {
            original.call();
        }
    }
}
