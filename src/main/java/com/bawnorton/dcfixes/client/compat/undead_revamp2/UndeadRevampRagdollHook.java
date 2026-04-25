package com.bawnorton.dcfixes.client.compat.undead_revamp2;

import com.bawnorton.dcfixes.client.compat.physics_mod.DCFixesRagdollHook;
import com.bawnorton.dcfixes.client.compat.physics_mod.RagdollAssembler;
import net.diebuddies.physics.ragdoll.Ragdoll;
import net.mcreator.undeadrevamp.entity.*;

import java.util.function.Consumer;

public class UndeadRevampRagdollHook extends DCFixesRagdollHook {
    public UndeadRevampRagdollHook() {
        Consumer<Ragdoll> suckerRagdollAssembler = ragdoll -> new RagdollAssembler(ragdoll)
                .merge("head")
                .connect("head", "head2", RagdollAssembler.Type.VISUAL_FIXED)
                .connect("head", "head3", RagdollAssembler.Type.VISUAL_FIXED)
                .connect("head", "head4", RagdollAssembler.Type.VISUAL_FIXED)
                .connect("head", "tongue", RagdollAssembler.Type.FIXED)
                .assemble();

        register(BigsuckerEntity.class, (ragdoll, entity) -> suckerRagdollAssembler.accept(ragdoll),
                (entities, entity) -> trimTo(entities, 8));

        register(SuckerEntity.class, (ragdoll, entity) -> suckerRagdollAssembler.accept(ragdoll));

        register(BomberEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("head", "rightArm")
                .connect("body", "rightArm", RagdollAssembler.Type.LOOSE)
                .connect("body", "leftArm", RagdollAssembler.Type.LOOSE)
                .connect("body", "rightLeg", RagdollAssembler.Type.LOOSE)
                .connect("body", "leftLeg", RagdollAssembler.Type.LOOSE)
                .connect("body", "head", RagdollAssembler.Type.LOOSE)
                .assemble(), (entities, entity) -> entities.subList(3, 11).clear());

        register(LecheryEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                        .merge("bod2", "arm1", "head2", "arm2", "bone")
                        .connect("bod2", "arm1", RagdollAssembler.Type.LOOSE)
                        .connect("bod2", "arm2", RagdollAssembler.Type.LOOSE)
                        .connect("bod2", "head2", RagdollAssembler.Type.LOOSE)
                        .connect("bone2", "fleg1", RagdollAssembler.Type.LOOSE)
                        .connect("bone2", "fleg2", RagdollAssembler.Type.LOOSE)
                        .connect("bone", "bone7", RagdollAssembler.Type.FIXED)
                        .connect("bone2", "bone", RagdollAssembler.Type.LOOSE)
                        .connect("bone2", "bod2", RagdollAssembler.Type.LOOSE)
                        .assemble(),
                (entities, entity) -> trimTo(entities, 22));

        register(TheMoonflowerEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("head")
                .connect("leftarm", 13, RagdollAssembler.Type.FIXED)
                .connect("leftarm", 12, RagdollAssembler.Type.FIXED)
                .connect("leftarm2", 10, RagdollAssembler.Type.FIXED)
                .connect("leftarm2", 9, RagdollAssembler.Type.FIXED)
                .connect("head", 6, RagdollAssembler.Type.FIXED)
                .connect("head", 7, RagdollAssembler.Type.FIXED)
                .connect("body", "head", RagdollAssembler.Type.LOOSE)
                .connect("body", "leftarm", RagdollAssembler.Type.LOOSE)
                .connect("body", "leftarm2", RagdollAssembler.Type.LOOSE)
                .connect("body", "rightLeg", RagdollAssembler.Type.LOOSE)
                .connect("body", "leftLeg", RagdollAssembler.Type.LOOSE)
                .assemble(), (entities, entity) -> trimTo(entities, 14));

        register(ThebeartamerEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("head", "RightArm", "hammer", "Leftarmdis", "LeftArm", "RightArm", "Body")
                .connect("Leftarmdis", "Leftarm", RagdollAssembler.Type.FIXED)
                .connect("Body", "head", RagdollAssembler.Type.LOOSE)
                .connect("Body", "RightArm", RagdollAssembler.Type.LOOSE)
                .connect("Body", "LeftArmdis", RagdollAssembler.Type.LOOSE)
                .connect("Body", "RightLeg", RagdollAssembler.Type.LOOSE)
                .connect("Body", "LeftLeg", RagdollAssembler.Type.LOOSE)
                .assemble());

        register(ThewolfEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("head", "RightArm", "hammer", "LeftArm", "Body")
                .connect("Body", "jacket", RagdollAssembler.Type.FIXED)
                .connect("Body", "head", RagdollAssembler.Type.LOOSE)
                .connect("Body", "RightArm", RagdollAssembler.Type.LOOSE)
                .connect("Body", "LeftArm", RagdollAssembler.Type.LOOSE)
                .connect("Body", "RightLeg", RagdollAssembler.Type.LOOSE)
                .connect("Body", "LeftLeg", RagdollAssembler.Type.LOOSE)
                .connect("hammer", "blade", RagdollAssembler.Type.FIXED)
                .assemble());

        register(ThedungeonEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("LeftLeg", "RightLeg", "Body", "LeftArm", "RightArm", "Head")
                .connect("Body", "Head", RagdollAssembler.Type.LOOSE)
                .connect("Body", "LeftArm", RagdollAssembler.Type.LOOSE)
                .connect("Body", "RightArm", RagdollAssembler.Type.LOOSE)
                .connect("Body", "LeftLeg", RagdollAssembler.Type.LOOSE)
                .connect("Body", "RightLeg", RagdollAssembler.Type.LOOSE)
                .assemble(), (entities, entity) -> trimTo(entities, 12));

        register(ThegliterEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("head", "cube_r1")
                .connect("head", "cube_r1", RagdollAssembler.Type.FIXED)
                .connect("head", "hair1", RagdollAssembler.Type.LOOSE)
                .connect("head", "hair2", RagdollAssembler.Type.LOOSE)
                .connect("head", "hair3", RagdollAssembler.Type.LOOSE)
                .connect(25, "tumour", RagdollAssembler.Type.FIXED)
                .connect(25, "tumour2", RagdollAssembler.Type.FIXED)
                .connect(25, "tumour3", RagdollAssembler.Type.FIXED)
                .connect(25, "head", RagdollAssembler.Type.LOOSE)
                .connect(25, "rightLeg", RagdollAssembler.Type.LOOSE)
                .connect(25, "leftLeg", RagdollAssembler.Type.LOOSE)
                .mergeOn(25, "body", RagdollAssembler.Type.VISUAL_FIXED)
                .assemble(), (entities, entity) -> trimTo(entities, 39));

        register(TheheavyEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("body", "head", "rightArm", "rightbow", "leftArm", "leftLeg", "rightLeg")
                .connect("head", "cube_r1", RagdollAssembler.Type.FIXED)
                .connect("head", "cube_r2", RagdollAssembler.Type.FIXED)
                .connect("rightArm", "pit_r2", RagdollAssembler.Type.FIXED)
                .connect("rightArm", "rightbow", RagdollAssembler.Type.LOOSE)
                .connect("leftArm", "pit_r1", RagdollAssembler.Type.FIXED)
                .connect("leftArm", "rightbow2", RagdollAssembler.Type.LOOSE)
                .connect("body", "head", RagdollAssembler.Type.LOOSE)
                .connect("body", "rightArm", RagdollAssembler.Type.LOOSE)
                .connect("body", "leftArm", RagdollAssembler.Type.LOOSE)
                .connect("body", "rightLeg", RagdollAssembler.Type.LOOSE)
                .connect("body", "leftLeg", RagdollAssembler.Type.LOOSE)
                .assemble(), (entities, entity) -> trimTo(entities, 25));

        register(ThehorrorsEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("head")
                .connect("body", "head", RagdollAssembler.Type.LOOSE)
                .connect("body", "rightArm", RagdollAssembler.Type.LOOSE)
                .connect("body", "leftArm", RagdollAssembler.Type.LOOSE)
                .connect("body", "rightLeg", RagdollAssembler.Type.LOOSE)
                .connect("body", "leftLeg", RagdollAssembler.Type.LOOSE)
                .assemble());

        register(ThehunterEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("LeftArm", "RightArm", "eye", "tentacle1", "tentacle3")
                .connect(14, "eye", RagdollAssembler.Type.FIXED)
                .connect(14, "tentacle1", RagdollAssembler.Type.LOOSE)
                .connect(14, "tentacle3", RagdollAssembler.Type.LOOSE)
                .connect("Body", 14, RagdollAssembler.Type.LOOSE)
                .connect("Body", "wing", RagdollAssembler.Type.VISUAL_FIXED)
                .connect("Body", "wing2", RagdollAssembler.Type.VISUAL_FIXED)
                .connect("Body", "wing3", RagdollAssembler.Type.VISUAL_FIXED)
                .connect("Body", "wing4", RagdollAssembler.Type.VISUAL_FIXED)
                .connect("Body", "LeftArm", RagdollAssembler.Type.LOOSE)
                .connect("Body", "RightArm", RagdollAssembler.Type.LOOSE)
                .connect("Body", "LeftLeg", RagdollAssembler.Type.LOOSE)
                .connect("Body", "RightLeg", RagdollAssembler.Type.LOOSE)
                .assemble(), (entities, entity) -> trimTo(entities, 24));

        register(TheimmortalEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("head", "rightarm", "leftarm")
                .connect("head", "nose", RagdollAssembler.Type.FIXED)
                .connect("head", "neck", RagdollAssembler.Type.FIXED)
                .connect("body", "head", RagdollAssembler.Type.LOOSE)
                .connect("body", "rightarm", RagdollAssembler.Type.LOOSE)
                .connect("body", "leftarm", RagdollAssembler.Type.LOOSE)
                .connect("body", "leg0", RagdollAssembler.Type.LOOSE)
                .connect("body", "leg1", RagdollAssembler.Type.LOOSE)
                .connect("body", "bode", RagdollAssembler.Type.FIXED)
                .assemble());

        register(ThelurkerEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("armleft1", "rigbod", "head", "armright1")
                .connect("armleft2", "clawleft1", RagdollAssembler.Type.FIXED)
                .connect("armleft2", "clawleft2", RagdollAssembler.Type.FIXED)
                .connect("armleft2", "clawleft3", RagdollAssembler.Type.FIXED)
                .connect("armleft1", "armleft2", RagdollAssembler.Type.LOOSE)
                .connect("armright2", "clawright1", RagdollAssembler.Type.FIXED)
                .connect("armright2", "clawright2", RagdollAssembler.Type.FIXED)
                .connect("armright2", "clawright3", RagdollAssembler.Type.FIXED)
                .connect("armright1", "armright2", RagdollAssembler.Type.LOOSE)
                .connect("rigbod", "head", RagdollAssembler.Type.LOOSE)
                .connect("rigbod", "armleft1", RagdollAssembler.Type.LOOSE)
                .connect("rigbod", "armright1", RagdollAssembler.Type.LOOSE)
                .assemble(), (entities, entity) -> trimTo(entities, 20));

        register(TheposessiveEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("abducter", "body", "head", "bone3", "bone4", "shell1", "shell2", "bone2", "bone")
                .assemble(), (entities, entity) -> trimTo(entities, 88));

        register(ThepregnantEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("breath1", "breath2", "leftArm", "head", "body")
                .connect("breath2", "leftArm", RagdollAssembler.Type.LOOSE)
                .connect("breath1", "rightArm", RagdollAssembler.Type.LOOSE)
                .connect("body", "head", RagdollAssembler.Type.LOOSE)
                .connect("body", "rightLeg", RagdollAssembler.Type.LOOSE)
                .connect("body", "leftleg", RagdollAssembler.Type.LOOSE)
                .assemble());

        register(TherabidusEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("upperBody", "head", "bone", "leg1", "leg0")
                .connect("hindleg2", "second2", RagdollAssembler.Type.FIXED)
                .connect("leg1", "hindleg2", RagdollAssembler.Type.FIXED)
                .connect("hindleg", "second", RagdollAssembler.Type.FIXED)
                .connect("leg0", "hindleg", RagdollAssembler.Type.FIXED)
                .connect("body", "leg1", RagdollAssembler.Type.LOOSE)
                .connect("body", "leg0", RagdollAssembler.Type.LOOSE)
                .connect("head", "eye", RagdollAssembler.Type.FIXED)
                .connect("head", "eye2", RagdollAssembler.Type.FIXED)
                .connect("upperBody", "bone", RagdollAssembler.Type.LOOSE)
                .connect("upperBody", "head", RagdollAssembler.Type.LOOSE)
                .connect("body", "tail", RagdollAssembler.Type.LOOSE)
                .connect("body", "upperBody", RagdollAssembler.Type.LOOSE)
                .assemble());

        register(TherodEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("rod", "head", "rightArm")
                .connect("head", "rod", RagdollAssembler.Type.FIXED)
                .connect("body", "blackrobe", RagdollAssembler.Type.FIXED)
                .connect("body", "rightArm", RagdollAssembler.Type.LOOSE)
                .connect("body", "leftArm", RagdollAssembler.Type.LOOSE)
                .connect("body", "rightLeg", RagdollAssembler.Type.LOOSE)
                .connect("body", "leftLeg", RagdollAssembler.Type.LOOSE)
                .assemble(), (entities, entity) -> trimTo(entities, 15));

        register(TheskeeperEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("Body")
                .connect("helmet", "bone", RagdollAssembler.Type.FIXED)
                .connect("Body", "helmet", RagdollAssembler.Type.LOOSE)
                .connect("Body", "RightArm", RagdollAssembler.Type.LOOSE)
                .connect("Body", "LeftArm", RagdollAssembler.Type.LOOSE)
                .connect("Body", "RightLeg", RagdollAssembler.Type.LOOSE)
                .connect("Body", "LeftLeg", RagdollAssembler.Type.LOOSE)
                .assemble());

        register(ThesmokerEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("wheatsack", "farmer", "cube_r1", "head")
                .connect("body", "head", RagdollAssembler.Type.LOOSE)
                .connect("body", 14, RagdollAssembler.Type.LOOSE)
                .connect("body", "rightLeg", RagdollAssembler.Type.LOOSE)
                .connect("body", "leftLeg", RagdollAssembler.Type.LOOSE)
                .assemble());

        registerFilter(ThesomnolenceEntity.class, (entities, entity) -> entities.clear());

        register(ThespitterEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("body")
                .connect("head", "neck", RagdollAssembler.Type.FIXED)
                .connect("head", "ephungus_r1", RagdollAssembler.Type.FIXED)
                .connect("body", "head", RagdollAssembler.Type.LOOSE)
                .connect("body", "rightLeg", RagdollAssembler.Type.LOOSE)
                .connect("body", "leftLeg", RagdollAssembler.Type.LOOSE)
                .assemble());

        register(TheswarmerEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("helmet", "RightArm", "Body")
                .connect("Body", "helmet", RagdollAssembler.Type.LOOSE)
                .connect("Body", "RightArm", RagdollAssembler.Type.LOOSE)
                .connect("Body", "LeftArm", RagdollAssembler.Type.LOOSE)
                .connect("Body", "RightLeg", RagdollAssembler.Type.LOOSE)
                .connect("Body", "LeftLeg", RagdollAssembler.Type.LOOSE)
                .assemble());

        registerFilter(CloggerEntity.class, (entities, entity) -> entities.clear());
        registerFilter(DeadcloggerEntity.class, (entities, entity) -> entities.clear());

        Consumer<Ragdoll> bidyRagdollAssmbler = ragdoll -> new RagdollAssembler(ragdoll)
                .connect("body", "blob1", RagdollAssembler.Type.FIXED)
                .connect("body", "blob2", RagdollAssembler.Type.FIXED)
                .connect("body", "blobl2", RagdollAssembler.Type.FIXED)
                .connect("body", "rightleg", RagdollAssembler.Type.LOOSE)
                .connect("body", "leftleg", RagdollAssembler.Type.LOOSE)
                .connect("body", "rightarm", RagdollAssembler.Type.LOOSE)
                .connect("body", "leftarm", RagdollAssembler.Type.LOOSE)
                .connect("body", "head2", RagdollAssembler.Type.LOOSE)
                .assemble();

        register(ThebidyEntity.class, (ragdoll, entity) -> bidyRagdollAssmbler.accept(ragdoll));
        register(ThebidyupsideEntity.class, (ragdoll, entity) -> bidyRagdollAssmbler.accept(ragdoll));

        registerFilter(TheordureEntity.class, (entities, entity) -> entities.clear());

        register(ThespectreEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("head", "body", "rightArm", "leftArm")
                .connect("body", "cube_r1", RagdollAssembler.Type.FIXED)
                .connect("body", "head", RagdollAssembler.Type.LOOSE)
                .connect("body", "rightArm", RagdollAssembler.Type.LOOSE)
                .connect("body", "leftArm", RagdollAssembler.Type.LOOSE)
                .connect("body", "rightLeg", RagdollAssembler.Type.LOOSE)
                .connect("body", "leftLeg", RagdollAssembler.Type.LOOSE)
                .assemble(), (entities, entity) -> trimTo(entities, 18));
    }
}

