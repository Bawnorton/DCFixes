package com.bawnorton.dcfixes.compat;

import com.bawnorton.dcfixes.compat.hordes.HordesCompat;
import com.bawnorton.dcfixes.compat.immersive_vehicles.ImmersiveVehiclesCompat;
import net.minecraftforge.fml.ModList;

import java.util.Optional;

public class Compat {
    private final ImmersiveVehiclesCompat immersiveVehiclesCompat;
    private HordesCompat hordesCompat;

    public Compat() {
        this.immersiveVehiclesCompat = new ImmersiveVehiclesCompat();
    }

    public ImmersiveVehiclesCompat getImmersiveVehiclesCompat() {
        return immersiveVehiclesCompat;
    }

    public Optional<HordesCompat> getHordesCompat() {
        if (!ModList.get().isLoaded("hordes")) return Optional.empty();
        if (hordesCompat == null) {
            hordesCompat = new HordesCompat();
        }
        return Optional.of(hordesCompat);
    }
}
