package com.bawnorton.super_early.reflection;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public final class FieldReference<T> {
    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    private final Type type;
    private final VarHandle handle;

    private FieldReference(Class<?> owningClass, String name, Type type, Class<T> fieldType) throws IllegalAccessException, NoSuchFieldException {
        this.type = type;
        MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(owningClass, LOOKUP);
        handle = switch (type) {
            case STATIC -> lookup.findStaticVarHandle(owningClass, name, fieldType);
            case INSTANCE -> lookup.findVarHandle(owningClass, name, fieldType);
        };
    }

    public static <T> FieldReference<T> ofStatic(Class<?> owningClass, String name, Class<T> fieldType) {
        try {
            return new FieldReference<>(owningClass, name, Type.STATIC, fieldType);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> FieldReference<T> ofInstance(Class<?> owningClass, String name, Class<T> fieldType) {
        try {
            return new FieldReference<>(owningClass, name, Type.INSTANCE, fieldType);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public T get(Object instance) {
        return switch (type) {
            case STATIC -> (T) handle.get();
            case INSTANCE -> (T) handle.get(requireInstance(instance));
        };
    }

    public T get() {
        return get(null);
    }

    public void set(Object instance, T value) {
        switch (type) {
            case STATIC -> handle.set(value);
            case INSTANCE -> handle.set(requireInstance(instance), value);
        }
    }

    public void set(T value) {
        set(null, value);
    }

    private Object requireInstance(Object instance) {
        if (instance == null) {
            throw new IllegalArgumentException("Instance cannot be null for instance field access");
        }
        return instance;
    }

    private enum Type {
        INSTANCE,
        STATIC
    }
}

