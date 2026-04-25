package com.bawnorton.dcfixes.mixin.immersive_vehicles.accessor;

import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import mcinterface1201.WrapperEntity;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@IfModLoaded("mts")
@Mixin(value = WrapperEntity.class, remap = false)
public interface WrapperEntityAccessor {
    @Invoker("<init>")
    static WrapperEntity dcfixes$create(Entity entity) {
        throw new AssertionError();
    }

    @Accessor("entity")
    Entity dcfixes$entity();
}
