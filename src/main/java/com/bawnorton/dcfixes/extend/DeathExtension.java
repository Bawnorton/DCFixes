package com.bawnorton.dcfixes.extend;

import com.bawnorton.dcfixes.reflection.FieldReference;
import de.maxhenkel.corpse.corelib.death.Death;

import java.util.function.Consumer;

public class DeathExtension {
    private static final ThreadLocal<Boolean> TRANSFER_CAPTURE = ThreadLocal.withInitial(() -> false);

    private static final FieldReference<Double> posXReference = FieldReference.ofInstance(Death.class, "posX", double.class);
    private static final FieldReference<Double> posYReference = FieldReference.ofInstance(Death.class, "posY", double.class);
    private static final FieldReference<Double> posZReference = FieldReference.ofInstance(Death.class, "posZ", double.class);

    private final Death instance;

    private DeathExtension(Death instance) {
        this.instance = instance;
    }

    public static void tryAs(Object instance, Consumer<DeathExtension> consumer) {
        if(Death.class.isAssignableFrom(instance.getClass())) {
            consumer.accept(new DeathExtension((Death) instance));
        }
    }

    public void setPosX(double posX) {
        posXReference.set(instance, posX);
    }

    public void setPosY(double posY) {
        posYReference.set(instance, posY);
    }

    public void setPosZ(double posZ) {
        posZReference.set(instance, posZ);
    }

    public static void setTransferCapture(boolean capture) {
        TRANSFER_CAPTURE.set(capture);
    }

    public static boolean isTransferCapture() {
        return TRANSFER_CAPTURE.get();
    }

    public static void removeTransferCapture() {
        TRANSFER_CAPTURE.remove();
    }
}
