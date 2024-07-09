package com.bawnorton.dcfixes.mixin.accessor;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(WorldChunk.class)
public interface WorldChunkAccessor {
    @Invoker
    BlockEntity callLoadBlockEntity(BlockPos pos, NbtCompound nbt);
}
