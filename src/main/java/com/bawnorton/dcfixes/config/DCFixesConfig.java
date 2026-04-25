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

    @SerialEntry(
            value = "reinforced_part_energy_capacity",
            comment = "Default: 20000"
    )
    public int reinforcedPartEnergyCapacity = 20000;

    @SerialEntry(
            value = "reinforced_energy_generation_efficiency",
            comment = "Default: 1.0"
    )
    public float reinforcedEnergyGenerationEfficiency = 1;

    @SerialEntry(
            value = "reinforced_max_energy_extraction_rate",
            comment = "Default: 50000000.0"
    )
    public double reinforcedMaxEnergyExtractionRate = 5.0E7;

    @SerialEntry(
            value = "reinforced_max_charger_rate",
            comment = "Default: 25000.0"
    )
    public double reinforcedMaxChargerRate = 25000;

    @SerialEntry(
            value = "reinforced_radiation_attenuation",
            comment = "Default: 0.75"
    )
    public float reinforcedRadiationAttenuation = 0.75F;

    @SerialEntry(
            value = "reinforced_residual_radiation_attenuation",
            comment = "Default: 0.15"
    )
    public float reinforcedResidualRadiationAttenuation = 0.15F;

    @SerialEntry(
            value = "reinforced_max_permitted_flow",
            comment = "Default: 2000"
    )
    public int reinforcedMaxPermittedFlow = 2000;

    @SerialEntry(
            value = "reinforced_base_fluid_per_blade",
            comment = "Default: 25"
    )
    public int reinforcedBaseFluidPerBlade = 25;

    @SerialEntry(
            value = "reinforced_rotor_drag_coefficient",
            comment = "Default: 0.01"
    )
    public float reinforcedRotorDragCoefficient = 0.01F;

    @SerialEntry(
            value = "reinforced_max_rotor_speed",
            comment = "Default: 2000.0"
    )
    public float reinforcedMaxRotorSpeed = 2000;

    @SerialEntry(
            value = "reinforced_rotor_blade_mass",
            comment = "Default: 10"
    )
    public int reinforcedRotorBladeMass = 10;

    @SerialEntry(
            value = "reinforced_rotor_shaft_mass",
            comment = "Default: 10"
    )
    public int reinforcedRotorShaftMass = 10;

    @SerialEntry(
            value = "reinforced_part_fluid_capacity",
            comment = "Default: 500"
    )
    public int reinforcedPartFluidCapacity = 500;

    @SerialEntry(
            value = "reinforced_max_fluid_capacity",
            comment = "Default: 300000"
    )
    public int reinforcedMaxFluidCapacity = 300000;

    @SerialEntry(
            value = "hordes_wave_loaded_distance_buffer",
            comment = "Determines how far, in blocks, from the furthest loaded block to the player should the horde waves spawn.\nDefault: 32"
    )
    public int hordeWavesLoadedDistanceBuffer = 32;

    @SerialEntry(
            value = "ragdoll_debug",
            comment = "Enable ragdoll part debug rendering and logging.\nDefault: False"
    )
    public boolean ragdollDebug = false;

    @SerialEntry(
            value = "render_player_shadows",
            comment = "Override player shadow rendering in iris shaders.\nDefault: True"
    )
    public boolean renderPlayerShadows = true;

    @SerialEntry(
            value = "iron_ammo_box_stack_count",
            comment = "Set the stack count of iron ammo boxes. Set to -1 to disable this change.\nDefault: -1"
    )
    public int ironAmmoBoxStackCount = -1;

    @SerialEntry(
            value = "gold_ammo_box_stack_count",
            comment = "Set the stack count of gold ammo boxes. Set to -1 to disable this change.\nDefault: -1"
    )
    public int goldAmmoBoxStackCount = -1;

    @SerialEntry(
            value = "diamond_ammo_box_stack_count",
            comment = "Set the stack count of diamond ammo boxes. Set to -1 to disable this change.\nDefault: -1"
    )
    public int diamondAmmoBoxStackCount = -1;

    @SerialEntry(
            value = "sugar_rush_movement_speed_bonus",
            comment = "Effect applifier times this value is the movement speed bonus given by sugar rush.\nDefault: 0.05"
    )
    public double sugarRushMovementSpeedBonus = 0.05;

    @SerialEntry(
            value = "sugar_rush_attack_speed_bonus",
            comment = "Effect applifier times this value is the attack speed bonus given by sugar rush.\nDefault: 0.5"
    )
    public double sugarRushAttackSpeedBons = 0.5;

    @SerialEntry(
            value = "sugar_rush_attack_damage_bonus",
            comment = "Effect applifier times this value is the attack damage bonus given by sugar rush.\nDefault: 1.0"
    )
    public double sugarRushAttackDamageBonus = 1;

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
