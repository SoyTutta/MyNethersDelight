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
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;

public class MNDRecipes extends RecipeProvider {
    public MNDRecipes (PackOutput output) {
        super(output);
    }

    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        MNDSmeltingRecipes.register(consumer);
        MNDCraftingRecipes.register(consumer);
        MNDCuttingRecipes.register(consumer);
        MNDCookingRecipes.register(consumer);
    }
}
