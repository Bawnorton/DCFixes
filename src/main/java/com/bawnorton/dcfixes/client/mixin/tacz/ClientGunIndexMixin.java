package com.bawnorton.dcfixes.client.mixin.tacz;

import com.bawnorton.dcfixes.client.extend.ClientGunIndexExtender;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.tacz.guns.client.resource.ClientIndexManager;
import com.tacz.guns.client.resource.GunDisplayInstance;
import com.tacz.guns.client.resource.index.ClientGunIndex;
import com.tacz.guns.client.resource.pojo.display.gun.GunDisplay;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@MixinEnvironment("client")
@Mixin(value = ClientGunIndex.class, remap = false)
abstract class ClientGunIndexMixin implements ClientGunIndexExtender {
    @Unique
    private ResourceLocation dcfixes$id;

    @Override
    public void dcfixes$setId(ResourceLocation id) {
        this.dcfixes$id = id;
    }

    @WrapOperation(
            method = "getInstance",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/tacz/guns/client/resource/GunDisplayInstance;create(Lcom/tacz/guns/client/resource/pojo/display/gun/GunDisplay;)Lcom/tacz/guns/client/resource/GunDisplayInstance;"
            )
    )
    private static GunDisplayInstance useLazyLoadedDisplayInstance(GunDisplay display, Operation<GunDisplayInstance> original) {
        return null;
    }

    @ModifyReturnValue(
            method = "getDefaultDisplay",
            at = @At("RETURN")
    )
    private GunDisplayInstance useLazyLoadedDisplayInstance(GunDisplayInstance original) {
        return ClientIndexManager.GUN_DISPLAY.get(dcfixes$id);
    }
}
