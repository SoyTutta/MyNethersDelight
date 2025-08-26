package com.soytutta.mynethersdelight.integration.MinerDelight;

import com.sammy.minersdelight.content.item.*;
import com.soytutta.mynethersdelight.common.utility.MNDFoodValues;
import net.minecraft.core.registries.*;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.*;
import static com.sammy.minersdelight.setup.MDItems.registerCupFood;

public class MDItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, "minersdelight");

    // Cups
    public static final DeferredHolder<Item, CopperCupFoodItem> ROCK_SOUP_CUP = registerCupFood("rock_soup_cup", MNDFoodValues.ROCK_SOUP);
    public static final DeferredHolder<Item, CopperCupFoodItem> SPICY_HOGLIN_STEW_CUP = registerCupFood("spicy_hoglin_stew_cup", MNDFoodValues.SPICY_HOGLIN_STEW);
    public static final DeferredHolder<Item, CopperCupFoodItem> SPICY_NOODLE_SOUP_CUP = registerCupFood("spicy_noodle_soup_cup", MNDFoodValues.SPICY_NOODLE_SOUP);
    public static final DeferredHolder<Item, CopperCupFoodItem> STRIDER_STEW_CUP = registerCupFood("strider_stew_cup", MNDFoodValues.STRIDER_STEW);
    public static final DeferredHolder<Item, CopperCupFoodItem> EGG_SOUP_CUP = registerCupFood("egg_soup_cup", MNDFoodValues.EGG_SOUP);
}