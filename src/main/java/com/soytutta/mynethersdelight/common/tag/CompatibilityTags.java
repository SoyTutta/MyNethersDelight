package com.soytutta.mynethersdelight.common.tag;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class CompatibilityTags {
    public static final TagKey<Item> HORROR_LASAGNA_MEATS = externalItemTag("brewinandchewin", "horror_lasagna_meats");
    public static final TagKey<Item> RAW_MEATS = externalItemTag("brewinandchewin", "horror_lasagna_meats");

    public CompatibilityTags() {
    }

    private static TagKey<Item> externalItemTag(String modId, String path) {
        return ItemTags.create(new ResourceLocation(modId, path));
    }
}
