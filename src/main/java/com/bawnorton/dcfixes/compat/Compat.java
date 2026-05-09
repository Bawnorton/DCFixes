package com.bawnorton.dcfixes.compat;

import com.bawnorton.dcfixes.compat.hordes.HordesCompat;
import com.bawnorton.dcfixes.compat.immersive_vehicles.ImmersiveVehiclesCompat;
import com.bawnorton.dcfixes.compat.legendarysurvivaloverhaul.LSOCompat;
import net.minecraftforge.fml.ModList;

import java.util.Optional;

public class Compat {
    private ImmersiveVehiclesCompat immersiveVehiclesCompat;
    private HordesCompat hordesCompat;
    private LSOCompat lsoCompat;

    public Optional<ImmersiveVehiclesCompat> getImmersiveVehiclesCompat() {
        if (!ModList.get().isLoaded("mts")) return Optional.empty();
        if (immersiveVehiclesCompat == null) {
            immersiveVehiclesCompat = new ImmersiveVehiclesCompat();
        }
        return Optional.of(immersiveVehiclesCompat);
    }

    public Optional<HordesCompat> getHordesCompat() {
        if (!ModList.get().isLoaded("hordes")) return Optional.empty();
        if (hordesCompat == null) {
            hordesCompat = new HordesCompat();
        }
        return Optional.of(hordesCompat);
    }

    public Optional<LSOCompat> getLSOCompat() {
        if (!ModList.get().isLoaded("legendarysurvivaloverhaul")) return Optional.empty();
        if (lsoCompat == null) {
            lsoCompat = new LSOCompat();
        }
        return Optional.of(lsoCompat);
    }
}
