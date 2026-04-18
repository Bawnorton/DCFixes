package com.bawnorton.dcfixes.client.compat.naturalist;

import net.diebuddies.physics.ragdoll.RagdollMapper;

public class NaturalistCompat {
    public void registerRagdolls() {
        RagdollMapper.addHook(new NaturalistGeckoLibRagdollHook());
    }
}

