package com.soytutta.mynethersdelight.core.data.worldgen.placement;

import com.soytutta.mynethersdelight.MyNethersDelight;
import com.soytutta.mynethersdelight.core.data.worldgen.MNDConfiguredFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountOnEveryLayerPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

import java.util.List;

public class MNDPlacements {
    public static final ResourceKey<PlacedFeature> PATCH_POWDERY_CANE = ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(MyNethersDelight.MODID, "patch_powdery_cane"));

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatureLookup = context.lookup(Registries.CONFIGURED_FEATURE);
        Holder<ConfiguredFeature<?, ?>> propelplantPatchConfiguredFeature = configuredFeatureLookup.getOrThrow(MNDConfiguredFeatures.PATCH_POWDERY_CANE);

        context.register(
                PATCH_POWDERY_CANE,
                new PlacedFeature(
                        propelplantPatchConfiguredFeature,
                        List.of(
                                RarityFilter.onAverageOnceEvery(16),
                                CountOnEveryLayerPlacement.of(1),
                                BiomeFilter.biome()
                        )
                )
        );
    }
}
