package com.bawnorton.dcfixes.client.compat.undead_revamp2;

import com.bawnorton.dcfixes.client.compat.physics_mod.GeckoLibRagdollHook;
import net.mcreator.undeadrevamp.entity.*;

public class UndeadRevampGeckoLibRagdollHook extends GeckoLibRagdollHook {
    public UndeadRevampGeckoLibRagdollHook() {
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
    }
}

