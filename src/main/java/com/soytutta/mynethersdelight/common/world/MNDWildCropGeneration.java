package com.soytutta.mynethersdelight.common.world;

import com.soytutta.mynethersdelight.common.registry.MNDBiomeFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import vectorwing.farmersdelight.common.world.configuration.WildCropConfiguration;

import java.util.List;

public class MNDWildCropGeneration {
    public static Holder<ConfiguredFeature<WildCropConfiguration, ?>> FEATURE_PATCH_POWDERY_CANE;
    public static Holder<PlacedFeature> PATCH_PATCH_POWDERY_CANE;

    public MNDWildCropGeneration() {
    }
    public static void addMNDGeneration() {
        FEATURE_PATCH_POWDERY_CANE = register(
                (new ResourceLocation("mynethersdelight", "patch_powdery_cane")),
                (Feature)((Feature) MNDBiomeFeatures.POWDERYCANE.get()),
                (FeatureConfiguration)FeatureConfiguration.NONE
        );

        PATCH_PATCH_POWDERY_CANE = registerPlacement(new ResourceLocation(
                "mynethersdelight", "patch_powdery_cane"),
                FEATURE_PATCH_POWDERY_CANE, CountPlacement.of(8), InSquarePlacement.spread(),
                PlacementUtils.FULL_RANGE, BiomeFilter.biome()
        );
    }

    static Holder<PlacedFeature> registerPlacement(ResourceLocation id, Holder<? extends ConfiguredFeature<?, ?>> feature, PlacementModifier... modifiers) {
        return BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, id, new PlacedFeature(Holder.hackyErase(feature), List.of(modifiers)));
    }

    protected static <FC extends FeatureConfiguration, F extends Feature<FC>> Holder<ConfiguredFeature<FC, ?>> register(ResourceLocation id, F feature, FC featureConfig) {
        return register((Registry)BuiltinRegistries.CONFIGURED_FEATURE, (ResourceLocation)id, (Object)(new ConfiguredFeature(feature, featureConfig)));
    }

    private static <V extends T, T> Holder<T> register(Registry<T> registry, ResourceLocation id, V value) {
        return BuiltinRegistries.register(registry, id, value);
    }
}

