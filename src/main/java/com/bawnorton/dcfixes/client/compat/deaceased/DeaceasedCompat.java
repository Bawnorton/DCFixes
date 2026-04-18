package com.bawnorton.dcfixes.client.compat.deaceased;

import net.diebuddies.physics.ragdoll.RagdollMapper;

public class DeaceasedCompat {
    public void registerRagdolls() {
        RagdollMapper.addHook(new DeaceasedGeckoLibRagdollHook());
    }
}

