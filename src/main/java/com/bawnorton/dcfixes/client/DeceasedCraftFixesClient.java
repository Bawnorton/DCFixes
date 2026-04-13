package com.bawnorton.dcfixes.client;

import com.bawnorton.dcfixes.client.compat.ClientCompat;

public class DeceasedCraftFixesClient {
    private static final ClientCompat COMPAT = new ClientCompat();

    public DeceasedCraftFixesClient() {
        COMPAT.getPhysicsModCompat().registerRagdollHooks();
    }

    public static ClientCompat getCompat() {
        return COMPAT;
    }
}
