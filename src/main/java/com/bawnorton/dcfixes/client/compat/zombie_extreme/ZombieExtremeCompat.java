package com.bawnorton.dcfixes.client.compat.zombie_extreme;

import com.bawnorton.dcfixes.client.compat.geckolib.RenderTypeReplacer;
import com.bawnorton.dcfixes.client.compat.geckolib.RenderTypeReplacerRegistrar;
import net.diebuddies.physics.ragdoll.RagdollMapper;
import zombie_extreme.client.renderer.*;

public class ZombieExtremeCompat {
    public void registerRagdolls() {
        RagdollMapper.addHook(new ZombieExtremeGeckoLibRagdollHook());
    }

    public void registerRenderTypeReplacers(RenderTypeReplacerRegistrar registrar) {
        registrar.register(AssasinBlackOpsRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(BoomerRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(ChainsawRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(ClickerRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(CrawlerRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(DemolisherRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(DevestatedRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(DividedRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(ExplosiveInfectedRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(FacelessRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(FetusRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(GoonRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(InfectedHazmatRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(InfectedJuggernautRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(InfectedMilitaryRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(InfectedPoliceRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(InfectedRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(InflatedRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(MilitaryRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(NightHunterRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(ParasiteRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(RamRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(RatKingRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(RevivedRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(RunnerRenderer.class, RenderTypeReplacer::defaultReplacer);
        registrar.register(ZeroPatientRenderer.class, RenderTypeReplacer::defaultReplacer);
    }
}

