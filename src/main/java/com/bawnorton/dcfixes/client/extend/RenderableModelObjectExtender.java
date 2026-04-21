package com.bawnorton.dcfixes.client.extend;

import minecrafttransportsimulator.baseclasses.TransformationMatrix;
import minecrafttransportsimulator.entities.components.AEntityD_Definable;
import minecrafttransportsimulator.rendering.RenderableVertices;

import java.nio.FloatBuffer;

public interface RenderableModelObjectExtender {
    int FLOATS_PER_VERTEX = 8;
    int VERTEX_BUFFER_NX_OFFSET = 0;
    int VERTEX_BUFFER_NY_OFFSET = 1;
    int VERTEX_BUFFER_NZ_OFFSET = 2;
    int VERTEX_BUFFER_X_OFFSET = 5;
    int VERTEX_BUFFER_Y_OFFSET = 6;
    int VERTEX_BUFFER_Z_OFFSET = 7;

    static RenderableVertices createSnapshotVertices(RenderableVertices sourceVertices, TransformationMatrix snapshotTransform) {
        if (snapshotTransform == null) {
            FloatBuffer sourceBuffer = sourceVertices.vertices.duplicate();
            sourceBuffer.rewind();
            FloatBuffer copiedBuffer = FloatBuffer.allocate(sourceBuffer.limit());
            copiedBuffer.put(sourceBuffer);
            copiedBuffer.flip();
            return new RenderableVertices(sourceVertices.name + "_dormant", copiedBuffer, true);
        }

        FloatBuffer sourceBuffer = sourceVertices.vertices.duplicate();
        sourceBuffer.rewind();
        FloatBuffer transformedBuffer = FloatBuffer.allocate(sourceBuffer.limit());
        while (sourceBuffer.hasRemaining()) {
            float[] vertexData = new float[FLOATS_PER_VERTEX];
            sourceBuffer.get(vertexData);

            double nx = vertexData[VERTEX_BUFFER_NX_OFFSET];
            double ny = vertexData[VERTEX_BUFFER_NY_OFFSET];
            double nz = vertexData[VERTEX_BUFFER_NZ_OFFSET];
            vertexData[VERTEX_BUFFER_NX_OFFSET] = (float) (snapshotTransform.m00 * nx + snapshotTransform.m01 * ny + snapshotTransform.m02 * nz);
            vertexData[VERTEX_BUFFER_NY_OFFSET] = (float) (snapshotTransform.m10 * nx + snapshotTransform.m11 * ny + snapshotTransform.m12 * nz);
            vertexData[VERTEX_BUFFER_NZ_OFFSET] = (float) (snapshotTransform.m20 * nx + snapshotTransform.m21 * ny + snapshotTransform.m22 * nz);

            double x = vertexData[VERTEX_BUFFER_X_OFFSET];
            double y = vertexData[VERTEX_BUFFER_Y_OFFSET];
            double z = vertexData[VERTEX_BUFFER_Z_OFFSET];
            vertexData[VERTEX_BUFFER_X_OFFSET] = (float) (snapshotTransform.m00 * x + snapshotTransform.m01 * y + snapshotTransform.m02 * z + snapshotTransform.m03);
            vertexData[VERTEX_BUFFER_Y_OFFSET] = (float) (snapshotTransform.m10 * x + snapshotTransform.m11 * y + snapshotTransform.m12 * z + snapshotTransform.m13);
            vertexData[VERTEX_BUFFER_Z_OFFSET] = (float) (snapshotTransform.m20 * x + snapshotTransform.m21 * y + snapshotTransform.m22 * z + snapshotTransform.m23);
            transformedBuffer.put(vertexData);
        }
        transformedBuffer.flip();
        return new RenderableVertices(sourceVertices.name + "_dormant", transformedBuffer, true);
    }

    boolean dcfixes$shouldRenderDynamically(AEntityD_Definable<?> entity);

    String dcfixes$getStaticTexture();

    RenderableVertices dcfixes$getVertexObject();

    String dcfixes$getDebugJsonObjectType();

    RenderableVertices dcfixes$getInteriorWindowVertexObject();

    boolean dcfixes$isWindowObject();

    DormantRenderableSnapshot dcfixes$createDormantSnapshot(AEntityD_Definable<?> entity, float partialTicks);

    record DormantRenderableSnapshot(RenderableVertices baseVertices, RenderableVertices interiorVertices,
                                     String texture, boolean windowBatch) {
    }
}
