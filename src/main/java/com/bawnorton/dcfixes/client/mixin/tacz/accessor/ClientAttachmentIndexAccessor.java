package com.bawnorton.dcfixes.client.mixin.tacz.accessor;

import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.tacz.guns.client.resource.index.ClientAttachmentIndex;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@IfModLoaded("tacz")
@MixinEnvironment("client")
@Mixin(value = ClientAttachmentIndex.class, remap = false)
public interface ClientAttachmentIndexAccessor {
    @Accessor("modelTexture")
    void dcfixes$modelTexture(ResourceLocation texture);
}
