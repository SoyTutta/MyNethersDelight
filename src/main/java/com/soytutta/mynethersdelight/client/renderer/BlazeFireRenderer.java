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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

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

        int maxSlots = BlazierBlockEntity.getMaxSlots(heat);

        if (maxSlots == 2) {
            renderTwoSlots(blockEntity, heat, facing, items, poseStack, bufferSource, packedLight, packedOverlay, seed);
        } else {
            renderFourSlots(facing, items, poseStack, bufferSource, packedLight, packedOverlay, seed);
        }
    }

    /**
     * Render para 4 ítems (tipo campfire).
     */
    private void renderFourSlots(Direction facing, NonNullList<ItemStack> items,
                                 PoseStack poseStack, MultiBufferSource bufferSource,
                                 int packedLight, int packedOverlay, int seed) {

        Vec3[] positions = new Vec3[] {
                new Vec3(-0.25, 0, -0.25),
                new Vec3(0.25, 0, -0.25),
                new Vec3(-0.25, 0, 0.25),
                new Vec3(0.25, 0, 0.25)
        };

        int count = Math.min(items.size(), 4);

        for (int j = 0; j < count; j++) {
            ItemStack stack = items.get(j);
            if (stack.isEmpty()) continue;

            Vec3 pos = positions[j];

            poseStack.pushPose();
            poseStack.translate(0.5F + pos.x, 0.386F, 0.5F + pos.z);

            Direction slotDir = Direction.from2DDataValue((j + facing.get2DDataValue()) % 4);
            float yRot = -slotDir.toYRot();

            poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            poseStack.scale(SIZE, SIZE, SIZE);

            itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED,
                    packedLight, packedOverlay, poseStack, bufferSource,
                    null, seed + j);

            poseStack.popPose();
        }
    }

    private void renderTwoSlots(BlazierBlockEntity blockEntity, BlazierBlock.HeatLevel heat,
                                Direction facing, NonNullList<ItemStack> items,
                                PoseStack poseStack, MultiBufferSource bufferSource,
                                int packedLight, int packedOverlay, int seed) {

        float yBase = heat == BlazierBlock.HeatLevel.SMOKING ? 0.15F : 0.386F;

        Direction perp = facing.getClockWise();
        float[] offsets = { -TWO_SLOT_OFFSET, TWO_SLOT_OFFSET };

        int count = Math.min(items.size(), 2);

        for (int j = 0; j < count; j++) {
            ItemStack stack = items.get(j);
            if (stack.isEmpty()) continue;

            float ox = perp.getStepX() * offsets[j];
            float oz = perp.getStepZ() * offsets[j];

            poseStack.pushPose();
            poseStack.translate(0.5F + ox, yBase, 0.5F + oz);

            Direction slotDir = Direction.from2DDataValue((j + facing.get2DDataValue()) % 4);
            float yRot = -slotDir.toYRot();

            poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            poseStack.scale(SIZE, SIZE, SIZE);

            itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED,
                    packedLight, packedOverlay, poseStack, bufferSource,
                    blockEntity.getLevel(), seed + j);

            poseStack.popPose();
        }
    }
}