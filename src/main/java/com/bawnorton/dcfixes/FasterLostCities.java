package com.bawnorton.dcfixes;

import com.bawnorton.dcfixes.mixin.accessor.WorldChunkAccessor;
import com.simibubi.create.AllBlocks;
import mcjty.lostcities.setup.ModSetup;
import mcjty.lostcities.varia.TodoQueue;
import mcjty.lostcities.worldgen.GlobalTodo;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.CommandBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.MobSpawnerLogic;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Todos moved to chunk-based queues.
 * @implNote World/Level interaction calls done here will cause a deadlock, chunk interaction only
 */
public final class FasterLostCities extends GlobalTodo {
    public static final ThreadLocal<Chunk> CHUNK = ThreadLocal.withInitial(() -> null);

    private final Map<ChunkPos, TodoQueue<Consumer<ServerWorld>>> todo = Collections.synchronizedMap(new HashMap<>());
    private final Map<ChunkPos, TodoQueue<Pair<BlockState, Identifier>>> todoSpawners = Collections.synchronizedMap(new HashMap<>());
    private final Map<ChunkPos, TodoQueue<Pair<BlockState, NbtCompound>>> todoBlockEntities = Collections.synchronizedMap(new HashMap<>());
    private final Map<ChunkPos, TodoQueue<BlockState>> todoPoi = Collections.synchronizedMap(new HashMap<>());

    public static FasterLostCities getData(ServerWorld world) {
        PersistentStateManager manager = world.getPersistentStateManager();
        return manager.getOrCreate(FasterLostCities::new, FasterLostCities::new, "FasterLostCities");
    }

    public FasterLostCities() {}

    public FasterLostCities(NbtCompound nbt) {
        NbtList spawners = nbt.getList("spawners", 10);

        for (NbtElement spawner : spawners) {
            NbtCompound spawnerTag = (NbtCompound) spawner;
            BlockPos pos = NbtHelper.toBlockPos(spawnerTag.getCompound("pos"));
            BlockState state = NbtHelper.toBlockState(spawnerTag.getCompound("state"));
            Identifier entity = new Identifier(spawnerTag.getString("entity"));
            addSpawnerTodo(pos, state, entity);
        }

        NbtList blockEntities = nbt.getList("blockentities", 10);
        for (NbtElement blockEntity : blockEntities) {
            NbtCompound blockEntityTag = (NbtCompound) blockEntity;
            BlockPos pos = NbtHelper.toBlockPos(blockEntityTag.getCompound("pos"));
            BlockState state = NbtHelper.toBlockState(blockEntityTag.getCompound("state"));
            NbtCompound tag = blockEntityTag.getCompound("tag");
            addBlockEntityTodo(pos, state, tag);
        }

        NbtList poi = nbt.getList("poi", 10);
        for (NbtElement p : poi) {
            NbtCompound pTag = (NbtCompound) p;
            BlockPos pos = NbtHelper.toBlockPos(pTag.getCompound("pos"));
            BlockState state = NbtHelper.toBlockState(pTag.getCompound("state"));
            addPoi(pos, state);
        }

        this.markDirty();
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtList spawners = new NbtList();
        this.todoSpawners.values().forEach(queue -> queue.forEach((pos, pair) -> {
            NbtCompound spawnerTag = new NbtCompound();
            spawnerTag.put("pos", NbtHelper.fromBlockPos(pos));
            spawnerTag.put("state", NbtHelper.fromBlockState(pair.getLeft()));
            spawnerTag.putString("entity", pair.getRight().toString());
            spawners.add(spawnerTag);
        }));
        nbt.put("spawners", spawners);
        NbtList blockEntities = new NbtList();
        this.todoBlockEntities.values().forEach(queue -> queue.forEach((pos, pair) -> {
            NbtCompound blockEntityTag = new NbtCompound();
            blockEntityTag.put("pos", NbtHelper.fromBlockPos(pos));
            blockEntityTag.put("state", NbtHelper.fromBlockState(pair.getLeft()));
            blockEntityTag.put("tag", pair.getRight());
            blockEntities.add(blockEntityTag);
        }));
        nbt.put("blockentities", blockEntities);
        NbtList poi = new NbtList();
        this.todoPoi.values().forEach(queue -> queue.forEach((pos, state) -> {
            NbtCompound pTag = new NbtCompound();
            pTag.put("pos", NbtHelper.fromBlockPos(pos));
            pTag.put("state", NbtHelper.fromBlockState(state));
            poi.add(pTag);
        }));
        nbt.put("poi", poi);
        return nbt;
    }

    public void addTodo(BlockPos pos, Consumer<ServerWorld> code) {
        todo.computeIfAbsent(new ChunkPos(pos), chunkPos -> new TodoQueue<>()).add(pos, code);
        markDirty();
    }

    public void addSpawnerTodo(BlockPos pos, BlockState spawnerState, Identifier randomEntity) {
        todoSpawners.computeIfAbsent(new ChunkPos(pos), chunkPos -> new TodoQueue<>()).add(pos, Pair.of(spawnerState, randomEntity));
        markDirty();
    }

