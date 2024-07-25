package com.soytutta.mynethersdelight.common.block;

import com.soytutta.mynethersdelight.common.registry.MNDBlocks;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.block.FeastBlock;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.function.Supplier;

public class StriderloafBlock extends FeastBlock {
    protected static final VoxelShape PLATE_SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 2.0, 15.0);
    protected static final VoxelShape ROAST_SHAPE;

    public StriderloafBlock(Properties properties, Supplier<Item> servingItem, boolean hasLeftovers) {
        super(properties, servingItem, hasLeftovers);
    }

    private static boolean hasLava(LevelReader level, BlockPos pos) {
        for (BlockPos nearbyPos : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
            if (level.getBlockState(nearbyPos).getFluidState().is(FluidTags.LAVA)) {
                return true;
            }
            if (nearbyPos.equals(pos.below()) && level.getBlockState(nearbyPos).is(ModTags.HEAT_SOURCES)) {
                return true;
            }
        }
        return false;
    }

    private void updateBlockState(ServerLevel worldIn, BlockPos pos, BlockState newState, SoundEvent sound) {
        worldIn.setBlockAndUpdate(pos, newState);
        worldIn.playSound(null, pos, sound, SoundSource.BLOCKS, 0.25F, 0.25F);
    }

    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isClientSide) {
            Block targetBlock = (state.getBlock() == MNDBlocks.COLD_STRIDERLOAF_BLOCK.get() && hasLava(level, pos)) ?
                    MNDBlocks.STRIDERLOAF_BLOCK.get() : (state.getBlock() == MNDBlocks.STRIDERLOAF_BLOCK.get() && !hasLava(level, pos)) ?
                    MNDBlocks.COLD_STRIDERLOAF_BLOCK.get() : null;
            if (targetBlock != null) {
                BlockState newState = targetBlock.defaultBlockState()
                        .setValue(FACING, state.getValue(FACING))
                        .setValue(SERVINGS, state.getValue(SERVINGS));
                SoundEvent sound = (targetBlock == MNDBlocks.STRIDERLOAF_BLOCK.get()) ? SoundEvents.STRIDER_HAPPY : SoundEvents.STRIDER_HURT;
                updateBlockState(level, pos, newState, sound);
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (worldIn.getBlockState(fromPos).getFluidState().is(FluidTags.LAVA)) {
            tick(state, (ServerLevel) worldIn, pos, null);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return (state.getValue(SERVINGS) == 0) ? PLATE_SHAPE : ROAST_SHAPE;
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

    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return new ItemStack(MNDItems.STRIDERLOAF_BLOCK.get());
    }

    static {
        ROAST_SHAPE = Shapes.joinUnoptimized(PLATE_SHAPE, Block.box(5, 2, 5, 11, 6, 11), BooleanOp.OR);
    }
}