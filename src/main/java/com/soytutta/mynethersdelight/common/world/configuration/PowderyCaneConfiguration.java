package com.soytutta.mynethersdelight.common.world.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record PowderyCaneConfiguration(int tries, int xzSpread, int ySpread) implements FeatureConfiguration {
    public static final Codec<PowderyCaneConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.POSITIVE_INT.fieldOf("tries").orElse(32).forGetter(PowderyCaneConfiguration::tries),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("xz_spread").orElse(6).forGetter(PowderyCaneConfiguration::xzSpread),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("y_spread").orElse(3).forGetter(PowderyCaneConfiguration::ySpread)
    ).apply(instance, PowderyCaneConfiguration::new));
}
