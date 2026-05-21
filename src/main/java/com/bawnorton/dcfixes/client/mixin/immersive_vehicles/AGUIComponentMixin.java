package com.bawnorton.dcfixes.client.mixin.immersive_vehicles;

import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import minecrafttransportsimulator.guis.components.AGUIBase;
import minecrafttransportsimulator.guis.components.AGUIComponent;
import org.spongepowered.asm.mixin.Mixin;

@IfModLoaded("mts")
@MixinEnvironment("client")
@Mixin(value = AGUIComponent.class, remap = false)
abstract class AGUIComponentMixin {
    @WrapMethod(
            method = "renderTooltip"
    )
    private void renderItNormallyPlease(AGUIBase gui, int mouseX, int mouseY, Operation<Void> original) {
//        TooltipRenderUtil.renderTooltipBackground();
        original.call(gui, mouseX, mouseY);
    }
}
