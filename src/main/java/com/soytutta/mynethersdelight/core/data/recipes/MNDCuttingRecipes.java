//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.core.data.recipes;

import com.soytutta.mynethersdelight.common.registry.MNDItems;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import vectorwing.farmersdelight.common.tag.ForgeTags;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

import java.util.function.Consumer;


public class MNDCuttingRecipes {
    public MNDCuttingRecipes() {
    }
    public static void register(Consumer<FinishedRecipe> consumer) {
        cuttingAnimalItems(consumer);
        cuttingVegetables(consumer);

    }
    private static void cuttingAnimalItems(Consumer<FinishedRecipe> consumer) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(new ItemLike[]{
                                (ItemLike)MNDItems.STRIDER_ROCK.get()}),
                        Ingredient.of(ForgeTags.TOOLS_PICKAXES),
                        (ItemLike)MNDItems.STRIDER_EGG.get())
                .addResultWithChance(Items.BONE_MEAL, 0.25F)
                .build(consumer, "mynethersdelight:cutting/strider_egg");
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(new ItemLike[]{
                                (ItemLike)MNDItems.STRIDER_SLICE.get()}),
                        Ingredient.of(ForgeTags.TOOLS_KNIVES),
                        (ItemLike)MNDItems.MINCED_STRIDER.get(), 2)
                .addResult(Items.STRING)
                .addResultWithChance(Items.STRING, 0.5F, 2)
                .build(consumer, "mynethersdelight:cutting/minced_strider");
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(new ItemLike[]{
                                (ItemLike)MNDItems.HOGLIN_LOIN.get()}),
                        Ingredient.of(ForgeTags.TOOLS_KNIVES),
                        (ItemLike)MNDItems.HOGLIN_SAUSAGE.get(), 2)
                .build(consumer, "mynethersdelight:cutting/hoglin_sausage");
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(new ItemLike[]{
                                (ItemLike)MNDItems.HOGLIN_HIDE.get()})
                        , Ingredient.of(ForgeTags.TOOLS_KNIVES),
                        (ItemLike)Items.LEATHER, 4)
                .addResultWithChance(Items.LEATHER, 0.5F, 2)
                .build(consumer, "mynethersdelight:cutting/hoglin_hide");
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(new ItemLike[]{
                                (ItemLike)Items.BLAZE_ROD}),
                        Ingredient.of(ForgeTags.TOOLS_KNIVES),
                        Items.BLAZE_POWDER, 3)
                .addResultWithChance(Items.BLAZE_POWDER, 0.25F, 1)
                .build(consumer, "mynethersdelight:cutting/balze_rod");
    }
    private static void cuttingVegetables(Consumer<FinishedRecipe> consumer) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(new ItemLike[]{
                                (ItemLike)Items.SUGAR_CANE}),
                        Ingredient.of(ForgeTags.TOOLS_KNIVES),
                        Items.SUGAR, 1)
                .addResultWithChance(Items.SUGAR, 0.25F, 1)
                .build(consumer, "mynethersdelight:cutting/sugar_cane");
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(new ItemLike[]{
                                (ItemLike)MNDItems.POWDER_CANNON.get()}),
                        Ingredient.of(ForgeTags.TOOLS_KNIVES),
                        Items.GUNPOWDER, 1)
                .addResultWithChance(Items.GUNPOWDER, 0.25F, 1)
                .build(consumer, "mynethersdelight:cutting/gunpowder_cane");
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(new ItemLike[]{
                                (ItemLike)MNDItems.BULLET_PEPPER.get()}),
                        Ingredient.of(ForgeTags.TOOLS_KNIVES),
                        Items.BLAZE_POWDER, 1)
                .addResultWithChance(Items.BLAZE_POWDER, 0.25F, 1)
                .build(consumer, "mynethersdelight:cutting/bullet_pepper");
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(new ItemLike[]{
                                (ItemLike)MNDItems.CRIMSON_FUNGUS_COLONY.get()}),
                        Ingredient.of(ForgeTags.TOOLS_KNIVES),
                        Items.CRIMSON_FUNGUS, 5)
                .build(consumer, "mynethersdelight:cutting/crimson_fungus");
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(new ItemLike[]{
                                (ItemLike)MNDItems.WARPED_FUNGUS_COLONY.get()}),
                        Ingredient.of(ForgeTags.TOOLS_KNIVES),
                        Items.WARPED_FUNGUS, 5)
                .build(consumer, "mynethersdelight:cutting/warped_fungus");
        }
}
