package com.bawnorton.dcfixes.mixin.solcarrot;

import com.bawnorton.dcfixes.extend.FoodInstanceExtender;
import com.bawnorton.dcfixes.extend.FoodListExtender;
import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.cazsius.solcarrot.tracking.FoodInstance;
import com.cazsius.solcarrot.tracking.FoodList;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("ConstantValue")
@IfModLoaded("solcarrot")
@Mixin(value = FoodList.class, remap = false)
abstract class FoodListMixin implements FoodListExtender {
    @Shadow
    @Final
    private Set<FoodInstance> foods;

    @WrapOperation(
            method = "addFood",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Set;add(Ljava/lang/Object;)Z"
            )
    )
    private <E> boolean updateTimestamp(Set<E> instance, E e, Operation<Boolean> original) {
        E existing = instance.stream().filter(e1 -> e1.equals(e)).findFirst().orElse(null);
        if(existing instanceof FoodInstanceExtender extender) {
            extender.dcfixes$setTimestamp(System.currentTimeMillis());
            extender.dcfixes$setForgotten(false);
        } else if (e instanceof FoodInstanceExtender extender) {
            extender.dcfixes$setTimestamp(System.currentTimeMillis());
            extender.dcfixes$setForgotten(false);
        }
        return original.call(instance, e);
    }

    @ModifyReturnValue(
            method = "getEatenFoods",
            at = @At("RETURN")
    )
    private Set<FoodInstance> ignoreForgottenFoods(Set<FoodInstance> original) {
        return original.stream()
                .filter(f -> {
                    if ((Object) f instanceof FoodInstanceExtender extender) {
                        return !extender.dcfixes$isForgotten();
                    }
                    return true;
                })
                .collect(HashSet::new, Set::add, Set::addAll);
    }

    @Override
    public boolean dcfixes$hasForgotten(Item food) {
        if(food.isEdible() && foods.contains(new FoodInstance(food))) {
            FoodInstance existing = foods.stream().filter(f -> f.equals(new FoodInstance(food))).findFirst().orElse(null);
            if((Object) existing instanceof FoodInstanceExtender extender) {
                return extender.dcfixes$isForgotten();
            }
        }
        return false;
    }
}
