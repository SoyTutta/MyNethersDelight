package com.soytutta.mynethersdelight.common.events;

import com.soytutta.mynethersdelight.common.enchantment.PoachingData;
import com.soytutta.mynethersdelight.common.registry.MNDEnchantmentComponents;
import com.soytutta.mynethersdelight.common.tag.MNDTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.hoglin.HoglinBase;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.List;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME)
public class CommonEvent {

    @SubscribeEvent
    public static void livingDie(LivingDeathEvent event){
        if (!event.getEntity().level().isClientSide
                && event.getEntity() instanceof Mob mob && event.getSource() != null
                && event.getSource().getDirectEntity() instanceof LivingEntity directSource
                && directSource.getItemInHand(InteractionHand.MAIN_HAND).is(net.minecraft.tags.ItemTags.WEAPON_ENCHANTABLE)) {
            if (EnchantmentHelper.has(directSource.getItemInHand(InteractionHand.MAIN_HAND), MNDEnchantmentComponents.POACHING.get())
                    && (mob.getMaxHealth() < 150.0F || mob.getType().is(MNDTags.SPECIAL_HUNT))
                    && (((directSource.hasEffect(MobEffects.LUCK) || directSource.hasEffect(MobEffects.UNLUCK)) && event.getEntity().level().random.nextFloat() < 0.6F)
                    || directSource.getItemInHand(InteractionHand.MAIN_HAND).is(ModTags.KNIVES)
                    || event.getEntity().level().random.nextFloat() < 0.4F)) {

                PoachingData poachingData = EnchantmentHelper.pickHighestLevel(directSource.getItemInHand(InteractionHand.MAIN_HAND), MNDEnchantmentComponents.POACHING.get()).orElse(PoachingData.DEFAULT);
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
                float FailProbability = baseFailProbability - (luckLevel * 0.1F) + (badLuckLevel * 0.2F);

                CompoundTag mobNBT = new CompoundTag();
                mob.save(mobNBT);

                if (mob instanceof Slime) {
                    if (((Slime) mob).getSize() != 1) {
                        for (int i = 0; i < 2; i++) {
                            Mob mobCopy = (Mob) mob.getType().create(mob.level());
                            if (mobCopy != null) {
                                ((Slime) mobCopy).setSize(((Slime) mob).getSize() - 1, true);
                                mobCopy.moveTo(mob.getX() + i, mob.getY(), mob.getZ() + i, mob.getYRot(), mob.getXRot());
                                mob.level().addFreshEntity(mobCopy);
                            }
                        }
                        return;
                    }
                }

                // FAILED HUNT
                if ((event.getEntity().level().random.nextFloat() < FailProbability
                        || (mob.isBaby() && event.getEntity().level().random.nextFloat() < 0.2F)
                        || directSource.getItemInHand(InteractionHand.MAIN_HAND).is(ModTags.KNIVES))
                        && !mob.hasEffect(MobEffects.CONFUSION)) {

                    if (mob instanceof Horse horse
                            && (event.getEntity().level().random.nextFloat() < (FailProbability / 2)
                            || (horse.isTamed() && event.getEntity().level().random.nextFloat() < FailProbability)
                            || (directSource.getItemInHand(InteractionHand.MAIN_HAND).is(ModTags.KNIVES) && event.getEntity().level().random.nextFloat() < FailProbability))) {

                        ZombieHorse zombieHorse = EntityType.ZOMBIE_HORSE.create(mob.level());
                        if (zombieHorse != null) {
                            transferAttributes(horse, zombieHorse);
                            mob.level().playSound(null, mob.getX(), mob.getY(), mob.getZ(), SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundSource.PLAYERS, 1.0F, 1.0F);
                            zombieHorse.moveTo(mob.getX(), mob.getY(), mob.getZ(), mob.getYRot(), mob.getXRot());
                            mob.level().addFreshEntity(zombieHorse);
                            zombieHorse.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));

                            mob.setInvisible(true);
                            return;
                        }
                    }

                    mob.addTag("prevent_drops");

                    if (mob instanceof Spider
                            && event.getEntity().level().random.nextFloat() < FailProbability) {

                        CaveSpider caveSpider = EntityType.CAVE_SPIDER.create(mob.level());
                        if (caveSpider != null) {
                            mob.level().playSound(null, mob.getX(), mob.getY(), mob.getZ(), SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundSource.PLAYERS, 1.0F, 1.0F);
                            caveSpider.moveTo(mob.getX(), mob.getY(), mob.getZ(), mob.getYRot(), mob.getXRot());
                            mob.level().addFreshEntity(caveSpider);

                            caveSpider.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));

                            mob.setInvisible(true);
                            return;
                        }
                    }

