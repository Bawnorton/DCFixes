package com.bawnorton.dcfixes.client.mixin.immersive_vehicles;

import com.bawnorton.dcfixes.client.extend.AEntityD_DefinableExtender;
import com.bawnorton.dcfixes.client.extend.RenderableModelObjectExtender;
import com.bawnorton.dcfixes.config.DCFixesConfig;
import com.bawnorton.dcfixes.mixin.accessor.WrapperWorldAccessor;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import minecrafttransportsimulator.baseclasses.*;
import minecrafttransportsimulator.entities.components.AEntityC_Renderable;
import minecrafttransportsimulator.entities.components.AEntityD_Definable;
import minecrafttransportsimulator.entities.instances.EntityParticle;
import minecrafttransportsimulator.jsondefs.JSONParticle;
import minecrafttransportsimulator.jsondefs.JSONText;
import minecrafttransportsimulator.mcinterface.AWrapperWorld;
import minecrafttransportsimulator.mcinterface.IWrapperNBT;
import minecrafttransportsimulator.mcinterface.IWrapperPlayer;
import minecrafttransportsimulator.mcinterface.InterfaceManager;
import minecrafttransportsimulator.rendering.*;
import minecrafttransportsimulator.systems.ConfigSystem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@MixinEnvironment("client")
@Mixin(value = AEntityD_Definable.class, remap = false)
abstract class AEntityD_DefinableMixin extends AEntityC_Renderable implements AEntityD_DefinableExtender {
    @Shadow
    private List<RenderableModelObject> objectList;
    @Shadow
    @Final
    public Set<IWrapperPlayer> playersInteracting;

    @Shadow
    public abstract void updateLightBrightness(float partialTicks);

    @Shadow
    @Final
    public Map<JSONText, String> text;

    @Shadow
    public abstract boolean renderTextLit();

    @Shadow
    private long lastTickParticlesSpawned;
    @Shadow
    private float lastPartialTickParticlesSpawned;

    @Shadow
    public abstract ComputedVariable getOrCreateVariable(String variable);

    @Shadow
    @Final
    private Map<JSONParticle, AnimationSwitchbox> particleActiveSwitchboxes;
    @Shadow
    @Final
    private Map<JSONParticle, AnimationSwitchbox> particleSpawningSwitchboxes;
    @Shadow
    @Final
    private Map<JSONParticle, Point3D> lastPositionParticleSpawned;
    @Shadow
    @Final
    private static Point3D particleSpawningPosition;
    @Shadow
    @Final
    private Map<JSONParticle, Long> lastTickParticleSpawned;
    @Unique
    private final Map<String, Double> dcfixes$cachedParticleAnimatedVariableValues = new HashMap<>();
    @Unique
    private boolean dcfixes$particleAnimatedVariableCacheActive;
    @Unique
    private float dcfixes$particleAnimatedVariableCachePartialTicks;
    @Unique
    private List<RenderableModelObject> dcfixes$dynamicObjectList;
    @Unique
    private final Map<String, StaticModelBatch> dcfixes$staticModelBatches = new LinkedHashMap<>();
    @Unique
    private final Map<String, StaticModelBatch> dcfixes$dormantDynamicModelBatches = new LinkedHashMap<>();
    @Unique
    private int dcfixes$cachedStaticBatchCount;
    @Unique
    private int dcfixes$cachedDormantBatchCount;
    @Unique
    private int dcfixes$cachedDynamicObjectCount;
    @Unique
    private boolean dcfixes$wasUsingDormantDynamicBatches;
    @Unique
    private boolean dcfixes$dormantBatchRebuildRequested = true;

    protected AEntityD_DefinableMixin(AWrapperWorld world, IWrapperNBT data) {
        super(world, data);
    }

    @Inject(
            method = "remove",
            at = @At("TAIL")
    )
    private void clearCache(CallbackInfo ci) {
        if(isValid && world.isClient()) {
            dcfixes$clearModelRenderCache();
        }
    }

