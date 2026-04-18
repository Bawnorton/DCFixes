package com.bawnorton.dcfixes.client.compat.zombie_extreme;

import net.diebuddies.physics.ragdoll.RagdollMapper;

public class ZombieExtremeCompat {
    public void registerRagdolls() {
        RagdollMapper.addHook(new ZombieExtremeGeckoLibRagdollHook());
    }
}

