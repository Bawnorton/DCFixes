package com.bawnorton.dcfixes.client.mixin.tacz;

import com.bawnorton.dcfixes.client.extend.CommonDataManagerExtender;
import com.bawnorton.dcfixes.client.mixin.tacz.accessor.CommonAssetsManagerAccessor;
import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.tacz.guns.item.AmmoItem;
import com.tacz.guns.resource.CommonAssetsManager;
import com.tacz.guns.resource.ICommonResourceProvider;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@IfModLoaded("tacz")
@MixinEnvironment("client")
@Mixin(value = AmmoItem.class, remap = false)
abstract class AmmoItemMixin {
    @Inject(
            method = "fillItemCategory",
            at = @At("HEAD")
    )
    private static void loadAllAmmo(CallbackInfoReturnable<NonNullList<ItemStack>> cir) {
        ICommonResourceProvider provider = CommonAssetsManager.get();
        if (provider instanceof CommonAssetsManagerAccessor accessor) {
            ((CommonDataManagerExtender) accessor.dcfixes$ammoIndex()).dcfixes$loadAll();
        }
    }
}
