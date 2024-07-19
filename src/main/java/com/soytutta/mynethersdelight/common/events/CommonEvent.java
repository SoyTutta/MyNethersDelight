package com.soytutta.mynethersdelight.common.events;

import com.soytutta.mynethersdelight.common.registry.MNDEnchantments;
import com.soytutta.mynethersdelight.common.tag.MNDTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ForgeTags;

import java.util.ArrayList;
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
                    && event.getEntity().level().random.nextFloat() < 0.4F) {
                PiglinBrute Hunter = EntityType.PIGLIN_BRUTE.create(mob.level());
                if (Hunter != null) {
                    Mob mobCopy = (Mob)mob.getType().create(mob.level());
                    if (mobCopy != null) {
                        mob.addTag("prevent_drops");
                        mobCopy.moveTo(mob.getX(), mob.getY(), mob.getZ(), mob.getYRot(), mob.getXRot());
                        mobCopy.setHealth(1);
                        mobCopy.setInvisible(true);

                        mobCopy.getMainHandItem().setCount(mob.getMainHandItem().getCount());
                        mobCopy.getOffhandItem().setCount(mob.getOffhandItem().getCount());
                        for (EquipmentSlot slot : EquipmentSlot.values()) {
                            if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                                mobCopy.setItemSlot(slot, mob.getItemBySlot(slot));
                            }
                        }

                        ItemStack knife = new ItemStack(ModItems.FLINT_KNIFE.get());
                        ItemStack playerItem = directSource.getItemInHand(InteractionHand.MAIN_HAND);
                        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(playerItem);

                        enchantments.remove(MNDEnchantments.HUNTING.get());
                        EnchantmentHelper.setEnchantments(enchantments, knife);
                        Hunter.setItemInHand(InteractionHand.MAIN_HAND, knife);
                        Hunter.doHurtTarget(mobCopy);
                    }
                    Hunter.remove(Entity.RemovalReason.DISCARDED);
                }

                if (mob.getType() == EntityType.VILLAGER && mob.getTags().contains("prevent_drops") &&
                        (event.getEntity().level().random.nextFloat() < 0.25F || (mob.isBaby() && event.getEntity().level().random.nextFloat() < 0.25F))) {
                    ZombieVillager zombieVillager = EntityType.ZOMBIE_VILLAGER.create(mob.level());
                    if (zombieVillager != null) {
                        mob.setInvisible(true);
                        if (mob.isBaby()) {
                            zombieVillager.setBaby(true);
                        }

                        if (mob instanceof Villager villager) {
                            zombieVillager.setVillagerData(villager.getVillagerData());
                        }

                        zombieVillager.moveTo(mob.getX(), mob.getY(), mob.getZ(), mob.getYRot(), mob.getXRot());
                        mob.level().addFreshEntity(zombieVillager);
                    }
                }

                if ((mob.getType() == EntityType.PIGLIN || mob.getType() == EntityType.PIGLIN_BRUTE) && (event.getEntity().level().random.nextFloat() < 0.25F
                        || (mob.isBaby() && event.getEntity().level().random.nextFloat() < 0.25F))) {
                    ZombifiedPiglin zombiefiedpiglin = EntityType.ZOMBIFIED_PIGLIN.create(mob.level());
                    if (zombiefiedpiglin != null) {
                        mob.setInvisible(true);
                        if (mob.isBaby()) {
                            zombiefiedpiglin.setBaby(true);
                        }
                        zombiefiedpiglin.moveTo(mob.getX(), mob.getY(), mob.getZ(), mob.getYRot(), mob.getXRot());
                        mob.level().addFreshEntity(zombiefiedpiglin);
                    }
                }

                if (mob.getType() == EntityType.HOGLIN  && (event.getEntity().level().random.nextFloat() < 0.25F
                        || (mob.isBaby() && event.getEntity().level().random.nextFloat() < 0.25F))) {
                    Zoglin zoglin = EntityType.ZOGLIN.create(mob.level());
                    if (zoglin != null) {
                        mob.setInvisible(true);
                        if (mob.isBaby()) {
                            zoglin.setBaby(true);
                        }
                        zoglin.moveTo(mob.getX(), mob.getY(), mob.getZ(), mob.getYRot(), mob.getXRot());
                        mob.level().addFreshEntity(zoglin);
                    }
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
