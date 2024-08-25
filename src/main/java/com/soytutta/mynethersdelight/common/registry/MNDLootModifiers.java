package com.soytutta.mynethersdelight.common.registry;

import com.mojang.serialization.MapCodec;
import com.soytutta.mynethersdelight.MyNethersDelight;
import com.soytutta.mynethersdelight.common.loot.MNDEspecialLootModifier;
import com.soytutta.mynethersdelight.common.loot.RemplaceLootModifier;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class MNDLootModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MyNethersDelight.MODID);

    public static final Supplier<MapCodec<? extends IGlobalLootModifier>> ESPECIAL_DROP =
            LOOT_MODIFIERS.register("especial_drop", MNDEspecialLootModifier.CODEC);
    public static final Supplier<MapCodec<? extends IGlobalLootModifier>> REMPLACE_LOOT =
            LOOT_MODIFIERS.register("remplace_item", RemplaceLootModifier.CODEC);
}