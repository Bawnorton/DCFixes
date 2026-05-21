package com.bawnorton.dcfixes.compat.immersive_engineering;

import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class IECompat {
    private LootItemFunctionType oreVeinMapType;

    public void registerOreVeinMap(LootItemFunctionType oreVeinMapType) {
        this.oreVeinMapType = oreVeinMapType;
    }

    public LootItemFunctionType getOreVeinMapType() {
        return oreVeinMapType;
    }
}
