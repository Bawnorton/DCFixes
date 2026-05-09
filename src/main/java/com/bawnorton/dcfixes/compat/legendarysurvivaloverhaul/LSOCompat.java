package com.bawnorton.dcfixes.compat.legendarysurvivaloverhaul;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.DamageDistributionEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.IBodyDamageCapability;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonBodyPartsDamageSource;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.BodyDamageDataManager;
import sfiomn.legendarysurvivaloverhaul.api.health.HealthUtil;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage.BodyDamageCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;
import sfiomn.legendarysurvivaloverhaul.util.PlayerModelUtil;
import sfiomn.legendarysurvivaloverhaul.util.internal.BodyDamageUtilInternal;

import java.util.*;

public class LSOCompat {
    private static final ThreadLocal<Boolean> LIMBS_ALREADY_HEALED = ThreadLocal.withInitial(() -> false);
    private static final ThreadLocal<Float> DAMAGE_TO_APPLY = ThreadLocal.withInitial(() -> 0.0f);

    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public float simulateLimbDamage(LivingEntity entity, DamageSource source, float damage) {
        if (!(entity instanceof Player player)) return damage;
        if (player.level().isClientSide) return damage;

        if (shouldApplyHealthOverhaul(player)) {
            damage = HealthUtil.hurtPlayer(player, damage);
        }

        if (!shouldApplyLocalizedBodyDamage(player)) return damage;

        JsonBodyPartsDamageSource damageSourceBodyParts = BodyDamageDataManager.getBodyParts(source.getMsgId());
        List<BodyPartEnum> hitBodyParts = new ArrayList<>();
        if (damageSourceBodyParts != null) {
            if (damageSourceBodyParts.damageDistribution != DamageDistributionEnum.NONE) {
                hitBodyParts.addAll(damageSourceBodyParts.getBodyParts(player));
            }
        } else {
            if (source.is(DamageTypeTags.IS_PROJECTILE) && source.getDirectEntity() != null) {
                hitBodyParts.addAll(PlayerModelUtil.getPreciseEntityImpact(source.getDirectEntity(), player));
            } else if (source.getDirectEntity() != null) {
                List<BodyPartEnum> possibleHitParts = PlayerModelUtil.getEntityImpact(source.getDirectEntity(), player);
                if (!possibleHitParts.isEmpty()) {
                    hitBodyParts.addAll(DamageDistributionEnum.ONE_OF.getBodyParts(player, possibleHitParts));
                }
            }
        }

        if (hitBodyParts.isEmpty()) {
            hitBodyParts.addAll(DamageDistributionEnum.ONE_OF.getBodyParts(player, Arrays.asList(BodyPartEnum.values())));
        }

        if (source.is(DamageTypeTags.IS_PROJECTILE)
                && hitBodyParts.contains(BodyPartEnum.HEAD)
                && Config.Baked.headCriticalShotMultiplier > 1.0
                && player.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
            damage *= (float) Config.Baked.headCriticalShotMultiplier;
            player.level().playLocalSound(player.blockPosition(), SoundRegistry.HEADSHOT.get(), SoundSource.HOSTILE, 1.0F, 1.0F, false);
        }

        if (!hitBodyParts.isEmpty()) {
            DAMAGE_TO_APPLY.set(0f);
            BodyDamageUtil.balancedHurtBodyParts(player, hitBodyParts, damage);
            damage = DAMAGE_TO_APPLY.get();
        }

        return damage;
    }

    public void hurtBodyPartAndDistribute(Player player, BodyPartEnum part, float amount) {
        if (!Config.Baked.localizedBodyDamageEnabled || part == null) return;

        IBodyDamageCapability capability = CapabilityUtil.getBodyDamageCapability(player);
        double bodyResistance = BodyDamageUtilInternal.BODY_RESISTANCE.getAttribute(player).getValue();

        record PartDamage(BodyPartEnum part, Float damage) {}
        Deque<PartDamage> partQueue = new ArrayDeque<>();
        Set<BodyPartEnum> visited = new HashSet<>();

        partQueue.add(new PartDamage(part, amount));
        visited.add(part);

        while (!partQueue.isEmpty()) {
            PartDamage current = partQueue.poll();
            BodyPartEnum currentPart = current.part;
            float rawDamage = current.damage;

            double limbResistance = BodyDamageUtilInternal.bodyPartResistanceAttribute.get(currentPart).getAttribute(player).getValue();
            float resistanceFactor = (float) (1.0 - bodyResistance - limbResistance);
            float effectiveDamage = rawDamage * resistanceFactor;

            float capacity = capability.getBodyPartMaxHealth(currentPart) - capability.getBodyPartDamage(currentPart);
            float overflow = Math.max(0.0F, effectiveDamage - capacity);

            capability.hurt(currentPart, effectiveDamage - overflow);
            DAMAGE_TO_APPLY.set(DAMAGE_TO_APPLY.get() + effectiveDamage - overflow);

            if (!(overflow > 0 && resistanceFactor > 0)) continue;

            float rawOverflow = overflow / resistanceFactor;
            List<BodyPartEnum> available = currentPart.getNeighbours().stream()
                    .filter(n -> !visited.contains(n))
                    .toList();

            if (available.isEmpty()) continue;

            float share = rawOverflow / available.size();
            for (BodyPartEnum neighbour : available) {
                visited.add(neighbour);
                partQueue.add(new PartDamage(neighbour, share));
            }
        }

        float limbDerivedHealth = getLimbDerivedHealth(player);
        if(limbDerivedHealth < 0.05) {
            DAMAGE_TO_APPLY.set(player.getHealth());
        }
    }

