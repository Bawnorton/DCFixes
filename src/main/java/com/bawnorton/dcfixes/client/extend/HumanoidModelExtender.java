package com.bawnorton.dcfixes.client.extend;

import net.minecraft.client.model.geom.ModelPart;

public interface HumanoidModelExtender {
    void dcfixes$setRoot(ModelPart root);

    ModelPart dcfixes$getRoot();
}
