package com.bawnorton.dcfixes.client.extend;

import minecrafttransportsimulator.baseclasses.ColorRGB;
import minecrafttransportsimulator.baseclasses.TransformationMatrix;
import minecrafttransportsimulator.entities.components.AEntityD_Definable;
import minecrafttransportsimulator.rendering.AModelParser;
import minecrafttransportsimulator.rendering.RenderableData;
import minecrafttransportsimulator.rendering.RenderableVertices;
import minecrafttransportsimulator.systems.ConfigSystem;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public interface AEntityD_DefinableExtender {
    class StaticModelBatch {
        private final RenderableData renderable;
        private final String textureOverride;
        private final boolean isWindowBatch;
        private final boolean isInteriorWindowBatch;

        public StaticModelBatch(String cacheKey, List<RenderableVertices> vertices, String textureOverride, boolean isWindowBatch, boolean isInteriorWindowBatch) {
            this.textureOverride = textureOverride;
            this.isWindowBatch = isWindowBatch;
            this.isInteriorWindowBatch = isInteriorWindowBatch;
            int totalFloatCount = 0;
            for (RenderableVertices vertexObject : vertices) {
                totalFloatCount += vertexObject.vertices.limit();
            }
            FloatBuffer mergedBuffer = FloatBuffer.allocate(totalFloatCount);
            for (RenderableVertices vertexObject : vertices) {
                FloatBuffer sourceBuffer = vertexObject.vertices.duplicate();
                sourceBuffer.rewind();
                mergedBuffer.put(sourceBuffer);
            }
            mergedBuffer.flip();
            String vertexName = cacheKey.contains("translucent") ? cacheKey + "_" + AModelParser.TRANSLUCENT_OBJECT_NAME : cacheKey;
            this.renderable = new RenderableData(new RenderableVertices(vertexName, mergedBuffer, true));
        }

        public void render(AEntityD_Definable<?> entity, TransformationMatrix transform, boolean blendingEnabled) {
            if (isWindowBatch && !ConfigSystem.client.renderingSettings.renderWindows.value) {
                return;
            }
            if (isInteriorWindowBatch && !ConfigSystem.client.renderingSettings.innerWindows.value) {
                return;
            }
            if (renderable.isTranslucent != blendingEnabled) {
                return;
            }
            renderable.transform.set(transform);
            renderable.setTexture(textureOverride != null ? textureOverride : entity.getTexture());
            renderable.setColor(ColorRGB.WHITE);
            renderable.setAlpha(1.0F);
            renderable.setLightValue(entity.worldLightValue);
            renderable.setLightMode(RenderableData.LightingMode.NORMAL);
            renderable.setBlending(false);
            renderable.render();
        }

        public void destroy() {
            renderable.destroy();
        }
    }

    class StaticBatchBuildData {
        public final List<RenderableVertices> vertices = new ArrayList<>();
        public final String textureOverride;
        public final boolean windowBatch;
        public final boolean interiorWindowBatch;

        public StaticBatchBuildData(String textureOverride, boolean windowBatch, boolean interiorWindowBatch) {
            this.textureOverride = textureOverride;
            this.windowBatch = windowBatch;
            this.interiorWindowBatch = interiorWindowBatch;
        }
    }
}
