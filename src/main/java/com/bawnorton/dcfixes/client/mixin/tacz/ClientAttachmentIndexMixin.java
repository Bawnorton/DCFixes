package com.bawnorton.dcfixes.client.mixin.tacz;

import com.bawnorton.dcfixes.client.extend.ClientAttachmentIndexExtender;
import com.bawnorton.dcfixes.client.mixin.accessor.ClientAttachmentIndexAccessor;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.tacz.guns.client.model.BedrockAttachmentModel;
import com.tacz.guns.client.resource.index.ClientAttachmentIndex;
import com.tacz.guns.client.resource.pojo.display.attachment.AttachmentDisplay;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@MixinEnvironment("client")
@Mixin(value = ClientAttachmentIndex.class, remap = false)
abstract class ClientAttachmentIndexMixin implements ClientAttachmentIndexExtender {
    @Shadow
    @Nullable
    private BedrockAttachmentModel attachmentModel;
    @Unique
    private AttachmentDisplay dcfixes$displayHolder;

    @Shadow
    private static void checkTextureAndModel(AttachmentDisplay display, ClientAttachmentIndex index) {
    }

    @WrapOperation(
            method = "getInstance",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/tacz/guns/client/resource/index/ClientAttachmentIndex;checkTextureAndModel(Lcom/tacz/guns/client/resource/pojo/display/attachment/AttachmentDisplay;Lcom/tacz/guns/client/resource/index/ClientAttachmentIndex;)V"
            )
    )
    private static void checkWithoutAnimationBuilding(AttachmentDisplay display, ClientAttachmentIndex index, Operation<Void> original) {
        ((ClientAttachmentIndexAccessor) index).dcfixes$modelTexture(display.getTexture());
        ((ClientAttachmentIndexExtender) index).dcfixes$setDisplayHolder(display);
    }

    @Override
    public void dcfixes$setDisplayHolder(AttachmentDisplay display) {
        dcfixes$displayHolder = display;
    }

    @ModifyReturnValue(
            method = "getAttachmentModel()Lcom/tacz/guns/client/model/BedrockAttachmentModel;",
            at = @At("RETURN")
    )
    private BedrockAttachmentModel createModelIfNeeded(BedrockAttachmentModel original) {
        if (original == null && dcfixes$displayHolder != null) {
            checkTextureAndModel(dcfixes$displayHolder, (ClientAttachmentIndex) (Object) this);
            original = attachmentModel;
            dcfixes$displayHolder = null;
        }
        return original;
    }
}
