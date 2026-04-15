package com.bawnorton.dcfixes.client.mixin.immersive_vehicles;

import com.bawnorton.dcfixes.client.extend.RenderableModelObjectExtender;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Cancellable;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import minecrafttransportsimulator.baseclasses.AnimationSwitchbox;
import minecrafttransportsimulator.baseclasses.TransformationMatrix;
import minecrafttransportsimulator.entities.components.AEntityD_Definable;
import minecrafttransportsimulator.jsondefs.JSONAnimatedObject;
import minecrafttransportsimulator.jsondefs.JSONLight;
import minecrafttransportsimulator.jsondefs.JSONText;
import minecrafttransportsimulator.rendering.RenderableData;
import minecrafttransportsimulator.rendering.RenderableModelObject;
import minecrafttransportsimulator.rendering.RenderableVertices;
import minecrafttransportsimulator.systems.ConfigSystem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@MixinEnvironment("client")
@Mixin(value = RenderableModelObject.class, remap = false)
abstract class RenderableModelObjectMixin implements RenderableModelObjectExtender {
    @Shadow
    @Final
    public RenderableData renderable;

    @Shadow
    @Final
    private static Map<String, String> erroredTextures;

    @Shadow
    @Final
    private static String ERROR_TEXTURE_NAME;

    @Shadow
    @Final
    private static Set<String> downloadedTextures;

    @Shadow
    @Final
    private static Set<String> downloadingTextures;

    @Shadow
    @Final
    private boolean isWindow;

    @Shadow
    @Final
    private AnimationSwitchbox switchbox;

    @Shadow
    @Final
    private JSONAnimatedObject objectDef;

    @Shadow
    @Final
    private boolean isOnlineTexture;

    @Shadow
    @Final
    private JSONLight lightDef;

    @Shadow
    @Final
    private List<Double[]> treadPoints;

    @Shadow
    @Final
    private RenderableData interiorWindowRenderable;

    @ModifyExpressionValue(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Set;iterator()Ljava/util/Iterator;",
                    ordinal = 0
            )
    )
    private <E> Iterator<E> useSmarterTextureGetter(Iterator<E> original, AEntityD_Definable<?> entity, @Cancellable CallbackInfo ci) {
        String onlineTexture = dcfixes$resolveOnlineTexture(entity);
        if (onlineTexture == null) {
            ci.cancel();
            return Collections.emptyIterator();
        }
        renderable.setTexture(onlineTexture);
        return Collections.emptyIterator();
    }

    @Unique
    public boolean dcfixes$shouldRenderDynamically(AEntityD_Definable<?> entity) {
        if (switchbox != null || lightDef != null || isOnlineTexture || treadPoints != null) {
            return true;
        }
        for (JSONText textDef : entity.text.keySet()) {
            if (renderable.vertexObject.name.equals(textDef.attachedTo)) {
                return true;
            }
        }
        return false;
    }

    @Unique
    public String dcfixes$getDebugJsonObjectType() {
        if (lightDef != null) {
            return "light";
        }
        if (objectDef != null) {
            return "animated";
        }
        if (isWindow) {
            return "window";
        }
        return "model";
    }

    @Unique
    public RenderableVertices dcfixes$getVertexObject() {
        return renderable.vertexObject;
    }

    @Unique
    public RenderableVertices dcfixes$getInteriorWindowVertexObject() {
        return interiorWindowRenderable != null ? interiorWindowRenderable.vertexObject : null;
    }

    @Unique
    public String dcfixes$getStaticTexture() {
        return renderable.texture;
    }

    @Unique
    public boolean dcfixes$isWindowObject() {
        return isWindow;
    }

    @Unique
    public DormantRenderableSnapshot dcfixes$createDormantSnapshot(AEntityD_Definable<?> entity, float partialTicks) {
        if (!dcfixes$shouldRenderForDormantSnapshot(partialTicks)) {
            return null;
        }

        String snapshotTexture = dcfixes$getSnapshotTexture(entity);
        if (snapshotTexture == null && isOnlineTexture) {
            return null;
        }

        TransformationMatrix snapshotTransform = switchbox != null ? new TransformationMatrix().set(switchbox.netMatrix) : null;
        RenderableVertices baseVertices = RenderableModelObjectExtender.createSnapshotVertices(renderable.vertexObject, snapshotTransform);
        RenderableVertices interiorVertices = interiorWindowRenderable != null ? RenderableModelObjectExtender.createSnapshotVertices(interiorWindowRenderable.vertexObject, snapshotTransform) : null;
        return new RenderableModelObjectExtender.DormantRenderableSnapshot(baseVertices, interiorVertices, snapshotTexture, isWindow);
    }

    @Unique
    private boolean dcfixes$shouldRenderForDormantSnapshot(float partialTicks) {
        if (isWindow && !ConfigSystem.client.renderingSettings.renderWindows.value) {
            return false;
        }
        if (switchbox == null) {
            return true;
        }
        if (objectDef != null && objectDef.blendedAnimations) {
            switchbox.runSwitchbox(partialTicks, false);
            return switchbox.lastVisibilityClock == null || switchbox.lastVisibilityValue > switchbox.lastVisibilityClock.animation.clampMin;
        }
        return switchbox.runSwitchbox(partialTicks, false);
    }

    @Unique
    private String dcfixes$getSnapshotTexture(AEntityD_Definable<?> entity) {
        if (isOnlineTexture) {
            return dcfixes$resolveOnlineTexture(entity);
        }
        return isWindow ? renderable.texture : entity.getTexture();
    }

    @Unique
    private String dcfixes$resolveOnlineTexture(AEntityD_Definable<?> entity) {
        for (Map.Entry<JSONText, String> textEntry : entity.text.entrySet()) {
            JSONText textDef = textEntry.getKey();
            if (textDef.fieldName != null && renderable.vertexObject.name.contains(textDef.fieldName)) {
                String textValue = textEntry.getValue();
                if (erroredTextures.containsKey(textValue)) {
                    textValue = erroredTextures.get(textValue);
                    textEntry.setValue(textValue);
                }
                if (textValue.startsWith(ERROR_TEXTURE_NAME)) {
                    return ERROR_TEXTURE_NAME;
                }
                if (downloadedTextures.contains(textValue)) {
                    return textValue;
                }
                if (downloadingTextures.contains(textValue)) {
                    return null;
                }
                if (textValue.isEmpty()) {
                    return null;
                }

//                RenderableModelObject$ConnectorThreadAccessor.dcfixes$create(textValue).run();
                downloadingTextures.add(textValue);
                return null;
            }
        }
        return null;
    }
}
