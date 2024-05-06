//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package com.soytutta.mynethersdelight.common.block;

import java.util.Iterator;

import com.soytutta.mynethersdelight.common.tag.MNDTags;
import com.soytutta.mynethersdelight.common.registry.MNDBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IPlantable;

public class LetiosCompostBlock extends Block {
    public static IntegerProperty FORGOTING = IntegerProperty.create("forgoting", 0, 9);
    public LetiosCompostBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(super.defaultBlockState().setValue(FORGOTING, 0));
    }
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FORGOTING);
        super.createBlockStateDefinition(builder);
    }

    public int getMaxForgotingStage() {
        return 9;
    }

    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        if (!worldIn.isClientSide) {
            float chance = 0.0F;
            boolean hasLava = false;
            boolean isSoulBiome = false;

            for (BlockPos neighborPos : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
                BlockState neighborState = worldIn.getBlockState(neighborPos);

                if (neighborState.is(MNDTags.LETIOS_ACTIVATORS)) {
                    chance += 0.02F;
                }

                if (neighborState.is(MNDTags.LETIOS_FLAMES)) {
                    if (!neighborState.hasProperty(BlockStateProperties.LIT) || (neighborState.hasProperty(BlockStateProperties.LIT) && neighborState.getValue(BlockStateProperties.LIT))) {
                        chance += 0.02F;
                    }
                }

                if (neighborState.getFluidState().is(FluidTags.LAVA)) {
                    hasLava = true;
                }

                if (worldIn.getBiome(pos).is(Biomes.SOUL_SAND_VALLEY)) {
                    isSoulBiome = true;
                }
            }

            chance += hasLava ? 0.3F : 0.0F;
            chance += isSoulBiome ? 0.3F : 0.0F;
            if (worldIn.getRandom().nextFloat() <= chance && worldIn.dimensionType().ultraWarm()) {
                if (state.getValue(FORGOTING) == this.getMaxForgotingStage()) {
                    worldIn.setBlock(pos, MNDBlocks.RESURGENT_SOIL.get().defaultBlockState(), 3);
                } else {
                    worldIn.setBlock(pos, state.setValue(FORGOTING, (Integer) state.getValue(FORGOTING) + 1), 3);
                }
            }

        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
        return this.getMaxForgotingStage() + 1 - blockState.getValue(FORGOTING);
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        if (random.nextInt(10) == 0 && level.dimensionType().ultraWarm()) {
            level.addParticle(ParticleTypes.SOUL, (double)pos.getX() + (double)random.nextFloat(), (double)pos.getY() + 1.1, (double)pos.getZ() + (double)random.nextFloat(), 0.0, 0.0, 0.0);
        }
    }
    public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
            if (plantable instanceof NetherWartBlock && facing == Direction.UP) {
                return true;
            }
            return super.canSustainPlant(state, world, pos, facing, plantable);
        }
    }
