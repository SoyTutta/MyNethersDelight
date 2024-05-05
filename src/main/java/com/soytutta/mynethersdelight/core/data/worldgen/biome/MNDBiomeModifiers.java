package com.soytutta.mynethersdelight.core.data.worldgen.biome;

import com.soytutta.mynethersdelight.MyNethersDelight;
import com.soytutta.mynethersdelight.core.data.worldgen.placement.MNDPlacements;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

public class MNDBiomeModifiers {
    private static final ResourceKey<BiomeModifier> FEATURE_PATCH_POWDERY_CANE = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(MyNethersDelight.MODID, "patch_powdery_cane"));
    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        HolderGetter<PlacedFeature> placedFeatureLookup = context.lookup(Registries.PLACED_FEATURE);
        Holder<PlacedFeature> propelplantPatchPlacedFeature = placedFeatureLookup.getOrThrow(MNDPlacements.PATCH_POWDERY_CANE);

        HolderGetter<Biome> biomeLookup = context.lookup(Registries.BIOME);
        Holder<Biome> crimsonForest = biomeLookup.getOrThrow(Biomes.CRIMSON_FOREST);

        context.register(
                FEATURE_PATCH_POWDERY_CANE,
                new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                        HolderSet.direct(crimsonForest),
                        HolderSet.direct(propelplantPatchPlacedFeature),
                        GenerationStep.Decoration.VEGETAL_DECORATION
                )
        );

    }
}