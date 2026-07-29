package com.soytutta.mynethersdelight.common.tag;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class MyCommonTags {
    public static final TagKey<Item> FOODS_RICE_PASTA = commonItemTag("foods/rice_pasta");
    public static final TagKey<Item> FOODS_RAW_GHAST = commonItemTag("foods/raw_ghast");
    public static final TagKey<Item> FOODS_RAW_HOGLIN = commonItemTag("foods/raw_hoglin");
    public static final TagKey<Item> FOODS_COOKED_HOGLIN = commonItemTag("foods/cooked_hoglin");
    public static final TagKey<Item> FOODS_COOKED_SAUSAGE = commonItemTag("foods/cooked_sausage");
    public static final TagKey<Item> FOODS_RAW_SAUSAGE = commonItemTag("foods/raw_sausage");
    public static final TagKey<Item> FOODS_MAGMA_CUBE = commonItemTag("foods/magma_cube");
    public static final TagKey<Item> FOODS_BOILED_EGG = commonItemTag("foods/boiled_egg");

    public static final TagKey<Item> FOODS_GIANT_TENTACLES = commonItemTag("foods/giant_tentacles");

    public static final TagKey<Item> FOODS_RAW_STRIDER = commonItemTag("foods/raw_strider");

    public static class Blocks {
        public static final TagKey<Block> STORAGE_BLOCKS_BULLET_PEPPER = commonBlockTag("storage_blocks/bullet_pepper");
    }

    public static class Items {
        public static final TagKey<Item> STORAGE_BLOCKS_BULLET_PEPPER = commonItemTag("storage_blocks/bullet_pepper");
    }

    private static TagKey<Block> commonBlockTag(String path) {
        return BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", path));
    }

    private static TagKey<Item> commonItemTag(String path) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", path));
    }
}