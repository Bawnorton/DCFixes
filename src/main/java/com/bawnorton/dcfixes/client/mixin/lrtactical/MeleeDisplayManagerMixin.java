package com.bawnorton.dcfixes.client.mixin.lrtactical;

import com.bawnorton.dcfixes.client.mixin.tacz.JsonDataManagerMixin;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import me.xjqsh.lrtactical.client.resource.display.MeleeDisplayInstance;
import me.xjqsh.lrtactical.client.resource.manager.MeleeDisplayManager;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;

import java.io.Reader;

@MixinEnvironment("client")
@Mixin(MeleeDisplayManager.class)
abstract class MeleeDisplayManagerMixin extends JsonDataManagerMixin<MeleeDisplayInstance> {
    @Override
    public MeleeDisplayInstance dcfixes$parseReader(Reader reader, ResourceLocation location) {
        MeleeDisplayInstance.MeleeDisplay pojo = getGson().fromJson(reader, MeleeDisplayInstance.MeleeDisplay.class);
        return MeleeDisplayInstance.create(pojo, location);
    }
}
