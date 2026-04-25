package com.bawnorton.dcfixes.mixin.globalgamerules.accessor;

import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(GameRules.class)
public interface GameRulesAcessor {
    @Accessor("GAME_RULE_TYPES")
    static <T extends GameRules.Value<T>> Map<GameRules.Key<T>, GameRules.Type<T>> dcfixes$GAME_RULE_TYPES() {
        throw new AssertionError();
    }
}
