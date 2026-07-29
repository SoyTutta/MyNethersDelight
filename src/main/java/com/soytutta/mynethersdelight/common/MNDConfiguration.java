package com.soytutta.mynethersdelight.common;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraft.util.RandomSource;

public final class MNDConfiguration {
    private MNDConfiguration() {
    }

    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue ENABLE_PIGLIN_FOOD_TRADES;
    public static final ForgeConfigSpec.DoubleValue PIGLIN_FOOD_TRADE_CHANCE;
    public static final ForgeConfigSpec.BooleanValue ENABLE_FROG_MAGMA_CAKE_BEHAVIOR;

    public static final ForgeConfigSpec.BooleanValue ENABLE_RESURGENT_SOIL_PROPAGATION;
    public static final ForgeConfigSpec.DoubleValue RESURGENT_SOIL_TICK_MULTIPLIER;
    public static final ForgeConfigSpec.IntValue RESURGENT_SOIL_GROWTH_RANGE;
    public static final ForgeConfigSpec.IntValue RESURGENT_FARMLAND_HEAT_SEARCH_RADIUS;

    public static final ForgeConfigSpec.BooleanValue ENABLE_BLAZIER;
    public static final ForgeConfigSpec.DoubleValue BLAZIER_COOKING_TIME_MULTIPLIER;
    public static final ForgeConfigSpec.BooleanValue ENABLE_STONE_CABINETS;

    public static final ForgeConfigSpec.BooleanValue GENERATE_POWDERY_CANE;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("settings");
        ENABLE_PIGLIN_FOOD_TRADES = builder
                .comment("Allows Piglins to barter food items and Strider Rocks from this mod.",
                        "Special hunting-related trades are not affected.")
                .define("enablePiglinFoodTrades", true);
        PIGLIN_FOOD_TRADE_CHANCE = builder
                .comment("Chance that a Piglin barter gives food or Strider Rocks from this mod.",
                        "0.25 equals 25%; 0 disables these results.")
                .defineInRange("piglinFoodTradeChance", 0.25, 0.0, 1.0);
        ENABLE_FROG_MAGMA_CAKE_BEHAVIOR = builder
                .comment("Allows Frogs to seek out Magma Cake and Magma Cake Slices,",
                        "and lets players feed slices directly to a Frog.")
                .define("enableFrogMagmaCakeBehavior", true);
        builder.pop();

        builder.push("farming");
        ENABLE_RESURGENT_SOIL_PROPAGATION = builder
                .comment("Allows Resurgent Soil and Resurgent Soil Farmland to propagate nearby plants.")
                .define("enableResurgentSoilPropagation", true);
        RESURGENT_SOIL_TICK_MULTIPLIER = builder
                .comment("Multiplier for growth, transformation and propagation attempts made by Resurgent Soil",
                        "and Resurgent Soil Farmland. Farmland hydration and drying are not affected.")
                .defineInRange("resurgentSoilTickMultiplier", 1.0, 0.0, 16.0);
        RESURGENT_SOIL_GROWTH_RANGE = builder
                .comment("Maximum vertical distance that Resurgent Soil follows connected plants while applying growth.",
                        "Default: 10. Range: 0 ~ 64.")
                .defineInRange("resurgentSoilGrowthRange", 10, 0, 64);
        RESURGENT_FARMLAND_HEAT_SEARCH_RADIUS = builder
                .comment("Horizontal radius that Resurgent Soil Farmland searches for valid heat sources.",
                        "Vertical range is half this value. Set to 0 to disable heat hydration.",
                        "Default: 7. Range: 0 ~ 16.")
                .defineInRange("resurgentFarmlandHeatSearchRadius", 7, 0, 16);
        builder.pop();

        builder.push("crafting");
        ENABLE_BLAZIER = builder
                .comment("Master switch for the Blazier. Disabling hides its creative-tab item and recipes,",
                        "prevents placement and stops its behavior.")
                .define("enableBlazier", true);
        BLAZIER_COOKING_TIME_MULTIPLIER = builder
                .comment("Multiplier for cooking time in every Blazier mode.",
                        "Preserves the original 6 / 3 / 1 / 6 proportions; higher values take longer.")
                .defineInRange("blazierCookingTimeMultiplier", 1.0, 0.0, 64.0);
        ENABLE_STONE_CABINETS = builder
                .comment("Enables Nether Brick, Red Nether Brick and Blackstone Brick Cabinets.",
                        "Disabling hides their creative-tab items and recipes.")
                .define("enableStoneCabinets", true);
        builder.pop();

        builder.push("world");
        GENERATE_POWDERY_CANE = builder
                .comment("Controls whether Powdery Cane patches generate naturally in Crimson Forests.")
                .define("generatePowderyCane", true);
        builder.pop();

        SPEC = builder.build();
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SPEC);
    }

    public static int getTickAttempts(RandomSource random) {
        double multiplier = RESURGENT_SOIL_TICK_MULTIPLIER.get();
        int attempts = (int) multiplier;
        return attempts + (random.nextDouble() < multiplier - attempts ? 1 : 0);
    }
}
