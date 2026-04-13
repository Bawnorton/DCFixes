package com.bawnorton.dcfixes.client.extend;

import software.bernie.geckolib.cache.object.GeoBone;

public interface PhysicsEntityExtender {
    GeoBone dcfixes$getGeoBone();

    void dcfixes$setGeoBone(GeoBone geoBone);

    int dcfixes$ragdollIndex();

    void dcfixes$setRagdollIndex(int index);
}
