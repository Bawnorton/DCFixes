package com.bawnorton.dcfixes.client.compat.physics_mod;

import com.bawnorton.dcfixes.client.DeceasedCraftFixesClient;
import com.bawnorton.dcfixes.client.compat.apocalypsenow.ModelReplacingHumanoidRenderer;
import com.bawnorton.dcfixes.client.compat.deaceased.DeaceasedCompat;
import com.bawnorton.dcfixes.client.compat.naturalist.NaturalistCompat;
import com.bawnorton.dcfixes.client.compat.undead_revamp2.UndeadRevampCompat;
import com.bawnorton.dcfixes.client.compat.zombie_extreme.ZombieExtremeCompat;
import com.bawnorton.dcfixes.client.extend.HumanoidModelExtender;
import com.bawnorton.dcfixes.client.extend.ModelPartExtender;
import com.bawnorton.dcfixes.client.extend.PhysicsEntityExtender;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.diebuddies.config.ConfigMobs;
import net.diebuddies.opengl.TextureHelper;
import net.diebuddies.physics.Mesh;
import net.diebuddies.physics.PhysicsEntity;
import net.diebuddies.physics.PhysicsMod;
import net.diebuddies.physics.StarterClient;
import net.diebuddies.physics.settings.mobs.MobPhysicsType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.joml.*;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.GeoCube;
import software.bernie.geckolib.cache.object.GeoQuad;
import software.bernie.geckolib.cache.object.GeoVertex;

import java.lang.Math;
import java.util.List;

public class PhysicsModCompat {
    public static final ThreadLocal<GeoBone> CURRENT_BONE_CAPTURE = new ThreadLocal<>();

