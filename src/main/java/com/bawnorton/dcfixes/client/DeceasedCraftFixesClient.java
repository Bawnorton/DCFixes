package com.bawnorton.dcfixes.client;

import net.mcreator.apocalypsenow.client.gui.CorpseclothguiScreen;
import net.mcreator.apocalypsenow.client.gui.IronlockerboxguiScreen;
import net.mcreator.apocalypsenow.client.gui.IronsafeboxGUIScreen;
import net.mcreator.apocalypsenow.client.gui.MedicalcrateGUIScreen;
import net.minecraft.client.gui.screen.Screen;
import java.util.Map;

public final class DeceasedCraftFixesClient {
    public static final Map<Class<? extends Screen>, Integer> QUARK_BUTTON_SLOT_INDEX = Map.ofEntries(
            Map.entry(IronsafeboxGUIScreen.class, 6),
            Map.entry(CorpseclothguiScreen.class, 6),
            Map.entry(IronlockerboxguiScreen.class, 6),
            Map.entry(MedicalcrateGUIScreen.class, 2)
    );

    public static void init() {
        //no-op
    }
}
