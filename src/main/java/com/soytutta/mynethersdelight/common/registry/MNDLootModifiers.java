package com.soytutta.mynethersdelight.common.registry;

import com.mojang.serialization.MapCodec;
import com.soytutta.mynethersdelight.MyNethersDelight;
import com.soytutta.mynethersdelight.common.loot.MNDPastrySlicingModifier;
import com.soytutta.mynethersdelight.common.loot.RemplaceLootModifier;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class MNDLootModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MyNethersDelight.MODID);

    public static final Supplier<MapCodec<? extends IGlobalLootModifier>> PASTRY_SLICING =
            LOOT_MODIFIERS.register("pastry_slicing", MNDPastrySlicingModifier.CODEC);
    public static final Supplier<MapCodec<? extends IGlobalLootModifier>> REMPLACE_LOOT =
            LOOT_MODIFIERS.register("remplace_item", RemplaceLootModifier.CODEC);
}