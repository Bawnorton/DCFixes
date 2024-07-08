package com.bawnorton.dcfixes;

import mcjty.lostcities.setup.Config;
import mcjty.lostcities.setup.ModSetup;
import mcjty.lostcities.varia.TodoQueue;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.MobSpawnerLogic;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class FasterLostCities {
    public static final ThreadLocal<Chunk> CHUNK = ThreadLocal.withInitial(() -> null);

    private static final Map<ChunkPos, TodoQueue<Consumer<ServerWorld>>> todo = new HashMap<>();
    private static final Map<ChunkPos, TodoQueue<Pair<BlockState, Identifier>>> todoSpawners = new HashMap<>();
    private static final Map<ChunkPos, TodoQueue<Pair<BlockState, NbtCompound>>> todoBlockEntities = new HashMap<>();
    private static final Map<ChunkPos, TodoQueue<BlockState>> todoPoi = new HashMap<>();

    public static void addTodo(BlockPos pos, Consumer<ServerWorld> code) {
        todo.computeIfAbsent(new ChunkPos(pos), chunkPos -> new TodoQueue<>()).add(pos, code);
    }

    public static void addSpawnerTodo(BlockPos pos, BlockState spawnerState, Identifier randomEntity) {
        todoSpawners.computeIfAbsent(new ChunkPos(pos), chunkPos -> new TodoQueue<>()).add(pos, Pair.of(spawnerState, randomEntity));
    }

    public static void addBlockEntityTodo(BlockPos pos, BlockState state, NbtCompound tag) {
        todoBlockEntities.computeIfAbsent(new ChunkPos(pos), chunkPos -> new TodoQueue<>()).add(pos, Pair.of(state, tag));
    }

    public static void addPoi(BlockPos pos, BlockState state) {
        todoPoi.computeIfAbsent(new ChunkPos(pos), chunkPos -> new TodoQueue<>()).add(pos, state);
    }

    public static void executeAndClearTodo(ServerWorld world, Chunk chunk) {
        ChunkPos chunkPos = chunk.getPos();
        int todoSize = Config.TODO_QUEUE_SIZE.get();
        todo.computeIfPresent(chunkPos, (c, queue) -> {
            queue.forEach(todoSize, (pos, code) -> attachData(() -> code.accept(world), chunk));
            return null;
        });
        todoSpawners.computeIfPresent(chunkPos, (c, queue) -> {
            queue.forEach(todoSize, (pos, pair) -> attachData(() -> {
                BlockState spawnerState = pair.getLeft();
                Identifier randomEntity = pair.getRight();
                if (chunk.getBlockState(pos).getBlock() == spawnerState.getBlock()) {
                    chunk.setBlockState(pos, Blocks.AIR.getDefaultState(), false);
                    chunk.setBlockState(pos, spawnerState, false);

                    BlockEntity tileentity = chunk.getBlockEntity(pos);
                    if (tileentity instanceof MobSpawnerBlockEntity spawner) {
                        MobSpawnerLogic logic = spawner.getLogic();
                        logic.setEntityId(ForgeRegistries.ENTITIES.getValue(randomEntity));
                    } else if (tileentity != null) {
                        ModSetup.getLogger().error("The mob spawner at ({}, {}, {}) has a TileEntity of incorrect type {}!", pos.getX(), pos.getY(), pos.getZ(), tileentity.getClass().getName());
                    } else {
                        ModSetup.getLogger().error("The mob spawner at ({}, {}, {}) is missing its TileEntity!", pos.getX(), pos.getY(), pos.getZ());
                    }
                }
            }, chunk));
            return null;
        });
        todoBlockEntities.computeIfPresent(chunkPos, (c, queue) -> {
            queue.forEach(todoSize, (pos, pair) -> attachData(() -> {
                NbtCompound tag = pair.getRight();
                BlockEntity be = chunk.getBlockEntity(pos);
                if (be != null) {
                    be.readNbt(tag);
                }
            }, chunk));
            return null;
        });
        todoPoi.computeIfPresent(chunkPos, (c, queue) -> {
            queue.forEach(todoSize, (pos, state) -> attachData(() -> {
                if (world.getPointOfInterestStorage().getType(pos).isEmpty() && chunk.getBlockState(pos)
                        .getBlock() == state.getBlock()) {
                    chunk.setBlockState(pos, state, false);
                }
            }, chunk));
            return null;
        });
    }

    private static void attachData(Runnable runnable, Chunk chunk) {
        CHUNK.set(chunk);
        runnable.run();
        CHUNK.remove();
    }
}
