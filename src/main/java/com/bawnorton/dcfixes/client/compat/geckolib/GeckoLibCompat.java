package com.bawnorton.dcfixes.client.compat.geckolib;

import com.bawnorton.dcfixes.client.DeceasedCraftFixesClient;
import com.bawnorton.dcfixes.client.compat.ClientCompat;
import net.minecraft.client.renderer.RenderType;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;

import java.util.HashMap;
import java.util.Map;

public class GeckoLibCompat {
    private final Map<Class<? extends GeoRenderer<?>>, RenderTypeReplacer<?, ?>> renderTypeReplacers = new HashMap<>();

    public void registerRenderTypeReplacers() {
        RenderTypeReplacerRegistrar registrar = this::registerRenderTypeReplacer;
        ClientCompat clientCompat = DeceasedCraftFixesClient.getCompat();
        clientCompat.getZombieExtremeCompat().ifPresent(compat -> compat.registerRenderTypeReplacers(registrar));
        clientCompat.getUndeadRevampCompat().ifPresent(compat -> compat.registerRenderTypeReplacers(registrar));
        clientCompat.getDeaceasedCompat().ifPresent(compat -> compat.registerRenderTypeReplacers(registrar));
    }

    private <E extends GeoAnimatable, R extends GeoRenderer<E>> void registerRenderTypeReplacer(Class<R> rendererClass, RenderTypeReplacer<E, R> typeReplacer) {
        if (renderTypeReplacers.containsKey(rendererClass)) {
            throw new IllegalStateException("Renderer class " + rendererClass.getName() + " already has a render type replacer registered");
        }
        renderTypeReplacers.put(rendererClass, typeReplacer);
    }

    @SuppressWarnings("unchecked")
    public <E extends GeoAnimatable, R extends GeoRenderer<E>> RenderType replaceRenderType(E entity, R renderer, RenderType renderType) {
        RenderTypeReplacer<E, R> renderTypeReplacer = (RenderTypeReplacer<E, R>) renderTypeReplacers.get(renderer.getClass());
        if (renderTypeReplacer != null) {
            return renderTypeReplacer.apply(entity, renderer, renderType);
        }
        return renderType;
    }
}
