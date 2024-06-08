//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.core.data.recipes;

import com.soytutta.mynethersdelight.common.registry.MNDItems;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.common.crafting.ingredient.ToolActionIngredient;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ForgeTags;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

import java.util.Objects;
import java.util.function.Consumer;


public class MNDCuttingRecipes {
    public MNDCuttingRecipes() {
    }
    public static void register(Consumer<FinishedRecipe> consumer) {
        cuttingAnimalItems(consumer);
        strippingWood(consumer);
        salvagingWoodenFurniture(consumer);
        cuttingVegetables(consumer);
    }

    private static void cuttingAnimalItems(Consumer<FinishedRecipe> consumer) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.STRIDER_ROCK.get()),
                        Ingredient.of(ForgeTags.TOOLS_PICKAXES),
                        MNDItems.STRIDER_EGG.get())
                .addResultWithChance(Items.BONE_MEAL, 0.25F)
                .build(consumer, "mynethersdelight:cutting/strider_egg");
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.STRIDER_SLICE.get()),
                        Ingredient.of(ForgeTags.TOOLS_KNIVES),
                        MNDItems.MINCED_STRIDER.get(), 2)
                .addResult(Items.STRING)
                .addResultWithChance(Items.STRING, 0.5F, 2)
                .build(consumer, "mynethersdelight:cutting/minced_strider");
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.HOGLIN_LOIN.get()),
                        Ingredient.of(ForgeTags.TOOLS_KNIVES),
                        MNDItems.HOGLIN_SAUSAGE.get(), 2)
                .build(consumer, "mynethersdelight:cutting/hoglin_sausage");
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.HOGLIN_HIDE.get())
                        , Ingredient.of(ForgeTags.TOOLS_KNIVES),
                        Items.LEATHER, 4)
                .addResultWithChance(Items.LEATHER, 0.5F, 2)
                .build(consumer, "mynethersdelight:cutting/hoglin_hide");
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.BLAZE_ROD),
                        Ingredient.of(ForgeTags.TOOLS_KNIVES),
                        Items.BLAZE_POWDER, 3)
                .addResultWithChance(Items.BLAZE_POWDER, 0.25F, 1)
                .build(consumer, "mynethersdelight:cutting/balze_rod");

        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.HOGLIN_TROPHY.get()),
                        Ingredient.of(ForgeTags.TOOLS_KNIVES),
                        MNDItems.SKOGLIN_TROPHY.get())
                .addResult(Items.LEATHER)
                .addResultWithChance(Items.LEATHER, 0.5F, 2)
                .build(consumer, "mynethersdelight:cutting/skoglin_trophy");
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.ZOGLIN_TROPHY.get()),
                        Ingredient.of(ForgeTags.TOOLS_KNIVES),
                        MNDItems.SKOGLIN_TROPHY.get())
                .addResult(Items.ROTTEN_FLESH)
                .addResultWithChance(Items.ROTTEN_FLESH, 0.5F, 2)
                .build(consumer, "mynethersdelight:cutting/skoglin_trophy_alt");

        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.WAXED_HOGLIN_TROPHY.get()),
                        Ingredient.of(ForgeTags.TOOLS_AXES),
                        MNDItems.HOGLIN_TROPHY.get())
                .build(consumer, "mynethersdelight:cutting/hoglin_trophy");
    }
    private static void strippingWood(Consumer<FinishedRecipe> consumer) {
        stripLogForBark(consumer, MNDItems.POWDERY_BLOCK.get(), MNDItems.STRIPPED_POWDERY_BLOCK.get());
    }

    private static void salvagingWoodenFurniture(Consumer<FinishedRecipe> consumer) {
        salvagePlankFromFurniture(consumer, MNDItems.POWDERY_PLANKS.get(), MNDItems.POWDERY_DOOR.get(), MNDItems.POWDERY_TRAPDOOR.get(), MNDItems.POWDERY_SIGN.get());
    }

    private static void cuttingVegetables(Consumer<FinishedRecipe> consumer) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.SUGAR_CANE),
                        Ingredient.of(ForgeTags.TOOLS_KNIVES),
                        Items.SUGAR, 1)
                .addResultWithChance(Items.SUGAR, 0.25F, 1)
                .build(consumer, "mynethersdelight:cutting/sugar_cane");
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.POWDER_CANNON.get()),
                        Ingredient.of(ForgeTags.TOOLS_KNIVES),
                        Items.GUNPOWDER, 1)
                .addResultWithChance(Items.GUNPOWDER, 0.25F, 1)
                .build(consumer, "mynethersdelight:cutting/gunpowder_cane");
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.BULLET_PEPPER.get()),
                        Ingredient.of(ForgeTags.TOOLS_KNIVES),
                        Items.BLAZE_POWDER, 1)
                .addResultWithChance(Items.BLAZE_POWDER, 0.25F, 1)
                .build(consumer, "mynethersdelight:cutting/bullet_pepper");
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.CRIMSON_FUNGUS_COLONY.get()),
                        Ingredient.of(ForgeTags.TOOLS_KNIVES),
                        Items.CRIMSON_FUNGUS, 5)
                .build(consumer, "mynethersdelight:cutting/crimson_fungus");
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.WARPED_FUNGUS_COLONY.get()),
                        Ingredient.of(ForgeTags.TOOLS_KNIVES),
                        Items.WARPED_FUNGUS, 5)
                .build(consumer, "mynethersdelight:cutting/warped_fungus");
        }

    private static void stripLogForBark(Consumer<FinishedRecipe> consumer, ItemLike log, ItemLike strippedLog) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(log), new ToolActionIngredient(ToolActions.AXE_STRIP), strippedLog).addResult(ModItems.STRAW.get()).addSound(Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getKey(SoundEvents.AXE_STRIP)).toString()).build(consumer);
    }

    private static void salvagePlankFromFurniture(Consumer<FinishedRecipe> consumer, ItemLike plank, ItemLike door, ItemLike trapdoor, ItemLike sign) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(door), new ToolActionIngredient(ToolActions.AXE_DIG), plank).build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(trapdoor), new ToolActionIngredient(ToolActions.AXE_DIG), plank).build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(sign), new ToolActionIngredient(ToolActions.AXE_DIG), plank).build(consumer);
    }
}
