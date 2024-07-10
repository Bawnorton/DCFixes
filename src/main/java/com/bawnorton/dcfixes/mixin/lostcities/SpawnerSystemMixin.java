package com.bawnorton.dcfixes.mixin.lostcities;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mcjty.incontrol.spawner.SpawnerSystem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SpawnerSystem.class)
public abstract class SpawnerSystemMixin {
    @WrapOperation(
            method = "executeRule(ILmcjty/incontrol/spawner/SpawnerRule;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/EntityType;Lnet/minecraft/entity/SpawnGroup;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;isSpaceEmpty(Lnet/minecraft/util/math/Box;)Z"
            )
    )
    private static boolean notGonnaCheck(ServerWorld instance, Box box, Operation<Boolean> original) {
        return true;
    }
}
