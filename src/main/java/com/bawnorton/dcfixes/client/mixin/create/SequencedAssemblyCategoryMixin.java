package com.bawnorton.dcfixes.client.mixin.create;

import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.compat.jei.category.SequencedAssemblyCategory;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@IfModLoaded("create")
@MixinEnvironment("client")
@Mixin(value = SequencedAssemblyCategory.class, remap = false)
abstract class SequencedAssemblyCategoryMixin {
    @Shadow
    protected abstract MutableComponent chanceComponent(float chance);

    @SuppressWarnings("removal")
    @Definition(id = "width", local = @Local(type = int.class, name = "width"))
    @Expression("width = ?")
    @Inject(
            method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lcom/simibubi/create/content/processing/sequenced/SequencedAssemblyRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private void addChanceSlot(IRecipeLayoutBuilder builder, SequencedAssemblyRecipe recipe, IFocusGroup focuses, CallbackInfo ci, @Local(name = "noRandomOutput") boolean noRandomOutput, @Local(name = "xOffset") int xOffset) {
        if(noRandomOutput) return;

        Map<Item, Float> outputChances = new HashMap<>();
        for (ProcessingOutput output : recipe.resultPool.subList(1, recipe.resultPool.size())) {
            outputChances.put(output.getStack().getItem(), output.getChance());
        }
        float total = outputChances.values().stream().reduce(0F, Float::sum) + recipe.resultPool.get(0).getChance();
        builder.addSlot(RecipeIngredientRole.OUTPUT, 132 + xOffset + 19, 91)
                .addItemStacks(recipe.resultPool.subList(1, recipe.resultPool.size()).stream().map(ProcessingOutput::getStack).toList())
                .addTooltipCallback((iRecipeSlotView, iTooltipBuilder) -> {
                    ItemStack displayedResult = iRecipeSlotView.getDisplayedItemStack().orElse(null);
                    if(displayedResult == null) return;

                    float chance = outputChances.getOrDefault(displayedResult.getItem(), 0F) / total;
                    if(chance < 1.0F) {
                        iTooltipBuilder.add(1, chanceComponent(chance));
                    }
                });
    }

    @WrapOperation(
            method = "draw(Lcom/simibubi/create/content/processing/sequenced/SequencedAssemblyRecipe;Lmezz/jei/api/gui/ingredient/IRecipeSlotsView;Lnet/minecraft/client/gui/GuiGraphics;DD)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)I",
                    remap = true
            )
    )
    private int dontDrawQuestionMark(GuiGraphics instance, Font font, Component text, int x, int y, int color, Operation<Integer> original) {
        return 0;
    }

    @Definition(id = "recipe", local = @Local(type = SequencedAssemblyRecipe.class, argsOnly = true))
    @Definition(id = "getOutputChance", method = "Lcom/simibubi/create/content/processing/sequenced/SequencedAssemblyRecipe;getOutputChance()F")
    @Expression("recipe.getOutputChance()")
    @ModifyExpressionValue(
            method = "getTooltipStrings(Lcom/simibubi/create/content/processing/sequenced/SequencedAssemblyRecipe;Lmezz/jei/api/gui/ingredient/IRecipeSlotsView;DD)Ljava/util/List;",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private float fakeAlwaysSingleOutput(float original) {
        return 1.0F;
    }
}
