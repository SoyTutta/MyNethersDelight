package com.soytutta.mynethersdelight.common.crafting.condition;

import com.mojang.serialization.MapCodec;
import com.soytutta.mynethersdelight.common.MNDConfiguration;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.NotNull;

public class StoneCabinetsEnabledCondition implements ICondition
{
    public static final MapCodec<StoneCabinetsEnabledCondition> CODEC = MapCodec.unit(new StoneCabinetsEnabledCondition());
    public static final StoneCabinetsEnabledCondition INSTANCE = new StoneCabinetsEnabledCondition();

    @Override
    public boolean test(@NotNull IContext context) {
        return MNDConfiguration.ENABLE_STONE_CABINETS.get();
    }

    @Override
    public @NotNull MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}
