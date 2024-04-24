package com.soytutta.mynethersdelight.common.world.feature;

import com.mojang.serialization.Codec;
import com.soytutta.mynethersdelight.common.block.PowderyCaneBlock;
import com.soytutta.mynethersdelight.common.registry.MNDBlocks;
import com.soytutta.mynethersdelight.common.tag.MNDTags;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.HashMap;
import java.util.Iterator;
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
        BlockState PowderyCaneLeave = MNDBlocks.POWDERY_CANE.get().defaultBlockState().setValue(PowderyCaneBlock.LEAVE, true);
        BlockState powderyFlower = MNDBlocks.BULLET_PEPPER.get().defaultBlockState().setValue(PowderyCaneBlock.LIT, true).setValue(PowderyCaneBlock.AGE, 2);
        BlockState powderyCannon = MNDBlocks.POWDERY_CHUBBY_SAPLING.get().defaultBlockState();
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
                            if (level.isEmptyBlock(blockpos) && !level.isOutsideBuildHeight(above)) {
                                if (rand.nextBoolean()) {
                                    blocks.put(blockpos, powderyFlower);
                                } else if (rand.nextBoolean() && level.isEmptyBlock(above)) {
                                    blocks.put(blockpos, powderyCaneBase);
                                    blocks.put(above, powderyFlower);
                                } else if (level.isEmptyBlock(above) && level.isEmptyBlock(evenMoreAbove) && rand.nextInt(8) == 0) {
                                    blocks.put(blockpos, powderyCannon);
                                } else if (level.isEmptyBlock(above) && level.isEmptyBlock(evenMoreAbove)) {
                                    blocks.put(blockpos, powderyCaneBase);
                                    blocks.put(above, PowderyCaneLeave);
                                    blocks.put(evenMoreAbove, powderyFlower);
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
