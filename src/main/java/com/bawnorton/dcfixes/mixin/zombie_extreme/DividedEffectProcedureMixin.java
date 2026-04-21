package com.bawnorton.dcfixes.mixin.zombie_extreme;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.ForgeEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import zombie_extreme.init.ZombieExtremeModEntities;
import zombie_extreme.procedures.DividedEffectProcedure;

@Mixin(DividedEffectProcedure.class)
abstract class DividedEffectProcedureMixin {
    @WrapOperation(
            method = "execute",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/EntityType;spawn(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/MobSpawnType;)Lnet/minecraft/world/entity/Entity;"
            )
    )
    private static <T extends Entity> T moveToHead(EntityType<T> instance, ServerLevel level, BlockPos pos, MobSpawnType spawnType, Operation<T> original, LevelAccessor world, double x, double y, double z) {
        T entity = instance.create(level);
        if (entity == null) return null;

        entity.setPos(x, y + ZombieExtremeModEntities.DIVIDED.get().getHeight() - entity.getBbHeight(), z);
        level.addFreshEntityWithPassengers(entity);
        if (entity instanceof Mob mob) {
            ForgeEventFactory.onFinalizeSpawn(mob, level, level.getCurrentDifficultyAt(mob.blockPosition()), spawnType, null, null);
            mob.playAmbientSound();
        }
        return entity;
    }
}
