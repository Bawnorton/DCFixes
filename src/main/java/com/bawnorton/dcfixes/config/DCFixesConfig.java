package com.bawnorton.dcfixes.config;

import com.bawnorton.dcfixes.DeceasedCraftFixes;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;

public class DCFixesConfig {
	public static ConfigClassHandler<DCFixesConfig> HANDLER = ConfigClassHandler.createBuilder(DCFixesConfig.class)
			.id(DeceasedCraftFixes.rl("config"))
			.serializer(config -> GsonConfigSerializerBuilder.create(config)
					.setPath(FMLPaths.CONFIGDIR.get().resolve("dcfixes.json5"))
					.setJson5(true)
					.appendGsonBuilder(gsonBuilder ->
							gsonBuilder.registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
					)
					.build())
			.build();

	private static boolean initialized = false;

	@SerialEntry(
			value = "frustum_cull_expansion",
			comment = "How much to expand the frustum culling box for IV entities.\nDefault: 2.0"
	)
	public double frustumCullExpansion = 2;

	@SerialEntry(
			value = "dormant_dynamic_batch_distance",
			comment = "The distance at which dynamic IV entities are rendered with static batching. The distance is calculated from the nearest player to the IV entity.\nDefault: 16.0"
	)
	public double dormantDynamicBatchDistance = 16;

	@SerialEntry(
			value = "particle_spawn_distance",
			comment = "The distance at which IV entities will spawn particles. The distance is calculated from the camera to the IV entity.\nDefault: 16.0"
	)
	public double particleSpawnDistance = 16;

	@SerialEntry(
			value = "instrument_render_distance",
			comment = "The distance at which instruments on IV entities will render. The distance is calculated from the camera to the IV entity.\nDefault: 16.0"
	)
	public double instrumentRenderDistance = 16;
	
	@SerialEntry(
			value = "text_render_distance",
			comment = "The distance at which text on IV entities will render. The distance is calculated from the camera to the IV entity.\nDefault: 32.0"
	)
	public double textRenderDistance = 32;
	
	@SerialEntry(
			value = "dormant_ticking_distance",
			comment = "The distance at which IV entities should be within relative to the nearest player to tick.\nDefault: 16.0"
	)
	public double dormatTickingDistance = 16;

	@SerialEntry(
			value = "aircraft_always_tick",
			comment = "Whether aircraft IV entities should always tick regardless of distance to a player.\nNote: Aircraft will still not tick outside simulated distance if 'tick_outside_simulated_distance' is false.\nDefault: True"
	)
	public boolean aircraftAlwaysTick = true;
	
	

	public static void init() {
		if (initialized) return;

		HANDLER.load();
		initialized = true;
	}

	public static DCFixesConfig get() {
		init();
		return HANDLER.instance();
	}

	public static void save() {
		HANDLER.save();
	}
}
