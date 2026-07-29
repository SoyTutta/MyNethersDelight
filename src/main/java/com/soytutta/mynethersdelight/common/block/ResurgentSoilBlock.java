//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package com.soytutta.mynethersdelight.common.block;

import com.soytutta.mynethersdelight.common.MNDConfiguration;
import com.soytutta.mynethersdelight.common.data.PlantRuleEngine;
import com.soytutta.mynethersdelight.common.data.PlantRuleSet;
import com.soytutta.mynethersdelight.common.registry.MNDBlocks;
import com.soytutta.mynethersdelight.common.tag.MNDTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.util.TriState;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.block.MushroomColonyBlock;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.tag.ModTags;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static net.minecraft.world.level.block.PinkPetalsBlock.FACING;

public class ResurgentSoilBlock extends Block {
    private static final Predicate<BlockState> NO_ADDITIONAL_CONTINUATION = state -> false;
    private static final Predicate<BlockState> NO_REPLACEMENT = state -> false;

    public ResurgentSoilBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
        if (level.isClientSide) return;

        int attempts = PlantRuleEngine.getTickAttempts(rand, MNDConfiguration.RESURGENT_SOIL_TICK_MULTIPLIER.get());
        for (int i = 0; i < attempts; i++) {
            runGrowthAttempt(level, pos, rand);
        }
    }

    private void runGrowthAttempt(ServerLevel level, BlockPos pos, RandomSource rand) {

        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        Block aboveBlock = aboveState.getBlock();

        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);
        Block belowBlock = belowState.getBlock();

        PlantRuleEngine.applySoilTransformRules(level, abovePos, aboveState,
                PlantRuleSet.SoilType.RESURGENT_SOIL, rand);

        if (Configuration.RICH_SOIL_BOOST_CHANCE.get() == 0.0) return;
        double ResurgentSoilBoostChance = PlantRuleEngine.getResurgentSoilBoostChance();

        boolean handledAbove = PlantRuleEngine.applyPropagationRules(level, abovePos, aboveState,
                PlantRuleSet.RuleDirection.ABOVE, PlantRuleSet.SoilType.RESURGENT_SOIL, rand,
                propagatedState -> propagateAboveIfPossible(propagatedState, abovePos, level),
                () -> performBonemealIfPossible(abovePos, level, 1, Direction.UP));

        if (!handledAbove && !aboveState.is(MNDTags.NOT_PROPAGATE_PLANT)) {
            if (aboveState.is(MNDTags.ABOVE_PROPAGATE_PLANT) && PlantRuleEngine.rollPropagation(rand, ResurgentSoilBoostChance * 0.6F)) {
                propagateAboveIfPossible(aboveBlock, abovePos, level);
            } else if (aboveBlock instanceof FlowerBlock && PlantRuleEngine.rollPropagation(rand, ResurgentSoilBoostChance * 0.8F)) {
                propagateAboveIfPossible(aboveBlock, abovePos, level);
            } else if ((aboveBlock instanceof FungusBlock || aboveBlock instanceof MushroomBlock) && PlantRuleEngine.rollPropagation(rand, ResurgentSoilBoostChance * 0.4F)) {
                propagateAboveIfPossible(aboveBlock, abovePos, level);
            } else if (aboveBlock instanceof MushroomColonyBlock colony) {
                int age = aboveState.getValue(MushroomColonyBlock.COLONY_AGE);
                if (age == 3 && PlantRuleEngine.rollPropagation(rand, ResurgentSoilBoostChance * 0.4F)) {
                    propagateAboveIfPossible(aboveBlock, abovePos, level);
                }
                if (PlantRuleEngine.rollPropagation(rand, 0.4F)) {
                    Block baseMushroom = Block.byItem(colony.mushroomType.value());
                    propagateAboveIfPossible(baseMushroom != Blocks.AIR ? baseMushroom : aboveBlock, abovePos, level);
                }
            } else if (aboveBlock instanceof DoublePlantBlock && PlantRuleEngine.rollPropagation(rand, ResurgentSoilBoostChance / 2)) {
                propagateAboveIfPossible(aboveBlock, abovePos, level);
            } else if (aboveBlock instanceof BushBlock && !(aboveBlock instanceof DoublePlantBlock)
                    && PlantRuleEngine.rollPropagation(rand, ResurgentSoilBoostChance / 3)) {
                propagateAboveIfPossible(aboveBlock, abovePos, level);
            }
        }

        boolean handledBelow = PlantRuleEngine.applyPropagationRules(level, belowPos, belowState,
                PlantRuleSet.RuleDirection.BELOW, PlantRuleSet.SoilType.RESURGENT_SOIL, rand,
                propagatedState -> propagateBelowIfPossible(propagatedState, belowPos, level),
                () -> performBonemealIfPossible(belowPos, level, 1, Direction.DOWN));

        if (!handledBelow && !belowState.is(MNDTags.NOT_PROPAGATE_PLANT)) {
            if (belowState.is(MNDTags.BELOW_PROPAGATE_PLANT) && PlantRuleEngine.rollPropagation(rand, ResurgentSoilBoostChance * 0.8F)) {
                propagateBelowIfPossible(belowBlock, belowPos, level);
            }
        }

        int BonemealChance = 1;
        if (rand.nextFloat() <= 0.2f) {
            BonemealChance = 2;
            if (rand.nextFloat() <= 0.01f) BonemealChance = 3;
        }

        for (int i = 0; i < rand.nextInt(BonemealChance) + 1; i++) {
            if (!aboveState.isAir()) {
                performBonemealIfPossible(abovePos, level, 1, Direction.UP);
            }
            if (!belowState.isAir()) {
                performBonemealIfPossible(belowPos, level, 1, Direction.DOWN);
            }
        }

        growColumnIfPossible(aboveState, abovePos, level, 7);
    }

    private void performBonemealIfPossible(BlockPos position, ServerLevel level, int distance, Direction direction) {
        boostConnectedPlant(position, level, distance, direction,
                MNDConfiguration.RESURGENT_SOIL_GROWTH_RANGE.get(), NO_ADDITIONAL_CONTINUATION);
    }

    static void boostConnectedPlant(BlockPos position, ServerLevel level, int distance, Direction direction,
                                    int maxDistance, Predicate<BlockState> additionalContinuation) {
        if (distance > maxDistance) return;

        BlockState state = level.getBlockState(position);
        Block block = state.getBlock();

        if (state.isAir() || state.is(ModTags.Blocks.UNAFFECTED_BY_RICH_SOIL) || block instanceof TallFlowerBlock) {
            return;
        }

        if (block instanceof BonemealableBlock growable && level.random.nextFloat() <= Configuration.RICH_SOIL_BOOST_CHANCE.get() / distance) {
            if (growable.isValidBonemealTarget(level, position, state) && CommonHooks.canCropGrow(level, position, state, true)) {
                growable.performBonemeal(level, level.random, position, state);
                CommonHooks.fireCropGrowPost(level, position, state);
                for (int i = 0; i < 3; i++) {
                    double d0 = (double) position.getX() + level.getRandom().nextDouble();
                    double d1 = (double) position.getY() + level.getRandom().nextDouble();
                    double d2 = (double) position.getZ() + level.getRandom().nextDouble();
                    level.sendParticles(ParticleTypes.SOUL, d0, d1, d2, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                }
                level.playSound(null, position, SoundEvents.SOUL_ESCAPE.value(), SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }

        BlockPos nextPos = position.relative(direction);
        BlockState nextState = level.getBlockState(nextPos);

        boolean isSameBlock = nextState.is(block);
        boolean isGrowingPlant = nextState.getBlock() instanceof GrowingPlantBlock || nextState.getBlock() instanceof GrowingPlantHeadBlock;

        if (isSameBlock || isGrowingPlant || additionalContinuation.test(nextState)) {
            boostConnectedPlant(nextPos, level, distance + 1, direction, maxDistance, additionalContinuation);
        }
    }

    public static void growIfPossible(BlockState aboveBlock, BlockPos abovePos, ServerLevel level, Block targetBlock, int maxHeight) {
        if (aboveBlock.getBlock() == targetBlock && level.random.nextFloat() <= (Configuration.RICH_SOIL_BOOST_CHANCE.get() * 0.6F)) {
            BlockPos topPos = abovePos;
            int height = 1;
            while (level.getBlockState(topPos.above()).getBlock() == targetBlock) {
                topPos = topPos.above();
                height++;
            }
            if (level.getBlockState(topPos.above()).isAir() && height < maxHeight) {
                level.setBlockAndUpdate(topPos.above(), targetBlock.defaultBlockState());
                for (int i = 0; i < 3; i++) {
                    double d0 = (double) topPos.above().getX() + level.getRandom().nextDouble();
                    double d1 = (double) topPos.above().getY() + level.getRandom().nextDouble();
                    double d2 = (double) topPos.above().getZ() + level.getRandom().nextDouble();
                    level.sendParticles(ParticleTypes.SOUL, d0, d1, d2, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                }
                level.playSound(null, topPos.above(), SoundEvents.SOUL_ESCAPE.value(), SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    private static void growColumnIfPossible(BlockState state, BlockPos pos, ServerLevel level, int maxHeight) {
        Block block = state.getBlock();
        if (block instanceof SugarCaneBlock || block instanceof CactusBlock) {
            growIfPossible(state, pos, level, block, maxHeight);
        }
    }

    private void propagateAboveIfPossible(Block block, BlockPos position, ServerLevel level) {
        propagateAboveIfPossible(block.defaultBlockState(), position, level);
    }

    private void propagateAboveIfPossible(BlockState propagatedState, BlockPos position, ServerLevel level) {
        Block block = propagatedState.getBlock();
        tryPropagatingPlant(propagatedState, position, level,
                newPos -> canAboveBlockSurvive(block, level.getBlockState(newPos), level, newPos),
                targetState -> block instanceof WitherRoseBlock && (targetState.is(BlockTags.SMALL_FLOWERS)
                        || targetState.getBlock() instanceof FungusBlock
                        || targetState.getBlock() instanceof MushroomBlock));
    }

    private static boolean canAboveBlockSurvive(Block block, BlockState newState, ServerLevel level, BlockPos newPos) {
        BlockState blockBelowState = level.getBlockState(newPos.below());
        if (block instanceof WitherRoseBlock
                && ((newState.is(BlockTags.SMALL_FLOWERS)
                || newState.getBlock() instanceof FungusBlock
                || newState.getBlock() instanceof MushroomBlock)
                && !(newState.getBlock() instanceof WitherRoseBlock))) {
            return true;  // Wither Rose can replace other FlowerBlocks
        } else if (block instanceof DoublePlantBlock) {
            BlockState doublePlantState = block.defaultBlockState();
            if (!(block instanceof LiquidBlockContainer && !(block instanceof SimpleWaterloggedBlock)) && level.isEmptyBlock(newPos.above())) {
                return doublePlantState.canSurvive(level, newPos);
            } else if (level.getFluidState(newPos.above()).is(FluidTags.WATER) && level.getFluidState(newPos.above()).getAmount() == 8) {
                return doublePlantState.canSurvive(level, newPos);
            }
        } else if (block instanceof NetherWartBlock) {
            return blockBelowState.getBlock() == Blocks.SOUL_SAND
                    || blockBelowState.getBlock() == MNDBlocks.RESURGENT_SOIL.get()
                    || blockBelowState.getBlock() == MNDBlocks.RESURGENT_SOIL_FARMLAND.get();
        } else if (!(block instanceof WitherRoseBlock || block instanceof DoublePlantBlock)) {
            return blockBelowState.getBlock() == ModBlocks.RICH_SOIL.get()
                    || blockBelowState.getBlock() == MNDBlocks.RESURGENT_SOIL.get();
        }
        return false;
    }

    private void propagateBelowIfPossible(Block block, BlockPos position, ServerLevel level) {
        propagateBelowIfPossible(block.defaultBlockState(), position, level);
    }

    private void propagateBelowIfPossible(BlockState propagatedState, BlockPos position, ServerLevel level) {
        tryPropagatingPlant(propagatedState, position, level,
                newPos -> canBelowBlockSurvive(level, newPos));
    }

    static void tryPropagatingPlant(BlockState propagatedState, BlockPos position, ServerLevel level,
                                    Predicate<BlockPos> canSurvive) {
        tryPropagatingPlant(propagatedState, position, level, canSurvive, NO_REPLACEMENT);
    }

    static void tryPropagatingPlant(BlockState propagatedState, BlockPos position, ServerLevel level,
                                    Predicate<BlockPos> canSurvive, Predicate<BlockState> canReplace) {
        Block block = propagatedState.getBlock();
        List<BlockPos> validPositions = new ArrayList<>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos newPos = position.offset(x, y, z);

                    if (canSurvive.test(newPos)) {
                        validPositions.add(newPos);
                    }
                }
            }
        }

        if (!validPositions.isEmpty()) {
            BlockPos plantPos = validPositions.get(level.random.nextInt(validPositions.size()));
            BlockState targetState = level.getBlockState(plantPos);

            boolean canPropagate = (block instanceof LiquidBlockContainer && targetState.getBlock() == Blocks.WATER)
                    || (!(block instanceof LiquidBlockContainer) && targetState.getBlock() == Blocks.AIR)
                    || canReplace.test(targetState)
                    || (block instanceof SimpleWaterloggedBlock && (targetState.getBlock() == Blocks.AIR || targetState.getBlock() == Blocks.WATER));

            if (canPropagate) {
                placeBlock(propagatedState, level, plantPos);
            }
        }
    }

    static boolean canBelowBlockSurvive(ServerLevel level, BlockPos newPos) {
        BlockState blockAboveState = level.getBlockState(newPos.above());
        return blockAboveState.getBlock() == ModBlocks.RICH_SOIL.get()
                || blockAboveState.getBlock() == MNDBlocks.RESURGENT_SOIL.get()
                || blockAboveState.getBlock() == ModBlocks.RICH_SOIL_FARMLAND.get()
                || blockAboveState.getBlock() == MNDBlocks.RESURGENT_SOIL_FARMLAND.get();
    }

    private static void placeBlock(BlockState state, ServerLevel level, BlockPos pos) {
        Block block = state.getBlock();
        if (block instanceof SimpleWaterloggedBlock) {
            FluidState fluidState = level.getFluidState(pos);
            if (fluidState.getType() == Fluids.WATER) {
                state = state.setValue(BlockStateProperties.WATERLOGGED, true);
            } else {
                state = state.setValue(BlockStateProperties.WATERLOGGED, false);
            }
        }
        if (block instanceof PinkPetalsBlock) {
            Direction[] allowedDirections = {Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};
            Direction randomDirection = allowedDirections[level.random.nextInt(allowedDirections.length)];
            level.setBlockAndUpdate(pos, state.setValue(FACING, randomDirection));
        } else if (block instanceof DoublePlantBlock) {
            DoublePlantBlock.placeAt(level, state, pos, 3);
        } else {
            level.setBlockAndUpdate(pos, state);
        }
        for (int i = 0; i < 3; i++) {
            double d0 = (double) pos.getX() + level.getRandom().nextDouble();
            double d1 = (double) pos.getY() + level.getRandom().nextDouble();
            double d2 = (double) pos.getZ() + level.getRandom().nextDouble();
            level.sendParticles(ParticleTypes.SOUL, d0, d1, d2, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
        level.playSound(null, pos, SoundEvents.SOUL_ESCAPE.value(), SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    @Nullable
    public BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility toolAction, boolean simulate) {
        if (toolAction.equals(ItemAbilities.HOE_TILL) && context.getLevel().getBlockState(context.getClickedPos().above()).isAir()) {
            return  MNDBlocks.RESURGENT_SOIL_FARMLAND.get().defaultBlockState();
        }
        return null;
    }

    @Override
    public TriState canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, BlockState plantState) {
        if (plantState.getBlock() instanceof NetherWartBlock) {
            return TriState.TRUE;
        }

        if (plantState.is(MNDTags.RESURGENT_SOIL_PLANT) && !(plantState.getBlock() instanceof CropBlock || plantState.is(BlockTags.CROPS))) {
            return TriState.TRUE;
        }

        return TriState.DEFAULT;
    }
}
