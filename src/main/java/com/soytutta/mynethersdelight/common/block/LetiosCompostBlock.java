//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package com.soytutta.mynethersdelight.common.block;

import com.soytutta.mynethersdelight.common.data.PlantRuleEngine;
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
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.util.TriState;


public class LetiosCompostBlock extends Block {
    public static IntegerProperty FORGOTING = IntegerProperty.create("forgoting", 0, 9);
    private static final ThreadLocal<Boolean> APPLYING_PLANT_RULE = ThreadLocal.withInitial(() -> false);

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
            boolean hasLeteosBooster = false;
            boolean isSoulBiome = worldIn.getBiome(pos).is(Biomes.SOUL_SAND_VALLEY);

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

                if (neighborState.getFluidState().is(MNDTags.LETEOS_BOOSTER)) {
                    hasLeteosBooster = true;
                }
            }

            chance += hasLeteosBooster ? 0.3F : 0.0F;
            chance += isSoulBiome ? 0.3F : 0.0F;
            if (random.nextFloat() <= chance && worldIn.dimensionType().ultraWarm()) {
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
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        if (level instanceof ServerLevel serverLevel && neighborPos.equals(pos.above()) && !APPLYING_PLANT_RULE.get()) {
            BlockState plantState = serverLevel.getBlockState(neighborPos);
            if (!plantState.isAir()) {
                APPLYING_PLANT_RULE.set(true);
                try {
                    PlantRuleEngine.applyLetiosCompostRules(serverLevel, neighborPos, plantState, serverLevel.random);
                } finally {
                    APPLYING_PLANT_RULE.remove();
                }
            }
        }
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return false;
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

        if (level.dimensionType().ultraWarm() && random.nextInt(10) == 0) {
            if (hasActivatorNear(level, pos)) {
                level.addParticle(ParticleTypes.SOUL, (double)pos.getX() + (double)random.nextFloat(), (double)pos.getY() + 1.1, (double)pos.getZ() + (double)random.nextFloat(), 0.0, 0.0, 0.0);
            } else {
                level.addParticle(ParticleTypes.MYCELIUM, (double)pos.getX() + (double)random.nextFloat(), (double)pos.getY() + 1.1, (double)pos.getZ() + (double)random.nextFloat(), 0.0, 0.0, 0.0);
            }
        }
    }

    private boolean hasActivatorNear(Level level, BlockPos centerPos) {
        if (level.getBiome(centerPos).is(Biomes.SOUL_SAND_VALLEY)) {
            return true;
        }

        for (BlockPos neighborPos : BlockPos.betweenClosed(centerPos.offset(-1, -1, -1), centerPos.offset(1, 1, 1))) {
            BlockState neighborState = level.getBlockState(neighborPos);
            if (neighborPos.equals(centerPos)) continue;

            if (neighborState.is(MNDTags.LETIOS_ACTIVATORS)) {
                return true;
            }
            if (neighborState.is(MNDTags.LETIOS_FLAMES)) {
                if (!neighborState.hasProperty(BlockStateProperties.LIT) || (neighborState.hasProperty(BlockStateProperties.LIT) && neighborState.getValue(BlockStateProperties.LIT))) {
                    return true;
                }
            }
            if (neighborState.getFluidState().is(MNDTags.LETEOS_BOOSTER)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public TriState canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, BlockState plantState) {
        if (plantState.getBlock() instanceof NetherWartBlock) {
            return TriState.TRUE;
        }
        return TriState.DEFAULT;
    }
}
