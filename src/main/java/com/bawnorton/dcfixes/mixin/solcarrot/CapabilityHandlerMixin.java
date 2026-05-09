package com.bawnorton.dcfixes.mixin.solcarrot;

import com.bawnorton.dcfixes.DeceasedCraftFixes;
import com.bawnorton.dcfixes.config.DCFixesConfig;
import com.bawnorton.dcfixes.extend.FoodInstanceExtender;
import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.cazsius.solcarrot.SOLCarrotConfig;
import com.cazsius.solcarrot.tracking.CapabilityHandler;
import com.cazsius.solcarrot.tracking.FoodList;
import com.cazsius.solcarrot.tracking.ProgressInfo;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Comparator;
import java.util.Objects;

@IfModLoaded("solcarrot")
@Mixin(value = CapabilityHandler.class, remap = false)
abstract class CapabilityHandlerMixin {
    @Inject(
            method = "onClone",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/event/entity/player/PlayerEvent$Clone;getOriginal()Lnet/minecraft/world/entity/player/Player;"
            ),
            cancellable = true
    )
    private static void resetToMilestone(PlayerEvent.Clone event, CallbackInfo ci) {
        if (!DCFixesConfig.get().solCarrotForgetFoods) return;

        Player originalPlayer = event.getOriginal();
        if (DCFixesConfig.get().solCarrotForgetFoodsMinDifficulty < originalPlayer.level().getDifficulty().getId()) return;

        originalPlayer.reviveCaps();
        if(DeceasedCraftFixes.getCompat().getLSOCompat().map(compat -> compat.willLoseHearts(originalPlayer)).orElse(false)) {
            return;
        }

        ci.cancel();
        FoodList original = FoodList.get(originalPlayer);
        FoodList newInstance = FoodList.get(event.getEntity());
        newInstance.deserializeNBT(original.serializeNBT());


        ProgressInfo progressInfo = newInstance.getProgressInfo();
        int priorMilestone = progressInfo.milestonesAchieved() - 1;
        if (priorMilestone < 0) {
            originalPlayer.invalidateCaps();
            return;
        }

        int foodNeededForPrior = priorMilestone == 0 ? 0 : SOLCarrotConfig.milestone(priorMilestone - 1);
        int foodsToForget = progressInfo.foodsEaten - foodNeededForPrior;
        newInstance.getEatenFoods()
                .stream()
                .map(f -> (FoodInstanceExtender) (Object) f)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingLong(FoodInstanceExtender::dcfixes$getTimestamp))
                .limit(foodsToForget)
                .forEach(extender -> extender.dcfixes$setForgotten(true));
        newInstance.invalidateProgressInfo();
        originalPlayer.invalidateCaps();
    }
}
