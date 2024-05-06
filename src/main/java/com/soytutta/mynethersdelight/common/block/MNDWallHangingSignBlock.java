package com.soytutta.mynethersdelight.common.block;

import com.soytutta.mynethersdelight.common.block.entity.MNDHangingSignBlockEntity;
import com.soytutta.mynethersdelight.common.block.entity.MNDSignBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

public class MNDWallHangingSignBlock extends WallHangingSignBlock {
    public MNDWallHangingSignBlock(Properties properties, WoodType woodType) {
        super(properties, woodType);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MNDHangingSignBlockEntity(pos, state);
    }
}