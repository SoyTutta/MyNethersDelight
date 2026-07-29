package com.soytutta.mynethersdelight.core.data;

import com.soytutta.mynethersdelight.common.tag.MNDTags;
import com.soytutta.mynethersdelight.common.registry.MNDBlocks;
import com.soytutta.mynethersdelight.common.tag.MyCommonTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
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
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(MNDBlocks.BLAZIER_BLOCK.get(),MNDBlocks.NETHER_BRICKS_CABINET.get(),MNDBlocks.RED_NETHER_BRICKS_CABINET.get(),MNDBlocks.BLACKSTONE_BRICKS_CABINET.get(),MNDBlocks.NETHER_STOVE.get(),MNDBlocks.GOLDEN_TROPHY.get());
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(MNDBlocks.HOGLIN_TROPHY.get(),MNDBlocks.WAXED_HOGLIN_TROPHY.get(),MNDBlocks.ZOGLIN_TROPHY.get(),MNDBlocks.SKOGLIN_TROPHY.get(),MNDBlocks.GOLDEN_TROPHY.get(),MNDBlocks.POWDERY_CANNON.get(),MNDBlocks.POWDERY_CANE.get(),MNDBlocks.POWDERY_CHUBBY_SAPLING.get(),MNDBlocks.BULLET_PEPPER.get(),MNDBlocks.POWDERY_CABINET.get(),MNDBlocks.BLOCK_OF_POWDERY_CANNON.get(),MNDBlocks.BLOCK_OF_STRIPPED_POWDERY_CANNON.get(),MNDBlocks.POWDERY_MOSAIC.get(),MNDBlocks.POWDERY_MOSAIC_STAIRS.get(),MNDBlocks.POWDERY_MOSAIC_SLAB.get(),MNDBlocks.BULLET_PEPPER_CRATE.get());
        this.tag(ModTags.Blocks.MINEABLE_WITH_KNIFE).add(MNDBlocks.STUFFED_HOGLIN.get(),MNDBlocks.BREAD_LOAF_BLOCK.get(),MNDBlocks.GHASTA_WITH_CREAM_BLOCK.get(),MNDBlocks.STRIDERLOAF_BLOCK.get(),MNDBlocks.MAGMA_CAKE_BLOCK.get(),MNDBlocks.POWDERY_CANNON.get(),MNDBlocks.POWDERY_CANE.get(),MNDBlocks.POWDERY_CHUBBY_SAPLING.get(),MNDBlocks.BULLET_PEPPER.get());
    }

    protected void registerMinecraftTags() {
        this.tag(BlockTags.FLOWER_POTS).add(MNDBlocks.POTTED_BULLET_PEPPER.get(),MNDBlocks.POTTED_POWDERY_CANNON.get());
        this.tag(BlockTags.PLANKS).add(MNDBlocks.POWDERY_PLANKS.get());
        this.tag(BlockTags.SLABS).add(MNDBlocks.POWDERY_MOSAIC_SLAB.get());
        this.tag(BlockTags.WOODEN_SLABS).add(MNDBlocks.POWDERY_PLANKS_SLAB.get());
        this.tag(BlockTags.STAIRS).add(MNDBlocks.POWDERY_MOSAIC_STAIRS.get());
        this.tag(BlockTags.WOODEN_STAIRS).add(MNDBlocks.POWDERY_PLANKS_STAIRS.get());
        this.tag(BlockTags.WOODEN_FENCES).add(MNDBlocks.POWDERY_FENCE.get());
        this.tag(BlockTags.FENCE_GATES).add(MNDBlocks.POWDERY_FENCE_GATE.get());
        this.tag(BlockTags.WOODEN_DOORS).add(MNDBlocks.POWDERY_DOOR.get());
        this.tag(BlockTags.WOODEN_TRAPDOORS).add(MNDBlocks.POWDERY_TRAPDOOR.get());
        this.tag(BlockTags.WOODEN_PRESSURE_PLATES).add(MNDBlocks.POWDERY_PRESSURE_PLATE.get());
        this.tag(BlockTags.WOODEN_BUTTONS).add(MNDBlocks.POWDERY_BUTTON.get());
        this.tag(BlockTags.SIGNS).add(MNDBlocks.POWDERY_WALL_SIGN.get());
        this.tag(BlockTags.STANDING_SIGNS).add(MNDBlocks.POWDERY_SIGN.get());
        this.tag(BlockTags.WALL_HANGING_SIGNS).add(MNDBlocks.POWDERY_WALL_HANGING_SIGN.get());
        this.tag(BlockTags.CEILING_HANGING_SIGNS).add(MNDBlocks.POWDERY_HANGING_SIGN.get());
        this.tag(BlockTags.BAMBOO_PLANTABLE_ON).add(MNDBlocks.RESURGENT_SOIL.get(),MNDBlocks.POWDERY_CANNON.get());
        this.tag(BlockTags.HOGLIN_REPELLENTS).add(MNDBlocks.HOGLIN_TROPHY.get(),MNDBlocks.WAXED_HOGLIN_TROPHY.get(),MNDBlocks.ZOGLIN_TROPHY.get(), MNDBlocks.BULLET_PEPPER.get(), MNDBlocks.WARPED_FUNGUS_COLONY.get());
        this.tag(BlockTags.PIGLIN_REPELLENTS).add(MNDBlocks.ZOGLIN_TROPHY.get(), MNDBlocks.BULLET_PEPPER.get());
        this.tag(BlockTags.GUARDED_BY_PIGLINS).add(MNDBlocks.GOLDEN_TROPHY.get());
        this.tag(BlockTags.WITHER_SUMMON_BASE_BLOCKS).add(MNDBlocks.RESURGENT_SOIL.get());

        this.tag(BlockTags.MUSHROOM_GROW_BLOCK).add(MNDBlocks.LETIOS_COMPOST.get(), MNDBlocks.RESURGENT_SOIL.get(), MNDBlocks.RESURGENT_SOIL_FARMLAND.get());
        this.tag(BlockTags.SMALL_FLOWERS).add(MNDBlocks.BULLET_PEPPER.get());
        this.tag(BlockTags.FENCES).add(MNDBlocks.POWDERY_FENCE.get());
    }

    protected void registerForgeTags() {
        this.tag(Tags.Blocks.STORAGE_BLOCKS).addTag(MyCommonTags.Blocks.STORAGE_BLOCKS_BULLET_PEPPER);
        this.tag(MyCommonTags.Blocks.STORAGE_BLOCKS_BULLET_PEPPER).add(MNDBlocks.BULLET_PEPPER_CRATE.get());
        this.tag(Tags.Blocks.STRIPPED_LOGS).add(MNDBlocks.BLOCK_OF_STRIPPED_POWDERY_CANNON.get());
        this.tag(BlockTags.DIRT).add(MNDBlocks.RESURGENT_SOIL.get(), MNDBlocks.RESURGENT_SOIL_FARMLAND.get());
        this.tag(BlockTags.NYLIUM).add(MNDBlocks.LETIOS_COMPOST.get(), MNDBlocks.RESURGENT_SOIL.get(), MNDBlocks.RESURGENT_SOIL_FARMLAND.get());
        this.tag(BlockTags.INFINIBURN_NETHER).add(MNDBlocks.LETIOS_COMPOST.get(), MNDBlocks.RESURGENT_SOIL.get(), MNDBlocks.RESURGENT_SOIL_FARMLAND.get());
        this.tag(BlockTags.SOUL_FIRE_BASE_BLOCKS).add(MNDBlocks.LETIOS_COMPOST.get(), MNDBlocks.RESURGENT_SOIL.get(), MNDBlocks.RESURGENT_SOIL_FARMLAND.get());
        this.tag(BlockTags.SOUL_SPEED_BLOCKS).add(MNDBlocks.LETIOS_COMPOST.get(), MNDBlocks.RESURGENT_SOIL.get(), MNDBlocks.RESURGENT_SOIL_FARMLAND.get());
    }

    protected void registerModTags() {
        this.tag(MNDTags.Blocks.POWDERY_BLOCKS).add(MNDBlocks.BLOCK_OF_POWDERY_CANNON.get(), MNDBlocks.BLOCK_OF_STRIPPED_POWDERY_CANNON.get());
        this.tag(MNDTags.BELOW_PROPAGATE_PLANT).add(Blocks.SPORE_BLOSSOM,Blocks.HANGING_ROOTS,Blocks.GLOW_LICHEN);
        this.tag(MNDTags.ABOVE_PROPAGATE_PLANT).add(Blocks.KELP,Blocks.LILY_PAD,Blocks.DEAD_BUSH,Blocks.GLOW_LICHEN).addTag(BlockTags.SAPLINGS).addTag(BlockTags.CROPS);

        this.tag(MNDTags.RESURGENT_SOIL_PLANT).add(Blocks.CACTUS)
                .addTag(MNDTags.ABOVE_PROPAGATE_PLANT).addTag(MNDTags.BELOW_PROPAGATE_PLANT).addTag(BlockTags.MINEABLE_WITH_HOE).addTag(BlockTags.SWORD_EFFICIENT);

        this.tag(ModTags.Blocks.CABINETS).add(MNDBlocks.NETHER_BRICKS_CABINET.get(), MNDBlocks.RED_NETHER_BRICKS_CABINET.get(), MNDBlocks.BLACKSTONE_BRICKS_CABINET.get());
        this.tag(ModTags.Blocks.CABINETS_WOODEN).add(MNDBlocks.POWDERY_CABINET.get());
        this.tag(ModTags.Blocks.TRAY_HEAT_SOURCES).add(MNDBlocks.MAGMA_CAKE_BLOCK.get(), MNDBlocks.BLAZIER_BLOCK.get());
        this.tag(ModTags.Blocks.HEAT_SOURCES).add(MNDBlocks.NETHER_STOVE.get(),MNDBlocks.BULLET_PEPPER_CRATE.get());
        this.tag(ModTags.Blocks.WILD_CROPS).add(MNDBlocks.BULLET_PEPPER.get());
        this.tag(ModTags.Blocks.UNAFFECTED_BY_RICH_SOIL).add(MNDBlocks.WARPED_FUNGUS_COLONY.get(),MNDBlocks.CRIMSON_FUNGUS_COLONY.get());
        this.tag(ModTags.Blocks.MUSHROOM_COLONY_GROWABLE_ON).add(MNDBlocks.LETIOS_COMPOST.get(), MNDBlocks.RESURGENT_SOIL.get());
        this.tag(ModTags.Blocks.FEASTS).add(MNDBlocks.STUFFED_HOGLIN.get(), MNDBlocks.STRIDERLOAF_BLOCK.get(), MNDBlocks.GHASTA_WITH_CREAM_BLOCK.get(), MNDBlocks.MAGMA_CAKE_BLOCK.get(), MNDBlocks.BREAD_LOAF_BLOCK.get());

        this.tag(MNDTags.SHOWCASE_ACTIVATORS).add(Blocks.WITHER_ROSE, Blocks.SOUL_SAND, MNDBlocks.LETIOS_COMPOST.get(), MNDBlocks.RESURGENT_SOIL.get(), Blocks.NETHER_WART, Blocks.CRIMSON_FUNGUS, MNDBlocks.CRIMSON_FUNGUS_COLONY.get(), Blocks.WARPED_FUNGUS, MNDBlocks.WARPED_FUNGUS_COLONY.get(), Blocks.RED_MUSHROOM, ModBlocks.RED_MUSHROOM_COLONY.get(), Blocks.BROWN_MUSHROOM, ModBlocks.BROWN_MUSHROOM_COLONY.get(), Blocks.TWISTING_VINES, Blocks.WEEPING_VINES, Blocks.BONE_BLOCK);
        this.tag(MNDTags.SHOWCASE_FLAMES).add(Blocks.MAGMA_BLOCK,MNDBlocks.MAGMA_CAKE_BLOCK.get(), Blocks.LANTERN, Blocks.TORCH, MNDBlocks.POWDERY_TORCH.get(), Blocks.SOUL_TORCH, Blocks.SOUL_LANTERN,Blocks.FURNACE,Blocks.SMOKER,Blocks.BLAST_FURNACE,ModBlocks.STOVE.get(),MNDBlocks.NETHER_STOVE.get()).addTag(net.minecraft.tags.BlockTags.CAMPFIRES).addTag(BlockTags.CANDLES);

        this.tag(MNDTags.LETIOS_ACTIVATORS).add(Blocks.TWISTING_VINES_PLANT, Blocks.WEEPING_VINES_PLANT).addTag(MNDTags.SHOWCASE_ACTIVATORS);
        this.tag(MNDTags.LETIOS_FLAMES).add(Blocks.LAVA_CAULDRON,MNDBlocks.POWDERY_CANE.get(),MNDBlocks.POWDERY_CANNON.get(),MNDBlocks.BULLET_PEPPER.get(),MNDBlocks.GHASTA_WITH_CREAM_BLOCK.get(),MNDBlocks.POTTED_BULLET_PEPPER.get(),MNDBlocks.WALL_POWDERY_TORCH.get(),Blocks.WALL_TORCH).addTag(MNDTags.SHOWCASE_FLAMES).addTag(ModTags.Blocks.HEAT_SOURCES).addTag(BlockTags.CANDLE_CAKES).addTag(net.minecraft.tags.BlockTags.FIRE).add(Blocks.LAVA);

        this.tag(MNDTags.POWDERY_CANNON_PLANTABLE_ON).add(Blocks.MAGMA_BLOCK,MNDBlocks.POWDERY_CANNON.get(),MNDBlocks.POWDERY_CHUBBY_SAPLING.get()).addTag(Tags.Blocks.GRAVELS).addTag(Tags.Blocks.NETHERRACKS).addTag(BlockTags.NYLIUM).addTag(BlockTags.SAND).addTag(BlockTags.DIRT).addTag(BlockTags.BASE_STONE_NETHER).addTag(BlockTags.SOUL_SPEED_BLOCKS);
        this.tag(MNDTags.POWDERY_CANE).add(MNDBlocks.POWDERY_CANNON.get(),MNDBlocks.POWDERY_CANE.get(),MNDBlocks.BULLET_PEPPER.get());
    }
}
