package com.soytutta.mynethersdelight.common.enchantment;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
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
        register(new PoachingFailureCase(EntityType.SPIDER, EntityType.CAVE_SPIDER, SoundEvents.ZOMBIE_VILLAGER_CONVERTED,
                (mob, weapon) -> mob.level().random.nextFloat() < 0.4F,
                CommonEvent::transferBasicMobData));

        register(new PoachingFailureCase(EntityType.FROG, EntityType.WITCH, SoundEvents.WITCH_CELEBRATE,
                (mob, weapon) -> (mob.level().getBiome(mob.blockPosition()).is(Biomes.SWAMP) && mob.level().random.nextFloat() < 0.3F) || (weapon.is(ModTags.KNIVES) && mob.level().random.nextFloat() < 0.3F),
                CommonEvent::transferBasicMobData));
        register(new PoachingFailureCase(EntityType.BAT, EntityType.WITCH, SoundEvents.WITCH_CELEBRATE,
                (mob, weapon) -> (mob.level().getBiome(mob.blockPosition()).is(Biomes.SWAMP) && mob.level().random.nextFloat() < 0.3F) || (weapon.is(ModTags.KNIVES) && mob.level().random.nextFloat() < 0.3F),
                CommonEvent::transferBasicMobData));

        register(new PoachingFailureCase(EntityType.ALLAY, EntityType.VEX, SoundEvents.EVOKER_PREPARE_SUMMON,
                (mob, weapon) -> true,
                CommonEvent::transferBasicMobData));

        register(new PoachingFailureCase(EntityType.HOGLIN, EntityType.ZOGLIN, SoundEvents.HOGLIN_CONVERTED_TO_ZOMBIFIED,
                (mob, weapon) -> true,
                (original, newMob) -> {
                    CommonEvent.transferBasicMobData(original, newMob);
                    if (original.isBaby()) ((Zoglin) newMob).setBaby(true);
                }));

        register(new PoachingFailureCase(EntityType.HORSE, EntityType.ZOMBIE_HORSE, SoundEvents.ZOMBIE_VILLAGER_CONVERTED,
                (mob, weapon) -> CommonEvent.shouldHorseTransform((AbstractHorse) mob, 0.4F, weapon), // Usamos la probabilidad base
                (original, newMob) -> CommonEvent.transferFullHorseData((AbstractHorse) original, (AbstractHorse) newMob)));

        register(new PoachingFailureCase(EntityType.ZOMBIE_HORSE, EntityType.SKELETON_HORSE, SoundEvents.ZOMBIE_INFECT,
                (mob, weapon) -> CommonEvent.shouldHorseTransform((AbstractHorse) mob, 0.4F, weapon),
                (original, newMob) -> CommonEvent.transferFullHorseData((AbstractHorse) original, (AbstractHorse) newMob)));

        register(new PoachingFailureCase(EntityType.ZOMBIE, EntityType.SKELETON, SoundEvents.ZOMBIE_INFECT,
                (mob, weapon) -> !(mob instanceof ZombieVillager) && mob.level().random.nextFloat() < 0.3F,
                CommonEvent::transferBasicMobData));

        register(new PoachingFailureCase(EntityType.VILLAGER, EntityType.ZOMBIE_VILLAGER, SoundEvents.ZOMBIE_VILLAGER_CONVERTED,
                (mob, weapon) -> true,
                (original, newMob) -> {
                    Villager villager = (Villager) original;
                    ZombieVillager zombieVillager = (ZombieVillager) newMob;
                    CompoundTag nbt = new CompoundTag();
                    original.addAdditionalSaveData(nbt);
                    nbt.remove("UUID");
                    nbt.remove("Health");
                    newMob.readAdditionalSaveData(nbt);
                    zombieVillager.setVillagerData(villager.getVillagerData());
                    if (original.isBaby()) {
                        zombieVillager.setBaby(true);
                    }
                }));

        register(new PoachingFailureCase(EntityType.PIGLIN, EntityType.ZOMBIFIED_PIGLIN, SoundEvents.PIGLIN_BRUTE_CONVERTED_TO_ZOMBIFIED,
                (mob, weapon) -> true,
                (original, newMob) -> {
                    CommonEvent.transferBasicMobData(original, newMob);
                    if (original.isBaby()) ((ZombifiedPiglin) newMob).setBaby(true);
                }));
        register(new PoachingFailureCase(EntityType.PIGLIN_BRUTE, EntityType.ZOMBIFIED_PIGLIN, SoundEvents.PIGLIN_BRUTE_CONVERTED_TO_ZOMBIFIED,
                (mob, weapon) -> true,
                (original, newMob) -> {
                    CommonEvent.transferBasicMobData(original, newMob);
                    if (original.isBaby()) ((ZombifiedPiglin) newMob).setBaby(true);
                }));
    }
}