    /**
     * @author Bawnorton
     * @reason performance optimisations
     */
    @Overwrite
    public void spawnParticles(float partialTicks) {
        IWrapperPlayer clientPlayer = InterfaceManager.clientInterface.getClientPlayer();
        if (clientPlayer != null && !position.isDistanceToCloserThan(clientPlayer.getPosition(), DCFixesConfig.get().particleSpawnDistance)) {
            return;
        }
        dcfixes$particleAnimatedVariableCacheActive = true;
        dcfixes$particleAnimatedVariableCachePartialTicks = partialTicks;
        dcfixes$cachedParticleAnimatedVariableValues.clear();
        //Check all particle defs and update the existing particles accordingly.
        try {
            for (Map.Entry<JSONParticle, AnimationSwitchbox> particleEntry : particleActiveSwitchboxes.entrySet()) {
                //Check if the particle should be spawned this tick.
                JSONParticle particleDef = particleEntry.getKey();
                AnimationSwitchbox switchbox = particleEntry.getValue();
                boolean shouldParticleSpawn = switchbox.runSwitchbox(partialTicks, false);

                //Make the particle spawn if able.
                if (shouldParticleSpawn) {
                    AnimationSwitchbox spawningSwitchbox = particleSpawningSwitchboxes.get(particleDef);
                    if (particleDef.distance > 0) {
                        //First get spawning position as defined by JSON and animations.
                        EntityParticle.setPointToSpawn(position, particleDef.spawningOrientation == JSONParticle.ParticleSpawningOrientation.WORLD ? null : orientation, particleDef.pos, scale, spawningSwitchbox, particleSpawningPosition);

                        //Now check if we need to spawn.
                        Point3D lastParticlePosition = lastPositionParticleSpawned.get(particleDef);
                        if (lastParticlePosition == null) {
                            lastPositionParticleSpawned.put(particleDef, particleSpawningPosition.copy());
                            continue;//First tick we are active, checks are assured to fail.
                        }
                        while (!lastParticlePosition.isDistanceToCloserThan(particleSpawningPosition, particleDef.distance)) {
                            double distanceFactor = particleDef.distance / particleSpawningPosition.distanceTo(lastParticlePosition);
                            Point3D spawningPosition = lastParticlePosition.copy().interpolate(particleSpawningPosition, distanceFactor);
                            for (int i = 0; i < particleDef.quantity; ++i) {
                                //No need to run switchbox here since it will have been done in the setPointToSpawn method above.
                                Point3D angles = null;
                                if (particleDef.spawningOrientation == JSONParticle.ParticleSpawningOrientation.STREAK) {
                                    angles = spawningPosition.copy().subtract(lastParticlePosition).getAngles(true);
                                }
                                world.addEntity(new EntityParticle(this, particleDef, spawningPosition, angles, spawningSwitchbox));
                            }
                            lastParticlePosition.set(spawningPosition);
                        }
                    } else {
                        //If we've never spawned the particle, or have waited a whole tick for constant-spawners, spawn one now.
                        Long particleSpawnTime = lastTickParticleSpawned.get(particleDef);
                        if (particleSpawnTime == null || (particleDef.spawnEveryTick && ticksExisted > particleSpawnTime)) {
                            for (int i = 0; i < particleDef.quantity; ++i) {
                                world.addEntity(new EntityParticle(this, particleDef, position, null, spawningSwitchbox));
                            }
                            lastTickParticleSpawned.put(particleDef, ticksExisted);
                        }
                    }
                } else {
                    lastTickParticleSpawned.remove(particleDef);
                    lastPositionParticleSpawned.remove(particleDef);
                }
            }
        } finally {
            dcfixes$particleAnimatedVariableCacheActive = false;
            dcfixes$cachedParticleAnimatedVariableValues.clear();
        }
    }

