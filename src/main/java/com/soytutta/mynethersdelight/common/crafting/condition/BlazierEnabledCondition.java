package com.soytutta.mynethersdelight.common.crafting.condition;

import com.mojang.serialization.MapCodec;
import com.soytutta.mynethersdelight.common.MNDConfiguration;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.NotNull;

public class BlazierEnabledCondition implements ICondition
{
    public static final MapCodec<BlazierEnabledCondition> CODEC = MapCodec.unit(new BlazierEnabledCondition());
    public static final BlazierEnabledCondition INSTANCE = new BlazierEnabledCondition();

    @Override
    public boolean test(@NotNull IContext context) {
        return MNDConfiguration.ENABLE_BLAZIER.get();
    }

    @Override
    public @NotNull MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}
