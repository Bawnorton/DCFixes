package com.bawnorton.dcfixes.mixin.hordes;

import com.bawnorton.dcfixes.extend.PlayerZombieExtender;
import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import de.maxhenkel.corpse.corelib.death.Death;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.smileycorp.hordes.common.entities.DrownedPlayer;
import net.smileycorp.hordes.common.entities.HuskPlayer;
import net.smileycorp.hordes.common.entities.PlayerZombie;
import net.smileycorp.hordes.common.entities.ZombiePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@IfModLoaded({"hordes", "corpse"})
@Mixin({ZombiePlayer.class, HuskPlayer.class, DrownedPlayer.class})
abstract class ZombiePlayerMixin implements PlayerZombieExtender {
    @Unique
    private Death dcfixes$death;

    @Inject(
            method = "setPlayer(Lnet/minecraft/world/entity/player/Player;)V",
            at = @At("TAIL"),
            remap = false
    )
    private void trackDeath(Player player, CallbackInfo ci) {
        if(player == null) return;

        dcfixes$death = Death.fromPlayer(player);
    }

    @Inject(
            method = "copyFrom",
            at = @At("TAIL"),
            remap = false
    )
    private void copyDeathOver(PlayerZombie<?> entity, CallbackInfo ci) {
        if(entity instanceof PlayerZombieExtender extender) {
            this.dcfixes$death = extender.dcfixes$getDeath();
        }
    }

    @Inject(
            method = "addAdditionalSaveData",
            at = @At("TAIL")
    )
    private void saveDeath(CompoundTag compound, CallbackInfo ci) {
        if(dcfixes$death == null) return;

        compound.put("dcfixes$death", dcfixes$death.toNBT());
    }

    @Inject(
            method = "readAdditionalSaveData",
            at = @At("TAIL")
    )
    private void readDeath(CompoundTag compound, CallbackInfo ci) {
        CompoundTag deathTag = compound.getCompound("dcfixes$death");
        if(deathTag.isEmpty()) return;

        dcfixes$death = Death.fromNBT(deathTag);
    }

    @Override
    public Death dcfixes$getDeath() {
        return dcfixes$death;
    }

    @Override
    public void dcfixes$setDeath(Death death) {
        this.dcfixes$death = death;
    }
}
