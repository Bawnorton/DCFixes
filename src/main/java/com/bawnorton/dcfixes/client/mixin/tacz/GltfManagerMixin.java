package com.bawnorton.dcfixes.client.mixin.tacz;

import com.bawnorton.dcfixes.collection.StandardLambdaMap;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.tacz.guns.GunMod;
import com.tacz.guns.api.client.animation.gltf.AnimationStructure;
import com.tacz.guns.client.resource.ClientAssetsManager;
import com.tacz.guns.client.resource.manager.GltfManager;
import com.tacz.guns.client.resource.pojo.animation.gltf.RawAnimationStructure;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import org.apache.logging.log4j.Marker;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@MixinEnvironment("client")
@Mixin(value = GltfManager.class, remap = false)
abstract class GltfManagerMixin {
    @Shadow
    @Final
    private static Marker MARKER;
    @Unique
    private final Map<ResourceLocation, Resource> dcfixes$resourceMap = new HashMap<>();
    @Shadow
    @Final
    @Mutable
    private Map<ResourceLocation, AnimationStructure> dataMap;
    @Shadow
    @Final
    private FileToIdConverter filetoidconverter;

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void useLambdaMap(CallbackInfo ci) {
        dataMap = new StandardLambdaMap<>(location -> {
            Resource resource = dcfixes$resourceMap.get(location);
            if (resource == null) return null;

            try (Reader reader = resource.openAsReader()) {
                RawAnimationStructure rawStructure = ClientAssetsManager.GSON.fromJson(reader, RawAnimationStructure.class);
                return new AnimationStructure(rawStructure);
            } catch (IOException e) {
                GunMod.LOGGER.warn(MARKER, "Failed to read gltf animation file: {}", location);
                return null;
            }
        });
    }

    @ModifyExpressionValue(
            method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Ljava/util/Map;",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Set;iterator()Ljava/util/Iterator;"
            )
    )
    private Iterator<Map.Entry<ResourceLocation, Resource>> cacheResources(Iterator<Map.Entry<ResourceLocation, Resource>> original) {
        original.forEachRemaining(entry -> {
            ResourceLocation location = filetoidconverter.fileToId(entry.getKey());
            dcfixes$resourceMap.put(location, entry.getValue());
        });
        return Collections.emptyIterator();
    }
}
