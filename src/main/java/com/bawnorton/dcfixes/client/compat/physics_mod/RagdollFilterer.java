package com.bawnorton.dcfixes.client.compat.physics_mod;

import net.diebuddies.physics.PhysicsEntity;
import net.minecraft.world.entity.Entity;

import java.util.List;

@FunctionalInterface
public interface RagdollFilterer<T extends Entity> {
    void filter(List<PhysicsEntity> entities, T entity);
}