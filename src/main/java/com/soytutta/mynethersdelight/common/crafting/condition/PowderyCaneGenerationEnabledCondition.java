package com.soytutta.mynethersdelight.common.crafting.condition;

import com.mojang.serialization.MapCodec;
import com.soytutta.mynethersdelight.common.MNDConfiguration;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.NotNull;

public class PowderyCaneGenerationEnabledCondition implements ICondition
{
    public static final MapCodec<PowderyCaneGenerationEnabledCondition> CODEC = MapCodec.unit(new PowderyCaneGenerationEnabledCondition());

    @Override
    public boolean test(@NotNull IContext context) {
        return MNDConfiguration.GENERATE_POWDERY_CANE.get();
    }

    @Override
    public @NotNull MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}
