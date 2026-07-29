package com.soytutta.mynethersdelight.common.block.crops;

import com.mojang.serialization.MapCodec;
import com.soytutta.mynethersdelight.common.block.utility.MNDBlockStateProperties;
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
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.util.TriState;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import javax.annotation.Nullable;

import static com.soytutta.mynethersdelight.common.block.utility.MNDBlockStateProperties.PRESSURE;

public class PowderyCaneBlock extends BushBlock implements BonemealableBlock {

    public static final MapCodec<PowderyCaneBlock> CODEC = simpleCodec(PowderyCaneBlock::new);
    public static final int MAX_AGE = 3;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final EnumProperty<BambooLeaves> LEAVES = BlockStateProperties.BAMBOO_LEAVES;
    public static final IntegerProperty STAGE = BlockStateProperties.STAGE;
    private static final VoxelShape SHAPE = Block.box(6.5, 0.0, 6.5, 10.5, 16.0, 10.5);

    public PowderyCaneBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(AGE, 0)
                .setValue(LIT, false)
                .setValue(PRESSURE, 0)
                .setValue(LEAVES, BambooLeaves.NONE)
                .setValue(STAGE, 0));
    }

    public MapCodec<PowderyCaneBlock> codec() {
        return CODEC;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState blockBelow = level.getBlockState(pos.below());
        return blockBelow.is(MNDTags.POWDERY_CANNON_PLANTABLE_ON) || blockBelow.is(MNDBlocks.POWDERY_CANE.get());
    }

    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        if (state.getValue(AGE) == 0) {
            return new ItemStack(MNDItems.BULLET_PEPPER.get());
        } else {
            return new ItemStack(MNDItems.POWDER_CANNON.get());
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        Vec3 vec3 = state.getOffset(worldIn, pos);
        return SHAPE.move(vec3.x, vec3.y, vec3.z);
    }

    @Nullable
    @Override
    public PathType getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob entity) {
        return PathType.DAMAGE_OTHER;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (!state.canSurvive(level, pos)) {
            level.scheduleTick(pos, this, 2);
            return state;
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        if (!fluidstate.isEmpty()) {
            return null;
        }

        BlockState blockstateBelow = context.getLevel().getBlockState(context.getClickedPos().below());
        TriState soilDecision = blockstateBelow.canSustainPlant(context.getLevel(), context.getClickedPos().below(), Direction.UP, this.defaultBlockState());

        if (soilDecision.isDefault() ? !(blockstateBelow.is(MNDTags.POWDERY_CANNON_PLANTABLE_ON) || blockstateBelow.is(MNDBlocks.POWDERY_CANE.get())) : !soilDecision.isTrue()) {
            return null;
        }

        if (blockstateBelow.is(MNDTags.POWDERY_CANNON_PLANTABLE_ON)) {
            return this.defaultBlockState().setValue(AGE, 1).setValue(LEAVES, BambooLeaves.NONE).setValue(STAGE, 0);
        } else if (blockstateBelow.is(MNDBlocks.POWDERY_CANE.get())) {
            return this.defaultBlockState().setValue(AGE, 0).setValue(LEAVES, BambooLeaves.NONE).setValue(STAGE, 0);
        } else {
            BlockState blockstateAbove = context.getLevel().getBlockState(context.getClickedPos().above());
            if (blockstateAbove.is(MNDBlocks.POWDERY_CANE.get())) {
                return this.defaultBlockState().setValue(AGE, 0).setValue(LEAVES, BambooLeaves.NONE).setValue(STAGE, 0);
            } else {
                return MNDBlocks.BULLET_PEPPER.get().defaultBlockState();
            }
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, LEAVES, STAGE, LIT, PRESSURE);
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return false;
    }

    protected boolean isRandomlyTicking(BlockState state) {
        return state.getValue(STAGE) == 0 || (state.getValue(AGE) < MAX_AGE && state.getValue(AGE) > 0);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.isClientSide || !state.is(this)) return;
        ensureBulletPepperAtTop(level, pos);

        int pressure = state.getValue(MNDBlockStateProperties.PRESSURE);
        boolean isLit = state.getValue(BlockStateProperties.LIT);

        if (!state.canSurvive(level, pos)) {
            if (isLit) explodeAndReset(level, pos, state);
            level.destroyBlock(pos, true);
            return;
        }

        if (pressure > 0) {
            BlockPos abovePos = pos.above();
            BlockState aboveState = level.getBlockState(abovePos);

            if (aboveState.hasProperty(MNDBlockStateProperties.PRESSURE)) {
                int abovePressure = aboveState.getValue(MNDBlockStateProperties.PRESSURE);

                if (abovePressure < pressure) {
                    level.setBlock(abovePos, aboveState.setValue(MNDBlockStateProperties.PRESSURE, pressure), 3);
                    level.scheduleTick(abovePos, aboveState.getBlock(), 1);
                }
            }
        }

        if (pressure == 2 && isLit) {
            explodeAndReset(level, pos, state);
            return;
        }

        if (pressure > 0) {
            level.scheduleTick(pos, this, 20);
            level.setBlock(pos, state.setValue(MNDBlockStateProperties.PRESSURE, pressure - 1), 2);
        }
    }

    protected void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!state.is(this)) return;

        BlockState blockBelow = world.getBlockState(pos.below());
        int i = state.getValue(AGE);
        if (i < MAX_AGE && blockBelow.is(MNDTags.POWDERY_CANNON_PLANTABLE_ON)
                && CommonHooks.canCropGrow(world, pos, state, random.nextInt(5) == 0)) {
            int newAge = i + 1;
            BlockState blockstate = state.setValue(AGE, newAge);
            if (newAge == MAX_AGE) {
                blockstate = blockstate.setValue(LIT, true);
            }
            world.setBlock(pos, blockstate, 2);
            CommonHooks.fireCropGrowPost(world, pos, blockstate);
            world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(blockstate));
        }

        int maxHeight = getSkyAccessMaxHeight(world, pos);
        int heightAboveCane = this.getHeightAboveUpToMax(world, pos, world.getMaxBuildHeight());
        int heightBelowCane = this.getHeightBelowUpToMax(world, pos, world.getMaxBuildHeight());
        int totalCaneHeight = heightAboveCane + heightBelowCane + 1;

        BlockPos topCanePos = pos.above(heightAboveCane);
        BlockState stateOfTopCane = world.getBlockState(topCanePos);

        if (state.getValue(STAGE) == 0 && stateOfTopCane.is(MNDBlocks.POWDERY_CANE.get()) && pos.equals(topCanePos)) {
            int blocksToDisplace = calculateBlocksToDisplace(world, topCanePos.above());

            boolean canGrowThisStep = (totalCaneHeight < maxHeight) &&
                    (blocksToDisplace != Integer.MAX_VALUE) &&
                    (topCanePos.above().getY() + blocksToDisplace < world.getMaxBuildHeight());

            if (canGrowThisStep && CommonHooks.canCropGrow(world, pos, state, random.nextInt(2) == 0)) {
                this.growCannon(stateOfTopCane, world, topCanePos, random, totalCaneHeight);
                CommonHooks.fireCropGrowPost(world, topCanePos, stateOfTopCane);
            }
        }
        world.scheduleTick(pos, this, 1);
    }

    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity livingEntity && entity.getType() != EntityType.PANDA && entity.getType() != EntityType.BEE && !livingEntity.isCrouching()) {
            entity.makeStuckInBlock(state, new Vec3(0.8F, 0.75F, 0.8F));
            entity.hurt(level.damageSources().cactus(), 1.0F);

            if (!level.isClientSide) {
                int currentPressure = state.getValue(PRESSURE);
                if (currentPressure < 2) {
                    level.setBlock(pos, state.setValue(PRESSURE, currentPressure + 1), 2);
                }
                level.scheduleTick(pos, this, 1);

                if (state.getValue(LIT)) {
                    explodeAndReset(level, pos, state);
                }
            }
        }
    }

    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        int i = state.getValue(AGE);
        boolean flag = i == MAX_AGE;
        if (i > 1 && state.getValue(LIT)) {
            ItemStack heldItem = player.getItemInHand(hand);
            if (ItemUtils.isKnife(heldItem) || heldItem.is(net.neoforged.neoforge.common.Tags.Items.TOOLS_SHEAR)) {
                int j = 1 + level.random.nextInt(2);
                popResource(level, pos, new ItemStack(MNDItems.BULLET_PEPPER.get(), j + (flag ? 1 : 0)));
                level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
                BlockState blockstate = state.setValue(LIT, false).setValue(AGE, 0).setValue(PRESSURE, 0);
                level.setBlock(pos, blockstate, 2);
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, blockstate));
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return !flag && stack.is(Items.BONE_MEAL) ? ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION : super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide && state.getValue(LIT)) {
            explodeAndReset(level, pos, state);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && state.getValue(PRESSURE) < 2 && !player.isCrouching()) {
            level.setBlock(pos, state.setValue(PRESSURE, state.getValue(PRESSURE) + 1), 2);
        }

        if (state.getValue(LIT)) {
            ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
            if (!ItemUtils.isKnife(heldItem) && !heldItem.is(Tags.Items.TOOLS_SHEAR)) {
                explodeAndReset(level, pos, state);
                return Blocks.AIR.defaultBlockState();
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        int maxHeight = getSkyAccessMaxHeight(level, pos);
        int heightAboveCane = this.getHeightAboveUpToMax(level, pos, level.getMaxBuildHeight());
        int heightBelowCane = this.getHeightBelowUpToMax(level, pos, level.getMaxBuildHeight());
        int totalCaneHeight = heightAboveCane + heightBelowCane + 1;

        BlockPos topCanePos = pos.above(heightAboveCane);
        BlockState actualTopCaneState = level.getBlockState(topCanePos);

        if (!actualTopCaneState.is(MNDBlocks.POWDERY_CANE.get()) || actualTopCaneState.getValue(STAGE) == 1) {
            return false;
        }

        BlockPos potentialNewCanePos = topCanePos.above();
        int blocksToDisplace = calculateBlocksToDisplace(level, potentialNewCanePos);

        return (totalCaneHeight < maxHeight) &&
                (blocksToDisplace != Integer.MAX_VALUE) &&
                (potentialNewCanePos.getY() + blocksToDisplace < level.getMaxBuildHeight());
    }

    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int currentBlockAge = state.getValue(AGE);
        if (currentBlockAge < MAX_AGE && currentBlockAge > 0) {
            int newAge = currentBlockAge + 1;
            BlockState newBlockState = state.setValue(AGE, newAge);
            if (newAge == MAX_AGE) {
                newBlockState = newBlockState.setValue(LIT, true);
            }
            level.setBlock(pos, newBlockState, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(newBlockState));
        } else {
            int maxHeight = getSkyAccessMaxHeight(level, pos);
            int segmentsToGrow = 1 + random.nextInt(2);

            for (int i1 = 0; i1 < segmentsToGrow; ++i1) {
                int heightAboveCane = this.getHeightAboveUpToMax(level, pos, level.getMaxBuildHeight());
                int heightBelowCane = this.getHeightBelowUpToMax(level, pos, level.getMaxBuildHeight());
                int totalCaneHeight = heightAboveCane + heightBelowCane + 1;

                BlockPos topCanePos = pos.above(heightAboveCane);
                BlockState actualTopCaneState = level.getBlockState(topCanePos);

                if (!actualTopCaneState.is(MNDBlocks.POWDERY_CANE.get()) || actualTopCaneState.getValue(STAGE) == 1) {
                    return;
                }

                BlockPos potentialNewCanePos = topCanePos.above();
                int blocksToDisplace = calculateBlocksToDisplace(level, potentialNewCanePos);

                boolean canGrowThisStep = (totalCaneHeight < maxHeight) &&
                        (blocksToDisplace != Integer.MAX_VALUE) &&
                        (potentialNewCanePos.getY() + blocksToDisplace < level.getMaxBuildHeight());

                if (!canGrowThisStep) {
                    return;
                }

                this.growCannon(actualTopCaneState, level, topCanePos, random, totalCaneHeight);
            }
        }
    }

    private void ensureBulletPepperAtTop(Level level, BlockPos pos) {
        int heightAbove = this.getHeightAboveUpToMax(level, pos, level.getMaxBuildHeight());
        BlockPos topPos = pos.above(heightAbove);
        BlockPos pepperPos = topPos.above();
        BlockState pepperState = level.getBlockState(pepperPos);

        if (!pepperState.is(MNDBlocks.BULLET_PEPPER.get())) {
            if (level.isEmptyBlock(pepperPos) || pepperState.canBeReplaced()) {
                level.setBlock(pepperPos, MNDBlocks.BULLET_PEPPER.get().defaultBlockState(), 3);
            }
        }
    }

    private int calculateBlocksToDisplace(LevelReader level, BlockPos startPos) {
        int blocksToDisplace = 0;
        BlockPos currentCheckPos = startPos;
        while (currentCheckPos.getY() < level.getMaxBuildHeight()) {
            BlockState currentCheckState = level.getBlockState(currentCheckPos);
            if (currentCheckState.isAir()) {
                break;
            } else if (currentCheckState.is(MNDBlocks.BULLET_PEPPER.get()) || currentCheckState.is(MNDBlocks.POWDERY_CANE.get())) {
                blocksToDisplace++;
                currentCheckPos = currentCheckPos.above();
            } else {
                return Integer.MAX_VALUE;
            }
        }
        return blocksToDisplace;
    }

    private void explodeAndReset(Level level, BlockPos pos, BlockState state) {
        if (!level.isClientSide && state.getValue(LIT)) {
            level.playSound(null, pos, SoundEvents.CREEPER_PRIMED, SoundSource.BLOCKS, 0.5F, 0.25F);
            level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.75F, false, Level.ExplosionInteraction.NONE);
            level.setBlock(pos, state.setValue(LIT, false).setValue(AGE, 0).setValue(PRESSURE, 0), 2);
        }
    }

    protected void growCannon(BlockState stateOfBottomCane, Level level, BlockPos posOfBottomCane, RandomSource random, int currentTotalHeight) {
        int maxHeight = getSkyAccessMaxHeight(level, posOfBottomCane);
        int newAge = 0;
        int newStage;

        if (currentTotalHeight + 1 >= maxHeight) {
            newStage = 1;
        } else if (currentTotalHeight + 1 >= maxHeight - 5 && random.nextFloat() < 0.25F) {
            newStage = 1;
        } else {
            newStage = 0;
        }

        BlockPos positionToPlaceNewBlock = posOfBottomCane.above();

        if ((stateOfBottomCane.getValue(LEAVES) != BambooLeaves.NONE || stateOfBottomCane.getValue(STAGE) == 1) && level.isEmptyBlock(positionToPlaceNewBlock)) {
            level.setBlock(positionToPlaceNewBlock, MNDBlocks.BULLET_PEPPER.get().defaultBlockState(), 3);
            return;
        }

        BlockPos currentScanPos = positionToPlaceNewBlock;
        BlockState currentScanState = level.getBlockState(currentScanPos);

        while (currentScanPos.getY() < level.getMaxBuildHeight() &&
                (currentScanState.is(MNDBlocks.POWDERY_CANE.get()) ||
                        currentScanState.is(MNDBlocks.BULLET_PEPPER.get()))) {
            currentScanPos = currentScanPos.above();
            currentScanState = level.getBlockState(currentScanPos);
        }

        BlockPos blockToMoveUp = currentScanPos.below();
        while (blockToMoveUp != null && blockToMoveUp.compareTo(positionToPlaceNewBlock) >= 0) {
            BlockState stateToDisplace = level.getBlockState(blockToMoveUp);
            if (!stateToDisplace.isAir()) {
                level.setBlock(blockToMoveUp.above(), stateToDisplace, 3);
            }
            blockToMoveUp = blockToMoveUp.below();
        }

        BlockState newCaneSegmentState = this.defaultBlockState()
                .setValue(AGE, newAge)
                .setValue(LEAVES, BambooLeaves.NONE)
                .setValue(STAGE, newStage)
                .setValue(LIT, false)
                .setValue(PRESSURE, 0);

        level.setBlock(positionToPlaceNewBlock, newCaneSegmentState, 3);

        int newHeightAbove = this.getHeightAboveUpToMax(level, positionToPlaceNewBlock, level.getMaxBuildHeight());

        for (int i = newHeightAbove; i >= 0; --i) {
            BlockPos currentCanePos = positionToPlaceNewBlock.above(i);
            BlockState currentCaneState = level.getBlockState(currentCanePos);

            if (currentCaneState.is(MNDBlocks.POWDERY_CANE.get())) {
                if (i == newHeightAbove) {
                    if (currentCaneState.getValue(LEAVES) != BambooLeaves.SMALL) {
                        level.setBlock(currentCanePos, currentCaneState.setValue(LEAVES, BambooLeaves.SMALL), 3);
                    }
                } else {
                    if (currentCaneState.getValue(LEAVES) != BambooLeaves.NONE) {
                        level.setBlock(currentCanePos, currentCaneState.setValue(LEAVES, BambooLeaves.NONE), 3);
                    }
                }
            }
        }

        BlockState bottomCaneState = level.getBlockState(posOfBottomCane);
        if (bottomCaneState.is(MNDBlocks.POWDERY_CANE.get()) && bottomCaneState.getValue(LEAVES) != BambooLeaves.NONE) {
            level.setBlock(posOfBottomCane, bottomCaneState.setValue(LEAVES, BambooLeaves.NONE), 3);
        }
    }

    private int getSkyAccessMaxHeight(LevelReader level, BlockPos pos) {
        if (level.canSeeSky(pos)) {
            return 5;
        }
        return 3;
    }

    protected int getHeightAboveUpToMax(BlockGetter level, BlockPos pos, int globalMaxHeight) {
        int i;
        for (i = 0; i < globalMaxHeight && level.getBlockState(pos.above(i + 1)).is(MNDBlocks.POWDERY_CANE.get()); ++i) {
        }
        return i;
    }

    protected int getHeightBelowUpToMax(BlockGetter level, BlockPos pos, int globalMaxHeight) {
        int i;
        for (i = 0; i < globalMaxHeight && level.getBlockState(pos.below(i + 1)).is(MNDBlocks.POWDERY_CANE.get()); ++i) {
        }
        return i;
    }
}
