package com.bawnorton.dcfixes;

import net.mcreator.apocalypsenow.client.gui.CorpseclothguiScreen;
import net.mcreator.apocalypsenow.client.gui.IronlockerboxguiScreen;
import net.mcreator.apocalypsenow.client.gui.IronsafeboxGUIScreen;
import net.mcreator.apocalypsenow.client.gui.MedicalcrateboxGUIScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

@Mod(DeceasedCraftFixes.MOD_ID)
public final class DeceasedCraftFixes {
    public static final String MOD_ID = "dcfixes";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Map<Class<? extends Screen>, Integer> QUARK_BUTTON_SLOT_INDEX = Map.ofEntries(
            Map.entry(IronsafeboxGUIScreen.class, 6),
            Map.entry(CorpseclothguiScreen.class, 6),
            Map.entry(IronlockerboxguiScreen.class, 6),
            Map.entry(MedicalcrateboxGUIScreen.class, 6)
    );

    public DeceasedCraftFixes() {
        LOGGER.info("Fixing DeceasedCraft");
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    public static void onChunkLoaded(ServerWorld world, Chunk chunk) {
        FasterLostCities.getData(world).executeAndClearTodo(world, chunk);
    }
}
