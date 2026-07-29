package com.soytutta.mynethersdelight.common.registry;

import com.soytutta.mynethersdelight.MyNethersDelight;
import com.soytutta.mynethersdelight.common.world.configuration.PowderyCaneConfiguration;
import com.soytutta.mynethersdelight.common.world.feature.PowderyCaneFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MNDBiomeFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.Keys.FEATURES, MyNethersDelight.MODID);
    public static final RegistryObject<Feature<PowderyCaneConfiguration>> POWDERYCANE =
            FEATURES.register("powderycane",
                    () -> new PowderyCaneFeature(PowderyCaneConfiguration.CODEC));
}
