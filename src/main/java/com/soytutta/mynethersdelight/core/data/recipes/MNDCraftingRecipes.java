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
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.ItemLike;
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
        ShapedRecipeBuilder.shaped(Items.STICK)
                .pattern("#").pattern("#")
                .define('#', (Ingredient.of(Items.BAMBOO,MNDItems.POWDER_CANNON.get())))
                .unlockedBy("has_powder_cannon", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDER_CANNON.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "stick_alt"));
        ShapelessRecipeBuilder.shapeless( Items.FIREWORK_ROCKET)
                .requires (MNDTags.POWDER_CANNON)
                .requires(Items.PAPER)
                .unlockedBy("has_powder_cannon", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDER_CANNON.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "firework_alt"));
        ShapedRecipeBuilder.shaped( Blocks.TNT)
                .pattern("#s#")
                .pattern("s#s")
                .pattern("#s#")
                .define('#', MNDTags.POWDER_CANNON)
                .define('s', ItemTags.SAND)
                .unlockedBy("has_powder_cannon", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDER_CANNON.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "tnt_alt"));
        ShapedRecipeBuilder.shaped(Items.SCAFFOLDING, 6)
                .pattern("B#b")
                .pattern("B b")
                .pattern("B b")
                .define('b', (Ingredient.of(Items.BAMBOO,MNDItems.POWDER_CANNON.get())))
                .define('B', (Ingredient.of(MNDItems.POWDER_CANNON.get(),Items.BAMBOO)))
                .define('#',(Ingredient.of(ModItems.CANVAS.get(), Items.STRING)))
                .unlockedBy("has_powder_cannon_or_canvas", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDER_CANNON.get(),ModItems.CANVAS.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "scaffolding_alt"));
        ShapedRecipeBuilder.shaped(ModBlocks.BASKET.get())
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
        ShapedRecipeBuilder.shaped(MNDBlocks.POWDERY_TORCH.get())
                .pattern("P").pattern("#")
                .define('#', MNDTags.POWDER_CANNON).define('P', MNDTags.BULLET_PEPPER)
                .unlockedBy("has_powder_cannon", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDER_CANNON.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "powdery_torch"));
        ShapedRecipeBuilder.shaped(MNDBlocks.HOGLIN_TROPHY.get())
                .pattern("W#W")
                .pattern("bBb")
                .pattern("WGW")
                .define('#', MNDTags.HOGLIN_HIDE).define('G', Items.GOLD_INGOT)
                .define('B', Blocks.BONE_BLOCK).define('b', Items.BONE)
                .define('W', ItemTags.PLANKS)
                .group("nether_trophy")
                .unlockedBy("has_hoglin_hide", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.HOGLIN_HIDE.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "hoglin_trophy"));
        ShapelessRecipeBuilder.shapeless(MNDItems.WAXED_HOGLIN_TROPHY.get())
                .requires(MNDItems.HOGLIN_TROPHY.get())
                .requires(Ingredient.of(MNDTags.HOGLIN_WAXED))
                .group("nether_trophy")
                .unlockedBy("has_hoglin_trophy", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.HOGLIN_TROPHY.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "waxed_hoglin_trophy"));
        ShapelessRecipeBuilder.shapeless(MNDItems.HOGLIN_TROPHY.get())
                .requires(MNDItems.ZOGLIN_TROPHY.get())
                .requires(Ingredient.of(MNDTags.HOGLIN_CURE))
                .group("nether_trophy")
                .unlockedBy("has_zoglin_trophy", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.ZOGLIN_TROPHY.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "hoglin_trophy_cure"));
        ShapedRecipeBuilder.shaped(MNDBlocks.ZOGLIN_TROPHY.get())
                .pattern("wZw")
                .pattern("Z#Z")
                .pattern("wZw")
                .define('#', MNDItems.HOGLIN_TROPHY.get()).define('Z', Items.ROTTEN_FLESH).define('w', Items.WARPED_FUNGUS)
                .group("nether_trophy")
                .unlockedBy("has_hoglin_trophy", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.HOGLIN_TROPHY.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "zoglin_trophy"));

        ShapelessRecipeBuilder.shapeless(MNDItems.NETHER_STOVE.get())
                .requires(MNDItems.SOUL_NETHER_STOVE.get())
                .requires(Ingredient.of(MNDTags.STOVE_FIRE_FUEL))
                .group("nether_stove")
                .unlockedBy("has_soul_stove", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.SOUL_NETHER_STOVE.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "nethers_stove_alt0"));
        ShapedRecipeBuilder.shaped(MNDItems.NETHER_STOVE.get())
                .pattern("iii")
                .pattern("B B")
                .pattern("BCB")
                .define('i', Tags.Items.INGOTS_NETHER_BRICK)
                .define('B', Blocks.POLISHED_BLACKSTONE_BRICKS).define('C', Blocks.CAMPFIRE)
                .group("nether_stove")
                .unlockedBy("has_campfire", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.CAMPFIRE))
                .save(consumer, new ResourceLocation("mynethersdelight", "nethers_stove"));
        ShapedRecipeBuilder.shaped(MNDItems.NETHER_STOVE.get())
                .pattern("iii")
                .pattern("B#B")
                .pattern("BCB")
                .define('i', Tags.Items.INGOTS_NETHER_BRICK).define('#', MNDTags.STOVE_FIRE_FUEL)
                .define('B', Blocks.POLISHED_BLACKSTONE_BRICKS).define('C', Blocks.SOUL_CAMPFIRE)
                .group("nether_stove")
                .unlockedBy("has_soul_campfire", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.SOUL_CAMPFIRE))
                .save(consumer, new ResourceLocation("mynethersdelight", "nethers_stove_alt"));

        ShapelessRecipeBuilder.shapeless(MNDItems.SOUL_NETHER_STOVE.get())
                .requires(MNDItems.NETHER_STOVE.get())
                .requires(Ingredient.of(MNDTags.STOVE_SOUL_FUEL))
                .group("nether_stove")
                .unlockedBy("has_stove", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.NETHER_STOVE.get()))
                .save(consumer, new ResourceLocation("mynethersdelight", "soul_nethers_stove_alt0"));
        ShapedRecipeBuilder.shaped(MNDItems.SOUL_NETHER_STOVE.get())
                .pattern("iii")
                .pattern("B B")
                .pattern("BCB")
                .define('i', Tags.Items.INGOTS_NETHER_BRICK)
                .define('B', Blocks.POLISHED_BLACKSTONE_BRICKS).define('C', Blocks.SOUL_CAMPFIRE)
                .group("nether_stove")
                .unlockedBy("has_soul_campfire", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.SOUL_CAMPFIRE))
                .save(consumer, new ResourceLocation("mynethersdelight", "soul_nethers_stove"));
        ShapedRecipeBuilder.shaped(MNDItems.SOUL_NETHER_STOVE.get())
                .pattern("iii")
                .pattern("B#B")
                .pattern("BCB")
                .define('i', Tags.Items.INGOTS_NETHER_BRICK).define('#', MNDTags.STOVE_SOUL_FUEL)
                .define('B', Blocks.POLISHED_BLACKSTONE_BRICKS).define('C', Blocks.CAMPFIRE)
                .group("nether_stove")
                .unlockedBy("has_campfire", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.CAMPFIRE))
                .save(consumer, new ResourceLocation("mynethersdelight", "soul_nethers_stove_alt"));

        ShapelessRecipeBuilder.shapeless(MNDItems.LETIOS_COMPOST.get(), 1)
                .requires(Ingredient.of(Items.SOUL_SAND, Items.SOUL_SOIL))
                .requires(Items.ROTTEN_FLESH,2)
                .requires(Ingredient.of(Items.WARPED_ROOTS, Items.CRIMSON_ROOTS))
                .requires(Ingredient.of(Items.CRIMSON_ROOTS, Items.WARPED_ROOTS))
                .requires(Items.BONE_MEAL, 4)
                .unlockedBy("has_rotten_flesh", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ROTTEN_FLESH))
                .unlockedBy("has_roots", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CRIMSON_ROOTS,Items.WARPED_ROOTS))
                .save(consumer, new ResourceLocation("mynethersdelight", "letios_compost_from_rotten_flesh"));
        ShapelessRecipeBuilder.shapeless(MNDItems.LETIOS_COMPOST.get(), 1)
                .requires(Ingredient.of(Items.SOUL_SAND, Items.SOUL_SOIL))
                .requires(Items.BONE_MEAL,2)
                .requires(Ingredient.of(Items.WARPED_ROOTS, Items.CRIMSON_ROOTS))
                .requires(Ingredient.of(Items.CRIMSON_ROOTS, Items.WARPED_ROOTS))
                .requires(Items.ROTTEN_FLESH, 4)
                .unlockedBy("has_bone_meal", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BONE_MEAL))
                .unlockedBy("has_roots", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CRIMSON_ROOTS,Items.WARPED_ROOTS))
                .save(consumer, new ResourceLocation("mynethersdelight", "letios_compost_from_bone_alt"));
    }
    private static void recipesCraftedMeals(Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(MNDItems.BLEEDING_TARTAR.get())
                .requires(MNDTags.MINCED_STRIDER).requires(MNDTags.MINCED_STRIDER)
                .requires(ForgeTags.EGGS)
                .requires(Items.BOWL)
                .unlockedBy("has_minced_strider", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.MINCED_STRIDER.get()))
                .save(consumer, "mynethersdelight:crafting/bleeding_tartar");
        ShapelessRecipeBuilder.shapeless(MNDItems.HOTDOG.get())
                .requires(MNDItems.ROASTED_SAUSAGE.get())
                .requires(Items.BREAD)
                .unlockedBy("has_sausage", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.HOGLIN_SAUSAGE.get()))
                .save(consumer, "mynethersdelight:crafting/hotdog");

        ShapelessRecipeBuilder.shapeless( MNDItems.BLUE_TENDERLOIN_STEAK.get())
                .requires(Blocks.WARPED_FUNGUS)
                .requires(Ingredient.of(Items.WARPED_FUNGUS, Items.WARPED_ROOTS))
                .requires(MNDTags.COOKED_HOGLIN_LOIN)
                .requires(Items.BOWL)
                .unlockedBy("has_hoglin_loin", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.HOGLIN_LOIN.get()))
                .group("blue_tenderloin_steak_group")
                .save(consumer, "mynethersdelight:crafting/blue_tenderloin_steak");
        ShapelessRecipeBuilder.shapeless(MNDItems.BREAKFAST_SAMPLER.get())
                .requires(MNDItems.ROASTED_SAUSAGE.get(),2)
                .requires(Items.HONEY_BOTTLE)
                .requires(ForgeTags.COOKED_EGGS)
                .requires(ForgeTags.COOKED_EGGS)
                .requires(Items.BREAD)
                .requires(Items.BOWL)
                .unlockedBy("has_sausage", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.ROASTED_SAUSAGE.get()))
                .save(consumer, "mynethersdelight:crafting/breakfast_sampler");

        ShapelessRecipeBuilder.shapeless(MNDItems.HOT_CREAM_CONE.get(), 3)
                .requires(MNDItems.HOT_CREAM.get())
                .requires(MNDTags.POWDER_CANNON)
                .requires(MNDTags.POWDER_CANNON)
                .requires(MNDTags.POWDER_CANNON)
                .unlockedBy("has_powder_cannon", InventoryChangeTrigger.TriggerInstance.hasItems(MNDItems.POWDER_CANNON.get(),MNDItems.HOT_CREAM.get(),MNDItems.HOT_CREAM_CONE.get()))
                .save(consumer, "mynethersdelight:crafting/hotcream_cone");

        ShapelessRecipeBuilder.shapeless(MNDItems.SPICY_SKEWER.get())
                .requires(MNDTags.STRIDER_MEATS)
                .requires(MNDTags.BULLET_PEPPER)
                .requires(MNDTags.BULLET_PEPPER)
                .requires(Items.BLAZE_ROD)
                .unlockedBy("has_blaze_rod", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BLAZE_ROD))
                .save(consumer, "mynethersdelight:crafting/spicy_skewer");
        ShapedRecipeBuilder.shaped(MNDItems.RAW_STUFFED_HOGLIN.get())
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

    }
}
