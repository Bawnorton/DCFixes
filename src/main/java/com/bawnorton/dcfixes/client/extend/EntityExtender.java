package com.bawnorton.dcfixes.client.extend;

import minecrafttransportsimulator.mcinterface.IWrapperEntity;

public interface EntityExtender {
    IWrapperEntity dcfixes$getWrapper();

    void dcfixes$setWrapper(IWrapperEntity wrapper);
}
