package com.bawnorton.dcfixes.client.compat.physics_mod;

import com.starfish_studios.naturalist.common.entity.*;
import net.diebuddies.physics.PhysicsEntity;
import net.diebuddies.physics.ragdoll.Ragdoll;
import net.diebuddies.physics.ragdoll.RagdollHook;
import net.mcreator.deaceased.entity.*;
import net.mcreator.undeadrevamp.entity.*;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.animatable.GeoEntity;
import zombie_extreme.entity.*;

import java.util.*;
import java.util.function.Consumer;

public class GeckoLibRagdollHook implements RagdollHook {
    private final Map<Class<? extends Entity>, InternalHandler<?>> handlers = new LinkedHashMap<>();

    public GeckoLibRagdollHook() {
        register(BigsuckerEntity.class, (ragdoll, entity) -> {
            connect(ragdoll, 1, true, 0);
            connect(ragdoll, 1, false, 3);
            connectVisually(ragdoll, 1, true, 2, 7, 4, 5, 6);
        }, (entities, entity) -> trimTo(entities, 4));

        register(SuckerEntity.class, (ragdoll, entity) -> {
            connect(ragdoll, 1, true, 0);
            connect(ragdoll, 1, false, 3);
            connectVisually(ragdoll, 1, true, 2, 7, 4, 5, 6);
        });

        register(BomberEntity.class, (ragdoll, entity) -> {
            connectRange(ragdoll, 3, true, 5, 7);
            connect(ragdoll, 10, true, 8, 9);
            connectRange(ragdoll, 12, true, 13, 15);
            connect(ragdoll, 17, true, 18, 19);
            connect(ragdoll, 2, false, 1, 0, 11, 16, 20);
        });

        register(LecheryEntity.class, (ragdoll, entity) -> {
            connect(ragdoll, 0, true, 1, 2);
            connectRange(ragdoll, 4, true, 5, 8);
            connect(ragdoll, 8, true, 9);
            connect(ragdoll, 11, true, 12, 13);
            connect(ragdoll, 14, true, 15, 16);
            connect(ragdoll, 17, true, 18, 19);
            connect(ragdoll, 3, false, 0, 4, 11, 14, 17);
        }, (entities, entity) -> trimTo(entities, 20));

        register(TheMoonflowerEntity.class, (ragdoll, entity) -> {
            connect(ragdoll, 27, true, 26, 28, 29, 30, 31);
            connect(ragdoll, 25, false, 0, 1, 27);
        }, (entities, entity) -> trimTo(entities, 33));

        register(ThebeartamerEntity.class, (ragdoll, entity) -> {
            connectRange(ragdoll, 6, true, 7, 13);
            connectRange(ragdoll, 17, true, 18, 27);
            connect(ragdoll, 4, true, 5);
            connect(ragdoll, 14, true, 15);
            connect(ragdoll, 2, true, 3, 16);
            connect(ragdoll, 2, false, 0, 1, 4, 14, 17);
        }, (entities, entity) -> {
            entities.remove(5);
            entities.remove(5);
            entities.remove(16);
            entities.remove(16);
        });

        register(ThewolfEntity.class, (ragdoll, entity) -> {
            connectRange(ragdoll, 6, true, 7, 17);
            connect(ragdoll, 27, true, 28, 29);
            connect(ragdoll, 4, true, 5);
            connectRange(ragdoll, 19, true, 20, 26);
            connect(ragdoll, 1, true, 0, 2, 30);
            connect(ragdoll, 1, false, 3, 4, 18, 19, 27);
        }, (entities, entity) -> {
            entities.remove(5);
            entities.remove(5);
            entities.remove(27);
            entities.remove(27);
            entities.remove(29);
        });

        register(ThebidyEntity.class, (ragdoll, entity) -> {
            connectRange(ragdoll, 0, false, 1, 5);
            connectRange(ragdoll, 0, true, 6, 8);
        });

        register(ThebidyupsideEntity.class, (ragdoll, entity) -> {
            connectRange(ragdoll, 0, false, 1, 5);
            connectRange(ragdoll, 0, true, 6, 8);
        });

        register(ThedungeonEntity.class, (ragdoll, entity) -> {
            connect(ragdoll, 0, true, 1);
            connect(ragdoll, 2, true, 3);
            connect(ragdoll, 6, true, 7);
            connect(ragdoll, 8, true, 9);
            connect(ragdoll, 10, true, 11);
            connect(ragdoll, 4, true, 5);
            connect(ragdoll, 4, false, 0, 2, 6, 8, 10);
        }, (entities, entity) -> trimTo(entities, 12));

        register(ThegliterEntity.class, (ragdoll, entity) -> {
            connectRange(ragdoll, 27, true, 26, 31);
            connect(ragdoll, 36, true, 37, 38);
            connect(ragdoll, 25, false, 0, 1, 27);
        }, (entities, entity) -> trimTo(entities, 39));

        register(TheheavyEntity.class, (ragdoll, entity) -> {
            connect(ragdoll, 0, true, 1);
            connect(ragdoll, 2, true, 3);
            connect(ragdoll, 14, true, 15, 16);
            connect(ragdoll, 18, true, 19);
            connect(ragdoll, 14, false, 18);
            connect(ragdoll, 20, true, 21, 22);
            connect(ragdoll, 23, true, 24);
            connect(ragdoll, 20, false, 23);
            connectRange(ragdoll, 9, true, 10, 13);
            connectRange(ragdoll, 4, true, 5, 8);
            connect(ragdoll, 4, false, 0, 2, 9, 14, 20);
        }, (entities, entity) -> trimTo(entities, 25));

        register(ThehorrorsEntity.class, (ragdoll, entity) -> {
            connect(ragdoll, 1, true, 2);
            connect(ragdoll, 0, false, 1, 3, 5);
            connect(ragdoll, 0, true, 4, 6);
        });

        register(ThehunterEntity.class, (ragdoll, entity) -> {
            connectRange(ragdoll, 3, true, 4, 8);
            connect(ragdoll, 12, true, 10, 11, 14, 13, 16, 18);
            connect(ragdoll, 16, true, 15);
            connect(ragdoll, 18, true, 17);
            connect(ragdoll, 19, true, 20);
            connect(ragdoll, 2, false, 0, 1, 3, 12, 19);
            connectVisually(ragdoll, 2, true, 9, 21, 22, 23);
        }, (entities, entity) -> trimTo(entities, 24));

        register(TheimmortalEntity.class, (ragdoll, entity) -> {
            connect(ragdoll, 8, true, 9);
            connect(ragdoll, 10, true, 11);
            connect(ragdoll, 2, true, 3, 4, 1);
            connect(ragdoll, 0, true, 7);
            connect(ragdoll, 0, false, 2, 5, 6, 8, 10);
        });

        register(ThelurkerEntity.class, (ragdoll, entity) -> {
            connect(ragdoll, 0, true, 1);
            connect(ragdoll, 0, false, 2);
            connectRange(ragdoll, 2, true, 3, 5);
            connect(ragdoll, 13, true, 14);
            connect(ragdoll, 13, false, 15);
            connectRange(ragdoll, 15, true, 16, 18);
            connectRange(ragdoll, 8, true, 9, 12);
            connect(ragdoll, 6, true, 7);
            connect(ragdoll, 6, false, 0, 13, 8);
        }, (entities, entity) -> trimTo(entities, 19));

        register(TheposessiveEntity.class, (ragdoll, entity) -> {
            connectRange(ragdoll, 2, true, 3, 14);
            connectRange(ragdoll, 25, true, 26, 31);
            connectRange(ragdoll, 32, true, 33, 39);
            connect(ragdoll, 0, true, 1);
            connect(ragdoll, 0, false, 2, 19, 24);
        }, (entities, entity) -> {
            trimTo(entities, 88);
            while (entities.size() > 44) {
                entities.remove(0);
            }
        });

        register(ThepregnantEntity.class, (ragdoll, entity) -> {
            connectRange(ragdoll, 7, true, 8, 15);
            connectRange(ragdoll, 16, true, 17, 23);
            connectRange(ragdoll, 24, true, 25, 32);
            connectRange(ragdoll, 33, true, 34, 43);
            connectRange(ragdoll, 2, true, 3, 6);
            connect(ragdoll, 2, false, 0, 1, 6, 7, 16, 24, 33);
        });

        register(TherabidusEntity.class, (ragdoll, entity) -> {
            connect(ragdoll, 20, true, 21, 19, 17, 18, 22, 23);
            connect(ragdoll, 3, true, 4);
            connect(ragdoll, 5, true, 6, 7, 8, 9);
            connect(ragdoll, 3, false, 5);
            connect(ragdoll, 10, true, 11);
            connectRange(ragdoll, 12, true, 13, 16);
            connect(ragdoll, 0, true, 1, 2);
            connect(ragdoll, 0, false, 3, 10, 20);
        });

        register(TherodEntity.class, (ragdoll, entity) -> {
            connectRange(ragdoll, 4, true, 5, 11);
            connect(ragdoll, 12, true, 13);
            connect(ragdoll, 2, true, 3);
            connect(ragdoll, 2, false, 0, 1, 4, 12, 14);
        }, (entities, entity) -> trimTo(entities, 15));

        register(TheskeeperEntity.class, (ragdoll, entity) -> {
            connect(ragdoll, 5, true, 6);
            connect(ragdoll, 2, true, 3);
            connect(ragdoll, 2, false, 0, 1, 4, 5, 7);
        });

        register(ThesmokerEntity.class, (ragdoll, entity) -> {
            connectRange(ragdoll, 14, true, 15, 19);
            connect(ragdoll, 8, true, 9);
            connectRange(ragdoll, 4, true, 5, 7);
            connect(ragdoll, 1, true, 2, 3);
            connect(ragdoll, 0, false, 4, 1, 10, 12, 13);
        }, (entities, entity) -> {
            entities.remove(5);
            entities.remove(5);
        });

        register(ThesomnolenceEntity.class, (ragdoll, entity) -> {
            connectVisually(ragdoll, 0, true, 1, 2, 13, 14, 15, 16);
            connectVisually(ragdoll, 0, false, 10, 12);
            connect(ragdoll, 0, true, 8, 9, 3);
            connect(ragdoll, 0, false, 11);
        }, (entities, entity) -> trimTo(entities, 17));

        register(ThespitterEntity.class, (ragdoll, entity) -> {
            connect(ragdoll, 8, true, 9, 10);
            connectRange(ragdoll, 2, true, 3, 7);
            connect(ragdoll, 2, false, 0, 1, 8);
        });

        register(TheswarmerEntity.class, (ragdoll, entity) -> {
            connectRange(ragdoll, 5, true, 6, 9);
            connect(ragdoll, 10, true, 11);
            connect(ragdoll, 2, true, 3);
            connect(ragdoll, 2, false, 0, 1, 4, 5, 10);
        });

        registerFilter(CloggerEntity.class, (entities, entity) -> entities.clear());
        registerFilter(DeadcloggerEntity.class, (entities, entity) -> entities.clear());

        register(AssasinBlackOpsEntity.class, (ragdoll, entity) -> {
            // head
            connectRange(ragdoll, 14, true, 15, 50);
            // dagger
            connectRange(ragdoll, 62, true, 63, 67);
            // right hand
            connect(ragdoll, 59, true, 60, 61);
            // right arm
            connectRange(ragdoll, 51, true, 52, 58);
            connect(ragdoll, 51, false, 59);
            // left hand
            connect(ragdoll, 76, true, 77, 78);
            // left arm
            connectRange(ragdoll, 68, true, 69, 75);
            connect(ragdoll, 68, false, 76);
            // right boot
            connect(ragdoll, 0, true, 1, 2);
            // right leg
            connectRange(ragdoll, 3, true, 4, 13);
            connect(ragdoll, 3, false, 0);
            // left boot
            connect(ragdoll, 120, true, 121, 122);
            // left leg
            connectRange(ragdoll, 109, true, 110, 119);
            connect(ragdoll, 109, false, 120);
            // chest
            connectRange(ragdoll, 79, true, 80, 108);
            connect(ragdoll, 79, false, 14, 109, 3, 68, 51);
        });

        register(BoomerEntity.class, (ragdoll, entity) -> {
            // head
            connect(ragdoll, 2, true, 3, 4);
            // right arm
            connect(ragdoll, 5, false, 6);
            // left arm
            connect(ragdoll, 7, false, 8);
            // right leg
            connect(ragdoll, 1, false, 0);
            // left leg
            connect(ragdoll, 17, false, 18);
            // chest
            connectRange(ragdoll, 9, true, 10, 16);
            connect(ragdoll, 9, false, 2, 5, 7, 1, 17);
        });

        register(ChainsawEntity.class, (ragdoll, entity) -> {
            // head
            connectRange(ragdoll, 5, true, 6, 18);
            // chainsaw
            connectRange(ragdoll, 21, true, 22, 33);
            // right arm
            connect(ragdoll, 19, false, 20);
            // left arm
            connect(ragdoll, 34, false, 35);
            // right leg
            connect(ragdoll, 3, true, 4);
            connect(ragdoll, 0, true, 1, 2);
            connect(ragdoll, 3, false, 0);
            // left leg
            connectRange(ragdoll, 43, true, 44, 46);
            connect(ragdoll, 47, true, 48, 49);
            connect(ragdoll, 43, false, 47);
            // chest
            connectRange(ragdoll, 36, true, 37, 42);
            connect(ragdoll, 36, false, 5, 19, 34, 3, 43);
        });

        register(ClickerEntity.class, (ragdoll, entity) -> {
            // head
            connectRange(ragdoll, 2, true, 3, 15);
            // right arm
            connect(ragdoll, 16, true, 17, 18);
            connect(ragdoll, 16, false, 19);
            // left arm
            connect(ragdoll, 20, false, 21);
            // right leg
            connect(ragdoll, 1, false, 0);
            // left leg
            connect(ragdoll, 30, false, 31);
            // chest
            connectRange(ragdoll, 22, true, 23, 29);
            connect(ragdoll, 22, false, 2, 16, 20, 1, 30);
        });

        register(CrawlerEntity.class, (ragdoll, entity) -> {
            // right arm
            connect(ragdoll, 1, false, 2);
            // left arm
            connect(ragdoll, 3, false, 4);
            // chest
            connect(ragdoll, 5, true, 6, 7);
            connect(ragdoll, 5, false, 0, 1, 3);
        });

        register(DemolisherEntity.class, (ragdoll, entity) -> {
            // helmet
            connectRange(ragdoll, 9, true, 10, 28);
            // right arm
            connect(ragdoll, 30, true, 31, 32);
            connect(ragdoll, 30, false, 33);
            // left arm
            connect(ragdoll, 34, false, 35);
            // right leg
            connect(ragdoll, 6, true, 7, 8);
            connectRange(ragdoll, 0, true, 1, 5);
            connect(ragdoll, 6, false, 0);
            // left leg
            connect(ragdoll, 36, true, 37, 38);
            connectRange(ragdoll, 39, true, 40, 44);
            connect(ragdoll, 36, false, 39);
            // chest
            connectRange(ragdoll, 45, true, 46, 68);
            connectRange(ragdoll, 69, true, 70, 74);
            connect(ragdoll, 45, false, 29, 30, 34, 0, 36, 69);
        });

        register(DevestatedEntity.class, (ragdoll, entity) -> {
            // finger 1
            connect(ragdoll, 13, true, 11, 12);
            // finger 2
            connect(ragdoll, 6, true, 5, 7);
            // finger 3
            connect(ragdoll, 9, true, 8, 10);
            // head
            connectRange(ragdoll, 0, true, 1, 4);
            connect(ragdoll, 0, false, 6, 9, 13);
        });

        register(DividedEntity.class, (ragdoll, entity) -> {
            // right arm
            connect(ragdoll, 23, true, 24, 25);
            connect(ragdoll, 26, true, 27, 28);
            connect(ragdoll, 29, true, 30, 31);
            connect(ragdoll, 23, false, 26, 29);
            // left arm
            connect(ragdoll, 32, true, 33, 34);
            connect(ragdoll, 32, false, 35);
            // right leg
            connect(ragdoll, 1, true, 2);
            connect(ragdoll, 1, false, 0);
            // left leg
            connectRange(ragdoll, 3, true, 4, 6);
            connect(ragdoll, 3, false, 7);
            // chest
            connectRange(ragdoll, 8, true, 9, 22);
            connect(ragdoll, 8, false, 23, 32, 1, 3);
        }, (entities, entity) -> entities.subList(3, 17).clear());

        register(ExplosiveInfectedEntity.class, (ragdoll, entity) -> {
            boolean exploded = Optional.ofNullable(entity.getLastDamageSource())
                    .map(ds -> ds.is(DamageTypes.EXPLOSION))
                    .orElse(false);

            if (exploded) {
                // right arm
                connect(ragdoll, 3, false, 4);
                // left arm
                connect(ragdoll, 5, false, 6);
                // right leg
                connect(ragdoll, 1, false, 0);
                // left leg
                connect(ragdoll, 15, false, 16);
                // chest
                connectRange(ragdoll, 7, true, 8, 14);

                Random random = new Random();
                if (random.nextBoolean()) connect(ragdoll, 7, false, 3);
                if (random.nextBoolean()) connect(ragdoll, 7, false, 5);
                if (random.nextBoolean()) connect(ragdoll, 7, false, 1);
                if (random.nextBoolean()) connect(ragdoll, 7, false, 15);
                if (random.nextBoolean()) connect(ragdoll, 7, false, 4);
            } else {
                // head
                connectRange(ragdoll, 2, true, 3, 8);
                // right arm
                connectRange(ragdoll, 9, true, 10, 12);
                connect(ragdoll, 9, false, 13);
                // left arm
                connect(ragdoll, 14, false, 15);
                // right leg
                connect(ragdoll, 1, false, 0);
                // left leg
                connect(ragdoll, 31, false, 32);
                // chest
                connectRange(ragdoll, 16, true, 17, 30);
                connect(ragdoll, 16, false, 2, 9, 14, 1, 31);
            }
        }, (entities, entity) -> {
            boolean exploded = Optional.ofNullable(entity.getLastDamageSource())
                    .map(ds -> ds.is(DamageTypes.EXPLOSION))
                    .orElse(false);
            if (!exploded) return;

            entities.subList(24, 31).clear();
            entities.subList(10, 13).clear();
            entities.subList(3, 9).clear();
        });

        register(FacelessEntity.class, (ragdoll, entity) -> {
            // head
            connectRange(ragdoll, 2, true, 3, 14);
            // right arm
            connect(ragdoll, 16, false, 15);
            // left arm
            connect(ragdoll, 17, false, 18);
            // right leg
            connect(ragdoll, 1, false, 0);
            // left leg
            connect(ragdoll, 26, false, 27);
            // chest
            connectRange(ragdoll, 19, true, 20, 25);
            connect(ragdoll, 19, false, 2, 16, 17, 1, 26);
        });

        register(FetusEntity.class, (ragdoll, entity) -> {
            // head
            connectRange(ragdoll, 4, true, 5, 9);
            // right arm
            connect(ragdoll, 0, false, 1);
            // left arm
            connect(ragdoll, 12, false, 13);
            // right leg
            connect(ragdoll, 3, false, 2);
            // left leg
            connect(ragdoll, 10, false, 11);
            // chest
            connectRange(ragdoll, 14, true, 15, 24);
            connect(ragdoll, 14, false, 4, 0, 12, 2, 10);
        });

        register(GoonEntity.class, (ragdoll, entity) -> {
            // head
            connectRange(ragdoll, 18, true, 19, 25);
            // right arm
            connectRange(ragdoll, 0, true, 1, 4);
            connectRange(ragdoll, 5, true, 6, 11);
            connect(ragdoll, 0, false, 5);
            // left arm
            connect(ragdoll, 28, false, 29);
            // right leg
            connectRange(ragdoll, 12, true, 13, 16);
            connect(ragdoll, 17, false, 12);
            // left leg
            connect(ragdoll, 26, false, 27);
            // chest
            connect(ragdoll, 31, true, 30);
            connect(ragdoll, 31, false, 18, 0, 28, 12, 26);
        });

        register(InfectedEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .connect(0, 2, RagdollAssembler.Type.LOOSE)
                .connect(2, 1, RagdollAssembler.Type.VISUAL_FIXED)
                .connect(8, 10, RagdollAssembler.Type.LOOSE)
                .connect(10, 9, RagdollAssembler.Type.VISUAL_FIXED)
                .connect("Rleg1", "Rleg2", RagdollAssembler.Type.LOOSE)
                .connect("Lleg1", "Lleg2", RagdollAssembler.Type.LOOSE)
                .connect("chest1", "chest2", RagdollAssembler.Type.FIXED)
                .connect("chest1", "head", RagdollAssembler.Type.LOOSE)
                .connect("chest1", "Rarm1", RagdollAssembler.Type.LOOSE)
                .connect("chest1", "Larm1", RagdollAssembler.Type.LOOSE)
                .connect("chest1", "Rleg1", RagdollAssembler.Type.LOOSE)
                .connect("chest1", "Lleg1", RagdollAssembler.Type.LOOSE)
                .assemble()
        );

        register(InfectedHazmatEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("helmet", "Skull", "Rarm1", "Rarm2", "Larm1", "Larm2", "Rleg1", "Rleg2", "RBoot", "Lleg1", "Lleg2", "Chest2", "Chest1")
                .connect("helmet", "Skull", RagdollAssembler.Type.FIXED)
                .connect("Rarm1", "Rarm2", RagdollAssembler.Type.LOOSE)
                .connect("Larm1", "Larm2", RagdollAssembler.Type.LOOSE)
                .connect("Rleg2", "RBoot", RagdollAssembler.Type.FIXED)
                .connect("Rleg1", "Rleg2", RagdollAssembler.Type.LOOSE)
                .connect("Lleg1", "Lleg2", RagdollAssembler.Type.LOOSE)
                .connect("Chest2", "Chest1", RagdollAssembler.Type.FIXED)
                .connect("Chest2", "helmet", RagdollAssembler.Type.LOOSE)
                .connect("Chest2", "Rarm1", RagdollAssembler.Type.LOOSE)
                .connect("Chest2", "Larm1", RagdollAssembler.Type.LOOSE)
                .connect("Chest2", "Rleg1", RagdollAssembler.Type.LOOSE)
                .connect("Chest2", "Lleg1", RagdollAssembler.Type.LOOSE)
                .assemble()
        );

        register(InfectedJuggernautEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("head2", "helmet", "rarm1", "rarm2", "larm1", "larm2", "rleg1", "rleg2", "RBoot", "lleg1", "lleg2", "LBoot", "chestarm", "chest1", "chest2")
                .connect("helmet", "head2", RagdollAssembler.Type.FIXED)
                .connect("rarm1", "rarm2", RagdollAssembler.Type.LOOSE)
                .connect("larm1", "larm2", RagdollAssembler.Type.LOOSE)
                .connect("rleg2", "RBoot", RagdollAssembler.Type.FIXED)
                .connect("rleg1", "rleg2", RagdollAssembler.Type.LOOSE)
                .connect("lleg2", "LBoot", RagdollAssembler.Type.FIXED)
                .connect("lleg1", "lleg2", RagdollAssembler.Type.LOOSE)
                .connect("chest1", "chestarm", RagdollAssembler.Type.FIXED)
                .connect("chest1", "chest2", RagdollAssembler.Type.FIXED)
                .connect("chest1", "helmet", RagdollAssembler.Type.LOOSE)
                .connect("chest1", "rarm1", RagdollAssembler.Type.LOOSE)
                .connect("chest1", "larm1", RagdollAssembler.Type.LOOSE)
                .connect("chest1", "rleg1", RagdollAssembler.Type.LOOSE)
                .connect("chest1", "lleg1", RagdollAssembler.Type.LOOSE)
                .assemble()
        );

        register(InfectedMilitaryEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("Rleg2", "Rleg1", "Head", "Gasmask", "Rarm1", "Larm1", "Vest2", "Vest1", "Lleg1", "Lleg2")
                .connect("Rarm1", "Rarm2", RagdollAssembler.Type.LOOSE)
                .connect("Larm1", "Larm2", RagdollAssembler.Type.LOOSE)
                .connect("Rleg1", "Rleg2", RagdollAssembler.Type.LOOSE)
                .connect("Lleg1", "Lleg2", RagdollAssembler.Type.LOOSE)
                .connect("Vest1", "Rarm1", RagdollAssembler.Type.LOOSE)
                .connect("Vest1", "Larm1", RagdollAssembler.Type.LOOSE)
                .connect("Vest1", "Head", RagdollAssembler.Type.LOOSE)
                .connect("Vest2", "Rleg1", RagdollAssembler.Type.LOOSE)
                .connect("Vest2", "Lleg1", RagdollAssembler.Type.LOOSE)
                .assemble()
        );

        register(InfectedPoliceEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("rleg2", "Head", "Chest1", "lleg2")
                .connect("rarm1", "rarm2", RagdollAssembler.Type.LOOSE)
                .connect("larm1", "larm2", RagdollAssembler.Type.LOOSE)
                .connect("rleg1", "rleg2", RagdollAssembler.Type.LOOSE)
                .connect("lleg1", "lleg2", RagdollAssembler.Type.LOOSE)
                .connect("Chest1", "Head", RagdollAssembler.Type.LOOSE)
                .connect("Chest1", "Chest2", RagdollAssembler.Type.FIXED)
                .connect("Chest1", "rarm1", RagdollAssembler.Type.LOOSE)
                .connect("Chest1", "larm1", RagdollAssembler.Type.LOOSE)
                .connect("Chest1", "rleg1", RagdollAssembler.Type.LOOSE)
                .connect("Chest1", "lleg1", RagdollAssembler.Type.LOOSE)
                .assemble()
        );

        register(InflatedEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("Rarm1", "Skull", "Larm2", "Chest2", "Chest1")
                .connect("Skull", "Underskull", RagdollAssembler.Type.FIXED)
                .connect("Skull", "Blop1", RagdollAssembler.Type.FIXED)
                .connect("Skull", "Blop2", RagdollAssembler.Type.FIXED)
                .connect("Skull", "Blop3", RagdollAssembler.Type.FIXED)
                .connect("Skull", "Blop4", RagdollAssembler.Type.FIXED)
                .connect("Rarm1", "Rarm2", RagdollAssembler.Type.LOOSE)
                .connect("Larm1", "Larm2", RagdollAssembler.Type.LOOSE)
                .connect("RLeg1", "RLeg2", RagdollAssembler.Type.LOOSE)
                .connect("LLeg1", "LLeg2", RagdollAssembler.Type.LOOSE)
                .connect("Chest2", "Blop6", RagdollAssembler.Type.FIXED)
                .connect("Chest2", "Blop8", RagdollAssembler.Type.FIXED)
                .connect("Chest1", "Blop5", RagdollAssembler.Type.FIXED)
                .connect("Chest1", "Blop7", RagdollAssembler.Type.FIXED)
                .connect("Chest1", "Chest2", RagdollAssembler.Type.FIXED)
                .connect("Chest1", "Skull", RagdollAssembler.Type.LOOSE)
                .connect("Chest1", "Rarm1", RagdollAssembler.Type.LOOSE)
                .connect("Chest1", "Larm1", RagdollAssembler.Type.LOOSE)
                .connect("Chest1", "RLeg1", RagdollAssembler.Type.LOOSE)
                .connect("Chest1", "LLeg1", RagdollAssembler.Type.LOOSE)
                .assemble()
        );

        register(MilitaryEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("Rleg2", "Rleg1", "Helmet", "Gasmask", "Rarm1", "Rarm2", "weapon", "Larm1", "Larm2", "Vest2", "Vest1", "Lleg1", "Lleg2")
                .connect("Helmet", "Gasmask", RagdollAssembler.Type.FIXED)
                .connect("Rarm1", "Rarm2", RagdollAssembler.Type.LOOSE)
                .connect("Larm1", "Larm2", RagdollAssembler.Type.LOOSE)
                .connect("Rleg1", "Rleg2", RagdollAssembler.Type.LOOSE)
                .connect("Lleg1", "Lleg2", RagdollAssembler.Type.LOOSE)
                .connect("Vest1", "Rarm1", RagdollAssembler.Type.LOOSE)
                .connect("Vest1", "Larm1", RagdollAssembler.Type.LOOSE)
                .connect("Vest1", "Head", RagdollAssembler.Type.LOOSE)
                .connect("Vest2", "Rleg1", RagdollAssembler.Type.LOOSE)
                .connect("Vest2", "Lleg1", RagdollAssembler.Type.LOOSE)
                .assemble()
        );

        register(NightHunterEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("Head", "chest2", "chest1")
                .connect("Rarm1", "armbone1", RagdollAssembler.Type.FIXED)
                .connect("Rarm1", "armbone2", RagdollAssembler.Type.FIXED)
                .connect(6, 8, RagdollAssembler.Type.FIXED) // Rarm1 <- wire
                .connect(6, 9, RagdollAssembler.Type.FIXED) // Rarm1 <- wire
                .connect("Rarm1", "laze1", RagdollAssembler.Type.LOOSE)
                .connect("Rarm1", "laze2", RagdollAssembler.Type.LOOSE)
                .connect("Larm1", "armbone3", RagdollAssembler.Type.FIXED)
                .connect("Larm1", "armbone4", RagdollAssembler.Type.FIXED)
                .connect(13, 16, RagdollAssembler.Type.FIXED) // Larm1 <- wire2
                .connect(13, 17, RagdollAssembler.Type.FIXED) // Larm1 <- wire2
                .connect("Rleg1", "Rleg2", RagdollAssembler.Type.LOOSE)
                .connect("Lleg1", "Lleg2", RagdollAssembler.Type.LOOSE)
                .connect("chest2", "chest1", RagdollAssembler.Type.FIXED)
                .connect("chest2", "Head", RagdollAssembler.Type.LOOSE)
                .connect("chest2", "Rarm1", RagdollAssembler.Type.LOOSE)
                .connect("chest2", "Larm1", RagdollAssembler.Type.LOOSE)
                .connect("chest2", "Rleg1", RagdollAssembler.Type.LOOSE)
                .connect("chest2", "Lleg1", RagdollAssembler.Type.LOOSE)
                .assemble()
        );

        register(ParasiteEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("head")
                // lleg1
                .connect(0, 1, RagdollAssembler.Type.FIXED)
                .connect(1, 2, RagdollAssembler.Type.FIXED)
                // lleg2
                .connect(6, 7, RagdollAssembler.Type.FIXED)
                .connect(7, 8, RagdollAssembler.Type.FIXED)
                // lleg3
                .connect(9, 10, RagdollAssembler.Type.FIXED)
                .connect(10, 11, RagdollAssembler.Type.FIXED)
                // rleg1
                .connect(22, 23, RagdollAssembler.Type.FIXED)
                .connect(23, 24, RagdollAssembler.Type.FIXED)
                // rleg2
                .connect(15, 16, RagdollAssembler.Type.FIXED)
                .connect(16, 17, RagdollAssembler.Type.FIXED)
                // rleg3
                .connect(12, 13, RagdollAssembler.Type.FIXED)
                .connect(13, 14, RagdollAssembler.Type.FIXED)
                // chest
                .connect(21, 18, RagdollAssembler.Type.FIXED)
                .connect(21, 20, RagdollAssembler.Type.VISUAL_FIXED)
                .connect(21, 19, RagdollAssembler.Type.VISUAL_FIXED)
                .connect(21, 22, RagdollAssembler.Type.LOOSE)
                .connect(21, 12, RagdollAssembler.Type.LOOSE)
                .connect(21, 9, RagdollAssembler.Type.LOOSE)
                .connect(21, 15, RagdollAssembler.Type.LOOSE)
                .connect(21, 6, RagdollAssembler.Type.LOOSE)
                .connect(21, 0, RagdollAssembler.Type.LOOSE)
                .connect(21, 3, RagdollAssembler.Type.LOOSE)
                .assemble()
        );

        register(RamEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("mask", "chest1", "chest2", "Lleg2", "Rleg2")
                .connect("Rarm1", "Rarm2", RagdollAssembler.Type.LOOSE)
                .connect("Larm1", "Larm2", RagdollAssembler.Type.LOOSE)
                .connect("Rleg1", "Rleg2", RagdollAssembler.Type.LOOSE)
                .connect("Lleg1", "Lleg2", RagdollAssembler.Type.LOOSE)
                .connect("chest1", "head", RagdollAssembler.Type.FIXED)
                .connect("chest1", "Rarm1", RagdollAssembler.Type.LOOSE)
                .connect("chest1", "Larm1", RagdollAssembler.Type.LOOSE)
                .connect("chest2", "Rleg1", RagdollAssembler.Type.LOOSE)
                .connect("chest2", "Lleg1", RagdollAssembler.Type.LOOSE)
                .assemble()
        );

        register(RatKingEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("head1", "head2", "head3", "body1", "body2", "chest1", "chest2", "Rarm1", "Rleg2", "Lleg1")
                // right minion
                .connect("BRarm1", "BRarm2", RagdollAssembler.Type.LOOSE)
                .connect("body2", "BRarm1", RagdollAssembler.Type.LOOSE)
                .connect("body2", "head2", RagdollAssembler.Type.LOOSE)
                // left minion
                .connect("BLarm1", "BLarm2", RagdollAssembler.Type.LOOSE)
                .connect("body1", "BLarm1", RagdollAssembler.Type.LOOSE)
                .connect("body1", "head3", RagdollAssembler.Type.LOOSE)
                // main body
                .connect("Rarm1", "Rarm2", RagdollAssembler.Type.LOOSE)
                .connect("Larm1", "Larm2", RagdollAssembler.Type.LOOSE)
                .connect("Rleg1", "Rleg2", RagdollAssembler.Type.LOOSE)
                .connect("Lleg1", "Lleg2", RagdollAssembler.Type.LOOSE)
                .connect("BArmChest1", "BArmChest2", RagdollAssembler.Type.LOOSE)
                .connect("chest1", "chest2", RagdollAssembler.Type.FIXED)
                .connect("chest1", "head1", RagdollAssembler.Type.LOOSE)
                .connect("chest1", "Rarm1", RagdollAssembler.Type.LOOSE)
                .connect("chest1", "Larm1", RagdollAssembler.Type.LOOSE)
                .connect("chest1", "Rleg1", RagdollAssembler.Type.LOOSE)
                .connect("chest1", "Lleg1", RagdollAssembler.Type.LOOSE)
                .connect("chest1", "BArmChest1", RagdollAssembler.Type.LOOSE)
                .assemble()
        );

        register(RevivedEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("skull1", "skull2", "chest2", "chest1")
                .connect(4, 2, RagdollAssembler.Type.FIXED) // skull1 <- wire
                .connect(4, 3, RagdollAssembler.Type.FIXED) // skull1 <- wire
                .connect("skull1", "skull2", RagdollAssembler.Type.LOOSE)
                .connect("rarm1", "rarm2", RagdollAssembler.Type.LOOSE)
                .connect("larm1", "larm2", RagdollAssembler.Type.LOOSE)
                .connect("larm2", "larm3", RagdollAssembler.Type.LOOSE)
                .connect("rleg1", "rleg2", RagdollAssembler.Type.LOOSE)
                .connect("lleg1", "lleg2", RagdollAssembler.Type.LOOSE)
                .connect("chest2", "chest1", RagdollAssembler.Type.FIXED)
                .connect("chest2", "skull1", RagdollAssembler.Type.LOOSE)
                .connect("chest2", "rarm1", RagdollAssembler.Type.LOOSE)
                .connect("chest2", "larm1", RagdollAssembler.Type.LOOSE)
                .connect("chest2", "rleg1", RagdollAssembler.Type.LOOSE)
                .connect("chest2", "lleg1", RagdollAssembler.Type.LOOSE)
                .assemble()
        );

        register(RunnerEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .connect(0, 2, RagdollAssembler.Type.LOOSE)
                .connect(2, 1, RagdollAssembler.Type.VISUAL_FIXED)
                .connect(8, 10, RagdollAssembler.Type.LOOSE)
                .connect(10, 9, RagdollAssembler.Type.VISUAL_FIXED)
                .connect("Rleg1", "Rleg2", RagdollAssembler.Type.LOOSE)
                .connect("Lleg1", "Lleg2", RagdollAssembler.Type.LOOSE)
                .connect("chest1", "chest2", RagdollAssembler.Type.FIXED)
                .connect("chest1", "head", RagdollAssembler.Type.LOOSE)
                .connect("chest1", "Rarm1", RagdollAssembler.Type.LOOSE)
                .connect("chest1", "Larm1", RagdollAssembler.Type.LOOSE)
                .connect("chest1", "Rleg1", RagdollAssembler.Type.LOOSE)
                .connect("chest1", "Lleg1", RagdollAssembler.Type.LOOSE)
                .assemble()
        );

        register(ZeroPatientEntity.class, (ragdoll, entity) -> new RagdollAssembler(ragdoll)
                .merge("skull1", "skull2", "skull3", "skull4", "Rarm2", "Larm2", "chest1")
                .connect("Rarm1", "Rarm2", RagdollAssembler.Type.LOOSE)
                .connect("Larm1", "Larm2", RagdollAssembler.Type.LOOSE)
                .connect("Rleg1", "Rleg2", RagdollAssembler.Type.LOOSE)
                .connect("Rleg2", "Rleg3", RagdollAssembler.Type.LOOSE)
                .connect("Lleg1", "Lleg2", RagdollAssembler.Type.LOOSE)
                .connect("Lleg2", "Lleg3", RagdollAssembler.Type.LOOSE)
                .connect("BRarm1", "BRarm2", RagdollAssembler.Type.LOOSE)
                .connect("BLarm1", "BLarm2", RagdollAssembler.Type.LOOSE)
                .connect("chest1", "chest2", RagdollAssembler.Type.FIXED)
                .connect("chest1", "skull1", RagdollAssembler.Type.LOOSE)
                .connect("chest1", "skull2", RagdollAssembler.Type.LOOSE)
                .connect("chest1", "skull3", RagdollAssembler.Type.LOOSE)
                .connect("chest1", "skull4", RagdollAssembler.Type.LOOSE)
                .connect("chest1", "Rarm1", RagdollAssembler.Type.LOOSE)
                .connect("chest1", "Larm1", RagdollAssembler.Type.LOOSE)
                .connect("chest1", "Rleg1", RagdollAssembler.Type.LOOSE)
                .connect("chest1", "Lleg1", RagdollAssembler.Type.LOOSE)
                .connect("chest1", "BRarm1", RagdollAssembler.Type.LOOSE)
                .connect("chest1", "BLarm1", RagdollAssembler.Type.LOOSE)
                .assemble()
        );

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
                    if(entity.hasMane()) {
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

    private static void connect(Ragdoll ragdoll, int target, boolean fixed, int... sources) {
        for (int source : sources) {
            ragdoll.addConnection(source, target, fixed);
        }
    }

    private static void connectVisually(Ragdoll ragdoll, int target, boolean fixed, int... sources) {
        for (int source : sources) {
            ragdoll.addConnection(source, target, fixed, true);
        }
    }

    private static void connectRange(Ragdoll ragdoll, int target, boolean fixed, int start, int end) {
        for (int i = start; i <= end; i++) {
            ragdoll.addConnection(i, target, fixed);
        }
    }

    private static void trimTo(List<?> list, int maxSize) {
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