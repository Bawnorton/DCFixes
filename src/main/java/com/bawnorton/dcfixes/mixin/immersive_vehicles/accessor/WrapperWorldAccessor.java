package com.bawnorton.dcfixes.mixin.immersive_vehicles.accessor;

import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import mcinterface1201.WrapperWorld;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@IfModLoaded("mts")
@Mixin(value = WrapperWorld.class, remap = false)
public interface WrapperWorldAccessor {
    @Accessor("world")
    Level dcfixes$world();
}
