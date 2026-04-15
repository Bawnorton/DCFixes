package com.bawnorton.dcfixes.compat;

import com.bawnorton.dcfixes.compat.immersive_vehicles.ImmersiveVehiclesCompat;

public class Compat {
    private final ImmersiveVehiclesCompat immersiveVehiclesCompat;

    public Compat() {
        this.immersiveVehiclesCompat = new ImmersiveVehiclesCompat();
    }

    public ImmersiveVehiclesCompat getImmersiveVehiclesCompat() {
        return immersiveVehiclesCompat;
    }
}
