package com.soytutta.mynethersdelight.common.tag;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class MNDTags {
    public static final TagKey<Item> NETHER_STOVE = modItemTag("nether_stove");
    public static final TagKey<Item> BLOCK_OF_POWDERY = modItemTag("block_of_powdery");
    public static final TagKey<Item> POWDERY_BLOCKS = modItemTag("powdery_blocks");

    public static final TagKey<Block> LETIOS_ACTIVATORS = modBlockTag("letios_activators");
    public static final TagKey<Block> LETIOS_FLAMES = modBlockTag("letios_flames");
    public static final TagKey<Block> SHOWCASE_ACTIVATORS = modBlockTag("showcase_activators");
    public static final TagKey<Block> SHOWCASE_FLAMES = modBlockTag("showcase_flames");

    public static final TagKey<Item> STOVE_SOUL_FUEL = modItemTag("stove_soul_fuel");
    public static final TagKey<Item> STOVE_FIRE_FUEL = modItemTag("stove_fire_fuel");
    public static final TagKey<Item> CURRY_MEATS = modItemTag("curry_meats");
    public static final TagKey<Item> HOT_SPICE = modItemTag("hot_spice");
    public static final TagKey<Item> BULLET_PEPPER = modItemTag("bullet_pepper");
    public static final TagKey<Item> STRIDER_MEATS = modItemTag("strider_meats");
    public static final TagKey<Item> STRIDER_SLICE = modItemTag("strider_slice");
    public static final TagKey<Item> MINCED_STRIDER = modItemTag("minced_strider");
    public static final TagKey<Item> HOGLIN_CURE = modItemTag("hoglin_cure");
    public static final TagKey<Item> HOGLIN_WAXED = modItemTag("hoglin_waxed");
    public static final TagKey<Item> HOGLIN_HIDE = modItemTag("hoglin_hide");
    public static final TagKey<Item> STUFFED_HOGLIN = modItemTag("stuffed_hoglin");
    public static final TagKey<Item> RAW_HOGLIN = modItemTag("raw_hoglin");
    public static final TagKey<Item> COOKED_HOGLIN = modItemTag("cooked_hoglin");
    public static final TagKey<Item> COOKED_HOGLIN_LOIN = modItemTag("cooked_hoglin_loin");
    public static final TagKey<Item> RAW_HOGLIN_LOIN = modItemTag("raw_hoglin_loin");
    public static final TagKey<Item> LOIN_HOGLIN = modItemTag("loin_hoglin");
    public static final TagKey<Item> STUFFED_HOGLIN_ITEMS = modItemTag("stuffed_hoglin_items");
    public static final TagKey<Item> CRIMSON_COLONY = modItemTag("crimson_colony");
    public static final TagKey<Item> WARPED_COLONY = modItemTag("warped_colony");
    public static final TagKey<Item> POWDER_CANNON = modItemTag("powder_cannon");
    public static final TagKey<Block> POWDERY_CANNON_PLANTABLE_ON = modBlockTag("powdery_cannon_plantable_on");
    public static final TagKey<Block> POWDERY_CANE = modBlockTag("powdery_cane");

    public MNDTags() {}

    private static TagKey<Item> modItemTag(String path) {
        return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("mynethersdelight:" + path));
    }
    private static TagKey<Block> modBlockTag(String path) {
        return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation("mynethersdelight:" + path));
    }

}