package com.bawnorton.dcfixes.client.compat;

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
    private final PhysicsModCompat physicsModCompat;
    private final EMFCompat emfCompat;
    private final GeckoLibCompat geckoLibCompat;
    private UndeadRevampCompat undeadRevampCompat;
    private ZombieExtremeCompat zombieExtremeCompat;
    private NaturalistCompat naturalistCompat;
    private DeaceasedCompat deaceasedCompat;

    public ClientCompat() {
        physicsModCompat = new PhysicsModCompat();
        emfCompat = new EMFCompat();
        geckoLibCompat = new GeckoLibCompat();
    }

    public PhysicsModCompat getPhysicsModCompat() {
        return physicsModCompat;
    }

    public EMFCompat getEmfCompat() {
        return emfCompat;
    }

    public GeckoLibCompat getGeckoLibCompat() {
        return geckoLibCompat;
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
}
