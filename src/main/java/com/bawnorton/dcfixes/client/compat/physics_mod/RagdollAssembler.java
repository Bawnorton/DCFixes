package com.bawnorton.dcfixes.client.compat.physics_mod;

import com.bawnorton.dcfixes.DeceasedCraftFixes;
import com.bawnorton.dcfixes.client.extend.PhysicsEntityExtender;
import net.diebuddies.physics.PhysicsEntity;
import net.diebuddies.physics.ragdoll.Ragdoll;
import net.diebuddies.physics.ragdoll.RagdollJoint;
import software.bernie.geckolib.cache.object.GeoBone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RagdollAssembler {
    private boolean debug;

    private final Ragdoll ragdoll;
    private final Map<String, List<Integer>> boneIndices = new HashMap<>();
    private final Map<Integer, String> indexMap = new HashMap<>();
    private final List<Runnable> queue = new ArrayList<>();
    private final Map<Integer, Integer> parentMap = new HashMap<>();

    public enum Type {
        FIXED(true, false),
        LOOSE(false, false),
        VISUAL_FIXED(true, true),
        VISUAL_LOOSE(false, true);

        final boolean isFixed;
        final boolean isVisual;

        Type(boolean isFixed, boolean isVisual) {
            this.isFixed = isFixed;
            this.isVisual = isVisual;
        }
    }

    public RagdollAssembler(Ragdoll ragdoll) {
        this.ragdoll = ragdoll;
        
        for (int i = 0; i < ragdoll.bodies.size(); i++) {
            PhysicsEntityExtender extender = (PhysicsEntityExtender) ragdoll.bodies.get(i);
            GeoBone bone = extender.dcfixes$getGeoBone();
            if (bone != null) {
                boneIndices.computeIfAbsent(bone.getName(), k -> new ArrayList<>()).add(i);
            }
        }
    }

    public RagdollAssembler debug() {
        this.debug = true;
        boneIndices.forEach((key, value) -> {
            for (Integer i : value) {
                indexMap.put(i, key);
            }
        });
        return this;
    }

    public RagdollAssembler merge(String... partNames) {
        return merge(Type.FIXED, partNames);
    }

    public RagdollAssembler merge(RagdollAssembler.Type type, String... partNames) {
        for (String partName : partNames) {
            queue.add(() -> {
                List<Integer> indices = boneIndices.get(partName);
                if (indices == null || indices.size() <= 1) {
                    if(debug) {
                        DeceasedCraftFixes.LOGGER.warn("Ragdoll Merge Warning: Part '{}' has {} associated bodies. Merge skipped.", partName, indices == null ? "n/a" : indices.size());
                    }
                    return;
                }

                int target = indices.get(0);
                for (int i = 1; i < indices.size(); i++) {
                    validateAndConnect(indices.get(i), target, type.isFixed, type.isVisual);
                }
            });
        }
        return this;
    }

    public RagdollAssembler connect(String targetName, String sourceName, Type type) {
        queue.add(() -> {
            List<Integer> targets = boneIndices.get(targetName);
            List<Integer> sources = boneIndices.get(sourceName);
            if (targets == null || sources == null || targets.isEmpty() || sources.isEmpty()) {
                if (debug) {
                    DeceasedCraftFixes.LOGGER.warn("Ragdoll Connect Warning: Target part '{}' has {} bodies, Source part '{}' has {} bodies. Connection skipped.", targetName, targets == null ? "n/a" : targets.size(), sourceName, sources == null ? "n/a" : sources.size());
                }
                return;
            }

            validateAndConnect(sources.get(0), targets.get(0), type.isFixed, type.isVisual);
        });
        return this;
    }

    public RagdollAssembler connect(int targetIdx, int sourceIdx, Type type) {
        queue.add(() -> validateAndConnect(sourceIdx, targetIdx, type.isFixed, type.isVisual));
        return this;
    }

    private void validateAndConnect(int source, int target, boolean fixed, boolean visual) {
        if(debug) {
            DeceasedCraftFixes.LOGGER.info(
                    "Source {} ({}) -> Target {} ({}) - f:{} v:{}",
                    source,
                    indexMap.get(source),
                    target,
                    indexMap.get(target),
                    fixed,
                    visual
            );
            if (parentMap.containsKey(source)) {
                int existingTarget = parentMap.get(source);
                if (existingTarget != target) {
                    throw new IllegalStateException(String.format("Ragdoll Constraint Violation: Index %d is already connected to %d. Cannot connect to %d.", source, existingTarget, target));
                }
                return;
            }

            if (wouldCreateCycle(source, target)) {
                throw new IllegalStateException(String.format("Ragdoll Cycle Detected: Connecting %d to %d would create a circular dependency.", source, target));
            }

            parentMap.put(source, target);
        }
        ragdoll.addConnection(source, target, fixed, visual);
    }

    private boolean wouldCreateCycle(int source, int target) {
        Integer current = target;
        while (current != null) {
            if (current == source) return true;
            current = parentMap.get(current);
        }
        return false;
    }

    public void assemble() {
        if(debug) {
            for (int i = 0; i < ragdoll.bodies.size(); i++) {
                PhysicsEntity physicsEntity = ragdoll.bodies.get(i);
                PhysicsEntityExtender extender = (PhysicsEntityExtender) physicsEntity;
                extender.dcfixes$setRagdollIndex(i);

                DeceasedCraftFixes.LOGGER.info("Body {}: {}", i, indexMap.get(i));
            }
        }
        for (Runnable action : queue) {
            action.run();
        }
    }
}