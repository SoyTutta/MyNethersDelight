package com.soytutta.mynethersdelight.common.block;

import com.soytutta.mynethersdelight.common.MNDConfiguration;
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
import net.minecraftforge.common.*;
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
    private static final double BOOST_MULTIPLIER = 1.05;
    private static final Predicate<BlockState> NO_ADDITIONAL_CONTINUATION = state -> false;
    private static final Predicate<BlockState> NO_REPLACEMENT = state -> false;

    public ResurgentSoilBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int attempts = MNDConfiguration.getTickAttempts(random);
        for (int i = 0; i < attempts; i++) {
            runGrowthAttempt(level, pos, random);
        }
    }

    private void runGrowthAttempt(ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        Block aboveBlock = aboveState.getBlock();

        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);
        Block belowBlock = belowState.getBlock();

        transformPlant(aboveState, abovePos, level);

        if (Configuration.RICH_SOIL_BOOST_CHANCE.get() == 0.0) {
            return;
        }

        double boostChance = getResurgentSoilBoostChance();
        boolean handledAbove = applyAboveRules(aboveState, abovePos, level, random, boostChance);

        if (!handledAbove && MNDConfiguration.ENABLE_RESURGENT_SOIL_PROPAGATION.get()
                && !aboveState.is(MNDTags.NOT_PROPAGATE_PLANT)) {
            if (aboveState.is(MNDTags.ABOVE_PROPAGATE_PLANT) && random.nextDouble() < boostChance * 0.6) {
                propagateAboveIfPossible(aboveState, abovePos, level);
            } else if (aboveBlock instanceof FlowerBlock && random.nextDouble() < boostChance * 0.8) {
                propagateAboveIfPossible(aboveState, abovePos, level);
            } else if ((aboveBlock instanceof FungusBlock || aboveBlock instanceof MushroomBlock)
                    && random.nextDouble() < boostChance * 0.4) {
                propagateAboveIfPossible(aboveState, abovePos, level);
            } else if (aboveBlock instanceof MushroomColonyBlock colony) {
                if (aboveState.getValue(MushroomColonyBlock.COLONY_AGE) == 3
                        && random.nextDouble() < boostChance * 0.4) {
                    propagateAboveIfPossible(aboveState, abovePos, level);
                }
                if (random.nextFloat() < 0.4F) {
                    Block baseMushroom = Block.byItem(colony.mushroomType.get());
                    propagateAboveIfPossible(baseMushroom != Blocks.AIR
                            ? baseMushroom.defaultBlockState() : aboveState, abovePos, level);
                }
            } else if (aboveBlock instanceof DoublePlantBlock && random.nextDouble() < boostChance / 2.0) {
                propagateAboveIfPossible(aboveState, abovePos, level);
            } else if (aboveBlock instanceof BushBlock && !(aboveBlock instanceof DoublePlantBlock)
                    && random.nextDouble() < boostChance / 3.0) {
                propagateAboveIfPossible(aboveState, abovePos, level);
            }
        }

        if (MNDConfiguration.ENABLE_RESURGENT_SOIL_PROPAGATION.get()
                && !belowState.is(MNDTags.NOT_PROPAGATE_PLANT)
                && belowState.is(MNDTags.BELOW_PROPAGATE_PLANT)
                && random.nextDouble() < boostChance * 0.8) {
            propagateBelowIfPossible(belowState, belowPos, level);
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

        growColumnIfPossible(aboveState, abovePos, level, 7);
    }

    private static void transformPlant(BlockState state, BlockPos pos, ServerLevel level) {
        if (state.is(Blocks.CRIMSON_FUNGUS)) {
            level.setBlockAndUpdate(pos, MNDBlocks.CRIMSON_FUNGUS_COLONY.get().defaultBlockState());
        } else if (state.is(Blocks.WARPED_FUNGUS)) {
            level.setBlockAndUpdate(pos, MNDBlocks.WARPED_FUNGUS_COLONY.get().defaultBlockState());
        } else if (state.is(Blocks.BROWN_MUSHROOM)) {
            level.setBlockAndUpdate(pos, ModBlocks.BROWN_MUSHROOM_COLONY.get().defaultBlockState());
        } else if (state.is(Blocks.RED_MUSHROOM)) {
            level.setBlockAndUpdate(pos, ModBlocks.RED_MUSHROOM_COLONY.get().defaultBlockState());
        }
    }

    private boolean applyAboveRules(BlockState state, BlockPos pos, ServerLevel level,
                                    RandomSource random, double boostChance) {
        Block block = state.getBlock();
        boolean propagationEnabled = MNDConfiguration.ENABLE_RESURGENT_SOIL_PROPAGATION.get();

        if (state.is(Blocks.CRIMSON_FUNGUS) || state.is(Blocks.WARPED_FUNGUS)
                || state.is(Blocks.BROWN_MUSHROOM) || state.is(Blocks.RED_MUSHROOM)) {
            if (propagationEnabled && random.nextDouble() < boostChance * 0.4) {
                propagateAboveIfPossible(state, pos, level);
            }
            return propagationEnabled;
        }

        if (block instanceof MushroomColonyBlock colony) {
            if (propagationEnabled && state.getValue(MushroomColonyBlock.COLONY_AGE) == 3
                    && random.nextDouble() < boostChance * 0.4) {
                propagateAboveIfPossible(state, pos, level);
            }
            if (propagationEnabled && random.nextFloat() < 0.4F) {
                Block baseMushroom = Block.byItem(colony.mushroomType.get());
                propagateAboveIfPossible(baseMushroom != Blocks.AIR
                        ? baseMushroom.defaultBlockState() : state, pos, level);
            }
            return propagationEnabled;
        }

        if (state.is(MNDBlocks.POWDERY_TORCH.get())) {
            if (propagationEnabled && random.nextDouble() < boostChance / 15.0
                    && level.isEmptyBlock(pos.above())) {
                level.setBlockAndUpdate(pos, MNDBlocks.POWDERY_CANE.get().defaultBlockState()
                        .setValue(PowderyCaneBlock.AGE, 1));
                level.setBlockAndUpdate(pos.above(), MNDBlocks.BULLET_PEPPER.get().defaultBlockState()
                        .setValue(PowderyCaneBlock.AGE, 2)
                        .setValue(PowderyCaneBlock.LIT, true));
            }
            return propagationEnabled;
        }

        if (state.is(MNDBlocks.POWDERY_CANE.get())) {
            if (propagationEnabled && random.nextDouble() < boostChance * 0.4) {
                propagateAboveIfPossible(MNDBlocks.BULLET_PEPPER.get().defaultBlockState(), pos, level);
            }
            return propagationEnabled;
        }

        if (state.is(MNDBlocks.POWDERY_CANNON.get())) {
            if (propagationEnabled && random.nextDouble() < boostChance * 0.2) {
                propagateAboveIfPossible(MNDBlocks.POWDERY_CHUBBY_SAPLING.get().defaultBlockState(), pos, level);
            }
            return propagationEnabled;
        }

        if (state.is(Blocks.BAMBOO)) {
            if (propagationEnabled && random.nextDouble() < boostChance * 0.2) {
                propagateAboveIfPossible(Blocks.BAMBOO_SAPLING.defaultBlockState(), pos, level);
            }
            return propagationEnabled;
        }

        if (block instanceof PinkPetalsBlock) {
            if (state.getValue(PinkPetalsBlock.AMOUNT) < PinkPetalsBlock.MAX_FLOWERS) {
                performBonemealIfPossible(pos, level, 1, Direction.UP);
            } else if (propagationEnabled && random.nextInt(6) == 0) {
                propagateAboveIfPossible(state, pos, level);
            }
            return propagationEnabled;
        }

        return false;
    }

    static double getResurgentSoilBoostChance() {
        return Configuration.RICH_SOIL_BOOST_CHANCE.get() * BOOST_MULTIPLIER;
    }

    private void performBonemealIfPossible(BlockPos position, ServerLevel level, int distance, Direction direction) {
        boostConnectedPlant(position, level, distance, direction,
                MNDConfiguration.RESURGENT_SOIL_GROWTH_RANGE.get(), NO_ADDITIONAL_CONTINUATION);
    }

    static void boostConnectedPlant(BlockPos position, ServerLevel level, int distance, Direction direction,
                                    int maxDistance, Predicate<BlockState> additionalContinuation) {
        if (distance > maxDistance) {
            return;
        }

        BlockState state = level.getBlockState(position);
        Block block = state.getBlock();
        if (state.isAir() || state.is(ModTags.Blocks.UNAFFECTED_BY_RICH_SOIL)
                || block instanceof TallFlowerBlock) {
            return;
        }

        if (block instanceof BonemealableBlock growable
                && level.random.nextFloat() <= Configuration.RICH_SOIL_BOOST_CHANCE.get() / distance
                && growable.isValidBonemealTarget(level, position, state, false)
                && ForgeHooks.onCropsGrowPre(level, position, state, true)) {
            growable.performBonemeal(level, level.random, position, state);
            ForgeHooks.onCropsGrowPost(level, position, state);
            playGrowthEffects(level, position);
        }

        BlockPos nextPos = position.relative(direction);
        BlockState nextState = level.getBlockState(nextPos);
        boolean isSameBlock = nextState.is(block);
        boolean isGrowingPlant = nextState.getBlock() instanceof GrowingPlantBlock
                || nextState.getBlock() instanceof GrowingPlantHeadBlock;

        if (isSameBlock || isGrowingPlant || additionalContinuation.test(nextState)) {
            boostConnectedPlant(nextPos, level, distance + 1, direction, maxDistance, additionalContinuation);
        }
    }

    private static void growColumnIfPossible(BlockState state, BlockPos pos, ServerLevel level, int maxHeight) {
        Block block = state.getBlock();
        if (block instanceof SugarCaneBlock || block instanceof CactusBlock) {
            growIfPossible(state, pos, level, block, maxHeight);
        }
    }

    public static void growIfPossible(BlockState state, BlockPos pos, ServerLevel level,
                                      Block targetBlock, int maxHeight) {
        if (state.is(targetBlock)
                && level.random.nextFloat() <= Configuration.RICH_SOIL_BOOST_CHANCE.get() * 0.6F) {
            BlockPos topPos = pos;
            int height = 1;
            while (level.getBlockState(topPos.above()).is(targetBlock)) {
                topPos = topPos.above();
                height++;
            }
            if (level.getBlockState(topPos.above()).isAir() && height < maxHeight) {
                level.setBlockAndUpdate(topPos.above(), targetBlock.defaultBlockState());
                playGrowthEffects(level, topPos.above());
            }
        }
    }

    private void propagateAboveIfPossible(Block block, BlockPos position, ServerLevel level) {
        propagateAboveIfPossible(block.defaultBlockState(), position, level);
    }

    private void propagateAboveIfPossible(BlockState state, BlockPos position, ServerLevel level) {
        Block block = state.getBlock();
        tryPropagatingPlant(state, position, level,
                newPos -> canAboveBlockSurvive(block, level.getBlockState(newPos), level, newPos),
                targetState -> block instanceof WitherRoseBlock
                        && (targetState.is(BlockTags.SMALL_FLOWERS)
                        || targetState.getBlock() instanceof FungusBlock
                        || targetState.getBlock() instanceof MushroomBlock));
    }

    private static boolean canAboveBlockSurvive(Block block, BlockState newState,
                                                ServerLevel level, BlockPos newPos) {
        BlockState blockBelowState = level.getBlockState(newPos.below());
        if (block instanceof WitherRoseBlock
                && ((newState.is(BlockTags.SMALL_FLOWERS)
                || newState.getBlock() instanceof FungusBlock
                || newState.getBlock() instanceof MushroomBlock)
                && !(newState.getBlock() instanceof WitherRoseBlock))) {
            return true;
        }
        if (block instanceof DoublePlantBlock) {
            BlockState doublePlantState = block.defaultBlockState();
            if (!(block instanceof LiquidBlockContainer && !(block instanceof SimpleWaterloggedBlock))
                    && level.isEmptyBlock(newPos.above())) {
                return doublePlantState.canSurvive(level, newPos);
            }
            if (level.getFluidState(newPos.above()).is(FluidTags.WATER)
                    && level.getFluidState(newPos.above()).getAmount() == 8) {
                return doublePlantState.canSurvive(level, newPos);
            }
        } else if (block instanceof NetherWartBlock) {
            return blockBelowState.is(Blocks.SOUL_SAND)
                    || blockBelowState.is(MNDBlocks.RESURGENT_SOIL.get())
                    || blockBelowState.is(MNDBlocks.RESURGENT_SOIL_FARMLAND.get());
        } else if (!(block instanceof WitherRoseBlock)) {
            return blockBelowState.is(ModBlocks.RICH_SOIL.get())
                    || blockBelowState.is(MNDBlocks.RESURGENT_SOIL.get());
        }
        return false;
    }

    private void propagateBelowIfPossible(BlockState state, BlockPos position, ServerLevel level) {
        tryPropagatingPlant(state, position, level, newPos -> canBelowBlockSurvive(level, newPos));
    }

    static void tryPropagatingPlant(BlockState state, BlockPos position, ServerLevel level,
                                    Predicate<BlockPos> canSurvive) {
        tryPropagatingPlant(state, position, level, canSurvive, NO_REPLACEMENT);
    }

    static void tryPropagatingPlant(BlockState state, BlockPos position, ServerLevel level,
                                    Predicate<BlockPos> canSurvive, Predicate<BlockState> canReplace) {
        Block block = state.getBlock();
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

        if (validPositions.isEmpty()) {
            return;
        }

        BlockPos plantPos = validPositions.get(level.random.nextInt(validPositions.size()));
        BlockState targetState = level.getBlockState(plantPos);
        boolean canPropagate = block instanceof LiquidBlockContainer && targetState.is(Blocks.WATER)
                || !(block instanceof LiquidBlockContainer) && targetState.isAir()
                || canReplace.test(targetState)
                || block instanceof SimpleWaterloggedBlock
                && (targetState.isAir() || targetState.is(Blocks.WATER));

        if (canPropagate) {
            placeBlock(state, level, plantPos);
        }
    }

    static boolean canBelowBlockSurvive(ServerLevel level, BlockPos newPos) {
        BlockState blockAboveState = level.getBlockState(newPos.above());
        return blockAboveState.is(ModBlocks.RICH_SOIL.get())
                || blockAboveState.is(MNDBlocks.RESURGENT_SOIL.get())
                || blockAboveState.is(ModBlocks.RICH_SOIL_FARMLAND.get())
                || blockAboveState.is(MNDBlocks.RESURGENT_SOIL_FARMLAND.get());
    }

    private static void placeBlock(BlockState state, ServerLevel level, BlockPos pos) {
        Block block = state.getBlock();
        if (block instanceof SimpleWaterloggedBlock) {
            FluidState fluidState = level.getFluidState(pos);
            state = state.setValue(BlockStateProperties.WATERLOGGED, fluidState.getType() == Fluids.WATER);
        }

        if (block instanceof PinkPetalsBlock) {
            Direction[] directions = {Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};
            level.setBlockAndUpdate(pos, state.setValue(FACING,
                    directions[level.random.nextInt(directions.length)]));
        } else if (block instanceof DoublePlantBlock doublePlant) {
            doublePlant.placeAt(level, state, pos, 3);
        } else {
            level.setBlockAndUpdate(pos, state);
        }
        playGrowthEffects(level, pos);
    }

    private static void playGrowthEffects(ServerLevel level, BlockPos pos) {
        for (int i = 0; i < 3; i++) {
            level.sendParticles(ParticleTypes.SOUL,
                    pos.getX() + level.random.nextDouble(),
                    pos.getY() + level.random.nextDouble(),
                    pos.getZ() + level.random.nextDouble(),
                    1, 0.0, 0.0, 0.0, 0.0);
        }
        level.playSound(null, pos, SoundEvents.SOUL_ESCAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    @Nullable
    public BlockState getToolModifiedState(BlockState state, UseOnContext context,
                                           ToolAction toolAction, boolean simulate) {
        if (toolAction.equals(ToolActions.HOE_TILL)
                && context.getLevel().getBlockState(context.getClickedPos().above()).isAir()) {
            return MNDBlocks.RESURGENT_SOIL_FARMLAND.get().defaultBlockState();
        }
        return null;
    }

    @Override
    public boolean canSustainPlant(BlockState state, BlockGetter level, BlockPos pos,
                                   Direction facing, IPlantable plantable) {
        BlockState plantState = level.getBlockState(pos.relative(facing));
        if (plantState.getBlock() instanceof NetherWartBlock) {
            return true;
        }
        if (plantState.is(MNDTags.RESURGENT_SOIL_PLANT)
                && !(plantState.getBlock() instanceof CropBlock)
                && !plantState.is(BlockTags.CROPS)) {
            return true;
        }
        return super.canSustainPlant(state, level, pos, facing, plantable);
    }
}
