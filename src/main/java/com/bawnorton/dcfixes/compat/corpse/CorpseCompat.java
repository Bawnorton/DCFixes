package com.bawnorton.dcfixes.compat.corpse;

import com.bawnorton.dcfixes.mixin.corpse.accessor.DeathEventsAccessor;
import de.maxhenkel.corpse.corelib.death.Death;
import de.maxhenkel.corpse.corelib.death.DeathEvents;
import net.minecraft.server.level.ServerPlayer;

public class CorpseCompat {
    private DeathEvents deathEventsInstance;

    public void setDeathEventsInstance(DeathEvents deathEventsInstance) {
        this.deathEventsInstance = deathEventsInstance;
    }

    public Death getDeath(ServerPlayer player) {
        if(deathEventsInstance == null) return null;

        return ((DeathEventsAccessor) (Object) deathEventsInstance).dcfixes$deathMap().remove(player);
    }
}