    /**
     * @author Bawnorton
     * @reason performance optimisations
     */
    @Overwrite
    public final double getAnimatedVariableValue(DurationDelayClock clock, double scaleFactor, double offset, float partialTicks) {
        String variableName = clock.animation.variable;
        double value;
        if (dcfixes$particleAnimatedVariableCacheActive && Float.compare(partialTicks, dcfixes$particleAnimatedVariableCachePartialTicks) == 0 && !dcfixes$isRandomAnimationVariable(variableName)) {
            value = dcfixes$cachedParticleAnimatedVariableValues.computeIfAbsent(variableName, key -> getOrCreateVariable(key).computeValue(partialTicks));
        } else {
            value = getOrCreateVariable(variableName).computeValue(partialTicks);
        }
        return clock.clampAndScale((AEntityD_Definable<?>) (Object) this, value, scaleFactor, offset, partialTicks);
    }

    @Unique
    private static boolean dcfixes$isRandomAnimationVariable(String variableName) {
        return variableName.startsWith("random") || variableName.startsWith(ComputedVariable.INVERTED_PREFIX + "random");
    }

    /**
     * @author Bawnorton
     * @reason performance optimisations
     */
    @Overwrite
    protected void renderModel(TransformationMatrix transform, boolean blendingEnabled, float partialTicks) {
        //Update internal lighting states.
        world.beginProfiling("LightStateUpdates", true);
        updateLightBrightness(partialTicks);

        //Parse model if it hasn't been already.
        world.beginProfiling("MainModel", false);
        if (objectList == null) {
            objectList = AModelParser.generateRenderables((AEntityD_Definable<?>) (Object) this);
            dcfixes$buildModelRenderCache();
        } else if (dcfixes$dynamicObjectList == null) {
            dcfixes$buildModelRenderCache();
        }

        //Render static batches first, then dynamic object instances.
        world.beginProfiling("MainModel_Static_" + dcfixes$cachedStaticBatchCount, false);
        dcfixes$staticModelBatches.values().forEach(batch -> batch.render((AEntityD_Definable<?>) (Object) this, transform, blendingEnabled));
        boolean useDormantBatches = dcfixes$shouldUseDormantDynamicBatches();
        if (useDormantBatches) {
            dcfixes$rebuildDormantDynamicModelBatches(partialTicks, !dcfixes$wasUsingDormantDynamicBatches);
            dcfixes$wasUsingDormantDynamicBatches = true;
        } else {
            dcfixes$wasUsingDormantDynamicBatches = false;
        }
        boolean dormantBatchesReady = useDormantBatches && dcfixes$canUseDormantDynamicBatches();
        if (dormantBatchesReady) {
            world.beginProfiling("MainModel_DormantDynamic_" + dcfixes$cachedDormantBatchCount, false);
            dcfixes$dormantDynamicModelBatches.values().forEach(batch -> batch.render((AEntityD_Definable<?>) (Object) this, transform, blendingEnabled));
        }
        world.beginProfiling("MainModel_Dynamic_" + dcfixes$cachedDynamicObjectCount, false);
        dcfixes$dynamicObjectList.forEach(modelObject -> {
            if (!dormantBatchesReady) {
                modelObject.render((AEntityD_Definable<?>) (Object) this, transform, blendingEnabled, partialTicks);
            }
        });

        //Render any static text.
        world.beginProfiling("MainText", false);
        IWrapperPlayer clientPlayer = InterfaceManager.clientInterface.getClientPlayer();
        if (clientPlayer == null || position.isDistanceToCloserThan(clientPlayer.getPosition(), DCFixesConfig.get().textRenderDistance)) {
            for (Map.Entry<JSONText, String> textEntry : text.entrySet()) {
                JSONText textDef = textEntry.getKey();
                if (textDef.attachedTo == null) {
                    boolean isLitTexture = textDef.lightsUp && renderTextLit();
                    if (isLitTexture ? (ConfigSystem.client.renderingSettings.lightsTransp.value == blendingEnabled) : (!blendingEnabled)) {
                        RenderText.draw3DText(textEntry.getValue(), (AEntityD_Definable<?>) (Object) this, transform, textDef, false, isLitTexture);
                    }
                }
            }
        }
        //Handle particles.  Need to only do this once per frame-render.  Shaders may have us render multiple times.
        if (!InterfaceManager.clientInterface.isGamePaused() && !(ticksExisted == lastTickParticlesSpawned && partialTicks == lastPartialTickParticlesSpawned)) {
            world.beginProfiling("Particles", false);
            spawnParticles(partialTicks);
            lastTickParticlesSpawned = ticksExisted;
            lastPartialTickParticlesSpawned = partialTicks;
        }
        world.endProfiling();
    }

