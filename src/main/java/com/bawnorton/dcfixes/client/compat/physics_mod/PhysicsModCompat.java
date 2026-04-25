package com.bawnorton.dcfixes.client.compat.physics_mod;

import com.bawnorton.dcfixes.client.DeceasedCraftFixesClient;
import com.bawnorton.dcfixes.client.compat.ClientCompat;
import com.bawnorton.dcfixes.client.compat.apocalypsenow.ModelReplacingHumanoidRenderer;
import com.bawnorton.dcfixes.client.compat.cnpc.CustomNpcRagdollHook;
import com.bawnorton.dcfixes.client.compat.deaceased.DeaceasedGeckoLibRagdollHook;
import com.bawnorton.dcfixes.client.compat.naturalist.NaturalistRagdollHook;
import com.bawnorton.dcfixes.client.compat.undead_revamp2.UndeadRevampRagdollHook;
import com.bawnorton.dcfixes.client.compat.zombie_extreme.ZombieExtremeRagdollHook;
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
import net.diebuddies.physics.ragdoll.RagdollHook;
import net.diebuddies.physics.ragdoll.RagdollMapper;
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
    private final ThreadLocal<GeoBone> GEO_BONE_CAPTURE = new ThreadLocal<>();
    private final ThreadLocal<String> CURRENT_BONE_ID = new ThreadLocal<>();

    public void registerRagdollHooks() {
        ClientCompat compat = DeceasedCraftFixesClient.getCompat();
        compat.getUndeadRevampCompat().ifPresent(ignored -> addRagdollHook(new UndeadRevampRagdollHook()));
        compat.getZombieExtremeCompat().ifPresent(ignored -> addRagdollHook(new ZombieExtremeRagdollHook()));
        compat.getNaturalistCompat().ifPresent(ignored -> addRagdollHook(new NaturalistRagdollHook()));
        compat.getDeaceasedCompat().ifPresent(ignored -> addRagdollHook(new DeaceasedGeckoLibRagdollHook()));
        compat.getCustomNpcCompat().ifPresent(ignored -> addRagdollHook(new CustomNpcRagdollHook()));
    }

    public void addRagdollHook(RagdollHook ragdollHook) {
        RagdollMapper.addHook(ragdollHook);
    }

    public boolean isBlockifyingEntity() {
        return PhysicsMod.getCurrentInstance() != null && PhysicsMod.getCurrentInstance().blockify;
    }

    public void setCurrentBoneId(String boneId) {
        CURRENT_BONE_ID.set(boneId);
    }

    public String getCurrentBoneId() {
        return CURRENT_BONE_ID.get();
    }

    public void removeCurrentBoneId() {
        CURRENT_BONE_ID.remove();
    }

    public void setGeoBoneCapture(GeoBone geoBone) {
        GEO_BONE_CAPTURE.set(geoBone);
    }

    public GeoBone getGeoBoneCapture() {
        return GEO_BONE_CAPTURE.get();
    }

    public void removeGeoBoneCapture() {
        GEO_BONE_CAPTURE.remove();
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> void replaceRenderer(EntityRenderer<T> instance, T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, Operation<Void> original) {
        if (entity instanceof Mob mob && instance instanceof HumanoidMobRenderer<?, ?>) {
            HumanoidMobRenderer<Mob, HumanoidModel<Mob>> humanoidMobRenderer = (HumanoidMobRenderer<Mob, HumanoidModel<Mob>>) instance;
            Object model = humanoidMobRenderer.getModel();
            if (model instanceof HumanoidModelExtender humanoidModel) {
                ModelPart modelPart = humanoidModel.dcfixes$getRoot();
                if (modelPart instanceof ModelPartExtender extender) {
                    ModelLayerLocation layerLocation = extender.dcfixes$getLocation();
                    if (ModelLayers.PLAYER.equals(layerLocation)) {
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

    public void createParticlesFromCuboids(PoseStack local, GeoCube cube, Entity entity, RenderLayer<?, ?> feature, float red, float green, float blue) {
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
            ((PhysicsEntityExtender) particle).dcfixes$setBoneId(getCurrentBoneId());
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

            GeoBone owningBone = getGeoBoneCapture();
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
}
