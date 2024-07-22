package com.soytutta.mynethersdelight.common.events;

import com.soytutta.mynethersdelight.common.registry.MNDEnchantments;
import com.soytutta.mynethersdelight.common.tag.MNDTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Animal;
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
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ForgeTags;

import java.util.List;
import java.util.Map;

public class CommonEvent {

    @SubscribeEvent
    public void livingDie(LivingDeathEvent event){
        if (!event.getEntity().level().isClientSide
                && event.getEntity() instanceof Mob mob && event.getSource() != null
                && event.getSource().getDirectEntity() instanceof LivingEntity directSource
                && directSource.getItemInHand(InteractionHand.MAIN_HAND).is(ForgeTags.TOOLS)) {
            if (directSource.getItemInHand(InteractionHand.MAIN_HAND).getEnchantmentLevel(MNDEnchantments.HUNTING.get()) > 0
                    && (mob.getMaxHealth() < 150.0F || mob.getType().is(MNDTags.SPECIAL_HUNT))
                    && (event.getEntity().level().random.nextFloat() < 0.4F)
                    || ((directSource.hasEffect(MobEffects.LUCK) || directSource.hasEffect(MobEffects.UNLUCK))
                    && event.getEntity().level().random.nextFloat() < 0.6F)) {

                Difficulty difficulty = event.getEntity().level().getDifficulty();
                float baseFailProbability = switch (difficulty) {
                    default -> 0.1F;
                    case PEACEFUL -> 0.0F;
                    case EASY -> 0.2F;
                    case NORMAL -> 0.3F;
                    case HARD -> 0.4F;
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
                        || (mob.isBaby() && event.getEntity().level().random.nextFloat() < 0.2F))
                        && !mob.hasEffect(MobEffects.CONFUSION)) {
                    mob.setInvisible(true);

                    if (mob instanceof Horse horse
                            && (event.getEntity().level().random.nextFloat() < (FailProbability / 2)
                            || (horse.isTamed() && event.getEntity().level().random.nextFloat() < FailProbability))){
                        ZombieHorse zombieHorse = EntityType.ZOMBIE_HORSE.create(mob.level());
                        if (zombieHorse != null) {
                            zombieHorse.setTamed(true);

                            AttributeInstance horseMaxHealth = horse.getAttribute(Attributes.MAX_HEALTH);
                            AttributeInstance zombieHorseMaxHealth = zombieHorse.getAttribute(Attributes.MAX_HEALTH);
                            if (horseMaxHealth != null && zombieHorseMaxHealth != null) {
                                zombieHorseMaxHealth.setBaseValue(horseMaxHealth.getBaseValue());
                            }
                            AttributeInstance horseMovementSpeed = horse.getAttribute(Attributes.MOVEMENT_SPEED);
                            AttributeInstance zombieHorseMovementSpeed = zombieHorse.getAttribute(Attributes.MOVEMENT_SPEED);
                            if (horseMovementSpeed != null && zombieHorseMovementSpeed != null) {
                                zombieHorseMovementSpeed.setBaseValue(horseMovementSpeed.getBaseValue());
                            }
                            AttributeInstance horseJumpStrength = horse.getAttribute(Attributes.JUMP_STRENGTH);
                            AttributeInstance zombieHorseJumpStrength = zombieHorse.getAttribute(Attributes.JUMP_STRENGTH);
                            if (horseJumpStrength != null && zombieHorseJumpStrength != null) {
                                zombieHorseJumpStrength.setBaseValue(horseJumpStrength.getBaseValue());
                            }

                            mob.level().playSound(null, mob.getX(), mob.getY(), mob.getZ(), SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundSource.PLAYERS, 1.0F, 1.0F);
                            zombieHorse.moveTo(mob.getX(), mob.getY(), mob.getZ(), mob.getYRot(), mob.getXRot());
                            mob.level().addFreshEntity(zombieHorse);

                            zombieHorse.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));                        }
                        return;
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
                        }
                        return;
                    }

                    if ((mob instanceof Frog || mob instanceof Bat)
                            && event.getEntity().level().random.nextFloat() < (FailProbability / 3)
                            || (mob.level().getBiome(mob.blockPosition()).is(Biomes.SWAMP) && event.getEntity().level().random.nextFloat() < 0.2F)) {
                        Witch witch = EntityType.WITCH.create(mob.level());
                        if (witch != null) {
                            mob.level().playSound(null, mob.getX(), mob.getY(), mob.getZ(), SoundEvents.EVOKER_PREPARE_SUMMON, SoundSource.PLAYERS, 1.0F, 1.0F);
                            witch.moveTo(mob.getX(), mob.getY(), mob.getZ(), mob.getYRot(), mob.getXRot());
                            mob.level().addFreshEntity(witch);

                            witch.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
                        }
                        return;
                    }

                    if (mob instanceof Allay) {
                        Vex vex = EntityType.VEX.create(mob.level());
                        if (vex != null) {
                            mob.level().playSound(null, mob.getX(), mob.getY(), mob.getZ(), SoundEvents.EVOKER_PREPARE_SUMMON, SoundSource.PLAYERS, 1.0F, 1.0F);
                            vex.moveTo(mob.getX(), mob.getY(), mob.getZ(), mob.getYRot(), mob.getXRot());
                            mob.level().addFreshEntity(vex);

                            vex.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
                        }
                        return;
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
                            return;
                        }
                    }

                    List<Mob> nearbyMobs = mob.level().getEntitiesOfClass(Mob.class, mob.getBoundingBox().inflate(15));
                    for (Mob nearbyMob : nearbyMobs) {
                        if (nearbyMob.getType() == mob.getType()) {
                            mob.level().playSound(null, mob.getX(), mob.getY(), mob.getZ(), SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                            mob.setInvisible(false);

                            if (mob instanceof NeutralMob || mob instanceof Enemy) {
                                nearbyMob.setTarget(directSource);
                            } else if (mob instanceof Animal) {
                                double deltaX = nearbyMob.getX() - mob.getX();
                                double deltaZ = nearbyMob.getZ() - mob.getZ();
                                double distance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
                                double targetX = nearbyMob.getX() + (15.0 * deltaX / distance);
                                double targetZ = nearbyMob.getZ() + (15.0 * deltaZ / distance);
                                nearbyMob.getNavigation().moveTo(targetX, nearbyMob.getY(), targetZ, 2.2);
                            }
                        }
                    }
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

                        mob.addTag("prevent_drops");
                        mobCopy.moveTo(mob.getX(), mob.getY(), mob.getZ(), mob.getYRot(), mob.getXRot());

                        mobCopy.setHealth(1); mobCopy.setInvisible(true);

                        ItemStack knife = new ItemStack(ModItems.FLINT_KNIFE.get());
                        ItemStack playerItem = directSource.getItemInHand(InteractionHand.MAIN_HAND);
                        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(playerItem);

                        enchantments.remove(MNDEnchantments.HUNTING.get());
                        EnchantmentHelper.setEnchantments(enchantments, knife);

                        if (mob.isBaby()) {
                            mobCopy.setBaby(false);
                        } else {
                            Hunter.setItemInHand(InteractionHand.MAIN_HAND, knife);
                        }

                        Hunter.doHurtTarget(mobCopy);
                    }
                    Hunter.remove(Entity.RemovalReason.DISCARDED);
                }
            }
        }
    }

    @SubscribeEvent
    public void onMobDrop(LivingDropsEvent event) {
        if (event.getEntity() instanceof Mob mob && mob.getTags().contains("prevent_drops")) {
            event.getDrops().clear();
        }
    }
}
