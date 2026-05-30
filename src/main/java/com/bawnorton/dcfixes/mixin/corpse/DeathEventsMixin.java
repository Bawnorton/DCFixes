package com.bawnorton.dcfixes.mixin.corpse;

import com.bawnorton.dcfixes.DeceasedCraftFixes;
import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import de.maxhenkel.corpse.corelib.death.DeathEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@IfModLoaded("corpse")
@Mixin(value = DeathEvents.class, remap = false)
abstract class DeathEventsMixin {
    @ModifyArg(
            method = "register",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/eventbus/api/IEventBus;register(Ljava/lang/Object;)V"
            )
    )
    private static Object captureInstance(Object original) {
        DeceasedCraftFixes.getCompat().getCorpseCompat().orElseThrow().setDeathEventsInstance((DeathEvents) original);
        return original;
    }
}