                    if ((mob instanceof Frog || mob instanceof Bat)
                            && (event.getEntity().level().random.nextFloat() < (FailProbability / 2)
                            || (mob.level().getBiome(mob.blockPosition()).is(Biomes.SWAMP) && event.getEntity().level().random.nextFloat() < 0.3F)
                            || (directSource.getItemInHand(InteractionHand.MAIN_HAND).is(ModTags.KNIVES) && event.getEntity().level().random.nextFloat() < 0.3F))) {

                        Witch witch = EntityType.WITCH.create(mob.level());
                        if (witch != null) {
                            mob.level().playSound(null, mob.getX(), mob.getY(), mob.getZ(), SoundEvents.EVOKER_PREPARE_SUMMON, SoundSource.PLAYERS, 0.5F, 1.0F);
                            mob.level().playSound(null, mob.getX(), mob.getY(), mob.getZ(), SoundEvents.WITCH_CELEBRATE, SoundSource.PLAYERS, 1.0F, 1.0F);
                            witch.moveTo(mob.getX(), mob.getY(), mob.getZ(), mob.getYRot(), mob.getXRot());
                            mob.level().addFreshEntity(witch);

                            witch.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));

                            mob.setInvisible(true);
                            return;
                        }
                    }

                    if (mob instanceof Allay) {
                        Vex vex = EntityType.VEX.create(mob.level());
                        if (vex != null) {
                            mob.level().playSound(null, mob.getX(), mob.getY(), mob.getZ(), SoundEvents.EVOKER_PREPARE_SUMMON, SoundSource.PLAYERS, 1.0F, 1.0F);
                            vex.moveTo(mob.getX(), mob.getY(), mob.getZ(), mob.getYRot(), mob.getXRot());
                            mob.level().addFreshEntity(vex);

                            vex.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));

                            mob.setInvisible(true);
                            return;
                        }
                    }

                    if (mob instanceof Villager villager) {
                        mob.level().playSound(null, mob.getX(), mob.getY(), mob.getZ(), SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundSource.PLAYERS, 1.0F, 1.0F);
                        ZombieVillager zombieVillager = EntityType.ZOMBIE_VILLAGER.create(mob.level());
                        if (zombieVillager != null) {
                            if (mob.isBaby()) {
                                zombieVillager.setBaby(true);
                            }

                            zombieVillager.setCanPickUpLoot(true);
                            zombieVillager.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
                            zombieVillager.setVillagerData(villager.getVillagerData());
                            zombieVillager.moveTo(mob.getX(), mob.getY(), mob.getZ(), mob.getYRot(), mob.getXRot());
                            mob.level().addFreshEntity(zombieVillager);

                            mob.setInvisible(true);
                            return;
                        }
                    }

                    if (mob instanceof AbstractPiglin) {
                        mob.level().playSound(null, mob.getX(), mob.getY(), mob.getZ(), SoundEvents.PIGLIN_BRUTE_CONVERTED_TO_ZOMBIFIED, SoundSource.PLAYERS, 1.0F, 1.0F);
                        ZombifiedPiglin zombifiedPiglin = EntityType.ZOMBIFIED_PIGLIN.create(mob.level());
                        if (zombifiedPiglin != null) {
                            if (mob.isBaby()) {
                                zombifiedPiglin.setBaby(true);
                            }

                            for (EquipmentSlot slot : EquipmentSlot.values()) {
                                zombifiedPiglin.setItemSlot(slot, mob.getItemBySlot(slot));
                                mob.setItemSlot(slot, ItemStack.EMPTY);
                            }

                            zombifiedPiglin.setCanPickUpLoot(true);
                            zombifiedPiglin.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
                            zombifiedPiglin.moveTo(mob.getX(), mob.getY(), mob.getZ(), mob.getYRot(), mob.getXRot());
                            mob.level().addFreshEntity(zombifiedPiglin);

                            mob.setInvisible(true);
                            return;
                        }
                    }

                    if (mob instanceof HoglinBase) {
                        mob.level().playSound(null, mob.getX(), mob.getY(), mob.getZ(), SoundEvents.HOGLIN_CONVERTED_TO_ZOMBIFIED, SoundSource.PLAYERS, 1.0F, 1.0F);
                        Zoglin zoglin = EntityType.ZOGLIN.create(mob.level());
                        if (zoglin != null) {
                            if (mob.isBaby()) {
                                zoglin.setBaby(true);
                            }

                            zoglin.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
                            zoglin.moveTo(mob.getX(), mob.getY(), mob.getZ(), mob.getYRot(), mob.getXRot());
                            mob.level().addFreshEntity(zoglin);

                            mob.setInvisible(true);
                            return;
                        }
                    }

                    List<Mob> nearbyMobs = mob.level().getEntitiesOfClass(Mob.class, mob.getBoundingBox().inflate(5));
                    for (Mob nearbyMob : nearbyMobs) {
                        if (nearbyMob.getType() == mob.getType()) {
                            mob.level().playSound(null, mob.getX(), mob.getY(), mob.getZ(), SoundEvents.SOUL_ESCAPE, SoundSource.PLAYERS, 0.5F, 1.0F);
                            mob.setInvisible(false);

                            DamageSource damageSource = event.getSource();
                            nearbyMob.hurt(damageSource, 0.0F);
                        }
                    }
                    return;
                }

                // SUCCESSFUL HUNT
                PiglinBrute Hunter = EntityType.PIGLIN_BRUTE.create(mob.level());
                if (Hunter != null) {
                    Mob mobCopy = (Mob)mob.getType().create(mob.level());
                    if (mobCopy != null) {
                        mobCopy.load(mobNBT);
                        for (EquipmentSlot slot : EquipmentSlot.values()) {
                            mobCopy.setItemSlot(slot, mob.getItemBySlot(slot));
                            mob.setItemSlot(slot, ItemStack.EMPTY);
                        }

                        Hunter.setCustomName(Component.literal("\u00A7kHunter"));
                        mob.addTag("prevent_drops");
                        mobCopy.moveTo(mob.getX(), mob.getY(), mob.getZ(), mob.getYRot(), mob.getXRot());

                        mobCopy.setHealth(1);

                        ItemStack knife = new ItemStack(ModItems.FLINT_KNIFE.get());
                        ItemStack playerItem = directSource.getItemInHand(InteractionHand.MAIN_HAND);
                        EnchantmentHelper.setEnchantments(knife, playerItem.getTagEnchantments());

                        EnchantmentHelper.updateEnchantments(knife, (enchantments) -> {
                            enchantments.removeIf((enchantmentHolder) -> enchantmentHolder.is(MNDTags.POACHING_ENCHANTMENT));
                        });

                        if (mob.isBaby()) {
                            mobCopy.setBaby(false);
                        } else {
                            Hunter.setItemInHand(InteractionHand.MAIN_HAND, knife);
                        }

                        Hunter.doHurtTarget(mobCopy);
                        mob.remove(Entity.RemovalReason.DISCARDED);
                    }
                    Hunter.remove(Entity.RemovalReason.DISCARDED);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onMobDrop(LivingDropsEvent event) {
        if (event.getEntity() instanceof Mob mob && mob.getTags().contains("prevent_drops")) {
            event.getDrops().clear();
        }
    }

    private static void transferAttributes(Horse horse, ZombieHorse zombieHorse) {
        AttributeInstance horseMaxHealth = horse.getAttribute(Attributes.MAX_HEALTH);
        AttributeInstance zombieHorseMaxHealth = zombieHorse.getAttribute(Attributes.MAX_HEALTH);
        if (horseMaxHealth != null && zombieHorseMaxHealth != null) {
            zombieHorseMaxHealth.setBaseValue(horseMaxHealth.getBaseValue());
        }
        zombieHorse.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(horse.getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue());
        zombieHorse.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(horse.getAttribute(Attributes.JUMP_STRENGTH).getBaseValue());
    }
}
