package com.bawnorton.dcfixes.client.compat.geckolib;

import net.minecraft.client.renderer.RenderType;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;

public interface RenderTypeReplacer<E extends GeoAnimatable, R extends GeoRenderer<E>> {
    RenderType apply(E entity, R renderer, RenderType renderType);

    static <E extends GeoAnimatable, R extends GeoRenderer<E>> RenderType defaultReplacer(E entity, R renderer, RenderType ignoredRenderType) {
        return RenderType.entityCutout(renderer.getTextureLocation(entity));
    }
}
