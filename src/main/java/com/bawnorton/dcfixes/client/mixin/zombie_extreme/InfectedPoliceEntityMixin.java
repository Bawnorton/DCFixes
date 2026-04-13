package com.bawnorton.dcfixes.client.mixin.zombie_extreme;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import zombie_extreme.entity.InfectedPoliceEntity;

@MixinEnvironment("client")
@Mixin(InfectedPoliceEntity.class)
abstract class InfectedPoliceEntityMixin {
    @Definition(id = "setAndContinue", method = "Lsoftware/bernie/geckolib/core/animation/AnimationState;setAndContinue(Lsoftware/bernie/geckolib/core/animation/RawAnimation;)Lsoftware/bernie/geckolib/core/object/PlayState;")
    @Expression("?.setAndContinue(?.?(''))")
    @WrapOperation(
            method = "movementPredicate",
            at = @At("MIXINEXTRAS:EXPRESSION"),
            remap = false
    )
    private PlayState dont(AnimationState<InfectedPoliceEntity> instance, RawAnimation animation, Operation<PlayState> original) {
        return PlayState.CONTINUE;
    }
}
