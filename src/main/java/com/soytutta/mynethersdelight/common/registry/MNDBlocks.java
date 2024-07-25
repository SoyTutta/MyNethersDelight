//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.common.registry;

import com.soytutta.mynethersdelight.common.block.*;
import com.soytutta.mynethersdelight.common.block.PowderyCannonSaplingBlock;
import com.soytutta.mynethersdelight.common.block.MNDStandingSignBlock;
import com.soytutta.mynethersdelight.common.block.MNDWallSignBlock;
import com.soytutta.mynethersdelight.common.block.utility.MNDWoodTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.block.*;

import java.util.function.ToIntFunction;

public class MNDBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "mynethersdelight");

    public static final RegistryObject<Block> NETHER_BRICKS_CABINET;
    public static final RegistryObject<Block> NETHER_STOVE;
    public static final RegistryObject<Block> BULLET_PEPPER_CRATE;
    public static final RegistryObject<Block> LETIOS_COMPOST;
    public static final RegistryObject<Block> RESURGENT_SOIL;
    public static final RegistryObject<Block> RESURGENT_SOIL_FARMLAND;
    public static final RegistryObject<Block> WARPED_FUNGUS_COLONY;
    public static final RegistryObject<Block> CRIMSON_FUNGUS_COLONY;
    public static final RegistryObject<Block> STUFFED_HOGLIN;
    public static final RegistryObject<Block> HOGLIN_TROPHY;
    public static final RegistryObject<Block> WAXED_HOGLIN_TROPHY;
    public static final RegistryObject<Block> ZOGLIN_TROPHY;
    public static final RegistryObject<Block> SKOGLIN_TROPHY;
    public static final RegistryObject<Block> POWDERY_CHUBBY_SAPLING;
    public static final RegistryObject<Block> POWDERY_CANNON;
    public static final RegistryObject<Block> POWDERY_CANE;
    public static final RegistryObject<Block> BULLET_PEPPER;
    public static final RegistryObject<Block> POWDERY_TORCH;
    public static final RegistryObject<Block> WALL_POWDERY_TORCH;
    public static final RegistryObject<Block> POTTED_POWDERY_CANNON;
    public static final RegistryObject<Block> POTTED_BULLET_PEPPER;
    public static final RegistryObject<Block> POWDERY_CABINET;
    public static final RegistryObject<Block> BLOCK_OF_POWDERY_CANNON;
    public static final RegistryObject<Block> BLOCK_OF_STRIPPED_POWDERY_CANNON;
    public static final RegistryObject<Block> POWDERY_PLANKS;
    public static final RegistryObject<Block> POWDERY_PLANKS_SLAB;
    public static final RegistryObject<Block> POWDERY_PLANKS_STAIRS;
    public static final RegistryObject<Block> POWDERY_MOSAIC;
    public static final RegistryObject<Block> POWDERY_MOSAIC_SLAB;
    public static final RegistryObject<Block> POWDERY_MOSAIC_STAIRS;
    public static final RegistryObject<Block> POWDERY_FENCE;
    public static final RegistryObject<Block> POWDERY_TRAPDOOR;
    public static final RegistryObject<Block> POWDERY_DOOR;
    public static final RegistryObject<Block> POWDERY_BUTTON;
    public static final RegistryObject<Block> POWDERY_PRESSURE_PLATE;
    public static final RegistryObject<Block> POWDERY_FENCE_GATE;
    public static final RegistryObject<Block> POWDERY_SIGN;
    public static final RegistryObject<Block> POWDERY_WALL_SIGN;
    public static final RegistryObject<Block> STRIDERLOAF_BLOCK;
    public static final RegistryObject<Block> COLD_STRIDERLOAF_BLOCK;
    public static final RegistryObject<Block> GHASTA_WITH_CREAM_BLOCK;
    public static final RegistryObject<Block> MAGMA_CAKE;

    public MNDBlocks() {
    }
    static {
        NETHER_BRICKS_CABINET = BLOCKS.register("nether_bricks_cabinet", () -> 
                new CabinetBlock(Properties.copy(Blocks.NETHER_BRICKS))
        );
        NETHER_STOVE = BLOCKS.register("nether_stove", () ->
                new NetherStoveBlock(Properties.copy(Blocks.NETHER_BRICKS)
                        .lightLevel(litBlockEmission(13)))
        );
        BULLET_PEPPER_CRATE = BLOCKS.register("bullet_pepper_crate", () ->
                new PepperCrateBlock(Properties.copy(Blocks.CRIMSON_STEM)
                        .color(MaterialColor.FIRE).strength(2.0F, 3.0F)
                        .lightLevel((light) -> 15))
        );
        LETIOS_COMPOST = BLOCKS.register("letios_compost", () ->
                new LetiosCompostBlock(Properties.copy(Blocks.DIRT)
                        .color(MaterialColor.COLOR_BROWN).strength(1.2F).speedFactor(0.4F)
                        .sound(SoundType.SOUL_SAND))
        );
        RESURGENT_SOIL = BLOCKS.register("resurgent_soil", () ->
                new ResurgentSoilBlock(Properties.copy(Blocks.SOUL_SOIL)
                        .color(MaterialColor.COLOR_BROWN).speedFactor(0.4F).randomTicks())
        );
        RESURGENT_SOIL_FARMLAND = BLOCKS.register("resurgent_soil_farmland", () ->
                new ResurgentSoilFarmlandBlock(Properties.copy(Blocks.FARMLAND)
                        .color(MaterialColor.COLOR_BROWN).speedFactor(0.4F)
                        .lightLevel(FlameBlockEmission(10)))
        );
        WARPED_FUNGUS_COLONY = BLOCKS.register("warped_fungus_colony", () ->
                new MushroomColonyBlock(Properties.copy(Blocks.BROWN_MUSHROOM).sound(SoundType.FUNGUS),
                        () -> Items.WARPED_FUNGUS)
        );
        CRIMSON_FUNGUS_COLONY = BLOCKS.register("crimson_fungus_colony", () ->
                new MushroomColonyBlock(Properties.copy(Blocks.RED_MUSHROOM).sound(SoundType.FUNGUS),
                        () -> Items.CRIMSON_FUNGUS)
        );
        POWDERY_CHUBBY_SAPLING = BLOCKS.register("powdery_chubby_sapling", () ->
                new PowderyCannonSaplingBlock(Properties.copy(Blocks.BAMBOO_SAPLING)
                        .color(MaterialColor.TERRACOTTA_BLACK).strength(2.0F, 3.0F))
        );
        POWDERY_CANNON = BLOCKS.register("powdery_cannon", () ->
                new PowderyCannonBlock(Properties.copy(Blocks.BAMBOO)
                        .color(MaterialColor.TERRACOTTA_BLACK).strength(3.0F, 3.0F)
                        .lightLevel(litBlockEmission(12)))
        );
        POWDERY_CANE = BLOCKS.register("powdery_cane", () ->
                new PowderyCaneBlock(Properties.copy(Blocks.BAMBOO)
                        .color(MaterialColor.TERRACOTTA_BLACK).noCollission()
                        .instabreak().lightLevel(litBlockEmission(12)))
        );
        BULLET_PEPPER = BLOCKS.register("bullet_pepper", () ->
                new PowderyFlowerBlock(Properties.copy(Blocks.BAMBOO_SAPLING)
                        .color(MaterialColor.TERRACOTTA_BLACK).noCollission()
                        .instabreak().lightLevel(litBlockEmission(8)))
        );
        POWDERY_TORCH = BLOCKS.register("powdery_torch", () ->
                new TorchBlock(Properties.of(Material.DECORATION)
                        .noCollission().instabreak().lightLevel((light) -> 8)
                        .sound(SoundType.BAMBOO), ParticleTypes.FLAME)
        );
        WALL_POWDERY_TORCH = BLOCKS.register("wall_powdery_torch", () ->
                new WallTorchBlock(Properties.of(Material.DECORATION).dropsLike(POWDERY_TORCH.get())
                        .noCollission().instabreak().lightLevel((light) -> 8)
                        .sound(SoundType.BAMBOO), ParticleTypes.FLAME));
        // POWDERY_POTS
        POTTED_POWDERY_CANNON = BLOCKS.register("potted_powdery_cannon", () ->
                new FlowerPotBlock(POWDERY_CANNON.get(), Properties.of(Material.DECORATION)
                        .instabreak().noOcclusion())
        );
        POTTED_BULLET_PEPPER = BLOCKS.register("potted_bullet_pepper", () ->
                new FlowerPotBlock(BULLET_PEPPER.get(), Properties.of(Material.DECORATION)
                        .instabreak().noOcclusion().lightLevel((light) ->  8))
        );
        // POWDERY_BLOCKS
        POWDERY_CABINET = BLOCKS.register("powdery_cabinet", () ->
                new CabinetBlock(Properties.copy(Blocks.BARREL)
                        .color(MaterialColor.TERRACOTTA_GRAY))
        );
        BLOCK_OF_POWDERY_CANNON = BLOCKS.register("powdery_block", () ->
                new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_STEM)
                        .color(MaterialColor.TERRACOTTA_BLACK))
        );
        BLOCK_OF_STRIPPED_POWDERY_CANNON = BLOCKS.register("stripped_powdery_block", () ->
                new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_STEM)
                        .color(MaterialColor.TERRACOTTA_GRAY))
        );
        POWDERY_PLANKS = BLOCKS.register("powdery_planks", () ->
                new Block(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)
                        .color(MaterialColor.TERRACOTTA_GRAY))
        );
        POWDERY_PLANKS_STAIRS = BLOCKS.register("powdery_stairs", () ->
                new StairBlock(Blocks.CRIMSON_PLANKS::defaultBlockState,
                        BlockBehaviour.Properties.copy(Blocks.CRIMSON_STAIRS)
                        .color(MaterialColor.TERRACOTTA_GRAY))
        );
        POWDERY_PLANKS_SLAB = BLOCKS.register("powdery_slab", () ->
                new SlabBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_SLAB)
                        .color(MaterialColor.TERRACOTTA_GRAY))
        );
        POWDERY_MOSAIC = BLOCKS.register("powdery_mosaic", () ->
                new Block(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)
                        .color(MaterialColor.TERRACOTTA_GRAY))
        );
        POWDERY_MOSAIC_STAIRS = BLOCKS.register("powdery_mosaic_stairs", () ->
                new StairBlock(Blocks.CRIMSON_PLANKS::defaultBlockState,
                        BlockBehaviour.Properties.copy(Blocks.CRIMSON_STAIRS)
                        .color(MaterialColor.TERRACOTTA_GRAY))
        );
        POWDERY_MOSAIC_SLAB = BLOCKS.register("powdery_mosaic_slab", () ->
                new SlabBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_SLAB)
                        .color(MaterialColor.TERRACOTTA_GRAY))
        );
        POWDERY_FENCE = BLOCKS.register("powdery_fence", () ->
                new FenceBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_FENCE)
                        .color(MaterialColor.TERRACOTTA_GRAY))
        );
        POWDERY_FENCE_GATE = BLOCKS.register("powdery_fence_gate", () ->
                new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_FENCE_GATE)
                        .color(MaterialColor.TERRACOTTA_GRAY))
        );
        POWDERY_DOOR = BLOCKS.register("powdery_door", () ->
                new DoorBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_DOOR)
                        .color(MaterialColor.TERRACOTTA_GRAY))
        );
        POWDERY_TRAPDOOR = BLOCKS.register("powdery_trapdoor", () ->
                new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_TRAPDOOR)
                        .color(MaterialColor.TERRACOTTA_GRAY))
        );
        POWDERY_BUTTON = BLOCKS.register("powdery_button", () ->
                new WoodButtonBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_BUTTON)
                        .color(MaterialColor.TERRACOTTA_GRAY))
        );
        POWDERY_PRESSURE_PLATE = BLOCKS.register("powdery_pressure_plate", () ->
                new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING,
                        BlockBehaviour.Properties.copy(Blocks.CRIMSON_PRESSURE_PLATE)
                        .color(MaterialColor.TERRACOTTA_GRAY))
        );
        POWDERY_SIGN = BLOCKS.register("powdery_sign", () ->
                new MNDStandingSignBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_SIGN),
                        MNDWoodTypes.POWDERY)
        );
        POWDERY_WALL_SIGN = BLOCKS.register("powdery_wall_sign", () ->
                new MNDWallSignBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_WALL_SIGN),
                        MNDWoodTypes.POWDERY)
        );
        STRIDERLOAF_BLOCK = BLOCKS.register("striderloaf_block", () ->
                new StriderloafBlock(Properties.copy(Blocks.CAKE), MNDItems.STRIDERLOAF, true)
        );
        COLD_STRIDERLOAF_BLOCK = BLOCKS.register("cold_striderloaf_block", () ->
                new StriderloafBlock(Properties.copy(Blocks.CAKE).dropsLike(STRIDERLOAF_BLOCK.get()), MNDItems.COLD_STRIDERLOAF, true)
        );
        GHASTA_WITH_CREAM_BLOCK = BLOCKS.register("ghasta_with_cream_block", () ->
                new GhastaWithCreamBlock(Properties.copy(Blocks.CAKE).lightLevel((light) ->  3)
                        , MNDItems.GHASTA_WITH_CREAM, true)
        );
        MAGMA_CAKE = BLOCKS.register("magma_cake", () ->
                new MagmaCakeBlock (Block.Properties.copy(Blocks.CAKE).lightLevel((light) ->  9)
                        , MNDItems.MAGMA_CAKE_SLICE)
        );
        STUFFED_HOGLIN = BLOCKS.register("stuffed_hoglin", () ->
                new StuffedHoglinBlock(Properties.copy(Blocks.CAKE)
                        .color(MaterialColor.TERRACOTTA_PINK))
        );
        HOGLIN_TROPHY = BLOCKS.register("hoglin_trophy", () ->
                new TrophyBlock(Properties.copy(Blocks.CRIMSON_PLANKS)
                        .color(MaterialColor.TERRACOTTA_PINK))
        );
        WAXED_HOGLIN_TROPHY = BLOCKS.register("waxed_hoglin_trophy", () ->
                new TrophyBlock(Properties.copy(Blocks.CRIMSON_PLANKS)
                        .color(MaterialColor.TERRACOTTA_PINK))
        );
        ZOGLIN_TROPHY = BLOCKS.register("zoglin_trophy", () ->
                new TrophyBlock(Properties.copy(Blocks.CRIMSON_PLANKS)
                        .color(MaterialColor.TERRACOTTA_GREEN))
        );
        SKOGLIN_TROPHY = BLOCKS.register("skoglin_trophy", () ->
                new TrophyBlock(Properties.copy(Blocks.BONE_BLOCK)
                        .color(MaterialColor.TERRACOTTA_WHITE))
        );
    }
    private static ToIntFunction<BlockState> FlameBlockEmission(int lightValue) {
        return (state) -> state.getValue(BlockStateProperties.MOISTURE) == 7 ? lightValue : 0;
    }
    private static ToIntFunction<BlockState> litBlockEmission(int lightValue) {
        return (state) -> state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
    }
}
