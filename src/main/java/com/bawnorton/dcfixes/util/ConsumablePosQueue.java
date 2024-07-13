package com.bawnorton.dcfixes.util;

import net.minecraft.util.math.BlockPos;
import java.util.ArrayDeque;
import java.util.function.BiConsumer;

// based on TodoQueue from LostCities
public final class ConsumablePosQueue<T> {
    private final ArrayDeque<Entry<T>> queue = new ArrayDeque<>();

    public void add(BlockPos pos, T data) {
        queue.add(new Entry<>(pos, data));
    }

    public void forEach(BiConsumer<BlockPos, T> consumer) {
        queue.forEach(entry -> consumer.accept(entry.pos, entry.data));
    }

    private record Entry<T>(BlockPos pos, T data) {}
}
