package com.soytutta.mynethersdelight.common.enchantment;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biomes;
import vectorwing.farmersdelight.common.tag.ModTags;
import com.soytutta.mynethersdelight.common.events.CommonEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PoachingFailureRegistry {
    private static final List<PoachingFailureCase> CASES = new ArrayList<>();

    public static void register(PoachingFailureCase failureCase) {
        CASES.add(failureCase);
    }

    public static Optional<PoachingFailureCase> findCaseFor(Mob mob, ItemStack weapon) {
        return CASES.stream().filter(c -> c.matches(mob, weapon)).findFirst();
    }

    public static void registerAll() {
        // Spider → Cave Spider
        register(new PoachingFailureCase(EntityType.SPIDER, EntityType.CAVE_SPIDER, SoundEvents.ZOMBIE_VILLAGER_CONVERTED,
                (mob, weapon) -> mob.level().random.nextFloat() < 0.4F,
                CommonEvent::transferDataAndMakeHostile));

        // Sheep → Wolf
        register(new PoachingFailureCase(EntityType.SHEEP, EntityType.WOLF, SoundEvents.WOLF_HOWL,
                (mob, weapon) -> !((Sheep) mob).isSheared() && mob.level().random.nextFloat() < 0.3F,
                CommonEvent::transferDataAndMakeHostile));

        // Frog → Witch
        register(new PoachingFailureCase(EntityType.FROG, EntityType.WITCH, SoundEvents.WITCH_CELEBRATE,
                (mob, weapon) -> (mob.level().getBiome(mob.blockPosition()).is(Biomes.SWAMP) && mob.level().random.nextFloat() < 0.3F) || (weapon.is(ModTags.Items.KNIVES) && mob.level().random.nextFloat() < 0.3F),
                CommonEvent::transferDataAndMakeHostile));

        // Bat → Witch
        register(new PoachingFailureCase(EntityType.BAT, EntityType.WITCH, SoundEvents.WITCH_CELEBRATE,
                (mob, weapon) -> (mob.level().getBiome(mob.blockPosition()).is(Biomes.SWAMP) && mob.level().random.nextFloat() < 0.3F) || (weapon.is(ModTags.Items.KNIVES) && mob.level().random.nextFloat() < 0.3F),
                CommonEvent::transferDataAndMakeHostile));

        // Allay → Vex
        register(new PoachingFailureCase(EntityType.ALLAY, EntityType.VEX, SoundEvents.EVOKER_PREPARE_SUMMON,
                (mob, weapon) -> true,
                CommonEvent::transferDataAndMakeHostile));

        // Hoglin → Zoglin
        register(new PoachingFailureCase(EntityType.HOGLIN, EntityType.ZOGLIN, SoundEvents.HOGLIN_CONVERTED_TO_ZOMBIFIED,
                (mob, weapon) -> true,
                CommonEvent::transferDataAndMakeHostile));

        // Horse → Zombie Horse
        register(new PoachingFailureCase(EntityType.HORSE, EntityType.ZOMBIE_HORSE, SoundEvents.ZOMBIE_VILLAGER_CONVERTED,
                (mob, weapon) -> mob.level().random.nextFloat() < 0.4F || (((AbstractHorse) mob).isTamed() && mob.level().random.nextFloat() < 0.2F),
                (original, newMob) -> CommonEvent.transferFullHorseData((AbstractHorse) original, (AbstractHorse) newMob)));

        // Zombie Horse → Skeleton Horse
        register(new PoachingFailureCase(EntityType.ZOMBIE_HORSE, EntityType.SKELETON_HORSE, SoundEvents.ZOMBIE_INFECT,
                (mob, weapon) -> mob.level().random.nextFloat() < 0.2F || (((AbstractHorse) mob).isTamed() && mob.level().random.nextFloat() < 0.1F),
                (original, newMob) -> CommonEvent.transferFullHorseData((AbstractHorse) original, (AbstractHorse) newMob)));

        // Zombie → Skeleton
        register(new PoachingFailureCase(EntityType.ZOMBIE, EntityType.SKELETON, SoundEvents.ZOMBIE_INFECT,
                (mob, weapon) -> !(mob instanceof ZombieVillager)  && !mob.isBaby() && mob.level().random.nextFloat() < 0.3F,
                CommonEvent::transferDataAndMakeHostile));

        // Villager → Zombie Villager
        register(new PoachingFailureCase(EntityType.VILLAGER, EntityType.ZOMBIE_VILLAGER, SoundEvents.ZOMBIE_VILLAGER_CONVERTED,
                (mob, weapon) -> true,
                (original, newMob) -> {
                    Villager villager = (Villager) original;
                    ZombieVillager zombieVillager = (ZombieVillager) newMob;
                    CompoundTag nbt = new CompoundTag();
                    original.addAdditionalSaveData(nbt);
                    nbt.remove("UUID"); nbt.remove("Health");
                    newMob.readAdditionalSaveData(nbt);
                    zombieVillager.setVillagerData(villager.getVillagerData());
                    CommonEvent.makeHostile(newMob, original.getLastHurtByMob());
                }));

        // Dog → Wolf
        register(new PoachingFailureCase(EntityType.WOLF, EntityType.WOLF, SoundEvents.WOLF_GROWL,
                (mob, weapon) -> ((Wolf) mob).isTame() && mob.level().random.nextFloat() < 0.5F,
                (original, newMob) -> {
                    CompoundTag nbt = new CompoundTag();
                    original.addAdditionalSaveData(nbt);
                    nbt.remove("UUID"); nbt.remove("Health");
                    newMob.readAdditionalSaveData(nbt);
                    CommonEvent.transferBasicMobData(original, newMob);
                    ((Wolf) newMob).setTame(false, true);
                    CommonEvent.makeHostile(newMob, original.getLastHurtByMob());
                }));

        // Piglin → Zombified Piglin
        register(new PoachingFailureCase(EntityType.PIGLIN, EntityType.ZOMBIFIED_PIGLIN, SoundEvents.PIGLIN_BRUTE_CONVERTED_TO_ZOMBIFIED,
                (mob, weapon) -> true,
                CommonEvent::transferDataAndMakeHostile));

        // Piglin Brute → Zombified Piglin
        register(new PoachingFailureCase(EntityType.PIGLIN_BRUTE, EntityType.ZOMBIFIED_PIGLIN, SoundEvents.PIGLIN_BRUTE_CONVERTED_TO_ZOMBIFIED,
                (mob, weapon) -> true,
                CommonEvent::transferDataAndMakeHostile));
    }
}