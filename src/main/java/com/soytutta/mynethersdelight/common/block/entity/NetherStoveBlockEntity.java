package com.soytutta.mynethersdelight.common.block.entity;

import com.soytutta.mynethersdelight.common.registry.MNDBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.crafting.*;
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
        float X = 0.3F;
        float Y = 0.2F;

        Vec2[] OFFSETS = {
                new Vec2(X, Y),
                new Vec2(0.0F, Y),
                new Vec2(-X, Y),
                new Vec2(X, -Y),
                new Vec2(0.0F, -Y),
                new Vec2(-X, -Y)
        };

        return OFFSETS[index];
    }

    public static void particleTick(Level level, BlockPos pos, BlockState state, NetherStoveBlockEntity stove) {
        if (!stove.isEmpty()) {
            stove.addSmokeParticles();
        }
    }

    public void addSmokeParticles() {
        if (this.level == null) return;

        for (int i = 0; i < this.getItems().getSlots(); ++i) {
            if (!this.getItems().getStackInSlot(i).isEmpty() && this.level.random.nextFloat() < 0.2F) {

                Vec2 offset = this.getStoveItemOffset(i);
                Direction dir = this.getBlockState().getValue(AbstractStoveBlock.FACING);

                if (dir.get2DDataValue() % 2 != 0) {
                    offset = new Vec2(offset.y, offset.x);
                }

                double x = this.worldPosition.getX() + 0.5 - dir.getStepX() * offset.x + dir.getClockWise().getStepX() * offset.x;
                double y = this.worldPosition.getY() + 1.0;
                double z = this.worldPosition.getZ() + 0.5 - dir.getStepZ() * offset.y + dir.getClockWise().getStepZ() * offset.y;

                for (int k = 0; k < 3; ++k) {
                    this.level.addParticle(ParticleTypes.SMOKE, x, y, z, 0, 5e-4, 0);
                }
            }
        }
    }
}