package com.bawnorton.dcfixes.client.mixin.lrtactical;

import com.bawnorton.dcfixes.client.extend.CommonDataManagerExtender;
import com.bawnorton.dcfixes.client.mixin.tacz.JsonDataManagerMixin;
import com.bawnorton.dcfixes.collection.NullSkippingLambdaMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.tacz.guns.GunMod;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import me.xjqsh.lrtactical.item.index.MeleeWeaponIndex;
import me.xjqsh.lrtactical.resource.manager.MeleeIndexManager;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

@MixinEnvironment("client")
@Mixin(value = MeleeIndexManager.class, remap = false)
abstract class MeleeIndexManagerMixin extends JsonDataManagerMixin<MeleeWeaponIndex<?>> implements CommonDataManagerExtender {
    @Unique
    private final Map<ResourceLocation, String> dcfixes$elementMap = new HashMap<>();
    @Shadow
    private Map<ResourceLocation, String> networkCache;

    @Shadow
    public static MeleeWeaponIndex<?> parse(JsonObject pJson, ResourceLocation id) throws JsonParseException {
        throw new AssertionError();
    }

    @Override
    public MeleeWeaponIndex<?> dcfixes$parseReader(Reader reader, ResourceLocation location) {
        JsonElement element = getGson().fromJson(reader, JsonElement.class);
        if (!element.isJsonObject()) {
            GunMod.LOGGER.error(this.getMarker(), "Failed to load index file {}: Expected object, got {} ", location, element);
        } else {
            JsonObject pJson = element.getAsJsonObject();

            try {
                dcfixes$elementMap.put(location, pJson.toString());
                return parse(pJson, location);
            } catch (IllegalArgumentException | JsonParseException e) {
                GunMod.LOGGER.error(this.getMarker(), "Failed to load index file {}", location, e);
            }
        }
        return null;
    }

    @Inject(
            method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
            at = @At("TAIL")
    )
    private void useLambdaMapForNetworkCache(CallbackInfo ci) {
        networkCache = new NullSkippingLambdaMap<>(location -> {
            dataMap.get(location);
            return dcfixes$elementMap.get(location);
        });
    }

    @Override
    public void dcfixes$loadAll() {
        dcfixes$resourceMap.keySet().forEach(dataMap::get);
    }
}
