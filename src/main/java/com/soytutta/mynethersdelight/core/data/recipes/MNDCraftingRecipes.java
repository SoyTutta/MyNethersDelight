//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.core.data.recipes;

import com.soytutta.mynethersdelight.common.tag.MNDTags;
import com.soytutta.mynethersdelight.common.registry.MNDBlocks;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModRecipeSerializers;
import vectorwing.farmersdelight.common.tag.ForgeTags;

import java.util.function.Consumer;



public class MNDCraftingRecipes {
    public MNDCraftingRecipes() {
    }

    public static void register(Consumer<FinishedRecipe> consumer) {
        recipesVanillaAlternatives(consumer);
        recipesBlocks(consumer);
        recipesCraftedMeals(consumer);
        SpecialRecipeBuilder.special( ModRecipeSerializers.FOOD_SERVING.get()).save(consumer, "food_serving");
    }
    private static void recipesVanillaAlternatives(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,Items.STICK)
                .pattern("#").pattern("#")
                .define('#', (Ingredient.of(Items.BAMBOO,MNDItems.POWDER_CANNON.get())))
                .unlockedBy("has_powder_cannon", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDER_CANNON.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "stick_alt"));
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE,Blocks.TNT)
                .pattern("#s#")
                .pattern("s#s")
                .pattern("#s#")
                .define('#', MNDTags.POWDER_CANNON)
                .define('s', ItemTags.SAND)
                .unlockedBy("has_powder_cannon", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDER_CANNON.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "tnt_alt"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,Items.SCAFFOLDING, 6)
                .pattern("B#b")
                .pattern("B b")
                .pattern("B b")
                .define('b', (Ingredient.of(Items.BAMBOO,MNDItems.POWDER_CANNON.get())))
                .define('B', (Ingredient.of(MNDItems.POWDER_CANNON.get(),Items.BAMBOO)))
                .define('#',(Ingredient.of(ModItems.CANVAS.get(), Items.STRING)))
                .unlockedBy("has_powder_cannon_or_canvas", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDER_CANNON.get(),ModItems.CANVAS.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "scaffolding_alt"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModBlocks.BASKET.get())
                .pattern("B b")
                .pattern("# #")
                .pattern("b#B")
                .define('b', (Ingredient.of(Items.BAMBOO,MNDItems.POWDER_CANNON.get())))
                .define('B', (Ingredient.of(MNDItems.POWDER_CANNON.get(),Items.BAMBOO)))
                .define('#', ModItems.CANVAS.get())
                .unlockedBy("has_powder_cannon_or_canvas", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDER_CANNON.get(),ModItems.CANVAS.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "basket_alt"));
    }

    private static void recipesBlocks(Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,MNDItems.BULLET_PEPPER_CRATE.get(), 1)
                .requires(MNDItems.BULLET_PEPPER.get(),9)
                .unlockedBy("has_pepper", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.BULLET_PEPPER.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "bullet_papper_crate"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.BULLET_PEPPER.get(),9)
                .requires(MNDItems.BULLET_PEPPER_CRATE.get())
                .unlockedBy("has_pepper", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.BULLET_PEPPER.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "bullet_papper_crate_alt"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,MNDBlocks.BLOCK_OF_POWDERY_CANNON.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', MNDTags.POWDER_CANNON)
                .unlockedBy("has_powder_cannon", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDER_CANNON.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "block_of_powdery_cannon"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,MNDBlocks.POWDERY_CABINET.get())
                .pattern("#X#")
                .pattern("# #")
                .pattern("#X#")
                .define('#', MNDItems.POWDERY_PLANKS.get()).define('X', MNDItems.POWDERY_PLANKS_SLAB.get())
                .unlockedBy("has_powdery_planks", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_PLANKS.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "block_of_powdery_cabinet"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,MNDItems.POWDERY_PLANKS.get(),4)
                .requires(MNDTags.BLOCK_OF_POWDERY)
                .group("planks")
                .unlockedBy("has_powder_cannon", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDER_CANNON.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "powdery_plank"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,MNDBlocks.POWDERY_PLANKS_SLAB.get(),6)
                .pattern("###")
                .define('#', MNDItems.POWDERY_PLANKS.get())
                .group("wooden_slab")
                .unlockedBy("has_powdery_planks", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_PLANKS.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "powdery_slab"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,MNDBlocks.POWDERY_PLANKS_STAIRS.get(),4)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .define('#', MNDItems.POWDERY_PLANKS.get())
                .group("wooden_stairs")
                .unlockedBy("has_powdery_planks", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_PLANKS.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "powdery_stairs"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,MNDBlocks.POWDERY_MOSAIC.get())
                .pattern("#")
                .pattern("#")
                .define('#', MNDItems.POWDERY_PLANKS_SLAB.get())
                .unlockedBy("has_powdery_mosaic", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_MOSAIC.get(),MNDItems.POWDERY_PLANKS.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "powdery_mosaic"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,MNDBlocks.POWDERY_MOSAIC_SLAB.get(),6)
                .pattern("###")
                .define('#', MNDItems.POWDERY_MOSAIC.get())
                .group("wooden_slab")
                .unlockedBy("has_powdery_mosaic", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_MOSAIC.get(),MNDItems.POWDERY_PLANKS.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "powdery_mosaic_slab"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,MNDBlocks.POWDERY_MOSAIC_STAIRS.get(),4)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .define('#', MNDItems.POWDERY_MOSAIC.get())
                .group("wooden_stairs")
                .unlockedBy("has_powdery_mosaic", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_MOSAIC.get(),MNDItems.POWDERY_PLANKS.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "powdery_mosaic_stairs"));
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE,MNDBlocks.POWDERY_DOOR.get(),3)
                .pattern("##")
                .pattern("##")
                .pattern("##")
                .define('#', MNDItems.POWDERY_PLANKS.get())
                .group("wooden_door")
                .unlockedBy("has_powdery_planks", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_PLANKS.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "powdery_door"));
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE,MNDBlocks.POWDERY_TRAPDOOR.get(),2)
                .pattern("###")
                .pattern("###")
                .define('#', MNDItems.POWDERY_PLANKS.get())
                .group("wooden_trapdoor")
                .unlockedBy("has_powdery_planks", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_PLANKS.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "powdery_trapdoor"));
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE,MNDBlocks.POWDERY_PRESSURE_PLATE.get())
                .pattern("##")
                .define('#', MNDItems.POWDERY_PLANKS.get())
                .group("wooden_pressure_plate")
                .unlockedBy("has_powdery_planks", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_PLANKS.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "powdery_pressure_plate"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE,MNDItems.POWDERY_BUTTON.get())
                .requires(MNDItems.POWDERY_PLANKS.get())
                .group("wooden_button")
                .unlockedBy("has_powdery_planks", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_PLANKS.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "powdery_button"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,MNDBlocks.POWDERY_SIGN.get(),3)
                .pattern("###")
                .pattern("###")
                .pattern(" X ")
                .define('#', MNDItems.POWDERY_PLANKS.get()).define('X', Items.STICK)
                .group("wooden_sign")
                .unlockedBy("has_powdery_planks", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_PLANKS.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "powdery_sign"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,MNDBlocks.POWDERY_HANGING_SIGN.get(),6)
                .pattern("X X")
                .pattern("###")
                .pattern("###")
                .define('#', MNDItems.BLOCK_OF_STRIPPED_POWDERY_CANNON.get()).define('X', Items.CHAIN)
                .group("wooden_hanging_sign")
                .unlockedBy("has_powdery_planks", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.BLOCK_OF_STRIPPED_POWDERY_CANNON.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "powdery_hanging_sign"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,MNDBlocks.POWDERY_FENCE.get(),3)
                .pattern("#X#")
                .pattern("#X#")
                .define('#', MNDItems.POWDERY_PLANKS.get()).define('X', Items.STICK)
                .group("wooden_fence")
                .unlockedBy("has_powdery_planks", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_PLANKS.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "powdery_fence"));
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE,MNDBlocks.POWDERY_FENCE_GATE.get())
                .pattern("X#X")
                .pattern("X#X")
                .define('#', MNDItems.POWDERY_PLANKS.get()).define('X', Items.STICK)
                .group("wooden_fence_gate")
                .unlockedBy("has_powdery_planks", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_PLANKS.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "powdery_fence_gate"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,MNDBlocks.POWDERY_TORCH.get(),2)
                .pattern("P").pattern("#")
                .define('#', MNDTags.POWDER_CANNON).define('P', MNDTags.BULLET_PEPPER)
                .unlockedBy("has_powder_cannon", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDER_CANNON.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "powdery_torch"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,MNDBlocks.HOGLIN_TROPHY.get())
                .pattern("W#W")
                .pattern("bBb")
                .pattern("WGW")
                .define('#', MNDTags.HOGLIN_HIDE).define('G', Items.GOLD_INGOT)
                .define('B', Blocks.BONE_BLOCK).define('b', Items.BONE)
                .define('W', ItemTags.PLANKS)
                .group("nether_trophy")
                .unlockedBy("has_hoglin_hide", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.HOGLIN_HIDE.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "hoglin_trophy"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,MNDItems.WAXED_HOGLIN_TROPHY.get())
                .requires(MNDItems.HOGLIN_TROPHY.get())
                .requires(Ingredient.of(MNDTags.HOGLIN_WAXED))
                .group("nether_trophy")
                .unlockedBy("has_hoglin_trophy", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.HOGLIN_TROPHY.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "waxed_hoglin_trophy"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,MNDItems.HOGLIN_TROPHY.get())
                .requires(MNDItems.ZOGLIN_TROPHY.get())
                .requires(Ingredient.of(MNDTags.HOGLIN_CURE))
                .group("nether_trophy")
                .unlockedBy("has_zoglin_trophy", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.ZOGLIN_TROPHY.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "hoglin_trophy_cure"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,MNDBlocks.ZOGLIN_TROPHY.get())
                .pattern("wZw")
                .pattern("Z#Z")
                .pattern("wZw")
                .define('#', MNDItems.HOGLIN_TROPHY.get()).define('Z', Items.ROTTEN_FLESH).define('w', Items.WARPED_FUNGUS)
                .group("nether_trophy")
                .unlockedBy("has_hoglin_trophy", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.HOGLIN_TROPHY.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "zoglin_trophy"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,MNDItems.HOGLIN_TROPHY.get())
                .requires(MNDItems.SKOGLIN_TROPHY.get())
                .requires(Ingredient.of(MNDTags.HOGLIN_HIDE))
                .group("nether_trophy")
                .unlockedBy("has_skoglin_trophy", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.SKOGLIN_TROPHY.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "skoglin_trophy"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.NETHER_STOVE.get())
                .requires(MNDItems.SOUL_NETHER_STOVE.get())
                .requires(Ingredient.of(MNDTags.STOVE_FIRE_FUEL))
                .group("nether_stove")
                .unlockedBy("has_soul_stove", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.SOUL_NETHER_STOVE.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "nethers_stove_alt0"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,MNDItems.NETHER_STOVE.get())
                .pattern("iii")
                .pattern("B B")
                .pattern("BCB")
                .define('i', Tags.Items.INGOTS_NETHER_BRICK)
                .define('B', Blocks.POLISHED_BLACKSTONE_BRICKS).define('C', Blocks.CAMPFIRE)
                .group("nether_stove")
                .unlockedBy("has_campfire", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.CAMPFIRE))
                .save(consumer, new ResourceLocation("mynethersdelight", "nethers_stove"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.SOUL_NETHER_STOVE.get())
                .requires(MNDItems.NETHER_STOVE.get())
                .requires(Ingredient.of(MNDTags.STOVE_SOUL_FUEL))
                .group("soul_nether_stove")
                .unlockedBy("has_stove", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.NETHER_STOVE.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "soul_nethers_stove_alt0"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,MNDItems.SOUL_NETHER_STOVE.get())
                .pattern("iii")
                .pattern("B B")
                .pattern("BCB")
                .define('i', Tags.Items.INGOTS_NETHER_BRICK)
                .define('B', Blocks.POLISHED_BLACKSTONE_BRICKS).define('C', Blocks.SOUL_CAMPFIRE)
                .group("soul_nether_stove")
                .unlockedBy("has_soul_campfire", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.SOUL_CAMPFIRE))
                .save(consumer, new ResourceLocation("mynethersdelight", "soul_nethers_stove"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,MNDItems.LETIOS_COMPOST.get(), 1)
                .requires(Ingredient.of(Items.SOUL_SAND, Items.SOUL_SOIL))
                .requires(Items.ROTTEN_FLESH,2)
                .requires(Ingredient.of(Items.WARPED_ROOTS, Items.CRIMSON_ROOTS))
                .requires(Ingredient.of(Items.CRIMSON_ROOTS, Items.WARPED_ROOTS, ModItems.STRAW.get()))
                .requires(Items.BONE_MEAL, 4)
                .unlockedBy("has_rotten_flesh", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ROTTEN_FLESH))
                .unlockedBy("has_roots", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CRIMSON_ROOTS,Items.WARPED_ROOTS))
                .save(consumer, new ResourceLocation("mynethersdelight", "letios_compost_from_rotten_flesh"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,MNDItems.LETIOS_COMPOST.get(), 1)
                .requires(Ingredient.of(Items.SOUL_SAND, Items.SOUL_SOIL))
                .requires(Items.BONE_MEAL,2)
                .requires(Ingredient.of(Items.WARPED_ROOTS, Items.CRIMSON_ROOTS))
                .requires(Ingredient.of(Items.CRIMSON_ROOTS, Items.WARPED_ROOTS, ModItems.STRAW.get()))
                .requires(Items.ROTTEN_FLESH, 4)
                .unlockedBy("has_bone_meal", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BONE_MEAL))
                .unlockedBy("has_roots", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CRIMSON_ROOTS,Items.WARPED_ROOTS))
                .save(consumer, new ResourceLocation("mynethersdelight", "letios_compost_from_bone_alt"));
    }
    private static void recipesCraftedMeals(Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.BLEEDING_TARTAR.get())
                .requires(MNDTags.MINCED_STRIDER).requires(MNDTags.MINCED_STRIDER)
                .requires(ForgeTags.EGGS)
                .requires(Items.BOWL)
                .unlockedBy("has_minced_strider", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.MINCED_STRIDER.get()))
                .save(consumer, "mynethersdelight:crafting/bleeding_tartar");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.HOTDOG.get())
                .requires(MNDItems.ROASTED_SAUSAGE.get())
                .requires(ForgeTags.BREAD)
                .unlockedBy("has_sausage", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.HOGLIN_SAUSAGE.get()))
                .save(consumer, "mynethersdelight:crafting/hotdog");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.STRIDERLOAF_BLOCK.get())
                .requires(MNDTags.STRIDER_SLICE)
                .requires(MNDTags.MINCED_STRIDER).requires(MNDTags.MINCED_STRIDER).requires(MNDTags.MINCED_STRIDER)
                .requires(Items.BOWL)
                .unlockedBy("has_minced_strider", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.MINCED_STRIDER.get()))
                .save(consumer, "mynethersdelight:crafting/striderloaf");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.BLUE_TENDERLOIN_STEAK.get())
                .requires(Blocks.WARPED_FUNGUS)
                .requires(Ingredient.of(Items.WARPED_FUNGUS, Items.WARPED_ROOTS, ModItems.STRAW.get()))
                .requires(MNDTags.COOKED_HOGLIN_LOIN)
                .requires(Items.BOWL)
                .unlockedBy("has_hoglin_loin", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.HOGLIN_LOIN.get()))
                .group("blue_tenderloin_steak_group")
                .save(consumer, "mynethersdelight:crafting/blue_tenderloin_steak");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.BREAKFAST_SAMPLER.get())
                .requires(MNDItems.ROASTED_SAUSAGE.get(),2)
                .requires(Ingredient.of(Items.HONEY_BOTTLE, MNDItems.STRIDER_EGG.get()))
                .requires(ForgeTags.COOKED_EGGS)
                .requires(ForgeTags.COOKED_EGGS)
                .requires(ForgeTags.BREAD)
                .requires(Items.BOWL)
                .unlockedBy("has_sausage", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.ROASTED_SAUSAGE.get()))
                .save(consumer, "mynethersdelight:crafting/breakfast_sampler");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.HOT_CREAM_CONE.get(), 3)
                .requires(MNDItems.HOT_CREAM.get())
                .requires(MNDTags.POWDER_CANNON)
                .requires(MNDTags.POWDER_CANNON)
                .requires(MNDTags.POWDER_CANNON)
                .unlockedBy("has_powder_cannon", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDER_CANNON.get(),MNDItems.HOT_CREAM.get(),MNDItems.HOT_CREAM_CONE.get()))
                .save(consumer, "mynethersdelight:crafting/hotcream_cone");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.SPICY_COTTON.get())
                .requires(MNDItems.GHASTA.get())
                .requires(MNDTags.HOT_SPICE)
                .requires(Items.BLAZE_ROD)
                .requires(MNDItems.GHASTA.get())
                .unlockedBy("has_blaze_rod", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BLAZE_ROD,MNDItems.GHASTA.get()))
                .save(consumer, "mynethersdelight:crafting/spicy_cotton");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,MNDItems.GHASTA_WITH_CREAM_BLOCK.get())
                .pattern("GGG")
                .pattern("GT#")
                .pattern("GBG")
                .define('G', MNDTags.GHAST_MEATS).define('T', Items.GHAST_TEAR)
                .define('#', Items.MAGMA_CREAM).define('B', Items.BOWL)
                .unlockedBy("has_ghast", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.GHASMATI.get(),MNDItems.GHASTA.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "ghasta_with_cream"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.GHAST_DOUGH.get(),2)
                .requires(ForgeTags.EGGS)
                .requires(MNDItems.GHASMATI.get(),2)
                .requires(ForgeTags.EGGS)
                .unlockedBy("has_ghast", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.GHASMATI.get(),MNDItems.GHASTA.get()))
                .save(consumer, "mynethersdelight:crafting/ghast_dough");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.GHAST_SALAD.get())
                .requires(MNDTags.GHAST_MEATS)
                .requires(ForgeTags.VEGETABLES)
                .requires(ForgeTags.VEGETABLES_CARROT)
                .requires(Items.BOWL)
                .unlockedBy("has_ghast", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.GHASMATI.get(),MNDItems.GHASTA.get()))
                .save(consumer, "mynethersdelight:crafting/ghast_salad");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.SPICY_SKEWER.get())
                .requires(MNDTags.STRIDER_MEATS)
                .requires(MNDTags.BULLET_PEPPER)
                .requires(Items.BLAZE_ROD)
                .requires(MNDTags.BULLET_PEPPER)
                .unlockedBy("has_blaze_rod", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BLAZE_ROD))
                .save(consumer, "mynethersdelight:crafting/spicy_skewer");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,MNDItems.RAW_STUFFED_HOGLIN.get())
                .pattern("hwh")
                .pattern("l#l")
                .pattern("hch")
                .define('c', MNDTags.CRIMSON_COLONY)
                .define('l', MNDTags.RAW_HOGLIN_LOIN)
                .define('#', MNDTags.HOGLIN_HIDE)
                .define('h', ModItems.HAM.get())
                .define('w', MNDTags.WARPED_COLONY)
                .unlockedBy("has_hoglin_hide", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.HOGLIN_HIDE.get()))
                .save(consumer, "mynethersdelight:crafting/raw_stuffed_hoglin");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.HOT_WINGS_BUCKET.get())
                .requires(MNDItems.HOT_WINGS.get(), 3)
                .requires(Items.BUCKET)
                .unlockedBy("has_hot_wings", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.HOT_WINGS_BUCKET.get()))
                .save(consumer, "mynethersdelight:crafting/hot_wings_bucket");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.BURNT_ROLL.get())
                .requires(Items.MAGMA_CREAM)
                .requires(MNDTags.CURRY_MEATS)
                .unlockedBy("has_magma_cream", InventoryChangeTrigger.TriggerInstance.hasItems(Items.MAGMA_CREAM))
                .save(consumer, "mynethersdelight:crafting/burnt_roll");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.ROCK_SOUP.get())
                .requires(Items.MAGMA_CREAM,2)
                .requires(MNDItems.STRIDER_EGG.get(),2)
                .requires(Items.BOWL)
                .unlockedBy("has_magma_cream", InventoryChangeTrigger.TriggerInstance.hasItems(Items.MAGMA_CREAM))
                .save(consumer, "mynethersdelight:crafting/rock_soup");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,MNDItems.MAGMA_CAKE.get())
                .pattern("MMM")
                .pattern("PHP")
                .pattern("###")
                .define('H', MNDItems.HOT_CREAM.get()).define('M', Items.MAGMA_CREAM)
                .define('#', ModItems.STRAW.get()).define('P', Items.GUNPOWDER)
                .unlockedBy("has_magma_cream", InventoryChangeTrigger.TriggerInstance.hasItems(Items.MAGMA_CREAM))
                .save(consumer, new ResourceLocation("mynethersdelight", "magma_cake"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.MAGMA_CAKE.get())
                .requires(MNDItems.MAGMA_CAKE_SLICE.get(),7)
                .unlockedBy("has_magma_cream", InventoryChangeTrigger.TriggerInstance.hasItems(Items.MAGMA_CREAM))
                .save(consumer, new ResourceLocation("mynethersdelight", "magma_cake_alt"));
    }
}
