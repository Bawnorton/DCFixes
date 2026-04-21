package com.bawnorton.dcfixes.client.compat.naturalist;

import com.bawnorton.dcfixes.client.compat.physics_mod.GeckoLibRagdollHook;
import com.bawnorton.dcfixes.client.compat.physics_mod.RagdollAssembler;
import com.starfish_studios.naturalist.common.entity.*;

public class NaturalistGeckoLibRagdollHook extends GeckoLibRagdollHook {
    public NaturalistGeckoLibRagdollHook() {
        register(Alligator.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("body", "head", "snout", "lower_jaw", "tail", "tail2")
                .connect("head", "snout", RagdollAssembler.Type.FIXED)
                .connect("head", "lower_jaw", RagdollAssembler.Type.FIXED)
                .connect("body", "head", RagdollAssembler.Type.LOOSE)
                .connect("tail", "tail2", RagdollAssembler.Type.LOOSE)
                .connect("body", "tail", RagdollAssembler.Type.LOOSE)
                .connect("right_arm", "right_hand", RagdollAssembler.Type.VISUAL_FIXED)
                .connect("left_arm", "left_hand", RagdollAssembler.Type.VISUAL_FIXED)
                .connect("right_leg", "right_foot", RagdollAssembler.Type.VISUAL_FIXED)
                .connect("left_leg", "left_foot", RagdollAssembler.Type.VISUAL_FIXED)
                .connect("body", "right_arm", RagdollAssembler.Type.LOOSE)
                .connect("body", "left_arm", RagdollAssembler.Type.LOOSE)
                .connect("body", "right_leg", RagdollAssembler.Type.LOOSE)
                .connect("body", "left_leg", RagdollAssembler.Type.LOOSE)
                .assemble(), (entities, entity) -> trimTo(entities, 24)
        );

        register(Bass.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("body")
                .connect("body", "head", RagdollAssembler.Type.LOOSE)
                .connect("body", "right_fin", RagdollAssembler.Type.VISUAL_FIXED)
                .connect("body", "left_fin", RagdollAssembler.Type.VISUAL_FIXED)
                .connect("body", "tail", RagdollAssembler.Type.VISUAL_FIXED)
                .assemble()
        );

        register(Bear.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                        .merge("body")
                        .connect("snout", "nose", RagdollAssembler.Type.FIXED)
                        .connect("head", "snout", RagdollAssembler.Type.FIXED)
                        .connect("head", "left_ear", RagdollAssembler.Type.FIXED)
                        .connect("head", "right_ear", RagdollAssembler.Type.FIXED)
                        .connect("body", "head", RagdollAssembler.Type.LOOSE)
                        .connect("body", "left_arm", RagdollAssembler.Type.LOOSE)
                        .connect("body", "right_arm", RagdollAssembler.Type.LOOSE)
                        .connect("body", "left_leg", RagdollAssembler.Type.LOOSE)
                        .connect("body", "right_leg", RagdollAssembler.Type.LOOSE)
                        .assemble(), (entities, entity) -> {
                    entities.remove(7);
                    entities.remove(7);
                }
        );

        register(Bird.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("head", "leftLeg")
                .connect("body", "head", RagdollAssembler.Type.LOOSE)
                .connect("body", "rightLeg", RagdollAssembler.Type.LOOSE)
                .connect("body", "leftLeg", RagdollAssembler.Type.LOOSE)
                .connect("body", "rightWing", RagdollAssembler.Type.LOOSE)
                .connect("body", "leftWing", RagdollAssembler.Type.LOOSE)
                .connect("body", "tail", RagdollAssembler.Type.VISUAL_FIXED)
                .assemble()
        );

        register(Boar.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("body", "head")
                .connect("head", "ear_right", RagdollAssembler.Type.FIXED)
                .connect("head", "ear_left", RagdollAssembler.Type.FIXED)
                .connect("body", "head", RagdollAssembler.Type.LOOSE)
                .connect("body", "left_arm", RagdollAssembler.Type.LOOSE)
                .connect("body", "right_arm", RagdollAssembler.Type.LOOSE)
                .connect("body", "left_leg", RagdollAssembler.Type.LOOSE)
                .connect("body", "right_leg", RagdollAssembler.Type.LOOSE)
                .assemble()
        );

        register(Butterfly.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .connect("main", "antennae", RagdollAssembler.Type.VISUAL_FIXED)
                .connect("main", "front_legs", RagdollAssembler.Type.VISUAL_FIXED)
                .connect("main", "back_legs", RagdollAssembler.Type.VISUAL_FIXED)
                .connect("main", "middle_legs", RagdollAssembler.Type.VISUAL_FIXED)
                .assemble()
        );

        register(Caterpillar.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .connect("head", "antennae", RagdollAssembler.Type.VISUAL_FIXED)
                .connect("chest", "butt", RagdollAssembler.Type.LOOSE)
                .connect("chest", "head", RagdollAssembler.Type.LOOSE)
                .assemble()
        );

        register(Catfish.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("body", "tail")
                .connect("head", "whiskers", RagdollAssembler.Type.VISUAL_FIXED)
                .connect("body", "head", RagdollAssembler.Type.LOOSE)
                .connect("body", "right_fin", RagdollAssembler.Type.VISUAL_FIXED)
                .connect("body", "left_fin", RagdollAssembler.Type.VISUAL_FIXED)
                .connect("body", "tail", RagdollAssembler.Type.LOOSE)
                .assemble()
        );

        register(Deer.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("body", "neck", "head")
                .connect("head", "left_antler", RagdollAssembler.Type.FIXED)
                .connect(4, 9, RagdollAssembler.Type.VISUAL_FIXED)
                .connect("head", "right_antler", RagdollAssembler.Type.FIXED)
                .connect(4, 7, RagdollAssembler.Type.VISUAL_FIXED)
                .connect("head", "right_ear", RagdollAssembler.Type.FIXED)
                .connect("head", "left_ear", RagdollAssembler.Type.FIXED)
                .connect("head", "beard", RagdollAssembler.Type.VISUAL_FIXED)
                .connect("body", "neck", RagdollAssembler.Type.LOOSE)
                .connect("neck", "head", RagdollAssembler.Type.LOOSE)
                .connect("body", "right_arm", RagdollAssembler.Type.LOOSE)
                .connect("body", "left_arm", RagdollAssembler.Type.LOOSE)
                .connect("body", "right_leg", RagdollAssembler.Type.LOOSE)
                .connect("body", "left_leg", RagdollAssembler.Type.LOOSE)
                .connect("body", "tail", RagdollAssembler.Type.LOOSE)
                .assemble()
        );

        register(Dragonfly.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("main")
                .connect("main", "right_front_wing", RagdollAssembler.Type.FIXED)
                .connect("main", "left_front_wing", RagdollAssembler.Type.FIXED)
                .connect("main", "right_back_wing", RagdollAssembler.Type.FIXED)
                .connect("main", "left_back_wing", RagdollAssembler.Type.FIXED)
                .connect("main", "front_legs", RagdollAssembler.Type.VISUAL_FIXED)
                .connect("main", "middle_legs", RagdollAssembler.Type.VISUAL_FIXED)
                .connect("main", "back_legs", RagdollAssembler.Type.VISUAL_FIXED)
                .assemble()
        );

        register(Duck.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("head")
                .connect(1, 4, RagdollAssembler.Type.FIXED)
                .connect("body", "head", RagdollAssembler.Type.LOOSE)
                .connect("body", "left_wing", RagdollAssembler.Type.LOOSE)
                .connect("body", "right_wing", RagdollAssembler.Type.LOOSE)
                .connect("body", "left_leg", RagdollAssembler.Type.LOOSE)
                .connect("body", "right_leg", RagdollAssembler.Type.LOOSE)
                .assemble()
        );

        register(Elephant.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("left_tusk", "right_tusk")
                .connect("head", "left_ear", RagdollAssembler.Type.LOOSE)
                .connect("head", "right_ear", RagdollAssembler.Type.LOOSE)
                .connect("head", "left_tusk", RagdollAssembler.Type.LOOSE)
                .connect("head", "right_tusk", RagdollAssembler.Type.LOOSE)
                .connect("trunk3", "trunk4", RagdollAssembler.Type.LOOSE)
                .connect("trunk2", "trunk3", RagdollAssembler.Type.LOOSE)
                .connect("trunk1", "trunk2", RagdollAssembler.Type.LOOSE)
                .connect("head", "trunk1", RagdollAssembler.Type.LOOSE)
                .connect("body", "head", RagdollAssembler.Type.LOOSE)
                .connect("body", "left_arm", RagdollAssembler.Type.LOOSE)
                .connect("body", "right_arm", RagdollAssembler.Type.LOOSE)
                .connect("body", "left_leg", RagdollAssembler.Type.LOOSE)
                .connect("body", "right_leg", RagdollAssembler.Type.LOOSE)
                .connect("body", "tail", RagdollAssembler.Type.VISUAL_FIXED)
                .assemble()
        );

        register(Firefly.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .connect("head", "antennae", RagdollAssembler.Type.VISUAL_FIXED)
                .connect("body", "head", RagdollAssembler.Type.LOOSE)
                .assemble(), (entities, entity) -> trimTo(entities, 5)
        );

        register(Giraffe.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("head")
                .connect(6, 7, RagdollAssembler.Type.VISUAL_FIXED)
                .connect(8, 9, RagdollAssembler.Type.VISUAL_FIXED)
                .connect("head", "leftHorn", RagdollAssembler.Type.FIXED)
                .connect("head", "rightHorn", RagdollAssembler.Type.FIXED)
                .connect("head", "snout", RagdollAssembler.Type.FIXED)
                .connect("neck", "head", RagdollAssembler.Type.LOOSE)
                .connect("body", "neck", RagdollAssembler.Type.LOOSE)
                .connect("body", "left_arm", RagdollAssembler.Type.LOOSE)
                .connect("body", "right_arm", RagdollAssembler.Type.LOOSE)
                .connect("body", "left_leg", RagdollAssembler.Type.LOOSE)
                .connect("body", "right_leg", RagdollAssembler.Type.LOOSE)
                .connect("body", "tail", RagdollAssembler.Type.VISUAL_FIXED)
                .assemble()
        );

        register(Hippo.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("topJaw", "botJaw")
                .connect("botJaw", "skinFlap", RagdollAssembler.Type.FIXED)
                .connect("topJaw", "botJaw", RagdollAssembler.Type.FIXED)
                .connect("body", "topJaw", RagdollAssembler.Type.LOOSE)
                .connect("body", "left_arm", RagdollAssembler.Type.LOOSE)
                .connect("body", "right_arm", RagdollAssembler.Type.LOOSE)
                .connect("body", "left_leg", RagdollAssembler.Type.LOOSE)
                .connect("body", "right_leg", RagdollAssembler.Type.LOOSE)
                .connect("body", "tail", RagdollAssembler.Type.VISUAL_FIXED)
                .assemble()
        );

        register(Lion.class, (ragdoll, entity) -> {
                    RagdollAssembler assembler = new RagdollAssembler(ragdoll)
                            .merge("tail2")
                            .connect("tail1", "tail2", RagdollAssembler.Type.LOOSE)
                            .connect("head_not_angry", "bone", RagdollAssembler.Type.FIXED)
                            .connect("head_not_angry", "bone2", RagdollAssembler.Type.FIXED)
                            .connect("head_not_angry", "head", RagdollAssembler.Type.FIXED)
                            .connect("body_male", "head_not_angry", RagdollAssembler.Type.LOOSE)
                            .connect("body_male", "tail1", RagdollAssembler.Type.LOOSE)
                            .connect("body_male", "left_arm", RagdollAssembler.Type.LOOSE)
                            .connect("body_male", "right_arm", RagdollAssembler.Type.LOOSE)
                            .connect("body_male", "left_leg", RagdollAssembler.Type.LOOSE)
                            .connect("body_male", "right_leg", RagdollAssembler.Type.LOOSE);
                    if (entity.hasMane()) {
                        assembler.connect("body_male", "mane", RagdollAssembler.Type.FIXED);
                    }
                    assembler.assemble();
                },
                (entities, entity) -> {
                    entities.remove(6);
                    entities.remove(6);
                    entities.remove(8);
                    if (entity.hasMane()) {
                        entities.remove(10);
                    }
                }
        );

        register(Lizard.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                        .merge("head")
                        .connect("head", "lower_jaw", RagdollAssembler.Type.FIXED)
                        .connect("body", "head", RagdollAssembler.Type.LOOSE)
                        .connect("body", "tail", RagdollAssembler.Type.LOOSE)
                        .connect("body", "left_arm", RagdollAssembler.Type.VISUAL_FIXED)
                        .connect("body", "right_arm", RagdollAssembler.Type.VISUAL_FIXED)
                        .connect("body", "left_leg", RagdollAssembler.Type.VISUAL_FIXED)
                        .connect("body", "right_leg", RagdollAssembler.Type.VISUAL_FIXED)
                        .assemble(), (entities, entity) -> {
                    entities.remove(9);
                    entities.remove(9);
                    entities.remove(9);
                    entities.remove(9);
                    entities.remove(9);
                    entities.remove(10);
                    entities.remove(10);
                    entities.remove(10);
                }
        );

        register(LizardTail.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge(RagdollAssembler.Type.VISUAL_FIXED, "root")
                .assemble()
        );

        register(Moose.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("body", "saddle", "head")
                .connect("head", "leftAntler", RagdollAssembler.Type.FIXED)
                .connect(8, 14, RagdollAssembler.Type.VISUAL_FIXED)
                .connect("head", "rightAntler", RagdollAssembler.Type.FIXED)
                .connect(8, 16, RagdollAssembler.Type.VISUAL_FIXED)
                .connect("head", "rightEar", RagdollAssembler.Type.FIXED)
                .connect("head", "leftEar", RagdollAssembler.Type.FIXED)
                .connect("head", "bell", RagdollAssembler.Type.FIXED)
                .connect("body", "head", RagdollAssembler.Type.LOOSE)
                .connect("body", "leftArm", RagdollAssembler.Type.LOOSE)
                .connect("body", "rightArm", RagdollAssembler.Type.LOOSE)
                .connect("body", "leftLeg", RagdollAssembler.Type.LOOSE)
                .connect("body", "rightLeg", RagdollAssembler.Type.LOOSE)
                .connect("body", "saddle", RagdollAssembler.Type.FIXED)
                .assemble()
        );

        register(Rhino.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("body", "head")
                .connect("head", "small_horn", RagdollAssembler.Type.FIXED)
                .connect("head", "big_horn", RagdollAssembler.Type.FIXED)
                .connect("head", "right_ear", RagdollAssembler.Type.FIXED)
                .connect("head", "left_ear", RagdollAssembler.Type.FIXED)
                .connect("body", "head", RagdollAssembler.Type.LOOSE)
                .connect("body", "left_arm", RagdollAssembler.Type.LOOSE)
                .connect("body", "right_arm", RagdollAssembler.Type.LOOSE)
                .connect("body", "left_leg", RagdollAssembler.Type.LOOSE)
                .connect("body", "right_leg", RagdollAssembler.Type.LOOSE)
                .assemble()
        );

        register(Snail.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("shell", "body", "right_eye", "left_eye")
                .connect("body", "right_eye", RagdollAssembler.Type.FIXED)
                .connect("body", "left_eye", RagdollAssembler.Type.FIXED)
                .assemble()
        );

        register(Snake.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .connect("head", "tongue", RagdollAssembler.Type.VISUAL_FIXED)
                .connect("neck", "head", RagdollAssembler.Type.LOOSE)
                .connect("tail", "neck", RagdollAssembler.Type.LOOSE)
                .connect("tail2", "tail", RagdollAssembler.Type.LOOSE)
                .connect("tail3", "tail2", RagdollAssembler.Type.LOOSE)
                .assemble()
        );

        register(Tortoise.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("body")
                .connect("body", "mask", RagdollAssembler.Type.FIXED)
                .connect("body", "head", RagdollAssembler.Type.LOOSE)
                .connect("body", "left_arm", RagdollAssembler.Type.LOOSE)
                .connect("body", "right_arm", RagdollAssembler.Type.LOOSE)
                .connect("body", "left_leg", RagdollAssembler.Type.LOOSE)
                .connect("body", "right_leg", RagdollAssembler.Type.LOOSE)
                .assemble()
        );

        register(Vulture.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("body", "head")
                .connect("left_wing", "left_wing_tip", RagdollAssembler.Type.FIXED)
                .connect("right_wing", "right_wing_tip", RagdollAssembler.Type.FIXED)
                .connect("body", "head", RagdollAssembler.Type.LOOSE)
                .connect("body", "left_wing", RagdollAssembler.Type.LOOSE)
                .connect("body", "right_wing", RagdollAssembler.Type.LOOSE)
                .connect("body", "tail", RagdollAssembler.Type.VISUAL_FIXED)
                .assemble()
        );
    }
}

