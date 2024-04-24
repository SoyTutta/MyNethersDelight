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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.StemGrownBlock;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.MathUtils;

public class ResurgentSoilFarmlandBlock extends FarmBlock {

    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0);
    public ResurgentSoilFarmlandBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    private static boolean hasFireOrLava(LevelReader level, BlockPos pos) {
        Iterator<BlockPos> var2 = BlockPos.betweenClosed(pos.offset(-8, -4, -8), pos.offset(8, 4, 8)).iterator();

        BlockPos nearbyPos;
        do {
            if (!var2.hasNext()) {
                return false;
            }
            nearbyPos = var2.next();
            BlockState state = level.getBlockState(nearbyPos);
            if (state.getFluidState().is(FluidTags.LAVA)) {
                return true;
            }
            if (state.is(MNDTags.LETIOS_FLAMES)) {
                if (!state.hasProperty(BlockStateProperties.LIT) || (state.hasProperty(BlockStateProperties.LIT) && state.getValue(BlockStateProperties.LIT))) {
                    return true;
                }
            }
        } while(true);
    }

    public static void turnToRichSoil(BlockState state, Level level, BlockPos pos) {
        level.setBlockAndUpdate(pos, pushEntitiesUp(state, ((Block)MNDBlocks.RESURGENT_SOIL.get()).defaultBlockState(), level, pos));
    }

    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState aboveState = level.getBlockState(pos.above());
        return super.canSurvive(state, level, pos) || aboveState.getBlock() instanceof StemGrownBlock;
    }

    public boolean isFertile(BlockState state, BlockGetter world, BlockPos pos) {
        if (state.is(MNDBlocks.RESURGENT_SOIL_FARMLAND.get())) {
            return state.getValue(MOISTURE) > 0;
        } else {
            return false;
        }
    }

    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
        if (!state.canSurvive(level, pos)) {
            turnToRichSoil(state, level, pos);
        }
    }


    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int moisture = state.getValue(MOISTURE);
        if (!hasFireOrLava(level, pos) && !level.isRainingAt(pos.above())) {
            if (moisture > 0) {
                level.setBlock(pos, state.setValue(MOISTURE, moisture - 1), 2);
            }
        } else if (moisture < 7) {
                level.setBlock(pos, state.setValue(MOISTURE, 7), 2);
        } else if (moisture == 7) {
                if (Configuration.RICH_SOIL_BOOST_CHANCE.get() == 0.0) {
                    return;
                }

                BlockState aboveState = level.getBlockState(pos.above());
                Block aboveBlock = aboveState.getBlock();
                if (aboveState.is(ModTags.UNAFFECTED_BY_RICH_SOIL) || aboveBlock instanceof TallFlowerBlock) {
                    return;
                }

                if (aboveBlock instanceof BonemealableBlock) {
                    BonemealableBlock growable = (BonemealableBlock)aboveBlock;
                    if ((double)MathUtils.RAND.nextFloat() <= Configuration.RICH_SOIL_BOOST_CHANCE.get() && growable.isValidBonemealTarget(level, pos.above(), aboveState, false) && ForgeHooks.onCropsGrowPre(level, pos.above(), aboveState, true)) {
                        growable.performBonemeal(level, level.random, pos.above(), aboveState);
                        if (!level.isClientSide) {
                            level.levelEvent(2005, pos.above(), 0);
                        }
                        ForgeHooks.onCropsGrowPost(level, pos.above(), aboveState);
                    }

                }
            }
        }
        public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
        PlantType plantType = plantable.getPlantType(world, pos.relative(facing));
        return plantType == PlantType.CROP || plantType == PlantType.PLAINS;
        }
        public BlockState getStateForPlacement(BlockPlaceContext context) {
        return !this.defaultBlockState().canSurvive(context.getLevel(), context.getClickedPos()) ? MNDBlocks.RESURGENT_SOIL.get().defaultBlockState() : super.getStateForPlacement(context);
        }
        public void fallOn(Level level, BlockState state, BlockPos pos, Entity entityIn, float fallDistance) {
    }
}