package com.bawnorton.dcfixes.mixin.tacz;

import com.bawnorton.dcfixes.client.extend.CommonDataManagerExtender;
import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.tacz.guns.resource.CommonAssetsManager;
import com.tacz.guns.resource.index.CommonAmmoIndex;
import com.tacz.guns.resource.index.CommonAttachmentIndex;
import com.tacz.guns.resource.index.CommonBlockIndex;
import com.tacz.guns.resource.index.CommonGunIndex;
import com.tacz.guns.resource.manager.CommonDataManager;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Set;

@IfModLoaded("tacz")
@Mixin(value = CommonAssetsManager.class, remap = false)
abstract class CommonAssetsManagerMixin {
    @Shadow
    private CommonDataManager<CommonBlockIndex> blockIndex;

    @Shadow
    private CommonDataManager<CommonGunIndex> gunIndex;

    @Shadow
    private CommonDataManager<CommonAmmoIndex> ammoIndex;

    @Shadow
    private CommonDataManager<CommonAttachmentIndex> attachmentIndex;

    @Inject(
            method = "getAllBlocks",
            at = @At("HEAD")
    )
    private void loadAllBlocks(CallbackInfoReturnable<Set<Map.Entry<ResourceLocation, CommonBlockIndex>>> cir) {
        if (blockIndex instanceof CommonDataManagerExtender extender) {
            extender.dcfixes$loadAll();
        }
    }

    @Inject(
            method = "getAllGuns",
            at = @At("HEAD")
    )
    private void loadAllGuns(CallbackInfoReturnable<Set<Map.Entry<ResourceLocation, CommonGunIndex>>> cir) {
        if (gunIndex instanceof CommonDataManagerExtender extender) {
            extender.dcfixes$loadAll();
        }
    }

    @Inject(
            method = "getAllAmmos",
            at = @At("HEAD")
    )
    private void loadAllAmmos(CallbackInfoReturnable<Set<Map.Entry<ResourceLocation, CommonAmmoIndex>>> cir) {
        if (ammoIndex instanceof CommonDataManagerExtender extender) {
            extender.dcfixes$loadAll();
        }
    }

    @Inject(
            method = "getAllAttachments",
            at = @At("HEAD")
    )
    private void loadAllAttachments(CallbackInfoReturnable<Set<Map.Entry<ResourceLocation, CommonAttachmentIndex>>> cir) {
        if (attachmentIndex instanceof CommonDataManagerExtender extender) {
            extender.dcfixes$loadAll();
        }
    }
}
