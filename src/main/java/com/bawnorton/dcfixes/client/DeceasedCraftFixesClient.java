package com.bawnorton.dcfixes.client;

import com.bawnorton.dcfixes.client.compat.ClientCompat;
import com.bawnorton.dcfixes.client.compat.geckolib.GeckoLibCompat;
import com.bawnorton.dcfixes.client.compat.physics_mod.PhysicsModCompat;
import com.bawnorton.dcfixes.client.config.DCFixesConfigScreenHandler;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;

public class DeceasedCraftFixesClient {
    private static final ClientCompat COMPAT = new ClientCompat();

    @SuppressWarnings({"removal", "Convert2MethodRef"})
    public DeceasedCraftFixesClient() {
        COMPAT.getPhysicsModCompat().ifPresent(compat -> compat.registerRagdollHooks());
        COMPAT.getGeckoLibCompat().ifPresent(compat -> compat.registerRenderTypeReplacers());
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((client, screen) -> DCFixesConfigScreenHandler.generateConfigScreen(screen)));
    }

    public static ClientCompat getCompat() {
        return COMPAT;
    }
}
