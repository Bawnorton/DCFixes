package com.bawnorton.dcfixes.mixin.chunky;

import com.bawnorton.dcfixes.extend.MinecraftServerExtender;
import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.util.Unit;
import net.minecraft.world.level.ChunkPos;
import org.popcraft.chunky.platform.ForgeWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.BiConsumer;

@IfModLoaded("chunky")
@Mixin(value = ForgeWorld.class, remap = false)
abstract class ForgeWorldMixin {
    @Shadow
    @Final
    private static TicketType<Unit> CHUNKY;

    @Shadow
    @Final
    private ServerLevel world;

    @ModifyArg(
            method = "getChunkAtAsync",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/concurrent/CompletableFuture;whenCompleteAsync(Ljava/util/function/BiConsumer;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"
            )
    )
    private <T> BiConsumer<? super T, ? super Throwable> removeUnknownChunks(BiConsumer<? super T, ? super Throwable> action, @Local(name = "serverChunkCache") ServerChunkCache serverChunkCache, @Local(name = "chunkPos") ChunkPos chunkPos) {
        return (ignored, throwable) -> {
            serverChunkCache.removeRegionTicket(CHUNKY, chunkPos, 0, Unit.INSTANCE);
            ((MinecraftServerExtender) world.getServer()).dcfixes$markChunkSystemHousekeeping();
        };
    }
}
