package com.bawnorton.dcfixes.client.mixin.moreculling;

import ca.fxco.moreculling.api.blockstate.MoreStateCulling;
import ca.fxco.moreculling.api.model.BakedOpacity;
import ca.fxco.moreculling.api.quad.QuadOpacity;
import ca.fxco.moreculling.utils.BitUtils;
import ca.fxco.moreculling.utils.CullingUtils;
import ca.fxco.moreculling.utils.DirectionUtils;
import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.MultiPartBakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.data.ModelData;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.function.Predicate;

@IfModLoaded("moreculling")
@MixinEnvironment("client")
@Mixin(value = MultiPartBakedModel.class, priority = 1010)
abstract class MultipartBakedModel_cacheMixin implements BakedOpacity {
    @Shadow
    @Final
    private List<Pair<Predicate<BlockState>, BakedModel>> selectors;

    @Shadow(remap = false)
    public abstract List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random, ModelData modelData, @Nullable RenderType renderType);

    public void resetTranslucencyCache(BlockState state) {
        byte emptyFaces = 0;
        boolean translucency = false;

        for (Direction face : DirectionUtils.DIRECTIONS) {
            List<BakedQuad> quads = this.getQuads(state, face, CullingUtils.RANDOM, ModelData.EMPTY, null);
            if (quads.isEmpty()) {
                emptyFaces = BitUtils.set(emptyFaces, face.ordinal());
            } else if (!translucency) {
                for (BakedQuad quad : quads) {
                    if (((QuadOpacity) quad).getTextureTranslucency()) {
                        translucency = true;
                        break;
                    }
                }
            }
        }

        ((MoreStateCulling) state).moreculling$setHasQuadsOnSide(emptyFaces);
        ((MoreStateCulling) state).moreculling$setHasTextureTranslucency(translucency);
    }

    public @Nullable VoxelShape getCullingShape(BlockState state) {
        VoxelShape cachedShape = null;

        for (Pair<Predicate<BlockState>, BakedModel> pair : this.selectors) {
            if (pair.getLeft().test(state)) {
                VoxelShape shape = ((BakedOpacity) pair.getRight()).getCullingShape(state);
                if (shape != null) {
                    if (cachedShape == null) {
                        cachedShape = shape;
                    } else {
                        cachedShape = Shapes.or(cachedShape, shape);
                    }
                }
            }
        }

        return cachedShape;
    }
}
