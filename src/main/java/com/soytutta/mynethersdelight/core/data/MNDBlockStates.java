package com.soytutta.mynethersdelight.core.data;

import com.soytutta.mynethersdelight.MyNethersDelight;
import com.soytutta.mynethersdelight.common.block.NetherStoveBlock;
import com.soytutta.mynethersdelight.common.registry.MNDBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.block.CabinetBlock;
import vectorwing.farmersdelight.common.block.MushroomColonyBlock;

public class MNDBlockStates extends BlockStateProvider {
    private static final int DEFAULT_ANGLE_OFFSET = 180;

    public MNDBlockStates(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, "mynethersdelight", exFileHelper);
    }

    private String blockName(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block).getPath();
    }

    public ResourceLocation resourceBlock(String path) {
        return new ResourceLocation("mynethersdelight", "block/" + path);
    }

    protected void registerStatesAndModels() {
        this.simpleBlock( MNDBlocks.RESURGENT_SOIL.get(), this.cubeRandomRotation(MNDBlocks.RESURGENT_SOIL.get(), ""));
        this.stageBlock(MNDBlocks.CRIMSON_FUNGUS_COLONY.get(), MushroomColonyBlock.COLONY_AGE);
        this.stageBlock(MNDBlocks.WARPED_FUNGUS_COLONY.get(), MushroomColonyBlock.COLONY_AGE);
        this.cabinetBlock(MNDBlocks.NETHER_BRICKS_CABINET.get(), "nether_bricks");

        // POWDERY
        this.cabinetBlock(MNDBlocks.POWDERY_CABINET.get(), "powdery");
        this.logBlock(((RotatedPillarBlock) MNDBlocks.BLOCK_OF_POWDERY_CANNON.get()));
        this.blockItem(MNDBlocks.BLOCK_OF_POWDERY_CANNON);

        this.logBlock(((RotatedPillarBlock) MNDBlocks.BLOCK_OF_STRIPPED_POWDERY_CANNON.get()));
        this.blockItem(MNDBlocks.BLOCK_OF_STRIPPED_POWDERY_CANNON);

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
    }

    private void blockItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockItem(blockRegistryObject.get(), new ModelFile.UncheckedModelFile(MyNethersDelight.MODID +
                ":block/" + ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath()));
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
