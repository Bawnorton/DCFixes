package com.bawnorton.dcfixes.client.compat.deaceased;

import com.bawnorton.dcfixes.client.compat.geckolib.RenderTypeReplacer;
import com.bawnorton.dcfixes.client.compat.geckolib.RenderTypeReplacerRegistrar;
import net.diebuddies.physics.ragdoll.RagdollMapper;
import net.mcreator.deaceased.client.renderer.*;

public class DeaceasedCompat {
    public void registerRagdolls() {
        RagdollMapper.addHook(new DeaceasedGeckoLibRagdollHook());
    }

    public void registerRenderTypeReplacers(RenderTypeReplacerRegistrar registrar) {
        registrar.register(BoulderRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(BuggerRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(FlayedmanheadRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(FlayedmansectionsRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(FlayedmantailRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(GuardianRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(StunnerRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(ThediggerRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(ThefaceRenderer.class, RenderTypeReplacer::defaultReplacer);
    }
}

