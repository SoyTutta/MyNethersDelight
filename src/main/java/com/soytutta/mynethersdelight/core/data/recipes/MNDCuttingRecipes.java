//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.core.data.recipes;

import com.soytutta.mynethersdelight.MyNethersDelight;
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
import vectorwing.farmersdelight.common.tag.CommonTags;
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
        cuttingFoods(consumer);
    }

    private static void cuttingAnimalItems(Consumer<FinishedRecipe> consumer) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.GHASTA.get()),
                        Ingredient.of(CommonTags.Items.TOOLS_KNIVES),
                        MNDItems.GHASMATI.get())
                .addResultWithChance(MNDItems.GHASMATI.get(), 0.05F)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.STRIDER_ROCK.get()),
                        Ingredient.of(CommonTags.Items.TOOLS_PICKAXES),
                        MNDItems.STRIDER_EGG.get())
                .addResultWithChance(Items.BONE_MEAL, 0.25F)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.STRIDER_SLICE.get()),
                        Ingredient.of(CommonTags.Items.TOOLS_KNIVES),
                        MNDItems.MINCED_STRIDER.get(), 2)
                .addResultWithChance(Items.STRING, 0.5F, 2)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.HOGLIN_LOIN.get()),
                        Ingredient.of(CommonTags.Items.TOOLS_KNIVES),
                        MNDItems.HOGLIN_SAUSAGE.get(), 2)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.HOGLIN_HIDE.get())
                        , Ingredient.of(CommonTags.Items.TOOLS_KNIVES),
                        Items.LEATHER, 4)
                .addResultWithChance(Items.LEATHER, 0.5F, 2)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.BLAZE_ROD),
                        Ingredient.of(CommonTags.Items.TOOLS_KNIVES),
                        Items.BLAZE_POWDER, 3)
                .addResultWithChance(Items.BLAZE_POWDER, 0.25F, 1)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);

        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.HOGLIN_TROPHY.get()),
                        Ingredient.of(CommonTags.Items.TOOLS_KNIVES),
                        MNDItems.SKOGLIN_TROPHY.get())
                .addResult(Items.LEATHER, 2)
                .addResultWithChance(Items.LEATHER, 0.5F)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.ZOGLIN_TROPHY.get()),
                        Ingredient.of(CommonTags.Items.TOOLS_KNIVES),
                        MNDItems.SKOGLIN_TROPHY.get())
                .addResult(Items.ROTTEN_FLESH, 2)
                .addResultWithChance(Items.ROTTEN_FLESH, 0.5F)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);

        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.WAXED_HOGLIN_TROPHY.get()),
                        Ingredient.of(CommonTags.Items.TOOLS_AXES),
                        MNDItems.HOGLIN_TROPHY.get())
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);
    }
    private static void strippingWood(Consumer<FinishedRecipe> consumer) {
        stripLogForBark(consumer, MNDItems.BLOCK_OF_POWDERY_CANNON.get(), MNDItems.BLOCK_OF_STRIPPED_POWDERY_CANNON.get());
    }

    private static void salvagingWoodenFurniture(Consumer<FinishedRecipe> consumer) {
        salvagePlankFromFurniture(consumer, MNDItems.POWDERY_PLANKS.get(), MNDItems.POWDERY_DOOR.get(),
                MNDItems.POWDERY_TRAPDOOR.get(), MNDItems.POWDERY_SIGN.get(), MNDItems.POWDERY_HANGING_SIGN.get(),
                MNDItems.POWDERY_FENCE.get(), MNDItems.POWDERY_FENCE_GATE.get(), MNDItems.POWDERY_PRESSURE_PLATE.get(),
                MNDItems.POWDERY_BUTTON.get(), MNDItems.POWDERY_CABINET.get());
    }

    private static void cuttingVegetables(Consumer<FinishedRecipe> consumer) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.CARVED_PUMPKIN),
                        Ingredient.of(CommonTags.Items.TOOLS_KNIVES),
                        ModItems.PUMPKIN_SLICE.get(), 1)
                .addResultWithChance(Items.PUMPKIN_SEEDS, 0.25F, 1)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.POWDER_CANNON.get()),
                        Ingredient.of(CommonTags.Items.TOOLS_KNIVES),
                        Items.STICK, 1)
                .addResultWithChance(Items.GUNPOWDER, 0.25F, 1)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.BAMBOO),
                        Ingredient.of(CommonTags.Items.TOOLS_KNIVES),
                        Items.STICK, 1)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.BULLET_PEPPER.get()),
                        Ingredient.of(CommonTags.Items.TOOLS_KNIVES),
                        MNDItems.PEPPER_POWDER.get(), 1)
                .addResultWithChance(MNDItems.PEPPER_POWDER.get(), 0.25F, 1)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.CRIMSON_FUNGUS_COLONY.get()),
                        Ingredient.of(CommonTags.Items.TOOLS_KNIVES),
                        Items.CRIMSON_FUNGUS, 5)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.WARPED_FUNGUS_COLONY.get()),
                        Ingredient.of(CommonTags.Items.TOOLS_KNIVES),
                        Items.WARPED_FUNGUS, 5)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);
    }

    private static void cuttingFoods(Consumer<FinishedRecipe> consumer) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.MAGMA_CAKE.get()),
                        Ingredient.of(CommonTags.Items.TOOLS_KNIVES),
                        MNDItems.MAGMA_CAKE_SLICE.get(), 7)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.BREAD_LOAF_BLOCK.get()),
                        Ingredient.of(CommonTags.Items.TOOLS_KNIVES),
                        MNDItems.SLICES_OF_BREAD.get(), 5)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MNDItems.GHAST_SOURDOUGH.get()),
                        Ingredient.of(CommonTags.Items.TOOLS_KNIVES),
                        MNDItems.GHAST_DOUGH.get(), 3)
                .setNamespace(MyNethersDelight.MODID)
                .save(consumer);
    }

    private static void stripLogForBark(Consumer<FinishedRecipe> consumer, ItemLike log, ItemLike strippedLog) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(log), new ToolActionIngredient(ToolActions.AXE_STRIP), strippedLog).addResult(ModItems.STRAW.get()).addSound(Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getKey(SoundEvents.AXE_STRIP)).toString()).setNamespace(MyNethersDelight.MODID)
                .save(consumer);
    }

    private static void salvagePlankFromFurniture(Consumer<FinishedRecipe> consumer, ItemLike plank, ItemLike... furniture) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(furniture), new ToolActionIngredient(ToolActions.AXE_DIG), plank, 1, 0.75F)
                .salvaging()
                .save(consumer, net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(MyNethersDelight.MODID, "salvaging/powdery_furniture"));
    }
}
