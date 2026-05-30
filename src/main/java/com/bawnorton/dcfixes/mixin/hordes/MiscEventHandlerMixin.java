package com.bawnorton.dcfixes.mixin.hordes;

import com.bawnorton.dcfixes.DeceasedCraftFixes;
import com.bawnorton.dcfixes.extend.DeathExtension;
import com.bawnorton.dcfixes.extend.PlayerZombieExtender;
import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import de.maxhenkel.corpse.corelib.death.Death;
import de.maxhenkel.corpse.corelib.death.DeathManager;
import de.maxhenkel.corpse.entities.CorpseEntity;
import de.maxhenkel.corpse.events.DeathEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.smileycorp.hordes.common.MiscEventHandler;
import net.smileycorp.hordes.common.entities.PlayerZombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collection;

@IfModLoaded({"hordes", "corpse"})
@Mixin(value = MiscEventHandler.class, remap = false)
abstract class MiscEventHandlerMixin {
    @WrapOperation(
            method = "onDrop",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/smileycorp/hordes/common/entities/PlayerZombie;storeDrops(Ljava/util/Collection;)V"
            )
    )
    private void attachDeath(PlayerZombie<?> instance, Collection<ItemEntity> itemEntities, Operation<Void> original, LivingDropsEvent event, @Local(name = "player") Player player) {
        if(instance instanceof PlayerZombieExtender extender && player instanceof ServerPlayer serverPlayer) {
            Death death = DeceasedCraftFixes.getCompat().getCorpseCompat().orElseThrow().getDeath(serverPlayer);
            if (death == null) {
                death = Death.fromPlayer(serverPlayer);
            }
            death.processDrops(new ArrayList<>(itemEntities));
            extender.dcfixes$setDeath(death);
            DeathManager.addDeath(serverPlayer, death);
        }
    }

    @Inject(
            method = "onDeath",
            at = @At("HEAD"),
            cancellable = true
    )
    private void spawnBody(LivingDeathEvent event, CallbackInfo ci) {
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof PlayerZombieExtender extender)) return;
        PlayerZombie<?> playerZombie = (PlayerZombie<?>) entity;

        ci.cancel();
        Death death = extender.dcfixes$getDeath();
        if (death == null) return;

        DeathExtension.tryAs(death, extension -> {
            extension.setPosX(entity.getX());
            extension.setPosY(entity.getY());
            extension.setPosZ(entity.getZ());
            extension.setDimension(entity.level().dimension().location().toString());
        });

        Player player = playerZombie.getPlayerUUID().map(entity.level()::getPlayerByUUID).orElse(null);
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.serverLevel().addFreshEntity(CorpseEntity.createFromDeath(serverPlayer, death));
            DeathEvents.deleteOldDeaths(serverPlayer.serverLevel());
        }
    }
}
