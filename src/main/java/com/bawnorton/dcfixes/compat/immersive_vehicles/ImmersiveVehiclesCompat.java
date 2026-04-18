package com.bawnorton.dcfixes.compat.immersive_vehicles;

import com.bawnorton.dcfixes.mixin.accessor.WrapperWorldAccessor;
import minecrafttransportsimulator.baseclasses.Point3D;
import minecrafttransportsimulator.entities.components.AEntityA_Base;
import minecrafttransportsimulator.entities.components.AEntityF_Multipart;
import minecrafttransportsimulator.entities.instances.APart;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.WeakHashMap;
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
            AEntityF_Multipart<?> owner = resolveOwner(entity);

            if (owner == null) {
                action.accept(entity);
                return;
            }

            Point3D globalCenter = owner.encompassingBox.globalCenter;
            if (globalCenter.isZero()) {
                action.accept(entity);
                return;
            }

            WrapperWorldAccessor accessor = (WrapperWorldAccessor) entity.world;
            Level level = accessor.dcfixes$world();

            IVVehicle vehicle = vehicleCache.computeIfAbsent(owner, IVVehicle::create);
            if (level instanceof ServerLevel serverLevel) {
                if (vehicle.shouldTickServer(globalCenter, serverLevel, currentTick)) {
                    action.accept(entity);
                }
            } else if (level.isClientSide()) {
                if (vehicle.shouldTickClient(globalCenter, level, currentTick)) {
                    action.accept(entity);
                }
            } else {
                action.accept(entity);
            }
        };
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