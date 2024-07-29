package com.soytutta.mynethersdelight.core.data;

import com.soytutta.mynethersdelight.common.registry.MNDBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import vectorwing.farmersdelight.common.block.*;

public class MNDBlockStates extends BlockStateProvider {

    public MNDBlockStates(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, "mynethersdelight", existingFileHelper);
    }

    private String blockName(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }

    public ResourceLocation resourceBlock(String path) {
        return ResourceLocation.fromNamespaceAndPath("mynethersdelight", "block/" + path);
    }

    protected void registerStatesAndModels() {
        this.simpleBlock(MNDBlocks.RESURGENT_SOIL.get(), cubeRandomRotation(MNDBlocks.RESURGENT_SOIL.get(), ""));
        this.stageBlock(MNDBlocks.CRIMSON_FUNGUS_COLONY.get(), MushroomColonyBlock.COLONY_AGE);
        this.stageBlock(MNDBlocks.WARPED_FUNGUS_COLONY.get(), MushroomColonyBlock.COLONY_AGE);
        this.cabinetBlock(MNDBlocks.NETHER_BRICKS_CABINET.get(), "nether_bricks");

        // POWDERY
        this.crateBlock(MNDBlocks.BULLET_PEPPER_CRATE.get(), "bullet_pepper");
        this.cabinetBlock(MNDBlocks.POWDERY_CABINET.get(), "powdery");
        this.logBlock(((RotatedPillarBlock) MNDBlocks.BLOCK_OF_POWDERY_CANNON.get()));
        this.logBlock(((RotatedPillarBlock) MNDBlocks.BLOCK_OF_STRIPPED_POWDERY_CANNON.get()));
        this.simpleBlock(MNDBlocks.POWDERY_PLANKS.get());

        this.stairsBlock(((StairBlock) MNDBlocks.POWDERY_PLANKS_STAIRS.get()), blockTexture(MNDBlocks.POWDERY_PLANKS.get()));
        this.slabBlock(((SlabBlock) MNDBlocks.POWDERY_PLANKS_SLAB.get()), blockTexture(MNDBlocks.POWDERY_PLANKS.get()), blockTexture(MNDBlocks.POWDERY_PLANKS.get()));
        this.doorBlockWithRenderType(((DoorBlock) MNDBlocks.POWDERY_DOOR.get()), modLoc("block/powdery_door_bottom"), modLoc("block/powdery_door_top"), "cutout");
        this.trapdoorBlockWithRenderType(((TrapDoorBlock) MNDBlocks.POWDERY_TRAPDOOR.get()), modLoc("block/powdery_trapdoor"), true, "cutout");
        this.buttonBlock(((ButtonBlock) MNDBlocks.POWDERY_BUTTON.get()), blockTexture(MNDBlocks.POWDERY_PLANKS.get()));
        this.pressurePlateBlock(((PressurePlateBlock) MNDBlocks.POWDERY_PRESSURE_PLATE.get()), blockTexture(MNDBlocks.POWDERY_PLANKS.get()));

        this.simpleBlock(MNDBlocks.POWDERY_MOSAIC.get());
        this.stairsBlock(((StairBlock) MNDBlocks.POWDERY_MOSAIC_STAIRS.get()), blockTexture(MNDBlocks.POWDERY_MOSAIC.get()));
        this.slabBlock(((SlabBlock) MNDBlocks.POWDERY_MOSAIC_SLAB.get()), blockTexture(MNDBlocks.POWDERY_MOSAIC.get()), blockTexture(MNDBlocks.POWDERY_MOSAIC.get()));

        this.signBlock(((StandingSignBlock) MNDBlocks.POWDERY_SIGN.get()), ((WallSignBlock) MNDBlocks.POWDERY_WALL_SIGN.get()),
                blockTexture(MNDBlocks.POWDERY_PLANKS.get()));
        this.hangingSignBlock(MNDBlocks.POWDERY_HANGING_SIGN.get(), MNDBlocks.POWDERY_WALL_HANGING_SIGN.get(),
                blockTexture(MNDBlocks.POWDERY_PLANKS.get()));

        // FEAST
        this.feastBlock((FeastBlock) MNDBlocks.GHASTA_WITH_CREAM_BLOCK.get());
        this.feastBlock((FeastBlock) MNDBlocks.STRIDERLOAF_BLOCK.get());
        this.feastBlock((FeastBlock) MNDBlocks.COLD_STRIDERLOAF_BLOCK.get());

    }

    public void feastBlock(FeastBlock block) {
        this.getVariantBuilder(block).forAllStates((state) -> {
            IntegerProperty servingsProperty = block.getServingsProperty();
            int servings = state.getValue(servingsProperty);
            String suffix = "_stage" + (block.getMaxServings() - servings);
            if (servings == 0) {
                suffix = block.hasLeftovers ? "_leftover" : "_stage" + (servingsProperty.getPossibleValues().toArray().length - 2);
            }

            ConfiguredModel.Builder var10000 = ConfiguredModel.builder();
            String var10002 = this.blockName(block);
            return var10000.modelFile(this.existingModel(var10002 + suffix)).rotationY(((int)(state.getValue(FeastBlock.FACING)).toYRot() + 180) % 360).build();
        });
    }

    public ModelFile existingModel(String path) {
        return new ModelFile.ExistingModelFile(this.resourceBlock(path), this.models().existingFileHelper);
    }

    public void hangingSignBlock(Block signBlock, Block wallSignBlock, ResourceLocation texture) {
        ModelFile sign = models().sign(blockName(signBlock), texture);
        hangingSignBlock(signBlock, wallSignBlock, sign);
    }

    public void hangingSignBlock(Block signBlock, Block wallSignBlock, ModelFile sign) {
        simpleBlock(signBlock, sign);
        simpleBlock(wallSignBlock, sign);
    }

    public void crateBlock(Block block, String cropName) {
        this.simpleBlock(block, this.models().cubeBottomTop(this.blockName(block), this.resourceBlock(cropName + "_crate_side"), this.resourceBlock(cropName + "_crate_bottom"), this.resourceBlock(cropName + "_crate_top")));
    }

    public ConfiguredModel[] cubeRandomRotation(Block block, String suffix) {
        String var10000 = this.blockName(block);
        String formattedName = var10000 + (suffix.isEmpty() ? "" : "_" + suffix);
        return ConfiguredModel.allYRotations(this.models().cubeAll(formattedName, this.resourceBlock(formattedName)), 0, false);
    }

    public void stageBlock(Block block, IntegerProperty ageProperty, Property<?>... ignored) {
        this.getVariantBuilder(block).forAllStatesExcept((state) -> {
            int ageSuffix = state.getValue(ageProperty);
            String blockName = this.blockName(block);
            String stageName = blockName + "_stage" + ageSuffix;
            return ConfiguredModel.builder().modelFile((this.models().cross(stageName, this.resourceBlock(stageName))).renderType("cutout")).build();
        }, ignored);
    }
    public void cabinetBlock(Block block, String woodType) {
        this.horizontalBlock(block, (state) -> {
            String suffix = state.getValue(CabinetBlock.OPEN) ? "_open" : "";
            return this.models().orientable(this.blockName(block) + suffix, this.resourceBlock(woodType + "_cabinet_side"), this.resourceBlock(woodType + "_cabinet_front" + suffix), this.resourceBlock(woodType + "_cabinet_top"));
        });
    }
}
