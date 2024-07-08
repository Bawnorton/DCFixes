package com.bawnorton.dcfixes.mixin;

import com.bawnorton.dcfixes.DeceasedCraftFixes;
import com.bawnorton.dcfixes.FasterLostCities;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class WorldMixin {
    @Inject(method = "getChunk(IILnet/minecraft/world/chunk/ChunkStatus;Z)Lnet/minecraft/world/chunk/Chunk;", at = @At("HEAD"), cancellable = true)
    private void getFasterLostCitiesWorldGenChunk(int chunkX, int chunkZ, ChunkStatus leastStatus, boolean create, CallbackInfoReturnable<Chunk> cir) {
        if(DeceasedCraftFixes.READING.get()) return;

        Chunk chunk = FasterLostCities.CHUNK.get();
        if(chunk == null) return;

        ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);
        if (chunk.getPos().equals(chunkPos)) {
            cir.setReturnValue(chunk);
        }
    }
}
