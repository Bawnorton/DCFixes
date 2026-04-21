package com.bawnorton.dcfixes.client.mixin.tacz;


import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.tacz.guns.api.client.animation.statemachine.LuaAnimationStateMachine;
import com.tacz.guns.client.animation.statemachine.GunAnimationStateContext;
import com.tacz.guns.client.resource.ClientAssetsManager;
import com.tacz.guns.client.resource.GunDisplayInstance;
import com.tacz.guns.client.resource.pojo.display.gun.GunDisplay;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@MixinEnvironment("client")
@Mixin(value = GunDisplayInstance.class, remap = false)
abstract class GunDisplayInstanceMixin {
    @Shadow
    @Nullable
    private LuaTable stateMachineParam;

    @Shadow
    private String thirdPersonAnimation;

    @Shadow
    @Nullable
    private ResourceLocation playerAnimator3rd;

    @Shadow
    private boolean is3rdFixedHand;
    @Shadow
    private LuaAnimationStateMachine<GunAnimationStateContext> animationStateMachine;
    @Unique
    private GunDisplay dcfixes$displayHolder;

    @Shadow
    protected abstract void checkAnimation(GunDisplay display);

    @WrapOperation(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/tacz/guns/client/resource/GunDisplayInstance;checkAnimation(Lcom/tacz/guns/client/resource/pojo/display/gun/GunDisplay;)V"
            )
    )
    private void checkWithoutAnimationBuilding(GunDisplayInstance instance, GunDisplay display, Operation<Void> original) {
        dcfixes$displayHolder = display;
        ResourceLocation stateMachineLocation = display.getStateMachineLocation();
        if (stateMachineLocation == null) {
            stateMachineLocation = ResourceLocation.fromNamespaceAndPath("tacz", "default_state_machine");
        }

        LuaTable script = ClientAssetsManager.INSTANCE.getScript(stateMachineLocation);
        if (script == null) {
            throw new IllegalArgumentException("statemachine not found: " + stateMachineLocation);
        } else {
            Map<String, Object> params = display.getStateMachineParam();
            if (params != null) {
                this.stateMachineParam = new LuaTable();

                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    this.stateMachineParam.set(entry.getKey(), CoerceJavaToLua.coerce(entry.getValue()));
                }
            }

            if (StringUtils.isNoneBlank(display.getThirdPersonAnimation())) {
                this.thirdPersonAnimation = display.getThirdPersonAnimation();
            }

            if (display.getPlayerAnimator3rd() != null) {
                this.playerAnimator3rd = display.getPlayerAnimator3rd();
                this.is3rdFixedHand = display.is3rdFixedHand();
            }
        }
    }

    @ModifyReturnValue(
            method = "getAnimationStateMachine",
            at = @At("RETURN")
    )
    private LuaAnimationStateMachine<GunAnimationStateContext> createAnimationStateMachineIfNeeded(LuaAnimationStateMachine<GunAnimationStateContext> original) {
        if (original == null && dcfixes$displayHolder != null) {
            checkAnimation(dcfixes$displayHolder);
            dcfixes$displayHolder = null;
            original = animationStateMachine;
        }
        return original;
    }
}
