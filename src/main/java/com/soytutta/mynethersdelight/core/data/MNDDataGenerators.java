//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.core.data;

import com.soytutta.mynethersdelight.MyNethersDelight;
import com.soytutta.mynethersdelight.core.data.worldgen.MNDConfiguredFeatures;
import com.soytutta.mynethersdelight.core.data.worldgen.biome.MNDBiomeModifiers;
import com.soytutta.mynethersdelight.core.data.worldgen.placement.MNDPlacements;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.concurrent.CompletableFuture;

import java.util.Set;

@Mod.EventBusSubscriber(
        modid = "mynethersdelight",
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class MNDDataGenerators {

    public MNDDataGenerators() {
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();
        MNDBlockTags blockTags = new MNDBlockTags(output, lookupProvider, helper);
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new MNDItemTags(output, lookupProvider, blockTags.contentsGetter(), helper));
        generator.addProvider(event.includeServer(), new MNDRecipes(output));
        MNDBlockStates blockStates = new MNDBlockStates(output, helper);
        generator.addProvider(event.includeClient(), blockStates);
        generator.addProvider(event.includeClient(), new MNDLang(output));

        RegistrySetBuilder registrySetBuilder = new RegistrySetBuilder()
                .add(Registries.CONFIGURED_FEATURE, MNDConfiguredFeatures::bootstrap)
                .add(Registries.PLACED_FEATURE, MNDPlacements::bootstrap)
                .add(ForgeRegistries.Keys.BIOME_MODIFIERS, MNDBiomeModifiers::bootstrap);
        generator.addProvider(true, new DatapackBuiltinEntriesProvider(
                output,
                lookupProvider,
                registrySetBuilder,
                Set.of(MyNethersDelight.MODID)
        ));
    }
}
