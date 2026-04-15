package com.bawnorton.dcfixes.client.extend;

import com.bawnorton.dcfixes.config.DCFixesConfig;
import minecrafttransportsimulator.entities.components.AEntityC_Renderable;
import minecrafttransportsimulator.entities.components.AEntityE_Interactable;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.phys.AABB;

public interface InterfaceRenderExtender {
    static boolean shouldRenderInFrustum(AEntityC_Renderable entity) {
        if (StaticHolder.activeFrustum == null) {
            return true;
        }
        final double minX;
        final double minY;
        final double minZ;
        final double maxX;
        final double maxY;
        final double maxZ;
        double frustumCullExpansion = DCFixesConfig.get().frustumCullExpansion;
        if (entity instanceof AEntityE_Interactable<?> interactable) {
            minX = interactable.encompassingBox.globalCenter.x - interactable.encompassingBox.widthRadius - frustumCullExpansion;
            minY = interactable.encompassingBox.globalCenter.y - interactable.encompassingBox.heightRadius - frustumCullExpansion;
            minZ = interactable.encompassingBox.globalCenter.z - interactable.encompassingBox.depthRadius - frustumCullExpansion;
            maxX = interactable.encompassingBox.globalCenter.x + interactable.encompassingBox.widthRadius + frustumCullExpansion;
            maxY = interactable.encompassingBox.globalCenter.y + interactable.encompassingBox.heightRadius + frustumCullExpansion;
            maxZ = interactable.encompassingBox.globalCenter.z + interactable.encompassingBox.depthRadius + frustumCullExpansion;
        } else {
            minX = entity.boundingBox.globalCenter.x - entity.boundingBox.widthRadius - frustumCullExpansion;
            minY = entity.boundingBox.globalCenter.y - entity.boundingBox.heightRadius - frustumCullExpansion;
            minZ = entity.boundingBox.globalCenter.z - entity.boundingBox.depthRadius - frustumCullExpansion;
            maxX = entity.boundingBox.globalCenter.x + entity.boundingBox.widthRadius + frustumCullExpansion;
            maxY = entity.boundingBox.globalCenter.y + entity.boundingBox.heightRadius + frustumCullExpansion;
            maxZ = entity.boundingBox.globalCenter.z + entity.boundingBox.depthRadius + frustumCullExpansion;
        }
        return StaticHolder.activeFrustum.isVisible(new AABB(minX, minY, minZ, maxX, maxY, maxZ));
    }

    class StaticHolder {
        public static Frustum activeFrustum;
    }
}
