package com.soytutta.mynethersdelight.common.block.entity;

import com.soytutta.mynethersdelight.common.registry.MNDBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MNDSignBlockEntity extends SignBlockEntity {
    public MNDSignBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(pWorldPosition, pBlockState);
    }

    public BlockEntityType<?> getType() {
        return MNDBlockEntityTypes.MND_SIGN.get();
    }
}