package com.bawnorton.dcfixes.super_early.reflection;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public final class MethodReference<T> {
    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    private final MethodHandle handle;

    private MethodReference(Class<?> owningClass, String name, Type type, Class<T> returnType, Class<?>... parameterTypes) throws IllegalAccessException, NoSuchMethodException {
        MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(owningClass, LOOKUP);
        handle = switch (type) {
            case STATIC -> lookup.findStatic(owningClass, name, MethodType.methodType(returnType, parameterTypes));
            case INSTANCE -> lookup.findVirtual(owningClass, name, MethodType.methodType(returnType, parameterTypes));
            case CTOR -> lookup.findConstructor(owningClass, MethodType.methodType(void.class, parameterTypes));
        };
    }

    public static <T> MethodReference<T> ofStatic(Class<?> owningClass, String name, Class<T> returnType, Class<?>... parameterTypes) {
        try {
            return new MethodReference<>(owningClass, name, Type.STATIC, returnType, parameterTypes);
        } catch (IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> MethodReference<T> ofInstance(Class<?> owningClass, String name, Class<T> returnType, Class<?>... parameterTypes) {
        try {
            return new MethodReference<>(owningClass, name, Type.INSTANCE, returnType, parameterTypes);
        } catch (IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> MethodReference<T> ofConstructor(Class<T> owningClass, Class<?>... parameterTypes) {
        try {
            return new MethodReference<>(owningClass, null, Type.CTOR, null, parameterTypes);
        } catch (IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public T invoke(Object... args) {
        try {
            return (T) handle.invokeWithArguments(args);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private enum Type {
        INSTANCE,
        STATIC,
        CTOR
    }
}

