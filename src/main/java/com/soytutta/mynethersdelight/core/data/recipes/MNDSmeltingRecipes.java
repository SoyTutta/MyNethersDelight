//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.core.data.recipes;

import com.soytutta.mynethersdelight.common.registry.MNDItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.function.Consumer;


public class MNDSmeltingRecipes {  public MNDSmeltingRecipes() {
}

    public static void register(Consumer<FinishedRecipe> consumer) {
        foodSmeltingRecipes("hoglin_loin", MNDItems.HOGLIN_LOIN.get(), MNDItems.COOKED_LOIN.get(), 0.35F, consumer);
        foodSmeltingRecipes("hoglin_sausage", MNDItems.HOGLIN_SAUSAGE.get(), MNDItems.ROASTED_SAUSAGE.get(), 0.35F, consumer);
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.WHEAT_DOUGH.get()), Items.BREAD, 0.35F, 200).unlockedBy("has_dough", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.WHEAT_DOUGH.get()));
    }

    private static void foodSmeltingRecipes(String name, ItemLike ingredient, ItemLike result, float experience, Consumer<FinishedRecipe> consumer) {
        String namePrefix = (new ResourceLocation("mynethersdelight", name)).toString();
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ingredient), result, experience, 200).unlockedBy(name, InventoryChangeTrigger.TriggerInstance.hasItems(ingredient)).save(consumer);
        SimpleCookingRecipeBuilder.cooking(Ingredient.of(ingredient), result, experience, 600, RecipeSerializer.CAMPFIRE_COOKING_RECIPE).unlockedBy(name, InventoryChangeTrigger.TriggerInstance.hasItems(ingredient)).save(consumer, namePrefix + "_from_campfire_cooking");
        SimpleCookingRecipeBuilder.cooking(Ingredient.of(ingredient), result, experience, 100, RecipeSerializer.SMOKING_RECIPE).unlockedBy(name, InventoryChangeTrigger.TriggerInstance.hasItems(ingredient)).save(consumer, namePrefix + "_from_smoking");
    }
}
