package com.bawnorton.dcfixes.client.compat.cnpc;

import com.bawnorton.dcfixes.client.compat.physics_mod.DCFixesRagdollHook;
import noppes.npcs.entity.EntityCustomNpc;

public class CustomNpcRagdollHook extends DCFixesRagdollHook {
    public CustomNpcRagdollHook() {
        registerFilter(EntityCustomNpc.class, (entities, entity) -> entities.clear());
    }
}
