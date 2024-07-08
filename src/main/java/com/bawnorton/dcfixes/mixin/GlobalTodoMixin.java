package com.bawnorton.dcfixes.mixin;

import com.bawnorton.dcfixes.FasterLostCities;
import mcjty.lostcities.worldgen.GlobalTodo;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import java.util.function.Consumer;

/**
 * @author bawnorton
 * @see FasterLostCities
 */
@SuppressWarnings("OverwriteAuthorRequired")
@Mixin(GlobalTodo.class)
public abstract class GlobalTodoMixin {

    @Overwrite
    public void executeAndClearTodo(ServerWorld world) {
    }

    @Overwrite
    public void addTodo(BlockPos pos, Consumer<ServerWorld> code) {
        FasterLostCities.addTodo(pos, code);
    }

    @Overwrite
    public void addSpawnerTodo(BlockPos pos, BlockState spawnerState, Identifier randomEntity) {
        FasterLostCities.addSpawnerTodo(pos, spawnerState, randomEntity);
    }

    @Overwrite
    public void addBlockEntityTodo(BlockPos pos, BlockState state, NbtCompound tag) {
        FasterLostCities.addBlockEntityTodo(pos, state, tag);
    }

    @Overwrite
    public void addPoi(BlockPos pos, BlockState state) {
        FasterLostCities.addPoi(pos, state);
    }
}
