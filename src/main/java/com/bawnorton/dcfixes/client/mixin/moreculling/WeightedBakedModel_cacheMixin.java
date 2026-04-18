package com.bawnorton.dcfixes.client.mixin.moreculling;

import ca.fxco.moreculling.api.blockstate.MoreStateCulling;
import ca.fxco.moreculling.api.model.BakedOpacity;
import ca.fxco.moreculling.api.quad.QuadOpacity;
import ca.fxco.moreculling.utils.BitUtils;
import ca.fxco.moreculling.utils.CullingUtils;
import ca.fxco.moreculling.utils.DirectionUtils;
import java.util.List;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.WeightedBakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@MixinEnvironment("client")
@Mixin(value = WeightedBakedModel.class, priority = 1010)
abstract class WeightedBakedModel_cacheMixin implements BakedOpacity {
    @Shadow(remap = false)
    public abstract List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random, ModelData modelData, @Nullable RenderType renderType);

    public void resetTranslucencyCache(BlockState state) {
        byte emptyFaces = 0;
        boolean translucency = false;

        for(Direction face : DirectionUtils.DIRECTIONS) {
            List<BakedQuad> quads = this.getQuads(state, face, CullingUtils.RANDOM, ModelData.EMPTY, null);
            if (quads.isEmpty()) {
                emptyFaces = BitUtils.set(emptyFaces, face.ordinal());
            } else if (!translucency) {
                for(BakedQuad quad : quads) {
                    if (((QuadOpacity)quad).getTextureTranslucency()) {
                        translucency = true;
                        break;
                    }
                }
            }
        }

        ((MoreStateCulling)state).moreculling$setHasQuadsOnSide(emptyFaces);
        ((MoreStateCulling)state).moreculling$setHasTextureTranslucency(translucency);
    }
}