    public void registerRagdollHooks() {
        DeceasedCraftFixesClient.getCompat().getUndeadRevampCompat().ifPresent(UndeadRevampCompat::registerRagdolls);
        DeceasedCraftFixesClient.getCompat().getZombieExtremeCompat().ifPresent(ZombieExtremeCompat::registerRagdolls);
        DeceasedCraftFixesClient.getCompat().getNaturalistCompat().ifPresent(NaturalistCompat::registerRagdolls);
        DeceasedCraftFixesClient.getCompat().getDeaceasedCompat().ifPresent(DeaceasedCompat::registerRagdolls);
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> void replaceRenderer(EntityRenderer<T> instance, T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, Operation<Void> original) {
        if(entity instanceof Mob mob && instance instanceof HumanoidMobRenderer<?,?>) {
            HumanoidMobRenderer<Mob, HumanoidModel<Mob>> humanoidMobRenderer = (HumanoidMobRenderer<Mob, HumanoidModel<Mob>>) instance;
            Object model = humanoidMobRenderer.getModel();
            if(model instanceof HumanoidModelExtender humanoidModel) {
                ModelPart modelPart = humanoidModel.dcfixes$getRoot();
                if(modelPart instanceof ModelPartExtender extender) {
                    ModelLayerLocation layerLocation = extender.dcfixes$getLocation();
                    if(ModelLayers.PLAYER.equals(layerLocation)) {
                        ModelReplacingHumanoidRenderer<Mob, HumanoidModel<Mob>> newRenderer = ModelReplacingHumanoidRenderer.create(
                                humanoidMobRenderer,
                                context -> new HumanoidModel<>(context.bakeLayer(ModelLayers.ZOMBIE)),
                                humanoidMobRenderer.getTextureLocation(mob)
                        );
                        original.call(newRenderer, entity, entityYaw, partialTick, poseStack, buffer, packedLight);
                        return;
                    }
                }
            }
        }
        original.call(instance, entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    public void createParticlesFromCuboids(PoseStack local, GeoBone owningBone, GeoCube cube, Entity entity, RenderLayer<?, ?> feature, float red, float green, float blue) {
        Matrix4f localM = local.last().pose();
        Matrix3f localNM = local.last().normal();

        PhysicsMod mod = PhysicsMod.getInstance(entity.getCommandSenderWorld());

        int textureID = TextureHelper.getLoadedTextures();

        float partialTicks = Minecraft.getInstance().getFrameTime();
        double px = Mth.lerp(partialTicks, entity.xo, entity.getX());
        double py = Mth.lerp(partialTicks, entity.yo, entity.getY());
        double pz = Mth.lerp(partialTicks, entity.zo, entity.getZ());

        Vector4f[] minMax = new Vector4f[6];
        Vector3f min = new Vector3f(Float.MAX_VALUE);
        Vector3f max = new Vector3f(-Float.MAX_VALUE);

        Vector3f tmpNormal = new Vector3f();
        Vector4f tmpPos = new Vector4f();
        for (int i = 0; i < minMax.length; i++) minMax[i] = new Vector4f();
        MobPhysicsType type = ConfigMobs.getMobSetting(entity).getType();

        Vector3f tmp = new Vector3f();

        for (int i = 0; i < cube.quads().length; i++) {
            GeoQuad quad = cube.quads()[i];

            float minU = 1.0f, maxU = 0.0f;
            float minV = 1.0f, maxV = 0.0f;

            for (GeoVertex vertex : quad.vertices()) {
                if (vertex.texU() < minU) minU = vertex.texU();
                if (vertex.texV() < minV) minV = vertex.texV();
                if (vertex.texU() > maxU) maxU = vertex.texU();
                if (vertex.texV() > maxV) maxV = vertex.texV();

                tmp.set(vertex.position().x(), vertex.position().y(), vertex.position().z());
                min.min(tmp);
                max.max(tmp);
            }

            minMax[i].set(minU, maxU, minV, maxV);
        }

        int[] remap = new int[]{3, 0, 2, 1, 4, 5};

        float volume = (Math.abs(max.x - min.x)) * (Math.abs(max.y - min.y)) * (Math.abs(max.z - min.z));
        boolean isBlocky = type == MobPhysicsType.BLOCKY || type == MobPhysicsType.RAGDOLL || type == MobPhysicsType.RAGDOLL_BREAK || type == MobPhysicsType.RAGDOLL_BREAK_BLOOD;

        boolean noVolume = false;

        if (volume <= 0.0001) {
            if (isBlocky) {
                noVolume = true;
            } else {
                return;
            }
        }

        boolean mirror = cube.mirror();

        List<Mesh> meshes = PhysicsMod.brokenBlocksLittle.get((int) (net.diebuddies.math.Math.random() * PhysicsMod.brokenBlocksLittle.size()));

        if (volume <= 0.04 || isBlocky) meshes = PhysicsMod.brokenBlock;

        for (Mesh mesh : meshes) {
            PhysicsEntity particle = new PhysicsEntity(PhysicsEntity.Type.MOB, entity.getType());
            ((PhysicsEntityExtender) particle).dcfixes$setGeoBone(owningBone);
            particle.feature = feature;
            particle.noVolume = noVolume;
            particle.models.get(0).textureID = textureID;
            Mesh clone = new Mesh();
            particle.models.get(0).mesh = clone;
            particle.getTransformation().translation(px, py, pz);
            particle.getOldTransformation().set(particle.getTransformation());
            int count = 0;
            Vector3f offset = new Vector3f();

            for (int i = 0; i < mesh.indices.size(); i++) {
                int index = mesh.indices.getInt(i);
                byte sideIndex = mesh.sides.getByte(index);

                Vector3f position = mesh.positions.get(index);
                Vector2f uv = mesh.uvs.get(index);
                Vector3f normal = mesh.normals.get(index);

                float r = red, g = green, b = blue;

                if (sideIndex == -1) {
                    if (type == MobPhysicsType.FRACTURED_BLOOD) {
                        r = 0.6f;
                        g = 0.0f;
                        b = 0.0f;
                    }

                    sideIndex = 0;
                }

                tmpNormal.set((mirror) ? -normal.x : normal.x, normal.y, normal.z);
                localNM.transform(tmpNormal);

                Vector4f minMaxUVs = minMax[remap[sideIndex]];

                boolean lmirror = (mirror && (sideIndex == 0 || sideIndex == 2)) || (!mirror && (sideIndex == 1 || sideIndex == 3));

                tmpPos.set((float) net.diebuddies.math.Math.remap(position.x + mesh.offset.x, lmirror ? 0.5 : -0.5, lmirror ? -0.5 : 0.5, min.x, max.x), (float) net.diebuddies.math.Math.remap(position.y + mesh.offset.y, -0.5, 0.5, min.y, max.y), (float) net.diebuddies.math.Math.remap(position.z + mesh.offset.z, -0.5, 0.5, min.z, max.z), 1.0f);

                localM.transform(tmpPos);

                clone.indices.add(count);
                offset.add(tmpPos.x(), tmpPos.y(), tmpPos.z());
                count++;
                Vector3f posR = new Vector3f(tmpPos.x(), tmpPos.y(), tmpPos.z());
                clone.positions.add(posR);

                if (sideIndex == 4 || sideIndex == 5) {
                    clone.uvs.add(new Vector2f(
                            net.diebuddies.math.Math.remap(uv.x, 0.0f, 1.0f, minMaxUVs.x, minMaxUVs.y),
                            net.diebuddies.math.Math.remap(uv.y, 0.0f, 1.0f, minMaxUVs.z, minMaxUVs.w)
                    ));
                } else if (sideIndex == 0 || sideIndex == 2) {
                    clone.uvs.add(new Vector2f(
                            net.diebuddies.math.Math.remap(uv.x, 0.0f, 1.0f, minMaxUVs.x, minMaxUVs.y),
                            net.diebuddies.math.Math.remap(uv.y, 1.0f, 0.0f, minMaxUVs.z, minMaxUVs.w)
                    ));
                } else {
                    clone.uvs.add(new Vector2f(
                            net.diebuddies.math.Math.remap(uv.x, 1.0f, 0.0f, minMaxUVs.x, minMaxUVs.y),
                            net.diebuddies.math.Math.remap(uv.y, 1.0f, 0.0f, minMaxUVs.z, minMaxUVs.w)
                    ));
                }

                clone.normals.add(new Vector3f(tmpNormal.x(), tmpNormal.y(), tmpNormal.z()));
                clone.addColor(r, g, b);
            }

            if (StarterClient.iris || StarterClient.optifabric) {
                clone.calculatePBRData(false);
            }

            offset.div(clone.positions.size());

            for (Vector3f position : clone.positions) {
                position.sub(offset);
            }

            float scale = 1.0f / 16.0f;

            clone.offset = offset;

            Vector4f bonePivotWorld = new Vector4f(
                    owningBone.getPivotX() * scale,
                    owningBone.getPivotY() * scale,
                    owningBone.getPivotZ() * scale,
                    1.0f
            );
            localM.transform(bonePivotWorld);

            particle.pivot.set(bonePivotWorld.x(), bonePivotWorld.y(), bonePivotWorld.z());
            particle.setRotation(new Quaternionf(owningBone.getRotX(), owningBone.getRotY(), owningBone.getRotZ(), 1f));
            particle.setOldRotation(new Quaternionf(owningBone.getRotX(), owningBone.getRotY(), owningBone.getRotZ(), 1f));

            mod.blockifiedEntity.add(particle);
        }
    }

    /*public void createParticlesFromEmfCuboids(
            PoseStack.Pose stack,
            PoseStack local,
            List<ModelPart.Cube> cuboids,
            ModelPart modelPart,
            Entity entity,
            RenderLayer feature,
            float red,
            float green,
            float blue
    ) {
        Matrix4f m = stack.pose();
        Matrix4f localM = local.last().pose();
        Matrix3f localNM = local.last().normal();
        Matrix4d transformation = new Matrix4d();
        Matrix4d transformationLocal = new Matrix4d();
        transformation.set(m);
        transformationLocal.set(localM);
        transformation.mul(transformationLocal.invert(new Matrix4d()));
        PhysicsMod mod = PhysicsMod.getInstance(entity.getCommandSenderWorld());
        int textureID = TextureHelper.getLoadedTextures();
        float partialTicks = Minecraft.getInstance().getFrameTime();
        double px = Mth.lerp(partialTicks, entity.xo, entity.getX());
        double py = Mth.lerp(partialTicks, entity.yo, entity.getY());
        double pz = Mth.lerp(partialTicks, entity.zo, entity.getZ());
        transformation.setTranslation(px + transformation.m30(), py + transformation.m31(), pz + transformation.m32());
        Vector4f[] minMax = new Vector4f[6];
        Vector3f tmpNormal = new Vector3f();
        Vector4f tmpPos = new Vector4f();

        for (int i = 0; i < minMax.length; i++) {
            minMax[i] = new Vector4f();
        }

        MobPhysicsType type = ConfigMobs.getMobSetting(entity).getType();
        Iterator var28 = cuboids.iterator();

        while (true) {
            ModelPart.Cube box;
            float minX;
            float minY;
            float minZ;
            float maxX;
            float maxY;
            float maxZ;
            boolean mirrorX;
            boolean mirrorY;
            boolean mirrorZ;
            int[] remap;
            float volume;
            boolean isBlocky;
            boolean noVolume;
            while (true) {
                if (!var28.hasNext()) {
                    return;
                }

                box = (ModelPart.Cube)var28.next();
                if (box.polygons.length >= 6) {
                    minX = box.polygons[0].vertices[2].pos.x();
                    minY = box.polygons[0].vertices[2].pos.y();
                    minZ = box.polygons[0].vertices[2].pos.z();
                    maxX = box.polygons[1].vertices[3].pos.x();
                    maxY = box.polygons[1].vertices[3].pos.y();
                    maxZ = box.polygons[1].vertices[3].pos.z();
                    mirrorX = false;
                    mirrorY = false;
                    mirrorZ = false;
                    if (minX > maxX) {
                        mirrorX = true;
                    }

                    if (minY > maxY) {
                        mirrorY = true;
                    }

                    if (minZ > maxZ) {
                        mirrorZ = true;
                    }

                    remap = new int[]{5, 4, 3, 2, 1, 0};
                    if (mirrorX) {
                        remap[0] = 3;
                        remap[2] = 5;
                    }

                    volume = Math.abs(maxX - minX) / 16.0F * (Math.abs(maxY - minY) / 16.0F) * (Math.abs(maxZ - minZ) / 16.0F);
                    isBlocky = type == MobPhysicsType.BLOCKY
                            || type == MobPhysicsType.RAGDOLL
                            || type == MobPhysicsType.RAGDOLL_BREAK
                            || type == MobPhysicsType.RAGDOLL_BREAK_BLOOD;
                    noVolume = false;
                    if (!(volume <= 1.0E-4)) {
                        break;
                    }

                    if (isBlocky) {
                        noVolume = true;
                        break;
                    }
                }
            }

            List<Mesh> meshes = PhysicsMod.brokenBlocksLittle.get((int)(net.diebuddies.math.Math.random() * PhysicsMod.brokenBlocksLittle.size()));
            if (volume <= 0.04 || isBlocky) {
                meshes = PhysicsMod.brokenBlock;
            }

            for (int i = 0; i < box.polygons.length; i++) {
                float minU = 1.0F;
                float maxU = 0.0F;
                float minV = 1.0F;
                float maxV = 0.0F;
                ModelPart.Vertex[] vertices = box.polygons[i].vertices;
                if (DeceasedCraftFixesClient.getCompat().getEmfCompat().isEMFModel()) {
                    minU = vertices[1].u;
                    maxU = vertices[3].u;
                    minV = vertices[1].v;
                    maxV = vertices[3].v;
                    minMax[i].set(minU, maxU, minV, maxV);
                } else {
                    for (ModelPart.Vertex vertex : vertices) {
                        if (vertex.u < minU) {
                            minU = vertex.u;
                        }

                        if (vertex.v < minV) {
                            minV = vertex.v;
                        }

                        if (vertex.u > maxU) {
                            maxU = vertex.u;
                        }

                        if (vertex.v > maxV) {
                            maxV = vertex.v;
                        }
                    }

                    minMax[i].set(minU, maxU, minV, maxV);
                }
            }

            PhysicsEntity parent = null;

            for (Mesh mesh : meshes) {
                PhysicsEntity particle = new PhysicsEntity(PhysicsEntity.Type.MOB, entity.getType());
                particle.feature = feature;
                particle.noVolume = noVolume;
                particle.models.get(0).textureID = textureID;
                Mesh clone = new Mesh();
                particle.models.get(0).mesh = clone;
                particle.getTransformation().set(transformation);
                particle.getOldTransformation().set(particle.getTransformation());
                int count = 0;
                Vector3f offset = new Vector3f();

                for (int ix = 0; ix < mesh.indices.size(); ix++) {
                    int index = mesh.indices.getInt(ix);
                    byte sideIndex = mesh.sides.getByte(index);
                    Vector3f position = mesh.positions.get(index);
                    Vector2f uv = mesh.uvs.get(index);
                    Vector3f normal = mesh.normals.get(index);
                    float r = red;
                    float g = green;
                    float b = blue;
                    if (sideIndex == -1) {
                        if (type == MobPhysicsType.FRACTURED_BLOOD) {
                            r = 0.6F;
                            g = 0.0F;
                            b = 0.0F;
                        }

                        sideIndex = 0;
                    }

                    tmpNormal.set(mirrorX ? -normal.x : normal.x, mirrorY ? -normal.y : normal.y, mirrorZ ? -normal.z : normal.z);
                    localNM.transform(tmpNormal);
                    Vector4f minMaxUVs = minMax[remap[sideIndex]];
                    tmpPos.set(
                            (float)net.diebuddies.math.Math.remap(position.x + mesh.offset.x, -0.5, 0.5, minX, maxX) / 16.0F,
                            (float)net.diebuddies.math.Math.remap(position.y + mesh.offset.y, -0.5, 0.5, minY, maxY) / 16.0F,
                            (float)net.diebuddies.math.Math.remap(position.z + mesh.offset.z, -0.5, 0.5, minZ, maxZ) / 16.0F,
                            1.0F
                    );
                    localM.transform(tmpPos);
                    clone.indices.add(count);
                    offset.add(tmpPos.x(), tmpPos.y(), tmpPos.z());
                    count++;
                    Vector3f posR = new Vector3f(tmpPos.x(), tmpPos.y(), tmpPos.z());
                    clone.positions.add(posR);
                    float fromX = 1.0F;
                    float toX = 0.0F;
                    float fromY = 0.0F;
                    float toY = 1.0F;

                    clone.uvs
                            .add(
                                    new Vector2f(
                                            net.diebuddies.math.Math.remap(uv.x, fromX, toX, minMaxUVs.x, minMaxUVs.y),
                                            net.diebuddies.math.Math.remap(uv.y, fromY, toY, minMaxUVs.z, minMaxUVs.w)
                                    )
                            );
                    clone.normals.add(new Vector3f(tmpNormal.x(), tmpNormal.y(), tmpNormal.z()));
                    clone.addColor(r, g, b);
                }

                if (StarterClient.iris || StarterClient.optifabric) {
                    clone.calculatePBRData(false);
                }

                offset.div(clone.positions.size());

                for (Vector3f positionx : clone.positions) {
                    positionx.sub(offset);
                }

                clone.offset = offset;
                Vector3d ps = transformationLocal.getTranslation(new Vector3d());
                particle.pivot.set(ps);

                particle.setRotation(new Quaternionf(modelPart.xRot, modelPart.yRot, modelPart.zRot, 1f));
                particle.setOldRotation(new Quaternionf(modelPart.xRot, modelPart.yRot, modelPart.zRot, 1f));

                if (parent == null) {
                    parent = particle;
                    mod.blockifiedEntity.add(particle);
                } else {
                    parent.children.add(particle);
                }
            }
        }
    }*/
}
