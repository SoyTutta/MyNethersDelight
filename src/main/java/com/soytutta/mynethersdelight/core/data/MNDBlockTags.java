package com.soytutta.mynethersdelight.core.data;

import com.soytutta.mynethersdelight.common.tag.MNDTags;
import com.soytutta.mynethersdelight.common.registry.MNDBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.tag.ModTags;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class MNDBlockTags extends BlockTagsProvider {
    public MNDBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, "mynethersdelight", existingFileHelper);
    }
    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.registerModTags();
        this.registerMinecraftTags();
        this.registerForgeTags();
        this.registerBlockMineables();
    }

    protected void registerBlockMineables() {
        this.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(MNDBlocks.LETIOS_COMPOST.get(), MNDBlocks.RESURGENT_SOIL.get(), MNDBlocks.RESURGENT_SOIL_FARMLAND.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(MNDBlocks.NETHER_BRICKS_CABINET.get(),MNDBlocks.NETHER_STOVE.get());
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(MNDBlocks.HOGLIN_TROPHY.get(),MNDBlocks.WAXED_HOGLIN_TROPHY.get(),MNDBlocks.ZOGLIN_TROPHY.get(),MNDBlocks.POWDERY_CANNON.get(),MNDBlocks.POWDERY_CANE.get(),MNDBlocks.POWDERY_CHUBBY_SAPLING.get(),MNDBlocks.BULLET_PEPPER.get(),MNDBlocks.BLOCK_OF_POWDERY_CANNON.get(),MNDBlocks.BLOCK_OF_STRIPPED_POWDERY_CANNON.get(),MNDBlocks.POWDERY_PLANKS.get(),MNDBlocks.POWDERY_PLANKS_SLAB.get(),MNDBlocks.POWDERY_PLANKS_STAIRS.get(),MNDBlocks.POWDERY_MOSAIC.get(),MNDBlocks.POWDERY_MOSAIC_STAIRS.get(),MNDBlocks.POWDERY_MOSAIC_SLAB.get(),MNDBlocks.POWDERY_FENCE.get(),MNDBlocks.POWDERY_FENCE_GATE.get(),MNDBlocks.POWDERY_TRAPDOOR.get(),MNDBlocks.POWDERY_DOOR.get(),MNDBlocks.POWDERY_BUTTON.get(),MNDBlocks.POWDERY_PRESSURE_PLATE.get(),MNDBlocks.POWDERY_WALL_SIGN.get(),MNDBlocks.POWDERY_SIGN.get(),MNDBlocks.POWDERY_WALL_HANGING_SIGN.get(),MNDBlocks.POWDERY_HANGING_SIGN.get(),MNDBlocks.POWDERY_CABINET.get());
        this.tag(ModTags.MINEABLE_WITH_KNIFE).add(MNDBlocks.POWDERY_CANNON.get(),MNDBlocks.POWDERY_CANE.get(),MNDBlocks.POWDERY_CHUBBY_SAPLING.get(),MNDBlocks.BULLET_PEPPER.get());
    }

    protected void registerMinecraftTags() {
        this.tag(BlockTags.BAMBOO_PLANTABLE_ON).add((Block)MNDBlocks.RESURGENT_SOIL.get(),MNDBlocks.POWDERY_CANNON.get());
        this.tag(BlockTags.HOGLIN_REPELLENTS).add((Block)MNDBlocks.HOGLIN_TROPHY.get(),MNDBlocks.WAXED_HOGLIN_TROPHY.get(),MNDBlocks.ZOGLIN_TROPHY.get(), MNDBlocks.BULLET_PEPPER.get());
        this.tag(BlockTags.PIGLIN_REPELLENTS).add((Block)MNDBlocks.ZOGLIN_TROPHY.get(), MNDBlocks.BULLET_PEPPER.get());
        this.tag(BlockTags.MUSHROOM_GROW_BLOCK).add(MNDBlocks.LETIOS_COMPOST.get(), MNDBlocks.RESURGENT_SOIL.get(), MNDBlocks.RESURGENT_SOIL_FARMLAND.get());
        this.tag(BlockTags.TALL_FLOWERS).add(MNDBlocks.POWDERY_CHUBBY_SAPLING.get(),MNDBlocks.POWDERY_CANE.get());
        this.tag(BlockTags.SMALL_FLOWERS).add(MNDBlocks.BULLET_PEPPER.get());
        this.tag(BlockTags.FENCES).add(MNDBlocks.POWDERY_FENCE.get());
        this.tag(BlockTags.FENCE_GATES).add(MNDBlocks.POWDERY_FENCE_GATE.get());
    }

    protected void registerForgeTags() {
        this.tag(BlockTags.CROPS).add(MNDBlocks.POWDERY_CANNON.get(),MNDBlocks.POWDERY_CHUBBY_SAPLING.get(),MNDBlocks.POWDERY_CANE.get(),MNDBlocks.BULLET_PEPPER.get());
        this.tag(BlockTags.SAND).add(MNDBlocks.RESURGENT_SOIL.get(), MNDBlocks.RESURGENT_SOIL_FARMLAND.get());
        this.tag(BlockTags.DIRT).add(MNDBlocks.RESURGENT_SOIL.get(), MNDBlocks.RESURGENT_SOIL_FARMLAND.get());
        this.tag(BlockTags.NYLIUM).add(MNDBlocks.LETIOS_COMPOST.get(), MNDBlocks.RESURGENT_SOIL.get(), MNDBlocks.RESURGENT_SOIL_FARMLAND.get());
        this.tag(BlockTags.MUSHROOM_GROW_BLOCK).add(MNDBlocks.LETIOS_COMPOST.get(), MNDBlocks.RESURGENT_SOIL.get(), MNDBlocks.RESURGENT_SOIL_FARMLAND.get());
        this.tag(BlockTags.INFINIBURN_NETHER).add(MNDBlocks.LETIOS_COMPOST.get(), MNDBlocks.RESURGENT_SOIL.get(), MNDBlocks.RESURGENT_SOIL_FARMLAND.get());
        this.tag(BlockTags.SOUL_FIRE_BASE_BLOCKS).add(MNDBlocks.LETIOS_COMPOST.get(), MNDBlocks.RESURGENT_SOIL.get(), MNDBlocks.RESURGENT_SOIL_FARMLAND.get());
        this.tag(BlockTags.SOUL_SPEED_BLOCKS).add(MNDBlocks.LETIOS_COMPOST.get(), MNDBlocks.RESURGENT_SOIL.get(), MNDBlocks.RESURGENT_SOIL_FARMLAND.get());
    }

    protected void registerModTags() {
        this.tag(ModTags.HEAT_SOURCES).add(MNDBlocks.NETHER_STOVE.get());
        this.tag(ModTags.WILD_CROPS).add(MNDBlocks.POWDERY_CANE.get());

        this.tag(MNDTags.SHOWCASE_ACTIVATORS).add(Blocks.WITHER_ROSE, Blocks.SOUL_SAND, MNDBlocks.LETIOS_COMPOST.get(), MNDBlocks.RESURGENT_SOIL.get(), Blocks.NETHER_WART, Blocks.CRIMSON_FUNGUS, MNDBlocks.CRIMSON_FUNGUS_COLONY.get(), Blocks.WARPED_FUNGUS, MNDBlocks.WARPED_FUNGUS_COLONY.get(), Blocks.RED_MUSHROOM, ModBlocks.RED_MUSHROOM_COLONY.get(), Blocks.BROWN_MUSHROOM, ModBlocks.BROWN_MUSHROOM_COLONY.get(), Blocks.TWISTING_VINES, Blocks.WEEPING_VINES, Blocks.BONE_BLOCK);
        this.tag(MNDTags.SHOWCASE_FLAMES).add(Blocks.MAGMA_BLOCK, Blocks.LANTERN, Blocks.TORCH, MNDBlocks.POWDERY_TORCH.get(), Blocks.SOUL_TORCH, Blocks.SOUL_LANTERN,Blocks.FURNACE,Blocks.SMOKER,Blocks.BLAST_FURNACE,ModBlocks.STOVE.get(),MNDBlocks.NETHER_STOVE.get()).addTag(net.minecraft.tags.BlockTags.CAMPFIRES).addTag(BlockTags.CANDLES);

        this.tag(MNDTags.LETIOS_ACTIVATORS).add(Blocks.TWISTING_VINES_PLANT, Blocks.WEEPING_VINES_PLANT).addTag(MNDTags.SHOWCASE_ACTIVATORS);
        this.tag(MNDTags.LETIOS_FLAMES).add(Blocks.LAVA_CAULDRON,MNDBlocks.POWDERY_CANE.get(),MNDBlocks.POWDERY_CANNON.get(),MNDBlocks.BULLET_PEPPER.get()).addTag(MNDTags.SHOWCASE_FLAMES).addTag(ModTags.HEAT_SOURCES).addTag(BlockTags.CANDLE_CAKES).addTag(net.minecraft.tags.BlockTags.FIRE).add(Blocks.LAVA);

        this.tag(MNDTags.POWDERY_CANNON_PLANTABLE_ON).add(Blocks.MAGMA_BLOCK,Blocks.GRAVEL,Blocks.NETHERRACK,Blocks.CRIMSON_NYLIUM,Blocks.WARPED_FUNGUS,MNDBlocks.POWDERY_CANNON.get(),MNDBlocks.POWDERY_CHUBBY_SAPLING.get()).addTag(BlockTags.SAND).addTag(BlockTags.DIRT).addTag(BlockTags.BASE_STONE_NETHER).addTag(BlockTags.SOUL_SPEED_BLOCKS);
        this.tag(MNDTags.POWDERY_CANE).add(MNDBlocks.POWDERY_CANNON.get(),MNDBlocks.POWDERY_CANE.get());
    }
}