package com.bawnorton.dcfixes;

import com.bawnorton.dcfixes.client.DeceasedCraftFixesClient;
import com.bawnorton.dcfixes.compat.Compat;
import com.bawnorton.dcfixes.network.DCNetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

@SuppressWarnings("Convert2MethodRef")
@Mod(DeceasedCraftFixes.MOD_ID)
public final class DeceasedCraftFixes {
    public static final String MOD_ID = "dcfixes";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static final Compat COMPAT = new Compat();

    public DeceasedCraftFixes() {
        LOGGER.info("Fixing DeceasedCraft");
        COMPAT.getHordesCompat().ifPresent(hordes -> hordes.init());
        COMPAT.getLSOCompat().ifPresent(lso -> lso.init());
        COMPAT.getLostCitiesCompat().ifPresent(lostCities -> lostCities.init());
        DCNetworking.registerPackets();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> DeceasedCraftFixesClient::new);
    }

    public static Compat getCompat() {
        return COMPAT;
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
