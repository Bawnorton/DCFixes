package com.bawnorton.dcfixes.super_early.extensions;

import com.bawnorton.dcfixes.super_early.reflection.FieldReference;
import net.minecraftforge.fml.loading.ImmediateWindowHandler;
import net.minecraftforge.fml.loading.ImmediateWindowProvider;

public class ImmediateWindowHandlerExtension {
    private static final FieldReference<ImmediateWindowProvider> providerReference;

    public static void setProvider(ImmediateWindowProvider provider) {
        providerReference.set(provider);
    }

    public static ImmediateWindowProvider getProvider() {
        return providerReference.get();
    }

    static {
        providerReference = FieldReference.ofStatic(ImmediateWindowHandler.class, "provider", ImmediateWindowProvider.class);
    }
}

