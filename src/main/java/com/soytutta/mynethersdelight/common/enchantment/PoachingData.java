package com.soytutta.mynethersdelight.common.enchantment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record PoachingData(float defaultProbability, float peacefulProbability, float easyProbability, float normalProbability, float hardProbability) {
    public static final PoachingData DEFAULT = new PoachingData(0.1f, 0.0f, 0.2f, 0.3f, 0.4f);
    public static final Codec<PoachingData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("default_probability").forGetter(PoachingData::defaultProbability),
            Codec.FLOAT.fieldOf("peaceful_probability").forGetter(PoachingData::peacefulProbability),
            Codec.FLOAT.fieldOf("easy_probability").forGetter(PoachingData::easyProbability),
            Codec.FLOAT.fieldOf("normal_probability").forGetter(PoachingData::normalProbability),
            Codec.FLOAT.fieldOf("hard_probability").forGetter(PoachingData::hardProbability)
    ).apply(instance, PoachingData::new));
}
