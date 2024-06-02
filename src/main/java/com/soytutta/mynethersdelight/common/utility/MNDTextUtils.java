package com.soytutta.mynethersdelight.common.utility;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class MNDTextUtils {
    public MNDTextUtils() {
    }
    public static MutableComponent getTranslation(String key, Object... args) {
        return Component.translatable("mynethersdelight." + key, args);
    }
}
