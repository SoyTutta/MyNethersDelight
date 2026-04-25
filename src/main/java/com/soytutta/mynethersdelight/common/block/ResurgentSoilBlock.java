//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package com.soytutta.mynethersdelight.common.block;

import com.soytutta.mynethersdelight.common.block.crops.PowderyCaneBlock;
import com.soytutta.mynethersdelight.common.block.crops.PowderyFlowerBlock;
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
import vectorwing.farmersdelight.common.utility.MathUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static net.minecraft.world.level.block.PinkPetalsBlock.FACING;

public class ResurgentSoilBlock extends Block {
    public ResurgentSoilBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
        if (level.isClientSide) return;

        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        Block aboveBlock = aboveState.getBlock();

        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);
        Block belowBlock = belowState.getBlock();

        if (aboveBlock == Blocks.CRIMSON_FUNGUS) {
            level.setBlockAndUpdate(abovePos, MNDBlocks.CRIMSON_FUNGUS_COLONY.get().defaultBlockState());
        }
        if (aboveBlock == Blocks.WARPED_FUNGUS) {
            level.setBlockAndUpdate(abovePos, MNDBlocks.WARPED_FUNGUS_COLONY.get().defaultBlockState());
        }
        if (aboveBlock == Blocks.BROWN_MUSHROOM) {
            level.setBlockAndUpdate(abovePos, ModBlocks.BROWN_MUSHROOM_COLONY.get().defaultBlockState());
        }
        if (aboveBlock == Blocks.RED_MUSHROOM) {
            level.setBlockAndUpdate(abovePos, ModBlocks.RED_MUSHROOM_COLONY.get().defaultBlockState());
        }

        if (Configuration.RICH_SOIL_BOOST_CHANCE.get() == 0.0) return;
        double ResurgentSoilBoostChance = Configuration.RICH_SOIL_BOOST_CHANCE.get() * 1.05F;

        if (aboveBlock == MNDBlocks.POWDERY_TORCH.get()
                && MathUtils.RAND.nextFloat() <= (ResurgentSoilBoostChance) / 15) {
            if (level.isEmptyBlock(pos.above(2))) {
                level.setBlockAndUpdate(pos.above(), MNDBlocks.POWDERY_CANE.get().defaultBlockState().setValue(PowderyCaneBlock.AGE, 1));
                level.setBlockAndUpdate(pos.above(2), MNDBlocks.BULLET_PEPPER.get().defaultBlockState().setValue(PowderyFlowerBlock.LIT, true).setValue(PowderyFlowerBlock.AGE, 2));
            }
        }

        if (!aboveState.is(MNDTags.NOT_PROPAGATE_PLANT)) {
            if (aboveState.is(MNDTags.ABOVE_PROPAGATE_PLANT) && MathUtils.RAND.nextFloat() <= (ResurgentSoilBoostChance * 0.6F)) {
                propagateAboveIfPossible(aboveBlock, abovePos, level);
            } else if (aboveBlock instanceof FlowerBlock && MathUtils.RAND.nextFloat() <= (ResurgentSoilBoostChance * 0.8F)) {
                propagateAboveIfPossible(aboveBlock, abovePos, level);
            } else if ((aboveBlock instanceof FungusBlock || aboveBlock instanceof MushroomBlock) && MathUtils.RAND.nextFloat() <= (ResurgentSoilBoostChance * 0.4F)) {
                propagateAboveIfPossible(aboveBlock, abovePos, level);
            } else if (aboveBlock instanceof MushroomColonyBlock colony) {
                int age = aboveState.getValue(MushroomColonyBlock.COLONY_AGE);
                if (age == 3 && MathUtils.RAND.nextFloat() <= (ResurgentSoilBoostChance * 0.4F)) {
                    propagateAboveIfPossible(aboveBlock, abovePos, level);
                }
                if (MathUtils.RAND.nextFloat() <= 0.4F) {
                    Block baseMushroom = Block.byItem(colony.mushroomType.value());
                    propagateAboveIfPossible(baseMushroom != Blocks.AIR ? baseMushroom : aboveBlock, abovePos, level);
                }
            } else if ((aboveBlock == MNDBlocks.POWDERY_CANE.get()) && (MathUtils.RAND.nextFloat() <= (ResurgentSoilBoostChance * 0.4F))) {
                propagateAboveIfPossible(MNDBlocks.BULLET_PEPPER.get(), abovePos, level);
            } else if (aboveBlock == MNDBlocks.POWDERY_CANNON.get() && MathUtils.RAND.nextFloat() <= (ResurgentSoilBoostChance * 0.2F)) {
                propagateAboveIfPossible(MNDBlocks.POWDERY_CHUBBY_SAPLING.get(), abovePos, level);
            } else if (aboveBlock == Blocks.BAMBOO && MathUtils.RAND.nextFloat() <= (ResurgentSoilBoostChance * 0.2F)) {
                propagateAboveIfPossible(Blocks.BAMBOO_SAPLING, abovePos, level);
            } else if (aboveBlock instanceof NetherWartBlock) {
                if (aboveState.getValue(NetherWartBlock.AGE) == 3 && level.random.nextInt(8) == 0) {
                    propagateAboveIfPossible(aboveBlock, abovePos, level);
                }
            } else if (aboveBlock instanceof PinkPetalsBlock) {
                int amount = aboveState.getValue(PinkPetalsBlock.AMOUNT);
                if (amount != PinkPetalsBlock.MAX_FLOWERS) {
                    performBonemealIfPossible(abovePos, level, 1, Direction.UP);
                } else if (level.random.nextInt(6) == 0) {
                    propagateAboveIfPossible(aboveBlock, abovePos, level);
                }
            } else if (aboveBlock instanceof DoublePlantBlock && MathUtils.RAND.nextFloat() <= (ResurgentSoilBoostChance / 2)) {
                propagateAboveIfPossible(aboveBlock, abovePos, level);
            } else if ((aboveBlock instanceof BushBlock && !(aboveBlock instanceof DoublePlantBlock || aboveBlock instanceof PowderyCaneBlock || aboveBlock instanceof PowderyFlowerBlock))
                    && MathUtils.RAND.nextFloat() <= (ResurgentSoilBoostChance / 3)) {
                propagateAboveIfPossible(aboveBlock, abovePos, level);
            }
        }

        if (!belowState.is(MNDTags.NOT_PROPAGATE_PLANT)) {
            if (belowState.is(MNDTags.BELOW_PROPAGATE_PLANT) && MathUtils.RAND.nextFloat() <= (ResurgentSoilBoostChance * 0.8F)) {
                propagateBelowIfPossible(belowBlock, belowPos, level);
            }
        }

        int BonemealChance = 1;
        if (MathUtils.RAND.nextFloat() <= 0.2f) {
            BonemealChance = 2;
            if (MathUtils.RAND.nextFloat() <= 0.01f) BonemealChance = 3;
        }

        for (int i = 0; i < MathUtils.RAND.nextInt(BonemealChance) + 1; i++) {
            if (!aboveState.isAir()) {
                performBonemealIfPossible(abovePos, level, 1, Direction.UP);
            }
            if (!belowState.isAir()) {
                performBonemealIfPossible(belowPos, level, 1, Direction.DOWN);
            }
        }

        growIfPossible(aboveState, abovePos, level, Blocks.SUGAR_CANE, 7);
        growIfPossible(aboveState, abovePos, level, Blocks.CACTUS, 7);
    }

    private void performBonemealIfPossible(BlockPos position, ServerLevel level, int distance, Direction direction) {
        if (distance > 10) return;

        BlockState state = level.getBlockState(position);
        Block block = state.getBlock();

        if (state.isAir() || state.is(ModTags.Blocks.UNAFFECTED_BY_RICH_SOIL) || block instanceof TallFlowerBlock) {
            return;
        }

        if (block instanceof BonemealableBlock growable && MathUtils.RAND.nextFloat() <= Configuration.RICH_SOIL_BOOST_CHANCE.get() / distance) {
            if (growable.isValidBonemealTarget(level, position.above(),state) && CommonHooks.canCropGrow(level, position.above(), state, true)) {
                growable.performBonemeal(level, level.random, position, state);
                CommonHooks.fireCropGrowPost(level, position, state);
                for (int i = 0; i < 3; i++) {
                    double d0 = (double) position.getX() + level.getRandom().nextDouble();
                    double d1 = (double) position.getY() + level.getRandom().nextDouble();
                    double d2 = (double) position.getZ() + level.getRandom().nextDouble();
                    level.sendParticles(ParticleTypes.SOUL, d0, d1, d2, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                    level.playSound(null, position, SoundEvents.SOUL_ESCAPE.value(), SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            }
        }

        BlockPos nextPos = position.relative(direction);
        BlockState nextState = level.getBlockState(nextPos);

        boolean isSameBlock = nextState.is(block);
        boolean isGrowingPlant = nextState.getBlock() instanceof GrowingPlantBlock || nextState.getBlock() instanceof GrowingPlantHeadBlock;

        if (isSameBlock || isGrowingPlant) {
            performBonemealIfPossible(nextPos, level, distance + 1, direction);
        }
    }

    public static void growIfPossible(BlockState aboveBlock, BlockPos abovePos, ServerLevel level, Block targetBlock, int maxHeight) {
        if (aboveBlock.getBlock() == targetBlock && MathUtils.RAND.nextFloat() <= (Configuration.RICH_SOIL_BOOST_CHANCE.get() * 0.6F)) {
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
                    level.playSound(null, topPos.above(), SoundEvents.SOUL_ESCAPE.value(), SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            }
        }
    }

    private void propagateAboveIfPossible(Block block, BlockPos position, ServerLevel level) {
        List<BlockPos> validPositions = new ArrayList<>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos newPos = position.offset(x, y, z);
                    BlockState newState = level.getBlockState(newPos);

                    if (canAboveBlockSurvive(block, newState, level, newPos)) {
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
                    || (block instanceof WitherRoseBlock && (targetState.is(BlockTags.SMALL_FLOWERS)
                    || targetState.getBlock() instanceof FungusBlock || targetState.getBlock() instanceof MushroomBlock))
                    || ((block instanceof SimpleWaterloggedBlock) && (targetState.getBlock() == Blocks.AIR || targetState.getBlock() == Blocks.WATER));

            if (canPropagate) {
                placeBlock(block, level, plantPos);
            }
        }
    }

    private boolean canAboveBlockSurvive(Block block, BlockState newState, ServerLevel level, BlockPos newPos) {
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
        List<BlockPos> validPositions = new ArrayList<>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos newPos = position.offset(x, y, z);

                    if (canBelowBlockSurvive(level, newPos)) {
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
                    || ((block instanceof SimpleWaterloggedBlock) && (targetState.getBlock() == Blocks.AIR || targetState.getBlock() == Blocks.WATER));

            if (canPropagate) {
                placeBlock(block, level, plantPos);
            }
        }
    }

    private boolean canBelowBlockSurvive(ServerLevel level, BlockPos newPos) {
        BlockState blockAboveState = level.getBlockState(newPos.above());
        return blockAboveState.getBlock() == ModBlocks.RICH_SOIL.get()
                || blockAboveState.getBlock() == MNDBlocks.RESURGENT_SOIL.get()
                || blockAboveState.getBlock() ==ModBlocks.RICH_SOIL_FARMLAND.get()
                || blockAboveState.getBlock() == MNDBlocks.RESURGENT_SOIL_FARMLAND.get();
    }

    private void placeBlock(Block block, ServerLevel level, BlockPos pos) {
        BlockState state = block.defaultBlockState();
        if (block instanceof SimpleWaterloggedBlock) {
            FluidState fluidState = level.getFluidState(pos);
            if (fluidState.getType() == Fluids.WATER) {
                state = state.setValue(BlockStateProperties.WATERLOGGED, true);
            } else {
                state = state.setValue(BlockStateProperties.WATERLOGGED, false);
            }
        }
        if (block instanceof PinkPetalsBlock) {
            Random random = new Random();
            Direction[] allowedDirections = {Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};
            Direction randomDirection = allowedDirections[random.nextInt(allowedDirections.length)];
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
            level.playSound(null, pos, SoundEvents.SOUL_ESCAPE.value(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }
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