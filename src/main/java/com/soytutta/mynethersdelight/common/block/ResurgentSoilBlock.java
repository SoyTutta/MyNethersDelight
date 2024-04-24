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
import vectorwing.farmersdelight.common.registry.ModBlocks;
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

            if (aboveBlock instanceof NetherWartBlock) {
                int age = aboveState.getValue(NetherWartBlock.AGE);
                if (age < NetherWartBlock.MAX_AGE) {
                    aboveState = aboveState.setValue(NetherWartBlock.AGE, age + 1);
                    level.setBlockAndUpdate(abovePos, aboveState);
                }
                return;
            }

            if (aboveBlock instanceof TallFlowerBlock) {
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
                level.setBlockAndUpdate(pos.above(), ((Block)ModBlocks.RED_MUSHROOM_COLONY.get()).defaultBlockState());
                return;
            }

            if (Configuration.RICH_SOIL_BOOST_CHANCE.get() == 0.0) {
                return;
            }

            if (aboveBlock instanceof BonemealableBlock) {
                BonemealableBlock growable = (BonemealableBlock)aboveBlock;
                if ((double)MathUtils.RAND.nextFloat() <= Configuration.RICH_SOIL_BOOST_CHANCE.get() && growable.isValidBonemealTarget(level, pos.above(), aboveState, false) && ForgeHooks.onCropsGrowPre(level, pos.above(), aboveState, true)) {
                    growable.performBonemeal(level, level.random, pos.above(), aboveState);
                    level.levelEvent(2005, pos.above(), 0);
                    ForgeHooks.onCropsGrowPost(level, pos.above(), aboveState);
                }
            }
        }
    }

    @Nullable
    public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
        return toolAction.equals(ToolActions.HOE_TILL) && context.getLevel().getBlockState(context.getClickedPos().above()).isAir() ? MNDBlocks.RESURGENT_SOIL_FARMLAND.get().defaultBlockState() : null;
    }

    public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
        if (plantable instanceof NetherWartBlock && facing == Direction.UP) {
            return true;
        }
        PlantType plantType = plantable.getPlantType(world, pos.relative(facing));
        return plantType != PlantType.CROP && plantType != PlantType.NETHER && plantType != PlantType.WATER;
    }
}