    @SubscribeEvent
    public void simulateLimbHeal(LivingHealEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide) return;

        float healAmount = event.getAmount();
        if(LIMBS_ALREADY_HEALED.get()) return;

        healLimbs(player, healAmount);
    }

    private void healLimbs(Player player, float healAmount) {
        BodyDamageCapability capability = CapabilityUtil.getBodyDamageCapability(player);
        List<BodyPartEnum> sortedParts = new ArrayList<>();
        for (BodyPartEnum part : BodyPartEnum.values()) {
            if (capability.getBodyPartDamage(part) > 0) {
                sortedParts.add(part);
            }
        }
        if (sortedParts.isEmpty()) return;

        sortedParts.sort(Comparator.<BodyPartEnum>comparingDouble(part -> capability.getBodyPartDamage(part) / capability.getBodyPartMaxHealth(part)).reversed());

        float threshold = 0.05f;
        int n = sortedParts.size();
        float[] remainingDamage = new float[n];
        for (int i = 0; i < n; i++) {
            remainingDamage[i] = capability.getBodyPartDamage(sortedParts.get(i));
        }

        for (int tier = 1; tier <= n && healAmount > 0; tier++) {
            float topDamage = remainingDamage[0];
            float nextDamage = tier < n ? remainingDamage[tier] : 0f;
            float gap = topDamage - nextDamage;

            if (gap < threshold && tier < n) continue;

            float[] healNeeds = new float[tier];
            float totalHealNeeded = 0;
            for (int i = 0; i < tier; i++) {
                healNeeds[i] = Math.max(0, remainingDamage[i] - nextDamage);
                totalHealNeeded += healNeeds[i];
            }

            if (totalHealNeeded > 0) {
                float ratio = Math.min(1f, healAmount / totalHealNeeded);
                for (int i = 0; i < tier; i++) {
                    float amount = healNeeds[i] * ratio;
                    capability.heal(sortedParts.get(i), amount);
                    remainingDamage[i] -= amount;
                }
                healAmount -= totalHealNeeded * ratio;
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void monitorHealth(TickEvent.PlayerTickEvent event) {
        if (!(event.player instanceof ServerPlayer player)) return;
        if (player.isDeadOrDying()) return;

        float limbHealth = getLimbDerivedHealth(player);
        if(Math.abs(player.getHealth() - limbHealth) > 0.01f) {
            if (limbHealth < player.getHealth()) {
                healLimbs(player, player.getHealth() - limbHealth);
            } else {
                player.setHealth(limbHealth);
            }
        }
    }

    public void syncHealth(Player player) {
        if(player.isDeadOrDying()) return;

        player.setHealth(getLimbDerivedHealth(player));
    }

    private float getLimbDerivedHealth(Player player) {
        BodyDamageCapability capability = CapabilityUtil.getBodyDamageCapability(player);
        float totalDamage = 0;
        for (BodyPartEnum part : BodyPartEnum.values()) {
            totalDamage += capability.getBodyPartDamage(part);
        }
        return (float) HealthUtil.getPlayerStableMaxHealth(player) - totalDamage;
    }

    private static boolean shouldApplyLocalizedBodyDamage(Player player) {
        return !player.isCreative() && !player.isSpectator() && Config.Baked.localizedBodyDamageEnabled;
    }

    private static boolean shouldApplyHealthOverhaul(Player player) {
        return !player.isCreative() && !player.isSpectator() && Config.Baked.healthOverhaulEnabled;
    }

    public void healPlayerDirectly(Player player, float healAmount) {
        LIMBS_ALREADY_HEALED.set(true);
        float health = getLimbDerivedHealth(player);
        if (player.getHealth() < health) {
            player.heal(healAmount);
        }
        LIMBS_ALREADY_HEALED.set(false);
    }

    public boolean willLoseHearts(Player player) {
        return CapabilityUtil.getHealthCapability(player).getAdditionalHealth() > 0;
    }
}
