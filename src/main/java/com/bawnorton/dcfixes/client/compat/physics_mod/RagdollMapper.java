package com.bawnorton.dcfixes.client.compat.physics_mod;

import net.diebuddies.physics.ragdoll.Ragdoll;
import net.minecraft.world.entity.Entity;

@FunctionalInterface
public interface RagdollMapper<T extends Entity> {
    void map(Ragdoll ragdoll, T entity);
}
