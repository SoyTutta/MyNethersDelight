//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.core.data.recipes;

import com.soytutta.mynethersdelight.MyNethersDelight;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import com.soytutta.mynethersdelight.common.tag.MNDTags;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.Arrays;
import java.util.function.Consumer;


public class MNDSmeltingRecipes {  public MNDSmeltingRecipes() {
}

    public static void register(Consumer<FinishedRecipe> consumer) {
        foodSmeltingRecipes("boiled_egg", Ingredient.of(MNDTags.BOILED_EGG_CANDIDATE), MNDItems.BOILED_EGG.get(), 0.35F, consumer);
        foodSmeltingRecipes("hoglin_loin", Ingredient.of(MNDItems.HOGLIN_LOIN.get()), MNDItems.COOKED_LOIN.get(), 0.35F, consumer);
        foodSmeltingRecipes("hoglin_sausage", Ingredient.of(MNDItems.HOGLIN_SAUSAGE.get()), MNDItems.ROASTED_SAUSAGE.get(), 0.35F, consumer);

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(MNDItems.GHAST_DOUGH.get()), RecipeCategory.FOOD,
                        Items.BREAD, 0.35F, 200)
                .unlockedBy("has_dough", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.GHAST_DOUGH.get()))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(MyNethersDelight.MODID, "bread_from_smelting"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(MNDItems.GHAST_DOUGH.get()), RecipeCategory.FOOD,
                        Items.BREAD, 0.35F, 100)
                .unlockedBy("has_dough", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.GHAST_DOUGH.get()))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(MyNethersDelight.MODID, "bread_from_smoking"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(MNDItems.GHAST_SOURDOUGH.get()), RecipeCategory.FOOD,
                        MNDItems.BREAD_LOAF_BLOCK.get(), 0.35F, 400)
                .unlockedBy("has_dough", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.GHAST_SOURDOUGH.get()))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(MyNethersDelight.MODID, "bread_loaf_from_smelting"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(MNDItems.GHAST_SOURDOUGH.get()), RecipeCategory.FOOD,
                        MNDItems.BREAD_LOAF_BLOCK.get(), 0.35F, 200)
                .unlockedBy("has_dough", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.GHAST_SOURDOUGH.get()))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(MyNethersDelight.MODID, "bread_loaf_from_smoking"));

        foodSmeltingRecipes("toast", Ingredient.of(MNDItems.SLICES_OF_BREAD.get()), MNDItems.TOASTS.get(), 0.15F, consumer);
    }

    private static void foodSmeltingRecipes(String name, Ingredient ingredient, ItemLike result, float experience, Consumer<FinishedRecipe> consumer) {
        String namePrefix = (ResourceLocation.fromNamespaceAndPath("mynethersdelight", name)).toString();

        ItemLike[] items = Arrays.stream(ingredient.getItems())
                .map(ItemStack::getItem)
                .toArray(ItemLike[]::new);

        SimpleCookingRecipeBuilder.smelting(ingredient, RecipeCategory.FOOD, result, experience, 200)
                .unlockedBy(name, InventoryChangeTrigger.TriggerInstance.hasItems(items))
                .save(consumer, namePrefix + "_cooking");
        SimpleCookingRecipeBuilder.campfireCooking(ingredient, RecipeCategory.FOOD, result, experience, 600)
                .unlockedBy(name, InventoryChangeTrigger.TriggerInstance.hasItems(items))
                .save(consumer, namePrefix + "_from_campfire_cooking");
        SimpleCookingRecipeBuilder.smoking(ingredient, RecipeCategory.FOOD, result, experience, 100)
                .unlockedBy(name, InventoryChangeTrigger.TriggerInstance.hasItems(items))
                .save(consumer, namePrefix + "_from_smoking");
    }
}