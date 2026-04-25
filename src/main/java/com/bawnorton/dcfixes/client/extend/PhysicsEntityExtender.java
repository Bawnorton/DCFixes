package com.bawnorton.dcfixes.client.extend;

public interface PhysicsEntityExtender {
    String dcfixes$getBoneId();

    void dcfixes$setBoneId(String geoBone);

    int dcfixes$ragdollIndex();

    void dcfixes$setRagdollIndex(int index);
}
