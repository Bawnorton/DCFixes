package com.bawnorton.dcfixes.mixin_extensions;

import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.util.Annotations;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public final class DCFixesMixinPlugin implements IMixinConfigPlugin {
    private static final Logger LOGGER = LoggerFactory.getLogger(DCFixesMixinPlugin.class);

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        try {
            ClassNode mixinClassNode = MixinService.getService().getBytecodeProvider().getClassNode(mixinClassName);
            AnnotationNode ifModLoadedNode = Annotations.get(mixinClassNode.visibleAnnotations, Type.getDescriptor(IfModLoaded.class));
            if(ifModLoadedNode == null) return true;

            List<String> modIds = Annotations.getValue(ifModLoadedNode);
            if(modIds == null || modIds.isEmpty()) return true;

            boolean areModsPresent = true;
            for(String modId : modIds) {
                if(!isModLoaded(modId)) {
                    areModsPresent = false;
                    break;
                }
            }
            return areModsPresent;
        } catch (ClassNotFoundException | IOException e) {
            LOGGER.warn("Could not determine if mixin '%s' should apply, skipping");
            return false;
        }
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    private boolean isModLoaded(String id) {
        LoadingModList modList = LoadingModList.get();
        for(ModInfo modInfo : modList.getMods()) {
            if(modInfo.getModId().equals(id)) {
                return true;
            }
        }
        return false;
    }
}
