package com.bawnorton.dcfixes.compat;

import com.bawnorton.dcfixes.compat.corpse.CorpseCompat;
import com.bawnorton.dcfixes.compat.hordes.HordesCompat;
import com.bawnorton.dcfixes.compat.immersive_engineering.IECompat;
import com.bawnorton.dcfixes.compat.immersive_vehicles.ImmersiveVehiclesCompat;
import com.bawnorton.dcfixes.compat.legendarysurvivaloverhaul.LSOCompat;
import com.bawnorton.dcfixes.compat.lostcities.LostCitiesCompat;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;

import java.util.Optional;

public class Compat {
    private ImmersiveVehiclesCompat immersiveVehiclesCompat;
    private HordesCompat hordesCompat;
    private LSOCompat lsoCompat;
    private IECompat ieCompat;
    private LostCitiesCompat lostCitiesCompat;
    private CorpseCompat corpseCompat;

    public Optional<ImmersiveVehiclesCompat> getImmersiveVehiclesCompat() {
        if (isModAbsent("mts")) return Optional.empty();
        if (immersiveVehiclesCompat == null) {
            immersiveVehiclesCompat = new ImmersiveVehiclesCompat();
        }
        return Optional.of(immersiveVehiclesCompat);
    }

    public Optional<HordesCompat> getHordesCompat() {
        if (isModAbsent("hordes")) return Optional.empty();
        if (hordesCompat == null) {
            hordesCompat = new HordesCompat();
        }
        return Optional.of(hordesCompat);
    }

    public Optional<LSOCompat> getLSOCompat() {
        if (isModAbsent("legendarysurvivaloverhaul")) return Optional.empty();
        if (lsoCompat == null) {
            lsoCompat = new LSOCompat();
        }
        return Optional.of(lsoCompat);
    }

    public Optional<IECompat> getImmersiveEngineeringCompat() {
        if (isModAbsent("immersiveengineering")) return Optional.empty();
        if (ieCompat == null) {
            ieCompat = new IECompat();
        }
        return Optional.of(ieCompat);
    }

    public Optional<LostCitiesCompat> getLostCitiesCompat() {
        if (isModAbsent("lostcities")) return Optional.empty();
        if (lostCitiesCompat == null) {
            lostCitiesCompat = new LostCitiesCompat();
        }
        return Optional.of(lostCitiesCompat);
    }

    public Optional<CorpseCompat> getCorpseCompat() {
        if (isModAbsent("corpse")) return Optional.empty();
        if (corpseCompat == null) {
            corpseCompat = new CorpseCompat();
        }
        return Optional.of(corpseCompat);
    }

    private boolean isModAbsent(String id) {
        ModList modList = ModList.get();
        if (modList != null) {
            return !modList.isLoaded(id);
        }
        LoadingModList loadingModList = LoadingModList.get();
        if(loadingModList == null) {
            return true;
        }

        for (ModInfo modInfo : loadingModList.getMods()) {
            if (modInfo.getModId().equals(id)) {
                return false;
            }
        }
        return true;
    }
}
