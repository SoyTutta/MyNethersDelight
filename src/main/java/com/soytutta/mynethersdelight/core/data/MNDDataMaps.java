//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.core.data;

import com.soytutta.mynethersdelight.common.registry.MNDItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.concurrent.CompletableFuture;

public class MNDDataMaps extends DataMapProvider
{
    protected MNDDataMaps(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather() {
        builder(NeoForgeDataMaps.COMPOSTABLES)
                // 30% chance
                .add(MNDItems.BULLET_PEPPER.get().asItem().builtInRegistryHolder(), new Compostable(0.3F), false)
                .add(MNDItems.STRIDER_EGG.get().asItem().builtInRegistryHolder(), new Compostable(0.3F), false)
                // 65% chance
                .add(MNDItems.GHASMATI.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)
                // 100% chance
                .add(MNDItems.GHASTA.get().asItem().builtInRegistryHolder(), new Compostable(1.0F), false)
                .add(MNDItems.CRIMSON_FUNGUS_COLONY.get().asItem().builtInRegistryHolder(), new Compostable(1.0F), false)
                .add(MNDItems.WARPED_FUNGUS_COLONY.get().asItem().builtInRegistryHolder(), new Compostable(1.0F), false);
    }
}