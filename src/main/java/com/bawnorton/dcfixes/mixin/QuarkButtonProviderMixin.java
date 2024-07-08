package com.bawnorton.dcfixes.mixin;

import blusunrize.immersiveengineering.client.gui.IEContainerScreen;
import net.mcreator.apocalypsenow.client.gui.*;
import org.spongepowered.asm.mixin.Mixin;
import vazkii.quark.api.IQuarkButtonAllowed;
import zombie_extreme.client.gui.*;

@Mixin({
        IEContainerScreen.class,
        AlicepackGUIScreen.class,
        BigbackpackGUIScreen.class,
        CampingbackpackGUIScreen.class,
        CardboardGUIScreen.class,
        CorpseclothguiScreen.class,
        CorpsesackGUIScreen.class,
        CrateGUIScreen.class,
        HiddenstashplanksGUIScreen.class,
        IronlockerboxguiScreen.class,
        IronsafeboxGUIScreen.class,
        MedicalcrateboxGUIScreen.class,
        MedicalcrateGUIScreen.class,
        MilitarybackpackGUIScreen.class,
        MilitarycrateGUIScreen.class,
        NormalbackpackGUIScreen.class,
        PalletstorageguiScreen.class,
        PinkbackpackGUIScreen.class,
        SedexGUIScreen.class,
        SimplebackpackGUIScreen.class,
        SuplyshelvesboxGUIScreen.class,
        WalletguiScreen.class,
        BirdsnestGUIScreen.class,
        BlackOpsCargoGUIScreen.class,
        CupboardGUIScreen.class,
        DeadBodyGUIScreen.class,
        DecomposingBackpackGUIScreen.class,
        FridgeGUIScreen.class,
        MedicalBoxGUIScreen.class,
        MilitaryCargoGUIScreen.class,
        NightStandGUIScreen.class,
        ToolBoxGUIScreen.class,
        VendingMachineGUIScreen.class
})
public abstract class QuarkButtonProviderMixin implements IQuarkButtonAllowed {
}
