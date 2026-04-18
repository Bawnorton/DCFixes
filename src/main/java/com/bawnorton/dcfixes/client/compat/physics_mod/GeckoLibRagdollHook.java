package com.bawnorton.dcfixes.client.compat.physics_mod;

import net.diebuddies.physics.PhysicsEntity;
import net.diebuddies.physics.ragdoll.Ragdoll;
import net.diebuddies.physics.ragdoll.RagdollHook;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.animatable.GeoEntity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GeckoLibRagdollHook implements RagdollHook {
    private final Map<Class<? extends Entity>, InternalHandler<?>> handlers = new LinkedHashMap<>();

    protected static void connect(Ragdoll ragdoll, int target, boolean fixed, int... sources) {
        for (int source : sources) {
            ragdoll.addConnection(source, target, fixed);
        }
    }

    protected static void connectVisually(Ragdoll ragdoll, int target, boolean fixed, int... sources) {
        for (int source : sources) {
            ragdoll.addConnection(source, target, fixed, true);
        }
    }

    protected static void connectRange(Ragdoll ragdoll, int target, boolean fixed, int start, int end) {
        for (int i = start; i <= end; i++) {
            ragdoll.addConnection(i, target, fixed);
        }
    }

    protected static void trimTo(List<?> list, int maxSize) {
        while (list.size() > maxSize) {
            list.remove(list.size() - 1);
        }
    }

    public <T extends Entity> void register(Class<T> clazz, RagdollMapper<T> mapper, RagdollFilterer<T> filterer) {
        handlers.put(clazz, new InternalHandler<>(mapper, filterer));
    }

    public <T extends Entity> void register(Class<T> clazz, RagdollMapper<T> mapper) {
        register(clazz, mapper, null);
    }

    public <T extends Entity> void registerFilter(Class<T> clazz, RagdollFilterer<T> filterer) {
        register(clazz, null, filterer);
    }

    @SuppressWarnings("unchecked")
    private InternalHandler<Entity> getHandler(Class<?> entityClass) {
        for (Map.Entry<Class<? extends Entity>, InternalHandler<?>> entry : handlers.entrySet()) {
            if (entry.getKey().isAssignableFrom(entityClass)) {
                return (InternalHandler<Entity>) entry.getValue();
            }
        }
        return null;
    }

    @Override
    public void map(Ragdoll ragdoll, Entity entity, EntityModel entityModel) {
        if (!(entity instanceof GeoEntity)) return;

        InternalHandler<Entity> handler = getHandler(entity.getClass());
        if (handler != null && handler.mapper != null) {
            handler.mapper.map(ragdoll, entity);
        }
    }

    @Override
    public void filterCuboidsFromEntities(List<PhysicsEntity> blockifiedEntity, Entity entity, EntityModel entityModel) {
        if (!(entity instanceof GeoEntity)) return;

        InternalHandler<Entity> handler = getHandler(entity.getClass());
        if (handler != null && handler.filterer != null) {
            handler.filterer.filter(blockifiedEntity, entity);
        }
    }

    @FunctionalInterface
    public interface RagdollMapper<T extends Entity> {
        void map(Ragdoll ragdoll, T entity);
    }

    @FunctionalInterface
    public interface RagdollFilterer<T extends Entity> {
        void filter(List<PhysicsEntity> entities, T entity);
    }

    private record InternalHandler<T extends Entity>(RagdollMapper<T> mapper, RagdollFilterer<T> filterer) {
    }
}