    public void addBlockEntityTodo(BlockPos pos, BlockState state, NbtCompound tag) {
        todoBlockEntities.computeIfAbsent(new ChunkPos(pos), chunkPos -> new TodoQueue<>()).add(pos, Pair.of(state, tag));
        markDirty();
    }

    public void addPoi(BlockPos pos, BlockState state) {
        todoPoi.computeIfAbsent(new ChunkPos(pos), chunkPos -> new TodoQueue<>()).add(pos, state);
        markDirty();
    }

    public void executeAndClearTodo(ServerWorld world, Chunk chunk) {
        ChunkPos chunkPos = chunk.getPos();
        var todoQueue = todo.remove(chunkPos);
        var todoSpawnersQueue = todoSpawners.remove(chunkPos);
        var todoBlockEntitiesQueue = todoBlockEntities.remove(chunkPos);
        var todoPoiQueue = todoPoi.remove(chunkPos);
        if(todoQueue != null) {
            todoQueue.forEach((pos, code) -> attachData(() -> code.accept(world), chunk));
        }
        if(todoSpawnersQueue != null) {
            todoSpawnersQueue.forEach((pos, pair) -> attachData(() -> {
                BlockState spawnerState = pair.getLeft();
                Identifier randomEntity = pair.getRight();
                if (chunk.getBlockState(pos).getBlock() == spawnerState.getBlock()) {
                    chunk.setBlockState(pos, Blocks.AIR.getDefaultState(), false);
                    chunk.setBlockState(pos, spawnerState, false);

                    BlockEntity tileentity = chunk.getBlockEntity(pos);
                    if (tileentity instanceof MobSpawnerBlockEntity spawner) {
                        MobSpawnerLogic logic = spawner.getLogic();
                        //? if <=1.18.2 {
                        logic.setEntityId(ForgeRegistries.ENTITIES.getValue(randomEntity));
                        //?} else {
                        /*logic.setEntityId(ForgeRegistries.ENTITY_TYPES.getValue(randomEntity));
                        *///?}
                    } else if (tileentity != null) {
                        ModSetup.getLogger().error("The mob spawner at ({}, {}, {}) has a TileEntity of incorrect type {}!", pos.getX(), pos.getY(), pos.getZ(), tileentity.getClass().getName());
                    } else {
                        ModSetup.getLogger().error("The mob spawner at ({}, {}, {}) is missing its TileEntity!", pos.getX(), pos.getY(), pos.getZ());
                    }
                }
            }, chunk));
        }
        if(todoBlockEntitiesQueue != null) {
            todoBlockEntitiesQueue.forEach((pos, pair) -> attachData(() -> {
                NbtCompound tag = pair.getRight();
                if(chunk instanceof WorldChunk worldChunk) {
                    BlockState state = worldChunk.getBlockState(pos);
                    Block block = state.getBlock();
                    if(block instanceof BlockWithEntity blockWithEntity) {
                        // get the id from the block entity, don't assume it's correct
                        BlockEntity dummyBlockEntity = blockWithEntity.createBlockEntity(pos, state);
                        if(dummyBlockEntity != null) {
                            //? if <=1.18.2 {
                            Identifier blockId = ForgeRegistries.BLOCK_ENTITIES.getKey(dummyBlockEntity.getType());
                            //?} else {
                            /*Identifier blockId = ForgeRegistries.BLOCK_ENTITY_TYPES.getKey(dummyBlockEntity.getType());
                            *///?}
                            if(blockId != null) {
                                tag.putString("id", blockId.toString());
                            }
                        }
                    } else if (!tag.contains("id")) {
                        // get the id from the block
                        Identifier blockId = ForgeRegistries.BLOCKS.getKey(block);
                        if(blockId != null) {
                            tag.putString("id", blockId.toString());
                        }
                    }
                    if(block == AllBlocks.COGWHEEL.get() || block == AllBlocks.SHAFT.get()) {
                        // redudnant, and just spam the logs when trying to load
                        return;
                    }

                    // in world block entity
                    BlockEntity be = ((WorldChunkAccessor) worldChunk).callLoadBlockEntity(pos, tag);
                    if(be != null) {
                        // manually trigger block update
                        world.getChunkManager().markForUpdate(pos);
                        world.createAndScheduleBlockTick(pos, block, 1);
                    }
                }
            }, chunk));
        }
        if (todoPoiQueue != null) {
            todoPoiQueue.forEach((pos, state) -> attachData(() -> {
                if (world.getPointOfInterestStorage().getType(pos).isEmpty() && chunk.getBlockState(pos).getBlock() == state.getBlock()) {
                    chunk.setBlockState(pos, state, false);
                }
            }, chunk));
        }
        markDirty();
    }

    private void attachData(Runnable runnable, Chunk chunk) {
        CHUNK.set(chunk);
        runnable.run();
        CHUNK.remove();
    }
}
