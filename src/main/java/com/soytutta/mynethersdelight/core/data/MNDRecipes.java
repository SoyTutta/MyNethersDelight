//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.core.data;

import java.util.function.Consumer;

import com.soytutta.mynethersdelight.core.data.recipes.MNDCraftingRecipes;
import com.soytutta.mynethersdelight.core.data.recipes.MNDCookingRecipes;
import com.soytutta.mynethersdelight.core.data.recipes.MNDCuttingRecipes;
import com.soytutta.mynethersdelight.core.data.recipes.MNDSmeltingRecipes;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;

public class MNDRecipes extends RecipeProvider {
    public MNDRecipes (DataGenerator generator) {
        super(generator);
    }

    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        MNDSmeltingRecipes.register(consumer);
        MNDCraftingRecipes.register(consumer);
        MNDCuttingRecipes.register(consumer);
        MNDCookingRecipes.register(consumer);
    }
}
