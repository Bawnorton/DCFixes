package com.bawnorton.dcfixes.client.compat.undead_revamp2;

import com.bawnorton.dcfixes.client.compat.geckolib.RenderTypeReplacer;
import com.bawnorton.dcfixes.client.compat.geckolib.RenderTypeReplacerRegistrar;
import net.diebuddies.physics.ragdoll.RagdollMapper;
import net.mcreator.undeadrevamp.client.renderer.*;

public class UndeadRevampCompat {
    public void registerRagdolls() {
        RagdollMapper.addHook(new UndeadRevampGeckoLibRagdollHook());
    }

    public void registerRenderTypeReplacers(RenderTypeReplacerRegistrar registrar) {
        registrar.register(BigsuckerRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(BomberRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(CloggerRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(CrackleballRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(DeadcloggerRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(INVISIBLEBIDYRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(InvisicloggerRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(InvisiimmortalRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(InvisilehceryRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(LecheryRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(Propball1Renderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(SuckerRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(ThebeartamerRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(ThebidyRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(ThebidyupsideRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(ThedungeonRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(ThegliterRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(TheheavyRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(ThehorrorsdecoysRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(ThehorrorsRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(ThehunterRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(TheimmortalRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(ThelurkerRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(TheMoonflowerRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(TheordureRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(TheposessiveRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(ThepregnantRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(TherabidusRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(TherodRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(TheskeeperRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(ThesmokerRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(ThesomnolenceRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(ThespectreRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(ThespitterRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(TheswarmerRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(ThewolfRenderer.class, RenderTypeReplacer::defaultReplacer);
    }
}

