package com.soytutta.mynethersdelight.common.world.feature;

import com.mojang.serialization.Codec;
import com.soytutta.mynethersdelight.common.block.PowderyCaneBlock;
import com.soytutta.mynethersdelight.common.block.PowderyCannonBlock;
import com.soytutta.mynethersdelight.common.registry.MNDBlocks;
import com.soytutta.mynethersdelight.common.tag.MNDTags;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BambooLeaves;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.HashMap;
import java.util.Map;

public class PowderyCaneFeature extends Feature<NoneFeatureConfiguration> {
    public PowderyCaneFeature(Codec<NoneFeatureConfiguration> config) {
        super(config);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        RandomSource rand = level.getRandom();
        BlockState powderyCaneBase = MNDBlocks.POWDERY_CANE.get().defaultBlockState().setValue(PowderyCaneBlock.BASE, true);
        BlockState PowderyCaneLeave = MNDBlocks.POWDERY_CANE.get().defaultBlockState().setValue(PowderyCaneBlock.LEAVE,  true);
        BlockState powderyFlower = MNDBlocks.BULLET_PEPPER.get().defaultBlockState().setValue(PowderyCaneBlock.AGE, rand.nextInt(1));
        BlockState powderyFlowerLIT = MNDBlocks.BULLET_PEPPER.get().defaultBlockState().setValue(PowderyCaneBlock.LIT, true).setValue(PowderyCaneBlock.AGE, 2);
        BlockState powderyCannon = MNDBlocks.POWDERY_CANNON.get().defaultBlockState();
        HashMap<BlockPos, BlockState> blocks = new HashMap<>();
        int i = 0;

        for(int x = -3; x <= 3; ++x) {
            for(int z = -3; z <= 3; ++z) {
                if (Math.abs(x) < 2 || Math.abs(z) < 2) {
                    for(int y = -1; y <= 1; ++y) {
                        BlockPos blockpos = pos.offset(x, y, z);
                        BlockPos below = blockpos.below();
                        BlockState belowState = level.getBlockState(below);
                        if (canGrowPowderyCane(belowState) && rand.nextInt(3) == 0) {
                            BlockPos above = blockpos.above();
                            BlockPos evenMoreAbove = blockpos.above(2);
                            BlockPos evenMoreeAbove = blockpos.above(3);
                            if (level.isEmptyBlock(blockpos) && !level.isOutsideBuildHeight(above)) {
                                if (rand.nextBoolean()) {
                                    if (rand.nextBoolean()) {
                                        blocks.put(blockpos, powderyFlower);
                                    } else { blocks.put(blockpos, powderyFlowerLIT); }
                                } else if (rand.nextBoolean() && level.isEmptyBlock(above)) {
                                    blocks.put(blockpos, powderyCaneBase);
                                    blocks.put(above, powderyFlower);
                                } else if (level.isEmptyBlock(above) && level.isEmptyBlock(evenMoreAbove)) {
                                    blocks.put(blockpos, powderyCaneBase);
                                    blocks.put(above, PowderyCaneLeave);
                                    blocks.put(evenMoreAbove, powderyFlowerLIT);
                                } else if (level.isEmptyBlock(above) && level.isEmptyBlock(evenMoreAbove) && level.isEmptyBlock(evenMoreeAbove) && rand.nextInt(4) == 0) {
                                    blocks.put(blockpos, powderyCannon.setValue(PowderyCannonBlock.AGE, 1));
                                    blocks.put(above, powderyCannon.setValue(PowderyCannonBlock.AGE, 1));
                                    blocks.put(evenMoreAbove, powderyCannon.setValue(PowderyCannonBlock.AGE, 1).setValue(PowderyCannonBlock.LEAVES, BambooLeaves.SMALL));
                                    blocks.put(evenMoreeAbove, powderyCannon.setValue(PowderyCannonBlock.AGE, 1).setValue(PowderyCannonBlock.LEAVES, BambooLeaves.LARGE).setValue(PowderyCannonBlock.LIT, true));
                                }
                            }
                        } ++i;
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
    public static boolean canGrowPowderyCane(BlockState state) {
        return state.is(MNDTags.POWDERY_CANNON_PLANTABLE_ON);
    }
}
