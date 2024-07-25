//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.common.registry;

import com.soytutta.mynethersdelight.MyNethersDelight;
import com.soytutta.mynethersdelight.common.item.*;
import com.soytutta.mynethersdelight.common.utility.MNDFoodValues;
import com.soytutta.mynethersdelight.common.block.NetherStoveBlock;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.item.ConsumableItem;
import vectorwing.farmersdelight.common.item.MushroomColonyItem;

public class MNDItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "mynethersdelight");

    public static final RegistryObject<Item> NETHER_BRICKS_CABINET;
    public static final RegistryObject<Item> NETHER_STOVE;
    public static final RegistryObject<Item> SOUL_NETHER_STOVE;
    public static final RegistryObject<Item> LETIOS_COMPOST;
    public static final RegistryObject<Item> RESURGENT_SOIL;
    public static final RegistryObject<Item> RESURGENT_SOIL_FARMLAND;
    // POWDERY
    public static final RegistryObject<Item> BULLET_PEPPER_CRATE;
    public static final RegistryObject<Item> POWDER_CANNON;
    public static final RegistryObject<Item> POWDERY_TORCH;
    public static final RegistryObject<Item> BLOCK_OF_POWDERY_CANNON;
    public static final RegistryObject<Item> BLOCK_OF_STRIPPED_POWDERY_CANNON;
    public static final RegistryObject<Item> POWDERY_CABINET;
    public static final RegistryObject<Item> POWDERY_PLANKS;
    public static final RegistryObject<Item> POWDERY_PLANKS_STAIRS;
    public static final RegistryObject<Item> POWDERY_PLANKS_SLAB;
    public static final RegistryObject<Item> POWDERY_MOSAIC;
    public static final RegistryObject<Item> POWDERY_MOSAIC_STAIRS;
    public static final RegistryObject<Item> POWDERY_MOSAIC_SLAB;
    public static final RegistryObject<Item> POWDERY_FENCE;
    public static final RegistryObject<Item> POWDERY_FENCE_GATE;
    public static final RegistryObject<Item> POWDERY_DOOR;
    public static final RegistryObject<Item> POWDERY_TRAPDOOR;
    public static final RegistryObject<Item> POWDERY_PRESSURE_PLATE;
    public static final RegistryObject<Item> POWDERY_BUTTON;
    public static final RegistryObject<Item> POWDERY_SIGN;
    // FUNGUS-Colony
    public static final RegistryObject<Item> WARPED_FUNGUS_COLONY;
    public static final RegistryObject<Item> CRIMSON_FUNGUS_COLONY;
    // STRIDER
    public static final RegistryObject<Item> STRIDER_ROCK;
    public static final RegistryObject<Item> STRIDER_EGG;
    public static final RegistryObject<Item> BOILED_EGG;
    public static final RegistryObject<Item> DEVILED_EGG;
    public static final RegistryObject<Item> STRIDER_SLICE;
    public static final RegistryObject<Item> MINCED_STRIDER;
    public static final RegistryObject<Item> BLEEDING_TARTAR;
    public static final RegistryObject<Item> STRIDER_WITH_GRILLED_FUNGUS;
    public static final RegistryObject<Item> STRIDER_STEW;
    public static final RegistryObject<Item> CRIMSON_STROGANOFF;
    public static final RegistryObject<Item> STRIDERLOAF_BLOCK;
    public static final RegistryObject<Item> STRIDERLOAF;
    public static final RegistryObject<Item> COLD_STRIDERLOAF;
    // THOPHY
    public static final RegistryObject<Item> HOGLIN_TROPHY;
    public static final RegistryObject<Item> WAXED_HOGLIN_TROPHY;
    public static final RegistryObject<Item> ZOGLIN_TROPHY;
    public static final RegistryObject<Item> SKOGLIN_TROPHY;
    // HOGLIN
    public static final RegistryObject<Item> HOGLIN_HIDE;
    public static final RegistryObject<Item> HOGLIN_LOIN;
    // HOGLIN-Sausage
    public static final RegistryObject<Item> HOGLIN_SAUSAGE;
    public static final RegistryObject<Item> ROASTED_SAUSAGE;
    public static final RegistryObject<Item> HOTDOG;
    public static final RegistryObject<Item> SAUSAGE_AND_POTATOES;
    public static final RegistryObject<Item> BREAKFAST_SAMPLER;
    // HOGLIN-Loin
    public static final RegistryObject<Item> COOKED_LOIN;
    public static final RegistryObject<Item> BLUE_TENDERLOIN_STEAK;
    public static final RegistryObject<Item> FRIED_HOGLIN_CHOP;
    // STUFFED HOGLIN
    public static final RegistryObject<Item> RAW_STUFFED_HOGLIN;
    public static final RegistryObject<Item> ROAST_STUFFED_HOGLIN;
    public static final RegistryObject<Item> ROAST_EAR;
    public static final RegistryObject<Item> PLATE_OF_STUFFED_HOGLIN_SNOUT;
    public static final RegistryObject<Item> PLATE_OF_STUFFED_HOGLIN_HAM;
    public static final RegistryObject<Item> PLATE_OF_STUFFED_HOGLIN;
    // GHAST
    public static final RegistryObject<Item> GHASTA;
    public static final RegistryObject<Item> GHASMATI;
    public static final RegistryObject<Item> GHAST_DOUGH;
    public static final RegistryObject<Item> GHAST_SALAD;
    public static final RegistryObject<Item> SPICY_NOODLE_SOUP;
    public static final RegistryObject<Item> SPICY_COTTON;
    public static final RegistryObject<Item> GHASTA_WITH_CREAM_BLOCK;
    public static final RegistryObject<Item> GHASTA_WITH_CREAM;
    // SPICY
    public static final RegistryObject<Item> BULLET_PEPPER;
    public static final RegistryObject<Item> SPICY_SKEWER;
    public static final RegistryObject<Item> CHILIDOG;
    public static final RegistryObject<Item> SPICY_HOGLIN_STEW;
    public static final RegistryObject<Item> HOT_WINGS;
    public static final RegistryObject<Item> HOT_WINGS_BUCKET;
    public static final RegistryObject<Item> SPICY_CURRY;
    // MAGMA CUBE
    public static final RegistryObject<Item> ROCK_SOUP;
    public static final RegistryObject<Item> BURNT_ROLL;
    public static final RegistryObject<Item> MAGMA_CAKE;
    public static final RegistryObject<Item> MAGMA_CAKE_SLICE;
    // LAVA
    public static final RegistryObject<Item> HOT_CREAM;
    public static final RegistryObject<Item> HOT_CREAM_CONE;

    public MNDItems() {
    }

    public static Item.Properties basicItem() {
        return (new Item.Properties()).tab(MyNethersDelight.CREATIVE_TAB);
    }
    public static Item.Properties foodItem(FoodProperties food) {
        return (new Item.Properties()).food(food).tab(MyNethersDelight.CREATIVE_TAB);
    }

    public static Item.Properties bowlFoodItem(FoodProperties food) {
        return (new Item.Properties()).food(food).craftRemainder(Items.BOWL).stacksTo(16).tab(MyNethersDelight.CREATIVE_TAB);
    }
    public static Item.Properties bucketFoodItem(FoodProperties food) {
        return (new Item.Properties()).food(food).craftRemainder(Items.BUCKET).stacksTo(3).tab(MyNethersDelight.CREATIVE_TAB);
    }

    static {
        NETHER_BRICKS_CABINET = ITEMS.register("nether_bricks_cabinet", () ->
                new BlockItem(MNDBlocks.NETHER_BRICKS_CABINET.get(), basicItem())
        );
        NETHER_STOVE = ITEMS.register("nether_bricks_stove", () -> new BlockItem(MNDBlocks.NETHER_STOVE.get(), basicItem())
                { protected boolean placeBlock(BlockPlaceContext context, BlockState state)
                    { return super.placeBlock(context, state.setValue(NetherStoveBlock.SOUL, false));}}
        );

        SOUL_NETHER_STOVE = ITEMS.register("nether_bricks_soul_stove", () -> new BlockItem(MNDBlocks.NETHER_STOVE.get(), basicItem().tab(CreativeModeTab.TAB_HOTBAR))
                { protected boolean placeBlock(BlockPlaceContext context, BlockState state)
                { return super.placeBlock(context, state.setValue(NetherStoveBlock.SOUL, true));}}
        );
        // LETIOS-Compost/soil
        LETIOS_COMPOST = ITEMS.register("letios_compost", () ->
                new BlockItem(MNDBlocks.LETIOS_COMPOST.get(), basicItem())
        );
        RESURGENT_SOIL = ITEMS.register("resurgent_soil", () ->
                new BlockItem(MNDBlocks.RESURGENT_SOIL.get(), basicItem())
        );
        RESURGENT_SOIL_FARMLAND = ITEMS.register("resurgent_soil_farmland", () ->
                new BlockItem(MNDBlocks.RESURGENT_SOIL_FARMLAND.get(), basicItem())
        );
        // POWDERY
        BULLET_PEPPER_CRATE = ITEMS.register("bullet_pepper_crate", () ->
                new BlockItem(MNDBlocks.BULLET_PEPPER_CRATE.get(), basicItem())
        );
        POWDER_CANNON = ITEMS.register("powder_cannon", () ->
                new BlockItem(MNDBlocks.POWDERY_CANNON.get(), basicItem())
        );
        POWDERY_TORCH = ITEMS.register("powdery_torch", () ->
                new StandingAndWallBlockItem(
                        MNDBlocks.POWDERY_TORCH.get(),
                        MNDBlocks.WALL_POWDERY_TORCH.get()
                        , basicItem()
            )
        );
        BLOCK_OF_POWDERY_CANNON = ITEMS.register("powdery_block", () ->
                new BlockItem(MNDBlocks.BLOCK_OF_POWDERY_CANNON.get(), basicItem())
        );
        BLOCK_OF_STRIPPED_POWDERY_CANNON = ITEMS.register("stripped_powdery_block", () ->
                new BlockItem(MNDBlocks.BLOCK_OF_STRIPPED_POWDERY_CANNON.get(), basicItem())
        );
        POWDERY_CABINET = ITEMS.register("powdery_cabinet", () ->
                new BlockItem(MNDBlocks.POWDERY_CABINET.get(), basicItem())
        );
        POWDERY_PLANKS = ITEMS.register("powdery_planks", () ->
                new BlockItem(MNDBlocks.POWDERY_PLANKS.get(), basicItem())
        );
        POWDERY_PLANKS_STAIRS = ITEMS.register("powdery_stairs", () ->
                new BlockItem(MNDBlocks.POWDERY_PLANKS_STAIRS.get(), basicItem())
        );
        POWDERY_PLANKS_SLAB = ITEMS.register("powdery_slab", () ->
                new BlockItem(MNDBlocks.POWDERY_PLANKS_SLAB.get(), basicItem())
        );
        POWDERY_MOSAIC = ITEMS.register("powdery_mosaic", () ->
                new BlockItem(MNDBlocks.POWDERY_MOSAIC.get(), basicItem())
        );
        POWDERY_MOSAIC_STAIRS = ITEMS.register("powdery_mosaic_stairs", () ->
                new BlockItem(MNDBlocks.POWDERY_MOSAIC_STAIRS.get(), basicItem())
        );
        POWDERY_MOSAIC_SLAB = ITEMS.register("powdery_mosaic_slab", () ->
                new BlockItem(MNDBlocks.POWDERY_MOSAIC_SLAB.get(), basicItem())
        );
        POWDERY_FENCE = ITEMS.register("powdery_fence", () ->
                new BlockItem(MNDBlocks.POWDERY_FENCE.get(), basicItem())
        );
        POWDERY_FENCE_GATE = ITEMS.register("powdery_fence_gate", () ->
                new BlockItem(MNDBlocks.POWDERY_FENCE_GATE.get(), basicItem())
        );
        POWDERY_DOOR = ITEMS.register("powdery_door", () ->
                new BlockItem(MNDBlocks.POWDERY_DOOR.get(), basicItem())
        );
        POWDERY_TRAPDOOR = ITEMS.register("powdery_trapdoor", () ->
                new BlockItem(MNDBlocks.POWDERY_TRAPDOOR.get(), basicItem())
        );
        POWDERY_PRESSURE_PLATE = ITEMS.register("powdery_pressure_plate", () ->
                new BlockItem(MNDBlocks.POWDERY_PRESSURE_PLATE.get(), basicItem())
        );
        POWDERY_BUTTON = ITEMS.register("powdery_button", () ->
                new BlockItem(MNDBlocks.POWDERY_BUTTON.get(), basicItem())
        );
        POWDERY_SIGN = ITEMS.register("powdery_sign", () ->
                new SignItem(basicItem().stacksTo(16), MNDBlocks.POWDERY_SIGN.get(), MNDBlocks.POWDERY_WALL_SIGN.get())
        );
        // FUNGUS-Colony
        WARPED_FUNGUS_COLONY = ITEMS.register("warped_fungus_colony", () ->
                new MushroomColonyItem(MNDBlocks.WARPED_FUNGUS_COLONY.get(), basicItem())
        );
        CRIMSON_FUNGUS_COLONY = ITEMS.register("crimson_fungus_colony", () ->
                new MushroomColonyItem(MNDBlocks.CRIMSON_FUNGUS_COLONY.get(), basicItem())
        );
        // THOPHY
        HOGLIN_TROPHY = ITEMS.register("hoglin_trophy", () ->
                new BlockItem(MNDBlocks.HOGLIN_TROPHY.get(), basicItem())
        );
        WAXED_HOGLIN_TROPHY = ITEMS.register("waxed_hoglin_trophy", () ->
                new BlockItem(MNDBlocks.WAXED_HOGLIN_TROPHY.get(), basicItem())
        );
        ZOGLIN_TROPHY = ITEMS.register("zoglin_trophy", () ->
                new BlockItem(MNDBlocks.ZOGLIN_TROPHY.get(), basicItem())
        );
        SKOGLIN_TROPHY = ITEMS.register("skoglin_trophy", () ->
                new BlockItem(MNDBlocks.SKOGLIN_TROPHY.get(), basicItem())
        );
        // HOGLIN
        HOGLIN_HIDE = ITEMS.register("hoglin_hide", () ->
                new Item(basicItem())
        );
        HOGLIN_LOIN = ITEMS.register("hoglin_loin", ()  ->
                new ConsumableItem(foodItem(MNDFoodValues.HOGLIN_LOIN))
        );
        // HOGLIN-Sausage
        HOGLIN_SAUSAGE = ITEMS.register("hoglin_sausage", ()  ->
                new ConsumableItem(foodItem(MNDFoodValues.HOGLIN_SAUSAGE))
        );
        ROASTED_SAUSAGE = ITEMS.register("roasted_sausage", ()  ->
                new ConsumableItem(foodItem(MNDFoodValues.ROASTED_SAUSAGE))
        );
        HOTDOG = ITEMS.register("hotdog", ()  ->
                new ConsumableItem(foodItem(MNDFoodValues.HOTDOG))
        );
        SAUSAGE_AND_POTATOES = ITEMS.register("sausage_and_potatoes", ()  ->
                new ConsumableItem(bowlFoodItem(MNDFoodValues.SAUSAGE_AND_POTATOES))
        );
        BREAKFAST_SAMPLER = ITEMS.register("breakfast_sampler", ()  ->
                new ConsumableItem(bowlFoodItem(MNDFoodValues.BREAKFAST_SAMPLER), true)
        );
        // HOGLIN-Loin
        COOKED_LOIN = ITEMS.register("cooked_loin", ()  ->
                new ConsumableItem(foodItem(MNDFoodValues.COOKED_LOIN))
        );
        BLUE_TENDERLOIN_STEAK = ITEMS.register("blue_tenderloin_steak", ()  ->
                new ConsumableItem(bowlFoodItem(MNDFoodValues.BLUE_TENDERLOIN_STEAK), true)
        );
        FRIED_HOGLIN_CHOP = ITEMS.register("fried_hoglin_chop", ()  ->
                new ConsumableItem(bowlFoodItem(MNDFoodValues.FRIED_HOGLIN_CHOP), true)
        );
        // STUFFED HOGLIN
        RAW_STUFFED_HOGLIN = ITEMS.register("raw_stuffed_hoglin", () ->
                new Item(basicItem().stacksTo(1))
        );
        ROAST_STUFFED_HOGLIN = ITEMS.register("roast_stuffed_hoglin", () ->
                new BlockItem(MNDBlocks.STUFFED_HOGLIN.get(),(basicItem().stacksTo(1)))
        );
        ROAST_EAR = ITEMS.register("roast_ear", ()  ->
                new ConsumableItem(foodItem(MNDFoodValues.ROAST_EAR))
        );
        PLATE_OF_STUFFED_HOGLIN_SNOUT = ITEMS.register("plate_of_stuffed_hoglin_snout", ()  ->
                new ConsumableItem(bowlFoodItem(MNDFoodValues.PLATE_OF_STUFFED_HOGLIN_SNOUT), true)
        );
        PLATE_OF_STUFFED_HOGLIN_HAM = ITEMS.register("plate_of_stuffed_hoglin_ham", ()  ->
                new ConsumableItem(bowlFoodItem(MNDFoodValues.PLATE_OF_STUFFED_HOGLIN_HAM), true)
        );
        PLATE_OF_STUFFED_HOGLIN = ITEMS.register("plate_of_stuffed_hoglin", ()  ->
                new ConsumableItem(bowlFoodItem(MNDFoodValues.PLATE_OF_STUFFED_HOGLIN), true)
        );
        // STRIDER
        STRIDER_ROCK = ITEMS.register("strider_rock", () ->
                new StriderRockItem((new Item.Properties()).stacksTo(16).fireResistant().tab(MyNethersDelight.CREATIVE_TAB))
        );
        STRIDER_EGG = ITEMS.register("strider_egg", () ->
                new StriderEggItem(foodItem(MNDFoodValues.STRIDER_EGG).stacksTo(16))
        );
        BOILED_EGG = ITEMS.register("boiled_egg", () ->
                new ConsumableItem(foodItem(MNDFoodValues.BOILED_EGG))
        );
        DEVILED_EGG = ITEMS.register("deviled_egg", () ->
                new ConsumableItem(foodItem(MNDFoodValues.DEVILED_EGG))
        );
        STRIDER_SLICE = ITEMS.register("strider_slice", ()  ->
                new ConsumableItem(foodItem(MNDFoodValues.STRIDER_SLICE).fireResistant())
        );
        MINCED_STRIDER = ITEMS.register("minced_strider", ()  ->
                new ConsumableItem(foodItem(MNDFoodValues.MINCED_STRIDER).fireResistant())
        );
        BLEEDING_TARTAR = ITEMS.register("bleeding_tartar", ()  ->
                new ConsumableItem(bowlFoodItem(MNDFoodValues.BLEEDING_TARTAR), true)
        );
        STRIDER_WITH_GRILLED_FUNGUS = ITEMS.register("strider_with_grilled_fungus", ()  ->
                new ConsumableItem(bowlFoodItem(MNDFoodValues.STRIDER_AND_GRILLED_FUNGUS), true)
        );
        STRIDER_STEW = ITEMS.register("strider_stew", ()  ->
                new ConsumableItem(bowlFoodItem(MNDFoodValues.STRIDER_STEW), true)
        );
        CRIMSON_STROGANOFF = ITEMS.register("crimson_stroganoff", ()  ->
                new ConsumableItem(bowlFoodItem(MNDFoodValues.CRIMSON_STROGANOFF), true)
        );
        STRIDERLOAF_BLOCK = ITEMS.register("striderloaf", ()  ->
                new BlockItem(MNDBlocks.STRIDERLOAF_BLOCK.get(), basicItem().stacksTo(1))
        );
        STRIDERLOAF = ITEMS.register("plate_of_striderloaf", ()  ->
                new ConsumableItem(bowlFoodItem(MNDFoodValues.STRIDERLOAF), true)
        );
        COLD_STRIDERLOAF = ITEMS.register("plate_of_cold_striderloaf", ()  ->
                new ConsumableItem(bowlFoodItem(MNDFoodValues.COLD_STRIDERLOAF), true)
        );
        // GHAST
        GHASTA = ITEMS.register("ghasta", () ->
                new ConsumableItem(foodItem(MNDFoodValues.GHASTA))
        );
        GHASMATI = ITEMS.register("ghasmati", () ->
                new Item(basicItem())
        );
        GHAST_DOUGH = ITEMS.register("ghast_dough", () ->
                new ConsumableItem(foodItem(MNDFoodValues.GHASTA))
        );
        GHAST_SALAD = ITEMS.register("ghast_salad", () ->
                new ConsumableItem(bowlFoodItem(MNDFoodValues.GHAST_SALAD), true)
        );
        SPICY_NOODLE_SOUP = ITEMS.register("spicy_noodle_soup", () ->
                new ConsumableItem(bowlFoodItem(MNDFoodValues.SPICY_NOODLE_SOUP), true)
        );
        SPICY_COTTON = ITEMS.register("spicy_cotton", () ->
                new ConsumableItem(foodItem(MNDFoodValues.SPICY_COTTON))
        );
        GHASTA_WITH_CREAM_BLOCK = ITEMS.register("ghasta_with_cream", () ->
                new BlockItem(MNDBlocks.GHASTA_WITH_CREAM_BLOCK.get(), basicItem().stacksTo(1))
        );
        GHASTA_WITH_CREAM = ITEMS.register("plate_of_ghasta_with_cream", () ->
                new ConsumableItem(bowlFoodItem(MNDFoodValues.GHASTA_WITH_CREAM), true)
        );
        // SPICY
        BULLET_PEPPER = ITEMS.register("bullet_pepper", () ->
                new ConsumableBlockItem(MNDBlocks.BULLET_PEPPER.get(),
                new Item.Properties().food((MNDFoodValues.BULLET_PEPPER)))
        );
        SPICY_SKEWER = ITEMS.register("spicy_skewer", ()  ->
                new ConsumableItem(foodItem(MNDFoodValues.SPICY_SKEWER))
        );
        CHILIDOG = ITEMS.register("chilidog", ()  ->
                new ConsumableItem(foodItem(MNDFoodValues.CHILIDOG), true)
        );
        SPICY_HOGLIN_STEW = ITEMS.register("spicy_hoglin_stew", ()  ->
                new ConsumableItem(bowlFoodItem(MNDFoodValues.SPICY_HOGLIN_STEW), true)
        );
        HOT_WINGS = ITEMS.register("hot_wings", ()  ->
                new ConsumableItem(bowlFoodItem(MNDFoodValues.HOT_WINGS), true)
        );
        HOT_WINGS_BUCKET = ITEMS.register("hot_wings_bucket", ()  ->
                new ConsumableItem(bucketFoodItem(MNDFoodValues.HOT_WINGS_BUCKET), true)
        );
        SPICY_CURRY = ITEMS.register("spicy_curry", ()  ->
                new ConsumableItem(bowlFoodItem(MNDFoodValues.SPICY_CURRY), true)
        );
        // MAGMA CUBE
        ROCK_SOUP = ITEMS.register("rock_soup", ()  ->
                new ConsumableItem(bowlFoodItem(MNDFoodValues.ROCK_SOUP), true)
        );
        BURNT_ROLL = ITEMS.register("burnt_roll", ()  ->
                new ConsumableItem(foodItem(MNDFoodValues.BURNT_ROLL))
        );
        MAGMA_CAKE = ITEMS.register("magma_cake", () ->
                new BlockItem(MNDBlocks.MAGMA_CAKE.get(), basicItem().stacksTo(1))
        );
        MAGMA_CAKE_SLICE = ITEMS.register("magma_cake_slice",
                () -> new ConsumableItem(foodItem(MNDFoodValues.MAGMA_CAKE_SLICE).stacksTo(16),
                        false, true)
        );
        // LAVA
        HOT_CREAM = ITEMS.register("hot_cream", ()  ->
                new HotCreamItem(bucketFoodItem(MNDFoodValues.HOT_CREAM))
        );
        HOT_CREAM_CONE = ITEMS.register("hot_cream_cone", ()  ->
                new HotCreamConeItem(foodItem(MNDFoodValues.HOT_CREAM_CONE).stacksTo(16))
        );
    }
}
