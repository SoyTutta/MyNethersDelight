//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.core.data.recipes;

import com.soytutta.mynethersdelight.common.tag.MNDTags;
import com.soytutta.mynethersdelight.common.registry.MNDBlocks;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import com.soytutta.mynethersdelight.common.tag.MyCommonTags;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.DifferenceIngredient;
import vectorwing.farmersdelight.common.crafting.FoodServingRecipe;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.CommonTags;

public class MNDCraftingRecipes {

    public static void register(RecipeOutput output) {
        recipesVanillaAlternatives(output);
        recipesBlocks(output);
        recipesCraftedMeals(output);
        SpecialRecipeBuilder.special(FoodServingRecipe::new).save(output, "food_serving");
    }

    private static void recipesVanillaAlternatives(RecipeOutput output) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,Items.SUGAR)
                .requires(MNDItems.STRIDER_EGG.get())
                .unlockedBy("has_sugar", InventoryChangeTrigger.TriggerInstance.hasItems(Items.SUGAR,MNDItems.STRIDER_EGG.get()))
                .save(output, "mynethersdelight:crafting/sugar_alt");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,Items.STICK)
                .pattern("#").pattern("#")
                .define('#', (Ingredient.of(Items.BAMBOO,MNDItems.POWDER_CANNON.get())))
                .unlockedBy("has_powder_cannon", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDER_CANNON.get()))
                .save(output, "mynethersdelight:crafting/stick_alt");
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE,Blocks.TNT)
                .pattern("#s#")
                .pattern("s#s")
                .pattern("#s#")
                .define('#', MNDItems.POWDER_CANNON.get())
                .define('s', ItemTags.SAND)
                .unlockedBy("has_powder_cannon", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDER_CANNON.get()))
                .save(output, "mynethersdelight:crafting/tnt_alt");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,Items.SCAFFOLDING, 6)
                .pattern("B#b")
                .pattern("B b")
                .pattern("B b")
                .define('b', (Ingredient.of(Items.BAMBOO,MNDItems.POWDER_CANNON.get())))
                .define('B', (Ingredient.of(MNDItems.POWDER_CANNON.get(),Items.BAMBOO)))
                .define('#',(Ingredient.of(ModItems.CANVAS.get(), Items.STRING)))
                .unlockedBy("has_powder_cannon_or_canvas", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDER_CANNON.get(),ModItems.CANVAS.get()))
                .save(output, "mynethersdelight:crafting/scaffolding_alt");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModBlocks.BASKET.get())
                .pattern("B b")
                .pattern("# #")
                .pattern("b#B")
                .define('b', (Ingredient.of(Items.BAMBOO,MNDItems.POWDER_CANNON.get())))
                .define('B', (Ingredient.of(MNDItems.POWDER_CANNON.get(),Items.BAMBOO)))
                .define('#', ModItems.CANVAS.get())
                .unlockedBy("has_powder_cannon_or_canvas", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDER_CANNON.get(),ModItems.CANVAS.get()))
                .save(output, "mynethersdelight:crafting/basket_alt");
    }

    private static void recipesBlocks(RecipeOutput output) {
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(Items.NETHER_BRICKS), RecipeCategory.BUILDING_BLOCKS, MNDItems.NETHER_BRICKS_CABINET.get())
                .unlockedBy("has_nether_bricks", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHER_BRICKS))
                .save(output,"mynethersdelight:stonecutting/nether_bricks_cabinet");
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(Items.RED_NETHER_BRICKS), RecipeCategory.BUILDING_BLOCKS, MNDItems.RED_NETHER_BRICKS_CABINET.get())
                .unlockedBy("has_nether_bricks", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHER_BRICKS))
                .save(output,"mynethersdelight:stonecutting/red_nether_bricks_cabinet");
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(Items.POLISHED_BLACKSTONE_BRICKS,Items.POLISHED_BLACKSTONE, Items.BLACKSTONE), RecipeCategory.BUILDING_BLOCKS, MNDItems.BLACKSTONE_BRICKS_CABINET.get())
                .unlockedBy("has_blackstone", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BLACKSTONE,Items.POLISHED_BLACKSTONE,Items.POLISHED_BLACKSTONE_BRICKS))
                .save(output,"mynethersdelight:stonecutting/blackstone_bricks_cabinet");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,MNDItems.BULLET_PEPPER_CRATE.get(), 1)
                .requires(MNDItems.BULLET_PEPPER.get(),9)
                .unlockedBy("has_pepper", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.BULLET_PEPPER.get()))
                .save(output, "mynethersdelight:crafting/bullet_papper_crate");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.BULLET_PEPPER.get(),9)
                .requires(MNDItems.BULLET_PEPPER_CRATE.get())
                .unlockedBy("has_pepper", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.BULLET_PEPPER.get()))
                .save(output, "mynethersdelight:crafting/bullet_papper_crate_alt");

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,MNDBlocks.BLOCK_OF_POWDERY_CANNON.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', MNDItems.POWDER_CANNON.get())
                .unlockedBy("has_powder_cannon", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDER_CANNON.get()))
                .save(output, "mynethersdelight:crafting/block_of_powdery_cannon");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,MNDBlocks.POWDERY_CABINET.get())
                .pattern("#X#")
                .pattern("# #")
                .pattern("#X#")
                .define('#', MNDItems.POWDERY_PLANKS.get()).define('X', MNDItems.POWDERY_PLANKS_SLAB.get())
                .unlockedBy("has_powdery_planks", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_PLANKS.get()))
                .save(output, "mynethersdelight:crafting/block_of_powdery_cabinet");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,MNDItems.POWDERY_PLANKS.get(),4)
                .requires(MNDTags.BLOCK_OF_POWDERY)
                .group("planks")
                .unlockedBy("has_powder_cannon", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDER_CANNON.get()))
                .save(output, "mynethersdelight:crafting/powdery_plank");
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,MNDBlocks.POWDERY_PLANKS_SLAB.get(),6)
                .pattern("###")
                .define('#', MNDItems.POWDERY_PLANKS.get())
                .group("wooden_slab")
                .unlockedBy("has_powdery_planks", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_PLANKS.get()))
                .save(output, "mynethersdelight:crafting/powdery_slab");
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,MNDBlocks.POWDERY_PLANKS_STAIRS.get(),4)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .define('#', MNDItems.POWDERY_PLANKS.get())
                .group("wooden_stairs")
                .unlockedBy("has_powdery_planks", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_PLANKS.get()))
                .save(output, "mynethersdelight:crafting/powdery_stairs");
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,MNDBlocks.POWDERY_MOSAIC.get())
                .pattern("#")
                .pattern("#")
                .define('#', MNDItems.POWDERY_PLANKS_SLAB.get())
                .unlockedBy("has_powdery_mosaic", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_MOSAIC.get(),MNDItems.POWDERY_PLANKS.get()))
                .save(output, "mynethersdelight:crafting/powdery_mosaic");
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,MNDBlocks.POWDERY_MOSAIC_SLAB.get(),6)
                .pattern("###")
                .define('#', MNDItems.POWDERY_MOSAIC.get())
                .group("wooden_slab")
                .unlockedBy("has_powdery_mosaic", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_MOSAIC.get(),MNDItems.POWDERY_PLANKS.get()))
                .save(output, "mynethersdelight:crafting/powdery_mosaic_slab");
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,MNDBlocks.POWDERY_MOSAIC_STAIRS.get(),4)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .define('#', MNDItems.POWDERY_MOSAIC.get())
                .group("wooden_stairs")
                .unlockedBy("has_powdery_mosaic", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_MOSAIC.get(),MNDItems.POWDERY_PLANKS.get()))
                .save(output, "mynethersdelight:crafting/powdery_mosaic_stairs");
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE,MNDBlocks.POWDERY_DOOR.get(),3)
                .pattern("##")
                .pattern("##")
                .pattern("##")
                .define('#', MNDItems.POWDERY_PLANKS.get())
                .group("wooden_door")
                .unlockedBy("has_powdery_planks", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_PLANKS.get()))
                .save(output, "mynethersdelight:crafting/powdery_door");
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE,MNDBlocks.POWDERY_TRAPDOOR.get(),2)
                .pattern("###")
                .pattern("###")
                .define('#', MNDItems.POWDERY_PLANKS.get())
                .group("wooden_trapdoor")
                .unlockedBy("has_powdery_planks", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_PLANKS.get()))
                .save(output, "mynethersdelight:crafting/powdery_trapdoor");
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE,MNDBlocks.POWDERY_PRESSURE_PLATE.get())
                .pattern("##")
                .define('#', MNDItems.POWDERY_PLANKS.get())
                .group("wooden_pressure_plate")
                .unlockedBy("has_powdery_planks", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_PLANKS.get()))
                .save(output, "mynethersdelight:crafting/powdery_pressure_plate");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE,MNDItems.POWDERY_BUTTON.get())
                .requires(MNDItems.POWDERY_PLANKS.get())
                .group("wooden_button")
                .unlockedBy("has_powdery_planks", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_PLANKS.get()))
                .save(output, "mynethersdelight:crafting/powdery_button");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,MNDBlocks.POWDERY_SIGN.get(),3)
                .pattern("###")
                .pattern("###")
                .pattern(" X ")
                .define('#', MNDItems.POWDERY_PLANKS.get()).define('X', Items.STICK)
                .group("wooden_sign")
                .unlockedBy("has_powdery_planks", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_PLANKS.get()))
                .save(output, "mynethersdelight:crafting/powdery_sign");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,MNDBlocks.POWDERY_HANGING_SIGN.get(),6)
                .pattern("X X")
                .pattern("###")
                .pattern("###")
                .define('#', MNDItems.BLOCK_OF_STRIPPED_POWDERY_CANNON.get()).define('X', Items.CHAIN)
                .group("wooden_hanging_sign")
                .unlockedBy("has_powdery_planks", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.BLOCK_OF_STRIPPED_POWDERY_CANNON.get()))
                .save(output, "mynethersdelight:crafting/powdery_hanging_sign");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,MNDBlocks.POWDERY_FENCE.get(),3)
                .pattern("#X#")
                .pattern("#X#")
                .define('#', MNDItems.POWDERY_PLANKS.get()).define('X', Items.STICK)
                .group("wooden_fence")
                .unlockedBy("has_powdery_planks", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_PLANKS.get()))
                .save(output, "mynethersdelight:crafting/powdery_fence");
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE,MNDBlocks.POWDERY_FENCE_GATE.get())
                .pattern("X#X")
                .pattern("X#X")
                .define('#', MNDItems.POWDERY_PLANKS.get()).define('X', Items.STICK)
                .group("wooden_fence_gate")
                .unlockedBy("has_powdery_planks", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_PLANKS.get()))
                .save(output, "mynethersdelight:crafting/powdery_fence_gate");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,MNDBlocks.POWDERY_TORCH.get(),2)
                .pattern("P").pattern("#")
                .define('#', MNDItems.POWDER_CANNON.get()).define('P', MNDItems.BULLET_PEPPER.get())
                .unlockedBy("has_powder_cannon", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDER_CANNON.get()))
                .save(output, "mynethersdelight:crafting/powdery_torch");
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,MNDBlocks.HOGLIN_TROPHY.get())
                .pattern("W#W")
                .pattern("bBb")
                .pattern("WGW")
                .define('#', MNDItems.HOGLIN_HIDE.get()).define('G', Items.GOLD_INGOT)
                .define('B', Blocks.BONE_BLOCK).define('b', Items.BONE)
                .define('W', ItemTags.PLANKS)
                .group("nether_trophy")
                .unlockedBy("has_hoglin_hide", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.HOGLIN_HIDE.get()))
                .save(output, "mynethersdelight:crafting/hoglin_trophy");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,MNDItems.WAXED_HOGLIN_TROPHY.get())
                .requires(MNDItems.HOGLIN_TROPHY.get())
                .requires(Ingredient.of(MNDTags.HOGLIN_WAXED))
                .group("nether_trophy")
                .unlockedBy("has_hoglin_trophy", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.HOGLIN_TROPHY.get()))
                .save(output, "mynethersdelight:crafting/waxed_hoglin_trophy");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,MNDItems.HOGLIN_TROPHY.get())
                .requires(MNDItems.ZOGLIN_TROPHY.get())
                .requires(Ingredient.of(MNDTags.HOGLIN_CURE))
                .group("nether_trophy")
                .unlockedBy("has_zoglin_trophy", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.ZOGLIN_TROPHY.get()))
                .save(output, "mynethersdelight:crafting/hoglin_trophy_cure");
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,MNDBlocks.ZOGLIN_TROPHY.get())
                .pattern("wZw")
                .pattern("Z#Z")
                .pattern("wZw")
                .define('#', MNDItems.HOGLIN_TROPHY.get()).define('Z', Items.ROTTEN_FLESH).define('w', Items.WARPED_FUNGUS)
                .group("nether_trophy")
                .unlockedBy("has_hoglin_trophy", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.HOGLIN_TROPHY.get()))
                .save(output, "mynethersdelight:crafting/zoglin_trophy");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,MNDItems.HOGLIN_TROPHY.get())
                .requires(MNDItems.SKOGLIN_TROPHY.get())
                .requires(Ingredient.of(MNDItems.HOGLIN_HIDE.get()))
                .group("nether_trophy")
                .unlockedBy("has_skoglin_trophy", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.SKOGLIN_TROPHY.get()))
                .save(output, "mynethersdelight:crafting/skoglin_trophy");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.NETHER_STOVE.get())
                .requires(MNDItems.SOUL_NETHER_STOVE.get())
                .requires(Ingredient.of(MNDTags.STOVE_FIRE_FUEL))
                .group("nether_stove")
                .unlockedBy("has_soul_stove", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.SOUL_NETHER_STOVE.get()))
                .save(output, "mynethersdelight:crafting/nethers_stove_alt0");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,MNDItems.NETHER_STOVE.get())
                .pattern("iii")
                .pattern("B B")
                .pattern("BCB")
                .define('i', Tags.Items.BRICKS_NETHER)
                .define('B', Blocks.POLISHED_BLACKSTONE_BRICKS).define('C', Blocks.CAMPFIRE)
                .group("nether_stove")
                .unlockedBy("has_campfire", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.CAMPFIRE))
                .save(output, "mynethersdelight:crafting/nethers_stove");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.SOUL_NETHER_STOVE.get())
                .requires(MNDItems.NETHER_STOVE.get())
                .requires(Ingredient.of(MNDTags.STOVE_SOUL_FUEL))
                .group("soul_nether_stove")
                .unlockedBy("has_stove", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.NETHER_STOVE.get()))
                .save(output, "mynethersdelight:crafting/soul_nethers_stove_alt0");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,MNDItems.SOUL_NETHER_STOVE.get())
                .pattern("iii")
                .pattern("B B")
                .pattern("BCB")
                .define('i', Tags.Items.BRICKS_NETHER)
                .define('B', Blocks.POLISHED_BLACKSTONE_BRICKS).define('C', Blocks.SOUL_CAMPFIRE)
                .group("soul_nether_stove")
                .unlockedBy("has_soul_campfire", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.SOUL_CAMPFIRE))
                .save(output, "mynethersdelight:crafting/soul_nethers_stove");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,MNDItems.LETIOS_COMPOST.get(), 1)
                .requires(Ingredient.of(Items.SOUL_SAND, Items.SOUL_SOIL))
                .requires(Items.ROTTEN_FLESH,2)
                .requires(Ingredient.of(Items.WARPED_ROOTS, Items.CRIMSON_ROOTS))
                .requires(Ingredient.of(Items.CRIMSON_ROOTS, Items.WARPED_ROOTS, ModItems.STRAW.get()))
                .requires(Items.BONE_MEAL, 4)
                .unlockedBy("has_rotten_flesh", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ROTTEN_FLESH))
                .unlockedBy("has_roots", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CRIMSON_ROOTS,Items.WARPED_ROOTS))
                .save(output, "mynethersdelight:crafting/letios_compost_from_rotten_flesh");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,MNDItems.LETIOS_COMPOST.get(), 1)
                .requires(Ingredient.of(Items.SOUL_SAND, Items.SOUL_SOIL))
                .requires(Items.BONE_MEAL,2)
                .requires(Ingredient.of(Items.WARPED_ROOTS, Items.CRIMSON_ROOTS))
                .requires(Ingredient.of(Items.CRIMSON_ROOTS, Items.WARPED_ROOTS, ModItems.STRAW.get()))
                .requires(Items.ROTTEN_FLESH, 4)
                .unlockedBy("has_bone_meal", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BONE_MEAL))
                .unlockedBy("has_roots", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CRIMSON_ROOTS,Items.WARPED_ROOTS))
                .save(output, "mynethersdelight:crafting/letios_compost_from_bone_alt");
    }
    private static void recipesCraftedMeals(RecipeOutput output) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.BLEEDING_TARTAR.get())
                .requires(MNDItems.MINCED_STRIDER.get(),2)
                .requires(Tags.Items.EGGS)
                .requires(Items.BOWL)
                .unlockedBy("has_minced_strider", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.MINCED_STRIDER.get()))
                .save(output, "mynethersdelight:crafting/bleeding_tartar");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.HOTDOG.get())
                .requires(MNDItems.ROASTED_SAUSAGE.get())
                .requires(Tags.Items.FOODS_BREAD)
                .unlockedBy("has_sausage", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.HOGLIN_SAUSAGE.get()))
                .save(output, "mynethersdelight:crafting/hotdog");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.STRIDERLOAF_BLOCK.get())
                .requires(MNDItems.STRIDER_SLICE.get())
                .requires(MNDItems.MINCED_STRIDER.get(),3)
                .requires(Items.BOWL)
                .unlockedBy("has_minced_strider", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.MINCED_STRIDER.get()))
                .save(output, "mynethersdelight:crafting/striderloaf");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.BLUE_TENDERLOIN_STEAK.get())
                .requires(MNDItems.COOKED_LOIN.get())
                .requires(Blocks.WARPED_FUNGUS)
                .requires(Ingredient.of(Items.WARPED_FUNGUS, Items.WARPED_ROOTS, ModItems.STRAW.get()))
                .requires(Items.BOWL)
                .unlockedBy("has_hoglin_loin", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.HOGLIN_LOIN.get()))
                .group("blue_tenderloin_steak_group")
                .save(output, "mynethersdelight:crafting/blue_tenderloin_steak");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.RED_LOIN_STICK.get())
                .requires(MNDItems.COOKED_LOIN.get())
                .requires(Blocks.CRIMSON_FUNGUS)
                .requires(Ingredient.of(Items.CRIMSON_FUNGUS, Items.RED_MUSHROOM))
                .requires(Items.STICK)
                .unlockedBy("has_hoglin_loin", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.HOGLIN_LOIN.get()))
                .save(output, "mynethersdelight:crafting/red_loin_on_a_stick");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.BREAKFAST_SAMPLER.get())
                .requires(MNDItems.ROASTED_SAUSAGE.get(),2)
                .requires(Ingredient.of(Items.HONEY_BOTTLE, MNDItems.STRIDER_EGG.get()))
                .requires(CommonTags.FOODS_COOKED_EGG)
                .requires(CommonTags.FOODS_COOKED_EGG)
                .requires(Tags.Items.FOODS_BREAD)
                .requires(Items.BOWL)
                .unlockedBy("has_sausage", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.ROASTED_SAUSAGE.get()))
                .save(output, "mynethersdelight:crafting/breakfast_sampler");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.HOT_CREAM_CONE.get(), 3)
                .requires(MNDItems.HOT_CREAM.get())
                .requires(MNDItems.POWDER_CANNON.get())
                .requires(MNDItems.POWDER_CANNON.get())
                .requires(MNDItems.POWDER_CANNON.get())
                .unlockedBy("has_powder_cannon", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDER_CANNON.get(),MNDItems.HOT_CREAM.get(),MNDItems.HOT_CREAM_CONE.get()))
                .save(output, "mynethersdelight:crafting/hotcream_cone");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.HOT_CREAM.get(), 1)
                .requires(Items.BUCKET)
                .requires(MNDItems.HOT_CREAM_CONE.get())
                .requires(MNDItems.HOT_CREAM_CONE.get())
                .requires(MNDItems.HOT_CREAM_CONE.get())
                .unlockedBy("has_powder_cannon", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDER_CANNON.get(),MNDItems.HOT_CREAM.get(),MNDItems.HOT_CREAM_CONE.get()))
                .save(output, "mynethersdelight:crafting/hotcream_bucket");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.SPICY_COTTON.get())
                .requires(MNDItems.GHASTA.get())
                .requires(MNDTags.HOT_SPICE)
                .requires(Items.BLAZE_ROD)
                .requires(MNDItems.GHASTA.get())
                .unlockedBy("has_blaze_rod", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BLAZE_ROD,MNDItems.GHASTA.get()))
                .save(output, "mynethersdelight:crafting/spicy_cotton");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,MNDItems.GHASTA_WITH_CREAM_BLOCK.get())
                .pattern("GGG")
                .pattern("GT#")
                .pattern("GBG")
                .define('G', Ingredient.of(MNDItems.GHASTA.get(), MNDItems.GHASMATI.get())).define('T', Items.GHAST_TEAR)
                .define('#', Items.MAGMA_CREAM).define('B', Items.BOWL)
                .unlockedBy("has_ghast", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.GHASMATI.get(),MNDItems.GHASTA.get()))
                .save(output, "mynethersdelight:crafting/ghasta_with_cream");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.GHAST_DOUGH.get(),2)
                .requires(Tags.Items.EGGS)
                .requires(MNDItems.GHASMATI.get(),2)
                .requires(Tags.Items.EGGS)
                .unlockedBy("has_ghast", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.GHASMATI.get(),MNDItems.GHASTA.get()))
                .save(output, "mynethersdelight:crafting/ghast_dough");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.GHAST_SALAD.get())
                .requires(MyCommonTags.FOODS_RAW_GHAST)
                .requires(vegetablesPatch())
                .requires(Tags.Items.CROPS_CARROT)
                .requires(Items.BOWL)
                .unlockedBy("has_ghast", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.GHASMATI.get(),MNDItems.GHASTA.get()))
                .save(output, "mynethersdelight:crafting/ghast_salad");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.SPICY_SKEWER.get())
                .requires(MNDItems.BULLET_PEPPER.get())
                .requires(MyCommonTags.FOODS_RAW_STRIDER)
                .requires(Items.BLAZE_ROD)
                .requires(MNDItems.BULLET_PEPPER.get())
                .unlockedBy("has_blaze_rod", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BLAZE_ROD))
                .save(output, "mynethersdelight:crafting/spicy_skewer");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,MNDItems.RAW_STUFFED_HOGLIN.get())
                .pattern("hwh")
                .pattern("l#l")
                .pattern("hch")
                .define('c', MNDItems.CRIMSON_FUNGUS_COLONY.get())
                .define('l', MNDItems.HOGLIN_LOIN.get())
                .define('#', MNDItems.HOGLIN_HIDE.get())
                .define('h', ModItems.HAM.get())
                .define('w', MNDItems.WARPED_FUNGUS_COLONY.get())
                .unlockedBy("has_hoglin_hide", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.HOGLIN_HIDE.get()))
                .save(output, "mynethersdelight:crafting/raw_stuffed_hoglin");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.HOT_WINGS_BUCKET.get())
                .requires(MNDItems.HOT_WINGS.get(), 3)
                .requires(Items.BUCKET)
                .unlockedBy("has_hot_wings", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.HOT_WINGS_BUCKET.get(), MNDItems.HOT_WINGS.get()))
                .save(output, "mynethersdelight:crafting/hot_wings_bucket");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.HOT_WINGS.get(), 3)
                .requires(MNDItems.HOT_WINGS_BUCKET.get())
                .requires(Items.BOWL, 3)
                .unlockedBy("has_hot_wings", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.HOT_WINGS_BUCKET.get(), MNDItems.HOT_WINGS.get()))
                .save(output, "mynethersdelight:crafting/hot_wings_bucket_alt");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.BURNT_ROLL.get())
                .requires(Items.MAGMA_CREAM)
                .requires(Tags.Items.FOODS_RAW_MEAT)
                .unlockedBy("has_magma_cream", InventoryChangeTrigger.TriggerInstance.hasItems(Items.MAGMA_CREAM))
                .save(output, "mynethersdelight:crafting/burnt_roll");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.ROCK_SOUP.get())
                .requires(Items.MAGMA_CREAM,2)
                .requires(MNDItems.STRIDER_EGG.get(),2)
                .requires(Items.BOWL)
                .unlockedBy("has_magma_cream", InventoryChangeTrigger.TriggerInstance.hasItems(Items.MAGMA_CREAM))
                .save(output, "mynethersdelight:crafting/rock_soup");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,MNDItems.MAGMA_CAKE.get())
                .pattern("MMM")
                .pattern("PHP")
                .pattern("###")
                .define('H', MNDItems.HOT_CREAM.get()).define('M', Items.MAGMA_CREAM)
                .define('#', ModItems.STRAW.get()).define('P', Items.GUNPOWDER)
                .unlockedBy("has_magma_cream", InventoryChangeTrigger.TriggerInstance.hasItems(Items.MAGMA_CREAM))
                .save(output, "mynethersdelight:crafting/magma_cake");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.MAGMA_CAKE.get())
                .requires(MNDItems.MAGMA_CAKE_SLICE.get(),7)
                .unlockedBy("has_magma_cream", InventoryChangeTrigger.TriggerInstance.hasItems(Items.MAGMA_CREAM))
                .save(output, "mynethersdelight:crafting/magma_cake_alt");
    }

    private static Ingredient vegetablesPatch() {
        return DifferenceIngredient.of(Ingredient.of(Tags.Items.FOODS_VEGETABLE), Ingredient.of(new ItemLike[]{net.minecraft.world.item.Items.MELON_SLICE}));
    }
}
