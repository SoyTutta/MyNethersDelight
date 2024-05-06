package com.soytutta.mynethersdelight.common.block.entity;

import com.soytutta.mynethersdelight.common.registry.MNDBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MNDHangingSignBlockEntity extends HangingSignBlockEntity {

    public MNDHangingSignBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(pWorldPosition, pBlockState);
    }

    public BlockEntityType<?> getType() {
        return MNDBlockEntityTypes.MND_HSIGN.get();
    }
}