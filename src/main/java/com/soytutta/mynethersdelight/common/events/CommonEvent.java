package com.soytutta.mynethersdelight.common.events;

import com.soytutta.mynethersdelight.common.MNDConfiguration;
import com.soytutta.mynethersdelight.common.entity.ia.EatMagmaCakeGoal;
import com.soytutta.mynethersdelight.common.enchantment.PoachingFailureCase;
import com.soytutta.mynethersdelight.common.enchantment.PoachingFailureRegistry;
import com.soytutta.mynethersdelight.common.registry.MNDEnchantments;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import com.soytutta.mynethersdelight.common.tag.MNDTags;
import com.soytutta.mynethersdelight.common.utility.EntityDropChanceAccessor;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.CommonTags;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Collections;

public class CommonEvent {
    @SubscribeEvent
    public void onFrogMagmaCakeInteraction(PlayerInteractEvent.EntityInteract event) {
        if (!MNDConfiguration.ENABLE_FROG_MAGMA_CAKE_BEHAVIOR.get()
                || !event.getItemStack().is(MNDItems.MAGMA_CAKE_SLICE.get())
                || !(event.getTarget() instanceof Frog frog)
                || !frog.isAlive()) {
            return;
        }

        boolean accepted = frog.goalSelector.getAvailableGoals().stream()
                .map(wrappedGoal -> wrappedGoal.getGoal())
                .filter(EatMagmaCakeGoal.class::isInstance)
                .map(EatMagmaCakeGoal.class::cast)
                .anyMatch(goal -> goal.requestFeeding(event.getEntity(), event.getHand()));

        if (accepted) {
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void livingDie(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide || !(event.getEntity() instanceof Mob mob)
                || !(event.getSource().getDirectEntity() instanceof LivingEntity directSource)) {
            return;
        }

        ItemStack weapon = directSource.getItemInHand(InteractionHand.MAIN_HAND);
        if (!weapon.is(CommonTags.Items.TOOLS)
                || weapon.getEnchantmentLevel(MNDEnchantments.HUNTING.get()) <= 0
                || (mob.getMaxHealth() >= 150.0F && !mob.getType().is(MNDTags.SPECIAL_HUNT))
                || (!((directSource.hasEffect(MobEffects.LUCK) || directSource.hasEffect(MobEffects.UNLUCK))
                && mob.level().random.nextFloat() < 0.6F)
                && !weapon.is(CommonTags.Items.TOOLS_KNIVES)
                && mob.level().random.nextFloat() >= 0.4F)) {
            return;
        }

        if (directSource instanceof ServerPlayer player && !player.isCreative()
                && !(weapon.getItem() instanceof TieredItem tieredItem && tieredItem.getTier() == Tiers.GOLD)) {
            weapon.hurtAndBreak(player.getRandom().nextInt(6) + 4, player,
                    living -> living.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }

        if (mob instanceof net.minecraft.world.entity.monster.Slime slime && slime.getSize() > 1) {
            for (int i = 0; i < 2; i++) {
                if (mob.getType().create(mob.level()) instanceof net.minecraft.world.entity.monster.Slime copy) {
                    copy.setSize(slime.getSize() - 1, true);
                    copy.moveTo(mob.getX() + i, mob.getY(), mob.getZ() + i, mob.getYRot(), mob.getXRot());
                    mob.level().addFreshEntity(copy);
                }
            }
            return;
        }

        float baseFailProbability = switch (mob.level().getDifficulty()) {
            default -> 0.1F;
            case PEACEFUL -> 0.0F;
            case EASY -> 0.2F;
            case NORMAL -> 0.3F;
            case HARD -> 0.4F;
        };
        int luckLevel = directSource.hasEffect(MobEffects.LUCK)
                ? directSource.getEffect(MobEffects.LUCK).getAmplifier() + 1 : 0;
        int badLuckLevel = directSource.hasEffect(MobEffects.UNLUCK)
                ? directSource.getEffect(MobEffects.UNLUCK).getAmplifier() + 1 : 0;
        float failProbability = baseFailProbability - luckLevel * 0.1F + badLuckLevel * 0.2F;
        boolean failed = (mob.level().random.nextFloat() < failProbability
                || (mob.isBaby() && mob.level().random.nextFloat() < 0.2F)
                || weapon.is(CommonTags.Items.TOOLS_KNIVES))
                && !mob.hasEffect(MobEffects.CONFUSION);

        if (failed) {
            Optional<PoachingFailureCase> failure = PoachingFailureRegistry.findCaseFor(mob, weapon);
            if (failure.isPresent()) {
                PoachingFailureCase failureCase = failure.get();
                Mob replacement = failureCase.getTargetType().create(mob.level());
                if (replacement != null) {
                    mob.level().playSound(null, mob.getX(), mob.getY(), mob.getZ(),
                            failureCase.getTransformSound(), SoundSource.PLAYERS, 1.0F, 1.0F);
                    if (mob.level() instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, mob.getX(), mob.getY(0.5),
                                mob.getZ(), 25, 0.5, 0.5, 0.5, 0.05);
                    }
                    failureCase.getSetupAction().accept(mob, replacement);
                    replacement.moveTo(mob.getX(), mob.getY(), mob.getZ(), mob.getYRot(), mob.getXRot());
                    replacement.setYHeadRot(mob.getYHeadRot());
                    replacement.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 600, 0));
                    mob.level().addFreshEntity(replacement);
                    event.setCanceled(true);
                    mob.discard();
                }
                return;
            }

            mob.addTag("prevent_drops");
            List<Mob> nearbyMobs = mob.level().getEntitiesOfClass(Mob.class, mob.getBoundingBox().inflate(10));
            for (Mob nearby : nearbyMobs) {
                if (nearby.getType() == mob.getType()) {
                    mob.level().playSound(null, mob.getX(), mob.getY(), mob.getZ(),
                            SoundEvents.SOUL_ESCAPE, SoundSource.PLAYERS, 0.5F, 1.0F);
                    nearby.hurt(event.getSource(), 0.0F);
                }
            }
            return;
        }

