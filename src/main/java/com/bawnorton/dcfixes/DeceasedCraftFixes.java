package com.bawnorton.dcfixes;

import com.bawnorton.dcfixes.client.DeceasedCraftFixesClient;
import net.mcreator.apocalypsenow.client.gui.CorpseclothguiScreen;
import net.mcreator.apocalypsenow.world.inventory.CorpseclothguiMenu;
import net.mcreator.apocalypsenow.world.inventory.CorpsesackGUIMenu;
import net.mcreator.apocalypsenow.world.inventory.IronlockerboxguiMenu;
import net.mcreator.apocalypsenow.world.inventory.IronsafeboxGUIMenu;
import net.mcreator.apocalypsenow.world.inventory.MedicalcrateGUIMenu;
import net.mcreator.apocalypsenow.world.inventory.MedicalcrateboxGUIMenu;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Mod(DeceasedCraftFixes.MOD_ID)
public final class DeceasedCraftFixes {
    public static final String MOD_ID = "dcfixes";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final List<Class<? extends ScreenHandler>> QUARK_ALLOWED_HANDLERS = List.of(
            GenericContainerScreenHandler.class,
            MedicalcrateboxGUIMenu.class,
            MedicalcrateGUIMenu.class,
            IronlockerboxguiMenu.class,
            IronsafeboxGUIMenu.class,
            CorpseclothguiMenu.class,
            CorpsesackGUIMenu.class
    );

    public DeceasedCraftFixes() {
        LOGGER.info("Fixing DeceasedCraft");
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> DeceasedCraftFixesClient::init);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    public static void onChunkLoaded(ServerWorld world, Chunk chunk) {
        FasterLostCities.getData(world).executeAndClearTodo(world, chunk);
    }
}
