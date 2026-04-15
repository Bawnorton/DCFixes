package com.bawnorton.dcfixes.mixin.accessor;

import mcinterface1201.WrapperWorld;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = WrapperWorld.class, remap = false)
public interface WrapperWorldAccessor {
    @Accessor("world")
    Level dcfixes$world();
}
