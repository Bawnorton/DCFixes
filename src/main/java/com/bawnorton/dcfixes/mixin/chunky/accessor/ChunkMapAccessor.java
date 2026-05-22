package com.bawnorton.dcfixes.mixin.chunky.accessor;

import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import net.minecraft.server.level.ChunkMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.BooleanSupplier;

@IfModLoaded("chunky")
@Mixin(ChunkMap.class)
public interface ChunkMapAccessor {
    @Invoker("tick")
    void dcfixes$tick(BooleanSupplier haveTime);
}
