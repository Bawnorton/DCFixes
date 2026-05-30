package com.bawnorton.dcfixes.mixin.corpse.accessor;

import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import de.maxhenkel.corpse.corelib.death.Death;
import de.maxhenkel.corpse.corelib.death.DeathEvents;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import java.util.UUID;

@IfModLoaded("corpse")
@Mixin(value = DeathEvents.class, remap = false)
public interface DeathEventsAccessor {
    @Accessor("deathMap")
    Map<ServerPlayer, Death> dcfixes$deathMap();
}
