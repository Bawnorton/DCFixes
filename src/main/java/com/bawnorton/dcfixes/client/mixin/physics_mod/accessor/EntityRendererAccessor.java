package com.bawnorton.dcfixes.client.mixin.physics_mod.accessor;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@MixinEnvironment("client")
@Mixin(EntityRenderer.class)
public interface EntityRendererAccessor {
    @Accessor("entityRenderDispatcher")
    EntityRenderDispatcher dcfixes$entityRenderDispatcher();

    @Accessor("shadowRadius")
    float dcfixes$shadowRadius();
}
