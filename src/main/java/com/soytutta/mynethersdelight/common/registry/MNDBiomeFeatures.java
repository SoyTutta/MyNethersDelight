package com.soytutta.mynethersdelight.common.registry;

import com.soytutta.mynethersdelight.MyNethersDelight;
import com.soytutta.mynethersdelight.common.world.feature.PowderyCaneFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MNDBiomeFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, MyNethersDelight.MODID);
    public static final Supplier<Feature<NoneFeatureConfiguration>> POWDERYCANE = FEATURES.register("powderycane", () -> new PowderyCaneFeature(NoneFeatureConfiguration.CODEC));
}