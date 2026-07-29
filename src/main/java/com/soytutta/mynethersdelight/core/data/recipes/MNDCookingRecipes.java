//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.core.data.recipes;

import com.soytutta.mynethersdelight.MyNethersDelight;
import com.soytutta.mynethersdelight.common.tag.MNDTags;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import com.soytutta.mynethersdelight.common.tag.MyCommonTags;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.data.builder.CookingPotRecipeBuilder;

import java.util.function.Consumer;


public class MNDCookingRecipes {
    public MNDCookingRecipes() {
    }
    public static void register(Consumer<FinishedRecipe> consumer) {
        cookMinecraftSoups(consumer);
        cookMeals(consumer);
    }

    private static void cookMinecraftSoups(Consumer<FinishedRecipe> consumer) {
        CookingPotRecipeBuilder.cookingPotRecipe(MNDItems.STRIDER_STEW.get(), 1, 400, 1.0F)
                .addIngredient(MyCommonTags.FOODS_RAW_STRIDER)
                .addIngredient(Ingredient.of(Items.WARPED_FUNGUS, Items.CRIMSON_FUNGUS))
                .addIngredient(Ingredient.of(Items.WARPED_FUNGUS, Items.CRIMSON_FUNGUS, Items.RED_MUSHROOM, Items.BROWN_MUSHROOM))
                .addIngredient(Ingredient.of(Items.WARPED_ROOTS, Items.CRIMSON_ROOTS, ModItems.STRAW.get()))
                .unlockedByAnyIngredient(Items.CRIMSON_FUNGUS, Items.WARPED_FUNGUS, MNDItems.STRIDER_SLICE.get(), MNDItems.MINCED_STRIDER.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(MNDItems.SPICY_NOODLE_SOUP.get(), 1, 200, 1.0F)
                .addIngredient(MyCommonTags.FOODS_RICE_PASTA)
                .addIngredient(CommonTags.Items.COOKED_EGGS)
                .addIngredient(MNDTags.HOT_SPICE)
                .addIngredient(MyCommonTags.FOODS_RAW_HOGLIN)
                .unlockedByAnyIngredient(MNDItems.GHASTA.get(), MNDItems.BULLET_PEPPER.get(), Items.PORKCHOP)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(MNDItems.TWISTED_GHASTA.get(), 1, 200, 1.0F)
                .addIngredient(MNDItems.GHASTA.get())
                .addIngredient(Ingredient.of(Items.WARPED_FUNGUS, Items.TWISTING_VINES),2)
                .unlockedByAnyIngredient(MNDItems.GHASTA.get(), Items.WARPED_FUNGUS, Items.TWISTING_VINES)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(MNDItems.FRIES_GHASTA.get(), 1, 100, 1.0F, Items.PAPER)
                .addIngredient(MNDItems.GHASTA.get(),2)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(MNDItems.GIANT_TAKOYAKI.get(), 1, 200, 1.0F)
                .addIngredient(MyCommonTags.FOODS_GIANT_TENTACLES)
                .addIngredient(MyCommonTags.FOODS_GIANT_TENTACLES)
                .addIngredient(CommonTags.Items.DOUGH)
                .addIngredient(ModItems.ONION.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(MNDItems.SPICY_HOGLIN_STEW.get(), 1, 200, 1.0F)
                .addIngredient(Ingredient.of(MNDItems.HOGLIN_LOIN.get(), MNDItems.HOGLIN_SAUSAGE.get(), MNDItems.ROASTED_SAUSAGE.get(), MNDItems.COOKED_LOIN.get()))
                .addIngredient(Tags.Items.CROPS_POTATO)
                .addIngredient(Tags.Items.CROPS_CARROT)
                .addIngredient(Ingredient.of(MNDItems.BULLET_PEPPER.get(), MNDItems.PEPPER_POWDER.get()))
                .unlockedByAnyIngredient(MNDItems.HOGLIN_LOIN.get(), MNDItems.BULLET_PEPPER.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);
    }
    private static void cookMeals(Consumer<FinishedRecipe> consumer) {
        CookingPotRecipeBuilder.cookingPotRecipe(MNDItems.BOILED_EGG.get(), 4, 200, 1.0F)
                .addIngredient(Ingredient.of(MNDTags.BOILED_EGG_CANDIDATE), 4)
                .unlockedByAnyIngredient(MNDItems.STRIDER_EGG.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(MNDItems.SCOTCH_EGGS.get(), 1, 200, 1.0F)
                .addIngredient(Ingredient.of(MyCommonTags.FOODS_BOILED_EGG),2)
                .addIngredient(Ingredient.of(ModItems.MINCED_BEEF.get(), MNDItems.HOGLIN_SAUSAGE.get()))
                .addIngredient(CommonTags.Items.BREAD)
                .unlockedByAnyIngredient(Items.EGG)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(MNDItems.EGG_SOUP.get(), 1, 200, 1.0F)
                .addIngredient(MyCommonTags.FOODS_BOILED_EGG)
                .addIngredient(Ingredient.of(CommonTags.Items.COOKED_EGGS),2)
                .addIngredient(CommonTags.Items.VEGETABLES_ONION)
                .unlockedByAnyIngredient(Items.EGG)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(MNDItems.DEVILED_EGG.get(), 2, 100, 1.0F)
                .addIngredient(MyCommonTags.FOODS_BOILED_EGG)
                .addIngredient(MNDTags.HOT_SPICE)
                .addIngredient(Ingredient.of(MNDItems.HOGLIN_SAUSAGE.get(), ModItems.BACON.get()))
                .unlockedByAnyIngredient(MNDItems.STRIDER_EGG.get(),MNDItems.BOILED_EGG.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(MNDItems.STRIDER_WITH_GRILLED_FUNGUS.get(), 1, 400, 1.0F)
                .addIngredient(MNDItems.STRIDER_SLICE.get())
                .addIngredient(Items.CRIMSON_FUNGUS, 1)
                .addIngredient(Items.WARPED_FUNGUS, 1)
                .addIngredient(Ingredient.of(Items.WARPED_FUNGUS, Items.CRIMSON_FUNGUS, Items.RED_MUSHROOM, Items.BROWN_MUSHROOM))
                .unlockedByAnyIngredient(Items.CRIMSON_FUNGUS, Items.WARPED_FUNGUS, MNDItems.STRIDER_SLICE.get(), MNDItems.MINCED_STRIDER.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);


        CookingPotRecipeBuilder.cookingPotRecipe(MNDItems.CRIMSON_STROGANOFF.get(), 1, 400, 1.0F)
                .addIngredient(MNDItems.MINCED_STRIDER.get())
                .addIngredient(Items.CRIMSON_FUNGUS, 2)
                .addIngredient(CommonTags.Items.MILK)
                .addIngredient(CommonTags.Items.PASTA_RAW_PASTA)
                .unlockedByAnyIngredient(MNDItems.MINCED_STRIDER.get(), Items.CRIMSON_FUNGUS)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);

        CookingPotRecipeBuilder.cookingPotRecipe(MNDItems.FRIED_HOGLIN_CHOP.get(), 1, 200, 1.0F)
                .addIngredient(MNDTags.LOIN_HOGLIN)
                .addIngredient(Items.WHEAT)
                .addIngredient(CommonTags.Items.MILK)
                .addIngredient(Tags.Items.EGGS)
                .addIngredient(MNDItems.BULLET_PEPPER.get())
                .unlockedByAnyIngredient(MNDItems.HOGLIN_LOIN.get(), MNDItems.BULLET_PEPPER.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);

        CookingPotRecipeBuilder.cookingPotRecipe(MNDItems.HOT_WINGS.get(), 1, 150, 0.35F)
                .addIngredient(CommonTags.Items.RAW_CHICKEN)
                .addIngredient(MNDTags.HOT_SPICE)
                .addIngredient(CommonTags.Items.CROPS_ONION)
                .unlockedByAnyIngredient(MNDItems.BULLET_PEPPER.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);

        CookingPotRecipeBuilder.cookingPotRecipe(MNDItems.SPICY_CURRY.get(), 1, 200, 1.0F)
                .addIngredient(MNDTags.CURRY_MEATS)
                .addIngredient(CommonTags.Items.MILK)
                .addIngredient(MNDTags.HOT_SPICE)
                .addIngredient(CommonTags.Items.CROPS_RICE)
                .addIngredient(CommonTags.Items.VEGETABLES)
                .addIngredient(Ingredient.of(Items.PUMPKIN,ModItems.PUMPKIN_SLICE.get()))
                .unlockedByAnyIngredient(MNDItems.BULLET_PEPPER.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);

        CookingPotRecipeBuilder.cookingPotRecipe(MNDItems.ROAST_STUFFED_HOGLIN.get(), 1, 700, 2.0F,Items.BOWL)
                .addIngredient(MNDTags.HOT_SPICE)
                .addIngredient(MNDTags.STUFFED_HOGLIN)
                .addIngredient(Items.CRIMSON_FUNGUS)
                .addIngredient(ModItems.NETHER_SALAD.get(), 2)
                .unlockedByAnyIngredient(MNDItems.RAW_STUFFED_HOGLIN.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);

        CookingPotRecipeBuilder.cookingPotRecipe(MNDItems.CHILIDOG.get(), 1, 200, 0.35F,MNDItems.HOTDOG.get())
                .addIngredient(MNDTags.HOT_SPICE)
                .addIngredient(Ingredient.of(MNDItems.MINCED_STRIDER.get(),ModItems.MINCED_BEEF.get()))
                .addIngredient(Items.NETHER_WART)
                .unlockedByAnyIngredient(MNDItems.HOTDOG.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(MNDItems.SAUSAGE_AND_POTATOES.get(), 1, 200, 0.35F)
                .addIngredient(Tags.Items.CROPS_POTATO)
                .addIngredient(MNDItems.HOGLIN_SAUSAGE.get(), 2)
                .unlockedByAnyIngredient(MNDItems.HOGLIN_SAUSAGE.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);

        CookingPotRecipeBuilder.cookingPotRecipe(MNDItems.HOT_CREAM.get(), 1, 50, 1.0F,Items.BUCKET)
                .addIngredient(MNDTags.HOT_SPICE)
                .addIngredient(Tags.Items.EGGS)
                .addIngredient(Ingredient.of(MNDItems.BULLET_PEPPER.get(), MNDItems.PEPPER_POWDER.get()))
                .addIngredient(Items.MAGMA_CREAM)
                .addIngredient(Items.LAVA_BUCKET)
                .addIngredient(Items.MAGMA_CREAM)

                .unlockedByAnyIngredient(Items.LAVA_BUCKET)
                .setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);

        CookingPotRecipeBuilder.cookingPotRecipe(MNDItems.ROCK_SOUP.get(), 1, 50, 0.35F)
                .addIngredient(Items.MAGMA_CREAM,2)
                .addIngredient(Ingredient.of(MNDItems.STRIDER_ROCK.get(),MNDItems.STRIDER_EGG.get()),2)

                .unlockedByAnyIngredient(Items.MAGMA_CREAM)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);
        CookingPotRecipeBuilder.cookingPotRecipe(MNDItems.BURNT_ROLL.get(), 2, 50, 0.35F)
                .addIngredient(Items.MAGMA_CREAM,2)
                .addIngredient(MNDTags.CURRY_MEATS)

                .unlockedByAnyIngredient(Items.MAGMA_CREAM)
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);
    }
}
