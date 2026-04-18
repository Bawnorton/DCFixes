package com.bawnorton.dcfixes.client.compat.deaceased;

import com.bawnorton.dcfixes.client.compat.physics_mod.GeckoLibRagdollHook;
import com.bawnorton.dcfixes.client.compat.physics_mod.RagdollAssembler;
import net.diebuddies.physics.PhysicsEntity;
import net.diebuddies.physics.ragdoll.Ragdoll;
import net.mcreator.deaceased.entity.*;

import java.util.List;
import java.util.function.Consumer;

public class DeaceasedGeckoLibRagdollHook extends GeckoLibRagdollHook {
    public DeaceasedGeckoLibRagdollHook() {
        register(BuggerEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("bone")
                .assemble()
        );

        register(FlayedmanheadEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                        .merge("bone", "body", "head")
                        .connect(2, 9, RagdollAssembler.Type.FIXED)
                        .connect(2, 10, RagdollAssembler.Type.FIXED)
                        .connect(2, 11, RagdollAssembler.Type.FIXED)
                        .connect(2, 12, RagdollAssembler.Type.FIXED)
                        .connect(13, 20, RagdollAssembler.Type.FIXED)
                        .connect(13, 21, RagdollAssembler.Type.FIXED)
                        .connect(13, 22, RagdollAssembler.Type.FIXED)
                        .connect(13, 23, RagdollAssembler.Type.FIXED)
                        .connect(13, 0, RagdollAssembler.Type.LOOSE)
                        .connect(2, 13, RagdollAssembler.Type.LOOSE)
                        .assemble(), (entities, entity) -> {
                    entities.remove(10);
                    entities.remove(11);
                    entities.remove(21);
                    entities.remove(23);
                }
        );

        Consumer<Ragdoll> flayedManAssembler = ragdoll -> new RagdollAssembler(ragdoll)
                .merge("bone")
                .connect(0, 8, RagdollAssembler.Type.FIXED)
                .connect(0, 7, RagdollAssembler.Type.FIXED)
                .connect(0, 10, RagdollAssembler.Type.FIXED)
                .connect(0, 9, RagdollAssembler.Type.FIXED)
                .assemble();
        Consumer<List<PhysicsEntity>> flayedManFilter = entities -> {
            entities.remove(0);
            entities.remove(8);
        };

        register(FlayedmansectionsEntity.class,
                (ragdoll, entity) -> flayedManAssembler.accept(ragdoll),
                (entities, entity) -> flayedManFilter.accept(entities)
        );

        register(FlayedmantailEntity.class,
                (ragdoll, entity) -> flayedManAssembler.accept(ragdoll),
                (entities, entity) -> flayedManFilter.accept(entities)
        );

        register(GuardianEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("head", "body", "pad6", "pad7", "pad4", "pad5", "pad2", "pad8", "pad9")
                .connect("body", "head", RagdollAssembler.Type.LOOSE)
                .connect("pad3", "pad10", RagdollAssembler.Type.FIXED)
                .connect("body", "pad2", RagdollAssembler.Type.VISUAL_FIXED)
                .connect("body", "pad3", RagdollAssembler.Type.VISUAL_FIXED)
                .connect("body", "pad4", RagdollAssembler.Type.VISUAL_FIXED)
                .connect("body", "pad5", RagdollAssembler.Type.VISUAL_FIXED)
                .connect("body", "pad6", RagdollAssembler.Type.VISUAL_FIXED)
                .connect("body", "pad7", RagdollAssembler.Type.VISUAL_FIXED)
                .connect("body", "pad8", RagdollAssembler.Type.VISUAL_FIXED)
                .connect("body", "pad9", RagdollAssembler.Type.VISUAL_FIXED)
                .assemble()
        );

        register(StunnerEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("bone", "body1")
                .merge(RagdollAssembler.Type.VISUAL_FIXED, "grid")
                .connect("body1", "bone2", RagdollAssembler.Type.FIXED)
                .connect("bone", "body1", RagdollAssembler.Type.LOOSE)
                .connect("head", "grid", RagdollAssembler.Type.VISUAL_FIXED)
                .connect("bone", "body0", RagdollAssembler.Type.FIXED)
                .connect("bone", "body1", RagdollAssembler.Type.LOOSE)
                .connect("bone", "bone3", RagdollAssembler.Type.FIXED)
                .connect("bone", "leg0", RagdollAssembler.Type.LOOSE)
                .connect("bone", "leg1", RagdollAssembler.Type.LOOSE)
                .connect("bone", "leg2", RagdollAssembler.Type.LOOSE)
                .connect("bone", "leg3", RagdollAssembler.Type.LOOSE)
                .connect("bone", "leg4", RagdollAssembler.Type.LOOSE)
                .connect("bone", "leg5", RagdollAssembler.Type.LOOSE)
                .connect("bone", "leg6", RagdollAssembler.Type.LOOSE)
                .connect("bone", "leg7", RagdollAssembler.Type.LOOSE)
                .assemble(), (entities, entity) -> trimTo(entities, 26)
        );

        register(ThediggerEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("leftleg", "rightleg", "body", "head", "rightarm", "leftarm")
                .connect("body", "leftleg", RagdollAssembler.Type.LOOSE)
                .connect("body", "rightleg", RagdollAssembler.Type.LOOSE)
                .connect("body", "head", RagdollAssembler.Type.LOOSE)
                .connect("body", "rightarm", RagdollAssembler.Type.LOOSE)
                .connect("body", "leftarm", RagdollAssembler.Type.LOOSE)
                .assemble(), (entities, entity) -> entities.remove(18)
        );

        register(ThefaceEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                        .merge("chest2", "chest1")
                        .assemble(), (entities, entity) -> {
                    entities.remove(0);
                    trimTo(entities, 18);
                }
        );
    }
}
