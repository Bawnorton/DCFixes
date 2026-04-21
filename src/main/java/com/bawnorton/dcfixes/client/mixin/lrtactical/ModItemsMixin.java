package com.bawnorton.dcfixes.client.mixin.lrtactical;

import com.bawnorton.dcfixes.client.extend.CommonDataManagerExtender;
import com.bawnorton.dcfixes.client.mixin.accessor.lrtactical.CommonAssetsManagerAccessor;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import me.xjqsh.lrtactical.init.ModItems;
import me.xjqsh.lrtactical.resource.CommonAssetsManager;
import me.xjqsh.lrtactical.resource.ICommonResourceProvider;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@MixinEnvironment("client")
@Mixin(value = ModItems.class, remap = false)
abstract class ModItemsMixin {
    @Inject(
            method = "fillThrowables",
            at = @At("HEAD")
    )
    private static void loadAllThrowables(CreativeModeTab.ItemDisplayParameters pParameters, CreativeModeTab.Output pOutput, CallbackInfo ci) {
        ICommonResourceProvider provider = CommonAssetsManager.get();
        if (provider instanceof CommonAssetsManagerAccessor accessor) {
            ((CommonDataManagerExtender) accessor.dcfixes$throwableIndexManager()).dcfixes$loadAll();
        }
    }

    @Inject(
            method = "fillMeleeWeapons",
            at = @At("HEAD")
    )
    private static void loadAllMeleeWeapons(CreativeModeTab.ItemDisplayParameters pParameters, CreativeModeTab.Output pOutput, CallbackInfo ci) {
        ICommonResourceProvider provider = CommonAssetsManager.get();
        if (provider instanceof CommonAssetsManagerAccessor accessor) {
            ((CommonDataManagerExtender) accessor.dcfixes$meleeIndexManager()).dcfixes$loadAll();
        }
    }
}
