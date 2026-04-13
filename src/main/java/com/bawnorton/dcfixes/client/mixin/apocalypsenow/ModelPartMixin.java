package com.bawnorton.dcfixes.client.mixin.apocalypsenow;

import com.bawnorton.dcfixes.client.extend.ModelPartExtender;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@MixinEnvironment("client")
@Mixin(ModelPart.class)
abstract class ModelPartMixin implements ModelPartExtender {
    @Unique
    private ModelLayerLocation dcfixes$location;

    @Override
    public void dcfixes$setLocation(ModelLayerLocation location) {
        this.dcfixes$location = location;
    }

    @Override
    public ModelLayerLocation dcfixes$getLocation() {
        return this.dcfixes$location;
    }
}
