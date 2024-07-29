package com.soytutta.mynethersdelight.common.block;

import com.soytutta.mynethersdelight.common.block.entity.MNDSignBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

public class MNDWallSignBlock extends WallSignBlock {
    public MNDWallSignBlock(Properties properties, WoodType woodType) {
        super(woodType, properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MNDSignBlockEntity(pos, state);
    }
}