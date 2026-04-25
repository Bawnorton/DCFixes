package com.bawnorton.dcfixes.super_early.extensions;

import net.minecraftforge.fml.earlydisplay.RenderElement;
import net.minecraftforge.fml.earlydisplay.SimpleBufferBuilder;

import java.lang.reflect.Proxy;

public class RendererExtension {
    public static Object create(Forwarder forwarder) {
        try {
            Class<?> rendererType = Class.forName("net.minecraftforge.fml.earlydisplay.RenderElement$Renderer");
            return Proxy.newProxyInstance(RenderElement.class.getClassLoader(), new Class<?>[]{rendererType}, (o, method, objects) -> {
                if(method.getName().equals("accept")) {
                    forwarder.accept((SimpleBufferBuilder) objects[0], (RenderElement.DisplayContext) objects[1], (int) objects[2]);
                    return null;
                }
                throw new UnsupportedOperationException("Unsupported method: " + method);
            });
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public interface Forwarder {
        void accept(SimpleBufferBuilder bb, RenderElement.DisplayContext context, int frame);
    }
}