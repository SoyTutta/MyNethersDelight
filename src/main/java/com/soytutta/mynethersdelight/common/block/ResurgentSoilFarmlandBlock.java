package com.soytutta.mynethersdelight.common.block;

import com.soytutta.mynethersdelight.common.MNDConfiguration;
import com.soytutta.mynethersdelight.common.registry.MNDBlocks;
import com.soytutta.mynethersdelight.common.tag.MNDTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.IPlantable;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.block.TomatoBlock;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.function.Predicate;

public class ResurgentSoilFarmlandBlock extends FarmBlock {
    private static final Predicate<BlockState> TOMATO_CONTINUATION =
            state -> state.getBlock() instanceof TomatoBlock;

    public ResurgentSoilFarmlandBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    private static boolean hasFireOrLava(LevelReader level, BlockPos pos) {
        int radius = MNDConfiguration.RESURGENT_FARMLAND_HEAT_SEARCH_RADIUS.get();
        if (radius == 0) {
            return false;
        }

        int verticalRadius = radius / 2;
        for (BlockPos nearbyPos : BlockPos.betweenClosed(
                pos.offset(-radius, -verticalRadius, -radius),
                pos.offset(radius, verticalRadius, radius))) {
            BlockState state = level.getBlockState(nearbyPos);
            int distance = pos.distManhattan(nearbyPos);

            if (state.getFluidState().is(MNDTags.LETEOS_BOOSTER)
                    && distance <= state.getLightEmission(level, nearbyPos)) {
                return true;
            }

            if (state.is(MNDTags.LETIOS_FLAMES)
                    && (!state.hasProperty(BlockStateProperties.LIT)
                    || state.getValue(BlockStateProperties.LIT))) {
                int lightLevel = state.getLightEmission(level, nearbyPos);
                if (state.getBlock() instanceof TorchBlock || state.getBlock() instanceof WallTorchBlock) {
                    lightLevel /= 2;
                } else if (state.getBlock() instanceof LanternBlock) {
                    lightLevel = lightLevel / 2 + 2;
                } else if (state.getBlock() instanceof MagmaCakeBlock
                        || state.getBlock() instanceof MagmaBlock) {
                    lightLevel += 3;
                }
                if (distance <= lightLevel) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void turnToRichSoil(BlockState state, Level level, BlockPos pos) {
        level.setBlockAndUpdate(pos, pushEntitiesUp(
                state, MNDBlocks.RESURGENT_SOIL.get().defaultBlockState(), level, pos));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return !defaultBlockState().canSurvive(context.getLevel(), context.getClickedPos())
                ? MNDBlocks.RESURGENT_SOIL.get().defaultBlockState()
                : super.getStateForPlacement(context);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        entity.causeFallDamage(fallDistance, 1.0F, entity.damageSources().fall());
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState aboveState = level.getBlockState(pos.above());
        return super.canSurvive(state, level, pos)
                || aboveState.is(Blocks.MELON) || aboveState.is(Blocks.PUMPKIN);
    }

    @Override
    public boolean isFertile(BlockState state, BlockGetter world, BlockPos pos) {
        return state.is(MNDBlocks.RESURGENT_SOIL_FARMLAND.get())
                && state.getValue(MOISTURE) > 0;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.canSurvive(level, pos)) {
            turnToRichSoil(state, level, pos);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int moisture = state.getValue(MOISTURE);
        boolean hasHeat = hasFireOrLava(level, pos) && !level.isRainingAt(pos.above());

        if (!hasHeat) {
            if (moisture > 0) {
                level.setBlock(pos, state.setValue(MOISTURE, moisture - 1), 2);
            }
        } else if (moisture < 7) {
            level.setBlock(pos, state.setValue(MOISTURE, 7), 2);
        }

        if (Configuration.RICH_SOIL_BOOST_CHANCE.get() == 0.0) {
            return;
        }

        int attempts = MNDConfiguration.getTickAttempts(random);
        boolean canRunMoistGrowth = hasHeat && moisture == 7;
        for (int i = 0; i < attempts; i++) {
            runGrowthAttempt(level, pos, random, canRunMoistGrowth);
        }
    }

    private void runGrowthAttempt(ServerLevel level, BlockPos pos,
                                  RandomSource random, boolean canRunMoistGrowth) {
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        Block aboveBlock = aboveState.getBlock();
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);
        Block belowBlock = belowState.getBlock();
        double boostChance = ResurgentSoilBlock.getResurgentSoilBoostChance();

        if (canRunMoistGrowth) {
            if (aboveBlock instanceof NetherWartBlock) {
                int age = aboveState.getValue(NetherWartBlock.AGE);
                if (age < NetherWartBlock.MAX_AGE) {
                    level.setBlockAndUpdate(abovePos,
                            aboveState.setValue(NetherWartBlock.AGE, age + 1));
                    return;
                }
            }

            boolean handledAbove = applyFarmlandRules(
                    aboveState, abovePos, level, random, boostChance);
            if (!handledAbove && MNDConfiguration.ENABLE_RESURGENT_SOIL_PROPAGATION.get()
                    && !aboveState.is(MNDTags.NOT_PROPAGATE_PLANT)) {
                if (aboveState.is(MNDTags.ABOVE_PROPAGATE_PLANT)
                        && random.nextDouble() < boostChance / 3.0) {
                    propagateAboveIfPossible(aboveState, abovePos, level);
                }
                if (aboveBlock instanceof DoublePlantBlock
                        && random.nextDouble() < boostChance) {
                    propagateAboveIfPossible(aboveState, abovePos, level);
                }
            }

            int bonemealAttempts = 1;
            if (random.nextFloat() <= 0.2F) {
                bonemealAttempts = 2;
                if (random.nextFloat() <= 0.01F) {
                    bonemealAttempts = 3;
                }
            }

            for (int i = 0; i < random.nextInt(bonemealAttempts) + 1; i++) {
                if (!aboveState.isAir()) {
                    performBonemealIfPossible(abovePos, level, 1, Direction.UP);
                }
                if (!belowState.isAir()) {
                    performBonemealIfPossible(belowPos, level, 1, Direction.DOWN);
                }
            }
        }

        if (MNDConfiguration.ENABLE_RESURGENT_SOIL_PROPAGATION.get()
                && !belowState.is(MNDTags.NOT_PROPAGATE_PLANT)
                && belowState.is(MNDTags.BELOW_PROPAGATE_PLANT)
                && random.nextDouble() < boostChance * 0.8) {
            propagateBelowIfPossible(belowState, belowPos, level);
        }

        if ((belowBlock instanceof GrowingPlantHeadBlock || belowBlock instanceof GrowingPlantBodyBlock)
                && random.nextDouble() < boostChance * 0.2) {
            performBonemealIfPossible(belowPos, level, 1, Direction.DOWN);
        }
        if ((aboveBlock instanceof GrowingPlantHeadBlock || aboveBlock instanceof GrowingPlantBodyBlock)
                && random.nextDouble() < boostChance * 0.2) {
            performBonemealIfPossible(abovePos, level, 1, Direction.UP);
        }
    }

    private boolean applyFarmlandRules(BlockState state, BlockPos pos, ServerLevel level,
                                       RandomSource random, double boostChance) {
        if (!MNDConfiguration.ENABLE_RESURGENT_SOIL_PROPAGATION.get()) {
            return false;
        }

        if (state.getBlock() instanceof TomatoBlock) {
            if (random.nextDouble() < boostChance / 3.0) {
                propagateAboveIfPossible(state, pos, level);
            }
            propagateAboveIfPossible(ModBlocks.BUDDING_TOMATO_CROP.get().defaultBlockState(), pos, level);
            return true;
        }

        if (state.getBlock() instanceof NetherWartBlock
                && state.getValue(NetherWartBlock.AGE) == NetherWartBlock.MAX_AGE) {
            if (random.nextDouble() < boostChance / 3.0) {
                propagateAboveIfPossible(state, pos, level);
            }
            if (random.nextFloat() < 0.125F) {
                propagateAboveIfPossible(state, pos, level);
            }
            return true;
        }
        return false;
    }

    private void performBonemealIfPossible(BlockPos position, ServerLevel level,
                                           int distance, Direction direction) {
        ResurgentSoilBlock.boostConnectedPlant(position, level, distance, direction,
                MNDConfiguration.RESURGENT_SOIL_GROWTH_RANGE.get(), TOMATO_CONTINUATION);
    }

    private void propagateAboveIfPossible(BlockState state, BlockPos position, ServerLevel level) {
        Block block = state.getBlock();
        ResurgentSoilBlock.tryPropagatingPlant(state, position, level,
                newPos -> canAboveBlockSurvive(block, level, newPos));
    }

    private static boolean canAboveBlockSurvive(Block block, ServerLevel level, BlockPos newPos) {
        BlockState blockBelowState = level.getBlockState(newPos.below());
        if (block instanceof DoublePlantBlock && level.isEmptyBlock(newPos.above())) {
            return blockBelowState.is(ModBlocks.RICH_SOIL_FARMLAND.get())
                    || blockBelowState.is(MNDBlocks.RESURGENT_SOIL_FARMLAND.get());
        }
        if (block instanceof NetherWartBlock) {
            return blockBelowState.is(Blocks.SOUL_SAND)
                    || blockBelowState.is(MNDBlocks.RESURGENT_SOIL.get())
                    || blockBelowState.is(MNDBlocks.RESURGENT_SOIL_FARMLAND.get());
        }
        return !(block instanceof DoublePlantBlock)
                && (blockBelowState.is(ModBlocks.RICH_SOIL_FARMLAND.get())
                || blockBelowState.is(MNDBlocks.RESURGENT_SOIL_FARMLAND.get()));
    }

    private void propagateBelowIfPossible(BlockState state, BlockPos position, ServerLevel level) {
        ResurgentSoilBlock.tryPropagatingPlant(state, position, level,
                newPos -> ResurgentSoilBlock.canBelowBlockSurvive(level, newPos));
    }

    @Override
    public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos,
                                   Direction facing, IPlantable plantable) {
        return true;
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return false;
    }
}
