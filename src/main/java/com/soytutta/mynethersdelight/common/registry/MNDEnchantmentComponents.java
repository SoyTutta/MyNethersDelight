package com.soytutta.mynethersdelight.common.registry;

import com.soytutta.mynethersdelight.MyNethersDelight;
import com.soytutta.mynethersdelight.common.enchantment.PoachingData;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public class MNDEnchantmentComponents {
    public static final DeferredRegister<DataComponentType<?>> ENCHANTMENT_EFFECT_COMPONENTS = DeferredRegister.create(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, MyNethersDelight.MODID);
    public static final Supplier<DataComponentType<List<PoachingData>>> POACHING = ENCHANTMENT_EFFECT_COMPONENTS.register("poaching", () -> DataComponentType.<List<PoachingData>>builder()
            .persistent(PoachingData.CODEC.listOf())
            .build());
}
