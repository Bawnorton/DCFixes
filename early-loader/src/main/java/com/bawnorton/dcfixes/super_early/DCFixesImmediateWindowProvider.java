package com.bawnorton.dcfixes.super_early;

import com.bawnorton.dcfixes.super_early.extensions.*;
import net.minecraftforge.fml.earlydisplay.*;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.ImmediateWindowProvider;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11C;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("resource")
public class DCFixesImmediateWindowProvider extends DisplayWindow implements ImmediateWindowProvider {
    private DisplayWindowExtension extension;

    public DCFixesImmediateWindowProvider() {
        DisplayWindowExtension.tryAs(this, extension -> this.extension = extension);
    }

    public Runnable startAgain() {
        extension.setColourScheme(ColourScheme.BLACK);
        extension.getRenderScheduler().schedule(() -> {
            try {
                extension.getRenderLock().acquire();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            reInitWindow();


            ScheduledFuture<?> windowTick = extension.getWindowTick();
            if(windowTick == null) {
                return;
            } else {
                while(!windowTick.isDone()) {
                    windowTick.cancel(false);
                }
            }

            reInitRender();

            extension.getRenderLock().release();
        }, 1, TimeUnit.MILLISECONDS);
        return this::periodicTick;
    }

    private void reInitWindow() {
        GLFW.glfwSetFramebufferSizeCallback(extension.getWindow(), extension::invokeFbResize);
        GLFW.glfwSetWindowPosCallback(extension.getWindow(), extension::invokeWinMove);
        GLFW.glfwSetWindowSizeCallback(extension.getWindow(), extension::invokeWinResize);
    }

    private void reInitRender() {
        extension.setWindowTick(extension.getRenderScheduler().scheduleAtFixedRate(extension::invokeRenderThreadFunc, 50, 50, TimeUnit.MILLISECONDS));
        extension.getRenderScheduler().scheduleAtFixedRate(() -> extension.getAnimationTimerTrigger().set(true), 1, 50, TimeUnit.MILLISECONDS);

        GLFW.glfwMakeContextCurrent(extension.getWindow());
        ColourScheme colourScheme = extension.getColourScheme();
        GL11C.glClearColor(
                colourScheme.background().redf(),
                colourScheme.background().greenf(),
                colourScheme.background().bluef(),
                1f
        );
        createNewContext();
        addElements(extension.getElements());
        GLFW.glfwMakeContextCurrent(0);
    }

    private void addElements(List<RenderElement> elements) {
        SimpleFont font = extension.getFont();
        RenderElement logMessageOverlay = elements.get(1);
        RenderElement forgeVersionOverlay = elements.get(2);
        elements.clear();

        elements.add(RenderElementExtension.create(RenderElementExtension.reimplInitializeTexture(
                "main_menu.png",
                1200000,
                5,
                TextureRendererExtension.create((bb, context, size, frame) -> SimpleFontExtension.tryAs(font, fontExtension -> {
                    var x0 = 0;
                    var x1 = context.scaledWidth();
                    var y0 = 0;
                    var y1 = context.scaledHeight();
                    QuadHelper.loadQuad(bb, x0, x1, y0, y1, 0f, 1f, 0, 1f, RenderElementExtension.getGlobalAlpha() << 24 | 0xFFFFFF);
                }))
        )));
        elements.add(logMessageOverlay);
        elements.add(forgeVersionOverlay);
        elements.add(RenderElement.performanceBar(font));
        elements.add(RenderElement.progressBars(font));
    }

    private void createNewContext() {
        RenderElement.DisplayContext oldContext = extension.getContext();
        EarlyFramebuffer oldFrameBuffer = extension.getFramebuffer();
        int[] width = new int[1];
        int[] height = new int[1];

        GLFW.glfwGetFramebufferSize(extension.getWindow(), width, height);
        extension.setFbWidth(width[0]);
        extension.setFbHeight(height[0]);

        RenderElement.DisplayContext newContext = new RenderElement.DisplayContext(
                width[0],
                height[0],
                oldContext.scale(),
                oldContext.elementShader(),
                extension.getColourScheme(),
                oldContext.performance()
        );
        extension.setContext(newContext);
        extension.setFramebuffer(EarlyFrameBufferExtension.create(newContext));
        EarlyFrameBufferExtension.tryAs(oldFrameBuffer, EarlyFrameBufferExtension::invokeClose);
    }

    @Override
    public String name() {
        return "fmlearlywindow";
    }

    @Override
    public Runnable initialize(String[] arguments) {
        throw new UnsupportedOperationException("Initialize should not be called on DCFixesImmediateWindowProvider");
    }

    @Override
    public Runnable start(@Nullable String mcVersion, String forgeVersion) {
        throw new UnsupportedOperationException("Start should not be called on DCFixesImmediateWindowProvider");
    }

    @Override
    public void updateModuleReads(ModuleLayer layer) {
        Module forge = layer.findModule("forge").orElseThrow();
        getClass().getModule().addReads(forge);
        var clz = FMLLoader.getGameLayer().findModule("forge").map(l->Class.forName(l, "net.minecraftforge.client.loading.ForgeLoadingOverlay")).orElseThrow();
        extension.setLoadingOverlay(Arrays.stream(clz.getMethods()).filter(m-> Modifier.isStatic(m.getModifiers()) && m.getName().equals("newInstance")).findFirst().orElseThrow());

    }

    @Override
    public void addMojangTexture(int textureId) {
        extension.getElements().add(RenderElement.mojang(textureId, extension.getFramecount()));
    }
}

