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
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.Tags;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PowderyCaneBlock extends BushBlock implements IPlantable, BonemealableBlock {
    public static final int MAX_AGE = 3;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final EnumProperty<BambooLeaves> LEAVES = BlockStateProperties.BAMBOO_LEAVES;
    public static final IntegerProperty STAGE = BlockStateProperties.STAGE;
    public static final IntegerProperty PRESSURE = IntegerProperty.create("pressure", 0, 2);
    public static final BooleanProperty BASE = BooleanProperty.create("base");
    public static final BooleanProperty LEAVE = BooleanProperty.create("leave");
    private static final VoxelShape SHAPE = Block.box(6.5, 0.0, 6.5, 10.5, 16.0, 10.5);

    public PowderyCaneBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(AGE, 0)
                .setValue(LIT, false)
                .setValue(PRESSURE, 0)
                .setValue(LEAVES, BambooLeaves.NONE)
                .setValue(STAGE, 0)
                .setValue(BASE, false)
                .setValue(LEAVE, false));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState blockBelow = level.getBlockState(pos.below());
        return blockBelow.is(MNDTags.POWDERY_CANNON_PLANTABLE_ON)
                || blockBelow.is(MNDBlocks.POWDERY_CANE.get())
                || blockBelow.canSustainPlant(level, pos.below(), Direction.UP, this);
    }

    @Override
    @SuppressWarnings("deprecation")
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(state.getValue(AGE) == 0
                ? MNDItems.BULLET_PEPPER.get() : MNDItems.POWDER_CANNON.get());
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Vec3 offset = state.getOffset(level, pos);
        return SHAPE.move(offset.x, offset.y, offset.z);
    }

    @Override
    public BlockPathTypes getBlockPathType(BlockState state, BlockGetter level,
                                           BlockPos pos, @Nullable Mob mob) {
        return BlockPathTypes.DAMAGE_OTHER;
    }

    @Override
    public BlockPathTypes getAdjacentBlockPathType(BlockState state, BlockGetter level,
                                                   BlockPos pos, @Nullable Mob mob,
                                                   BlockPathTypes originalType) {
        return BlockPathTypes.DANGER_OTHER;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (!state.canSurvive(level, pos)) {
            level.scheduleTick(pos, this, 2);
            return state;
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
        boolean canSustain = blockBelow.is(MNDTags.POWDERY_CANNON_PLANTABLE_ON)
                || blockBelow.is(MNDBlocks.POWDERY_CANE.get())
                || blockBelow.canSustainPlant(context.getLevel(), belowPos, Direction.UP, this);
        if (!canSustain) {
            return null;
        }

        if (blockBelow.is(MNDTags.POWDERY_CANNON_PLANTABLE_ON)
                || !blockBelow.is(MNDBlocks.POWDERY_CANE.get())) {
            return defaultBlockState().setValue(AGE, 1);
        }
        return defaultBlockState();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, LEAVES, STAGE, LIT, PRESSURE, BASE, LEAVE);
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return false;
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(STAGE) == 0
                || state.getValue(AGE) < MAX_AGE && state.getValue(AGE) > 0;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.is(this)) {
            return;
        }
        ensureBulletPepperAtTop(level, pos);

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
                level.scheduleTick(abovePos, aboveState.getBlock(), 1);
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

        BlockState blockBelow = level.getBlockState(pos.below());
        int age = state.getValue(AGE);
        if (age < MAX_AGE && blockBelow.is(MNDTags.POWDERY_CANNON_PLANTABLE_ON)
                && ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt(5) == 0)) {
            int newAge = age + 1;
            BlockState newState = state.setValue(AGE, newAge);
            if (newAge == MAX_AGE) {
                newState = newState.setValue(LIT, true);
            }
            level.setBlock(pos, newState, 2);
            ForgeHooks.onCropsGrowPost(level, pos, newState);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(newState));
        }

        int maxHeight = getSkyAccessMaxHeight(level, pos);
        int heightAbove = getHeightAboveUpToMax(level, pos, level.getMaxBuildHeight());
        int heightBelow = getHeightBelowUpToMax(level, pos, level.getMaxBuildHeight());
        int totalHeight = heightAbove + heightBelow + 1;
        BlockPos topPos = pos.above(heightAbove);
        BlockState topState = level.getBlockState(topPos);

        if (state.getValue(STAGE) == 0 && topState.is(MNDBlocks.POWDERY_CANE.get())
                && pos.equals(topPos)) {
            int blocksToDisplace = calculateBlocksToDisplace(level, topPos.above());
            boolean canGrow = totalHeight < maxHeight
                    && blocksToDisplace != Integer.MAX_VALUE
                    && topPos.above().getY() + blocksToDisplace < level.getMaxBuildHeight();
            if (canGrow && ForgeHooks.onCropsGrowPre(
                    level, pos, state, random.nextInt(2) == 0)) {
                growCannon(topState, level, topPos, random, totalHeight);
                ForgeHooks.onCropsGrowPost(level, topPos, topState);
            }
        }
        level.scheduleTick(pos, this, 1);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)
                || entity.getType() == EntityType.PANDA
                || entity.getType() == EntityType.BEE
                || livingEntity.isCrouching()) {
            return;
        }

        entity.makeStuckInBlock(state, new Vec3(0.8F, 0.75F, 0.8F));
        entity.hurt(level.damageSources().cactus(), 1.0F);
        if (!level.isClientSide) {
            int pressure = state.getValue(PRESSURE);
            if (pressure < 2) {
                level.setBlock(pos, state.setValue(PRESSURE, pressure + 1), 2);
            }
            level.scheduleTick(pos, this, 1);
            if (state.getValue(LIT)) {
                explodeAndReset(level, pos, state);
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hitResult) {
        ItemStack heldItem = player.getItemInHand(hand);
        int age = state.getValue(AGE);
        if (age > 1 && state.getValue(LIT)
                && (ItemUtils.isKnife(heldItem) || heldItem.is(Tags.Items.SHEARS))) {
            int amount = 1 + level.random.nextInt(2) + (age == MAX_AGE ? 1 : 0);
            popResource(level, pos, new ItemStack(MNDItems.BULLET_PEPPER.get(), amount));
            level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES,
                    SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
            BlockState newState = state.setValue(LIT, false).setValue(AGE, 0).setValue(PRESSURE, 0);
            level.setBlock(pos, newState, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, newState));
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
    public boolean isPathfindable(@Nonnull BlockState state, @Nonnull BlockGetter level,
                                  @Nonnull BlockPos pos, @Nonnull PathComputationType pathType) {
        return false;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos,
                                         BlockState state, boolean isClientSide) {
        int maxHeight = getSkyAccessMaxHeight(level, pos);
        int heightAbove = getHeightAboveUpToMax(level, pos, level.getMaxBuildHeight());
        int heightBelow = getHeightBelowUpToMax(level, pos, level.getMaxBuildHeight());
        int totalHeight = heightAbove + heightBelow + 1;
        BlockPos topPos = pos.above(heightAbove);
        BlockState topState = level.getBlockState(topPos);
        if (!topState.is(MNDBlocks.POWDERY_CANE.get()) || topState.getValue(STAGE) == 1) {
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
        int age = state.getValue(AGE);
        if (age < MAX_AGE && age > 0) {
            int newAge = age + 1;
            BlockState newState = state.setValue(AGE, newAge);
            if (newAge == MAX_AGE) {
                newState = newState.setValue(LIT, true);
            }
            level.setBlock(pos, newState, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(newState));
            return;
        }

        int maxHeight = getSkyAccessMaxHeight(level, pos);
        int segmentsToGrow = 1 + random.nextInt(2);
        for (int i = 0; i < segmentsToGrow; i++) {
            int heightAbove = getHeightAboveUpToMax(level, pos, level.getMaxBuildHeight());
            int heightBelow = getHeightBelowUpToMax(level, pos, level.getMaxBuildHeight());
            int totalHeight = heightAbove + heightBelow + 1;
            BlockPos topPos = pos.above(heightAbove);
            BlockState topState = level.getBlockState(topPos);
            if (!topState.is(MNDBlocks.POWDERY_CANE.get()) || topState.getValue(STAGE) == 1) {
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

    private void ensureBulletPepperAtTop(Level level, BlockPos pos) {
        int heightAbove = getHeightAboveUpToMax(level, pos, level.getMaxBuildHeight());
        BlockPos pepperPos = pos.above(heightAbove + 1);
        BlockState pepperState = level.getBlockState(pepperPos);
        if (!pepperState.is(MNDBlocks.BULLET_PEPPER.get())
                && (level.isEmptyBlock(pepperPos) || pepperState.canBeReplaced())) {
            level.setBlock(pepperPos, MNDBlocks.BULLET_PEPPER.get().defaultBlockState(), 3);
        }
    }

    private int calculateBlocksToDisplace(LevelReader level, BlockPos startPos) {
        int blocksToDisplace = 0;
        BlockPos checkPos = startPos;
        while (checkPos.getY() < level.getMaxBuildHeight()) {
            BlockState checkState = level.getBlockState(checkPos);
            if (checkState.isAir()) {
                break;
            }
            if (checkState.is(MNDBlocks.BULLET_PEPPER.get())
                    || checkState.is(MNDBlocks.POWDERY_CANE.get())) {
                blocksToDisplace++;
                checkPos = checkPos.above();
            } else {
                return Integer.MAX_VALUE;
            }
        }
        return blocksToDisplace;
    }

    private void explodeAndReset(Level level, BlockPos pos, BlockState state) {
        if (!level.isClientSide && state.getValue(LIT)) {
            level.playSound(null, pos, SoundEvents.CREEPER_PRIMED,
                    SoundSource.BLOCKS, 0.5F, 0.25F);
            level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    0.75F, false, Level.ExplosionInteraction.NONE);
            level.setBlock(pos, state.setValue(LIT, false)
                    .setValue(AGE, 0).setValue(PRESSURE, 0), 2);
        }
    }

    protected void growCannon(BlockState bottomState, Level level, BlockPos bottomPos,
                              RandomSource random, int currentHeight) {
        int maxHeight = getSkyAccessMaxHeight(level, bottomPos);
        int newStage = currentHeight + 1 >= maxHeight
                || currentHeight + 1 >= maxHeight - 5 && random.nextFloat() < 0.25F ? 1 : 0;
        BlockPos newPos = bottomPos.above();

        if ((bottomState.getValue(LEAVES) != BambooLeaves.NONE
                || bottomState.getValue(STAGE) == 1) && level.isEmptyBlock(newPos)) {
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

        level.setBlock(newPos, defaultBlockState().setValue(STAGE, newStage), 3);
        int newHeightAbove = getHeightAboveUpToMax(
                level, newPos, level.getMaxBuildHeight());
        for (int i = newHeightAbove; i >= 0; i--) {
            BlockPos canePos = newPos.above(i);
            BlockState caneState = level.getBlockState(canePos);
            if (caneState.is(MNDBlocks.POWDERY_CANE.get())) {
                BambooLeaves leaves = i == newHeightAbove ? BambooLeaves.SMALL : BambooLeaves.NONE;
                if (caneState.getValue(LEAVES) != leaves) {
                    level.setBlock(canePos, caneState.setValue(LEAVES, leaves), 3);
                }
            }
        }

        BlockState bottomCaneState = level.getBlockState(bottomPos);
        if (bottomCaneState.is(MNDBlocks.POWDERY_CANE.get())
                && bottomCaneState.getValue(LEAVES) != BambooLeaves.NONE) {
            level.setBlock(bottomPos,
                    bottomCaneState.setValue(LEAVES, BambooLeaves.NONE), 3);
        }
    }

    private int getSkyAccessMaxHeight(LevelReader level, BlockPos pos) {
        return level.canSeeSky(pos) ? 5 : 3;
    }

    protected int getHeightAboveUpToMax(BlockGetter level, BlockPos pos, int maxHeight) {
        int height;
        for (height = 0; height < maxHeight
                && level.getBlockState(pos.above(height + 1))
                .is(MNDBlocks.POWDERY_CANE.get()); height++) {
        }
        return height;
    }

    protected int getHeightBelowUpToMax(BlockGetter level, BlockPos pos, int maxHeight) {
        int height;
        for (height = 0; height < maxHeight
                && level.getBlockState(pos.below(height + 1))
                .is(MNDBlocks.POWDERY_CANE.get()); height++) {
        }
        return height;
    }
}
