//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.core.data;

import com.soytutta.mynethersdelight.core.data.recipes.MNDCraftingRecipes;
import com.soytutta.mynethersdelight.core.data.recipes.MNDCookingRecipes;
import com.soytutta.mynethersdelight.core.data.recipes.MNDCuttingRecipes;
import com.soytutta.mynethersdelight.core.data.recipes.MNDSmeltingRecipes;
import net.minecraft.data.PackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.concurrent.CompletableFuture;

public class MNDRecipes extends RecipeProvider {

    public MNDRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }
    protected void buildRecipes(RecipeOutput output) {
        MNDSmeltingRecipes.register(output);
        MNDCraftingRecipes.register(output);
        MNDCuttingRecipes.register(output);
        MNDCookingRecipes.register(output);
    }
}
