package com.bawnorton.dcfixes;

import com.bawnorton.dcfixes.client.DeceasedCraftFixesClient;
import com.bawnorton.dcfixes.compat.Compat;
import com.bawnorton.dcfixes.compat.hordes.HordesCompat;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(DeceasedCraftFixes.MOD_ID)
public final class DeceasedCraftFixes {
    public static final String MOD_ID = "dcfixes";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static final Compat COMPAT = new Compat();

    public DeceasedCraftFixes() {
        LOGGER.info("Fixing DeceasedCraft");
        COMPAT.getHordesCompat().ifPresent(HordesCompat::init);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> DeceasedCraftFixesClient::new);
    }

    public static Compat getCompat() {
        return COMPAT;
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
