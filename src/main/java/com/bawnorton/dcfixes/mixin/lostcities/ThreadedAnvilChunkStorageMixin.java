package com.bawnorton.dcfixes.mixin.lostcities;

import com.bawnorton.dcfixes.DeceasedCraftFixes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.datafixers.util.Either;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import java.util.concurrent.CompletableFuture;

@Mixin(ThreadedAnvilChunkStorage.class)
public abstract class ThreadedAnvilChunkStorageMixin {
    @Shadow @Final
    ServerWorld world;

    @ModifyReturnValue(
            method = "convertToFullChunk",
            at = @At("RETURN")
    )
    private CompletableFuture<Either<Chunk, ChunkHolder.Unloaded>> onConverted(CompletableFuture<Either<Chunk, ChunkHolder.Unloaded>> original) {
        return original.whenComplete((either, t) -> either.left().ifPresent(chunk -> DeceasedCraftFixes.onChunkLoaded(world, chunk)));
    }
}
