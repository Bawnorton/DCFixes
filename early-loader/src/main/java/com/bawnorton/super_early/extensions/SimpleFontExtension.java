package com.bawnorton.super_early.extensions;

import com.bawnorton.super_early.reflection.MethodReference;
import net.minecraftforge.fml.earlydisplay.SimpleFont;

import java.util.function.Consumer;

public class SimpleFontExtension {
    public static final MethodReference<Integer> descentReference = MethodReference.ofInstance(SimpleFont.class, "descent", int.class);
    public static final MethodReference<Integer> lineSpacingReference = MethodReference.ofInstance(SimpleFont.class, "lineSpacing", int.class);

    private final SimpleFont instance;

    private SimpleFontExtension(SimpleFont instance) {
        this.instance = instance;
    }

    public static void tryAs(Object instance, Consumer<SimpleFontExtension> consumer) {
        if(SimpleFont.class.isAssignableFrom(instance.getClass())) {
            consumer.accept(new SimpleFontExtension((SimpleFont) instance));
        }
    }

    public int invokeDescent() {
        return descentReference.invoke(instance);
    }

    public int invokeLineSpacing() {
        return lineSpacingReference.invoke(instance);
    }
}
