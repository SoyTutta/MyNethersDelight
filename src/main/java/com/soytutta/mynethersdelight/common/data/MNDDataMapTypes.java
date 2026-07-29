package com.soytutta.mynethersdelight.common.data;

import com.soytutta.mynethersdelight.MyNethersDelight;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

public class MNDDataMapTypes {
    public static final DataMapType<Block, PlantRuleSet> PROPAGATION_TRANSFORMATIONS = DataMapType.builder(
            ResourceLocation.fromNamespaceAndPath(MyNethersDelight.MODID, "propagation_transformations"),
            Registries.BLOCK,
            PlantRuleSet.CODEC
    ).build();

    public static final DataMapType<Block, PlantRuleSet> LETIOS_COMPOST_TRANSFORMATIONS = DataMapType.builder(
            ResourceLocation.fromNamespaceAndPath(MyNethersDelight.MODID, "letios_compost_transformations"),
            Registries.BLOCK,
            PlantRuleSet.CODEC
    ).build();

    public static void register(RegisterDataMapTypesEvent event) {
        event.register(PROPAGATION_TRANSFORMATIONS);
        event.register(LETIOS_COMPOST_TRANSFORMATIONS);
    }
}
