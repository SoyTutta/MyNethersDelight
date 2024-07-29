//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.core.data.recipes;

import com.soytutta.mynethersdelight.common.registry.MNDItems;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.ItemAbilities;
import vectorwing.farmersdelight.common.crafting.ingredient.ItemAbilityIngredient;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

public class MNDCuttingRecipes {

    public static void register(RecipeOutput output) {
        cuttingAnimalItems(output);
        strippingWood(output);
        salvagingWoodenFurniture(output);
        cuttingVegetables(output);
        cuttingFoods(output);
    }

    private static void cuttingAnimalItems(RecipeOutput output) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.GHASTA.get()),
                        Ingredient.of(CommonTags.TOOLS_KNIFE),
                        MNDItems.GHASMATI.get())
                .addResultWithChance(MNDItems.GHASMATI.get(), 0.05F)
                .build(output, "mynethersdelight:cutting/ghasmati");
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.STRIDER_ROCK.get()),
                        new ItemAbilityIngredient(ItemAbilities.PICKAXE_DIG).toVanilla(),
                        MNDItems.STRIDER_EGG.get())
                .addResultWithChance(Items.BONE_MEAL, 0.25F)
                .build(output, "mynethersdelight:cutting/strider_egg");
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.STRIDER_SLICE.get()),
                        Ingredient.of(CommonTags.TOOLS_KNIFE),
                        MNDItems.MINCED_STRIDER.get(), 2)
                .addResult(Items.STRING)
                .addResultWithChance(Items.STRING, 0.5F, 2)
                .build(output, "mynethersdelight:cutting/minced_strider");
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.HOGLIN_LOIN.get()),
                        Ingredient.of(CommonTags.TOOLS_KNIFE),
                        MNDItems.HOGLIN_SAUSAGE.get(), 2)
                .build(output, "mynethersdelight:cutting/hoglin_sausage");
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.HOGLIN_HIDE.get())
                        , Ingredient.of(CommonTags.TOOLS_KNIFE),
                        Items.LEATHER, 4)
                .addResultWithChance(Items.LEATHER, 0.5F, 2)
                .build(output, "mynethersdelight:cutting/hoglin_hide");
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.BLAZE_ROD),
                        Ingredient.of(CommonTags.TOOLS_KNIFE),
                        Items.BLAZE_POWDER, 3)
                .addResultWithChance(Items.BLAZE_POWDER, 0.25F, 1)
                .build(output, "mynethersdelight:cutting/balze_rod");

        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.HOGLIN_TROPHY.get()),
                        Ingredient.of(CommonTags.TOOLS_KNIFE),
                        MNDItems.SKOGLIN_TROPHY.get())
                .addResult(Items.LEATHER)
                .addResultWithChance(Items.LEATHER, 0.5F, 2)
                .build(output, "mynethersdelight:cutting/skoglin_trophy");
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.ZOGLIN_TROPHY.get()),
                        Ingredient.of(CommonTags.TOOLS_KNIFE),
                        MNDItems.SKOGLIN_TROPHY.get())
                .addResult(Items.ROTTEN_FLESH)
                .addResultWithChance(Items.ROTTEN_FLESH, 0.5F, 2)
                .build(output, "mynethersdelight:cutting/skoglin_trophy_alt");

        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.WAXED_HOGLIN_TROPHY.get()), new ItemAbilityIngredient(ItemAbilities.AXE_WAX_OFF).toVanilla(),
                        MNDItems.HOGLIN_TROPHY.get())
                .build(output, "mynethersdelight:cutting/hoglin_trophy");
    }
    private static void strippingWood(RecipeOutput output) {
        stripLogForBark(output, MNDItems.BLOCK_OF_POWDERY_CANNON.get(), MNDItems.BLOCK_OF_STRIPPED_POWDERY_CANNON.get());
    }

    private static void salvagingWoodenFurniture(RecipeOutput output) {
        salvagePlankFromFurniture(output, MNDItems.POWDERY_PLANKS.get(), MNDItems.POWDERY_DOOR.get(), MNDItems.POWDERY_TRAPDOOR.get(),MNDItems.POWDERY_SIGN.get(), MNDItems.POWDERY_HANGING_SIGN.get());
    }

    private static void cuttingVegetables(RecipeOutput output) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.SUGAR_CANE),
                        Ingredient.of(CommonTags.TOOLS_KNIFE),
                        Items.SUGAR, 1)
                .addResultWithChance(Items.SUGAR, 0.25F, 1)
                .build(output, "farmersdelight:cutting/sugar_cane_alt");
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.CARVED_PUMPKIN),
                        Ingredient.of(CommonTags.TOOLS_KNIFE),
                        ModItems.PUMPKIN_SLICE.get(), 1)
                .addResultWithChance(Items.PUMPKIN_SEEDS, 0.25F, 1)
                .build(output, "farmersdelight:cutting/pumpkin_slice_alt");
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.POWDER_CANNON.get()),
                        Ingredient.of(CommonTags.TOOLS_KNIFE),
                        Items.GUNPOWDER, 1)
                .addResultWithChance(Items.GUNPOWDER, 0.25F, 1)
                .build(output, "mynethersdelight:cutting/gunpowder_cane");
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.BULLET_PEPPER.get()),
                        Ingredient.of(CommonTags.TOOLS_KNIFE),
                        Items.BLAZE_POWDER, 1)
                .addResultWithChance(Items.BLAZE_POWDER, 0.25F, 1)
                .build(output, "mynethersdelight:cutting/bullet_pepper");
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.CRIMSON_FUNGUS_COLONY.get()),
                        Ingredient.of(CommonTags.TOOLS_KNIFE),
                        Items.CRIMSON_FUNGUS, 5)
                .build(output, "mynethersdelight:cutting/crimson_fungus");
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.WARPED_FUNGUS_COLONY.get()),
                        Ingredient.of(CommonTags.TOOLS_KNIFE),
                        Items.WARPED_FUNGUS, 5)
                .build(output, "mynethersdelight:cutting/warped_fungus");
    }

    private static void cuttingFoods(RecipeOutput output) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.MAGMA_CAKE.get()),
                        Ingredient.of(CommonTags.TOOLS_KNIFE),
                        MNDItems.MAGMA_CAKE_SLICE.get(), 7)
                .build(output, "mynethersdelight:cutting/magma_cake");
    }

    private static void stripLogForBark(RecipeOutput output, ItemLike log, ItemLike strippedLog) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(log), new ItemAbilityIngredient(ItemAbilities.AXE_STRIP).toVanilla(), strippedLog)
                .addResult(ModItems.STRAW.get())
                .addSound(SoundEvents.AXE_STRIP).build(output);
    }

    private static void salvagePlankFromFurniture(RecipeOutput output, ItemLike plank, ItemLike door, ItemLike trapdoor, ItemLike sign, ItemLike hangingSign) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(door), new ItemAbilityIngredient(ItemAbilities.AXE_DIG).toVanilla(), plank).build(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(trapdoor), new ItemAbilityIngredient(ItemAbilities.AXE_DIG).toVanilla(), plank).build(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(sign), new ItemAbilityIngredient(ItemAbilities.AXE_DIG).toVanilla(), plank).build(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(hangingSign), new ItemAbilityIngredient(ItemAbilities.AXE_DIG).toVanilla(), plank).build(output);
    }
}
