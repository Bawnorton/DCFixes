package com.bawnorton.dcfixes.super_early.extensions;

import net.minecraftforge.fml.earlydisplay.RenderElement;

import java.lang.reflect.Proxy;

public class InitializerExtension {
    public static <T> Object create(Forwarder<T> forwarder) {
        try {
            Class<?> initializerType = Class.forName("net.minecraftforge.fml.earlydisplay.RenderElement$Initializer");
            return Proxy.newProxyInstance(RenderElement.class.getClassLoader(), new Class<?>[]{initializerType}, (o, method, objects) -> {
                if(method.getName().equals("get")) {
                    return forwarder.get();
                }
                throw new UnsupportedOperationException("Unsupported method: " + method);
            });
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public interface Forwarder<T> {
        T get();
    }
}