package com.bawnorton.dcfixes.client.compat.emf;

import net.minecraft.world.entity.LivingEntity;
import traben.entity_model_features.EMFManager;
import traben.entity_model_features.utils.EMFEntity;
import traben.entity_texture_features.ETFApi;
import traben.entity_texture_features.features.ETFRenderContext;

public class EMFCompat {
    private boolean emfModel = false;

    public void prepare(LivingEntity entity) {
        emfModel = EMFManager.getInstance().rootPartsPerEntityTypeForVariation.get(((EMFEntity) entity).emf$getTypeString()) != null;
        ETFRenderContext.setCurrentEntity(ETFApi.stateOfEntityOrEntityState(entity));
//        EMFAnimationEntityContext.setCurrentEntityIteration((EMFEntityRenderState) ETFEntityRenderState.forEntity((ETFEntity) entity));
    }

    public boolean isEMFModel() {
        return emfModel;
    }
}
