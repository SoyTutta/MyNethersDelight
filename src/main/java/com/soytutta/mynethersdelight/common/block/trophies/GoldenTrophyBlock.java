package com.soytutta.mynethersdelight.common.block.trophies;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.stream.Stream;

public class GoldenTrophyBlock extends AbstractTrophyBlock {

    protected static final VoxelShape NORTH_AABB = Stream.of( Block.box(1, 0, 15, 15, 3, 16), Block.box(0, 3, 15, 16, 15, 16), Block.box(1, 4, 13, 15, 13, 15), Block.box(2, 9, 9, 14, 13, 13), Block.box(2, 7, 7, 14, 11, 11), Block.box(2, 5, 5, 14, 9, 9), Block.box(2, 3, 3, 14, 7, 7)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    protected static final VoxelShape SOUTH_AABB = Stream.of( Block.box(1, 0, 0, 15, 3, 1), Block.box(0, 3, 0, 16, 15, 1), Block.box(1, 4, 1, 15, 13, 3), Block.box(2, 9, 3, 14, 13, 7), Block.box(2, 7, 5, 14, 11, 9), Block.box(2, 5, 7, 14, 9, 11), Block.box(2, 3, 9, 14, 7, 13)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    protected static final VoxelShape EAST_AABB = Stream.of( Block.box(0, 0, 1, 1, 3, 15), Block.box(0, 3, 0, 1, 15, 16),  Block.box(1, 4, 1, 3, 13, 15), Block.box(3, 9, 2, 7, 13, 14),  Block.box(5, 7, 2, 9, 11, 14), Block.box(7, 5, 2, 11, 9, 14),  Block.box(9, 3, 2, 13, 7, 14)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    protected static final VoxelShape WEST_AABB = Stream.of( Block.box(15, 0, 1, 16, 3, 15), Block.box(15, 3, 0, 16, 15, 16), Block.box(13, 4, 1, 15, 13, 15), Block.box(9, 9, 2, 13, 13, 14), Block.box(7, 7, 2, 11, 11, 14), Block.box(5, 5, 2, 9, 9, 14), Block.box(3, 3, 2, 7, 7, 14)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public GoldenTrophyBlock(Properties properties, double pushStrength, double pushVerticalStrength) {
        super(properties, pushStrength, pushVerticalStrength);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case SOUTH -> SOUTH_AABB;
            case WEST -> WEST_AABB;
            case EAST -> EAST_AABB;
            default -> NORTH_AABB;
        };
    }
}
