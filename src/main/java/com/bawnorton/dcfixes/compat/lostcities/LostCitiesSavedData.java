package com.bawnorton.dcfixes.compat.lostcities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class LostCitiesSavedData extends SavedData {
    public static final String ID = "dcfixes_lostcities";

    private final List<BlockPos> positions;

    public LostCitiesSavedData() {
        this.positions = new ArrayList<>();
    }

    private LostCitiesSavedData(List<BlockPos> positions) {
        this.positions = positions;
    }

    public static LostCitiesSavedData load(CompoundTag tag) {
        ListTag list = tag.getList("positions", Tag.TAG_LONG);
        List<BlockPos> positions = new ArrayList<>(list.size());
        for (Tag t : list) {
            positions.add(BlockPos.of(((LongTag) t).getAsLong()));
        }
        return new LostCitiesSavedData(positions);
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag list = new ListTag();
        for (BlockPos pos : positions) {
            list.add(LongTag.valueOf(pos.asLong()));
        }
        tag.put("positions", list);
        return tag;
    }

    public List<BlockPos> getPositions() {
        return positions;
    }

    public void update(Collection<BlockPos> current) {
        positions.clear();
        positions.addAll(current);
        setDirty();
    }
}
