package com.soytutta.mynethersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import vectorwing.farmersdelight.common.registry.ModDamageTypes;

import javax.annotation.Nullable;

public class PepperCrateBlock extends Block {

    public PepperCrateBlock(Properties properties) {
        super(properties);
    }

    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!entity.isSteppingCarefully() && entity instanceof LivingEntity) {
            entity.hurt(ModDamageTypes.getSimpleDamageSource(level, DamageTypes.HOT_FLOOR), 1.0F);
        }
        super.stepOn(level, pos, state, entity);
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return false;
    }

    @Nullable
    @Override
    public BlockPathTypes getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob) {
        return BlockPathTypes.DAMAGE_FIRE;
    }
}
