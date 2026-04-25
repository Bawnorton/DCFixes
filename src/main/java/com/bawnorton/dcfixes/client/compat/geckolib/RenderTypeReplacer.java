package com.bawnorton.dcfixes.client.compat.geckolib;

import net.minecraft.client.renderer.RenderType;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;

public interface RenderTypeReplacer<E extends GeoAnimatable, R extends GeoRenderer<E>> {
    static <E extends GeoAnimatable, R extends GeoRenderer<E>> RenderType cutoutReplacer(E entity, R renderer, RenderType ignoredRenderType) {
        return RenderType.entityCutout(renderer.getTextureLocation(entity));
    }

    static <E extends GeoAnimatable, R extends GeoRenderer<E>> RenderType cutoutNoCullReplacer(E entity, R renderer, RenderType ignoredRenderType) {
        return RenderType.entityCutoutNoCull(renderer.getTextureLocation(entity));
    }

    RenderType apply(E entity, R renderer, RenderType renderType);
}