    @Definition(id = "objectList", field = "Lminecrafttransportsimulator/entities/components/AEntityD_Definable;objectList:Ljava/util/List;")
    @Expression("?.objectList != null")
    @Inject(
            method = "resetModelsAndAnimations",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private void clearModelRenderCacheOnReset(CallbackInfo ci) {
        dcfixes$clearModelRenderCache();
    }

    @Unique
    private void dcfixes$clearModelRenderCache() {
        if (!dcfixes$staticModelBatches.isEmpty()) {
            dcfixes$staticModelBatches.values().forEach(StaticModelBatch::destroy);
            dcfixes$staticModelBatches.clear();
        }
        if (!dcfixes$dormantDynamicModelBatches.isEmpty()) {
            dcfixes$dormantDynamicModelBatches.values().forEach(StaticModelBatch::destroy);
            dcfixes$dormantDynamicModelBatches.clear();
        }
        dcfixes$dynamicObjectList = null;
        dcfixes$wasUsingDormantDynamicBatches = false;
        dcfixes$dormantBatchRebuildRequested = true;
        dcfixes$cachedStaticBatchCount = 0;
        dcfixes$cachedDormantBatchCount = 0;
        dcfixes$cachedDynamicObjectCount = 0;
    }

    @Unique
    private void dcfixes$buildModelRenderCache() {
        dcfixes$clearModelRenderCache();
        Map<String, StaticBatchBuildData> batchableObjects = new LinkedHashMap<>();
        dcfixes$dynamicObjectList = new ArrayList<>();
        objectList.forEach(modelObject -> {
            RenderableModelObjectExtender modelObjectExtender = (RenderableModelObjectExtender) modelObject;
            if (modelObjectExtender.dcfixes$shouldRenderDynamically((AEntityD_Definable<?>) (Object) this)) {
                dcfixes$dynamicObjectList.add(modelObject);
            } else {
                RenderableVertices baseVertices = modelObjectExtender.dcfixes$getVertexObject();
                String baseTexture = modelObjectExtender.dcfixes$getStaticTexture();
                String baseKey = dcfixes$createStaticBatchKey(modelObjectExtender.dcfixes$getDebugJsonObjectType(), false, baseVertices.isTranslucent, baseTexture);
                StaticBatchBuildData baseData = batchableObjects.computeIfAbsent(baseKey, key -> new StaticBatchBuildData(baseTexture, modelObjectExtender.dcfixes$isWindowObject(), false));
                baseData.vertices.add(baseVertices);

                RenderableVertices interiorVertices = modelObjectExtender.dcfixes$getInteriorWindowVertexObject();
                if (interiorVertices != null) {
                    String interiorKey = dcfixes$createStaticBatchKey(modelObjectExtender.dcfixes$getDebugJsonObjectType(), true, interiorVertices.isTranslucent, baseTexture);
                    StaticBatchBuildData interiorData = batchableObjects.computeIfAbsent(interiorKey, key -> new StaticBatchBuildData(baseTexture, true, true));
                    interiorData.vertices.add(interiorVertices);
                }
            }
        });
        batchableObjects.forEach((cacheKey, data) -> dcfixes$staticModelBatches.put(cacheKey, new StaticModelBatch(cacheKey, data.vertices, data.textureOverride, data.windowBatch, data.interiorWindowBatch)));
        dcfixes$rebuildDormantDynamicModelBatches(0, true);
        dcfixes$cachedStaticBatchCount = dcfixes$staticModelBatches.size();
        dcfixes$cachedDynamicObjectCount = dcfixes$dynamicObjectList.size();
    }

    @Unique
    private void dcfixes$rebuildDormantDynamicModelBatches(float partialTicks, boolean forceRebuild) {
        if (dcfixes$dynamicObjectList == null) {
            return;
        }
        if (!forceRebuild && !dcfixes$dormantBatchRebuildRequested && !dcfixes$dormantDynamicModelBatches.isEmpty()) {
            return;
        }
        dcfixes$dormantBatchRebuildRequested = false;

        if (!dcfixes$dormantDynamicModelBatches.isEmpty()) {
            dcfixes$dormantDynamicModelBatches.values().forEach(StaticModelBatch::destroy);
            dcfixes$dormantDynamicModelBatches.clear();
        }

        Map<String, StaticBatchBuildData> dormantBatchableDynamicObjects = new LinkedHashMap<>();
        for (RenderableModelObject modelObject : dcfixes$dynamicObjectList) {
            RenderableModelObjectExtender modelObjectExtender = (RenderableModelObjectExtender) modelObject;
            RenderableModelObjectExtender.DormantRenderableSnapshot snapshot = modelObjectExtender.dcfixes$createDormantSnapshot((AEntityD_Definable<?>) (Object) this, partialTicks);
            if (snapshot == null) {
                continue;
            }
            String baseKey = dcfixes$createStaticBatchKey(modelObjectExtender.dcfixes$getDebugJsonObjectType() + "_dormant", false, snapshot.baseVertices().isTranslucent, snapshot.texture());
            StaticBatchBuildData baseData = dormantBatchableDynamicObjects.computeIfAbsent(baseKey, key -> new StaticBatchBuildData(snapshot.texture(), snapshot.windowBatch(), false));
            baseData.vertices.add(snapshot.baseVertices());

            if (snapshot.interiorVertices() != null) {
                String interiorKey = dcfixes$createStaticBatchKey(modelObjectExtender.dcfixes$getDebugJsonObjectType() + "_dormant", true, snapshot.interiorVertices().isTranslucent, snapshot.texture());
                StaticBatchBuildData interiorData = dormantBatchableDynamicObjects.computeIfAbsent(interiorKey, key -> new StaticBatchBuildData(snapshot.texture(), true, true));
                interiorData.vertices.add(snapshot.interiorVertices());
            }
        }
        dormantBatchableDynamicObjects.forEach((cacheKey, data) -> dcfixes$dormantDynamicModelBatches.put(cacheKey, new StaticModelBatch(cacheKey, data.vertices, data.textureOverride, data.windowBatch, data.interiorWindowBatch)));
        dcfixes$cachedDormantBatchCount = dcfixes$dormantDynamicModelBatches.size();
    }

    @Unique
    private boolean dcfixes$shouldUseDormantDynamicBatches() {
        if (!playersInteracting.isEmpty()) {
            return false;
        }

        Level level = ((WrapperWorldAccessor) world).dcfixes$world();
        Player nearestPlayer = level.getNearestPlayer(position.x, position.y, position.z, DCFixesConfig.get().dormantDynamicBatchDistance, false);
        return nearestPlayer == null;
    }

    @Unique
    private boolean dcfixes$canUseDormantDynamicBatches() {
        return !dcfixes$dormantDynamicModelBatches.isEmpty();
    }

    @Unique
    private static String dcfixes$createStaticBatchKey(String debugType, boolean interiorWindowLayer, boolean translucent, String textureOverride) {
        return debugType + "|" + (interiorWindowLayer ? "interior" : "base") + "|" + (translucent ? "translucent" : "solid") + "|" + (textureOverride != null ? textureOverride : "entityTexture");
    }
}
