package com.bawnorton.dcfixes.extend;

import com.bawnorton.dcfixes.reflection.FieldReference;
import de.maxhenkel.corpse.corelib.death.Death;

import java.util.function.Consumer;

public class DeathExtension {
    private static final FieldReference<Double> posXReference = FieldReference.ofInstance(Death.class, "posX", double.class);
    private static final FieldReference<Double> posYReference = FieldReference.ofInstance(Death.class, "posY", double.class);
    private static final FieldReference<Double> posZReference = FieldReference.ofInstance(Death.class, "posZ", double.class);
    private static final FieldReference<String> dimensionReference = FieldReference.ofInstance(Death.class, "dimension", String.class);

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

    public void setDimension(String dimension) {
        dimensionReference.set(instance, dimension);
    }
}
