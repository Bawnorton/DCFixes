package com.bawnorton.dcfixes.mixin.corpse.accessor;

import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import de.maxhenkel.corpse.corelib.death.PlayerDeathEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@IfModLoaded("corpse")
@Mixin(value = PlayerDeathEvent.class, remap = false)
public interface PlayerDeathEventAccessor {
    @Invoker("isStoreDeath")
    boolean dcfixes$isStoreDeath();
}
