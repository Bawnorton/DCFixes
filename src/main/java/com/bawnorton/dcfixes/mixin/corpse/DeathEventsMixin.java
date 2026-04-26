package com.bawnorton.dcfixes.mixin.corpse;

import com.bawnorton.dcfixes.mixin.corpse.accessor.DeathAccessor;
import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import de.maxhenkel.corpse.corelib.death.Death;
import de.maxhenkel.corpse.corelib.death.DeathEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.smileycorp.hordes.common.entities.PlayerZombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.UUID;

@IfModLoaded({"corpse", "hordes"})
@Mixin(value = DeathEvents.class, remap = false)
abstract class DeathEventsMixin {
    @Definition(id = "getEntity", method = "Lnet/minecraftforge/event/entity/living/LivingDropsEvent;getEntity()Lnet/minecraft/world/entity/LivingEntity;")
    @Definition(id = "entity", local = @Local(type = Entity.class, name = "entity"))
    @Expression("entity = @(?.getEntity())")
    @ModifyExpressionValue(
            method = "playerDeath(Lnet/minecraftforge/event/entity/living/LivingDropsEvent;)V",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private LivingEntity trickCorpseModToThinkZombiePlayersArePlayers(LivingEntity original, @Local(name = "event") LivingDropsEvent event) {
        if(original instanceof PlayerZombie<?> playerZombie) {
            Optional<UUID> playerId = playerZombie.getPlayerUUID();
            Level level = original.level();
            return playerId.map(level::getPlayerByUUID).orElse(null);
        }
        return original;
    }


    @Definition(id = "getEntity", method = "Lnet/minecraftforge/event/entity/living/LivingDropsEvent;getEntity()Lnet/minecraft/world/entity/LivingEntity;")
    @Definition(id = "player", local = @Local(type = ServerPlayer.class, name = "player"))
    @Definition(id = "ServerPlayer", type = ServerPlayer.class)
    @Expression("player = (ServerPlayer) @(?.getEntity())")
    @ModifyExpressionValue(
            method = "playerDeath(Lnet/minecraftforge/event/entity/living/LivingDropsEvent;)V",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private LivingEntity attachDrops(LivingEntity original, @Local(name = "entity") Entity entity, @Local(name = "event") LivingDropsEvent event) {
        if(entity instanceof ServerPlayer serverPlayer && original instanceof PlayerZombie<?> playerZombie) {
            Level level = serverPlayer.level();
            event.getDrops().addAll(playerZombie.getInventory()
                    .stream()
                    .map(stack -> new ItemEntity(
                            level,
                            original.getX(),
                            original.getY(),
                            original.getZ(),
                            stack
                    ))
                    .toList());
            return serverPlayer;
        }
        return original;
    }

    @Inject(
            method = "playerDeath(Lnet/minecraftforge/event/entity/living/LivingDropsEvent;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/event/entity/living/LivingDropsEvent;getDrops()Ljava/util/Collection;"
            )
    )
    private void moveDeathLocation(LivingDropsEvent event, CallbackInfo ci, @Local(name = "death") Death death) {
        LivingEntity entity = event.getEntity();
        if(entity instanceof PlayerZombie<?>) {
            DeathAccessor accessor = (DeathAccessor) death;
            accessor.dcfixes$posX(entity.getX());
            accessor.dcfixes$posY(entity.getY());
            accessor.dcfixes$posZ(entity.getZ());
        }
    }
}
