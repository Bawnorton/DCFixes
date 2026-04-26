package com.bawnorton.super_early;

import com.bawnorton.super_early.extensions.DisplayWindowExtension;
import com.bawnorton.super_early.extensions.ImmediateWindowHandlerExtension;
import cpw.mods.modlauncher.api.*;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.ImmediateWindowProvider;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;

public class WindowReplacingTransformationService implements ITransformationService {
    private final Logger LOGGER = LoggerFactory.getLogger(WindowReplacingTransformationService.class);

    public WindowReplacingTransformationService() {
        ReplacingImmediateWindowProvider newProvider = new ReplacingImmediateWindowProvider();
        ImmediateWindowProvider currentProvider = ImmediateWindowHandlerExtension.getProvider();
        DisplayWindowExtension.tryAs(currentProvider, displayWindowExtension -> {
            ScheduledFuture<?> initializationFuture = displayWindowExtension.getInitializationFuture();
            try {
                initializationFuture.get();
            } catch (ExecutionException e) {
                LOGGER.error("Failed to wait for initialization future to complete, window provider may already be initialized, skipping window replacement", e);
                return;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            DisplayWindowExtension.tryAs(newProvider, newDisplayWindowExtension -> {
                newDisplayWindowExtension.setColourScheme(displayWindowExtension.getColourScheme());
                newDisplayWindowExtension.setElementShader(displayWindowExtension.getElementShader());
                newDisplayWindowExtension.setContext(displayWindowExtension.getContext());
                newDisplayWindowExtension.setElements(displayWindowExtension.getElements());
                newDisplayWindowExtension.setFramecount(displayWindowExtension.getFramecount());
                newDisplayWindowExtension.setFramebuffer(displayWindowExtension.getFramebuffer());
                newDisplayWindowExtension.setWindowTick(displayWindowExtension.getWindowTick());
                newDisplayWindowExtension.setInitializationFuture(displayWindowExtension.getInitializationFuture());
                newDisplayWindowExtension.setPerformanceInfo(displayWindowExtension.getPerformanceInfo());
                newDisplayWindowExtension.setPerformanceTick(displayWindowExtension.getPerformanceTick());
                newDisplayWindowExtension.setWindow(displayWindowExtension.getWindow());
                newDisplayWindowExtension.setRenderScheduler(displayWindowExtension.getRenderScheduler());
                newDisplayWindowExtension.setFbWidth(displayWindowExtension.getFbWidth());
                newDisplayWindowExtension.setFbHeight(displayWindowExtension.getFbHeight());
                newDisplayWindowExtension.setFbScale(displayWindowExtension.getFbScale());
                newDisplayWindowExtension.setWinWidth(displayWindowExtension.getWinWidth());
                newDisplayWindowExtension.setWinHeight(displayWindowExtension.getWinHeight());
                newDisplayWindowExtension.setWinX(displayWindowExtension.getWinX());
                newDisplayWindowExtension.setWinY(displayWindowExtension.getWinY());
                newDisplayWindowExtension.setMaximized(displayWindowExtension.isMaximized());
                newDisplayWindowExtension.setGlVersion(displayWindowExtension.getGlVersion());
                newDisplayWindowExtension.setFont(displayWindowExtension.getFont());
                newDisplayWindowExtension.setRepaintTick(displayWindowExtension.getRepaintTick());
            });
            ImmediateWindowHandlerExtension.setProvider(newProvider);
            FMLLoader.progressWindowTick = newProvider.startAgain();
        });
    }

    @NotNull
    @Override
    public String name() {
        return "DeceasedCraftWindowReplacer";
    }

    @Override
    public void initialize(IEnvironment environment) {

    }

    @Override
    public void onLoad(IEnvironment env, Set<String> otherServices) throws IncompatibleEnvironmentException {

    }

    @NotNull
    @Override
    public List<ITransformer> transformers() {
        return List.of();
    }
}

