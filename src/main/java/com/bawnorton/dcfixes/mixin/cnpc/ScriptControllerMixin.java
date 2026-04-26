package com.bawnorton.dcfixes.mixin.cnpc;

import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import noppes.npcs.controllers.ScriptController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.script.ScriptEngine;
import java.util.Map;

@IfModLoaded("customnpcs")
@Mixin(value = ScriptController.class, remap = false)
abstract class ScriptControllerMixin {
    @Unique
    private final Map<String, ScriptEngine> dcfixes$engineCache = new java.util.HashMap<>();

    @WrapMethod(
            method = "getEngineByName"
    )
    private ScriptEngine cacheEngines(String language, Operation<ScriptEngine> original) {
        return dcfixes$engineCache.computeIfAbsent(language, original::call);
    }
}
