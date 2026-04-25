package com.bawnorton.dcfixes.client.compat.zombie_extreme;

import com.bawnorton.dcfixes.client.compat.geckolib.RenderTypeReplacer;
import com.bawnorton.dcfixes.client.compat.geckolib.RenderTypeReplacerRegistrar;
import zombie_extreme.client.renderer.*;

public class ZombieExtremeCompat {
    public void registerRenderTypeReplacers(RenderTypeReplacerRegistrar registrar) {
        registrar.register(AssasinBlackOpsRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(BoomerRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(ChainsawRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(ClickerRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(CrawlerRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(DemolisherRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(DevestatedRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(DividedRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(ExplosiveInfectedRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(FacelessRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(FetusRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(GoonRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(InfectedHazmatRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(InfectedJuggernautRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(InfectedMilitaryRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(InfectedPoliceRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(InfectedRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(InflatedRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(MilitaryRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(NightHunterRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(ParasiteRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(RamRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(RatKingRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(RevivedRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(RunnerRenderer.class, RenderTypeReplacer::cutoutReplacer);
        registrar.register(ZeroPatientRenderer.class, RenderTypeReplacer::cutoutReplacer);
    }
}

