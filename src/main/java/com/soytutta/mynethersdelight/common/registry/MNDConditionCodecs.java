package com.soytutta.mynethersdelight.common.registry;

import com.mojang.serialization.MapCodec;
import com.soytutta.mynethersdelight.MyNethersDelight;
import com.soytutta.mynethersdelight.common.crafting.condition.BlazierEnabledCondition;
import com.soytutta.mynethersdelight.common.crafting.condition.PowderyCaneGenerationEnabledCondition;
import com.soytutta.mynethersdelight.common.crafting.condition.StoneCabinetsEnabledCondition;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class MNDConditionCodecs
{
    public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITION_CODECS = DeferredRegister.create(NeoForgeRegistries.CONDITION_SERIALIZERS, MyNethersDelight.MODID);

    public static final Supplier<MapCodec<? extends ICondition>> BLAZIER_ENABLED = CONDITION_CODECS.register("blazier_enabled", () -> BlazierEnabledCondition.CODEC);
    public static final Supplier<MapCodec<? extends ICondition>> STONE_CABINETS_ENABLED = CONDITION_CODECS.register("stone_cabinets_enabled", () -> StoneCabinetsEnabledCondition.CODEC);
    public static final Supplier<MapCodec<? extends ICondition>> POWDERY_CANE_GENERATION_ENABLED = CONDITION_CODECS.register("powdery_cane_generation_enabled", () -> PowderyCaneGenerationEnabledCondition.CODEC);
}
