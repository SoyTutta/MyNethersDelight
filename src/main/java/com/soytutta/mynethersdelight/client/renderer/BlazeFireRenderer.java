package com.soytutta.mynethersdelight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.soytutta.mynethersdelight.common.block.BlazierBlock;
import com.soytutta.mynethersdelight.common.block.entity.BlazierBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BlazeFireRenderer implements BlockEntityRenderer<BlazierBlockEntity> {
    private static final float SIZE = 0.375F;
    private static final float TWO_SLOT_OFFSET = 0.22F;
    private final ItemRenderer itemRenderer;

    public BlazeFireRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(BlazierBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlazierBlock.HeatLevel heat = blockEntity.getBlockState().getValue(BlazierBlock.HEAT);
        Direction facing = blockEntity.getBlockState().getValue(BlazierBlock.FACING);
        NonNullList<ItemStack> items = blockEntity.getItems();
        int seed = (int) blockEntity.getBlockPos().asLong();
        if (BlazierBlockEntity.getMaxSlots(heat) == 2) {
            renderTwoSlots(blockEntity, heat, facing, items, poseStack, bufferSource, packedLight, packedOverlay, seed);
        } else {
            renderFourSlots(facing, items, poseStack, bufferSource, packedLight, packedOverlay, seed);
        }
    }

    private void renderFourSlots(Direction facing, NonNullList<ItemStack> items, PoseStack poseStack,
                                 MultiBufferSource bufferSource, int packedLight, int packedOverlay, int seed) {
        Vec3[] positions = {
                new Vec3(-0.25, 0, -0.25), new Vec3(0.25, 0, -0.25),
                new Vec3(-0.25, 0, 0.25), new Vec3(0.25, 0, 0.25)
        };
        for (int index = 0; index < Math.min(items.size(), 4); index++) {
            ItemStack stack = items.get(index);
            if (stack.isEmpty()) {
                continue;
            }
            Vec3 position = positions[index];
            poseStack.pushPose();
            poseStack.translate(0.5F + position.x, 0.386F, 0.5F + position.z);
            Direction slotDirection = Direction.from2DDataValue((index + facing.get2DDataValue()) % 4);
            poseStack.mulPose(Axis.YP.rotationDegrees(-slotDirection.toYRot()));
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            poseStack.scale(SIZE, SIZE, SIZE);
            itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight, packedOverlay,
                    poseStack, bufferSource, null, seed + index);
            poseStack.popPose();
        }
    }

    private void renderTwoSlots(BlazierBlockEntity blockEntity, BlazierBlock.HeatLevel heat, Direction facing,
                                NonNullList<ItemStack> items, PoseStack poseStack,
                                MultiBufferSource bufferSource, int packedLight, int packedOverlay, int seed) {
        float y = heat == BlazierBlock.HeatLevel.SMOKING ? 0.15F : 0.386F;
        Direction perpendicular = facing.getClockWise();
        float[] offsets = {-TWO_SLOT_OFFSET, TWO_SLOT_OFFSET};
        for (int index = 0; index < Math.min(items.size(), 2); index++) {
            ItemStack stack = items.get(index);
            if (stack.isEmpty()) {
                continue;
            }
            poseStack.pushPose();
            poseStack.translate(0.5F + perpendicular.getStepX() * offsets[index], y,
                    0.5F + perpendicular.getStepZ() * offsets[index]);
            Direction slotDirection = Direction.from2DDataValue((index + facing.get2DDataValue()) % 4);
            poseStack.mulPose(Axis.YP.rotationDegrees(-slotDirection.toYRot()));
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            poseStack.scale(SIZE, SIZE, SIZE);
            itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight, packedOverlay,
                    poseStack, bufferSource, blockEntity.getLevel(), seed + index);
            poseStack.popPose();
        }
    }
}
