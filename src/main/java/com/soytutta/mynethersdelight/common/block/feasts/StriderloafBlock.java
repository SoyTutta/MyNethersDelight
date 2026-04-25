package com.soytutta.mynethersdelight.common.block.feasts;

import com.soytutta.mynethersdelight.common.registry.MNDBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.block.FeastBlock;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.function.Supplier;

public class StriderloafBlock extends FeastBlock {

    protected static final VoxelShape SHAPE_NORTH_SOUTH = Shapes.or(Block.box(2.0D, 1.0D, 1.0D, 14.0D, 3.0D, 15.0D), Block.box(2.0D, 0.0D, 3.0D, 14.0D, 2.0D, 5.0D), Block.box(2.0D, 0.0D, 11.0D, 14.0D, 2.0D, 13.0D));
    protected static final VoxelShape SHAPE_EAST_WEST = Shapes.or(Block.box(1.0D, 1.0D, 2.0D, 15.0D, 3.0D, 14.0D), Block.box(3.0D, 0.0D, 2.0D, 5.0D, 2.0D, 14.0D), Block.box(11.0D, 0.0D, 2.0D, 13.0D, 2.0D, 14.0D));
    protected static final VoxelShape ROAST_SHAPE_EAST_WEST = Shapes.joinUnoptimized(SHAPE_EAST_WEST, Block.box(5, 2, 5, 11, 8, 11), BooleanOp.OR);
    protected static final VoxelShape ROAST_SHAPE_NORTH_SOUTH = Shapes.joinUnoptimized(SHAPE_NORTH_SOUTH, Block.box(5, 2, 5, 11, 8, 11), BooleanOp.OR);

    public StriderloafBlock(Properties properties, Supplier<Item> servingItem, boolean hasLeftovers) {
        super(properties, servingItem, hasLeftovers);
    }

    private static boolean hasLava(LevelReader level, BlockPos pos) {
        for (BlockPos nearbyPos : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
            if (level.getBlockState(nearbyPos).getFluidState().is(FluidTags.LAVA)) {
                return true;
            }
            if (nearbyPos.equals(pos.below()) && level.getBlockState(nearbyPos).is(ModTags.Blocks.HEAT_SOURCES)) {
                return true;
            }
        }
        return false;
    }

    private void updateBlockState(ServerLevel worldIn, BlockPos pos, BlockState newState, SoundEvent sound) {
        worldIn.setBlockAndUpdate(pos, newState);
        worldIn.playSound(null, pos, sound, SoundSource.BLOCKS, 0.25F, 0.25F);
    }

    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        if (!worldIn.isClientSide) {
            Block targetBlock = (state.getBlock() == MNDBlocks.COLD_STRIDERLOAF_BLOCK.get() && hasLava(worldIn, pos)) ?
                    MNDBlocks.STRIDERLOAF_BLOCK.get() : (state.getBlock() == MNDBlocks.STRIDERLOAF_BLOCK.get() && !hasLava(worldIn, pos)) ?
                    MNDBlocks.COLD_STRIDERLOAF_BLOCK.get() : null;
            if (targetBlock != null) {
                BlockState newState = targetBlock.defaultBlockState()
                        .setValue(FACING, state.getValue(FACING))
                        .setValue(SERVINGS, state.getValue(SERVINGS));
                SoundEvent sound = (targetBlock == MNDBlocks.STRIDERLOAF_BLOCK.get()) ? SoundEvents.STRIDER_HAPPY : SoundEvents.STRIDER_HURT;
                updateBlockState(worldIn, pos, newState, sound);
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (worldIn.getBlockState(fromPos).getFluidState().is(FluidTags.LAVA)) {
            randomTick(state, (ServerLevel) worldIn, pos, null);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(FeastBlock.FACING).getAxis().equals(Direction.Axis.X)) {
            return state.getValue(SERVINGS) == 0 ? SHAPE_NORTH_SOUTH : ROAST_SHAPE_NORTH_SOUTH;
        }
        return state.getValue(SERVINGS) == 0 ? SHAPE_EAST_WEST : ROAST_SHAPE_EAST_WEST;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        int servings = blockState.getValue(this.getServingsProperty());
        if (servings > 0) {
            return (blockState.getBlock() == MNDBlocks.COLD_STRIDERLOAF_BLOCK.get()) ? servings : servings + 1;
        } else {
            return (blockState.getBlock() == MNDBlocks.STRIDERLOAF_BLOCK.get()) ? 1 : 0;
        }
    }

    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }
}