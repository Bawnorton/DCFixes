package com.bawnorton.dcfixes.mixin.minecraft.accessor;

import com.mojang.datafixers.util.Either;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.concurrent.CompletableFuture;

@Mixin(ServerChunkCache.class)
public interface ServerChunkCacheAccessor {
    @Invoker("getChunkFutureMainThread")
    CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> dcfixes$getChunkFutureMainThread(int chunkX, int chunkZ, ChunkStatus chunkStatus, boolean load);
}
