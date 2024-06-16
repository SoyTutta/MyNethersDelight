package com.soytutta.mynethersdelight.common.utility;

import com.soytutta.mynethersdelight.common.registry.MNDEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import vectorwing.farmersdelight.common.registry.ModEffects;

public class MNDFoodValues
    {
        // STRIDER
        public static final FoodProperties STRIDER_EGG = (new FoodProperties.Builder())
                .nutrition(3).saturationMod(0.3F)
                .alwaysEat().build();
        public static final FoodProperties BOILED_EGG = (new FoodProperties.Builder())
                .nutrition(4).saturationMod(0.7F)
                .fast().build();
        public static final FoodProperties DEVILED_EGG = (new FoodProperties.Builder())
                .nutrition(5).saturationMod(0.7F)
                .effect(() -> new MobEffectInstance(MNDEffects.BPUNGENT.get(), 200, 0), 1.0F)
                .fast().build();
        public static final FoodProperties STRIDER_SLICE = (new FoodProperties.Builder())
                .nutrition(4).saturationMod(0.7f)
                .effect(() -> new MobEffectInstance(MobEffects.POISON, 200, 0), 0.15F)
                .build();
        public static final FoodProperties MINCED_STRIDER = (new FoodProperties.Builder())
                .nutrition(2).saturationMod(0.75f)
                .meat().build();
        public static final FoodProperties BLEEDING_TARTAR = (new FoodProperties.Builder())
                .nutrition(6).saturationMod(0.75f)
                .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0), 1.0F)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), 1200, 0), 1.0F)
                .build();
        public static final FoodProperties STRIDER_AND_GRILLED_FUNGUS = (new FoodProperties.Builder())
                .nutrition(10).saturationMod(0.9f)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), 1200, 0), 1.0F)
                .build();
        public static final FoodProperties STRIDER_STEW = (new FoodProperties.Builder())
                .nutrition(8).saturationMod(0.6f)
                .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0), 1.0F)
                .effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), 1200, 0), 1.0F)
                .build();
        public static final FoodProperties CRIMSON_STROGANOFF = (new FoodProperties.Builder())
                .nutrition(11).saturationMod(0.75f)
                .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 600, 0), 1.0F)
                .effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), 2400, 0), 1.0F)
                .build();
        public static final FoodProperties STRIDERLOAF = (new FoodProperties.Builder())
                .nutrition(5).saturationMod(1.25f)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), 1200, 0), 1.0F)
                .build();
        public static final FoodProperties COLD_STRIDERLOAF = (new FoodProperties.Builder())
                .nutrition(11).saturationMod(0.1f)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), 800, 0), 1.0F)
                .build();
        // HOGLIN
        public static final FoodProperties HOGLIN_LOIN = (new FoodProperties.Builder())
                .nutrition(4).saturationMod(0.35f)
                .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 300, 0), 0.4F)
                .meat().build();
        // HOGLIN-Sausage
        public static final FoodProperties HOGLIN_SAUSAGE = (new FoodProperties.Builder())
                .nutrition(2).saturationMod(0.25f)
                .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 300, 0), 0.4F)
                .fast().meat().build();
        public static final FoodProperties ROASTED_SAUSAGE = (new FoodProperties.Builder())
                .nutrition(4).saturationMod(0.35f)
                .meat().build();
        public static final FoodProperties HOTDOG = (new FoodProperties.Builder())
                .nutrition(8).saturationMod(0.45f)
                .meat().build();
        public static final FoodProperties SAUSAGE_AND_POTATOES = (new FoodProperties.Builder())
                .nutrition(11).saturationMod(0.55f)
                .build();
        public static final FoodProperties BREAKFAST_SAMPLER = (new FoodProperties.Builder())
                .nutrition(15).saturationMod(0.75f)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), 2400, 0), 1.0F)
                .build();
        // HOGLIN-Loin
        public static final FoodProperties COOKED_LOIN = (new FoodProperties.Builder())
                .nutrition(7).saturationMod(0.75f)
                .meat().build();
        public static final FoodProperties BLUE_TENDERLOIN_STEAK = (new FoodProperties.Builder())
                .nutrition(9).saturationMod(0.8f)
                .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 300, 0), 1.0F)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), 1200, 0), 1.0F)
                .build();
        public static final FoodProperties FRIED_HOGLIN_CHOP = (new FoodProperties.Builder())
                .nutrition(12).saturationMod(0.9f)
                .effect(() -> new MobEffectInstance(MNDEffects.BPUNGENT.get(), 600, 0), 1.0F)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), 2400, 0), 1.0F)
                .build();
        // GHASTA
        public static final FoodProperties GHASTA = (new FoodProperties.Builder())
                .nutrition(2).saturationMod(0.3F)
                .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F)
                .build();
        public static final FoodProperties GHAST_SALAD = (new FoodProperties.Builder())
                .nutrition(8).saturationMod(0.7F)
                .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 300, 0), 1.0F)
                .build();
        public static final FoodProperties SPICY_NOODLE_SOUP = (new FoodProperties.Builder())
                .nutrition(14).saturationMod(0.75F)
                .effect(() ->  new MobEffectInstance(ModEffects.COMFORT.get(), 6000, 0), 1.0F)
                .effect(() -> new MobEffectInstance(MNDEffects.BPUNGENT.get(), 1200, 1), 1.0F)
                .build();
        public static final FoodProperties SPICY_COTTON = (new FoodProperties.Builder())
                .nutrition(2).saturationMod(2F)
                .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300, 0), 1.0F)
                .alwaysEat().build();
        public static final FoodProperties GHASTA_WITH_CREAM = (new FoodProperties.Builder())
                .nutrition(8).saturationMod(0.7F)
                .effect(() -> new MobEffectInstance(MNDEffects.BPUNGENT.get(), 600, 0), 1.0F)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), 1200, 0), 1.0F)
                .build();
        // SPICY
        public static final FoodProperties BULLET_PEPPER = (new FoodProperties.Builder())
                .nutrition(2).saturationMod(0.5f)
                .effect(() -> new MobEffectInstance(MNDEffects.BPUNGENT.get(), 600, 0), 1.0F)
                .alwaysEat().build();
        public static final FoodProperties SPICY_SKEWER = (new FoodProperties.Builder())
                .nutrition(7).saturationMod(0.9f)
                .effect(() -> new MobEffectInstance(MNDEffects.BPUNGENT.get(), 1200, 0), 1.0F)
                .build();
        public static final FoodProperties CHILIDOG = (new FoodProperties.Builder())
                .nutrition(13).saturationMod(0.45f)
                .effect(() -> new MobEffectInstance(MNDEffects.GPUNGENT.get(), 600, 1), 1.0F)
                .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300, 0), 1.0F)
                .build();
        public static final FoodProperties SPICY_HOGLIN_STEW = (new FoodProperties.Builder())
                .nutrition(12).saturationMod(0.8f)
                .effect(() -> new MobEffectInstance(MNDEffects.BPUNGENT.get(), 1200, 1), 1.0F)
                .effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), 2400, 0), 1.0F)
                .build();
        public static final FoodProperties HOT_WINGS = (new FoodProperties.Builder())
                .nutrition(10).saturationMod(0.45f)
                .effect(() -> new MobEffectInstance(MNDEffects.BPUNGENT.get(), 1200, 0), 1.0F)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), 1200, 0), 1.0F)
                .meat().build();
        public static final FoodProperties SPICY_CURRY = (new FoodProperties.Builder())
                .nutrition(15).saturationMod(0.65f)
                .effect(() -> new MobEffectInstance(MNDEffects.BPUNGENT.get(), 2400, 1), 1.0F)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), 3600, 0), 1.0F)
                .meat().build();


        // STUFFED HOGLIN
        public static final FoodProperties ROAST_EAR = (new FoodProperties.Builder())
                .nutrition(2).saturationMod(0.5f)
                .alwaysEat().meat().fast().build();
        public static final FoodProperties PLATE_OF_STUFFED_HOGLIN_SNOUT = (new FoodProperties.Builder())
                .nutrition(14).saturationMod(0.9f)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), 6000, 0), 1.0F)
                .build();
        public static final FoodProperties PLATE_OF_STUFFED_HOGLIN_HAM = (new FoodProperties.Builder())
                .nutrition(12).saturationMod(0.75f)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), 3600, 0), 1.0F)
                .build();
        public static final FoodProperties PLATE_OF_STUFFED_HOGLIN = (new FoodProperties.Builder())
                .nutrition(8).saturationMod(0.6f)
                .effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), 2400, 0), 1.0F)
                .build();

        // LAVA
        public static final FoodProperties HOT_CREAM_CONE = (new FoodProperties.Builder())
                .nutrition(4).saturationMod(1.0f)
                .effect(() -> new MobEffectInstance(MNDEffects.BPUNGENT.get(), 200, 0), 1.0F)
                .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0), 1.0F)
                .alwaysEat().build();
        public static final FoodProperties HOT_CREAM = (new FoodProperties.Builder())
                .nutrition(1).saturationMod(8.0f)
                .effect(() -> new MobEffectInstance(MNDEffects.BPUNGENT.get(), 600, 2), 1.0F)
                .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 600, 0), 1.0F)
                .alwaysEat().build();
    }