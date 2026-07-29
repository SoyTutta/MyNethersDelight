//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.common.block;

import java.util.function.Predicate;

import com.soytutta.mynethersdelight.common.MNDConfiguration;
import com.soytutta.mynethersdelight.common.block.feasts.MagmaCakeBlock;
import com.soytutta.mynethersdelight.common.data.PlantRuleEngine;
import com.soytutta.mynethersdelight.common.data.PlantRuleSet;
import com.soytutta.mynethersdelight.common.tag.MNDTags;
import com.soytutta.mynethersdelight.common.registry.MNDBlocks;
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
import net.neoforged.neoforge.common.util.TriState;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.block.TomatoBlock;
import vectorwing.farmersdelight.common.registry.ModBlocks;

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
        for (BlockPos nearbyPos : BlockPos.betweenClosed(pos.offset(-radius, -verticalRadius, -radius), pos.offset(radius, verticalRadius, radius))) {
            BlockState state = level.getBlockState(nearbyPos);

            if (state.getFluidState().is(MNDTags.LETEOS_BOOSTER)) {
                int lightLevel = state.getLightEmission(level, nearbyPos);
                int distance = pos.distManhattan(nearbyPos);
                if (distance <= lightLevel) {
                    return true;
                }
            }

            if (state.is(MNDTags.LETIOS_FLAMES)) {
                if (!state.hasProperty(BlockStateProperties.LIT) || state.getValue(BlockStateProperties.LIT)) {
                    int lightLevel = state.getLightEmission(level, nearbyPos);
                    int distance = pos.distManhattan(nearbyPos);
                    if (state.getBlock() instanceof TorchBlock || state.getBlock() instanceof WallTorchBlock) {
                        lightLevel = lightLevel / 2;
                    } else if (state.getBlock() instanceof LanternBlock) {
                        lightLevel = (lightLevel / 2) + 2;
                    } else if (state.getBlock() instanceof MagmaCakeBlock || state.getBlock() instanceof  MagmaBlock) {
                        lightLevel = lightLevel + 3;
                    }

                    if (distance <= lightLevel) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void turnToRichSoil(BlockState state, Level level, BlockPos pos) {
        level.setBlockAndUpdate(pos, pushEntitiesUp(state, MNDBlocks.RESURGENT_SOIL.get().defaultBlockState(), level, pos));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return !this.defaultBlockState().canSurvive(context.getLevel(), context.getClickedPos()) ? MNDBlocks.RESURGENT_SOIL.get().defaultBlockState() : super.getStateForPlacement(context);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        entity.causeFallDamage(fallDistance, 1.0F, entity.damageSources().fall());
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState aboveState = level.getBlockState(pos.above());
        return super.canSurvive(state, level, pos) || aboveState.is(Blocks.MELON) || aboveState.is(Blocks.PUMPKIN);
    }

    @Override
    public boolean isFertile(BlockState state, BlockGetter world, BlockPos pos) {
        if (state.is(MNDBlocks.RESURGENT_SOIL_FARMLAND.get())) {
            return state.getValue(MOISTURE) > 0;
        }
        return false;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
        if (!state.canSurvive(level, pos)) {
            turnToRichSoil(state, level, pos);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.isClientSide) return;

        int moisture = state.getValue(MOISTURE);
        boolean hasHeat = hasFireOrLava(level, pos) && !level.isRainingAt(pos.above());

        BlockPos initialAbovePos = pos.above();
        PlantRuleEngine.applySoilTransformRules(level, initialAbovePos, level.getBlockState(initialAbovePos),
                PlantRuleSet.SoilType.RESURGENT_FARMLAND, random);

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

        int attempts = PlantRuleEngine.getTickAttempts(random, MNDConfiguration.RESURGENT_SOIL_TICK_MULTIPLIER.get());
        boolean canRunMoistGrowth = hasHeat && moisture == 7;
        for (int i = 0; i < attempts; i++) {
            runGrowthAttempt(level, pos, random, canRunMoistGrowth);
        }
    }

    private void runGrowthAttempt(ServerLevel level, BlockPos pos, RandomSource random, boolean canRunMoistGrowth) {
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        Block aboveBlock = aboveState.getBlock();

        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);
        Block belowBlock = belowState.getBlock();

        double ResurgentSoilBoostChance = PlantRuleEngine.getResurgentSoilBoostChance();

        if (canRunMoistGrowth) {
            if (aboveBlock instanceof NetherWartBlock) {
                int age = aboveState.getValue(NetherWartBlock.AGE);
                if (age < NetherWartBlock.MAX_AGE) {
                    level.setBlockAndUpdate(abovePos, aboveState.setValue(NetherWartBlock.AGE, age + 1));
                    return;
                }
            }

            boolean handledAbove = PlantRuleEngine.applyPropagationRules(level, abovePos, aboveState,
                    PlantRuleSet.RuleDirection.ABOVE, PlantRuleSet.SoilType.RESURGENT_FARMLAND, random,
                    propagatedState -> propagateAboveIfPossible(propagatedState, abovePos, level),
                    () -> performBonemealIfPossible(abovePos, level, 1, Direction.UP));

            if (!handledAbove && !aboveState.is(MNDTags.NOT_PROPAGATE_PLANT)) {
                if (aboveState.is(MNDTags.ABOVE_PROPAGATE_PLANT)
                        && PlantRuleEngine.rollPropagation(random, ResurgentSoilBoostChance / 3)) {
                    propagateAboveIfPossible(aboveBlock, abovePos, level);
                }

                if (aboveBlock instanceof DoublePlantBlock
                        && PlantRuleEngine.rollPropagation(random, ResurgentSoilBoostChance)) {
                    propagateAboveIfPossible(aboveBlock, abovePos, level);
                }
            }

            int BonemealChance = 1;
            if (random.nextFloat() <= 0.2f) {
                BonemealChance = 2;
                if (random.nextFloat() <= 0.01f) {
                    BonemealChance = 3;
                }
            }

            for (int i = 0; i < random.nextInt(BonemealChance) + 1; i++) {
                if (!aboveState.isAir()) {
                    performBonemealIfPossible(abovePos, level, 1, Direction.UP);
                }
                if (!belowState.isAir()) {
                    performBonemealIfPossible(belowPos, level, 1, Direction.DOWN);
                }
            }
        }

        boolean handledBelow = PlantRuleEngine.applyPropagationRules(level, belowPos, belowState,
                PlantRuleSet.RuleDirection.BELOW, PlantRuleSet.SoilType.RESURGENT_FARMLAND, random,
                propagatedState -> propagateBelowIfPossible(propagatedState, belowPos, level),
                () -> performBonemealIfPossible(belowPos, level, 1, Direction.DOWN));

        if (!handledBelow && !belowState.is(MNDTags.NOT_PROPAGATE_PLANT)
                && belowState.is(MNDTags.BELOW_PROPAGATE_PLANT)
                && PlantRuleEngine.rollPropagation(random, ResurgentSoilBoostChance * 0.8F)) {
            propagateBelowIfPossible(belowBlock, belowPos, level);
        }

        if ((belowBlock instanceof GrowingPlantHeadBlock || belowBlock instanceof GrowingPlantBodyBlock)
                && random.nextFloat() <= (ResurgentSoilBoostChance * 0.2F)) {
            performBonemealIfPossible(belowPos, level, 1, Direction.DOWN);
        }

        if ((aboveBlock instanceof GrowingPlantHeadBlock || aboveBlock instanceof GrowingPlantBodyBlock)
                && random.nextFloat() <= (ResurgentSoilBoostChance * 0.2F)) {
            performBonemealIfPossible(abovePos, level, 1, Direction.UP);
        }
    }

    private void performBonemealIfPossible(BlockPos position, ServerLevel level, int distance, Direction direction) {
        ResurgentSoilBlock.boostConnectedPlant(position, level, distance, direction,
                MNDConfiguration.RESURGENT_SOIL_GROWTH_RANGE.get(), TOMATO_CONTINUATION);
    }

    private void propagateAboveIfPossible(Block block, BlockPos position, ServerLevel level) {
        propagateAboveIfPossible(block.defaultBlockState(), position, level);
    }

    private void propagateAboveIfPossible(BlockState propagatedState, BlockPos position, ServerLevel level) {
        Block block = propagatedState.getBlock();
        ResurgentSoilBlock.tryPropagatingPlant(propagatedState, position, level,
                newPos -> canAboveBlockSurvive(block, level, newPos));
    }

    private static boolean canAboveBlockSurvive(Block block, ServerLevel level, BlockPos newPos) {
        BlockState blockBelowState = level.getBlockState(newPos.below());
        if (block instanceof DoublePlantBlock &&
                level.getBlockState(newPos.above()).getBlock() == Blocks.AIR) {
            return blockBelowState.getBlock() == ModBlocks.RICH_SOIL_FARMLAND.get()
                    || blockBelowState.getBlock() == MNDBlocks.RESURGENT_SOIL_FARMLAND.get();
        } else if (block instanceof NetherWartBlock) {
            return blockBelowState.getBlock() == Blocks.SOUL_SAND
                    || blockBelowState.getBlock() == MNDBlocks.RESURGENT_SOIL.get()
                    || blockBelowState.getBlock() == MNDBlocks.RESURGENT_SOIL_FARMLAND.get();
        } else if (!(block instanceof DoublePlantBlock)) {
            return blockBelowState.getBlock() == ModBlocks.RICH_SOIL_FARMLAND.get()
                    || blockBelowState.getBlock() == MNDBlocks.RESURGENT_SOIL_FARMLAND.get();
        }
        return false;
    }

    private void propagateBelowIfPossible(Block block, BlockPos position, ServerLevel level) {
        propagateBelowIfPossible(block.defaultBlockState(), position, level);
    }

    private void propagateBelowIfPossible(BlockState propagatedState, BlockPos position, ServerLevel level) {
        ResurgentSoilBlock.tryPropagatingPlant(propagatedState, position, level,
                newPos -> ResurgentSoilBlock.canBelowBlockSurvive(level, newPos));
    }

    @Override
    public TriState canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, BlockState plantState) {
        return TriState.TRUE;
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return false;
    }
}
