package com.bawnorton.dcfixes.client.compat;

import com.bawnorton.dcfixes.client.compat.cnpc.CustomNpcCompat;
import com.bawnorton.dcfixes.client.compat.deaceased.DeaceasedCompat;
import com.bawnorton.dcfixes.client.compat.emf.EMFCompat;
import com.bawnorton.dcfixes.client.compat.geckolib.GeckoLibCompat;
import com.bawnorton.dcfixes.client.compat.naturalist.NaturalistCompat;
import com.bawnorton.dcfixes.client.compat.physics_mod.PhysicsModCompat;
import com.bawnorton.dcfixes.client.compat.undead_revamp2.UndeadRevampCompat;
import com.bawnorton.dcfixes.client.compat.zombie_extreme.ZombieExtremeCompat;
import net.minecraftforge.fml.ModList;

import java.util.Optional;

public class ClientCompat {
    private PhysicsModCompat physicsModCompat;
    private EMFCompat emfCompat;
    private GeckoLibCompat geckoLibCompat;
    private UndeadRevampCompat undeadRevampCompat;
    private ZombieExtremeCompat zombieExtremeCompat;
    private NaturalistCompat naturalistCompat;
    private DeaceasedCompat deaceasedCompat;
    private CustomNpcCompat customNpcCompat;

    public Optional<PhysicsModCompat> getPhysicsModCompat() {
        if (!ModList.get().isLoaded("physicsmod")) return Optional.empty();
        if (physicsModCompat == null) {
            physicsModCompat = new PhysicsModCompat();
        }
        return Optional.of(physicsModCompat);
    }

    public Optional<EMFCompat> getEmfCompat() {
        if (!ModList.get().isLoaded("entity_model_features")) return Optional.empty();
        if (emfCompat == null) {
            emfCompat = new EMFCompat();
        }
        return Optional.of(emfCompat);
    }

    public Optional<GeckoLibCompat> getGeckoLibCompat() {
        if (!ModList.get().isLoaded("geckolib")) return Optional.empty();
        if (geckoLibCompat == null) {
            geckoLibCompat = new GeckoLibCompat();
        }
        return Optional.of(geckoLibCompat);
    }

    public Optional<UndeadRevampCompat> getUndeadRevampCompat() {
        if (!ModList.get().isLoaded("undead_revamp2")) return Optional.empty();
        if (undeadRevampCompat == null) {
            undeadRevampCompat = new UndeadRevampCompat();
        }
        return Optional.of(undeadRevampCompat);
    }

    public Optional<ZombieExtremeCompat> getZombieExtremeCompat() {
        if (!ModList.get().isLoaded("zombie_extreme")) return Optional.empty();
        if (zombieExtremeCompat == null) {
            zombieExtremeCompat = new ZombieExtremeCompat();
        }
        return Optional.of(zombieExtremeCompat);
    }

    public Optional<NaturalistCompat> getNaturalistCompat() {
        if (!ModList.get().isLoaded("naturalist")) return Optional.empty();
        if (naturalistCompat == null) {
            naturalistCompat = new NaturalistCompat();
        }
        return Optional.of(naturalistCompat);
    }

    public Optional<DeaceasedCompat> getDeaceasedCompat() {
        if (!ModList.get().isLoaded("deaceased")) return Optional.empty();
        if (deaceasedCompat == null) {
            deaceasedCompat = new DeaceasedCompat();
        }
        return Optional.of(deaceasedCompat);
    }

    public Optional<CustomNpcCompat> getCustomNpcCompat() {
        if (!ModList.get().isLoaded("customnpcs")) return Optional.empty();
        if (customNpcCompat == null) {
            customNpcCompat = new CustomNpcCompat();
        }
        return Optional.of(customNpcCompat);
    }
}
