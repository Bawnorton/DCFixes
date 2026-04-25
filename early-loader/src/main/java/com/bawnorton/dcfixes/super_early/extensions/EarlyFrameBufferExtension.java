package com.bawnorton.dcfixes.super_early.extensions;

import com.bawnorton.dcfixes.super_early.reflection.MethodReference;
import net.minecraftforge.fml.earlydisplay.EarlyFramebuffer;
import net.minecraftforge.fml.earlydisplay.RenderElement;

import java.util.function.Consumer;

public class EarlyFrameBufferExtension {
    private static final MethodReference<EarlyFramebuffer> ctorReference = MethodReference.ofConstructor(EarlyFramebuffer.class, RenderElement.DisplayContext.class);
    private static final MethodReference<Void> closeReference = MethodReference.ofInstance(EarlyFramebuffer.class, "close", void.class);

    private final EarlyFramebuffer instance;

    private EarlyFrameBufferExtension(EarlyFramebuffer instance) {
        this.instance = instance;
    }

    public static EarlyFramebuffer create(RenderElement.DisplayContext context) {
        return ctorReference.invoke(context);
    }

    public static void tryAs(Object instance, Consumer<EarlyFrameBufferExtension> consumer) {
        if(EarlyFramebuffer.class.isAssignableFrom(instance.getClass())) {
            consumer.accept(new EarlyFrameBufferExtension((EarlyFramebuffer) instance));
        }
    }

    public void invokeClose() {
        closeReference.invoke(instance);
    }
}

