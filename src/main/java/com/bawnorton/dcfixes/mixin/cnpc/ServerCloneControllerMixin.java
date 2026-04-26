package com.bawnorton.dcfixes.mixin.cnpc;

import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import noppes.npcs.controllers.ServerCloneController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;

@IfModLoaded("customnpcs")
@Mixin(value = ServerCloneController.class, remap = false)
abstract class ServerCloneControllerMixin {
    @Unique
    private final Map<Key, CompoundTag> dcfixes$cloneDataCache = new HashMap<>();

    @WrapMethod(
            method = "getCloneData"
    )
    private CompoundTag cacheCloneData(CommandSourceStack player, String name, int tab, Operation<CompoundTag> original) {
        return dcfixes$cloneDataCache.computeIfAbsent(new Key(name, tab), k -> original.call(player, name, tab));
    }

    @WrapMethod(
            method = "saveClone"
    )
    private void setCacheOnSave(int tab, String name, CompoundTag compound, Operation<Void> original) {
        original.call(tab, name, compound);
        dcfixes$cloneDataCache.put(new Key(name, tab), compound);
    }

    @WrapMethod(
            method = "removeClone"
    )
    private boolean clearCachedValue(String name, int tab, Operation<Boolean> original) {
        boolean didRemoveFile = original.call(name, tab);
        if(didRemoveFile) {
            dcfixes$cloneDataCache.remove(new Key(name, tab));
        }
        return didRemoveFile;
    }

    private record Key(String name, int tab) {}
}
