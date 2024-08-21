package com.soytutta.mynethersdelight.common.world.feature;

import com.mojang.serialization.Codec;
import com.soytutta.mynethersdelight.common.block.PowderyCaneBlock;
import com.soytutta.mynethersdelight.common.block.PowderyFlowerBlock;
import com.soytutta.mynethersdelight.common.registry.MNDBlocks;
import com.soytutta.mynethersdelight.common.tag.MNDTags;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.HashMap;
import java.util.Map;

public class PowderyCaneFeature extends Feature<NoneFeatureConfiguration> {
    public PowderyCaneFeature(Codec<NoneFeatureConfiguration> config) {
        super(config);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource rand = level.getRandom();
        boolean hasPlacedAny = false;

        int areaSize = 4;

        int numFeatures = 8;

        int centerX = areaSize / 2;
        int centerZ = areaSize / 2;

        for (int i = 0; i < numFeatures; i++) {
            int offsetX = rand.nextInt(areaSize) - centerX;
            int offsetZ = rand.nextInt(areaSize) - centerZ;
            BlockPos newPos = origin.offset(offsetX, 0, offsetZ);

            double distanceToCenter = Math.sqrt(offsetX * offsetX + offsetZ * offsetZ);
            int maxHeight = (int) Math.max(1, 4 - distanceToCenter);

            if (placeSingleFeature(level, newPos, rand, maxHeight, origin, areaSize)) {
                hasPlacedAny = true;
            }
        }

        return hasPlacedAny;
    }

    private boolean placeSingleFeature(WorldGenLevel level, BlockPos pos, RandomSource rand, int maxHeight, BlockPos origin, int areaSize) {
        BlockState powderyCaneBase = MNDBlocks.POWDERY_CANE.get().defaultBlockState().setValue(PowderyCaneBlock.BASE, true);
        BlockState powderyCane = MNDBlocks.POWDERY_CANE.get().defaultBlockState();
        BlockState powderyCaneLeave = MNDBlocks.POWDERY_CANE.get().defaultBlockState().setValue(PowderyCaneBlock.LEAVE, true);
        BlockState powderyFlower = MNDBlocks.BULLET_PEPPER.get().defaultBlockState().setValue(PowderyFlowerBlock.AGE, rand.nextInt(1));
        BlockState powderyFlowerLIT = MNDBlocks.BULLET_PEPPER.get().defaultBlockState().setValue(PowderyFlowerBlock.LIT, true).setValue(PowderyFlowerBlock.AGE, 2);
        HashMap<BlockPos, BlockState> blocks = new HashMap<>();
        int i = 0;

        for (int x = -3; x <= 3; ++x) {
            for (int z = -3; z <= 3; ++z) {
                if (Math.abs(x) < 2 || Math.abs(z) < 2) {
                    for (int y = -1; y <= 1; ++y) {
                        BlockPos blockpos = pos.offset(x, y, z);
                        BlockPos below = blockpos.below();
                        BlockState belowState = level.getBlockState(below);
                        if (canGrowPowderyCane(belowState) && rand.nextInt(3) == 0) {
                            BlockPos above = blockpos.above();
                            int emptyBlocksAbove = countEmptyBlocksAbove(level, blockpos);
                            if (level.isEmptyBlock(blockpos) && !level.isOutsideBuildHeight(above)) {

                                int deltaX = origin.getX() - pos.getX();
                                int deltaZ = origin.getZ() - pos.getZ();
                                double distanceToCenter = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

                                double proximityFactor = Math.max(0.1, 1.0 - (distanceToCenter / (areaSize * 0.5)));
                                int heightToPlace = Math.min(maxHeight, (int) ((2 + rand.nextInt(5)) * proximityFactor));

                                if (rand.nextInt(3) == 0) {
                                    blocks.put(blockpos, rand.nextBoolean() ? powderyFlower : powderyFlowerLIT);
                                } else if (rand.nextBoolean() && emptyBlocksAbove >= heightToPlace) {
                                    for (int j = 0; j < heightToPlace; j++) {
                                        BlockPos canePos = blockpos.above(j);
                                        if (heightToPlace == 1) {
                                            blocks.put(canePos, powderyCaneBase);
                                        } else {
                                            if (level.isEmptyBlock(canePos)) {
                                                blocks.put(canePos, (j == 0) ? powderyCaneBase : powderyCane);
                                                if (j == heightToPlace - 1) {
                                                    blocks.put(canePos, powderyCaneLeave);
                                                }
                                            }
                                        }
                                    }
                                    BlockPos topPos = blockpos.above(heightToPlace);
                                    if (level.isEmptyBlock(topPos)) {
                                        blocks.put(topPos, rand.nextBoolean() ? powderyFlower : powderyFlowerLIT);
                                    }
                                }
                            }
                        }
                        ++i;
                    }
                }
            }
        }

        for (Map.Entry<BlockPos, BlockState> entry : blocks.entrySet()) {
            BlockPos entryPos = entry.getKey();
            BlockState entryState = entry.getValue();
            level.setBlock(entryPos, entryState, 19);
        }
        return i > 0;
    }

    private int countEmptyBlocksAbove(WorldGenLevel level, BlockPos pos) {
        int count = 0;
        BlockPos currentPos = pos.above();
        while (level.isEmptyBlock(currentPos) && count < 7) {
            count++;
            currentPos = currentPos.above();
        }
        return count;
    }

    public static boolean canGrowPowderyCane(BlockState state) {
        return state.is(MNDTags.POWDERY_CANNON_PLANTABLE_ON);
    }
}