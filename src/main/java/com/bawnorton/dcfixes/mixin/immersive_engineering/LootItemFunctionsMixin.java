package com.bawnorton.dcfixes.mixin.immersive_engineering;

import com.bawnorton.dcfixes.DeceasedCraftFixes;
import com.bawnorton.dcfixes.compat.immersive_engineering.IECompat;
import com.bawnorton.dcfixes.compat.immersive_engineering.OreVeinMapFunction;
import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import org.spongepowered.asm.mixin.Mixin;

@IfModLoaded("immersiveengineering")
@Mixin(LootItemFunctions.class)
abstract class LootItemFunctionsMixin {
    static {
        IECompat compat = DeceasedCraftFixes.getCompat()
                .getImmersiveEngineeringCompat()
                .orElseThrow();
        compat.registerOreVeinMap(Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, DeceasedCraftFixes.rl("ore_vein_map"), new LootItemFunctionType(new OreVeinMapFunction.Serializer())));
    }
}
