package com.bawnorton.dcfixes.client.mixin.physics_mod;

import com.bawnorton.dcfixes.client.extend.HumanoidModelExtender;
import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@IfModLoaded("physicsmod")
@MixinEnvironment("client")
@Mixin(HumanoidModel.class)
abstract class HumanoidModelMixin implements HumanoidModelExtender {
    @Unique
    private ModelPart dcfixes$root;

    @Inject(
            method = "<init>(Lnet/minecraft/client/model/geom/ModelPart;Ljava/util/function/Function;)V",
            at = @At("TAIL")
    )
    private void attachRoot(ModelPart root, Function renderType, CallbackInfo ci) {
        dcfixes$setRoot(root);
    }

    @Override
    public void dcfixes$setRoot(ModelPart root) {
        this.dcfixes$root = root;
    }

    @Override
    public ModelPart dcfixes$getRoot() {
        return dcfixes$root;
    }
}
