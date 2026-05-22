package com.bawnorton.dcfixes.mixin.chunky.accessor;

import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@IfModLoaded("chunky")
@Mixin(ServerLevel.class)
public interface ServerLevelAccessor {
    @Accessor("entityManager")
    PersistentEntitySectionManager<Entity> dcfixes$entityManager();
}
