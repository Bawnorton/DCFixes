package com.bawnorton.dcfixes.client.mixin.accessor;

import com.tacz.guns.client.resource.index.ClientAttachmentIndex;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@MixinEnvironment("client")
@Mixin(value = ClientAttachmentIndex.class, remap = false)
public interface ClientAttachmentIndexAccessor {
    @Accessor("modelTexture")
    void dcfixes$modelTexture(ResourceLocation texture);
}
