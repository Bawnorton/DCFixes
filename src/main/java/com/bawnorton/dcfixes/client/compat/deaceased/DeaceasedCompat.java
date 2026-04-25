package com.bawnorton.dcfixes.client.compat.deaceased;

import com.bawnorton.dcfixes.client.compat.geckolib.RenderTypeReplacer;
import com.bawnorton.dcfixes.client.compat.geckolib.RenderTypeReplacerRegistrar;
import net.mcreator.deaceased.client.renderer.*;

public class DeaceasedCompat {
    public void registerRenderTypeReplacers(RenderTypeReplacerRegistrar registrar) {
        registrar.register(BoulderRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(BuggerRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(FlayedmanheadRenderer.class, RenderTypeReplacer::cutoutNoCullReplacer);
        registrar.register(FlayedmansectionsRenderer.class, RenderTypeReplacer::cutoutNoCullReplacer);
        registrar.register(FlayedmantailRenderer.class, RenderTypeReplacer::cutoutNoCullReplacer);
        registrar.register(GuardianRenderer.class, RenderTypeReplacer::cutoutNoCullReplacer);
        registrar.register(StunnerRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(ThediggerRenderer.class, RenderTypeReplacer::cutoutNoCullReplacer);
        registrar.register(ThefaceRenderer.class, RenderTypeReplacer::cutoutReplacer);
    }
}

