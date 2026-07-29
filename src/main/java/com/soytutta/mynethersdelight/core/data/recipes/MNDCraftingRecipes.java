//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.core.data.recipes;

import com.soytutta.mynethersdelight.common.crafting.condition.ConfigEnabledCondition;
import com.soytutta.mynethersdelight.common.tag.MNDTags;
import com.soytutta.mynethersdelight.common.registry.MNDBlocks;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import com.soytutta.mynethersdelight.common.registry.MNDRecipeSerializers;
import com.soytutta.mynethersdelight.common.tag.MyCommonTags;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModRecipeSerializers;
import vectorwing.farmersdelight.common.tag.CommonTags;

import java.util.function.Consumer;



public class MNDCraftingRecipes {
    public MNDCraftingRecipes() {
    }

    public static void register(Consumer<FinishedRecipe> consumer) {
        recipesVanillaAlternatives(consumer);
        recipesBlocks(consumer);
        recipesCraftedMeals(consumer);
        SpecialRecipeBuilder.special( ModRecipeSerializers.FOOD_SERVING.get()).save(consumer, "food_serving");
        conditionalSpecialRecipe(consumer, MNDRecipeSerializers.BLAZIER_HEATING.get(),
                "crafting/blazier_heating", ConfigEnabledCondition.Setting.BLAZIER);
        conditionalSpecialRecipe(consumer, MNDRecipeSerializers.BLAZIER_COOLING.get(),
                "crafting/blazier_cooling", ConfigEnabledCondition.Setting.BLAZIER);
    }
    private static void recipesVanillaAlternatives(Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,Items.SUGAR)
                .requires(MNDItems.STRIDER_EGG.get())
                .unlockedBy("has_sugar", InventoryChangeTrigger.TriggerInstance.hasItems(Items.SUGAR,MNDItems.STRIDER_EGG.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/sugar_alt"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BLAZE_POWDER, 2)
                .requires(Items.BLAZE_POWDER)
                .requires(MNDItems.PEPPER_POWDER.get(), 3)
                .unlockedBy("has_blaze_powder", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BLAZE_POWDER))
                .save(consumer, ResourceLocation.parse("mynethersdelight:crafting/blaze_powder_alt"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,Items.STICK)
                .pattern("#").pattern("#")
                .define('#', (Ingredient.of(Items.BAMBOO,MNDItems.POWDER_CANNON.get())))
                .unlockedBy("has_powder_cannon", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDER_CANNON.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/stick_alt"));
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE,Blocks.TNT)
                .pattern("#s#")
                .pattern("s#s")
                .pattern("#s#")
                .define('#', MNDTags.POWDER_CANNON)
                .define('s', ItemTags.SAND)
                .unlockedBy("has_powder_cannon", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDER_CANNON.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/tnt_alt"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,Items.SCAFFOLDING, 6)
                .pattern("B#b")
                .pattern("B b")
                .pattern("B b")
                .define('b', (Ingredient.of(Items.BAMBOO,MNDItems.POWDER_CANNON.get())))
                .define('B', (Ingredient.of(MNDItems.POWDER_CANNON.get(),Items.BAMBOO)))
                .define('#',(Ingredient.of(ModItems.CANVAS.get(), Items.STRING)))
                .unlockedBy("has_powder_cannon_or_canvas", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDER_CANNON.get(),ModItems.CANVAS.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/scaffolding_alt"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModBlocks.BAMBOO_BASKET.get())
                .pattern("B b")
                .pattern("# #")
                .pattern("b#B")
                .define('b', (Ingredient.of(Items.BAMBOO,MNDItems.POWDER_CANNON.get())))
                .define('B', (Ingredient.of(MNDItems.POWDER_CANNON.get(),Items.BAMBOO)))
                .define('#', ModItems.CANVAS.get())
                .unlockedBy("has_powder_cannon_or_canvas", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDER_CANNON.get(),ModItems.CANVAS.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/basket_alt"));
    }

    private static void recipesBlocks(Consumer<FinishedRecipe> consumer) {
        ConditionalRecipe.builder()
                .addCondition(new ConfigEnabledCondition(ConfigEnabledCondition.Setting.BLAZIER))
                .addRecipe(nested -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MNDItems.BLAZIER.get())
                        .pattern("###")
                        .pattern("###")
                        .pattern("#b#")
                        .define('#', Items.BLAZE_ROD)
                        .define('b', Items.NETHER_BRICK)
                        .unlockedBy("has_blaze_rod", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BLAZE_ROD))
                        .save(nested))
                .generateAdvancement()
                .build(consumer, ResourceLocation.parse("mynethersdelight:crafting/blazefire"));
        conditionalStonecutting(consumer, Ingredient.of(Items.NETHER_BRICKS), MNDItems.NETHER_BRICKS_CABINET.get(),
                "nether_bricks_cabinet", "has_nether_bricks", Items.NETHER_BRICKS);
        conditionalStonecutting(consumer, Ingredient.of(Items.RED_NETHER_BRICKS), MNDItems.RED_NETHER_BRICKS_CABINET.get(),
                "red_nether_bricks_cabinet", "has_red_nether_bricks", Items.RED_NETHER_BRICKS);
        conditionalStonecutting(consumer, Ingredient.of(Items.POLISHED_BLACKSTONE_BRICKS, Items.POLISHED_BLACKSTONE, Items.BLACKSTONE),
                MNDItems.BLACKSTONE_BRICKS_CABINET.get(), "blackstone_bricks_cabinet", "has_blackstone", Items.BLACKSTONE);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,MNDItems.BULLET_PEPPER_CRATE.get(), 1)
                .requires(MNDItems.BULLET_PEPPER.get(),9)
                .unlockedBy("has_pepper", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.BULLET_PEPPER.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/bullet_papper_crate"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.BULLET_PEPPER.get(),9)
                .requires(MNDItems.BULLET_PEPPER_CRATE.get())
                .unlockedBy("has_pepper", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.BULLET_PEPPER.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/bullet_papper_crate_alt"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,MNDBlocks.BLOCK_OF_POWDERY_CANNON.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', MNDTags.POWDER_CANNON)
                .unlockedBy("has_powder_cannon", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDER_CANNON.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/block_of_powdery_cannon"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,MNDBlocks.POWDERY_CABINET.get())
                .pattern("XXX")
                .pattern("T T")
                .pattern("XXX")
                .define('X', MNDItems.POWDERY_PLANKS_SLAB.get()).define('T', MNDItems.POWDERY_TRAPDOOR.get())
                .unlockedBy("has_powdery_planks", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_PLANKS.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/block_of_powdery_cabinet"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,MNDItems.POWDERY_PLANKS.get(),2)
                .requires(MNDTags.BLOCK_OF_POWDERY)
                .group("planks")
                .unlockedBy("has_powder_cannon", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDER_CANNON.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/powdery_plank"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,MNDBlocks.POWDERY_PLANKS_SLAB.get(),6)
                .pattern("###")
                .define('#', MNDItems.POWDERY_PLANKS.get())
                .group("wooden_slab")
                .unlockedBy("has_powdery_planks", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_PLANKS.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/powdery_slab"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,MNDBlocks.POWDERY_PLANKS_STAIRS.get(),4)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .define('#', MNDItems.POWDERY_PLANKS.get())
                .group("wooden_stairs")
                .unlockedBy("has_powdery_planks", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_PLANKS.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/powdery_stairs"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,MNDBlocks.POWDERY_MOSAIC.get())
                .pattern("#")
                .pattern("#")
                .define('#', MNDItems.POWDERY_PLANKS_SLAB.get())
                .unlockedBy("has_powdery_mosaic", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_MOSAIC.get(),MNDItems.POWDERY_PLANKS.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/powdery_mosaic"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,MNDBlocks.POWDERY_MOSAIC_SLAB.get(),6)
                .pattern("###")
                .define('#', MNDItems.POWDERY_MOSAIC.get())
                .group("wooden_slab")
                .unlockedBy("has_powdery_mosaic", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_MOSAIC.get(),MNDItems.POWDERY_PLANKS.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/powdery_mosaic_slab"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,MNDBlocks.POWDERY_MOSAIC_STAIRS.get(),4)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .define('#', MNDItems.POWDERY_MOSAIC.get())
                .group("wooden_stairs")
                .unlockedBy("has_powdery_mosaic", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_MOSAIC.get(),MNDItems.POWDERY_PLANKS.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/powdery_mosaic_stairs"));
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE,MNDBlocks.POWDERY_DOOR.get(),3)
                .pattern("##")
                .pattern("##")
                .pattern("##")
                .define('#', MNDItems.POWDERY_PLANKS.get())
                .group("wooden_door")
                .unlockedBy("has_powdery_planks", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_PLANKS.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/powdery_door"));
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE,MNDBlocks.POWDERY_TRAPDOOR.get(),2)
                .pattern("###")
                .pattern("###")
                .define('#', MNDItems.POWDERY_PLANKS.get())
                .group("wooden_trapdoor")
                .unlockedBy("has_powdery_planks", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_PLANKS.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/powdery_trapdoor"));
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE,MNDBlocks.POWDERY_PRESSURE_PLATE.get())
                .pattern("##")
                .define('#', MNDItems.POWDERY_PLANKS.get())
                .group("wooden_pressure_plate")
                .unlockedBy("has_powdery_planks", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_PLANKS.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/powdery_pressure_plate"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE,MNDItems.POWDERY_BUTTON.get())
                .requires(MNDItems.POWDERY_PLANKS.get())
                .group("wooden_button")
                .unlockedBy("has_powdery_planks", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_PLANKS.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/powdery_button"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,MNDBlocks.POWDERY_SIGN.get(),3)
                .pattern("###")
                .pattern("###")
                .pattern(" X ")
                .define('#', MNDItems.POWDERY_PLANKS.get()).define('X', Items.STICK)
                .group("wooden_sign")
                .unlockedBy("has_powdery_planks", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_PLANKS.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/powdery_sign"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,MNDBlocks.POWDERY_HANGING_SIGN.get(),6)
                .pattern("X X")
                .pattern("###")
                .pattern("###")
                .define('#', MNDItems.BLOCK_OF_STRIPPED_POWDERY_CANNON.get()).define('X', Items.CHAIN)
                .group("wooden_hanging_sign")
                .unlockedBy("has_powdery_planks", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.BLOCK_OF_STRIPPED_POWDERY_CANNON.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/powdery_hanging_sign"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,MNDBlocks.POWDERY_FENCE.get(),3)
                .pattern("#X#")
                .pattern("#X#")
                .define('#', MNDItems.POWDERY_PLANKS.get()).define('X', Items.STICK)
                .group("wooden_fence")
                .unlockedBy("has_powdery_planks", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_PLANKS.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/powdery_fence"));
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE,MNDBlocks.POWDERY_FENCE_GATE.get())
                .pattern("X#X")
                .pattern("X#X")
                .define('#', MNDItems.POWDERY_PLANKS.get()).define('X', Items.STICK)
                .group("wooden_fence_gate")
                .unlockedBy("has_powdery_planks", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDERY_PLANKS.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/powdery_fence_gate"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,MNDBlocks.POWDERY_TORCH.get(),2)
                .pattern("P").pattern("#")
                .define('#', MNDTags.POWDER_CANNON).define('P', MNDItems.BULLET_PEPPER.get())
                .unlockedBy("has_powder_cannon", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDER_CANNON.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/powdery_torch"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,MNDBlocks.HOGLIN_TROPHY.get())
                .pattern("W#W")
                .pattern("bBb")
                .pattern("WGW")
                .define('#', MNDItems.HOGLIN_HIDE.get()).define('G', Items.GOLD_INGOT)
                .define('B', Blocks.BONE_BLOCK).define('b', Items.BONE)
                .define('W', ItemTags.PLANKS)
                .group("nether_trophy")
                .unlockedBy("has_hoglin_hide", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.HOGLIN_HIDE.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/hoglin_trophy"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, MNDBlocks.GOLDEN_TROPHY.get(), 2)
                .pattern("iGi")
                .pattern("iBi")
                .pattern("iSi")
                .define('G', MNDItems.GOLDEN_TROPHY.get())
                .define('S', MNDItems.SKOGLIN_TROPHY.get())
                .define('B', Blocks.GOLD_BLOCK)
                .define('i', Items.GOLD_INGOT)
                .group("nether_trophy")
                .unlockedBy("has_golden_trophy", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.GOLDEN_TROPHY.get()))
                .save(consumer, ResourceLocation.parse("mynethersdelight:crafting/golden_trophy"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,MNDItems.WAXED_HOGLIN_TROPHY.get())
                .requires(MNDItems.HOGLIN_TROPHY.get())
                .requires(Ingredient.of(MNDTags.HOGLIN_WAXED))
                .group("nether_trophy")
                .unlockedBy("has_hoglin_trophy", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.HOGLIN_TROPHY.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/waxed_hoglin_trophy"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,MNDItems.HOGLIN_TROPHY.get())
                .requires(MNDItems.ZOGLIN_TROPHY.get())
                .requires(Ingredient.of(MNDTags.HOGLIN_CURE))
                .group("nether_trophy")
                .unlockedBy("has_zoglin_trophy", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.ZOGLIN_TROPHY.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/hoglin_trophy_cure"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,MNDBlocks.ZOGLIN_TROPHY.get())
                .pattern("wZw")
                .pattern("Z#Z")
                .pattern("wZw")
                .define('#', MNDItems.HOGLIN_TROPHY.get()).define('Z', Items.ROTTEN_FLESH).define('w', Items.WARPED_FUNGUS)
                .group("nether_trophy")
                .unlockedBy("has_hoglin_trophy", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.HOGLIN_TROPHY.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/zoglin_trophy"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,MNDItems.HOGLIN_TROPHY.get())
                .requires(MNDItems.SKOGLIN_TROPHY.get())
                .requires(Ingredient.of(MNDItems.HOGLIN_HIDE.get()))
                .group("nether_trophy")
                .unlockedBy("has_skoglin_trophy", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.SKOGLIN_TROPHY.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/skoglin_trophy"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.NETHER_STOVE.get())
                .requires(MNDItems.SOUL_NETHER_STOVE.get())
                .requires(Ingredient.of(MNDTags.STOVE_FIRE_FUEL))
                .group("nether_stove")
                .unlockedBy("has_soul_stove", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.SOUL_NETHER_STOVE.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/nethers_stove_alt0"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,MNDItems.NETHER_STOVE.get())
                .pattern("iii")
                .pattern("B B")
                .pattern("BCB")
                .define('i', Tags.Items.INGOTS_NETHER_BRICK)
                .define('B', Blocks.POLISHED_BLACKSTONE_BRICKS).define('C', Blocks.CAMPFIRE)
                .group("nether_stove")
                .unlockedBy("has_campfire", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.CAMPFIRE))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/nethers_stove"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.SOUL_NETHER_STOVE.get())
                .requires(MNDItems.NETHER_STOVE.get())
                .requires(Ingredient.of(MNDTags.STOVE_SOUL_FUEL))
                .group("soul_nether_stove")
                .unlockedBy("has_stove", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.NETHER_STOVE.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/soul_nethers_stove_alt0"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,MNDItems.SOUL_NETHER_STOVE.get())
                .pattern("iii")
                .pattern("B B")
                .pattern("BCB")
                .define('i', Tags.Items.INGOTS_NETHER_BRICK)
                .define('B', Blocks.POLISHED_BLACKSTONE_BRICKS).define('C', Blocks.SOUL_CAMPFIRE)
                .group("soul_nether_stove")
                .unlockedBy("has_soul_campfire", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.SOUL_CAMPFIRE))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/soul_nethers_stove"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,MNDItems.LETIOS_COMPOST.get(), 1)
                .requires(Ingredient.of(Items.SOUL_SAND, Items.SOUL_SOIL))
                .requires(Items.ROTTEN_FLESH,2)
                .requires(Ingredient.of(Items.WARPED_ROOTS, Items.CRIMSON_ROOTS))
                .requires(Ingredient.of(Items.CRIMSON_ROOTS, Items.WARPED_ROOTS, ModItems.STRAW.get()))
                .requires(Items.BONE_MEAL, 4)
                .unlockedBy("has_rotten_flesh", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ROTTEN_FLESH))
                .unlockedBy("has_roots", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CRIMSON_ROOTS,Items.WARPED_ROOTS))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/letios_compost_from_rotten_flesh"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,MNDItems.LETIOS_COMPOST.get(), 1)
                .requires(Ingredient.of(Items.SOUL_SAND, Items.SOUL_SOIL))
                .requires(Items.BONE_MEAL,2)
                .requires(Ingredient.of(Items.WARPED_ROOTS, Items.CRIMSON_ROOTS))
                .requires(Ingredient.of(Items.CRIMSON_ROOTS, Items.WARPED_ROOTS, ModItems.STRAW.get()))
                .requires(Items.ROTTEN_FLESH, 4)
                .unlockedBy("has_bone_meal", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BONE_MEAL))
                .unlockedBy("has_roots", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CRIMSON_ROOTS,Items.WARPED_ROOTS))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/letios_compost_from_bone_alt"));
    }
    private static void recipesCraftedMeals(Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.BLEEDING_TARTAR.get())
                .requires(MNDItems.MINCED_STRIDER.get(),2)
                .requires(Tags.Items.EGGS)
                .requires(Items.BOWL)
                .unlockedBy("has_minced_strider", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.MINCED_STRIDER.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/bleeding_tartar"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.HOTDOG.get())
                .requires(MNDItems.ROASTED_SAUSAGE.get())
                .requires(CommonTags.Items.BREAD)
                .unlockedBy("has_sausage", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.HOGLIN_SAUSAGE.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/hotdog"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.HOTDOG_WITH_MIXED_SALAD.get(),2)
                .requires(ModItems.MIXED_SALAD.get())
                .requires(MNDItems.HOTDOG.get(),2)
                .unlockedBy("has_hotdog", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.HOTDOG.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/hotdog_with_mixed_salad"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.HOTDOG_WITH_NETHER_SALAD.get(),2)
                .requires(ModItems.NETHER_SALAD.get())
                .requires(MNDItems.HOTDOG.get(),2)
                .unlockedBy("has_hotdog", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.HOTDOG.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/hotdog_with_nether_salad"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.STRIDERLOAF_BLOCK.get())
                .requires(MNDItems.STRIDER_SLICE.get())
                .requires(MNDItems.MINCED_STRIDER.get(),3)
                .requires(Items.BOWL)
                .unlockedBy("has_minced_strider", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.MINCED_STRIDER.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/striderloaf"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.BLUE_TENDERLOIN_STEAK.get())
                .requires(MNDTags.COOKED_HOGLIN_LOIN)
                .requires(Blocks.WARPED_FUNGUS)
                .requires(Ingredient.of(Items.WARPED_FUNGUS, Items.WARPED_ROOTS, ModItems.STRAW.get()))
                .requires(Items.BOWL)
                .unlockedBy("has_hoglin_loin", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.HOGLIN_LOIN.get()))
                .group("blue_tenderloin_steak_group")
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/blue_tenderloin_steak"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.NETHER_BURGER.get())
                .requires(CommonTags.Items.BREAD)
                .requires(MNDTags.COOKED_HOGLIN_LOIN)
                .requires(Items.TWISTING_VINES)
                .requires(Items.CRIMSON_FUNGUS)
                .requires(Ingredient.of(Items.WARPED_FUNGUS, Items.CRIMSON_FUNGUS))
                .unlockedBy("has_hoglin_loin", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.HOGLIN_LOIN.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/nether_burger"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.RED_LOIN_STICK.get())
                .requires(MNDTags.COOKED_HOGLIN_LOIN)
                .requires(Blocks.CRIMSON_FUNGUS)
                .requires(Ingredient.of(Items.CRIMSON_FUNGUS, Items.RED_MUSHROOM))
                .requires(Items.STICK)
                .unlockedBy("has_hoglin_loin", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.HOGLIN_LOIN.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/red_loin_on_a_stick"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.BACON_WRAPPED_SAUSAGE_STICK.get())
                .requires(MNDItems.ROASTED_SAUSAGE.get())
                .requires(ModItems.COOKED_BACON.get())
                .requires(Items.STICK)
                .unlockedBy("has_sausage", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.HOGLIN_SAUSAGE.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/bacon_wrapped_sausage_stick"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.BREAKFAST_SAMPLER.get())
                .requires(MNDItems.ROASTED_SAUSAGE.get(),2)
                .requires(Ingredient.of(Items.HONEY_BOTTLE, MNDItems.STRIDER_EGG.get()))
                .requires(CommonTags.Items.COOKED_EGGS)
                .requires(CommonTags.Items.COOKED_EGGS)
                .requires(CommonTags.Items.BREAD)
                .requires(Items.BOWL)
                .unlockedBy("has_sausage", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.ROASTED_SAUSAGE.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/breakfast_sampler"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,MNDItems.GOLDEN_EGG.get())
                .pattern("###")
                .pattern("#E#")
                .pattern("###")
                .define('E', MyCommonTags.FOODS_BOILED_EGG)
                .define('#', Items.GOLD_INGOT)
                .unlockedBy("has_gold_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLD_INGOT))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/golden_egg"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.STUFFED_PEPPER.get())
                .requires(MNDItems.BULLET_PEPPER.get())
                .requires(CommonTags.Items.COOKED_PORK)
                .requires(CommonTags.Items.MILK)
                .unlockedBy("has_pepper", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.BULLET_PEPPER.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/stuffed_pepper"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.HOT_CREAM_CONE.get(), 3)
                .requires(MNDItems.HOT_CREAM.get())
                .requires(MNDTags.POWDER_CANNON)
                .requires(MNDTags.POWDER_CANNON)
                .requires(MNDTags.POWDER_CANNON)
                .unlockedBy("has_powder_cannon", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDER_CANNON.get(),MNDItems.HOT_CREAM.get(),MNDItems.HOT_CREAM_CONE.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/hotcream_cone"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.HOT_CREAM.get(), 1)
                .requires(Items.BUCKET)
                .requires(MNDItems.HOT_CREAM_CONE.get())
                .requires(MNDItems.HOT_CREAM_CONE.get())
                .requires(MNDItems.HOT_CREAM_CONE.get())
                .unlockedBy("has_powder_cannon", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDER_CANNON.get(),MNDItems.HOT_CREAM.get(),MNDItems.HOT_CREAM_CONE.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/hotcream_bucket"));

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, MNDItems.TEAR_POPSICLE.get(), 1)
                .pattern(" ii")
                .pattern("i#i")
                .pattern("-i ")
                .define('#', Items.GHAST_TEAR)
                .define('i', Items.ICE)
                .define('-', Items.STICK)
                .unlockedBy("has_ghast", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GHAST_TEAR))
                .save(consumer, ResourceLocation.parse("mynethersdelight:crafting/tear_popsicle"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.DRIED_GHAST_WITH_MILK.get())
                .requires(MNDItems.GHASMATI.get())
                .requires(CommonTags.Items.MILK)
                .requires(Items.BOWL)
                .unlockedBy("has_ghasmati", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.GHASMATI.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/dried_ghast_with_milk"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.SIZZLING_PUDDING.get())
                .requires(MNDItems.GHASMATI.get())
                .requires(CommonTags.Items.MILK)
                .requires(Tags.Items.EGGS)
                .requires(Ingredient.of(Items.BLAZE_POWDER, MNDItems.PEPPER_POWDER.get()))
                .requires(Items.BOWL)
                .unlockedBy("has_ghasmati", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.GHASMATI.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/sizzling_pudding"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.SPICY_COTTON.get())
                .requires(MNDItems.GHASTA.get())
                .requires(MNDTags.HOT_SPICE)
                .requires(Items.BLAZE_ROD)
                .requires(MNDItems.GHASTA.get())
                .unlockedBy("has_blaze_rod", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BLAZE_ROD,MNDItems.GHASTA.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/spicy_cotton"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,MNDItems.GHASTA_WITH_CREAM_BLOCK.get())
                .pattern("GGG")
                .pattern("GT#")
                .pattern("GBG")
                .define('G', Ingredient.of(MNDItems.GHASTA.get(), MNDItems.GHASMATI.get())).define('T', Items.GHAST_TEAR)
                .define('#', Items.MAGMA_CREAM).define('B', Items.BOWL)
                .unlockedBy("has_ghast", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.GHASMATI.get(),MNDItems.GHASTA.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/ghasta_with_cream"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.GHAST_DOUGH.get(),2)
                .requires(Tags.Items.EGGS)
                .requires(MNDItems.GHASMATI.get(),2)
                .requires(Tags.Items.EGGS)
                .unlockedBy("has_ghast", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.GHASMATI.get(),MNDItems.GHASTA.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/ghast_dough"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.GHAST_SOURDOUGH.get())
                .requires(MNDItems.GHAST_DOUGH.get())
                .requires(CommonTags.Items.DOUGH)
                .requires(CommonTags.Items.DOUGH)
                .requires(CommonTags.Items.DOUGH)
                .unlockedBy("has_ghast", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.GHASMATI.get(),MNDItems.GHASTA.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/ghast_sourdough"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.GHAST_SALAD.get())
                .requires(MyCommonTags.FOODS_RAW_GHAST)
                .requires(CommonTags.Items.VEGETABLES)
                .requires(Tags.Items.CROPS_CARROT)
                .requires(Items.BOWL)
                .unlockedBy("has_ghast", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.GHASMATI.get(),MNDItems.GHASTA.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/ghast_salad"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.SPICY_SKEWER.get())
                .requires(MNDItems.BULLET_PEPPER.get())
                .requires(MyCommonTags.FOODS_RAW_STRIDER)
                .requires(Items.BLAZE_ROD)
                .requires(MNDItems.BULLET_PEPPER.get())
                .unlockedBy("has_blaze_rod", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BLAZE_ROD))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/spicy_skewer"));
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
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/raw_stuffed_hoglin"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.HOT_WINGS_BUCKET.get())
                .requires(MNDItems.HOT_WINGS.get(), 3)
                .requires(Items.BUCKET)
                .unlockedBy("has_hot_wings", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.HOT_WINGS_BUCKET.get(), MNDItems.HOT_WINGS.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/hot_wings_bucket"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.HOT_WINGS.get(), 3)
                .requires(MNDItems.HOT_WINGS_BUCKET.get())
                .requires(Items.BOWL, 3)
                .unlockedBy("has_hot_wings", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.HOT_WINGS_BUCKET.get(), MNDItems.HOT_WINGS.get()))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/hot_wings_bucket_alt"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.BURNT_ROLL.get())
                .requires(Items.MAGMA_CREAM)
                .requires(MNDTags.CURRY_MEATS)
                .unlockedBy("has_magma_cream", InventoryChangeTrigger.TriggerInstance.hasItems(Items.MAGMA_CREAM))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/burnt_roll"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.ROCK_SOUP.get())
                .requires(Items.MAGMA_CREAM,2)
                .requires(MNDItems.STRIDER_EGG.get(),2)
                .requires(Items.BOWL)
                .unlockedBy("has_magma_cream", InventoryChangeTrigger.TriggerInstance.hasItems(Items.MAGMA_CREAM))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/rock_soup"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,MNDItems.MAGMA_CAKE.get())
                .pattern("MMM")
                .pattern("PHP")
                .pattern("###")
                .define('H', MNDItems.HOT_CREAM.get()).define('M', Items.MAGMA_CREAM)
                .define('#', ModItems.STRAW.get()).define('P', MNDItems.PEPPER_POWDER.get())
                .unlockedBy("has_magma_cream", InventoryChangeTrigger.TriggerInstance.hasItems(Items.MAGMA_CREAM))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/magma_cake"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,MNDItems.MAGMA_CAKE.get())
                .requires(MNDItems.MAGMA_CAKE_SLICE.get(),7)
                .unlockedBy("has_magma_cream", InventoryChangeTrigger.TriggerInstance.hasItems(Items.MAGMA_CREAM))
                .save(consumer, ResourceLocation.parse( "mynethersdelight:crafting/magma_cake_alt"));
    }

    private static void conditionalStonecutting(Consumer<FinishedRecipe> consumer, Ingredient ingredient,
                                                net.minecraft.world.level.ItemLike result, String name,
                                                String criterion, net.minecraft.world.level.ItemLike unlockItem) {
        ConditionalRecipe.builder()
                .addCondition(new ConfigEnabledCondition(ConfigEnabledCondition.Setting.STONE_CABINETS))
                .addRecipe(nested -> SingleItemRecipeBuilder.stonecutting(ingredient, RecipeCategory.BUILDING_BLOCKS, result)
                        .unlockedBy(criterion, InventoryChangeTrigger.TriggerInstance.hasItems(unlockItem))
                        .save(nested))
                .generateAdvancement()
                .build(consumer, ResourceLocation.parse("mynethersdelight:stonecutting/" + name));
    }

    private static void conditionalSpecialRecipe(Consumer<FinishedRecipe> consumer,
                                                 net.minecraft.world.item.crafting.RecipeSerializer<? extends net.minecraft.world.item.crafting.CraftingRecipe> serializer,
                                                 String name, ConfigEnabledCondition.Setting setting) {
        ConditionalRecipe.builder()
                .addCondition(new ConfigEnabledCondition(setting))
                .addRecipe(nested -> SpecialRecipeBuilder.special(serializer).save(nested, name))
                .build(consumer, ResourceLocation.parse("mynethersdelight:" + name));
    }
}
