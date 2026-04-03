package com.bawnorton.dcfixes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import traben.entity_model_features.EMFManager;
import traben.entity_model_features.utils.EMFEntity;

@Mod(DeceasedCraftFixes.MOD_ID)
public final class DeceasedCraftFixes {
    public static final String MOD_ID = "dcfixes";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static boolean emfModel;

    public DeceasedCraftFixes() {
        LOGGER.debug("Fixing DeceasedCraft");
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void prepareEMF(Entity entity) {
        emfModel = EMFManager.getInstance().rootPartsPerEntityTypeForVariation.get(((EMFEntity) entity).emf$getTypeString()) != null;
    }

    public static boolean isEMFModel() {
        return emfModel;
    }
}
