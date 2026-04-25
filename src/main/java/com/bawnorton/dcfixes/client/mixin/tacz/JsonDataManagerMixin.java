package com.bawnorton.dcfixes.client.mixin.tacz;

import com.bawnorton.dcfixes.DeceasedCraftFixes;
import com.bawnorton.dcfixes.collection.NullSkippingLambdaMap;
import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.tacz.guns.GunMod;
import com.tacz.guns.client.resource.pojo.display.IDisplay;
import com.tacz.guns.resource.manager.JsonDataManager;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.logging.log4j.Marker;
import org.spongepowered.asm.mixin.*;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@IfModLoaded("tacz")
@MixinEnvironment("client")
@Mixin(value = JsonDataManager.class, remap = false)
public abstract class JsonDataManagerMixin<T> {
    @Unique
    protected final Map<ResourceLocation, Resource> dcfixes$resourceMap = new HashMap<>();
    @Shadow
    @Final
    private FileToIdConverter fileToIdConverter;
    @Shadow
    @Final
    private Marker marker;
    @Shadow
    @Final
    private Gson gson;
    @Shadow
    @Final
    @Mutable
    protected Map<ResourceLocation, T> dataMap = new NullSkippingLambdaMap<>(location -> {
        Resource resource = dcfixes$resourceMap.get(location);
        if (resource == null) {
            DeceasedCraftFixes.LOGGER.warn("Failed to find json data resource: {}", location);
            return null;
        }
        try (Reader reader = resource.openAsReader()) {
            return dcfixes$parseReader(reader, location);
        } catch (IllegalArgumentException | JsonParseException | IOException e) {
            GunMod.LOGGER.error(marker, "Failed to load data file {}", location, e);
            return null;
        }
    });

    @Shadow
    protected abstract T parseJson(JsonElement element);

    @Shadow
    public abstract Gson getGson();

    @Shadow
    public abstract Marker getMarker();

    @Unique
    public T dcfixes$parseReader(Reader reader, ResourceLocation location) {
        JsonElement jsonelement = GsonHelper.fromJson(gson, reader, JsonElement.class, true);
        T data = parseJson(jsonelement);
        if (data instanceof IDisplay display) {
            display.init();
        }
        return data;
    }

    @WrapMethod(
            method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Ljava/util/Map;"
    )
    private Map<ResourceLocation, JsonElement> cacheResources(ResourceManager pResourceManager, ProfilerFiller pProfiler, Operation<Map<ResourceLocation, JsonElement>> original) {
        for (Map.Entry<ResourceLocation, Resource> entry : fileToIdConverter.listMatchingResources(pResourceManager).entrySet()) {
            dcfixes$resourceMap.put(fileToIdConverter.fileToId(entry.getKey()), entry.getValue());
        }

        return Collections.emptyMap();
    }
}
