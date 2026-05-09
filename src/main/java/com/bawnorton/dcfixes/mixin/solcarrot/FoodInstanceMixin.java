package com.bawnorton.dcfixes.mixin.solcarrot;

import com.bawnorton.dcfixes.extend.FoodInstanceExtender;
import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.cazsius.solcarrot.tracking.FoodInstance;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@IfModLoaded("solcarrot")
@Mixin(value = FoodInstance.class, remap = false)
abstract class FoodInstanceMixin implements FoodInstanceExtender {
    @Unique
    private long dcfixes$timestamp = 0L;

    @Unique
    private boolean dcfixes$forgotten = false;

    @Override
    public long dcfixes$getTimestamp() {
        return dcfixes$timestamp;
    }

    @Override
    public void dcfixes$setTimestamp(long time) {
        this.dcfixes$timestamp = time;
    }

    @Override
    public boolean dcfixes$isForgotten() {
        return dcfixes$forgotten;
    }

    @Override
    public void dcfixes$setForgotten(boolean forgotten) {
        this.dcfixes$forgotten = forgotten;
    }

    @ModifyReturnValue(
            method = "encode",
            at = @At("RETURN")
    )
    private String addExtras(String original) {
        return original + "|" + this.dcfixes$getTimestamp() + "|" + dcfixes$isForgotten();
    }

    @SuppressWarnings("ConstantValue")
    @WrapMethod(
            method = "decode"
    )
    private static FoodInstance extractTimestamp(String encoded, Operation<FoodInstance> original) {
        String[] parts = encoded.split("\\|");
        if(parts.length == 3) {
            FoodInstance instance = original.call(parts[0]);
            if((Object) instance instanceof FoodInstanceExtender extender) {
                try {
                    long timestamp = Long.parseLong(parts[1]);
                    extender.dcfixes$setTimestamp(timestamp);
                } catch (NumberFormatException ignored) {}
                boolean forgotten = Boolean.parseBoolean(parts[2]);
                extender.dcfixes$setForgotten(forgotten);
            }
            return instance;
        }
        return original.call(encoded);
    }
}
