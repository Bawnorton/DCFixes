package com.bawnorton.dcfixes.client.compat.physics_mod;

import net.minecraft.world.entity.Entity;

public record InternalHandler<T extends Entity>(RagdollMapper<T> mapper, RagdollFilterer<T> filterer) {
}