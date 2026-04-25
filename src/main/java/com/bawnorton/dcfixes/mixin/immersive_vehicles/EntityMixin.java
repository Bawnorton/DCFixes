package com.bawnorton.dcfixes.mixin.immersive_vehicles;

import com.bawnorton.dcfixes.client.extend.EntityExtender;
import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import minecrafttransportsimulator.mcinterface.IWrapperEntity;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@IfModLoaded("mts")
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
