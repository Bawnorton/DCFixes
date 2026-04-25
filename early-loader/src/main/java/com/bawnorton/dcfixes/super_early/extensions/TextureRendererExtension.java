package com.bawnorton.dcfixes.super_early.extensions;

import com.bawnorton.dcfixes.super_early.reflection.MethodReference;
import net.minecraftforge.fml.earlydisplay.RenderElement;
import net.minecraftforge.fml.earlydisplay.SimpleBufferBuilder;

import java.lang.reflect.Proxy;
import java.util.function.Consumer;

public class TextureRendererExtension {
    private static final MethodReference<Void> acceptReference;

    private final Object instance;

    private TextureRendererExtension(Object instance) {
        this.instance = instance;
    }

    public static Object create(Forwarder forwarder) {
        try {
            Class<?> textureRendererType = Class.forName("net.minecraftforge.fml.earlydisplay.RenderElement$TextureRenderer");
            return Proxy.newProxyInstance(RenderElement.class.getClassLoader(), new Class<?>[]{textureRendererType}, (o, method, objects) -> {
                if(method.getName().equals("accept")) {
                    forwarder.accept((SimpleBufferBuilder) objects[0], (RenderElement.DisplayContext) objects[1], (int[]) objects[2], (int) objects[3]);
                    return null;
                }
                throw new UnsupportedOperationException("Unsupported method: " + method);
            });
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void tryAs(Object textureRenderer, Consumer<TextureRendererExtension> consumer) {
        if(textureRenderer.getClass().getInterfaces().length == 1 && textureRenderer.getClass().getInterfaces()[0].getName().equals("net.minecraftforge.fml.earlydisplay.RenderElement$TextureRenderer")) {
            consumer.accept(new TextureRendererExtension(textureRenderer));
        }
    }

    public void invokeAccept(SimpleBufferBuilder bb, RenderElement.DisplayContext context, int[] size, int frame) {
        acceptReference.invoke(instance, bb, context, size, frame);
    }

    static {
        try {
            Class<?> textureRendererType = Class.forName("net.minecraftforge.fml.earlydisplay.RenderElement$TextureRenderer");
            acceptReference = MethodReference.ofInstance(textureRendererType, "accept", void.class, SimpleBufferBuilder.class, RenderElement.DisplayContext.class, int[].class, int.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public interface Forwarder {
        void accept(SimpleBufferBuilder bb, RenderElement.DisplayContext context, int[] size, int frame);
    }
}