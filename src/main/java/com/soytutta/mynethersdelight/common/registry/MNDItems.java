//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.common.registry;

import com.soytutta.mynethersdelight.common.item.*;
import com.soytutta.mynethersdelight.common.utility.MNDFoodValues;
import com.soytutta.mynethersdelight.common.block.NetherStoveBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.item.ConsumableItem;
import vectorwing.farmersdelight.common.item.FuelBlockItem;
import vectorwing.farmersdelight.common.item.MushroomColonyItem;

public class MNDItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "mynethersdelight");

    public static final RegistryObject<Item> NETHER_STOVE;
    public static final RegistryObject<Item> NETHER_BRICKS_CABINET;
    public static final RegistryObject<Item> SOUL_NETHER_STOVE;
    public static final RegistryObject<Item> LETIOS_COMPOST;
    public static final RegistryObject<Item> RESURGENT_SOIL;
    public static final RegistryObject<Item> RESURGENT_SOIL_FARMLAND;
    // POWDERY
    public static final RegistryObject<Item> POWDER_CANNON;
    public static final RegistryObject<Item> POWDERY_TORCH;
    public static final RegistryObject<Item> POWDERY_CABINET;
    public static final RegistryObject<Item> BLOCK_OF_POWDERY_CANNON;
    public static final RegistryObject<Item> BLOCK_OF_STRIPPED_POWDERY_CANNON;
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
    public static final RegistryObject<Item> POWDERY_BUTTON;
    public static final RegistryObject<Item> POWDERY_PRESSURE_PLATE;
    public static final RegistryObject<Item> POWDERY_SIGN;
    public static final RegistryObject<Item> POWDERY_HANGING_SIGN;
    // FUNGUS-Colony
    public static final RegistryObject<Item> WARPED_FUNGUS_COLONY;
    public static final RegistryObject<Item> CRIMSON_FUNGUS_COLONY;
    // STRIDER
    public static final RegistryObject<Item> STRIDER_ROCK;
    public static final RegistryObject<Item> STRIDER_EGG;
    public static final RegistryObject<Item> STRIDER_SLICE;
    public static final RegistryObject<Item> MINCED_STRIDER;
    public static final RegistryObject<Item> BLEEDING_TARTAR;
    public static final RegistryObject<Item> STRIDER_WITH_GRILLED_FUNGUS;
    public static final RegistryObject<Item> STRIDER_STEW;
    public static final RegistryObject<Item> CRIMSON_STROGANOFF;
    // HOGLIN
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
    // SPICY
    public static final RegistryObject<Item> BULLET_PEPPER;
    public static final RegistryObject<Item> SPICY_SKEWER;
    public static final RegistryObject<Item> CHILIDOG;
    public static final RegistryObject<Item> SPICY_HOGLIN_STEW;
    public static final RegistryObject<Item> HOT_WINGS;
    public static final RegistryObject<Item> SPICY_CURRY;
    // LAVA
    public static final RegistryObject<Item> HOT_CREAM;
    public static final RegistryObject<Item> HOT_CREAM_CONE;
    // THOPHY
    public static final RegistryObject<Item> HOGLIN_TROPHY;
    public static final RegistryObject<Item> WAXED_HOGLIN_TROPHY;
    public static final RegistryObject<Item> ZOGLIN_TROPHY;
    // STUFFED HOGLIN
    public static final RegistryObject<Item> HOGLIN_HIDE;
    public static final RegistryObject<Item> RAW_STUFFED_HOGLIN;
    public static final RegistryObject<Item> ROAST_STUFFED_HOGLIN;
    public static final RegistryObject<Item> ROAST_EAR;
    public static final RegistryObject<Item> PLATE_OF_STUFFED_HOGLIN_SNOUT;
    public static final RegistryObject<Item> PLATE_OF_STUFFED_HOGLIN_HAM;
    public static final RegistryObject<Item> PLATE_OF_STUFFED_HOGLIN;


    public MNDItems() {
    }

    public static Item.Properties basicItem() {
        return (new Item.Properties());
    }
    public static Item.Properties foodItem(FoodProperties food) {
        return (new Item.Properties()).food(food);
    }

    public static Item.Properties bowlFoodItem(FoodProperties food) {
        return (new Item.Properties()).food(food).craftRemainder(Items.BOWL).stacksTo(16);
    }
    public static Item.Properties bucketFoodItem(FoodProperties food) {
        return (new Item.Properties()).food(food).craftRemainder(Items.BUCKET).stacksTo(16);
    }

    static {
        NETHER_BRICKS_CABINET = ITEMS.register("nether_bricks_cabinet", () -> {
            return new BlockItem(MNDBlocks.NETHER_BRICKS_CABINET.get(), basicItem());
        });
        NETHER_STOVE = ITEMS.register("nether_bricks_stove", () -> {
            return new BlockItem(MNDBlocks.NETHER_STOVE.get(), basicItem()) {
                protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
                    return super.placeBlock(context, state.setValue(NetherStoveBlock.SOUL, false));
                }
            };
        });
        SOUL_NETHER_STOVE = ITEMS.register("nether_bricks_soul_stove", () -> {
            return new BlockItem(MNDBlocks.NETHER_STOVE.get(), new Item.Properties()) {
                protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
                    return super.placeBlock(context, state.setValue(NetherStoveBlock.SOUL, true));
                }
            };
        });
        // LETIOS-Compost/soil
        LETIOS_COMPOST = ITEMS.register("letios_compost", () -> {
            return new BlockItem(MNDBlocks.LETIOS_COMPOST.get(), basicItem());
        });
        RESURGENT_SOIL = ITEMS.register("resurgent_soil", () -> {
            return new BlockItem(MNDBlocks.RESURGENT_SOIL.get(), basicItem());
        });
        RESURGENT_SOIL_FARMLAND = ITEMS.register("resurgent_soil_farmland", () -> {
            return new BlockItem(MNDBlocks.RESURGENT_SOIL_FARMLAND.get(), basicItem());
        });
        // POWDERY
        POWDER_CANNON = ITEMS.register("powder_cannon", () -> {
            return new FuelBlockItem(MNDBlocks.POWDERY_CANNON.get(), basicItem(),500);
        });
        POWDERY_TORCH = ITEMS.register("powdery_torch", () -> {
            return new StandingAndWallBlockItem(
                    MNDBlocks.POWDERY_TORCH.get(),
                    MNDBlocks.WALL_POWDERY_TORCH.get(),
                    new Item.Properties(),
                    Direction.DOWN
            );
        });
        POWDERY_CABINET = ITEMS.register("powdery_cabinet", () -> {
            return new BlockItem(MNDBlocks.POWDERY_CABINET.get(), basicItem());
        });
        BLOCK_OF_POWDERY_CANNON = ITEMS.register("block_of_powdery_cannon", () -> {
            return new BlockItem(MNDBlocks.BLOCK_OF_POWDERY_CANNON.get(), basicItem());
        });
        BLOCK_OF_STRIPPED_POWDERY_CANNON = ITEMS.register("block_of_stripped_powdery_cannon", () -> {
            return new BlockItem(MNDBlocks.BLOCK_OF_STRIPPED_POWDERY_CANNON.get(), basicItem());
        });
        POWDERY_PLANKS = ITEMS.register("powdery_planks", () -> {
            return new BlockItem(MNDBlocks.POWDERY_PLANKS.get(), basicItem());
        });
        POWDERY_PLANKS_STAIRS = ITEMS.register("powdery_stairs", () -> {
            return new BlockItem(MNDBlocks.POWDERY_PLANKS_STAIRS.get(), basicItem());
        });
        POWDERY_PLANKS_SLAB = ITEMS.register("powdery_slab", () -> {
            return new BlockItem(MNDBlocks.POWDERY_PLANKS_SLAB.get(), basicItem());
        });
        POWDERY_MOSAIC = ITEMS.register("powdery_mosaic", () -> {
            return new BlockItem(MNDBlocks.POWDERY_MOSAIC.get(), basicItem());
        });
        POWDERY_MOSAIC_STAIRS = ITEMS.register("powdery_mosaic_stairs", () -> {
            return new BlockItem(MNDBlocks.POWDERY_MOSAIC_STAIRS.get(), basicItem());
        });
        POWDERY_MOSAIC_SLAB = ITEMS.register("powdery_mosaic_slab", () -> {
            return new BlockItem(MNDBlocks.POWDERY_MOSAIC_SLAB.get(), basicItem());
        });
        POWDERY_FENCE = ITEMS.register("powdery_fence", () -> {
            return new BlockItem(MNDBlocks.POWDERY_FENCE.get(), basicItem());
        });
        POWDERY_FENCE_GATE = ITEMS.register("powdery_fence_gate", () -> {
            return new BlockItem(MNDBlocks.POWDERY_FENCE_GATE.get(), basicItem());
        });
        POWDERY_DOOR = ITEMS.register("powdery_door", () -> {
            return new BlockItem(MNDBlocks.POWDERY_DOOR.get(), basicItem());
        });
        POWDERY_TRAPDOOR = ITEMS.register("powdery_trapdoor", () -> {
            return new BlockItem(MNDBlocks.POWDERY_TRAPDOOR.get(), basicItem());
        });
        POWDERY_BUTTON = ITEMS.register("powdery_button", () -> {
            return new BlockItem(MNDBlocks.POWDERY_BUTTON.get(), basicItem());
        });
        POWDERY_PRESSURE_PLATE = ITEMS.register("powdery_pressure_plate", () -> {
            return new BlockItem(MNDBlocks.POWDERY_PRESSURE_PLATE.get(), basicItem());
        });
        POWDERY_SIGN = ITEMS.register("powdery_sign", () -> {
            return new SignItem(basicItem(), MNDBlocks.POWDERY_SIGN.get(), MNDBlocks.POWDERY_WALL_SIGN.get());
        });
        POWDERY_HANGING_SIGN = ITEMS.register("powdery_hanging_sign", () -> {
            return new SignItem(basicItem(), MNDBlocks.POWDERY_HANGING_SIGN.get(), MNDBlocks.POWDERY_WALL_HANGING_SIGN.get());
        });
        // FUNGUS-Colony
        WARPED_FUNGUS_COLONY = ITEMS.register("warped_fungus_colony", () -> {
            return new MushroomColonyItem(MNDBlocks.WARPED_FUNGUS_COLONY.get(), basicItem());
        });
        CRIMSON_FUNGUS_COLONY = ITEMS.register("crimson_fungus_colony", () -> {
            return new MushroomColonyItem(MNDBlocks.CRIMSON_FUNGUS_COLONY.get(), basicItem());
        });
        // STRIDER
        STRIDER_ROCK = ITEMS.register("strider_rock", () -> {
            return new StriderRockItem((new Item.Properties()).stacksTo(16).fireResistant());
        });
        STRIDER_EGG = ITEMS.register("strider_egg", () -> {
            return new StriderEggItem(foodItem(MNDFoodValues.STRIDER_EGG).stacksTo(16));
        });
        STRIDER_SLICE = ITEMS.register("strider_slice", ()  -> {
            return new ConsumableItem(foodItem(MNDFoodValues.STRIDER_SLICE).fireResistant());
        });
        MINCED_STRIDER = ITEMS.register("minced_strider", ()  -> {
            return new ConsumableItem(foodItem(MNDFoodValues.MINCED_STRIDER).fireResistant());
        });
        BLEEDING_TARTAR = ITEMS.register("bleeding_tartar", ()  -> {
            return new ConsumableItem(bowlFoodItem(MNDFoodValues.BLEEDING_TARTAR), true);
        });
        STRIDER_WITH_GRILLED_FUNGUS = ITEMS.register("strider_with_grilled_fungus", ()  -> {
            return new ConsumableItem(bowlFoodItem(MNDFoodValues.STRIDER_AND_GRILLED_FUNGUS), true);
        });
        STRIDER_STEW = ITEMS.register("strider_stew", ()  -> {
            return new ConsumableItem(bowlFoodItem(MNDFoodValues.STRIDER_STEW), true);
        });
        CRIMSON_STROGANOFF = ITEMS.register("crimson_stroganoff", ()  -> {
            return new ConsumableItem(bowlFoodItem(MNDFoodValues.CRIMSON_STROGANOFF), true);
        });
        // HOGLIN
        HOGLIN_LOIN = ITEMS.register("hoglin_loin", ()  -> {
            return new ConsumableItem(foodItem(MNDFoodValues.HOGLIN_LOIN));
        });
        // HOGLIN-Sausage
        HOGLIN_SAUSAGE = ITEMS.register("hoglin_sausage", ()  -> {
            return new ConsumableItem(foodItem(MNDFoodValues.HOGLIN_SAUSAGE));
        });
        ROASTED_SAUSAGE = ITEMS.register("roasted_sausage", ()  -> {
            return new ConsumableItem(foodItem(MNDFoodValues.ROASTED_SAUSAGE));
        });
        HOTDOG = ITEMS.register("hotdog", ()  -> {
            return new ConsumableItem(foodItem(MNDFoodValues.HOTDOG));
        });
        SAUSAGE_AND_POTATOES = ITEMS.register("sausage_and_potatoes", ()  -> {
            return new ConsumableItem(bowlFoodItem(MNDFoodValues.SAUSAGE_AND_POTATOES));
        });
        BREAKFAST_SAMPLER = ITEMS.register("breakfast_sampler", ()  -> {
            return new ConsumableItem(bowlFoodItem(MNDFoodValues.BREAKFAST_SAMPLER), true);
        });
        // HOGLIN-Loin
        COOKED_LOIN = ITEMS.register("cooked_loin", ()  -> {
            return new ConsumableItem(foodItem(MNDFoodValues.COOKED_LOIN));
        });
        BLUE_TENDERLOIN_STEAK = ITEMS.register("blue_tenderloin_steak", ()  -> {
            return new ConsumableItem(bowlFoodItem(MNDFoodValues.BLUE_TENDERLOIN_STEAK), true);
        });
        FRIED_HOGLIN_CHOP = ITEMS.register("fried_hoglin_chop", ()  -> {
            return new ConsumableItem(bowlFoodItem(MNDFoodValues.FRIED_HOGLIN_CHOP), true);
        });
        // SPICY
        BULLET_PEPPER = ITEMS.register("bullet_pepper", () -> {
            return new ConsumableBlockItem(MNDBlocks.BULLET_PEPPER.get(),
                    new Item.Properties().food((MNDFoodValues.BULLET_PEPPER)));
        });
        SPICY_SKEWER = ITEMS.register("spicy_skewer", ()  -> {
            return new ConsumableItem(foodItem(MNDFoodValues.SPICY_SKEWER));
        });
        CHILIDOG = ITEMS.register("chilidog", ()  -> {
            return new ConsumableItem(foodItem(MNDFoodValues.CHILIDOG));
        });
        SPICY_HOGLIN_STEW = ITEMS.register("spicy_hoglin_stew", ()  -> {
            return new ConsumableItem(bowlFoodItem(MNDFoodValues.SPICY_HOGLIN_STEW), true);
        });
        HOT_WINGS = ITEMS.register("hot_wings", ()  -> {
            return new ConsumableItem(bowlFoodItem(MNDFoodValues.HOT_WINGS), true);
        });
        SPICY_CURRY = ITEMS.register("spicy_curry", ()  -> {
            return new ConsumableItem(bowlFoodItem(MNDFoodValues.SPICY_CURRY), true);
        });
        // LAVA
        HOT_CREAM = ITEMS.register("hot_cream", ()  -> {
            return new HotCreamItem(bucketFoodItem(MNDFoodValues.HOT_CREAM).stacksTo(1));
        });
        HOT_CREAM_CONE = ITEMS.register("hot_cream_cone", ()  -> {
            return new HotCreamConeItem(foodItem(MNDFoodValues.HOT_CREAM_CONE).stacksTo(16));
        });
        // THOPHY
        HOGLIN_TROPHY = ITEMS.register("hoglin_trophy", () -> {
            return new BlockItem(MNDBlocks.HOGLIN_TROPHY.get(), basicItem().stacksTo(1));
        });
        WAXED_HOGLIN_TROPHY = ITEMS.register("waxed_hoglin_trophy", () -> {
            return new BlockItem(MNDBlocks.WAXED_HOGLIN_TROPHY.get(), basicItem().stacksTo(1));
        });
        ZOGLIN_TROPHY = ITEMS.register("zoglin_trophy", () -> {
            return new BlockItem(MNDBlocks.ZOGLIN_TROPHY.get(), basicItem().stacksTo(1));
        });
        // STUFFED HOGLIN
        HOGLIN_HIDE = ITEMS.register("hoglin_hide", () -> {
            return new Item(basicItem());
        });
        RAW_STUFFED_HOGLIN = ITEMS.register("raw_stuffed_hoglin", () -> {
            return new Item(basicItem().stacksTo(1));
        });
        ROAST_STUFFED_HOGLIN = ITEMS.register("roast_stuffed_hoglin", () -> {
            return new BlockItem(MNDBlocks.STUFFED_HOGLIN.get(),(basicItem().stacksTo(1)));
        });
        ROAST_EAR = ITEMS.register("roast_ear", ()  -> {
            return new ConsumableItem(foodItem(MNDFoodValues.ROAST_EAR));
        });
        PLATE_OF_STUFFED_HOGLIN_SNOUT = ITEMS.register("plate_of_stuffed_hoglin_snout", ()  -> {
            return new ConsumableItem(bowlFoodItem(MNDFoodValues.PLATE_OF_STUFFED_HOGLIN_SNOUT), true);
        });
        PLATE_OF_STUFFED_HOGLIN_HAM = ITEMS.register("plate_of_stuffed_hoglin_ham", ()  -> {
            return new ConsumableItem(bowlFoodItem(MNDFoodValues.PLATE_OF_STUFFED_HOGLIN_HAM), true);
        });
        PLATE_OF_STUFFED_HOGLIN = ITEMS.register("plate_of_stuffed_hoglin", ()  -> {
            return new ConsumableItem(bowlFoodItem(MNDFoodValues.PLATE_OF_STUFFED_HOGLIN), true);
        });
    }
}
