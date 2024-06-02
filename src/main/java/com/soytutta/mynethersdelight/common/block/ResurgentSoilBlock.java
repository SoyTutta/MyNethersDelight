//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package com.soytutta.mynethersdelight.common.block;

import com.soytutta.mynethersdelight.common.registry.MNDBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.*;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.block.MushroomColonyBlock;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.MathUtils;

import javax.annotation.Nullable;

public class ResurgentSoilBlock extends Block {
    public ResurgentSoilBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
        if (!level.isClientSide) {
            BlockPos abovePos = pos.above();
            BlockState aboveState = level.getBlockState(abovePos);
            Block aboveBlock = aboveState.getBlock();

            BlockPos belowPos = pos.below();
            BlockState belowState = level.getBlockState(belowPos);
            Block belowBlock = belowState.getBlock();

            if (Configuration.RICH_SOIL_BOOST_CHANCE.get() == 0.0) {
                return;
            }

            if (aboveBlock instanceof NetherWartBlock) {
                int age = aboveState.getValue(NetherWartBlock.AGE);
                if (age < NetherWartBlock.MAX_AGE) {
                    aboveState = aboveState.setValue(NetherWartBlock.AGE, age + 1);
                    level.setBlockAndUpdate(abovePos, aboveState);
                }
                return;
            }

            if ((aboveBlock instanceof TwistingVinesBlock || aboveBlock instanceof TwistingVinesPlantBlock)
                    && MathUtils.RAND.nextFloat() <= (Configuration.RICH_SOIL_BOOST_CHANCE.get() * 0.3F)) {
                BonemealableBlock growable = (BonemealableBlock) aboveBlock;
                if (growable.isValidBonemealTarget(level, pos.above(), aboveState, false) && ForgeHooks.onCropsGrowPre(level, pos.above(), aboveState, true)) {
                    growable.performBonemeal(level, level.random, pos.above(), aboveState);
                    level.levelEvent(2005, pos.above(), 0);
                    ForgeHooks.onCropsGrowPost(level, pos.above(), aboveState);
                }
                return;
            }
            if ((belowBlock instanceof WeepingVinesBlock || belowBlock instanceof WeepingVinesPlantBlock)
                    && MathUtils.RAND.nextFloat() <= (Configuration.RICH_SOIL_BOOST_CHANCE.get() * 0.3F)) {
                BonemealableBlock growable = (BonemealableBlock) belowBlock;
                if (growable.isValidBonemealTarget(level, pos.below(), belowState, false) && ForgeHooks.onCropsGrowPre(level, pos.below(), belowState, true)) {
                    growable.performBonemeal(level, level.random, pos.below(), belowState);
                    level.levelEvent(2005, pos.below(), 0);
                    ForgeHooks.onCropsGrowPost(level, pos.below(), belowState);
                }
                return;
            }

            if (aboveBlock == MNDBlocks.POWDERY_TORCH.get()) {
                level.setBlockAndUpdate(pos.above(), MNDBlocks.POWDERY_CANE.get().defaultBlockState().setValue(PowderyCaneBlock.BASE, true));
                level.setBlockAndUpdate(pos.above(2), MNDBlocks.BULLET_PEPPER.get().defaultBlockState().setValue(PowderyCaneBlock.LIT, true).setValue(PowderyCaneBlock.AGE, 2));
                return;
            }

            if (aboveBlock == Blocks.CRIMSON_FUNGUS) {
                level.setBlockAndUpdate(pos.above(), MNDBlocks.CRIMSON_FUNGUS_COLONY.get().defaultBlockState());
                return;
            }
            if (aboveBlock == Blocks.WARPED_FUNGUS) {
                level.setBlockAndUpdate(pos.above(), MNDBlocks.WARPED_FUNGUS_COLONY.get().defaultBlockState());
                return;
            }

            if (aboveBlock == Blocks.BROWN_MUSHROOM) {
                level.setBlockAndUpdate(pos.above(), ModBlocks.BROWN_MUSHROOM_COLONY.get().defaultBlockState());
                return;
            }
            if (aboveBlock == Blocks.RED_MUSHROOM) {
                level.setBlockAndUpdate(pos.above(), ModBlocks.RED_MUSHROOM_COLONY.get().defaultBlockState());
                return;
            }

            if (aboveBlock instanceof MushroomColonyBlock && rand.nextFloat() < 0.75) {
                int age = aboveState.getValue(MushroomColonyBlock.COLONY_AGE);
                int maxAge = ((MushroomColonyBlock) aboveBlock).getMaxAge();
                if (age < maxAge) {
                    aboveState = aboveState.setValue(MushroomColonyBlock.COLONY_AGE, age + 1);
                    level.setBlockAndUpdate(abovePos, aboveState);
                }
                return;
            }

            if ((aboveBlock == Blocks.CACTUS) && MathUtils.RAND.nextFloat() <= (Configuration.RICH_SOIL_BOOST_CHANCE.get() * 0.5F)) {
                BlockPos topCactusPos = abovePos;
                int cactusHeight = 1;
                while (level.getBlockState(topCactusPos.above()).getBlock() == Blocks.CACTUS) {
                    topCactusPos = topCactusPos.above();
                    cactusHeight++;
                }
                if (level.getBlockState(topCactusPos.above()).isAir() && cactusHeight < 7) {
                    level.setBlockAndUpdate(topCactusPos.above(), Blocks.CACTUS.defaultBlockState());
                }
            }

            if ((aboveBlock == Blocks.SUGAR_CANE) && MathUtils.RAND.nextFloat() <= (Configuration.RICH_SOIL_BOOST_CHANCE.get() * 0.5F)) {
                BlockPos topCanePos = abovePos;
                int caneHeight = 1;
                while (level.getBlockState(topCanePos.above()).getBlock() == Blocks.SUGAR_CANE) {
                    topCanePos = topCanePos.above();
                    caneHeight++;
                }
                if (level.getBlockState(topCanePos.above()).isAir() && caneHeight < 7) {
                    level.setBlockAndUpdate(topCanePos.above(), Blocks.SUGAR_CANE.defaultBlockState());
                }
            }
            if (aboveState.is(ModTags.UNAFFECTED_BY_RICH_SOIL) || aboveBlock instanceof TallFlowerBlock) {
                return;
            }

            performBonemealIfPossible(aboveBlock, pos.above(), aboveState, level, 1);
            performBonemealIfPossible(belowBlock, pos.below(), belowState, level, 1);
        }
    }

    private void performBonemealIfPossible(Block block, BlockPos position, BlockState state, ServerLevel level, int distance) {
        if (block instanceof BonemealableBlock growable && MathUtils.RAND.nextFloat() <= Configuration.RICH_SOIL_BOOST_CHANCE.get() / distance) {
            if (growable.isValidBonemealTarget(level, position, state, false) && ForgeHooks.onCropsGrowPre(level, position, state, true)) {
                growable.performBonemeal(level, level.random, position, state);
                level.levelEvent(2005, position, 0);
                ForgeHooks.onCropsGrowPost(level, position, state);
            } else {
                BlockPos checkPos = position.above();
                BlockState checkState = level.getBlockState(checkPos);
                Block checkBlock = checkState.getBlock();
                while (checkBlock == block && distance <= 10) {
                    performBonemealIfPossible(checkBlock, checkPos, checkState, level, distance + 1);
                    distance++;
                    checkPos = checkPos.above();
                    checkState = level.getBlockState(checkPos);
                    checkBlock = checkState.getBlock();
                }

                checkPos = position.below();
                checkState = level.getBlockState(checkPos);
                checkBlock = checkState.getBlock();
                while (checkBlock == block && distance <= 10) {
                    performBonemealIfPossible(checkBlock, checkPos, checkState, level, distance + 1);
                    distance++;
                    checkPos = checkPos.below();
                    checkState = level.getBlockState(checkPos);
                    checkBlock = checkState.getBlock();
                }
            }
        }
    }

    @Nullable
    public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
        return toolAction.equals(ToolActions.HOE_TILL) && context.getLevel().getBlockState(context.getClickedPos().above()).isAir() ? MNDBlocks.RESURGENT_SOIL_FARMLAND.get().defaultBlockState() : null;
    }

    @Override
    public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
        if (plantable instanceof NetherWartBlock && facing == Direction.UP) {
            return true;
        }
        PlantType plantType = plantable.getPlantType(world, pos.relative(facing));
        return plantType != PlantType.CROP && plantType != PlantType.NETHER && plantType != PlantType.WATER;
    }
}