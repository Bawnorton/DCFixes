package com.bawnorton.dcfixes.extend;

import de.maxhenkel.corpse.corelib.death.Death;

public interface PlayerZombieExtender {
    Death dcfixes$getDeath();

    void dcfixes$setDeath(Death death);
}
