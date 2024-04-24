//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.common.registry;

import com.soytutta.mynethersdelight.common.block.*;
import com.soytutta.mynethersdelight.common.block.PowderyCannonSaplingBlock;
import com.soytutta.mynethersdelight.common.block.entity.MNDStandingSignBlock;
import com.soytutta.mynethersdelight.common.block.entity.MNDWallSignBlock;
import com.soytutta.mynethersdelight.common.block.utility.MNDWoodTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.block.*;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.function.ToIntFunction;

public class MNDBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "mynethersdelight");

    public static final RegistryObject<Block> NETHER_BRICKS_CABINET;
    public static final RegistryObject<Block> NETHER_STOVE;
    public static final RegistryObject<Block> LETIOS_COMPOST;
    public static final RegistryObject<Block> RESURGENT_SOIL;
    public static final RegistryObject<Block> RESURGENT_SOIL_FARMLAND;
    public static final RegistryObject<Block> WARPED_FUNGUS_COLONY;
    public static final RegistryObject<Block> CRIMSON_FUNGUS_COLONY;
    public static final RegistryObject<Block> STUFFED_HOGLIN;
    public static final RegistryObject<Block> HOGLIN_TROPHY;
    public static final RegistryObject<Block> WAXED_HOGLIN_TROPHY;
    public static final RegistryObject<Block> ZOGLIN_TROPHY;
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

    public MNDBlocks() {
    }
    static {
        NETHER_BRICKS_CABINET = BLOCKS.register("nether_bricks_cabinet", () -> {
            return new CabinetBlock(Properties.copy(Blocks.NETHER_BRICKS));
        });
        NETHER_STOVE = BLOCKS.register("nether_stove", () -> {
            return new NetherStoveBlock(Properties.copy(Blocks.NETHER_BRICKS).lightLevel(litBlockEmission(13)));
        });
        LETIOS_COMPOST = BLOCKS.register("letios_compost", () -> {
            return new LetiosCompostBlock(Properties.copy(Blocks.DIRT).color(MaterialColor.COLOR_BROWN).strength(1.2F).speedFactor(0.4F).sound(SoundType.SOUL_SAND));
        });
        RESURGENT_SOIL = BLOCKS.register("resurgent_soil", () -> {
            return new ResurgentSoilBlock(Properties.copy(Blocks.SOUL_SOIL).color(MaterialColor.COLOR_BROWN).speedFactor(0.4F).randomTicks());
        });
        RESURGENT_SOIL_FARMLAND = BLOCKS.register("resurgent_soil_farmland", () -> {
            return new ResurgentSoilFarmlandBlock(Properties.copy(Blocks.FARMLAND).color(MaterialColor.COLOR_BROWN).speedFactor(0.4F).lightLevel(FlameBlockEmission(7)));
        });
        WARPED_FUNGUS_COLONY = BLOCKS.register("warped_fungus_colony", () -> {
            return new MushroomColonyBlock(Properties.copy(Blocks.WARPED_FUNGUS), () -> {
                return Items.WARPED_FUNGUS;
            });
        });
        CRIMSON_FUNGUS_COLONY = BLOCKS.register("crimson_fungus_colony", () -> {
            return new MushroomColonyBlock(Properties.copy(Blocks.CRIMSON_FUNGUS), () -> {
                return Items.CRIMSON_FUNGUS;
            });
        });
        STUFFED_HOGLIN = BLOCKS.register("stuffed_hoglin", () -> {
            return new StuffedHoglinBlock(Properties.copy(Blocks.CAKE).color(MaterialColor.TERRACOTTA_PINK));
        });
        HOGLIN_TROPHY = BLOCKS.register("hoglin_trophy", () -> {
            return new TrophyBlock(Properties.copy(Blocks.MANGROVE_WOOD).color(MaterialColor.TERRACOTTA_PINK));
        });
        WAXED_HOGLIN_TROPHY = BLOCKS.register("waxed_hoglin_trophy", () -> {
            return new TrophyBlock(Properties.copy(Blocks.MANGROVE_WOOD).color(MaterialColor.TERRACOTTA_PINK));
        });
        ZOGLIN_TROPHY = BLOCKS.register("zoglin_trophy", () -> {
            return new TrophyBlock(Properties.copy(Blocks.MANGROVE_WOOD).color(MaterialColor.TERRACOTTA_GREEN));
        });
        POWDERY_CHUBBY_SAPLING = BLOCKS.register("powdery_chubby_sapling", () -> {
            return new PowderyCannonSaplingBlock(Properties.copy(Blocks.BAMBOO_SAPLING).color(MaterialColor.TERRACOTTA_BLACK).strength(2.0F, 3.0F));
        });
        POWDERY_CANNON = BLOCKS.register("powdery_cannon", () -> {
            return new PowderyCannonBlock(Properties.copy(Blocks.BAMBOO).color(MaterialColor.TERRACOTTA_BLACK).strength(3.0F, 3.0F).lightLevel(litBlockEmission(12)));
        });
        POWDERY_CANE = BLOCKS.register("powdery_cane", () -> {
            return new PowderyCaneBlock(Properties.copy(Blocks.BAMBOO).color(MaterialColor.TERRACOTTA_BLACK).noCollission().instabreak().lightLevel(litBlockEmission(12)));
        });
        BULLET_PEPPER = BLOCKS.register("bullet_pepper", () -> {
            return new PowderyFlowerBlock(Properties.copy(Blocks.BAMBOO_SAPLING).color(MaterialColor.TERRACOTTA_BLACK).noCollission().instabreak().lightLevel(litBlockEmission(8)));
        });
        POWDERY_TORCH = BLOCKS.register("powdery_torch", () -> {
            return new TorchBlock(Properties.of(Material.DECORATION).noCollission().instabreak().lightLevel((light) -> {
                return 8;
            }).sound(SoundType.BAMBOO), ParticleTypes.FLAME);
        });
        WALL_POWDERY_TORCH = BLOCKS.register("wall_powdery_torch", () -> {
            return new WallTorchBlock(Properties.of(Material.DECORATION).dropsLike(POWDERY_TORCH.get()).noCollission().instabreak().lightLevel((light) -> {
                return 8;
            }).sound(SoundType.BAMBOO), ParticleTypes.FLAME);
        });
        // POWDERY_POTS
        POTTED_POWDERY_CANNON = BLOCKS.register("potted_powdery_cannon", () -> {
            return new FlowerPotBlock(POWDERY_CANNON.get(), Properties.of(Material.DECORATION).instabreak().noOcclusion());
        });
        POTTED_BULLET_PEPPER = BLOCKS.register("potted_bullet_pepper", () -> {
            return new FlowerPotBlock(BULLET_PEPPER.get(), Properties.of(Material.DECORATION).instabreak().noOcclusion().lightLevel((light) -> {
                return 8;
            }));
        });
        // POWDERY_BLOCKS
        POWDERY_CABINET = BLOCKS.register("powdery_cabinet", () -> {
            return new CabinetBlock(Properties.copy(ModBlocks.CRIMSON_CABINET.get()));
        });
        BLOCK_OF_POWDERY_CANNON = BLOCKS.register("block_of_powdery_cannon", () -> {
            return new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS));
        });
        BLOCK_OF_STRIPPED_POWDERY_CANNON = BLOCKS.register("block_of_stripped_powdery_cannon", () -> {
            return new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS));
        });
        POWDERY_PLANKS = BLOCKS.register("powdery_planks", () -> {
            return new Block(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS));
        });
        POWDERY_PLANKS_STAIRS = BLOCKS.register("powdery_stairs", () -> {
            return new StairBlock(Blocks.CRIMSON_PLANKS::defaultBlockState, BlockBehaviour.Properties.copy(Blocks.CRIMSON_STAIRS));
        });
        POWDERY_PLANKS_SLAB = BLOCKS.register("powdery_slab", () -> {
            return new SlabBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_SLAB));
        });
        POWDERY_MOSAIC = BLOCKS.register("powdery_mosaic", () -> {
            return new Block(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS));
        });
        POWDERY_MOSAIC_STAIRS = BLOCKS.register("powdery_mosaic_stairs", () -> {
            return new StairBlock(Blocks.CRIMSON_PLANKS::defaultBlockState, BlockBehaviour.Properties.copy(Blocks.CRIMSON_STAIRS));
        });
        POWDERY_MOSAIC_SLAB = BLOCKS.register("powdery_mosaic_slab", () -> {
            return new SlabBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_SLAB));
        });
        POWDERY_FENCE = BLOCKS.register("powdery_fence", () -> {
            return new FenceBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_FENCE));
        });
        POWDERY_FENCE_GATE = BLOCKS.register("powdery_fence_gate", () -> {
            return new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_FENCE_GATE));
        });
        POWDERY_DOOR = BLOCKS.register("powdery_door", () -> {
            return new DoorBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_DOOR));
        });
        POWDERY_TRAPDOOR = BLOCKS.register("powdery_trapdoor", () -> {
            return new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_TRAPDOOR));
        });
        POWDERY_BUTTON = BLOCKS.register("powdery_button", () -> {
            return new WoodButtonBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_BUTTON));
        });
        POWDERY_PRESSURE_PLATE = BLOCKS.register("powdery_pressure_plate", () -> {
            return new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(Blocks.CRIMSON_PRESSURE_PLATE));
        });
        POWDERY_SIGN = BLOCKS.register("powdery_sign", () -> {
            return new MNDStandingSignBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_SIGN), MNDWoodTypes.POWDERY_CANNON);
        });
        POWDERY_WALL_SIGN = BLOCKS.register("powdery_wall_sign", () -> {
            return new MNDWallSignBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_WALL_SIGN), MNDWoodTypes.POWDERY_CANNON);
        });
    }

    private static ToIntFunction<BlockState> FlameBlockEmission(int lightValue) {
        return (state) -> state.getValue(BlockStateProperties.MOISTURE) == 7 ? lightValue : 0;
    }
    private static ToIntFunction<BlockState> litBlockEmission(int lightValue) {
        return (state) -> state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
    }
}
