package com.bawnorton.dcfixes.mixin.corpse;

import com.bawnorton.dcfixes.extend.DeathExtender;
import com.bawnorton.dcfixes.extend.DeathExtension;
import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import de.maxhenkel.corpse.corelib.death.Death;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.Collection;
import java.util.Map;

@IfModLoaded("corpse")
@Mixin(value = Death.class, remap = false)
abstract class DeathMixin implements DeathExtender {
    @Shadow
    private NonNullList<ItemStack> additionalItems;

    @Unique
    private final NonNullList<ItemStack> dcfixes$curios = NonNullList.create();

    @WrapMethod(
            method = "processDrops"
    )
    private void dontOnFakeDeathTransfer(Collection<ItemEntity> items, Operation<Void> original) {
        if(DeathExtension.isTransferCapture()) {
            additionalItems.addAll(dcfixes$curios);
            DeathExtension.removeTransferCapture();
            return;
        }
        original.call(items);
    }

    @ModifyReturnValue(
            method = "fromPlayer",
            at = @At("TAIL")
    )
    private static Death attachCurios(Death original, Player player) {
        ICuriosItemHandler curiosItemHandler = CuriosApi.getCuriosInventory(player).resolve().orElse(null);
        if(curiosItemHandler == null) return original;

        DeathExtender extender = (DeathExtender) original;
        Map<String, ICurioStacksHandler> curios = curiosItemHandler.getCurios();
        for (ICurioStacksHandler stacksHandler : curios.values()) {
            for (int i = 0; i < stacksHandler.getSlots(); i++) {
                ItemStack stack = stacksHandler.getStacks().getStackInSlot(i);
                ItemStack cosmeticStack = stacksHandler.getCosmeticStacks().getStackInSlot(i);
                if (!stack.isEmpty()) extender.dcfixes$getCurios().add(stack.copy());
                if (!cosmeticStack.isEmpty()) extender.dcfixes$getCurios().add(cosmeticStack.copy());
            }
        }
        return original;
    }

    @Override
    public NonNullList<ItemStack> dcfixes$getCurios() {
        return dcfixes$curios;
    }
}
