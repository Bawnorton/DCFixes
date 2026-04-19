package com.bawnorton.dcfixes.client.mixin.tacz;

import com.bawnorton.dcfixes.DeceasedCraftFixes;
import com.bawnorton.dcfixes.client.extend.CommonDataManagerExtender;
import com.bawnorton.dcfixes.collection.NullSkippingLambdaMap;
import com.bawnorton.dcfixes.collection.StandardLambdaMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.tacz.guns.GunMod;
import com.tacz.guns.client.resource.pojo.display.IDisplay;
import com.tacz.guns.resource.manager.CommonDataManager;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.GsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

@MixinEnvironment("client")
@Mixin(value = CommonDataManager.class, remap = false)
abstract class CommonDataManagerMixin<T> extends JsonDataManagerMixin<T> implements CommonDataManagerExtender {
    @Shadow
    protected Map<ResourceLocation, String> networkCache;

    @Unique
    private final Map<ResourceLocation, JsonElement> dcfixes$elementMap = new HashMap<>();

    @Override
    protected void useLambdaMap(CallbackInfo ci) {
        dataMap = new NullSkippingLambdaMap<>(location -> {
            Resource resource = dcfixes$resourceMap.get(location);
            if(resource == null) {
                DeceasedCraftFixes.LOGGER.warn("Failed to find json data resource: {}", location);
                return null;
            }
            try(Reader reader = resource.openAsReader()) {
                JsonElement jsonelement = GsonHelper.fromJson(getGson(), reader, JsonElement.class, true);
                dcfixes$elementMap.put(location, jsonelement);
                T data = parseJson(jsonelement);
                if(data instanceof IDisplay display) {
                    display.init();
                }
                return data;
            } catch (IllegalArgumentException | JsonParseException | IOException e) {
                GunMod.LOGGER.error(getMarker(), "Failed to load data file {}", location, e);
                return null;
            }
        });
    }

    @Inject(
            method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
            at = @At("TAIL")
    )
    private void useLambdaMapForNetworkCache(CallbackInfo ci) {
        networkCache = new NullSkippingLambdaMap<>(location -> {
            dataMap.get(location);
            return dcfixes$elementMap.get(location).toString();
        });
    }

    @Override
    public void dcfixes$loadAll() {
        dcfixes$resourceMap.keySet().forEach(dataMap::get);
    }
}
