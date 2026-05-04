package com.bawnorton.dcfixes.compat.immersive_vehicles;

import com.bawnorton.dcfixes.DeceasedCraftFixes;
import com.bawnorton.dcfixes.mixin.immersive_vehicles.accessor.WrapperWorldAccessor;
import com.bawnorton.dcfixes.mixin.minecraft.accessor.ServerChunkCacheAccessor;
import com.mojang.datafixers.util.Either;
import minecrafttransportsimulator.baseclasses.Point3D;
import minecrafttransportsimulator.entities.components.AEntityA_Base;
import minecrafttransportsimulator.entities.components.AEntityB_Existing;
import minecrafttransportsimulator.entities.components.AEntityF_Multipart;
import minecrafttransportsimulator.entities.instances.APart;
import minecrafttransportsimulator.entities.instances.EntityPlayerGun;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ImmersiveVehiclesCompat {
    private final Map<AEntityA_Base, AEntityF_Multipart<?>> partCache = new WeakHashMap<>();
    private final Map<AEntityF_Multipart<?>, IVVehicle> vehicleCache = new WeakHashMap<>();

    private long currentTick = 0;

    public void onTickStart() {
        currentTick++;
    }

    public Consumer<AEntityA_Base> filterTickingEntities(Consumer<AEntityA_Base> action) {
        return entity -> {
            if(entity instanceof EntityPlayerGun) {
                action.accept(entity);
                return;
            }
            long start = System.nanoTime();
            AEntityF_Multipart<?> owner = resolveOwner(entity);
            Point3D tickingPos = getTickingPos(entity, owner);

            WrapperWorldAccessor accessor = (WrapperWorldAccessor) entity.world;
            Level level = accessor.dcfixes$world();

            if (owner == null) {
                if (level instanceof ServerLevel serverLevel) {
                    ServerChunkCache chunkSource = serverLevel.getChunkSource();
                    int x = SectionPos.blockToSectionCoord(tickingPos.x);
                    int z = SectionPos.blockToSectionCoord(tickingPos.z);
                    CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> chunkFuture = ((ServerChunkCacheAccessor) chunkSource).dcfixes$getChunkFutureMainThread(x, z, ChunkStatus.FULL, false);
                    if (!chunkFuture.isDone()) return;

                    if(serverLevel.isPositionEntityTicking(BlockPos.containing(tickingPos.x, tickingPos.y, tickingPos.z))) {
                        action.accept(entity);
                    }
                } else if (level.isClientSide()) {
                    if (level.isLoaded(BlockPos.containing(tickingPos.x, tickingPos.y, tickingPos.z))) {
                        action.accept(entity);
                    }
                } else {
                    action.accept(entity);
                }
            } else {
                IVVehicle vehicle = vehicleCache.computeIfAbsent(owner, IVVehicle::create);
                if (level instanceof ServerLevel serverLevel) {
                    if (vehicle.shouldTickServer(tickingPos, serverLevel, currentTick)) {
                        action.accept(entity);
                    }
                } else if (level.isClientSide()) {
                    if (vehicle.shouldTickClient(tickingPos, level, currentTick)) {
                        action.accept(entity);
                    }
                } else {
                    action.accept(entity);
                }
            }
            long end = System.nanoTime() - start;
            if(end > 10_000_000) {
                DeceasedCraftFixes.LOGGER.warn("IVCompat: Ticking for entity {} at {} took {}ms", entity, tickingPos, end * 1e-6);
            }
        };
    }

    private static Point3D getTickingPos(AEntityA_Base entity, AEntityF_Multipart<?> owner) {
        Point3D tickingPos = null;
        if (owner == null) {
            if(entity instanceof AEntityB_Existing existingEntity) {
                tickingPos = existingEntity.position;
            }
        } else {
            Point3D globalCenter = owner.encompassingBox.globalCenter;
            if (globalCenter.isZero()) {
                tickingPos = owner.position;
            } else {
                tickingPos = globalCenter;
            }
        }
        if (tickingPos == null) {
            tickingPos = new Point3D(0, 0, 0);
        }
        return tickingPos;
    }

    private AEntityF_Multipart<?> resolveOwner(AEntityA_Base entity) {
        if (entity instanceof AEntityF_Multipart<?> root) {
            return root;
        }
        return partCache.computeIfAbsent(entity, this::findRootOwner);
    }

    private AEntityF_Multipart<?> findRootOwner(AEntityA_Base entity) {
        AEntityA_Base current = entity;
        int depth = 0;

        while (current instanceof APart part && depth < 10) {
            if (part.vehicleOn != null) {
                return part.vehicleOn;
            }

            current = part.masterEntity;
            depth++;
        }
        return null;
    }
}