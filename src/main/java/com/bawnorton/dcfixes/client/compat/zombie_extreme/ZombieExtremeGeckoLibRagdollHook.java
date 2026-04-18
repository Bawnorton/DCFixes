package com.bawnorton.dcfixes.client.compat.zombie_extreme;

import com.bawnorton.dcfixes.client.compat.physics_mod.GeckoLibRagdollHook;
import com.bawnorton.dcfixes.client.compat.physics_mod.RagdollAssembler;
import net.minecraft.world.damagesource.DamageTypes;
import zombie_extreme.entity.*;

import java.util.Optional;
import java.util.Random;

public class ZombieExtremeGeckoLibRagdollHook extends GeckoLibRagdollHook {
    public ZombieExtremeGeckoLibRagdollHook() {
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
    }
}