        PiglinBrute hunter = EntityType.PIGLIN_BRUTE.create(mob.level());
        if (hunter != null && mob.level() instanceof ServerLevel serverLevel) {
            DamageSource hunterDamage = mob.level().damageSources().mobAttack(hunter);
            List<ItemStack> lootToDrop = Collections.emptyList();
            if (mob.isBaby()) {
                Mob adultCopy = (Mob) mob.getType().create(serverLevel);
                if (adultCopy != null) {
                    CompoundTag mobTag = new CompoundTag();
                    mob.save(mobTag);
                    adultCopy.load(mobTag);
                    adultCopy.setBaby(false);
                    lootToDrop = ((EntityDropChanceAccessor) adultCopy)
                            .callGenerateLoot(hunterDamage);
                    adultCopy.remove(Entity.RemovalReason.DISCARDED);
                }
            } else {
                ItemStack knife = new ItemStack(ModItems.FLINT_KNIFE.get());
                Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(weapon);
                enchantments.remove(MNDEnchantments.HUNTING.get());
                EnchantmentHelper.setEnchantments(enchantments, knife);
                hunter.setItemInHand(InteractionHand.MAIN_HAND, knife);
                lootToDrop = ((EntityDropChanceAccessor) mob).callGenerateLoot(hunterDamage);
            }
            lootToDrop.forEach(mob::spawnAtLocation);
            mob.addTag("prevent_drops");
            hunter.remove(Entity.RemovalReason.DISCARDED);
        }
    }

    @SubscribeEvent
    public void onMobDrop(LivingDropsEvent event) {
        if (event.getEntity() instanceof Mob mob && mob.getTags().contains("prevent_drops")) {
            event.getDrops().clear();
        }
    }

    public static void transferBasicMobData(Mob original, Mob replacement) {
        if (original.hasCustomName()) {
            replacement.setCustomName(original.getCustomName());
            replacement.setCustomNameVisible(original.isCustomNameVisible());
        }
        if (original.isPersistenceRequired()) {
            replacement.setPersistenceRequired();
        }
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            replacement.setItemSlot(slot, original.getItemBySlot(slot).copy());
        }
        original.getActiveEffects().forEach(effect -> replacement.addEffect(new MobEffectInstance(effect)));
        replacement.setNoAi(original.isNoAi());
        if (original.isBaby()) {
            replacement.setBaby(true);
        }
    }

    public static void makeHostile(Mob mob, LivingEntity target) {
        if (target == null) {
            return;
        }
        if (mob instanceof NeutralMob neutralMob) {
            neutralMob.setPersistentAngerTarget(target.getUUID());
            neutralMob.startPersistentAngerTimer();
        }
        mob.setTarget(target);
    }

    public static void transferDataAndMakeHostile(Mob original, Mob replacement) {
        transferBasicMobData(original, replacement);
        makeHostile(replacement, original.getLastHurtByMob());
    }

    public static void transferFullHorseData(AbstractHorse original, AbstractHorse replacement) {
        CompoundTag tag = new CompoundTag();
        original.addAdditionalSaveData(tag);
        tag.remove("UUID");
        tag.remove("Health");
        replacement.readAdditionalSaveData(tag);
        transferBasicMobData(original, replacement);
    }
}
