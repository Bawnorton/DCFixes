package com.bawnorton.dcfixes.mixin.chunky;

import com.bawnorton.dcfixes.extend.MinecraftServerExtender;
import com.bawnorton.dcfixes.mixin.chunky.accessor.ChunkMapAccessor;
import com.bawnorton.dcfixes.mixin.chunky.accessor.ServerLevelAccessor;
import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;

@IfModLoaded("chunky")
@Mixin(MinecraftServer.class)
abstract class MinecraftServerMixin implements MinecraftServerExtender {
    @Shadow
    public abstract Iterable<ServerLevel> getAllLevels();

    @Unique
    private final AtomicBoolean dcfixes$needChunkSystemHousekeeping = new AtomicBoolean(false);

    @Inject(
            method = "tickChildren",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerConnectionListener;tick()V"
            )
    )
    private void tickPaused(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        this.dcfixes$runChunkSystemHousekeeping(hasTimeLeft);
    }

    @Override
    public void dcfixes$runChunkSystemHousekeeping(BooleanSupplier haveTime) {
        if(dcfixes$needChunkSystemHousekeeping.compareAndSet(true, false)) {
            for (ServerLevel level : getAllLevels()) {
                ((ChunkMapAccessor) level.getChunkSource().chunkMap).dcfixes$tick(haveTime);
                ((ServerLevelAccessor) level).dcfixes$entityManager().tick();
            }
        }
    }

    @Override
    public void dcfixes$markChunkSystemHousekeeping() {
        dcfixes$needChunkSystemHousekeeping.set(true);
    }
}
