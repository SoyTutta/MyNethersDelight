//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.common.registry;

import com.soytutta.mynethersdelight.common.item.*;
import com.soytutta.mynethersdelight.common.utility.MNDFoodValues;
import com.soytutta.mynethersdelight.common.block.NetherStoveBlock;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.common.item.ConsumableItem;
import vectorwing.farmersdelight.common.item.MushroomColonyItem;
import vectorwing.farmersdelight.common.item.PlaceableItem;
import vectorwing.farmersdelight.common.item.PopsicleItem;

import java.util.function.Supplier;

public class MNDItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, "mynethersdelight");

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
        return (new Item.Properties()).food(food).craftRemainder(Items.BUCKET).stacksTo(3);
    }

    public static final Supplier<Item> BLAZIER = ITEMS.register("blazier", () ->
            new BlockItem(MNDBlocks.BLAZIER_BLOCK.get(), basicItem())
    );

    public static final Supplier<Item> NETHER_BRICKS_CABINET = ITEMS.register("nether_bricks_cabinet", () ->
            new BlockItem(MNDBlocks.NETHER_BRICKS_CABINET.get(), basicItem())
    );
    public static final Supplier<Item> RED_NETHER_BRICKS_CABINET = ITEMS.register("red_nether_bricks_cabinet", () ->
            new BlockItem(MNDBlocks.RED_NETHER_BRICKS_CABINET.get(), basicItem())
    );
    public static final Supplier<Item> BLACKSTONE_BRICKS_CABINET = ITEMS.register("blackstone_bricks_cabinet", () ->
            new BlockItem(MNDBlocks.BLACKSTONE_BRICKS_CABINET.get(), basicItem())
    );
    public static final Supplier<Item> NETHER_STOVE = ITEMS.register("nether_bricks_stove", () -> new BlockItem(MNDBlocks.NETHER_STOVE.get(), basicItem())
            { protected boolean placeBlock(BlockPlaceContext context, BlockState state)
                { return super.placeBlock(context, state.setValue(NetherStoveBlock.SOUL, false));}}
    );

    public static final Supplier<Item> SOUL_NETHER_STOVE = ITEMS.register("nether_bricks_soul_stove", () -> new BlockItem(MNDBlocks.NETHER_STOVE.get(), basicItem())
            { protected boolean placeBlock(BlockPlaceContext context, BlockState state)
            { return super.placeBlock(context, state.setValue(NetherStoveBlock.SOUL, true));}}
    );
    // LETIOS-Compost/soil
    public static final Supplier<Item> LETIOS_COMPOST = ITEMS.register("letios_compost", () ->
            new BlockItem(MNDBlocks.LETIOS_COMPOST.get(), basicItem())
    );
    public static final Supplier<Item> RESURGENT_SOIL = ITEMS.register("resurgent_soil", () ->
            new BlockItem(MNDBlocks.RESURGENT_SOIL.get(), basicItem())
    );
    public static final Supplier<Item> RESURGENT_SOIL_FARMLAND = ITEMS.register("resurgent_soil_farmland", () ->
            new BlockItem(MNDBlocks.RESURGENT_SOIL_FARMLAND.get(), basicItem())
    );
    // POWDERY
    public static final Supplier<Item> BULLET_PEPPER_CRATE = ITEMS.register("bullet_pepper_crate", () ->
            new BlockItem(MNDBlocks.BULLET_PEPPER_CRATE.get(), basicItem())
    );
    public static final Supplier<Item> POWDER_CANNON = ITEMS.register("powder_cannon", () ->
            new BlockItem(MNDBlocks.POWDERY_CANNON.get(), basicItem())
    );
    public static final Supplier<Item> POWDERY_TORCH = ITEMS.register("powdery_torch", () ->
            new StandingAndWallBlockItem(
                MNDBlocks.POWDERY_TORCH.get(),
                MNDBlocks.WALL_POWDERY_TORCH.get(),
            new Item.Properties(), Direction.DOWN
        )
    );
    public static final Supplier<Item> POWDERY_CABINET = ITEMS.register("powdery_cabinet", () ->
            new BlockItem(MNDBlocks.POWDERY_CABINET.get(), basicItem())
    );
    public static final Supplier<Item> BLOCK_OF_POWDERY_CANNON = ITEMS.register("powdery_block", () ->
            new BlockItem(MNDBlocks.BLOCK_OF_POWDERY_CANNON.get(), basicItem())
    );
    public static final Supplier<Item> BLOCK_OF_STRIPPED_POWDERY_CANNON = ITEMS.register("stripped_powdery_block", () ->
            new BlockItem(MNDBlocks.BLOCK_OF_STRIPPED_POWDERY_CANNON.get(), basicItem())
    );
    public static final Supplier<Item> POWDERY_PLANKS = ITEMS.register("powdery_planks", () ->
            new BlockItem(MNDBlocks.POWDERY_PLANKS.get(), basicItem())
    );
    public static final Supplier<Item> POWDERY_PLANKS_STAIRS = ITEMS.register("powdery_stairs", () ->
            new BlockItem(MNDBlocks.POWDERY_PLANKS_STAIRS.get(), basicItem())
    );
    public static final Supplier<Item> POWDERY_PLANKS_SLAB = ITEMS.register("powdery_slab", () ->
            new BlockItem(MNDBlocks.POWDERY_PLANKS_SLAB.get(), basicItem())
    );
    public static final Supplier<Item> POWDERY_MOSAIC = ITEMS.register("powdery_mosaic", () ->
            new BlockItem(MNDBlocks.POWDERY_MOSAIC.get(), basicItem())
    );
    public static final Supplier<Item> POWDERY_MOSAIC_STAIRS = ITEMS.register("powdery_mosaic_stairs", () ->
            new BlockItem(MNDBlocks.POWDERY_MOSAIC_STAIRS.get(), basicItem())
    );
    public static final Supplier<Item> POWDERY_MOSAIC_SLAB = ITEMS.register("powdery_mosaic_slab", () ->
            new BlockItem(MNDBlocks.POWDERY_MOSAIC_SLAB.get(), basicItem())
    );
    public static final Supplier<Item> POWDERY_FENCE = ITEMS.register("powdery_fence", () ->
            new BlockItem(MNDBlocks.POWDERY_FENCE.get(), basicItem())
    );
    public static final Supplier<Item> POWDERY_FENCE_GATE = ITEMS.register("powdery_fence_gate", () ->
            new BlockItem(MNDBlocks.POWDERY_FENCE_GATE.get(), basicItem())
    );
    public static final Supplier<Item> POWDERY_DOOR = ITEMS.register("powdery_door", () ->
            new BlockItem(MNDBlocks.POWDERY_DOOR.get(), basicItem())
    );
    public static final Supplier<Item> POWDERY_TRAPDOOR = ITEMS.register("powdery_trapdoor", () ->
            new BlockItem(MNDBlocks.POWDERY_TRAPDOOR.get(), basicItem())
    );
    public static final Supplier<Item> POWDERY_BUTTON = ITEMS.register("powdery_button", () ->
            new BlockItem(MNDBlocks.POWDERY_BUTTON.get(), basicItem())
    );
    public static final Supplier<Item> POWDERY_PRESSURE_PLATE = ITEMS.register("powdery_pressure_plate", () ->
            new BlockItem(MNDBlocks.POWDERY_PRESSURE_PLATE.get(), basicItem())
    );
    public static final Supplier<Item> POWDERY_SIGN = ITEMS.register("powdery_sign", () ->
            new SignItem(basicItem().stacksTo(16), MNDBlocks.POWDERY_SIGN.get(), MNDBlocks.POWDERY_WALL_SIGN.get())
    );
    public static final Supplier<Item> POWDERY_HANGING_SIGN = ITEMS.register("powdery_hanging_sign", () ->
            new HangingSignItem(MNDBlocks.POWDERY_HANGING_SIGN.get(), MNDBlocks.POWDERY_WALL_HANGING_SIGN.get(), basicItem().stacksTo(16))
    );
    // FUNGUS-Colony
    public static final Supplier<Item> WARPED_FUNGUS_COLONY = ITEMS.register("warped_fungus_colony", () ->
            new MushroomColonyItem(MNDBlocks.WARPED_FUNGUS_COLONY.get(), basicItem())
    );
    public static final Supplier<Item> CRIMSON_FUNGUS_COLONY = ITEMS.register("crimson_fungus_colony", () ->
            new MushroomColonyItem(MNDBlocks.CRIMSON_FUNGUS_COLONY.get(), basicItem())
    );
    // STRIDER
    public static final Supplier<Item> STRIDER_ROCK = ITEMS.register("strider_rock", () ->
            new StriderRockItem((new Item.Properties()).stacksTo(16).fireResistant())
    );
    public static final Supplier<Item> STRIDER_EGG = ITEMS.register("strider_egg", () ->
            new StriderEggItem(foodItem(MNDFoodValues.STRIDER_EGG).stacksTo(16))
    );
    public static final Supplier<Item> ENCHANTED_GOLDEN_EGG = ITEMS.register("enchanted_golden_egg", () ->
            new GoldenEggItem(foodItem(MNDFoodValues.ENCHANTED_GOLDEN_EGG).rarity(Rarity.EPIC).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true))
    );
    public static final Supplier<Item> GOLDEN_EGG = ITEMS.register("golden_egg", () ->
            new GoldenEggItem(foodItem(MNDFoodValues.GOLDEN_EGG).rarity(Rarity.RARE))
    );
    public static final Supplier<Item> BOILED_EGG = ITEMS.register("boiled_egg", () ->
            new ConsumableItem(foodItem(MNDFoodValues.BOILED_EGG))
    );
    public static final Supplier<Item> DEVILED_EGG = ITEMS.register("deviled_egg", () ->
            new ConsumableItem(foodItem(MNDFoodValues.DEVILED_EGG))
    );
    public static final Supplier<Item> SCOTCH_EGGS = ITEMS.register("scotch_eggs", ()  ->
            new ConsumableItem(bowlFoodItem(MNDFoodValues.SCOTCH_EGGS), true)
    );
    public static final Supplier<Item> EGG_SOUP = ITEMS.register("egg_soup", ()  ->
            new ConsumableItem(bowlFoodItem(MNDFoodValues.EGG_SOUP), true)
    );
    public static final Supplier<Item> STRIDER_SLICE = ITEMS.register("strider_slice", ()  ->
            new ConsumableItem(foodItem(MNDFoodValues.STRIDER_SLICE).fireResistant(), false)
    );
    public static final Supplier<Item> MINCED_STRIDER = ITEMS.register("minced_strider", ()  ->
            new ConsumableItem(foodItem(MNDFoodValues.MINCED_STRIDER).fireResistant())
    );
    public static final Supplier<Item> BLEEDING_TARTAR = ITEMS.register("bleeding_tartar", ()  ->
            new ConsumableItem(bowlFoodItem(MNDFoodValues.BLEEDING_TARTAR), true)
    );
    public static final Supplier<Item> STRIDER_WITH_GRILLED_FUNGUS = ITEMS.register("strider_with_grilled_fungus", ()  ->
            new ConsumableItem(bowlFoodItem(MNDFoodValues.STRIDER_AND_GRILLED_FUNGUS), true)
    );
    public static final Supplier<Item> STRIDER_STEW = ITEMS.register("strider_stew", ()  ->
            new ConsumableItem(bowlFoodItem(MNDFoodValues.STRIDER_STEW), true)
    );
    public static final Supplier<Item> CRIMSON_STROGANOFF = ITEMS.register("crimson_stroganoff", ()  ->
            new ConsumableItem(bowlFoodItem(MNDFoodValues.CRIMSON_STROGANOFF), true)
    );
    public static final Supplier<Item> STRIDERLOAF_BLOCK = ITEMS.register("striderloaf", ()  ->
            new PlaceableItem(MNDBlocks.STRIDERLOAF_BLOCK.get(), basicItem().stacksTo(1))
    );
    public static final Supplier<Item> STRIDERLOAF = ITEMS.register("plate_of_striderloaf", ()  ->
            new ConsumableItem(bowlFoodItem(MNDFoodValues.STRIDERLOAF), true)
    );
    public static final Supplier<Item> COLD_STRIDERLOAF_BLOCK = ITEMS.register("cold_striderloaf", ()  ->
            new PlaceableItem(MNDBlocks.COLD_STRIDERLOAF_BLOCK.get(), basicItem().stacksTo(1))
    );
    public static final Supplier<Item> COLD_STRIDERLOAF = ITEMS.register("plate_of_cold_striderloaf", ()  ->
            new ConsumableItem(bowlFoodItem(MNDFoodValues.COLD_STRIDERLOAF), true)
    );
    // HOGLIN
    public static final Supplier<Item> HOGLIN_LOIN = ITEMS.register("hoglin_loin", ()  ->
            new ConsumableItem(foodItem(MNDFoodValues.HOGLIN_LOIN), false)
    );
    // HOGLIN-Sausage
    public static final Supplier<Item> HOGLIN_SAUSAGE = ITEMS.register("hoglin_sausage", ()  ->
            new ConsumableItem(foodItem(MNDFoodValues.HOGLIN_SAUSAGE), false)
    );
    public static final Supplier<Item> ROASTED_SAUSAGE = ITEMS.register("roasted_sausage", ()  ->
            new ConsumableItem(foodItem(MNDFoodValues.ROASTED_SAUSAGE))
    );
    public static final Supplier<Item> HOTDOG = ITEMS.register("hotdog", ()  ->
            new ConsumableItem(foodItem(MNDFoodValues.HOTDOG), false)
    );
    public static final Supplier<Item> HOTDOG_WITH_MIXED_SALAD = ITEMS.register("hotdog_with_mixed_salad", ()  ->
            new ConsumableItem(foodItem(MNDFoodValues.HOTDOG_WITH_MIXED_SALAD), true)
    );
    public static final Supplier<Item> HOTDOG_WITH_NETHER_SALAD = ITEMS.register("hotdog_with_nether_salad", ()  ->
            new ConsumableItem(foodItem(MNDFoodValues.HOTDOG_WITH_NETHER_SALAD), false)
    );
    public static final Supplier<Item> SAUSAGE_AND_POTATOES = ITEMS.register("sausage_and_potatoes", ()  ->
            new ConsumableItem(bowlFoodItem(MNDFoodValues.SAUSAGE_AND_POTATOES), false)
    );
    public static final Supplier<Item> BREAKFAST_SAMPLER = ITEMS.register("breakfast_sampler", ()  ->
            new ConsumableItem(bowlFoodItem(MNDFoodValues.BREAKFAST_SAMPLER), true)
    );
    // HOGLIN-Loin
    public static final Supplier<Item> COOKED_LOIN = ITEMS.register("cooked_loin", ()  ->
            new ConsumableItem(foodItem(MNDFoodValues.COOKED_LOIN))
    );
    public static final Supplier<Item> NETHER_BURGER = ITEMS.register("nether_burger", ()  ->
            new ConsumableItem(foodItem(MNDFoodValues.NETHER_BURGER))
    );
    public static final Supplier<Item> BLUE_TENDERLOIN_STEAK = ITEMS.register("blue_tenderloin_steak", ()  ->
            new ConsumableItem(bowlFoodItem(MNDFoodValues.BLUE_TENDERLOIN_STEAK), true)
    );
    public static final Supplier<Item> RED_LOIN_STICK = ITEMS.register("red_loin_on_a_stick", ()  ->
            new ConsumableItem(foodItem(MNDFoodValues.RED_LOIN_STICK))
    );
    public static final Supplier<Item> BACON_WRAPPED_SAUSAGE_STICK = ITEMS.register("bacon-wrapped_sausage_on_a_stick", ()  ->
            new ConsumableItem(foodItem(MNDFoodValues.BACON_WRAPPED_SAUSAGE_STICK))
    );
    public static final Supplier<Item> FRIED_HOGLIN_CHOP = ITEMS.register("fried_hoglin_chop", ()  ->
            new ConsumableItem(bowlFoodItem(MNDFoodValues.FRIED_HOGLIN_CHOP), true)
    );
    // GHAST
    public static final Supplier<Item> GHASTA = ITEMS.register("ghasta", () ->
            new ConsumableItem(foodItem(MNDFoodValues.GHASTA), false)
    );
    public static final Supplier<Item> TWISTED_GHASTA = ITEMS.register("twisted_ghasta", () ->
            new ConsumableItem(bowlFoodItem(MNDFoodValues.TWISTED_GHASTA),true)
    );
    public static final Supplier<Item> GIANT_TAKOYAKI = ITEMS.register("giant_takoyaki", () ->
            new ConsumableItem(bowlFoodItem(MNDFoodValues.GIANT_TAKOYAKI))
    );
    public static final Supplier<Item> FRIES_GHASTA = ITEMS.register("fries_ghasta", () ->
            new ConsumableItem(foodItem(MNDFoodValues.FRIES_GHASTA))
    );
    public static final Supplier<Item> SPICY_NOODLE_SOUP = ITEMS.register("spicy_noodle_soup", () ->
            new ConsumableItem(bowlFoodItem(MNDFoodValues.SPICY_NOODLE_SOUP), true)
    );
    public static final Supplier<Item> SPICY_COTTON = ITEMS.register("spicy_cotton", () ->
            new ConsumableItem(foodItem(MNDFoodValues.SPICY_COTTON), false)
    );
    public static final Supplier<Item> GHASMATI = ITEMS.register("ghasmati", () ->
            new Item(basicItem())
    );
    public static final Supplier<Item> GHAST_SALAD = ITEMS.register("ghast_salad", () ->
            new ConsumableItem(bowlFoodItem(MNDFoodValues.GHAST_SALAD), true)
    );
    public static final Supplier<Item> DRIED_GHAST_WITH_MILK = ITEMS.register("dried_ghast_with_milk", () ->
            new ConsumableItem(bowlFoodItem(MNDFoodValues.DRIED_GHAST_WITH_MILK), false)
    );
    public static final Supplier<Item> SIZZLING_PUDDING = ITEMS.register("sizzling_pudding", () ->
            new ConsumableItem(bowlFoodItem(MNDFoodValues.SIZZLING_PUDDING), false)
    );
    public static final Supplier<Item> TEAR_POPSICLE = ITEMS.register("tear_popsicle", () ->
            new PopsicleItem(foodItem(MNDFoodValues.TEAR_POPSICLE).rarity(Rarity.RARE))
    );
    public static final Supplier<Item> GHASTA_WITH_CREAM_BLOCK = ITEMS.register("ghasta_with_cream", () ->
            new PlaceableItem(MNDBlocks.GHASTA_WITH_CREAM_BLOCK.get(), basicItem().stacksTo(1))
    );
    public static final Supplier<Item> GHASTA_WITH_CREAM = ITEMS.register("plate_of_ghasta_with_cream", () ->
            new ConsumableItem(bowlFoodItem(MNDFoodValues.GHASTA_WITH_CREAM), true)
    );
    public static final Supplier<Item> GHAST_DOUGH = ITEMS.register("ghast_dough", () ->
            new ConsumableItem(foodItem(MNDFoodValues.GHASTA), false)
    );
    public static final Supplier<Item> GHAST_SOURDOUGH = ITEMS.register("ghast_sourdough", () ->
            new ConsumableItem(foodItem(MNDFoodValues.GHASTA), false)
    );
    public static final Supplier<Item> SLICES_OF_BREAD = ITEMS.register("slices_of_bread", () ->
            new ConsumableItem(foodItem(MNDFoodValues.SLICES_OF_BREAD))
    );
    public static final Supplier<Item> TOASTS = ITEMS.register("toasts", () ->
            new ConsumableItem(foodItem(MNDFoodValues.TOASTS))
    );
    public static final Supplier<Item> BREAD_LOAF_BLOCK = ITEMS.register("bread_loaf", () ->
            new PlaceableItem(MNDBlocks.BREAD_LOAF_BLOCK.get(), basicItem().stacksTo(16))
    );
    // SPICY
    public static final Supplier<Item> BULLET_PEPPER = ITEMS.register("bullet_pepper", () ->
            new ItemNameBlockItem(MNDBlocks.BULLET_PEPPER.get(),
                    new Item.Properties()
                            .food(MNDFoodValues.BULLET_PEPPER)
            )
    );
    public static final Supplier<Item> PEPPER_POWDER = ITEMS.register("pepper_powder", () ->
            new Item(basicItem())
    );
    public static final Supplier<Item> STUFFED_PEPPER = ITEMS.register("stuffed_pepper", ()  ->
            new ConsumableItem(foodItem(MNDFoodValues.STUFFED_PEPPER), false)
    );
    public static final Supplier<Item> SPICY_SKEWER = ITEMS.register("spicy_skewer", ()  ->
            new ConsumableItem(foodItem(MNDFoodValues.SPICY_SKEWER), false)
    );
    public static final Supplier<Item> CHILIDOG = ITEMS.register("chilidog", ()  ->
            new ConsumableItem(foodItem(MNDFoodValues.CHILIDOG), true)
    );
    public static final Supplier<Item> SPICY_HOGLIN_STEW = ITEMS.register("spicy_hoglin_stew", ()  ->
            new ConsumableItem(bowlFoodItem(MNDFoodValues.SPICY_HOGLIN_STEW), true)
    );
    public static final Supplier<Item> HOT_WINGS = ITEMS.register("hot_wings", ()  ->
            new ConsumableItem(bowlFoodItem(MNDFoodValues.HOT_WINGS), true)
    );
    public static final Supplier<Item> HOT_WINGS_BUCKET = ITEMS.register("hot_wings_bucket", ()  ->
            new ConsumableItem(bucketFoodItem(MNDFoodValues.HOT_WINGS_BUCKET), true)
    );
    public static final Supplier<Item> SPICY_CURRY = ITEMS.register("spicy_curry", ()  ->
            new ConsumableItem(bowlFoodItem(MNDFoodValues.SPICY_CURRY), true)
    );
    // MAGMA CUBE
    public static final Supplier<Item> ROCK_SOUP = ITEMS.register("rock_soup", ()  ->
            new ConsumableItem(bowlFoodItem(MNDFoodValues.ROCK_SOUP), true)
    );
    public static final Supplier<Item> BURNT_ROLL = ITEMS.register("burnt_roll", ()  ->
            new ConsumableItem(foodItem(MNDFoodValues.BURNT_ROLL).fireResistant(), false)
    );
    public static final Supplier<Item> MAGMA_CAKE = ITEMS.register("magma_cake_block", () ->
            new PlaceableItem(MNDBlocks.MAGMA_CAKE_BLOCK.get(), basicItem().stacksTo(1).fireResistant())
    );
    public static final Supplier<Item> MAGMA_CAKE_SLICE = ITEMS.register("magma_cake_slice",
            () -> new ConsumableItem(foodItem(MNDFoodValues.MAGMA_CAKE_SLICE).stacksTo(16).fireResistant(),
                    false, true)
    );
    // LAVA
    public static final Supplier<Item> HOT_CREAM = ITEMS.register("hot_cream", ()  ->
            new HotCreamItem(bucketFoodItem(MNDFoodValues.HOT_CREAM))
    );
    public static final Supplier<Item> HOT_CREAM_CONE = ITEMS.register("hot_cream_cone", ()  ->
            new HotCreamConeItem(foodItem(MNDFoodValues.HOT_CREAM_CONE).stacksTo(16))
    );
    // THOPHY
    public static final Supplier<Item> GOLDEN_TROPHY = ITEMS.register("golden_trophy", () ->
            new BlockItem(MNDBlocks.GOLDEN_TROPHY.get(), basicItem().rarity(Rarity.EPIC))
    );
    public static final Supplier<Item> HOGLIN_TROPHY = ITEMS.register("hoglin_trophy", () ->
            new BlockItem(MNDBlocks.HOGLIN_TROPHY.get(), basicItem())
    );
    public static final Supplier<Item> WAXED_HOGLIN_TROPHY = ITEMS.register("waxed_hoglin_trophy", () ->
            new BlockItem(MNDBlocks.WAXED_HOGLIN_TROPHY.get(), basicItem())
    );
    public static final Supplier<Item> ZOGLIN_TROPHY = ITEMS.register("zoglin_trophy", () ->
            new BlockItem(MNDBlocks.ZOGLIN_TROPHY.get(), basicItem())
    );
    public static final Supplier<Item> SKOGLIN_TROPHY = ITEMS.register("skoglin_trophy", () ->
            new BlockItem(MNDBlocks.SKOGLIN_TROPHY.get(), basicItem())
    );
    // STUFFED HOGLIN
    public static final Supplier<Item> HOGLIN_HIDE = ITEMS.register("hoglin_hide", () ->
            new Item(basicItem().rarity(Rarity.UNCOMMON))
    );
    public static final Supplier<Item> RAW_STUFFED_HOGLIN = ITEMS.register("raw_stuffed_hoglin", () ->
            new Item(basicItem().stacksTo(1).rarity(Rarity.UNCOMMON))
    );
    public static final Supplier<Item> ROAST_STUFFED_HOGLIN = ITEMS.register("roast_stuffed_hoglin", () ->
            new StuffedHoglinBlockItem(MNDBlocks.STUFFED_HOGLIN.get(),(basicItem().stacksTo(1)).rarity(Rarity.UNCOMMON))
    );
    public static final Supplier<Item> ROAST_EAR = ITEMS.register("roast_ear", ()  ->
            new ConsumableItem(foodItem(MNDFoodValues.ROAST_EAR).rarity(Rarity.UNCOMMON))
    );
    public static final Supplier<Item> PLATE_OF_STUFFED_HOGLIN_SNOUT = ITEMS.register("plate_of_stuffed_hoglin_snout", ()  ->
            new ConsumableItem(bowlFoodItem(MNDFoodValues.PLATE_OF_STUFFED_HOGLIN_SNOUT).rarity(Rarity.UNCOMMON), true)
    );
    public static final Supplier<Item> PLATE_OF_STUFFED_HOGLIN_HAM = ITEMS.register("plate_of_stuffed_hoglin_ham", ()  ->
            new ConsumableItem(bowlFoodItem(MNDFoodValues.PLATE_OF_STUFFED_HOGLIN_HAM).rarity(Rarity.UNCOMMON), true)
    );
    public static final Supplier<Item> PLATE_OF_STUFFED_HOGLIN = ITEMS.register("plate_of_stuffed_hoglin", ()  ->
            new ConsumableItem(bowlFoodItem(MNDFoodValues.PLATE_OF_STUFFED_HOGLIN).rarity(Rarity.UNCOMMON), true)
    );
}
