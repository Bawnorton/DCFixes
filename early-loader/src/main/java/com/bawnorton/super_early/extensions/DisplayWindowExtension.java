package com.bawnorton.super_early.extensions;

import com.bawnorton.super_early.reflection.FieldReference;
import com.bawnorton.super_early.reflection.MethodReference;
import net.minecraftforge.fml.earlydisplay.*;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DisplayWindowExtension {
    private static final FieldReference<AtomicBoolean> animationTimerTriggerReference = FieldReference.ofInstance(DisplayWindow.class, "animationTimerTrigger", AtomicBoolean.class);
    private static final FieldReference<ColourScheme> colourSchemeReference = FieldReference.ofInstance(DisplayWindow.class, "colourScheme", ColourScheme.class);
    private static final FieldReference<ElementShader> elementShaderReference = FieldReference.ofInstance(DisplayWindow.class, "elementShader", ElementShader.class);
    private static final FieldReference<RenderElement.DisplayContext> contextReference = FieldReference.ofInstance(DisplayWindow.class, "context", RenderElement.DisplayContext.class);
    private static final FieldReference<List> elementsReference = FieldReference.ofInstance(DisplayWindow.class, "elements", List.class);
    private static final FieldReference<Integer> framecountReference = FieldReference.ofInstance(DisplayWindow.class, "framecount", int.class);
    private static final FieldReference<EarlyFramebuffer> framebufferReference = FieldReference.ofInstance(DisplayWindow.class, "framebuffer", EarlyFramebuffer.class);
    private static final FieldReference<ScheduledFuture> windowTickReference = FieldReference.ofInstance(DisplayWindow.class, "windowTick", ScheduledFuture.class);
    private static final FieldReference<ScheduledFuture> initializationFutureReference = FieldReference.ofInstance(DisplayWindow.class, "initializationFuture", ScheduledFuture.class);
    private static final FieldReference<PerformanceInfo> performanceInfoReference = FieldReference.ofInstance(DisplayWindow.class, "performanceInfo", PerformanceInfo.class);
    private static final FieldReference<ScheduledFuture> performanceTickReference = FieldReference.ofInstance(DisplayWindow.class, "performanceTick", ScheduledFuture.class);
    private static final FieldReference<Long> windowReference = FieldReference.ofInstance(DisplayWindow.class, "window", long.class);
    private static final FieldReference<ScheduledExecutorService> renderSchedulerReference = FieldReference.ofInstance(DisplayWindow.class, "renderScheduler", ScheduledExecutorService.class);
    private static final FieldReference<Integer> fbWidthReference = FieldReference.ofInstance(DisplayWindow.class, "fbWidth", int.class);
    private static final FieldReference<Integer> fbHeightReference = FieldReference.ofInstance(DisplayWindow.class, "fbHeight", int.class);
    private static final FieldReference<Integer> fbScaleReference = FieldReference.ofInstance(DisplayWindow.class, "fbScale", int.class);
    private static final FieldReference<Integer> winWidthReference = FieldReference.ofInstance(DisplayWindow.class, "winWidth", int.class);
    private static final FieldReference<Integer> winHeightReference = FieldReference.ofInstance(DisplayWindow.class, "winHeight", int.class);
    private static final FieldReference<Integer> winXReference = FieldReference.ofInstance(DisplayWindow.class, "winX", int.class);
    private static final FieldReference<Integer> winYReference = FieldReference.ofInstance(DisplayWindow.class, "winY", int.class);
    private static final FieldReference<Semaphore> renderLockReference = FieldReference.ofInstance(DisplayWindow.class, "renderLock", Semaphore.class);
    private static final FieldReference<Boolean> maximizedReference = FieldReference.ofInstance(DisplayWindow.class, "maximized", boolean.class);
    private static final FieldReference<String> glVersionReference = FieldReference.ofInstance(DisplayWindow.class, "glVersion", String.class);
    private static final FieldReference<SimpleFont> fontReference = FieldReference.ofInstance(DisplayWindow.class, "font", SimpleFont.class);
    private static final FieldReference<Runnable> repaintTickReference = FieldReference.ofInstance(DisplayWindow.class, "repaintTick", Runnable.class);
    private static final FieldReference<Method> loadingOverlayReference = FieldReference.ofInstance(DisplayWindow.class, "loadingOverlay", Method.class);

    private static final MethodReference<Void> renderThreadFuncReference = MethodReference.ofInstance(DisplayWindow.class, "renderThreadFunc", void.class);
    private static final MethodReference<Void> winResizeReference = MethodReference.ofInstance(DisplayWindow.class, "winResize", void.class, long.class, int.class, int.class);
    private static final MethodReference<Void> fbResizeReference = MethodReference.ofInstance(DisplayWindow.class, "fbResize", void.class, long.class, int.class, int.class);
    private static final MethodReference<Void> winMoveReference = MethodReference.ofInstance(DisplayWindow.class, "winMove", void.class, long.class, int.class, int.class);

    private final DisplayWindow instance;

    private DisplayWindowExtension(DisplayWindow instance) {
        this.instance = instance;
    }

    public static void tryAs(Object instance, Consumer<DisplayWindowExtension> consumer) {
        if(DisplayWindow.class.isAssignableFrom(instance.getClass())) {
            consumer.accept(new DisplayWindowExtension((DisplayWindow) instance));
        }
    }

    public AtomicBoolean getAnimationTimerTrigger() {
        return animationTimerTriggerReference.get(instance);
    }

    public ColourScheme getColourScheme() {
        return colourSchemeReference.get(instance);
    }

    public ElementShader getElementShader() {
        return elementShaderReference.get(instance);
    }

    public RenderElement.DisplayContext getContext() {
        return contextReference.get(instance);
    }

    public List<RenderElement> getElements() {
        return (List<RenderElement>) elementsReference.get(instance);
    }

    public int getFramecount() {
        return framecountReference.get(instance);
    }

    public EarlyFramebuffer getFramebuffer() {
        return framebufferReference.get(instance);
    }

    public ScheduledFuture<?> getWindowTick() {
        return windowTickReference.get(instance);
    }

    public ScheduledFuture<?> getInitializationFuture() {
        return initializationFutureReference.get(instance);
    }

    public PerformanceInfo getPerformanceInfo() {
        return performanceInfoReference.get(instance);
    }

    public ScheduledFuture<?> getPerformanceTick() {
        return performanceTickReference.get(instance);
    }

    public long getWindow() {
        return windowReference.get(instance);
    }

    public ScheduledExecutorService getRenderScheduler() {
        return renderSchedulerReference.get(instance);
    }

    public int getFbWidth() {
        return fbWidthReference.get(instance);
    }

    public int getFbHeight() {
        return fbHeightReference.get(instance);
    }

    public int getFbScale() {
        return fbScaleReference.get(instance);
    }

    public int getWinWidth() {
        return winWidthReference.get(instance);
    }

    public int getWinHeight() {
        return winHeightReference.get(instance);
    }

    public int getWinX() {
        return winXReference.get(instance);
    }

    public int getWinY() {
        return winYReference.get(instance);
    }

    public Semaphore getRenderLock() {
        return renderLockReference.get(instance);
    }

    public boolean isMaximized() {
        return maximizedReference.get(instance);
    }

    public String getGlVersion() {
        return glVersionReference.get(instance);
    }

    public SimpleFont getFont() {
        return fontReference.get(instance);
    }

    public Runnable getRepaintTick() {
        return repaintTickReference.get(instance);
    }

    public void setColourScheme(ColourScheme colourScheme) {
        colourSchemeReference.set(instance, colourScheme);
    }

    public void setElementShader(ElementShader elementShader) {
        elementShaderReference.set(instance, elementShader);
    }

    public void setContext(RenderElement.DisplayContext context) {
        contextReference.set(instance, context);
    }

    public void setElements(List<RenderElement> elements) {
        elementsReference.set(instance, elements);
    }

    public void setFramecount(int framecount) {
        framecountReference.set(instance, framecount);
    }

    public void setFramebuffer(EarlyFramebuffer framebuffer) {
        framebufferReference.set(instance, framebuffer);
    }

    public void setWindowTick(ScheduledFuture<?> windowTick) {
        windowTickReference.set(instance, windowTick);
    }

    public void setInitializationFuture(ScheduledFuture<?> initializationFuture) {
        initializationFutureReference.set(instance, initializationFuture);
    }

    public void setPerformanceInfo(PerformanceInfo performanceInfo) {
        performanceInfoReference.set(instance, performanceInfo);
    }

    public void setPerformanceTick(ScheduledFuture<?> performanceTick) {
        performanceTickReference.set(instance, performanceTick);
    }

    public void setWindow(long window) {
        windowReference.set(instance, window);
    }

    public void setRenderScheduler(ScheduledExecutorService renderScheduler) {
        renderSchedulerReference.set(instance, renderScheduler);
    }

    public void setFbWidth(int fbWidth) {
        fbWidthReference.set(instance, fbWidth);
    }

    public void setFbHeight(int fbHeight) {
        fbHeightReference.set(instance, fbHeight);
    }

    public void setFbScale(int fbScale) {
        fbScaleReference.set(instance, fbScale);
    }

    public void setWinWidth(int winWidth) {
        winWidthReference.set(instance, winWidth);
    }

    public void setWinHeight(int winHeight) {
        winHeightReference.set(instance, winHeight);
    }

    public void setWinX(int winX) {
        winXReference.set(instance, winX);
    }

    public void setWinY(int winY) {
        winYReference.set(instance, winY);
    }

    public void setMaximized(boolean maximized) {
        maximizedReference.set(instance, maximized);
    }

    public void setGlVersion(String glVersion) {
        glVersionReference.set(instance, glVersion);
    }

    public void setFont(SimpleFont font) {
        fontReference.set(instance, font);
    }

    public void setRepaintTick(Runnable repaintTick) {
        repaintTickReference.set(instance, repaintTick);
    }

    public void setLoadingOverlay(Method method) {
        loadingOverlayReference.set(instance, method);
    }

    public void invokeRenderThreadFunc() {
        renderThreadFuncReference.invoke(instance);
    }

    public void invokeWinResize(long window, int width, int height) {
        winResizeReference.invoke(instance, window, width, height);
    }

    public void invokeFbResize(long window, int width, int height) {
        fbResizeReference.invoke(instance, window, width, height);
    }

    public void invokeWinMove(long window, int x, int y) {
        winMoveReference.invoke(instance, window, x, y);
    }
}

