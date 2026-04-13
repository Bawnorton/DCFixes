package com.bawnorton.dcfixes.client.compat;

import com.bawnorton.dcfixes.client.compat.emf.EMFCompat;
import com.bawnorton.dcfixes.client.compat.physics_mod.PhysicsModCompat;

public class ClientCompat {
    private final PhysicsModCompat physicsModCompat;
    private final EMFCompat emfCompat;

    public ClientCompat() {
        physicsModCompat = new PhysicsModCompat();
        emfCompat = new EMFCompat();
    }

    public PhysicsModCompat getPhysicsModCompat() {
        return physicsModCompat;
    }

    public EMFCompat getEmfCompat() {
        return emfCompat;
    }
}
