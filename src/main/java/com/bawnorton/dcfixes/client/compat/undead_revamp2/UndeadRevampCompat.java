package com.bawnorton.dcfixes.client.compat.undead_revamp2;

import net.diebuddies.physics.ragdoll.RagdollMapper;

public class UndeadRevampCompat {
    public void registerRagdolls() {
        RagdollMapper.addHook(new UndeadRevampGeckoLibRagdollHook());
    }
}

