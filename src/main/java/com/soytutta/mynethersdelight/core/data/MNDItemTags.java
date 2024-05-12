//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.core.data;

import javax.annotation.Nullable;

import com.soytutta.mynethersdelight.common.tag.CompatibilityTags;
import com.soytutta.mynethersdelight.common.tag.MNDTags;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ForgeTags;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.concurrent.CompletableFuture;

public class MNDItemTags extends ItemTagsProvider {
    public MNDItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, blockTagProvider, "mynethersdelight", existingFileHelper);
    }

    protected void addTags(HolderLookup.Provider provider) {
        this.registerModTags();
        this.registerForgeTags();
        this.registerMinecraftTags();
        this.registerCompatibilityTags();
    }

    private void registerModTags() {
        this.tag(MNDTags.NETHER_STOVE).add(MNDItems.NETHER_STOVE.get(),MNDItems.SOUL_NETHER_STOVE.get());

        this.tag(ModTags.WOODEN_CABINETS).add(MNDItems.POWDERY_CABINET.get());
        this.tag(ModTags.CABINETS).add(MNDItems.NETHER_BRICKS_CABINET.get());

        this.tag(MNDTags.BLOCK_OF_POWDERY).add(MNDItems.BLOCK_OF_POWDERY_CANNON.get(), MNDItems.BLOCK_OF_STRIPPED_POWDERY_CANNON.get());
        this.tag(MNDTags.POWDERY_BLOCKS).add(MNDItems.POWDERY_CABINET.get(),MNDItems.POWDERY_PLANKS.get(),MNDItems.POWDERY_PLANKS_STAIRS.get(),MNDItems.POWDERY_PLANKS_SLAB.get(),
                MNDItems.POWDERY_MOSAIC.get(),MNDItems.POWDERY_MOSAIC_STAIRS.get(),MNDItems.POWDERY_MOSAIC_SLAB.get(),MNDItems.POWDERY_FENCE.get(),MNDItems.POWDERY_FENCE_GATE.get(),
                MNDItems.POWDERY_DOOR.get(),MNDItems.POWDERY_TRAPDOOR.get(),  MNDItems.POWDERY_BUTTON.get(), MNDItems.POWDERY_PRESSURE_PLATE.get()).addTag(MNDTags.BLOCK_OF_POWDERY);

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
        this.tag(ModTags.CABBAGE_ROLL_INGREDIENTS).add( MNDItems.STRIDER_SLICE.get(), MNDItems.MINCED_STRIDER.get(), MNDItems.HOGLIN_LOIN.get(), MNDItems.HOGLIN_SAUSAGE.get());
        this.tag(ModTags.WOLF_PREY).add(MNDItems.MINCED_STRIDER.get(), MNDItems.HOGLIN_LOIN.get(), MNDItems.HOGLIN_SAUSAGE.get());
        this.tag(ModTags.WILD_CROPS_ITEM).add(MNDItems.BULLET_PEPPER.get(),MNDItems.POWDER_CANNON.get());
    }

    private void registerForgeTags() {
        this.tag(ForgeTags.COOKED_EGGS).add(MNDItems.BOILED_EGG.get());
        this.tag(ForgeTags.EGGS).add(MNDItems.STRIDER_EGG.get());
        this.tag(ForgeTags.PASTA_RAW_PASTA).add(MNDItems.GHASTA.get());
        this.tag(ForgeTags.PASTA).add(MNDItems.GHASTA.get());
        this.tag(ForgeTags.RAW_FISHES).add(MNDItems.STRIDER_SLICE.get());
        this.tag(ForgeTags.RAW_PORK).add(MNDItems.HOGLIN_SAUSAGE.get());
    }

    private void registerMinecraftTags() {
        this.tag(ItemTags.SOUL_FIRE_BASE_BLOCKS).add(MNDItems.LETIOS_COMPOST.get(), MNDItems.RESURGENT_SOIL.get());
        this.tag(ItemTags.PIGLIN_FOOD).addTag(MNDTags.STUFFED_HOGLIN_ITEMS).addTag(MNDTags.COOKED_HOGLIN).addTag(MNDTags.RAW_HOGLIN);
        this.tag(ItemTags.PLANKS).add(MNDItems.POWDERY_PLANKS.get());
        this.tag(ItemTags.WOODEN_BUTTONS).add(MNDItems.POWDERY_BUTTON.get());
        this.tag(ItemTags.WOODEN_PRESSURE_PLATES).add(MNDItems.POWDERY_PRESSURE_PLATE.get());
        this.tag(ItemTags.WOODEN_TRAPDOORS).add(MNDItems.POWDERY_TRAPDOOR.get());
        this.tag(ItemTags.WOODEN_DOORS).add(MNDItems.POWDERY_DOOR.get());
        this.tag(ItemTags.WOODEN_SLABS).add(MNDItems.POWDERY_PLANKS_SLAB.get());
        this.tag(ItemTags.WOODEN_STAIRS).add(MNDItems.POWDERY_PLANKS_STAIRS.get());
        this.tag(ItemTags.WOODEN_FENCES).add(MNDItems.POWDERY_FENCE.get());
        this.tag(ItemTags.NON_FLAMMABLE_WOOD).addTag(MNDTags.POWDERY_BLOCKS);

    }

    public void registerCompatibilityTags() {
        this.tag(CompatibilityTags.HORROR_LASAGNA_MEATS).add(MNDItems.MINCED_STRIDER.get(), MNDItems.HOGLIN_LOIN.get(), MNDItems.HOGLIN_SAUSAGE.get());
        this.tag(CompatibilityTags.RAW_MEATS).add(MNDItems.MINCED_STRIDER.get(), MNDItems.HOGLIN_LOIN.get(), MNDItems.HOGLIN_SAUSAGE.get());
    }
}