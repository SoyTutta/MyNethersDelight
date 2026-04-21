package com.soytutta.mynethersdelight.common.block.crops;

import com.mojang.serialization.MapCodec;
import com.soytutta.mynethersdelight.common.block.utility.MNDBlockStateProperties;
import com.soytutta.mynethersdelight.common.tag.MNDTags;
import com.soytutta.mynethersdelight.common.registry.MNDBlocks;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.util.TriState;
import vectorwing.farmersdelight.common.tag.CommonTags;

import static com.soytutta.mynethersdelight.common.block.utility.MNDBlockStateProperties.PRESSURE;

public class PowderyCannonBlock extends BambooStalkBlock {

    public static final MapCodec<PowderyCannonBlock> CODEC = simpleCodec(PowderyCannonBlock::new);
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public PowderyCannonBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(BambooStalkBlock.AGE, 0)
                .setValue(BambooStalkBlock.LEAVES, BambooLeaves.NONE)
                .setValue(BambooStalkBlock.STAGE, 0)
                .setValue(LIT, false)
                .setValue(PRESSURE, 0));
    }

    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return new ItemStack(MNDItems.POWDER_CANNON.get());
    }

    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        TriState soilDecision = level.getBlockState(pos.below()).canSustainPlant(level, pos.below(), Direction.UP, state);
        return !soilDecision.isDefault() ? soilDecision.isTrue() : level.getBlockState(pos.below()).is(MNDTags.POWDERY_CANNON_PLANTABLE_ON);
    }

    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape voxelshape = state.getValue(BambooStalkBlock.LEAVES) == BambooLeaves.LARGE ? BambooStalkBlock.LARGE_SHAPE : BambooStalkBlock.SMALL_SHAPE;
        Vec3 vec3 = state.getOffset(level, pos);
        return voxelshape.move(vec3.x, vec3.y, vec3.z);
    }

    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Vec3 vec3 = state.getOffset(level, pos);
        return BambooStalkBlock.COLLISION_SHAPE.move(vec3.x, vec3.y, vec3.z);
    }

    protected boolean isCollisionShapeFullBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    }

    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (!state.canSurvive(level, pos)) {
            level.scheduleTick(pos, this, 1);
        }

        if (state.getValue(PRESSURE) > 0) {
            level.scheduleTick(pos, this, 1);
        }

        if (direction == Direction.UP && neighborState.is(MNDBlocks.POWDERY_CANNON.get())) {
            if (neighborState.getValue(BambooStalkBlock.AGE) > state.getValue(BambooStalkBlock.AGE)) {
                level.setBlock(pos, state.cycle(BambooStalkBlock.AGE), 2);
            }
        }

        if (state.getValue(BambooStalkBlock.LEAVES) == BambooLeaves.NONE && state.getValue(LIT)) {
            return state.setValue(LIT, false);
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

        if (soilDecision.isDefault() ? !blockstateBelow.is(MNDTags.POWDERY_CANNON_PLANTABLE_ON) : !soilDecision.isTrue()) {
            return null;
        }

        if (blockstateBelow.is(MNDBlocks.POWDERY_CHUBBY_SAPLING.get())) {
            return this.defaultBlockState().setValue(BambooStalkBlock.AGE, 0);
        } else if (blockstateBelow.is(MNDBlocks.POWDERY_CANNON.get())) {
            int age = blockstateBelow.getValue(BambooStalkBlock.AGE) > 0 ? 1 : 0;
            return this.defaultBlockState().setValue(BambooStalkBlock.AGE, age);
        } else {
            BlockState blockstateAbove = context.getLevel().getBlockState(context.getClickedPos().above());
            if (blockstateAbove.is(MNDBlocks.POWDERY_CANNON.get())) {
                return this.defaultBlockState().setValue(BambooStalkBlock.AGE, blockstateAbove.getValue(BambooStalkBlock.AGE));
            } else {
                return MNDBlocks.POWDERY_CHUBBY_SAPLING.get().defaultBlockState();
            }
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIT, PRESSURE);
    }

    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return false;
    }

    protected boolean isRandomlyTicking(BlockState state) {
        return state.getValue(BambooStalkBlock.STAGE) == 0 || (state.getValue(BambooStalkBlock.LEAVES) != BambooLeaves.NONE && !state.getValue(LIT));
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.isClientSide || !state.is(this)) return;

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
                    level.scheduleTick(abovePos, aboveState.getBlock(), 2);
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

    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (world.isClientSide || !state.is(this)) return;

        int maxHeight = getSkyAccessMaxHeight(world, pos);
        int currentCannonHeight = this.getHeightBelowUpToMax(world, pos, maxHeight) + 1;

        boolean isLit = state.getValue(LIT);
        boolean hasLeaves = state.getValue(BambooStalkBlock.LEAVES) != BambooLeaves.NONE;
        boolean isDoneGrowingStage = state.getValue(BambooStalkBlock.STAGE) == 1;

        BlockPos posAboveThisCannon = pos.above();

        if (state.getValue(BambooStalkBlock.STAGE) == 0) {
            int blocksToDisplace = calculateBlocksToDisplace(world, posAboveThisCannon);

            boolean canGrowThisStep = (currentCannonHeight < maxHeight) &&
                    (blocksToDisplace != Integer.MAX_VALUE) &&
                    (posAboveThisCannon.getY() + blocksToDisplace < world.getMaxBuildHeight());

            if (canGrowThisStep && CommonHooks.canCropGrow(world, pos, state, random.nextInt(2) == 0)) {
                this.growCannon(state, world, pos, random, currentCannonHeight);
                CommonHooks.fireCropGrowPost(world, pos, state);
            }
        }

        if (!isLit && hasLeaves && (currentCannonHeight >= maxHeight - 5 || isDoneGrowingStage)) {
            int igniteChance = world.dimension() == Level.NETHER ? 300 : world.getBiome(pos).is(Tags.Biomes.IS_HOT) ? 500 : 900;

            if (random.nextInt(igniteChance) == 0) {
                world.setBlock(pos, state.setValue(LIT, true), 2);
                playCannonSound(world, pos);
            }
        }

        BlockState stateAboveCannon = world.getBlockState(posAboveThisCannon);
        if (isLit && stateAboveCannon.is(MNDBlocks.POWDERY_CANNON.get()) && !stateAboveCannon.getValue(LIT)) {
            world.setBlock(pos, state.setValue(LIT, false), 2);
            playCannonSound(world, pos);
            world.setBlock(posAboveThisCannon, stateAboveCannon.setValue(LIT, true), 2);
            playCannonSound(world, posAboveThisCannon);
        }
        world.scheduleTick(pos, this, 1);
    }

    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (entity instanceof LivingEntity livingEntity && entity.getType() != EntityType.PANDA && entity.getType() != EntityType.BEE && !livingEntity.isCrouching()) {
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

    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        this.stepOn(level, pos, state, entity);
    }

    protected ItemInteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (state.getValue(LIT)) {
            if (heldStack.is(CommonTags.TOOLS_KNIFE) || heldStack.is(Tags.Items.TOOLS_SHEAR)) {
                heldStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                int amount = 3 + level.random.nextInt(6);
                popResource(level, pos, new ItemStack(MNDItems.BULLET_PEPPER.get(), amount));
                level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
                level.setBlock(pos, state.setValue(LIT, Boolean.FALSE), 3);
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
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
            if (!heldItem.is(CommonTags.TOOLS_KNIFE) && !heldItem.is(Tags.Items.TOOLS_SHEAR)) {
                explodeAndReset(level, pos, state);
                return Blocks.AIR.defaultBlockState();
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        int maxHeight = getSkyAccessMaxHeight(level, pos);

        int heightAboveCannons = this.getHeightAboveUpToMax(level, pos, maxHeight);
        int heightBelowCannons = this.getHeightBelowUpToMax(level, pos, maxHeight);
        int totalCannonHeight = heightAboveCannons + heightBelowCannons + 1;

        BlockPos topCannonPos = pos.above(heightAboveCannons);
        BlockState actualTopCannonState = level.getBlockState(topCannonPos);

        if (!actualTopCannonState.is(MNDBlocks.POWDERY_CANNON.get()) || actualTopCannonState.getValue(BambooStalkBlock.STAGE) == 1) {
            return false;
        }

        BlockPos potentialNewCannonPos = topCannonPos.above();
        int blocksToDisplace = calculateBlocksToDisplace(level, potentialNewCannonPos);

        return (totalCannonHeight < maxHeight) &&
                (blocksToDisplace != Integer.MAX_VALUE) &&
                (potentialNewCannonPos.getY() + blocksToDisplace < level.getMaxBuildHeight());
    }

    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int maxHeight = getSkyAccessMaxHeight(level, pos);
        int segmentsToGrow = 1 + random.nextInt(2);

        for (int i1 = 0; i1 < segmentsToGrow; ++i1) {
            int heightAboveCannons = this.getHeightAboveUpToMax(level, pos, maxHeight);
            int heightBelowCannons = this.getHeightBelowUpToMax(level, pos, maxHeight);
            int totalCannonHeight = heightAboveCannons + heightBelowCannons + 1;

            BlockPos topCannonPos = pos.above(heightAboveCannons);
            BlockState actualTopCannonState = level.getBlockState(topCannonPos);

            if (!actualTopCannonState.is(MNDBlocks.POWDERY_CANNON.get()) || actualTopCannonState.getValue(BambooStalkBlock.STAGE) == 1) {
                return;
            }

            BlockPos potentialNewCannonPos = topCannonPos.above();
            int blocksToDisplace = calculateBlocksToDisplace(level, potentialNewCannonPos);

            boolean canGrowThisStep = (totalCannonHeight < maxHeight) &&
                    (blocksToDisplace != Integer.MAX_VALUE) &&
                    (potentialNewCannonPos.getY() + blocksToDisplace < level.getMaxBuildHeight());

            if (!canGrowThisStep) {
                return;
            }

            this.growCannon(actualTopCannonState, level, topCannonPos, random, totalCannonHeight);
        }
    }

    private int getSkyAccessMaxHeight(LevelReader level, BlockPos pos) {
        if (level.canSeeSky(pos)) {
            return 18;
        }
        return 8;
    }

    private int calculateBlocksToDisplace(LevelReader level, BlockPos startPos) {
        int blocksToDisplace = 0;
        BlockPos currentCheckPos = startPos;
        while (currentCheckPos.getY() < level.getMaxBuildHeight()) {
            BlockState currentCheckState = level.getBlockState(currentCheckPos);
            if (currentCheckState.isAir()) {
                break;
            } else if (currentCheckState.is(MNDBlocks.POWDERY_CANE.get()) || currentCheckState.is(MNDBlocks.BULLET_PEPPER.get())) {
                blocksToDisplace++;
                currentCheckPos = currentCheckPos.above();
            } else {
                return Integer.MAX_VALUE;
            }
        }
        return blocksToDisplace;
    }

    private void playCannonSound(Level level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.CROSSBOW_LOADING_MIDDLE.value(), SoundSource.BLOCKS, 0.5F, 0.25F);
    }

    private void explodeAndReset(Level level, BlockPos pos, BlockState state) {
        if (!level.isClientSide && state.getValue(LIT)) {
            level.playSound(null, pos, SoundEvents.CREEPER_PRIMED, SoundSource.BLOCKS, 0.5F, 0.25F);
            level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3.0F, false, Level.ExplosionInteraction.NONE);
            level.setBlock(pos, state.setValue(LIT, false), 2);
        }
    }

    protected void growCannon(BlockState stateOfTopCannon, Level level, BlockPos posOfTopCannon, RandomSource random, int currentTotalHeight) {
        BlockState blockstateBelowTopCannon = level.getBlockState(posOfTopCannon.below());
        BlockPos blockposTwoBelowTopCannon = posOfTopCannon.below(2);
        BlockState blockstateTwoBelowTopCannon = level.getBlockState(blockposTwoBelowTopCannon);

        BambooLeaves leaves = BambooLeaves.NONE;
        int maxHeight = getSkyAccessMaxHeight(level, posOfTopCannon);

        if (currentTotalHeight >= 1) {
            if (blockstateBelowTopCannon.is(MNDBlocks.POWDERY_CANNON.get()) && blockstateBelowTopCannon.getValue(BambooStalkBlock.LEAVES) != BambooLeaves.NONE) {
                leaves = BambooLeaves.LARGE;
                if (blockstateTwoBelowTopCannon.is(MNDBlocks.POWDERY_CANNON.get())) {
                    level.setBlock(posOfTopCannon.below(), blockstateBelowTopCannon.setValue(BambooStalkBlock.LEAVES, BambooLeaves.SMALL), 3);
                    level.setBlock(blockposTwoBelowTopCannon, blockstateTwoBelowTopCannon.setValue(BambooStalkBlock.LEAVES, BambooLeaves.NONE), 3);
                }
            } else {
                leaves = BambooLeaves.SMALL;
            }
        }

        int newAge = (stateOfTopCannon.getValue(BambooStalkBlock.AGE) != 1 && !blockstateTwoBelowTopCannon.is(MNDBlocks.POWDERY_CANNON.get())) ? 0 : 1;

        int newStage;
        if (currentTotalHeight + 1 >= maxHeight) {
            newStage = 1;
        } else if (currentTotalHeight + 1 >= maxHeight - 5 && random.nextFloat() < 0.25F) {
            newStage = 1;
        } else {
            newStage = 0;
        }

        BlockPos positionToPlaceNewBlock = posOfTopCannon.above();

        if ((level.getBlockState(posOfTopCannon).getValue(BambooStalkBlock.LEAVES) != BambooLeaves.NONE || level.getBlockState(posOfTopCannon).getValue(BambooStalkBlock.STAGE) == 1) && level.isEmptyBlock(positionToPlaceNewBlock)) {
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

        BlockState newCannonState = this.defaultBlockState()
                .setValue(BambooStalkBlock.AGE, newAge)
                .setValue(BambooStalkBlock.LEAVES, leaves)
                .setValue(BambooStalkBlock.STAGE, newStage)
                .setValue(LIT, false)
                .setValue(PRESSURE, 0);

        level.setBlock(positionToPlaceNewBlock, newCannonState, 3);
    }

    protected int getHeightAboveUpToMax(BlockGetter level, BlockPos pos, int globalMaxHeight) {
        int i;
        for (i = 0; i < globalMaxHeight && level.getBlockState(pos.above(i + 1)).is(MNDBlocks.POWDERY_CANNON.get()); ++i) {
        }
        return i;
    }

    protected int getHeightBelowUpToMax(BlockGetter level, BlockPos pos, int globalMaxHeight) {
        int i;
        for (i = 0; i < globalMaxHeight && level.getBlockState(pos.below(i + 1)).is(MNDBlocks.POWDERY_CANNON.get()); ++i) {
        }
        return i;
    }
}