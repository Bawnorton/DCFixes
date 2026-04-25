package com.bawnorton.dcfixes.client.mixin.tacz;

import com.bawnorton.dcfixes.DeceasedCraftFixes;
import com.bawnorton.dcfixes.client.extend.ClientGunIndexExtender;
import com.bawnorton.dcfixes.collection.StandardLambdaMap;
import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.client.resource.ClientAssetsManager;
import com.tacz.guns.client.resource.ClientIndexManager;
import com.tacz.guns.client.resource.GunDisplayInstance;
import com.tacz.guns.client.resource.index.ClientAmmoIndex;
import com.tacz.guns.client.resource.index.ClientAttachmentIndex;
import com.tacz.guns.client.resource.index.ClientBlockIndex;
import com.tacz.guns.client.resource.index.ClientGunIndex;
import com.tacz.guns.client.resource.pojo.display.gun.GunDisplay;
import com.tacz.guns.resource.index.CommonAmmoIndex;
import com.tacz.guns.resource.index.CommonAttachmentIndex;
import com.tacz.guns.resource.index.CommonBlockIndex;
import com.tacz.guns.resource.index.CommonGunIndex;
import com.tacz.guns.resource.pojo.AmmoIndexPOJO;
import com.tacz.guns.resource.pojo.AttachmentIndexPOJO;
import com.tacz.guns.resource.pojo.BlockIndexPOJO;
import com.tacz.guns.resource.pojo.GunIndexPOJO;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@SuppressWarnings("DataFlowIssue")
@IfModLoaded("tacz")
@MixinEnvironment("client")
@Mixin(value = ClientIndexManager.class, remap = false)
abstract class ClientIndexManagerMixin {
    @Shadow
    @Final
    @Mutable
    public static Map<ResourceLocation, GunDisplayInstance> GUN_DISPLAY;

    @Shadow
    @Final
    @Mutable
    public static Map<ResourceLocation, ClientGunIndex> GUN_INDEX;

    @Shadow
    @Final
    @Mutable
    public static Map<ResourceLocation, ClientAmmoIndex> AMMO_INDEX;

    @Shadow
    @Final
    @Mutable
    public static Map<ResourceLocation, ClientAttachmentIndex> ATTACHMENT_INDEX;

    @Shadow
    @Final
    @Mutable
    public static Map<ResourceLocation, ClientBlockIndex> BLOCK_INDEX;

    static {
        GUN_DISPLAY = new StandardLambdaMap<>(location -> {
            GunDisplay gunDisplay = ClientAssetsManager.INSTANCE.getGunDisplay(location);
            try {
                return GunDisplayInstance.create(gunDisplay);
            } catch (IllegalArgumentException var2) {
                DeceasedCraftFixes.LOGGER.warn("{} display init read fail!", location, var2);
                return null;
            }
        });
        GUN_INDEX = new StandardLambdaMap<>(location -> {
            GunIndexPOJO pojo = TimelessAPI.getCommonGunIndex(location).map(CommonGunIndex::getPojo).orElse(null);
            try {
                ClientGunIndex index = ClientGunIndex.getInstance(pojo);
                ((ClientGunIndexExtender) index).dcfixes$setId(location.withSuffix("_display"));
                return index;
            } catch (IllegalArgumentException var2) {
                DeceasedCraftFixes.LOGGER.warn("{} index file read fail!", location, var2);
                return null;
            }
        });
        AMMO_INDEX = new StandardLambdaMap<>(location -> {
            AmmoIndexPOJO pojo = TimelessAPI.getCommonAmmoIndex(location).map(CommonAmmoIndex::getPojo).orElse(null);
            try {
                return ClientAmmoIndex.getInstance(pojo);
            } catch (IllegalArgumentException var2) {
                DeceasedCraftFixes.LOGGER.warn("{} index file read fail!", location, var2);
                return null;
            }
        });
        ATTACHMENT_INDEX = new StandardLambdaMap<>(location -> {
            AttachmentIndexPOJO pojo = TimelessAPI.getCommonAttachmentIndex(location).map(CommonAttachmentIndex::getPojo).orElse(null);
            try {
                return ClientAttachmentIndex.getInstance(location, pojo);
            } catch (IllegalArgumentException var2) {
                DeceasedCraftFixes.LOGGER.warn("{} index file read fail!", location, var2);
                return null;
            }
        });
        BLOCK_INDEX = new StandardLambdaMap<>(location -> {
            BlockIndexPOJO pojo = TimelessAPI.getCommonBlockIndex(location).map(CommonBlockIndex::getPojo).orElse(null);
            try {
                return ClientBlockIndex.getInstance(pojo);
            } catch (IllegalArgumentException var2) {
                DeceasedCraftFixes.LOGGER.warn("{} index file read fail!", location, var2);
                return null;
            }
        });
    }

    @Inject(
            method = {
                    "loadGunDisplay",
                    "loadGunIndex",
                    "loadAmmoIndex",
                    "loadAttachmentIndex",
                    "loadBlockIndex"
            },
            at = @At("HEAD"),
            cancellable = true
    )
    private static void cancelRegularLoading(CallbackInfo ci) {
        ci.cancel();
    }
}
