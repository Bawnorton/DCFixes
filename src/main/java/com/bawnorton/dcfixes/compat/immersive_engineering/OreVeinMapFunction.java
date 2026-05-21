package com.bawnorton.dcfixes.compat.immersive_engineering;

import blusunrize.immersiveengineering.api.excavator.ExcavatorHandler;
import blusunrize.immersiveengineering.api.excavator.MineralVein;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import com.bawnorton.dcfixes.DeceasedCraftFixes;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ColumnPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapDecoration.Type;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class OreVeinMapFunction extends LootItemConditionalFunction {
    protected OreVeinMapFunction(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        if(!stack.is(Items.MAP)) {
            return stack;
        }

        Level world = context.getLevel();
        Vec3 pos = context.getParamOrNull(LootContextParams.ORIGIN);
        if(pos == null) return stack;

        BlockPos blockPos = BlockPos.containing(pos);
        List<MineralVein> veins = ExcavatorHandler.findVeinsForVillager(world, blockPos, 256L, List.of());
        if (veins.isEmpty()) {
            return stack;
        }

        RandomSource random = world.getRandom();
        int select = random.nextInt(Math.min(10, veins.size()));
        MineralVein vein = veins.get(select);
        ColumnPos veinPos = vein.getPos();
        BlockPos veinBlockPos = new BlockPos(veinPos.x(), 64, veinPos.z());
        ItemStack selling = MapItem.create(world, veinBlockPos.getX(), veinBlockPos.getZ(), (byte) 1, true, true);
        MapItem.lockMap(world, selling);
        MapItemSavedData.addTargetDecoration(selling, veinBlockPos, "ie:coresample_treasure", Type.RED_X);
        selling.setHoverName(Component.translatable("item.immersiveengineering.map_orevein"));
        ItemNBTHelper.setLore(selling, Component.translatable(vein.getMineral(world).getTranslationKey()));
        return selling;
    }

    @Override
    public LootItemFunctionType getType() {
        return DeceasedCraftFixes.getCompat().getImmersiveEngineeringCompat().orElseThrow().getOreVeinMapType();
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<OreVeinMapFunction> {
        @Override
        public OreVeinMapFunction deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootItemCondition[] conditions) {
            return new OreVeinMapFunction(conditions);
        }
    }
}
