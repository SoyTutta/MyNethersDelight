package com.soytutta.mynethersdelight.core.data.worldgen;

import com.soytutta.mynethersdelight.MyNethersDelight;
import com.soytutta.mynethersdelight.common.registry.MNDBiomeFeatures;
import com.soytutta.mynethersdelight.common.world.configuration.PowderyCaneConfiguration;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class MNDConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?,?>> PATCH_POWDERY_CANE = ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(MyNethersDelight.MODID, "patch_powdery_cane"));

    public static void bootstrap(BootstapContext<ConfiguredFeature<?,?>> context) {
        context.register(
                PATCH_POWDERY_CANE,
                new ConfiguredFeature<>(
                        MNDBiomeFeatures.POWDERYCANE.get(),
                        new PowderyCaneConfiguration(32, 6, 3)
                )
        );
    }
}
