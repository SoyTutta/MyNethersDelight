package com.soytutta.mynethersdelight.common.world.feature;

import com.mojang.serialization.Codec;
import com.soytutta.mynethersdelight.common.block.crops.PowderyCaneBlock;
import com.soytutta.mynethersdelight.common.block.crops.PowderyFlowerBlock;
import com.soytutta.mynethersdelight.common.registry.MNDBlocks;
import com.soytutta.mynethersdelight.common.tag.MNDTags;
import com.soytutta.mynethersdelight.common.world.configuration.PowderyCaneConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BambooLeaves;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PowderyCaneFeature extends Feature<PowderyCaneConfiguration> {
    public PowderyCaneFeature(Codec<PowderyCaneConfiguration> config) {
        super(config);
    }

    @Override
    public boolean place(FeaturePlaceContext<PowderyCaneConfiguration> context) {
        PowderyCaneConfiguration config = context.config();
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        BlockPos center = findPatchCenter(level, origin, random, config);
        if (center == null) {
            return false;
        }

        PatchShape shape = createPatchShape(config, random);
        int targetCaneCount = getTargetCaneCount(random);
        List<BlockPos> canePositions = collectCanePositions(level, center, random, config, shape);
        if (canePositions.size() > targetCaneCount) {
            canePositions = new ArrayList<>(canePositions.subList(0, targetCaneCount));
        }
        canePositions.sort(Comparator.comparingLong(pos -> horizontalDistanceSquared(center, pos)));

        List<BlockPos> placedCanes = placeCanes(level, center, canePositions, random, shape);
        if (placedCanes.isEmpty()) {
            return false;
        }

        placeLooseFlowers(level, center, placedCanes, random, config, shape);
        return true;
    }

    private BlockPos findPatchCenter(WorldGenLevel level, BlockPos origin, RandomSource random, PowderyCaneConfiguration config) {
        BlockPos center = findPlantablePosition(level, origin, config.ySpread(), true);
        if (center != null) {
            return center;
        }

        for (int i = 0; i < config.tries(); i++) {
            BlockPos candidate = origin.offset(
                    random.nextInt(config.xzSpread() * 2 + 1) - config.xzSpread(),
                    0,
                    random.nextInt(config.xzSpread() * 2 + 1) - config.xzSpread()
            );
            center = findPlantablePosition(level, candidate, config.ySpread(), true);
            if (center != null) {
                return center;
            }
        }

        return null;
    }

    private PatchShape createPatchShape(PowderyCaneConfiguration config, RandomSource random) {
        int flowerMajorRadius = config.xzSpread();
        int flowerMinorRadius = flowerMajorRadius == 0 ? 0 : Math.max(1, flowerMajorRadius * 2 / 3);
        int caneMajorRadius = flowerMajorRadius == 0 ? 0 : Math.max(1, flowerMajorRadius / 2);
        int caneMinorRadius = caneMajorRadius == 0 ? 0 : Math.max(1, caneMajorRadius * 2 / 3);
        double angle = random.nextDouble() * Math.PI * 2.0;
        return new PatchShape(
                Math.cos(angle),
                Math.sin(angle),
                caneMajorRadius,
                caneMinorRadius,
                flowerMajorRadius,
                flowerMinorRadius
        );
    }

    private int getTargetCaneCount(RandomSource random) {
        int roll = random.nextInt(20);
        if (roll < 3) {
            return 1;
        }
        if (roll < 10) {
            return 2;
        }
        if (roll < 16) {
            return 3;
        }
        if (roll < 19) {
            return 4;
        }
        return 5;
    }

    private List<BlockPos> collectCanePositions(WorldGenLevel level, BlockPos center, RandomSource random,
                                                PowderyCaneConfiguration config, PatchShape shape) {
        List<BlockPos> positions = new ArrayList<>();
        Set<BlockPos> uniquePositions = new HashSet<>();
        positions.add(center);
        uniquePositions.add(center);

        for (int i = 0; i < config.tries(); i++) {
            BlockPos candidate = offsetWithinEllipse(
                    center,
                    shape.caneMajorRadius(),
                    shape.caneMinorRadius(),
                    shape,
                    random
            );
            BlockPos plantablePos = findPlantablePosition(level, candidate, config.ySpread(), true);
            if (plantablePos != null
                    && isOnPatchLayer(center, plantablePos)
                    && uniquePositions.add(plantablePos)) {
                positions.add(plantablePos);
            }
        }

        return positions;
    }

    private List<BlockPos> placeCanes(WorldGenLevel level, BlockPos center, List<BlockPos> positions,
                                      RandomSource random, PatchShape shape) {
        List<BlockPos> placedCanes = new ArrayList<>();
        int centerHeight = placeCane(level, center, 3 + random.nextInt(2), random);
        if (centerHeight == 0) {
            return placedCanes;
        }
        placedCanes.add(center);
        int previousHeight = centerHeight;

        for (int i = 1; i < positions.size(); i++) {
            BlockPos pos = positions.get(i);
            double distance = Math.sqrt(horizontalDistanceSquared(center, pos));
            double radius = Math.max(1, shape.caneMajorRadius());
            int desiredHeight;
            if (distance <= radius * 0.4) {
                desiredHeight = 3 + random.nextInt(2);
            } else if (distance <= radius * 0.75) {
                desiredHeight = 2 + random.nextInt(2);
            } else {
                desiredHeight = 1 + random.nextInt(2);
            }
            desiredHeight = Math.min(desiredHeight, previousHeight);
            int placedHeight = placeCane(level, pos, desiredHeight, random);
            if (placedHeight > 0) {
                placedCanes.add(pos);
                previousHeight = Math.min(previousHeight, placedHeight);
            }
        }

        return placedCanes;
    }

    private int placeCane(WorldGenLevel level, BlockPos pos, int desiredHeight, RandomSource random) {
        if (!isPlantablePosition(level, pos) || !level.isEmptyBlock(pos.above())) {
            return 0;
        }

        int height = Math.min(desiredHeight, countEmptyBlocksAbove(level, pos, desiredHeight));
        if (height == 0) {
            return 0;
        }

        BlockState powderyCaneBase = MNDBlocks.POWDERY_CANE.get().defaultBlockState().setValue(PowderyCaneBlock.AGE, 1);
        BlockState powderyCane = MNDBlocks.POWDERY_CANE.get().defaultBlockState();
        BlockState powderyCaneLeaves = powderyCane.setValue(PowderyCaneBlock.LEAVES, BambooLeaves.SMALL);

        for (int i = 0; i < height; i++) {
            BlockState state = i == 0 ? powderyCaneBase : powderyCane;
            if (i == height - 1 && height > 1) {
                state = powderyCaneLeaves;
            }
            level.setBlock(pos.above(i), state, 19);
        }

        level.setBlock(pos.above(height), createFlowerState(random), 19);
        return height;
    }

    private void placeLooseFlowers(WorldGenLevel level, BlockPos center, List<BlockPos> canePositions,
                                   RandomSource random, PowderyCaneConfiguration config, PatchShape shape) {
        Set<BlockPos> placedFlowers = new HashSet<>();

        for (BlockPos canePos : canePositions) {
            BlockPos nearbyFlower = findFlowerPosition(
                    level,
                    center,
                    canePos,
                    3,
                    2,
                    0,
                    canePositions,
                    placedFlowers,
                    random,
                    config,
                    shape
            );
            if (nearbyFlower == null) {
                nearbyFlower = findFlowerPosition(
                        level,
                        center,
                        center,
                        shape.flowerMajorRadius(),
                        shape.flowerMinorRadius(),
                        0,
                        canePositions,
                        placedFlowers,
                        random,
                        config,
                        shape
                );
            }
            if (nearbyFlower != null) {
                level.setBlock(nearbyFlower, createFlowerState(random), 19);
                placedFlowers.add(nearbyFlower);
            }

            if (random.nextBoolean()) {
                long outerDistance = (long) shape.caneMajorRadius() * shape.caneMajorRadius();
                BlockPos outerFlower = findFlowerPosition(
                        level,
                        center,
                        center,
                        shape.flowerMajorRadius(),
                        shape.flowerMinorRadius(),
                        outerDistance,
                        canePositions,
                        placedFlowers,
                        random,
                        config,
                        shape
                );
                if (outerFlower == null) {
                    outerFlower = findFlowerPosition(
                            level,
                            center,
                            center,
                            shape.flowerMajorRadius(),
                            shape.flowerMinorRadius(),
                            0,
                            canePositions,
                            placedFlowers,
                            random,
                            config,
                            shape
                    );
                }
                if (outerFlower != null) {
                    level.setBlock(outerFlower, createFlowerState(random), 19);
                    placedFlowers.add(outerFlower);
                }
            }
        }
    }

    private BlockPos findFlowerPosition(WorldGenLevel level, BlockPos center, BlockPos anchor, int majorRadius,
                                        int minorRadius, long minimumCenterDistance, List<BlockPos> canePositions,
                                        Set<BlockPos> placedFlowers, RandomSource random,
                                        PowderyCaneConfiguration config, PatchShape shape) {
        BlockPos fallback = null;

        for (int i = 0; i < config.tries(); i++) {
            BlockPos candidate = offsetWithinEllipse(anchor, majorRadius, minorRadius, shape, random);
            BlockPos plantablePos = findPlantablePosition(level, candidate, config.ySpread(), false);
            if (plantablePos == null
                    || !isOnPatchLayer(center, plantablePos)
                    || horizontalDistanceSquared(center, plantablePos) < minimumCenterDistance
                    || sharesColumnWithCane(plantablePos, canePositions)
                    || placedFlowers.contains(plantablePos)) {
                continue;
            }

            if (fallback == null) {
                fallback = plantablePos;
            }
            if (isSeparatedFromFlowers(plantablePos, placedFlowers)) {
                return plantablePos;
            }
        }

        return fallback;
    }

    private BlockPos offsetWithinEllipse(BlockPos center, int majorRadius, int minorRadius,
                                         PatchShape shape, RandomSource random) {
        if (majorRadius == 0 || minorRadius == 0) {
            return center;
        }

        int localX = 0;
        int localZ = 0;
        for (int i = 0; i < 8; i++) {
            localX = random.nextInt(majorRadius + 1) - random.nextInt(majorRadius + 1);
            localZ = random.nextInt(minorRadius + 1) - random.nextInt(minorRadius + 1);
            double normalizedX = (double) localX / majorRadius;
            double normalizedZ = (double) localZ / minorRadius;
            if (normalizedX * normalizedX + normalizedZ * normalizedZ <= 1.0) {
                break;
            }
        }

        int rotatedX = (int) Math.round(localX * shape.cosine() - localZ * shape.sine());
        int rotatedZ = (int) Math.round(localX * shape.sine() + localZ * shape.cosine());
        return center.offset(rotatedX, 0, rotatedZ);
    }

    private BlockPos findPlantablePosition(WorldGenLevel level, BlockPos pos, int ySpread, boolean needsTopSpace) {
        if (isPlantablePosition(level, pos) && (!needsTopSpace || level.isEmptyBlock(pos.above()))) {
            return pos.immutable();
        }

        for (int distance = 1; distance <= ySpread; distance++) {
            BlockPos above = pos.above(distance);
            if (isPlantablePosition(level, above) && (!needsTopSpace || level.isEmptyBlock(above.above()))) {
                return above.immutable();
            }

            BlockPos below = pos.below(distance);
            if (isPlantablePosition(level, below) && (!needsTopSpace || level.isEmptyBlock(below.above()))) {
                return below.immutable();
            }
        }

        return null;
    }

    private boolean isPlantablePosition(WorldGenLevel level, BlockPos pos) {
        return !level.isOutsideBuildHeight(pos)
                && level.isEmptyBlock(pos)
                && canGrowPowderyCane(level.getBlockState(pos.below()));
    }

    private int countEmptyBlocksAbove(WorldGenLevel level, BlockPos pos, int limit) {
        int count = 0;
        BlockPos currentPos = pos.above();
        while (!level.isOutsideBuildHeight(currentPos) && level.isEmptyBlock(currentPos) && count < limit) {
            count++;
            currentPos = currentPos.above();
        }
        return count;
    }

    private BlockState createFlowerState(RandomSource random) {
        if (random.nextBoolean()) {
            return MNDBlocks.BULLET_PEPPER.get().defaultBlockState()
                    .setValue(PowderyFlowerBlock.LIT, true)
                    .setValue(PowderyFlowerBlock.AGE, 2);
        }
        return MNDBlocks.BULLET_PEPPER.get().defaultBlockState()
                .setValue(PowderyFlowerBlock.AGE, random.nextInt(PowderyFlowerBlock.MAX_AGE));
    }

    private boolean sharesColumnWithCane(BlockPos flowerPos, List<BlockPos> canePositions) {
        for (BlockPos canePos : canePositions) {
            if (flowerPos.getX() == canePos.getX() && flowerPos.getZ() == canePos.getZ()) {
                return true;
            }
        }
        return false;
    }

    private boolean isSeparatedFromFlowers(BlockPos candidate, Set<BlockPos> selectedPositions) {
        for (BlockPos selectedPos : selectedPositions) {
            if (horizontalDistanceSquared(candidate, selectedPos) < 4) {
                return false;
            }
        }
        return true;
    }

    private long horizontalDistanceSquared(BlockPos first, BlockPos second) {
        long x = first.getX() - second.getX();
        long z = first.getZ() - second.getZ();
        return x * x + z * z;
    }

    private boolean isOnPatchLayer(BlockPos center, BlockPos pos) {
        return Math.abs(center.getY() - pos.getY()) <= 2;
    }

    public static boolean canGrowPowderyCane(BlockState state) {
        return state.is(MNDTags.POWDERY_CANNON_PLANTABLE_ON);
    }

    private record PatchShape(double cosine, double sine, int caneMajorRadius, int caneMinorRadius,
                              int flowerMajorRadius, int flowerMinorRadius) {
    }
}
