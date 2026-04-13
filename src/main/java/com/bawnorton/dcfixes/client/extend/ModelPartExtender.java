package com.bawnorton.dcfixes.client.extend;

import net.minecraft.client.model.geom.ModelLayerLocation;

public interface ModelPartExtender {
    void dcfixes$setLocation(ModelLayerLocation location);

    ModelLayerLocation dcfixes$getLocation();
}
