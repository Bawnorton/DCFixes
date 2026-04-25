package com.bawnorton.dcfixes.client.mixin.lrtactical;

import com.bawnorton.dcfixes.client.mixin.tacz.JsonDataManagerMixin;
import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import me.xjqsh.lrtactical.client.resource.display.ThrowableDisplayInstance;
import me.xjqsh.lrtactical.client.resource.manager.ThrowableDisplayManager;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;

import java.io.Reader;

@IfModLoaded("lrtactical")
@MixinEnvironment("client")
@Mixin(ThrowableDisplayManager.class)
abstract class ThrowableDisplayManagerMixin extends JsonDataManagerMixin<ThrowableDisplayInstance> {
    @Override
    public ThrowableDisplayInstance dcfixes$parseReader(Reader reader, ResourceLocation location) {
        ThrowableDisplayInstance.ThrowableDisplay pojo = getGson().fromJson(reader, ThrowableDisplayInstance.ThrowableDisplay.class);
        return ThrowableDisplayInstance.create(pojo, location);
    }
}
