package com.bawnorton.dcfixes.mixin.globalgamerules;

import com.bawnorton.dcfixes.mixin.accessor.GameRulesAcessor;
import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import se.gory_moon.globalgamerules.Config;

import java.util.Map;

@Mixin(Config.Common.class)
abstract class Config$CommonMixin {
    @WrapOperation(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/GameRules;visitGameRuleTypes(Lnet/minecraft/world/level/GameRules$GameRuleTypeVisitor;)V"
            )
    )
    private <T extends GameRules.Value<T>> void useCMESafeIteration(GameRules.GameRuleTypeVisitor visitor, Operation<Void> original) {
        Map<GameRules.Key<T>, GameRules.Type<T>> gameRuleTypes = ImmutableMap.copyOf(GameRulesAcessor.dcfixes$GAME_RULE_TYPES());
        for (Map.Entry<GameRules.Key<T>, GameRules.Type<T>> entry : gameRuleTypes.entrySet()) {
            visitor.visit(entry.getKey(), entry.getValue());
        }
    }
}
