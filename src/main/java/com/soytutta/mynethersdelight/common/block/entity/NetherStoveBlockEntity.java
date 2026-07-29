package com.soytutta.mynethersdelight.common.block.entity;

import com.soytutta.mynethersdelight.common.registry.MNDBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import vectorwing.farmersdelight.common.block.AbstractStoveBlock;
import vectorwing.farmersdelight.common.block.entity.AbstractStoveBlockEntity;

public class NetherStoveBlockEntity extends AbstractStoveBlockEntity {
    public NetherStoveBlockEntity(BlockPos pos, BlockState state) {
        super(MNDBlockEntityTypes.NETHER_STOVE.get(), pos, state, RecipeType.CAMPFIRE_COOKING);
    }

    @Override
    protected int getInventorySlotCount() {
        return 6;
    }

    @Override
    public Vec2 getStoveItemOffset(int index) {
        float x = 0.3F;
        float y = 0.2F;
        Vec2[] offsets = {
                new Vec2(x, y),
                new Vec2(0.0F, y),
                new Vec2(-x, y),
                new Vec2(x, -y),
                new Vec2(0.0F, -y),
                new Vec2(-x, -y)
        };
        return offsets[index];
    }

    public static void particleTick(Level level, BlockPos pos, BlockState state, NetherStoveBlockEntity stove) {
        if (!stove.isEmpty()) {
            stove.addSmokeParticles();
        }
    }

    private void addSmokeParticles() {
        if (this.level == null) {
            return;
        }

        for (int i = 0; i < this.getItems().getSlots(); ++i) {
            if (this.getItems().getStackInSlot(i).isEmpty() || this.level.random.nextFloat() >= 0.2F) {
                continue;
            }

            Vec2 offset = this.getStoveItemOffset(i);
            Direction direction = this.getBlockState().getValue(AbstractStoveBlock.FACING);
            if (direction.get2DDataValue() % 2 != 0) {
                offset = new Vec2(offset.y, offset.x);
            }

            double x = this.worldPosition.getX() + 0.5
                    - direction.getStepX() * offset.x + direction.getClockWise().getStepX() * offset.x;
            double y = this.worldPosition.getY() + 1.0;
            double z = this.worldPosition.getZ() + 0.5
                    - direction.getStepZ() * offset.y + direction.getClockWise().getStepZ() * offset.y;

            for (int k = 0; k < 3; ++k) {
                this.level.addParticle(ParticleTypes.SMOKE, x, y, z, 0, 5.0E-4, 0);
            }
        }
    }
}
