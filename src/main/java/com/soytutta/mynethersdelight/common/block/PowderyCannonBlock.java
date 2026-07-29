package com.soytutta.mynethersdelight.common.block;

import com.soytutta.mynethersdelight.common.registry.MNDBlocks;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import com.soytutta.mynethersdelight.common.tag.MNDTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BambooLeaves;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.Tags;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import javax.annotation.Nullable;

public class PowderyCannonBlock extends BambooStalkBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final IntegerProperty PRESSURE = IntegerProperty.create("pressure", 0, 2);

    public PowderyCannonBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(AGE, 0)
                .setValue(LEAVES, BambooLeaves.NONE)
                .setValue(STAGE, 0)
                .setValue(LIT, false)
                .setValue(PRESSURE, 0));
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target,
                                       BlockGetter level, BlockPos pos, Player player) {
        return new ItemStack(MNDItems.POWDER_CANNON.get());
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState blockBelow = level.getBlockState(pos.below());
        return blockBelow.is(MNDTags.POWDERY_CANNON_PLANTABLE_ON)
                || blockBelow.canSustainPlant(level, pos.below(), Direction.UP, this);
    }

    @Override
    public BlockPathTypes getBlockPathType(BlockState state, BlockGetter level,
                                           BlockPos pos, @Nullable Mob mob) {
        return BlockPathTypes.DAMAGE_OTHER;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (!state.canSurvive(level, pos)) {
            level.scheduleTick(pos, this, 1);
        }
        if (state.getValue(PRESSURE) > 0) {
            level.scheduleTick(pos, this, 1);
        }
        if (direction == Direction.UP && neighborState.is(MNDBlocks.POWDERY_CANNON.get())
                && neighborState.getValue(AGE) > state.getValue(AGE)) {
            level.setBlock(pos, state.cycle(AGE), 2);
        }
        if (state.getValue(LEAVES) == BambooLeaves.NONE && state.getValue(LIT)) {
            return state.setValue(LIT, false);
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        if (!fluidState.isEmpty()) {
            return null;
        }

        BlockPos belowPos = context.getClickedPos().below();
        BlockState blockBelow = context.getLevel().getBlockState(belowPos);
        if (!blockBelow.is(MNDTags.POWDERY_CANNON_PLANTABLE_ON)
                && !blockBelow.canSustainPlant(context.getLevel(), belowPos, Direction.UP, this)) {
            return null;
        }

        if (blockBelow.is(MNDBlocks.POWDERY_CHUBBY_SAPLING.get())) {
            return defaultBlockState().setValue(AGE, 0);
        }
        if (blockBelow.is(MNDBlocks.POWDERY_CANNON.get())) {
            return defaultBlockState().setValue(AGE, blockBelow.getValue(AGE) > 0 ? 1 : 0);
        }

        BlockState blockAbove = context.getLevel().getBlockState(context.getClickedPos().above());
        return blockAbove.is(MNDBlocks.POWDERY_CANNON.get())
                ? defaultBlockState().setValue(AGE, blockAbove.getValue(AGE))
                : MNDBlocks.POWDERY_CHUBBY_SAPLING.get().defaultBlockState();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIT, PRESSURE);
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return false;
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(STAGE) == 0
                || state.getValue(LEAVES) != BambooLeaves.NONE && !state.getValue(LIT);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.is(this)) {
            return;
        }

        int pressure = state.getValue(PRESSURE);
        boolean isLit = state.getValue(LIT);
        if (!state.canSurvive(level, pos)) {
            if (isLit) {
                explodeAndReset(level, pos, state);
            }
            level.destroyBlock(pos, true);
            return;
        }

        if (pressure > 0) {
            BlockPos abovePos = pos.above();
            BlockState aboveState = level.getBlockState(abovePos);
            if (aboveState.hasProperty(PRESSURE)
                    && aboveState.getValue(PRESSURE) < pressure) {
                level.setBlock(abovePos, aboveState.setValue(PRESSURE, pressure), 3);
                level.scheduleTick(abovePos, aboveState.getBlock(), 2);
            }
        }

        if (pressure == 2 && isLit) {
            explodeAndReset(level, pos, state);
            return;
        }
        if (pressure > 0) {
            level.scheduleTick(pos, this, 20);
            level.setBlock(pos, state.setValue(PRESSURE, pressure - 1), 2);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.is(this)) {
            return;
        }

        int maxHeight = getSkyAccessMaxHeight(level, pos);
        int currentHeight = getHeightBelowUpToMax(level, pos, maxHeight) + 1;
        boolean isLit = state.getValue(LIT);
        boolean hasLeaves = state.getValue(LEAVES) != BambooLeaves.NONE;
        boolean finished = state.getValue(STAGE) == 1;
        BlockPos abovePos = pos.above();

        if (!finished) {
            int blocksToDisplace = calculateBlocksToDisplace(level, abovePos);
            boolean canGrow = currentHeight < maxHeight
                    && blocksToDisplace != Integer.MAX_VALUE
                    && abovePos.getY() + blocksToDisplace < level.getMaxBuildHeight();
            if (canGrow && ForgeHooks.onCropsGrowPre(
                    level, pos, state, random.nextInt(2) == 0)) {
                growCannon(state, level, pos, random, currentHeight);
                ForgeHooks.onCropsGrowPost(level, pos, state);
            }
        }

        if (!isLit && hasLeaves && (currentHeight >= maxHeight - 5 || finished)) {
            int igniteChance = level.dimension() == Level.NETHER
                    ? 300 : level.getBiome(pos).is(Tags.Biomes.IS_HOT_OVERWORLD) ? 500 : 900;
            if (random.nextInt(igniteChance) == 0) {
                level.setBlock(pos, state.setValue(LIT, true), 2);
                playCannonSound(level, pos);
            }
        }

        BlockState aboveState = level.getBlockState(abovePos);
        if (isLit && aboveState.is(MNDBlocks.POWDERY_CANNON.get())
                && !aboveState.getValue(LIT)) {
            level.setBlock(pos, state.setValue(LIT, false), 2);
            playCannonSound(level, pos);
            level.setBlock(abovePos, aboveState.setValue(LIT, true), 2);
            playCannonSound(level, abovePos);
        }
        level.scheduleTick(pos, this, 1);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (entity instanceof LivingEntity livingEntity
                && entity.getType() != EntityType.PANDA
                && entity.getType() != EntityType.BEE
                && !livingEntity.isCrouching()) {
            entity.makeStuckInBlock(state, new Vec3(0.8F, 0.75F, 0.8F));
            entity.hurt(level.damageSources().cactus(), 1.0F);
            if (!level.isClientSide) {
                int pressure = state.getValue(PRESSURE);
                if (pressure < 2) {
                    level.setBlock(pos, state.setValue(PRESSURE, pressure + 1), 2);
                    level.scheduleTick(pos, this, 1);
                }
                if (state.getValue(LIT)) {
                    explodeAndReset(level, pos, state);
                }
            }
        }
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        stepOn(level, pos, state, entity);
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hitResult) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (state.getValue(LIT)
                && (ItemUtils.isKnife(heldItem) || heldItem.is(Tags.Items.SHEARS))) {
            heldItem.hurtAndBreak(1, player, user -> user.broadcastBreakEvent(hand));
            popResource(level, pos,
                    new ItemStack(MNDItems.BULLET_PEPPER.get(), 3 + level.random.nextInt(6)));
            level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES,
                    SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
            level.setBlock(pos, state.setValue(LIT, false), 3);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        if (state.getValue(LIT)) {
            if (!level.isClientSide) {
                explodeAndReset(level, pos, state);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && state.getValue(PRESSURE) < 2 && !player.isCrouching()) {
            level.setBlock(pos, state.setValue(PRESSURE, state.getValue(PRESSURE) + 1), 2);
        }
        if (state.getValue(LIT)) {
            ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
            if (!ItemUtils.isKnife(heldItem) && !heldItem.is(Tags.Items.SHEARS)) {
                explodeAndReset(level, pos, state);
            }
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos,
                                         BlockState state, boolean isClientSide) {
        int maxHeight = getSkyAccessMaxHeight(level, pos);
        int heightAbove = getHeightAboveUpToMax(level, pos, maxHeight);
        int heightBelow = getHeightBelowUpToMax(level, pos, maxHeight);
        int totalHeight = heightAbove + heightBelow + 1;
        BlockPos topPos = pos.above(heightAbove);
        BlockState topState = level.getBlockState(topPos);
        if (!topState.is(MNDBlocks.POWDERY_CANNON.get()) || topState.getValue(STAGE) == 1) {
            return false;
        }

        BlockPos newPos = topPos.above();
        int blocksToDisplace = calculateBlocksToDisplace(level, newPos);
        return totalHeight < maxHeight
                && blocksToDisplace != Integer.MAX_VALUE
                && newPos.getY() + blocksToDisplace < level.getMaxBuildHeight();
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random,
                                     BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random,
                                BlockPos pos, BlockState state) {
        int maxHeight = getSkyAccessMaxHeight(level, pos);
        int segmentsToGrow = 1 + random.nextInt(2);
        for (int i = 0; i < segmentsToGrow; i++) {
            int heightAbove = getHeightAboveUpToMax(level, pos, maxHeight);
            int heightBelow = getHeightBelowUpToMax(level, pos, maxHeight);
            int totalHeight = heightAbove + heightBelow + 1;
            BlockPos topPos = pos.above(heightAbove);
            BlockState topState = level.getBlockState(topPos);
            if (!topState.is(MNDBlocks.POWDERY_CANNON.get()) || topState.getValue(STAGE) == 1) {
                return;
            }

            BlockPos newPos = topPos.above();
            int blocksToDisplace = calculateBlocksToDisplace(level, newPos);
            if (totalHeight >= maxHeight || blocksToDisplace == Integer.MAX_VALUE
                    || newPos.getY() + blocksToDisplace >= level.getMaxBuildHeight()) {
                return;
            }
            growCannon(topState, level, topPos, random, totalHeight);
        }
    }

    private int getSkyAccessMaxHeight(LevelReader level, BlockPos pos) {
        return level.canSeeSky(pos) ? 18 : 8;
    }

    private int calculateBlocksToDisplace(LevelReader level, BlockPos startPos) {
        int blocksToDisplace = 0;
        BlockPos checkPos = startPos;
        while (checkPos.getY() < level.getMaxBuildHeight()) {
            BlockState checkState = level.getBlockState(checkPos);
            if (checkState.isAir()) {
                break;
            }
            if (checkState.is(MNDBlocks.POWDERY_CANE.get())
                    || checkState.is(MNDBlocks.BULLET_PEPPER.get())) {
                blocksToDisplace++;
                checkPos = checkPos.above();
            } else {
                return Integer.MAX_VALUE;
            }
        }
        return blocksToDisplace;
    }

    private void playCannonSound(Level level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.CROSSBOW_LOADING_MIDDLE,
                SoundSource.BLOCKS, 0.5F, 0.25F);
    }

    private void explodeAndReset(Level level, BlockPos pos, BlockState state) {
        if (!level.isClientSide && state.getValue(LIT)) {
            level.playSound(null, pos, SoundEvents.CREEPER_PRIMED,
                    SoundSource.BLOCKS, 0.5F, 0.25F);
            level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    1.0F, false, Level.ExplosionInteraction.NONE);
            level.setBlock(pos, state.setValue(LIT, false), 2);
        }
    }

    protected void growCannon(BlockState topState, Level level, BlockPos topPos,
                              RandomSource random, int currentHeight) {
        BlockState belowState = level.getBlockState(topPos.below());
        BlockPos twoBelowPos = topPos.below(2);
        BlockState twoBelowState = level.getBlockState(twoBelowPos);
        BambooLeaves leaves = BambooLeaves.NONE;
        int maxHeight = getSkyAccessMaxHeight(level, topPos);

        if (currentHeight >= 1) {
            if (belowState.is(MNDBlocks.POWDERY_CANNON.get())
                    && belowState.getValue(LEAVES) != BambooLeaves.NONE) {
                leaves = BambooLeaves.LARGE;
                if (twoBelowState.is(MNDBlocks.POWDERY_CANNON.get())) {
                    level.setBlock(topPos.below(), belowState.setValue(LEAVES, BambooLeaves.SMALL), 3);
                    level.setBlock(twoBelowPos, twoBelowState.setValue(LEAVES, BambooLeaves.NONE), 3);
                }
            } else {
                leaves = BambooLeaves.SMALL;
            }
        }

        int newAge = topState.getValue(AGE) != 1
                && !twoBelowState.is(MNDBlocks.POWDERY_CANNON.get()) ? 0 : 1;
        int newStage = currentHeight + 1 >= maxHeight
                || currentHeight + 1 >= maxHeight - 5 && random.nextFloat() < 0.25F ? 1 : 0;
        BlockPos newPos = topPos.above();

        if ((topState.getValue(LEAVES) != BambooLeaves.NONE
                || topState.getValue(STAGE) == 1) && level.isEmptyBlock(newPos)) {
            level.setBlock(newPos, MNDBlocks.BULLET_PEPPER.get().defaultBlockState(), 3);
            return;
        }

        BlockPos scanPos = newPos;
        BlockState scanState = level.getBlockState(scanPos);
        while (scanPos.getY() < level.getMaxBuildHeight()
                && (scanState.is(MNDBlocks.POWDERY_CANE.get())
                || scanState.is(MNDBlocks.BULLET_PEPPER.get()))) {
            scanPos = scanPos.above();
            scanState = level.getBlockState(scanPos);
        }

        BlockPos blockToMove = scanPos.below();
        while (blockToMove.compareTo(newPos) >= 0) {
            BlockState stateToMove = level.getBlockState(blockToMove);
            if (!stateToMove.isAir()) {
                level.setBlock(blockToMove.above(), stateToMove, 3);
            }
            blockToMove = blockToMove.below();
        }

        level.setBlock(newPos, defaultBlockState()
                .setValue(AGE, newAge)
                .setValue(LEAVES, leaves)
                .setValue(STAGE, newStage)
                .setValue(LIT, false)
                .setValue(PRESSURE, 0), 3);
    }

    protected int getHeightAboveUpToMax(BlockGetter level, BlockPos pos, int maxHeight) {
        int height;
        for (height = 0; height < maxHeight
                && level.getBlockState(pos.above(height + 1))
                .is(MNDBlocks.POWDERY_CANNON.get()); height++) {
        }
        return height;
    }

    protected int getHeightBelowUpToMax(BlockGetter level, BlockPos pos, int maxHeight) {
        int height;
        for (height = 0; height < maxHeight
                && level.getBlockState(pos.below(height + 1))
                .is(MNDBlocks.POWDERY_CANNON.get()); height++) {
        }
        return height;
    }
}
