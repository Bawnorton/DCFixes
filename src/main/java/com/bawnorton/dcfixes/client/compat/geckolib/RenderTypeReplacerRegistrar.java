package com.bawnorton.dcfixes.client.compat.geckolib;

import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;

public interface RenderTypeReplacerRegistrar {
    <E extends GeoAnimatable, R extends GeoRenderer<E>> void register(Class<R> type, RenderTypeReplacer<E, R> replacer);
}
