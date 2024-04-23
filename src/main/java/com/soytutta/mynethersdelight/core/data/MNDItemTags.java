//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.core.data;

import javax.annotation.Nullable;

import com.soytutta.mynethersdelight.common.registry.MNDBlocks;
import com.soytutta.mynethersdelight.common.tag.CompatibilityTags;
import com.soytutta.mynethersdelight.common.tag.MNDTags;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ForgeTags;
import vectorwing.farmersdelight.common.tag.ModTags;

public class MNDItemTags extends ItemTagsProvider {
    public MNDItemTags(DataGenerator generatorIn, BlockTagsProvider blockTagProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(generatorIn, blockTagProvider, modId, existingFileHelper);
    }

    protected void addTags() {
        this.registerModTags();
        this.registerForgeTags();
        this.registerMinecraftTags();
        this.registerCompatibilityTags();
    }

    private void registerModTags() {
        this.tag(MNDTags.NETHER_STOVE).add(MNDItems.NETHER_STOVE.get(),MNDItems.SOUL_NETHER_STOVE.get());
        this.tag(MNDTags.STOVE_SOUL_FUEL).addTag(ItemTags.SOUL_FIRE_BASE_BLOCKS);
        this.tag(MNDTags.STOVE_FIRE_FUEL).add(Items.BLAZE_ROD, Items.FIRE_CHARGE, Items.MAGMA_BLOCK, Items.MAGMA_CREAM).addTag(MNDTags.HOT_SPICE);
        this.tag(MNDTags.HOGLIN_WAXED).add(Items.NETHER_WART, Items.HONEYCOMB);
        this.tag(MNDTags.HOGLIN_CURE).add(Items.GHAST_TEAR);
        this.tag(MNDTags.CURRY_MEATS).add(Items.CHICKEN,ModItems.CHICKEN_CUTS.get(),Items.BEEF,ModItems.MINCED_BEEF.get(),Items.MUTTON,ModItems.MUTTON_CHOPS.get(),
                Items.PORKCHOP,ModItems.BACON.get(),ModItems.HAM.get(),MNDItems.HOGLIN_SAUSAGE.get(), MNDItems.HOGLIN_LOIN.get(),
                MNDItems.MINCED_STRIDER.get());
        this.tag(MNDTags.HOT_SPICE).add(Items.BLAZE_POWDER).addTag(MNDTags.BULLET_PEPPER);
        this.tag(MNDTags.STRIDER_MEATS).addTag(MNDTags.STRIDER_SLICE).addTag(MNDTags.MINCED_STRIDER);
        this.tag(MNDTags.STUFFED_HOGLIN_ITEMS).add(MNDItems.ROAST_EAR.get(), MNDItems.PLATE_OF_STUFFED_HOGLIN.get(),
                MNDItems.PLATE_OF_STUFFED_HOGLIN_HAM.get(), MNDItems.PLATE_OF_STUFFED_HOGLIN_SNOUT.get());
        this.tag(ModTags.CABBAGE_ROLL_INGREDIENTS).add((Item) MNDItems.STRIDER_SLICE.get(), MNDItems.MINCED_STRIDER.get(), MNDItems.HOGLIN_LOIN.get(), MNDItems.HOGLIN_SAUSAGE.get());
        this.tag(ModTags.WOLF_PREY).add((Item) MNDItems.MINCED_STRIDER.get(), MNDItems.HOGLIN_LOIN.get(), MNDItems.HOGLIN_SAUSAGE.get());
        this.tag(ModTags.WILD_CROPS_ITEM).add(MNDItems.BULLET_PEPPER.get(),MNDItems.POWDER_CANNON.get());
    }
    private void registerForgeTags() {
        this.tag(ForgeTags.EGGS).add((Item) MNDItems.STRIDER_EGG.get());
        this.tag(ForgeTags.RAW_FISHES).add((Item) MNDItems.STRIDER_SLICE.get());
        this.tag(ForgeTags.RAW_PORK).add((Item) MNDItems.HOGLIN_SAUSAGE.get());
    }
    private void registerMinecraftTags() {
        this.tag(ItemTags.SOUL_FIRE_BASE_BLOCKS).add(MNDItems.LETIOS_COMPOST.get(), MNDItems.RESURGENT_SOIL.get());
        this.tag(ItemTags.PIGLIN_FOOD).addTag(MNDTags.STUFFED_HOGLIN_ITEMS).addTag(MNDTags.COOKED_HOGLIN).addTag(MNDTags.RAW_HOGLIN);
    }

    public void registerCompatibilityTags() {
        this.tag(CompatibilityTags.HORROR_LASAGNA_MEATS).add((Item) MNDItems.MINCED_STRIDER.get(), MNDItems.HOGLIN_LOIN.get(), MNDItems.HOGLIN_SAUSAGE.get());
        this.tag(CompatibilityTags.RAW_MEATS).add((Item) MNDItems.MINCED_STRIDER.get(), MNDItems.HOGLIN_LOIN.get(), MNDItems.HOGLIN_SAUSAGE.get());
    }
}