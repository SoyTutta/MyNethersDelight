//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.core.data;

import com.soytutta.mynethersdelight.MyNethersDelight;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

import java.util.Set;

@SuppressWarnings("unused")
public class MNDDataGenerators {

    public MNDDataGenerators() {
    }

    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();

        MNDBlockTags blockTags = new MNDBlockTags(output, lookupProvider, helper);
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new MNDItemTags(output, lookupProvider, blockTags.contentsGetter(), helper));
        generator.addProvider(event.includeServer(), new MNDRecipes(output, lookupProvider));
        generator.addProvider(event.includeServer(), new MNDDataMaps(output, lookupProvider));
        generator.addProvider(event.includeClient(), new MNDLang(output));

        MNDBlockStates blockStates = new MNDBlockStates(output, helper);
        generator.addProvider(event.includeClient(), blockStates);

        generator.addProvider(true, new DatapackBuiltinEntriesProvider(
                output,
                lookupProvider,
                new RegistrySetBuilder(),
                Set.of(MyNethersDelight.MODID)
        ));
    }
}
