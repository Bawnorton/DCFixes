package com.bawnorton.dcfixes.client.compat.apocalypsenow;

import com.bawnorton.dcfixes.client.mixin.accessor.EntityRendererAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

import java.util.function.Function;

public class ModelReplacingHumanoidRenderer<T extends Mob, M extends HumanoidModel<T>> extends HumanoidMobRenderer<T, M> {
    private final ResourceLocation texture;

    private ModelReplacingHumanoidRenderer(Context context, M newModel, float shadowRadius, ResourceLocation newTexture) {
        super(context, newModel, shadowRadius);
        this.texture = newTexture;
    }

    public static <T extends Mob, M extends HumanoidModel<T>> ModelReplacingHumanoidRenderer<T, M> create(HumanoidMobRenderer<T, M> parent, Function<Context, M> modelCreator, ResourceLocation newTexture) {
        Minecraft minecraft = Minecraft.getInstance();
        EntityRendererAccessor accessor = (EntityRendererAccessor) parent;
        Context context = new Context(
                accessor.dcfixes$entityRenderDispatcher(),
                minecraft.getItemRenderer(),
                minecraft.getBlockRenderer(),
                minecraft.gameRenderer.itemInHandRenderer,
                minecraft.getResourceManager(),
                minecraft.getEntityModels(),
                minecraft.font
        );
        return new ModelReplacingHumanoidRenderer<>(
                context,
                modelCreator.apply(context),
                accessor.dcfixes$shadowRadius(),
                newTexture
        );
    }

    @Override
    public ResourceLocation getTextureLocation(T t) {
        return texture;
    }
}
