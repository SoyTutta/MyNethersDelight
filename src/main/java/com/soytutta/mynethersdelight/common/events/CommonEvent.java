package com.soytutta.mynethersdelight.common.events;

import com.soytutta.mynethersdelight.common.enchantment.PoachingData;
import com.soytutta.mynethersdelight.common.enchantment.PoachingFailureCase;
import com.soytutta.mynethersdelight.common.enchantment.PoachingFailureRegistry;
import com.soytutta.mynethersdelight.common.registry.MNDEnchantmentComponents;
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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME)
public class CommonEvent {

    @SubscribeEvent
    public static void livingDie(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide || !(event.getEntity() instanceof Mob mob) || event.getSource() == null || !(event.getSource().getDirectEntity() instanceof LivingEntity directSource)) {
            return;
        }

        ItemStack weapon = directSource.getItemInHand(InteractionHand.MAIN_HAND);
        if (!weapon.is(net.minecraft.tags.ItemTags.WEAPON_ENCHANTABLE)) {
            return;
        }

        if (EnchantmentHelper.has(weapon, MNDEnchantmentComponents.POACHING.get())
                && (mob.getMaxHealth() < 150.0F || mob.getType().is(MNDTags.SPECIAL_HUNT))
                && (((directSource.hasEffect(MobEffects.LUCK) || directSource.hasEffect(MobEffects.UNLUCK)) && mob.level().random.nextFloat() < 0.6F)
                || weapon.is(ModTags.KNIVES)
                || mob.level().random.nextFloat() < 0.4F)) {

            if (directSource instanceof ServerPlayer player && !player.isCreative()) {
                weapon.hurtAndBreak(4, directSource, EquipmentSlot.MAINHAND);
            }

            if (mob instanceof Slime slime && slime.getSize() > 1) {
                for (int i = 0; i < 2; i++) {
                    if (mob.getType().create(mob.level()) instanceof Slime mobCopy) {
                        mobCopy.setSize(slime.getSize() - 1, true);
                        mobCopy.moveTo(mob.getX() + i, mob.getY(), mob.getZ() + i, mob.getYRot(), mob.getXRot());
                        mob.level().addFreshEntity(mobCopy);
                    }
                }
                return;
            }

            PoachingData poachingData = EnchantmentHelper.pickHighestLevel(weapon, MNDEnchantmentComponents.POACHING.get()).orElse(PoachingData.DEFAULT);
            Difficulty difficulty = event.getEntity().level().getDifficulty();
            float baseFailProbability = switch (difficulty) {
                default -> poachingData.defaultProbability();
                case PEACEFUL -> poachingData.peacefulProbability();
                case EASY -> poachingData.easyProbability();
                case NORMAL -> poachingData.normalProbability();
                case HARD -> poachingData.hardProbability();
            };

            int luckLevel = directSource.hasEffect(MobEffects.LUCK) ? directSource.getEffect(MobEffects.LUCK).getAmplifier() + 1 : 0;
            int badLuckLevel = directSource.hasEffect(MobEffects.UNLUCK) ? directSource.getEffect(MobEffects.UNLUCK).getAmplifier() + 1 : 0;
            float failProbability = baseFailProbability - (luckLevel * 0.1F) + (badLuckLevel * 0.2F);

            boolean isFail = (mob.level().random.nextFloat() < failProbability
                    || (mob.isBaby() && mob.level().random.nextFloat() < 0.2F)
                    || weapon.is(ModTags.KNIVES))
                    && !mob.hasEffect(MobEffects.CONFUSION);

            if (isFail) {
                Optional<PoachingFailureCase> failureCaseOpt = PoachingFailureRegistry.findCaseFor(mob, weapon);

                if (failureCaseOpt.isPresent()) {
                    PoachingFailureCase failureCase = failureCaseOpt.get();
                    Mob newMob = failureCase.getTargetType().create(mob.level());

                    if (newMob != null) {
                        mob.level().playSound(null, mob.getX(), mob.getY(), mob.getZ(), failureCase.getTransformSound(), SoundSource.PLAYERS, 1.0F, 1.0F);

                        if (mob.level() instanceof ServerLevel serverLevel) {
                            serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, mob.getX(), mob.getY(0.5), mob.getZ(), 25, 0.5, 0.5, 0.5, 0.05);
                        }

                        failureCase.getSetupAction().accept(mob, newMob);
                        newMob.moveTo(mob.getX(), mob.getY(), mob.getZ(), mob.getYRot(), mob.getXRot());
                        newMob.setYHeadRot(mob.getYHeadRot());

                        newMob.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 600, 0));

                        mob.level().addFreshEntity(newMob);
                        event.setCanceled(true);
                        mob.discard();
                    }
                    return;
                }

                mob.addTag("prevent_drops");
                mob.level().getEntitiesOfClass(Mob.class, mob.getBoundingBox().inflate(10)).stream()
                        .filter(nearby -> nearby.getType() == mob.getType())
                        .forEach(nearby -> {
                            mob.level().playSound(null, mob.getX(), mob.getY(), mob.getZ(), SoundEvents.SOUL_ESCAPE, SoundSource.PLAYERS, 0.5F, 1.0F);
                            nearby.hurt(event.getSource(), 0.0F);
                        });
                return;
            }

            // SUCCESSFUL HUNT
            PiglinBrute Hunter = EntityType.PIGLIN_BRUTE.create(mob.level());
            if (Hunter != null && mob.level() instanceof ServerLevel serverLevel) {
                DamageSource hunterDamage;
                List<ItemStack> lootToDrop = Collections.emptyList();
                if (mob.isBaby()) {
                    hunterDamage = mob.level().damageSources().mobAttack(Hunter);
                    Mob adultCopy = (Mob) mob.getType().create(serverLevel);
                    if (adultCopy != null) {
                        CompoundTag nbt = new CompoundTag();
                        mob.save(nbt);
                        adultCopy.load(nbt);
                        if (adultCopy instanceof AgeableMob ageableCopy) {
                            ageableCopy.setBaby(false);
                        }
                        EntityDropChanceAccessor adultCopyAcc = (EntityDropChanceAccessor) adultCopy;
                        lootToDrop = adultCopyAcc.callGenerateLoot(hunterDamage);
                        adultCopy.remove(Entity.RemovalReason.DISCARDED);
                    }
                } else {
                    ItemStack knife = new ItemStack(ModItems.FLINT_KNIFE.get());
                    EnchantmentHelper.setEnchantments(knife, weapon.getTagEnchantments());
                    EnchantmentHelper.updateEnchantments(knife, (enchants) -> enchants.removeIf(e -> e.is(MNDTags.POACHING_ENCHANTMENT)));
                    Hunter.setItemInHand(InteractionHand.MAIN_HAND, knife);
                    hunterDamage = mob.level().damageSources().mobAttack(Hunter);
                    EntityDropChanceAccessor acc = (EntityDropChanceAccessor) mob;
                    lootToDrop = acc.callGenerateLoot(hunterDamage);
                }
                for (ItemStack stack : lootToDrop) {
                    mob.spawnAtLocation(stack);
                }
                mob.addTag("prevent_drops");
                Hunter.remove(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    @SubscribeEvent
    public static void onMobDrop(LivingDropsEvent event) {
        if (event.getEntity() instanceof Mob mob && mob.getTags().contains("prevent_drops")) {
            event.getDrops().clear();
        }
    }

    public static boolean shouldHorseTransform(AbstractHorse horse, float probability, ItemStack weapon) {
        return horse.level().random.nextFloat() < (probability / 2)
                || (horse.isTamed() && horse.level().random.nextFloat() < probability)
                || (weapon.is(ModTags.KNIVES) && horse.level().random.nextFloat() < probability);
    }

    public static void transferBasicMobData(Mob original, Mob newMob) {
        if (original.hasCustomName()) {
            newMob.setCustomName(original.getCustomName());
            newMob.setCustomNameVisible(original.isCustomNameVisible());
        }

        if (original.isPersistenceRequired()) {
            newMob.setPersistenceRequired();
        }

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            newMob.setItemSlot(slot, original.getItemBySlot(slot).copy());
        }

        original.getActiveEffects().forEach(effect -> newMob.addEffect(new MobEffectInstance(effect)));

        if (original.isNoAi()) {
            newMob.setNoAi(true);
        }
    }

    public static void transferFullHorseData(AbstractHorse original, AbstractHorse newHorse) {
        CompoundTag nbt = new CompoundTag();
        original.addAdditionalSaveData(nbt);
        nbt.remove("UUID");
        nbt.remove("Health");
        newHorse.readAdditionalSaveData(nbt);

        transferBasicMobData(original, newHorse);
    }
}