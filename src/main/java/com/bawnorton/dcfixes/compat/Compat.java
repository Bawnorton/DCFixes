package com.bawnorton.dcfixes.compat;

import com.bawnorton.dcfixes.compat.hordes.HordesCompat;
import com.bawnorton.dcfixes.compat.immersive_vehicles.ImmersiveVehiclesCompat;

public class Compat {
    private final ImmersiveVehiclesCompat immersiveVehiclesCompat;
    private final HordesCompat hordesCompat;

    public Compat() {
        this.immersiveVehiclesCompat = new ImmersiveVehiclesCompat();
        this.hordesCompat = new HordesCompat();
    }

    public ImmersiveVehiclesCompat getImmersiveVehiclesCompat() {
        return immersiveVehiclesCompat;
    }

    public HordesCompat getHordesCompat() {
        return hordesCompat;
    }
}
