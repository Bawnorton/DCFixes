package com.bawnorton.dcfixes.client;

import com.bawnorton.dcfixes.client.compat.ClientCompat;
import com.bawnorton.dcfixes.client.config.DCFixesConfigScreenHandler;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;

public class DeceasedCraftFixesClient {
    private static final ClientCompat COMPAT = new ClientCompat();

    public DeceasedCraftFixesClient() {
        COMPAT.getPhysicsModCompat().registerRagdollHooks();
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((client, screen) -> DCFixesConfigScreenHandler.generateConfigScreen(screen)));
    }

    public static ClientCompat getCompat() {
        return COMPAT;
    }
}
