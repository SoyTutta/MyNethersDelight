//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.core.data;

import javax.annotation.Nullable;

import com.soytutta.mynethersdelight.common.tag.CompatibilityTags;
import com.soytutta.mynethersdelight.common.tag.MNDTags;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import com.soytutta.mynethersdelight.common.tag.MyCommonTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.tag.CommonTags;

import java.util.concurrent.CompletableFuture;

public class MNDItemTags extends ItemTagsProvider {
    public MNDItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, blockTagProvider, "mynethersdelight", existingFileHelper);
    }

    protected void addTags(HolderLookup.Provider provider) {
        this.registerMyCommonTags();
        this.registerCommonTags();
        this.registerModTags();
        this.registerMinecraftTags();
        this.registerCompatibilityTags();
    }

    private void registerMyCommonTags() {
        this.tag(MyCommonTags.FOODS_GIANT_TENTACLES).add(MNDItems.GHASTA.get());
        this.tag(MyCommonTags.FOODS_MAGMA_CUBE).add(MNDItems.MAGMA_CAKE.get(), MNDItems.MAGMA_CAKE_SLICE.get(), MNDItems.ROCK_SOUP.get(), MNDItems.BURNT_ROLL.get());
        this.tag(MyCommonTags.FOODS_RAW_STRIDER).addTag(MNDTags.STRIDER_SLICE).addTag(MNDTags.MINCED_STRIDER);
        this.tag(MyCommonTags.FOODS_RAW_HOGLIN).addTag(MNDTags.RAW_HOGLIN);
        this.tag(MyCommonTags.FOODS_COOKED_HOGLIN).addTag(MNDTags.COOKED_HOGLIN);
        this.tag(MyCommonTags.FOODS_RAW_SAUSAGE).add(MNDItems.HOGLIN_SAUSAGE.get());
        this.tag(MyCommonTags.FOODS_COOKED_SAUSAGE).add(MNDItems.ROASTED_SAUSAGE.get());
        this.tag(MyCommonTags.FOODS_BOILED_EGG).add(MNDItems.BOILED_EGG.get());
        this.tag(MyCommonTags.FOODS_RAW_GHAST).add(MNDItems.GHASTA.get(), MNDItems.GHASMATI.get());
        this.tag(MyCommonTags.FOODS_RICE_PASTA).add(MNDItems.GHASTA.get());
    }

    private void registerModTags() {
        this.tag(ModTags.Items.CABINETS_WOODEN).add(MNDItems.POWDERY_CABINET.get());
        this.tag(ModTags.Items.CABINETS).add(MNDItems.NETHER_BRICKS_CABINET.get(), MNDItems.RED_NETHER_BRICKS_CABINET.get(), MNDItems.BLACKSTONE_BRICKS_CABINET.get());
        this.tag(ModTags.Items.FEASTS).add(MNDItems.ROAST_STUFFED_HOGLIN.get(), MNDItems.STRIDERLOAF_BLOCK.get(),
                MNDItems.COLD_STRIDERLOAF_BLOCK.get(), MNDItems.GHASTA_WITH_CREAM_BLOCK.get(),
                MNDItems.BREAD_LOAF_BLOCK.get(), MNDItems.MAGMA_CAKE.get());

        this.tag(MNDTags.BLOCK_OF_POWDERY).add(MNDItems.BLOCK_OF_POWDERY_CANNON.get(), MNDItems.BLOCK_OF_STRIPPED_POWDERY_CANNON.get());

        this.tag(MNDTags.STOVE_SOUL_FUEL).addTag(ItemTags.SOUL_FIRE_BASE_BLOCKS);
        this.tag(MNDTags.STOVE_FIRE_FUEL).add(Items.BLAZE_ROD, Items.FIRE_CHARGE, Items.MAGMA_BLOCK, Items.MAGMA_CREAM).addTag(MNDTags.HOT_SPICE);
        this.tag(MNDTags.HOGLIN_WAXED).add(Items.NETHER_WART, Items.HONEYCOMB);
        this.tag(MNDTags.HOGLIN_CURE).add(Items.GHAST_TEAR);
        this.tag(MNDTags.BOILED_EGG_CANDIDATE).add(MNDItems.STRIDER_EGG.get());
        this.tag(MNDTags.CHILI_MEATS).addTag(MNDTags.MINCED_STRIDER).add(Items.BEEF,ModItems.MINCED_BEEF.get());
        this.tag(MNDTags.CURRY_MEATS).add(Items.CHICKEN,ModItems.CHICKEN_CUTS.get(),Items.BEEF,ModItems.MINCED_BEEF.get(),Items.MUTTON,ModItems.MUTTON_CHOPS.get(),
                Items.PORKCHOP,ModItems.BACON.get(),ModItems.HAM.get(),MNDItems.HOGLIN_SAUSAGE.get(), MNDItems.HOGLIN_LOIN.get(),
                MNDItems.MINCED_STRIDER.get());
        this.tag(MNDTags.HOT_SPICE).add(Items.BLAZE_POWDER, MNDItems.BULLET_PEPPER.get(), MNDItems.PEPPER_POWDER.get()).addTag(MNDTags.BULLET_PEPPER);
        this.tag(MNDTags.STRIDER_MEATS).addTag(MNDTags.STRIDER_SLICE).addTag(MNDTags.MINCED_STRIDER);
        this.tag(MNDTags.STUFFED_HOGLIN_ITEMS).add(MNDItems.ROAST_EAR.get(), MNDItems.PLATE_OF_STUFFED_HOGLIN.get(),
                MNDItems.PLATE_OF_STUFFED_HOGLIN_HAM.get(), MNDItems.PLATE_OF_STUFFED_HOGLIN_SNOUT.get());
        this.tag(CommonTags.Items.RAW_MEAT).add(MNDItems.MINCED_STRIDER.get(), MNDItems.HOGLIN_LOIN.get(), MNDItems.HOGLIN_SAUSAGE.get());
        this.tag(ModTags.Items.WILD_CROPS).add(MNDItems.BULLET_PEPPER.get(),MNDItems.POWDER_CANNON.get());
        this.tag(MNDTags.GHAST_MEATS).add(MNDItems.GHASMATI.get(), MNDItems.GHASTA.get());
    }

    private void registerCommonTags() {
        this.tag(Tags.Items.STORAGE_BLOCKS).addTag(MyCommonTags.Items.STORAGE_BLOCKS_BULLET_PEPPER);
        this.tag(MyCommonTags.Items.STORAGE_BLOCKS_BULLET_PEPPER).add(MNDItems.BULLET_PEPPER_CRATE.get());
        this.tag(MyCommonTags.Items.STRIPPED_LOGS).add(MNDItems.BLOCK_OF_STRIPPED_POWDERY_CANNON.get());
        this.tag(CommonTags.Items.COOKED_EGGS).addTag(MyCommonTags.FOODS_BOILED_EGG).add(MNDItems.GOLDEN_EGG.get(), MNDItems.ENCHANTED_GOLDEN_EGG.get());;
        this.tag(CommonTags.Items.EGGS).add(MNDItems.STRIDER_EGG.get());
        this.tag(CommonTags.Items.PASTA_RAW_PASTA).add(MNDItems.GHASTA.get());
        this.tag(CommonTags.Items.PASTA).add(MNDItems.GHASTA.get());
        this.tag(CommonTags.Items.DOUGH).add(MNDItems.GHAST_DOUGH.get());
        this.tag(CommonTags.Items.CROPS_RICE).add(MNDItems.GHASMATI.get());
        this.tag(CommonTags.Items.GRAIN_RICE).add(MNDItems.GHASMATI.get());
        this.tag(CommonTags.Items.RAW_FISHES).add(MNDItems.STRIDER_SLICE.get(), MNDItems.GHASTA.get());
        this.tag(CommonTags.Items.RAW_PORK).add(MNDItems.HOGLIN_SAUSAGE.get()).addTag(MyCommonTags.FOODS_RAW_HOGLIN);
        this.tag(CommonTags.Items.COOKED_PORK).addTag(MyCommonTags.FOODS_COOKED_HOGLIN);
        this.tag(CommonTags.Items.BREAD).add(MNDItems.SLICES_OF_BREAD.get(), MNDItems.TOASTS.get());
    }

    private void registerMinecraftTags() {
        this.tag(ItemTags.SOUL_FIRE_BASE_BLOCKS).add(MNDItems.LETIOS_COMPOST.get(), MNDItems.RESURGENT_SOIL.get(), MNDItems.RESURGENT_SOIL_FARMLAND.get());
        this.tag(ItemTags.PIGLIN_FOOD).addTag(MNDTags.STUFFED_HOGLIN_ITEMS).addTag(MNDTags.COOKED_HOGLIN).addTag(MNDTags.RAW_HOGLIN);
        this.tag(ItemTags.PIGLIN_LOVED).add(MNDItems.GOLDEN_EGG.get(), MNDItems.ENCHANTED_GOLDEN_EGG.get(), MNDItems.GOLDEN_TROPHY.get());
        this.tag(ItemTags.PLANKS).add(MNDItems.POWDERY_PLANKS.get());
        this.tag(ItemTags.WOODEN_BUTTONS).add(MNDItems.POWDERY_BUTTON.get());
        this.tag(ItemTags.WOODEN_PRESSURE_PLATES).add(MNDItems.POWDERY_PRESSURE_PLATE.get());
        this.tag(ItemTags.WOODEN_TRAPDOORS).add(MNDItems.POWDERY_TRAPDOOR.get());
        this.tag(ItemTags.WOODEN_DOORS).add(MNDItems.POWDERY_DOOR.get());
        this.tag(ItemTags.WOODEN_SLABS).add(MNDItems.POWDERY_PLANKS_SLAB.get());
        this.tag(ItemTags.SLABS).add(MNDItems.POWDERY_MOSAIC_SLAB.get());
        this.tag(ItemTags.WOODEN_STAIRS).add(MNDItems.POWDERY_PLANKS_STAIRS.get());
        this.tag(ItemTags.STAIRS).add(MNDItems.POWDERY_MOSAIC_STAIRS.get());
        this.tag(ItemTags.WOODEN_FENCES).add(MNDItems.POWDERY_FENCE.get());
        this.tag(ItemTags.SIGNS).add(MNDItems.POWDERY_SIGN.get());
        this.tag(ItemTags.HANGING_SIGNS).add(MNDItems.POWDERY_HANGING_SIGN.get());

        this.tag(ItemTags.NON_FLAMMABLE_WOOD).addTag(MNDTags.BLOCK_OF_POWDERY).add(MNDItems.POWDERY_PLANKS.get(),MNDItems.POWDERY_MOSAIC.get(),
                MNDItems.POWDERY_PLANKS_SLAB.get(),MNDItems.POWDERY_MOSAIC_SLAB.get(),MNDItems.POWDERY_PLANKS_STAIRS.get(),MNDItems.POWDERY_MOSAIC_STAIRS.get(),
                MNDItems.POWDERY_FENCE.get(),MNDItems.POWDERY_FENCE_GATE.get(),MNDItems.POWDERY_DOOR.get(),MNDItems.POWDERY_TRAPDOOR.get(),
                MNDItems.POWDERY_PRESSURE_PLATE.get(),MNDItems.POWDERY_BUTTON.get(),MNDItems.POWDERY_SIGN.get(),MNDItems.POWDERY_HANGING_SIGN.get());

    }

    public void registerCompatibilityTags() {
        this.tag(CompatibilityTags.HORROR_LASAGNA_MEATS).add(MNDItems.MINCED_STRIDER.get(), MNDItems.HOGLIN_LOIN.get(), MNDItems.HOGLIN_SAUSAGE.get());
        this.tag(CompatibilityTags.RAW_MEATS).add(MNDItems.MINCED_STRIDER.get(), MNDItems.HOGLIN_LOIN.get(), MNDItems.HOGLIN_SAUSAGE.get());
    }
}
