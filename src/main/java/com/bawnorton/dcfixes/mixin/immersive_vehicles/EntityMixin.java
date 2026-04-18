package com.bawnorton.dcfixes.mixin.immersive_vehicles;

import com.bawnorton.dcfixes.client.extend.EntityExtender;
import minecrafttransportsimulator.mcinterface.IWrapperEntity;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Entity.class)
abstract class EntityMixin implements EntityExtender {
    @Unique
    private IWrapperEntity dcfixes$wrapper;

    @Override
    public IWrapperEntity dcfixes$getWrapper() {
        return dcfixes$wrapper;
    }

    @Override
    public void dcfixes$setWrapper(IWrapperEntity wrapper) {
        this.dcfixes$wrapper = wrapper;
    }
}
