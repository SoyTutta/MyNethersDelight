package com.soytutta.mynethersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class AbstractTrophyBlock extends Block implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty HIT = BooleanProperty.create("hit");

    private final double pushStrength;
    private final double pushVerticalStrength;

    public AbstractTrophyBlock(Properties properties, double pushStrength, double pushVerticalStrength) {
        super(properties);
        this.pushStrength = pushStrength;
        this.pushVerticalStrength = pushVerticalStrength;
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false)
                .setValue(HIT, false));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = defaultBlockState();
        LevelReader level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        FluidState fluid = level.getFluidState(pos);

        for (Direction direction : context.getNearestLookingDirections()) {
            if (direction.getAxis().isHorizontal()) {
                state = state.setValue(FACING, direction.getOpposite());
                if (state.canSurvive(level, pos)) {
                    return state.setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
                }
            }
        }
        return null;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockPos supportPos = pos.relative(direction.getOpposite());
        return level.getBlockState(supportPos).isFaceSturdy(level, supportPos, direction);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (movedByPiston) {
            if (state.getValue(HIT)) {
                level.setBlock(pos, state.setValue(HIT, false), 3);
            }
            pushEntities(level, pos, state);
        }
        super.onPlace(state, level, pos, oldState, movedByPiston);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block,
                                BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide && level.hasNeighborSignal(pos)) {
            pushEntities(level, pos, state);
        }
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && level.hasNeighborSignal(pos) && !entity.isSteppingCarefully()) {
            pushEntity(entity, state, pos, level);
        }
    }

    private void pushEntities(Level level, BlockPos pos, BlockState state) {
        Direction facing = state.getValue(FACING);
        AABB searchBox = new AABB(pos).expandTowards(facing.getStepX(), facing.getStepY(), facing.getStepZ());
        for (Entity entity : level.getEntities(null, searchBox)) {
            pushEntity(entity, state, pos, level);
        }
    }

    private void pushEntity(Entity entity, BlockState state, BlockPos pos, Level level) {
        if (state.getValue(HIT)) {
            return;
        }

        Direction direction = state.getValue(FACING);
        Vec3 movement = new Vec3(direction.getStepX() * pushStrength,
                pushVerticalStrength,
                direction.getStepZ() * pushStrength);
        entity.setDeltaMovement(movement);
        entity.hurtMarked = true;
        level.playSound(null, pos, SoundEvents.HOGLIN_ATTACK, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.setBlock(pos, state.setValue(HIT, true), 3);
        level.scheduleTick(pos, this, 10);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(HIT)) {
            level.setBlock(pos, state.setValue(HIT, false), 3);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, HIT);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return false;
    }

    @Nullable
    @Override
    public BlockPathTypes getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob) {
        return BlockPathTypes.DAMAGE_OTHER;
    }
}
