package com.bawnorton.dcfixes.client.compat.undead_revamp2;

import com.bawnorton.dcfixes.client.compat.geckolib.RenderTypeReplacer;
import com.bawnorton.dcfixes.client.compat.geckolib.RenderTypeReplacerRegistrar;
import net.mcreator.undeadrevamp.client.renderer.*;

public class UndeadRevampCompat {
    public void registerRenderTypeReplacers(RenderTypeReplacerRegistrar registrar) {
        registrar.register(BigsuckerRenderer.class, RenderTypeReplacer::cutoutNoCullReplacer);
        registrar.register(BomberRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(CloggerRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(CrackleballRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(DeadcloggerRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(INVISIBLEBIDYRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(InvisicloggerRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(InvisiimmortalRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(InvisilehceryRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(LecheryRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(Propball1Renderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(SuckerRenderer.class, RenderTypeReplacer::cutoutNoCullReplacer);
        registrar.register(ThebeartamerRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(ThebidyRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(ThebidyupsideRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(ThedungeonRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(ThegliterRenderer.class, RenderTypeReplacer::cutoutNoCullReplacer);
        registrar.register(TheheavyRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(ThehorrorsdecoysRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(ThehorrorsRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(ThehunterRenderer.class, RenderTypeReplacer::cutoutNoCullReplacer);
        registrar.register(TheimmortalRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(ThelurkerRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(TheMoonflowerRenderer.class, RenderTypeReplacer::cutoutNoCullReplacer);
        registrar.register(TheordureRenderer.class, RenderTypeReplacer::cutoutNoCullReplacer);
        registrar.register(TheposessiveRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(ThepregnantRenderer.class, RenderTypeReplacer::cutoutNoCullReplacer);
        registrar.register(TherabidusRenderer.class, RenderTypeReplacer::cutoutNoCullReplacer);
        registrar.register(TherodRenderer.class, RenderTypeReplacer::cutoutNoCullReplacer);
        registrar.register(TheskeeperRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(ThesmokerRenderer.class, RenderTypeReplacer::cutoutNoCullReplacer);
        registrar.register(ThesomnolenceRenderer.class, RenderTypeReplacer::cutoutNoCullReplacer);
        registrar.register(ThespectreRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(ThespitterRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(TheswarmerRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(ThewolfRenderer.class, RenderTypeReplacer::cutoutNoCullReplacer);
    }
}

