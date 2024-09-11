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
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.common.block.*;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class MNDBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "mynethersdelight");

    public static final Supplier<Block> NETHER_BRICKS_CABINET = BLOCKS.register("nether_bricks_cabinet", () ->
                new CabinetBlock(Properties.ofFullCopy(Blocks.NETHER_BRICKS))
        );
    public static final Supplier<Block> RED_NETHER_BRICKS_CABINET = BLOCKS.register("red_nether_bricks_cabinet", () ->
            new CabinetBlock(Properties.ofFullCopy(Blocks.RED_NETHER_BRICKS))
    );
    public static final Supplier<Block> BLACKSTONE_BRICKS_CABINET = BLOCKS.register("blackstone_bricks_cabinet", () ->
            new CabinetBlock(Properties.ofFullCopy(Blocks.POLISHED_BLACKSTONE_BRICKS))
    );
    public static final Supplier<Block> NETHER_STOVE = BLOCKS.register("nether_stove", () ->
            new NetherStoveBlock(Properties.ofFullCopy(Blocks.NETHER_BRICKS)
                    .lightLevel(litBlockEmission(13)))
    );
    public static final Supplier<Block> BULLET_PEPPER_CRATE = BLOCKS.register("bullet_pepper_crate", () ->
            new PepperCrateBlock(Properties.ofFullCopy(Blocks.OAK_PLANKS)
                    .mapColor(MapColor.FIRE).strength(2.0F, 3.0F)
                    .sound(SoundType.BAMBOO_WOOD).lightLevel((light) -> 15))
    );
    public static final Supplier<Block> LETIOS_COMPOST = BLOCKS.register("letios_compost", () ->
            new LetiosCompostBlock(Properties.ofFullCopy(Blocks.SOUL_SOIL)
                    .mapColor(MapColor.COLOR_BROWN).strength(1.2F).speedFactor(0.4F))
    );
    public static final Supplier<Block> RESURGENT_SOIL = BLOCKS.register("resurgent_soil", () ->
            new ResurgentSoilBlock(Properties.ofFullCopy(Blocks.SOUL_SOIL)
                    .sound(SoundType.SOUL_SAND)
                    .mapColor(MapColor.COLOR_BROWN).speedFactor(0.4F).randomTicks())
    );
    public static final Supplier<Block> RESURGENT_SOIL_FARMLAND = BLOCKS.register("resurgent_soil_farmland", () ->
            new ResurgentSoilFarmlandBlock(Properties.ofFullCopy(Blocks.FARMLAND)
                    .sound(SoundType.SOUL_SAND)
                    .mapColor(MapColor.COLOR_BROWN).speedFactor(0.4F)
                    .lightLevel(FlameBlockEmission(10)))
    );
    public static final Supplier<Block> WARPED_FUNGUS_COLONY = BLOCKS.register("warped_fungus_colony", () ->
            new MushroomColonyBlock(Items.WARPED_FUNGUS.builtInRegistryHolder(),
                    Properties.ofFullCopy(Blocks.BROWN_MUSHROOM).sound(SoundType.FUNGUS))
    );
    public static final Supplier<Block> CRIMSON_FUNGUS_COLONY = BLOCKS.register("crimson_fungus_colony", () ->
            new MushroomColonyBlock(Items.CRIMSON_FUNGUS.builtInRegistryHolder(),
                    Properties.ofFullCopy(Blocks.RED_MUSHROOM).sound(SoundType.FUNGUS))
    );
    public static final Supplier<Block> POWDERY_CHUBBY_SAPLING = BLOCKS.register("powdery_chubby_sapling", () ->
            new PowderyCannonSaplingBlock(Properties.ofFullCopy(Blocks.BAMBOO_SAPLING)
                    .mapColor(MapColor.TERRACOTTA_BLACK).strength(2.0F, 3.0F))
    );
    public static final Supplier<Block> POWDERY_CANNON = BLOCKS.register("powdery_cannon", () ->
            new PowderyCannonBlock(Properties.ofFullCopy(Blocks.BAMBOO)
                    .mapColor(MapColor.TERRACOTTA_BLACK).strength(3.0F, 3.0F)
                    .lightLevel(litBlockEmission(12)))
    );
    public static final Supplier<Block> POWDERY_CANE = BLOCKS.register("powdery_cane", () ->
            new PowderyCaneBlock(Properties.ofFullCopy(Blocks.BAMBOO)
                    .mapColor(MapColor.TERRACOTTA_BLACK).noCollission()
                    .instabreak().lightLevel(litBlockEmission(12)))
    );
    public static final Supplier<Block> BULLET_PEPPER = BLOCKS.register("bullet_pepper", () ->
            new PowderyFlowerBlock(Properties.ofFullCopy(Blocks.BAMBOO_SAPLING)
                    .mapColor(MapColor.TERRACOTTA_BLACK).noCollission()
                    .instabreak().lightLevel(litBlockEmission(8)))
    );
    public static final Supplier<Block> POWDERY_TORCH = BLOCKS.register("powdery_torch", () ->
            new TorchBlock(ParticleTypes.FLAME, Properties.of()
                    .noCollission().instabreak().lightLevel((light) -> 8)
                    .sound(SoundType.BAMBOO))
    );
    public static final Supplier<Block> WALL_POWDERY_TORCH = BLOCKS.register("wall_powdery_torch", () ->
            new WallTorchBlock(ParticleTypes.FLAME, Properties.of().dropsLike(POWDERY_TORCH.get())
                    .noCollission().instabreak().lightLevel((light) -> 8)
                    .sound(SoundType.BAMBOO)));
    // POWDERY_POTS
    public static final Supplier<Block> POTTED_POWDERY_CANNON = BLOCKS.register("potted_powdery_cannon", () ->
            new FlowerPotBlock(POWDERY_CANNON.get(), Properties.of()
                    .instabreak().noOcclusion())
    );
    public static final Supplier<Block> POTTED_BULLET_PEPPER = BLOCKS.register("potted_bullet_pepper", () ->
            new FlowerPotBlock(BULLET_PEPPER.get(), Properties.of()
                    .instabreak().noOcclusion().lightLevel((light) ->  8))
    );
    // POWDERY_BLOCKS
    public static final Supplier<Block> POWDERY_CABINET = BLOCKS.register("powdery_cabinet", () ->
            new CabinetBlock(Properties.ofFullCopy(Blocks.BARREL)
                .sound(SoundType.BAMBOO_WOOD).mapColor(MapColor.TERRACOTTA_GRAY))
    );
    public static final Supplier<Block> BLOCK_OF_POWDERY_CANNON = BLOCKS.register("powdery_block", () ->
            new StrippableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_STEM).sound(SoundType.BAMBOO_WOOD)
                    .instrument(NoteBlockInstrument.BASS).explosionResistance(300.0F)
                    .mapColor(MapColor.TERRACOTTA_BLACK))
    );
    public static final Supplier<Block> BLOCK_OF_STRIPPED_POWDERY_CANNON = BLOCKS.register("stripped_powdery_block", () ->
            new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_STEM).sound(SoundType.BAMBOO_WOOD)
                    .instrument(NoteBlockInstrument.BASS)
                    .mapColor(MapColor.TERRACOTTA_GRAY))
    );
    public static final Supplier<Block> POWDERY_PLANKS = BLOCKS.register("powdery_planks", () ->
            new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_PLANKS)
                    .sound(SoundType.BAMBOO_WOOD)
                    .mapColor(MapColor.TERRACOTTA_GRAY))
    );
    public static final Supplier<Block> POWDERY_PLANKS_STAIRS = BLOCKS.register("powdery_stairs", () ->
            new StairBlock(POWDERY_PLANKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_STAIRS)
                    .sound(SoundType.BAMBOO_WOOD)
                    .mapColor(MapColor.TERRACOTTA_GRAY))
    );
    public static final Supplier<Block> POWDERY_PLANKS_SLAB = BLOCKS.register("powdery_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_SLAB)
                    .sound(SoundType.BAMBOO_WOOD)
                    .mapColor(MapColor.TERRACOTTA_GRAY))
    );
    public static final Supplier<Block> POWDERY_MOSAIC = BLOCKS.register("powdery_mosaic", () ->
            new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_PLANKS)
                    .sound(SoundType.BAMBOO_WOOD)
                    .mapColor(MapColor.TERRACOTTA_GRAY))
    );
    public static final Supplier<Block> POWDERY_MOSAIC_STAIRS = BLOCKS.register("powdery_mosaic_stairs", () ->
            new StairBlock(POWDERY_PLANKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_STAIRS)
                    .sound(SoundType.BAMBOO_WOOD)
                    .mapColor(MapColor.TERRACOTTA_GRAY))
    );
    public static final Supplier<Block> POWDERY_MOSAIC_SLAB = BLOCKS.register("powdery_mosaic_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_SLAB)
                    .sound(SoundType.BAMBOO_WOOD)
                    .mapColor(MapColor.TERRACOTTA_GRAY))
    );
    public static final Supplier<Block> POWDERY_FENCE = BLOCKS.register("powdery_fence", () ->
            new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_FENCE)
                    .sound(SoundType.BAMBOO_WOOD)
                    .mapColor(MapColor.TERRACOTTA_GRAY))
    );
    public static final Supplier<Block> POWDERY_FENCE_GATE = BLOCKS.register("powdery_fence_gate", () ->
            new FenceGateBlock(WoodType.BAMBOO, BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_FENCE_GATE)
                    .mapColor(MapColor.TERRACOTTA_GRAY))
    );
    public static final Supplier<Block> POWDERY_DOOR = BLOCKS.register("powdery_door", () ->
            new DoorBlock(BlockSetType.BAMBOO, BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_DOOR)
                    .mapColor(MapColor.TERRACOTTA_GRAY))
    );
    public static final Supplier<Block> POWDERY_TRAPDOOR = BLOCKS.register("powdery_trapdoor", () ->
            new TrapDoorBlock(BlockSetType.BAMBOO, BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_TRAPDOOR)
                    .mapColor(MapColor.TERRACOTTA_GRAY))
    );
    public static final Supplier<Block> POWDERY_BUTTON = BLOCKS.register("powdery_button", () ->
            new ButtonBlock(BlockSetType.BAMBOO, 30,BlockBehaviour.Properties.ofFullCopy(POWDERY_PLANKS.get())
                    .noCollission().strength(0.5F).mapColor(MapColor.TERRACOTTA_GRAY))
    );
    public static final Supplier<Block> POWDERY_PRESSURE_PLATE = BLOCKS.register("powdery_pressure_plate", () ->
            new PressurePlateBlock(BlockSetType.BAMBOO, BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_PRESSURE_PLATE)
                    .mapColor(MapColor.TERRACOTTA_GRAY))
    );
    public static final Supplier<Block> POWDERY_SIGN = BLOCKS.register("powdery_sign", () ->
            new MNDStandingSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_SIGN),
                    MNDWoodTypes.POWDERY)
    );
    public static final Supplier<Block> POWDERY_WALL_SIGN = BLOCKS.register("powdery_wall_sign", () ->
            new MNDWallSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_WALL_SIGN),
                    MNDWoodTypes.POWDERY)
    );
    public static final Supplier<Block> POWDERY_HANGING_SIGN = BLOCKS.register("powdery_hanging_sign", () ->
            new MNDHangingSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_HANGING_SIGN),
                    MNDWoodTypes.POWDERY)
    );
    public static final Supplier<Block> POWDERY_WALL_HANGING_SIGN = BLOCKS.register("powdery_wall_hanging_sign", () ->
            new MNDWallHangingSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_WALL_HANGING_SIGN),
                    MNDWoodTypes.POWDERY)
    );
    public static final Supplier<Block> STRIDERLOAF_BLOCK = BLOCKS.register("striderloaf_block", () ->
            new StriderloafBlock(Properties.ofFullCopy(Blocks.CAKE), MNDItems.STRIDERLOAF, true)
    );
    public static final Supplier<Block> COLD_STRIDERLOAF_BLOCK = BLOCKS.register("cold_striderloaf_block", () ->
            new StriderloafBlock(Properties.ofFullCopy(Blocks.CAKE), MNDItems.COLD_STRIDERLOAF, true)
    );
    public static final Supplier<Block> GHASTA_WITH_CREAM_BLOCK = BLOCKS.register("ghasta_with_cream_block", () ->
            new GhastaWithCreamBlock(Properties.ofFullCopy(Blocks.CAKE).lightLevel((light) ->  3)
                    , MNDItems.GHASTA_WITH_CREAM, true)
    );
    public static final Supplier<Block> MAGMA_CAKE_BLOCK = BLOCKS.register("magma_cake_block", () ->
            new MagmaCakeBlock (Block.Properties.ofFullCopy(Blocks.CAKE).lightLevel((light) ->  9)
                    , MNDItems.MAGMA_CAKE_SLICE)
    );
    public static final Supplier<Block> STUFFED_HOGLIN = BLOCKS.register("stuffed_hoglin", () ->
            new StuffedHoglinBlock(Properties.ofFullCopy(Blocks.CAKE)
                    .mapColor(MapColor.TERRACOTTA_PINK))
    );
    public static final Supplier<Block> HOGLIN_TROPHY = BLOCKS.register("hoglin_trophy", () ->
            new TrophyBlock(Properties.ofFullCopy(Blocks.CRIMSON_PLANKS)
                    .mapColor(MapColor.TERRACOTTA_PINK))
    );
    public static final Supplier<Block> WAXED_HOGLIN_TROPHY = BLOCKS.register("waxed_hoglin_trophy", () ->
            new TrophyBlock(Properties.ofFullCopy(Blocks.CRIMSON_PLANKS)
                    .mapColor(MapColor.TERRACOTTA_PINK))
    );
    public static final Supplier<Block> ZOGLIN_TROPHY = BLOCKS.register("zoglin_trophy", () ->
            new TrophyBlock(Properties.ofFullCopy(Blocks.CRIMSON_PLANKS)
                    .mapColor(MapColor.TERRACOTTA_GREEN))
    );
    public static final Supplier<Block> SKOGLIN_TROPHY = BLOCKS.register("skoglin_trophy", () ->
            new TrophyBlock(Properties.ofFullCopy(Blocks.BONE_BLOCK)
                    .mapColor(MapColor.TERRACOTTA_WHITE))
    );

    private static ToIntFunction<BlockState> FlameBlockEmission(int lightValue) {
        return (state) -> state.getValue(BlockStateProperties.MOISTURE) == 7 ? lightValue : 0;
    }
    private static ToIntFunction<BlockState> litBlockEmission(int lightValue) {
        return (state) -> state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
    }
}
