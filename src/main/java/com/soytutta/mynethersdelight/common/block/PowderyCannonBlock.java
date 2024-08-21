package com.soytutta.mynethersdelight.common.block;

import com.mojang.serialization.MapCodec;
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
import net.minecraft.world.level.block.BonemealableBlock;
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
import net.neoforged.neoforge.common.util.TriState;
import vectorwing.farmersdelight.common.tag.CommonTags;

import javax.annotation.Nullable;

public class PowderyCannonBlock extends Block implements BonemealableBlock {
    public static final MapCodec<PowderyCannonBlock> CODEC = simpleCodec(PowderyCannonBlock::new);

    protected static final VoxelShape SMALL_SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 16.0, 11.0);
    protected static final VoxelShape LARGE_SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 16.0, 13.0);
    protected static final VoxelShape COLLISION_SHAPE = Block.box(6.5, 0.0, 6.5, 9.5, 16.0, 9.5);
    public static final IntegerProperty AGE = BlockStateProperties.AGE_1;
    public static final EnumProperty<BambooLeaves> LEAVES = BlockStateProperties.BAMBOO_LEAVES;
    public static final IntegerProperty STAGE = BlockStateProperties.STAGE;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final IntegerProperty PRESSURE = IntegerProperty.create("pressure", 0, 2);

    public MapCodec<PowderyCannonBlock> codec() {
        return CODEC;
    }

    public PowderyCannonBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false).setValue(PRESSURE, 0));
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return new ItemStack(MNDItems.POWDER_CANNON.get());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, LEAVES, STAGE, LIT, PRESSURE);
    }

    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape voxelshape = state.getValue(LEAVES) == BambooLeaves.LARGE ? LARGE_SHAPE : SMALL_SHAPE;
        Vec3 vec3 = state.getOffset(level, pos);
        return voxelshape.move(vec3.x, vec3.y, vec3.z);
    }

    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Vec3 vec3 = state.getOffset(level, pos);
        return COLLISION_SHAPE.move(vec3.x, vec3.y, vec3.z);
    }

    protected boolean isCollisionShapeFullBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        if (!fluidstate.isEmpty()) {
            return null;
        } else {
            BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos().below());
            TriState soilDecision = blockstate.canSustainPlant(context.getLevel(), context.getClickedPos().below(), Direction.UP, this.defaultBlockState());
            if (soilDecision.isDefault()) {
                if (!blockstate.is(MNDTags.POWDERY_CANNON_PLANTABLE_ON)) {
                    return null;
                }
            } else if (!soilDecision.isTrue()) {
                return null;
            }

            if (blockstate.is(MNDBlocks.POWDERY_CHUBBY_SAPLING.get())) {
                return this.defaultBlockState().setValue(AGE, 0);
            } else if (blockstate.is(MNDBlocks.POWDERY_CANNON.get())) {
                int i = blockstate.getValue(AGE) > 0 ? 1 : 0;
                return this.defaultBlockState().setValue(AGE, i);
            } else {
                BlockState blockstate1 = context.getLevel().getBlockState(context.getClickedPos().above());
                return blockstate1.is(MNDBlocks.POWDERY_CANNON.get()) ? this.defaultBlockState()
                        .setValue(AGE, blockstate1.getValue(AGE)) : MNDBlocks.POWDERY_CHUBBY_SAPLING.get().defaultBlockState();
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.isClientSide) return;
        BlockPos posAbove = pos.above();
        BlockState stateAbove = level.getBlockState(posAbove);
        int pressure = state.getValue(PRESSURE);
        boolean isLit = state.getValue(LIT);
        if (!state.canSurvive(level, pos)) {
            if (isLit) {
                explodeAndReset(level, pos, state);
            }
            level.destroyBlock(pos, true);
        }
        if (pressure > 0) {
            level.setBlock(pos, state.setValue(PRESSURE, pressure - 1), 2);
        }
        if (stateAbove.hasProperty(PRESSURE) && stateAbove.getValue(PRESSURE) < 2 && (stateAbove.is(MNDTags.POWDERY_CANE) || stateAbove.is(MNDBlocks.BULLET_PEPPER.get()))) {
            level.setBlock(posAbove, stateAbove.setValue(PRESSURE, stateAbove.getValue(PRESSURE) + 1), 2);
        }
        if (pressure == 2 && isLit) {
            explodeAndReset(level, pos, state);
        }
    }

    protected boolean isRandomlyTicking(BlockState state) {
        return state.getValue(STAGE) == 0 || (state.getValue(LEAVES) != BambooLeaves.NONE && !state.getValue(LIT)) ;
    }

    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (world.isClientSide) return;

        BlockPos posAbove = pos.above();
        BlockState stateAbove = world.getBlockState(posAbove);

        int maxHeight = world.dimension() == Level.NETHER ? 8 : 16;
        int height = this.getHeightBelowUpToMax(world, pos) + 1;

        boolean canGrow = state.getValue(STAGE) == 0 && world.isEmptyBlock(posAbove);
        boolean isLit = state.getValue(LIT);
        boolean hasLeaves = state.getValue(LEAVES) != BambooLeaves.NONE;

        if (canGrow && height < maxHeight && CommonHooks.canCropGrow(world, pos, state, random.nextInt(3) == 0)) {
            this.growCannon(state, world, pos, random, height);
            CommonHooks.fireCropGrowPost(world, pos, state);
        }

        if (!isLit && hasLeaves) {
            if ((height == maxHeight || state.getValue(STAGE) > 0) && world.isEmptyBlock(posAbove)) {
                world.setBlock(posAbove, MNDBlocks.BULLET_PEPPER.get().defaultBlockState(), 3);
            }

            if (random.nextInt(world.dimension() == Level.NETHER ? 300 : 900) == 0) {
                world.setBlock(pos, state.setValue(LIT, true), 2);
                world.playSound(null, pos, SoundEvents.CROSSBOW_LOADING_MIDDLE.value(), SoundSource.BLOCKS, 0.5F, 0.25F);
            }
        } else if (isLit && stateAbove.is(MNDBlocks.POWDERY_CANNON.get()) && !stateAbove.getValue(LIT)) {
            if ((height >= maxHeight - 2 || stateAbove.getValue(STAGE) > 0) && world.isEmptyBlock(posAbove.above())) {
                world.setBlock(posAbove.above(), MNDBlocks.BULLET_PEPPER.get().defaultBlockState(), 3);
            }

            world.setBlock(pos, state.setValue(LIT, false), 2);
            world.playSound(null, pos, SoundEvents.CROSSBOW_LOADING_MIDDLE.value(), SoundSource.BLOCKS, 0.5F, 0.25F);
            world.setBlock(posAbove, stateAbove.setValue(LIT, true), 2);
        }
    }

    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        TriState soilDecision = level.getBlockState(pos.below()).canSustainPlant(level, pos.below(), Direction.UP, state);
        return !soilDecision.isDefault() ? soilDecision.isTrue() : level.getBlockState(pos.below()).is(MNDTags.POWDERY_CANNON_PLANTABLE_ON);
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState offsetState, LevelAccessor level, BlockPos pos, BlockPos offsetPos) {
        if (!state.canSurvive(level, pos)) {
            level.scheduleTick(pos, this, 1);
            if (state.getValue(LIT)) {
                explodeAndReset((Level) level, pos, state);
            }
            level.destroyBlock(pos, true);
        }
        if (state.getValue(PRESSURE) > 0) {
            level.scheduleTick(pos, this, 1);
        }
        if (direction == Direction.UP && offsetState.is(MNDBlocks.POWDERY_CANNON.get())) {
            if (offsetState.getValue(AGE) > state.getValue(AGE)) {
                level.setBlock(pos, state.cycle(AGE), 2);
            }
        }
        if (state.getValue(LEAVES) == BambooLeaves.NONE) {
            return state.setValue(LIT, false);
        }

        return super.updateShape(state, direction, offsetState, level, pos, offsetPos);
    }

    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        BlockState stateAbove = level.getBlockState(pos.above());
        if (!stateAbove.isAir() && stateAbove.hasProperty(BlockStateProperties.STAGE)) {
            int stage = stateAbove.getValue(BlockStateProperties.STAGE);
            return stage < 1;
        }
        return false;
    }

    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int i = this.getHeightAboveUpToMax(level, pos);
        int j = this.getHeightBelowUpToMax(level, pos);
        int k = i + j + 1;
        int l = 1 + random.nextInt(2);

        for(int i1 = 0; i1 < l; ++i1) {
            BlockPos blockpos = pos.above(i);
            BlockState blockstate = level.getBlockState(blockpos);
            if (k >= 16 || blockstate.getValue(STAGE) == 1 || !level.isEmptyBlock(blockpos.above())) {
                return;
            }

            this.growCannon(blockstate, level, blockpos, random, k);
            ++i;
            ++k;
        }

    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        int pressure = state.getValue(PRESSURE);

        if ((entity instanceof LivingEntity && entity.getType() != EntityType.PANDA && entity.getType() != EntityType.BEE) && !((LivingEntity) entity).isCrouching()) {
            entity.hurt(level.damageSources().cactus(), 1.0F);
            entity.makeStuckInBlock(state, new Vec3(0.8, 0.75, 0.8));
            if (!level.isClientSide && state.getValue(PRESSURE) < 2) {
                level.setBlock(pos, state.setValue(PRESSURE, pressure + 1), 2);
            }
        }
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        int pressure = state.getValue(PRESSURE);

        if ((entity instanceof LivingEntity && entity.getType() != EntityType.PANDA && entity.getType() != EntityType.BEE) && !((LivingEntity) entity).isCrouching()) {
            entity.hurt(level.damageSources().cactus(), 1.0F);
            entity.makeStuckInBlock(state, new Vec3(0.8, 0.75, 0.8));
            if (!level.isClientSide && state.getValue(PRESSURE) < 2) {
                level.setBlock(pos, state.setValue(PRESSURE, pressure + 1), 2);
            }
        }
    }

    private void explodeAndReset(Level level, BlockPos pos, BlockState state) {
        if (!level.isClientSide && state.hasProperty(LIT) && state.getValue(LIT)) {
        level.playSound(null, pos, SoundEvents.CREEPER_PRIMED, SoundSource.BLOCKS, 0.5F, 0.25F);
        level.explode(null, pos.getX(), pos.getY(), pos.getZ(), 3.0F,  false, Level.ExplosionInteraction.NONE);
        level.setBlock(pos, state.setValue(LIT, false), 2);
        }
    }

    protected void growCannon(BlockState state, Level level, BlockPos pos, RandomSource random, int height) {
        BlockState blockstate = level.getBlockState(pos.below());
        BlockPos blockpos = pos.below(2);
        BlockState blockstate1 = level.getBlockState(blockpos);
        BambooLeaves leaves = BambooLeaves.NONE;
        int maxHeight = (level.dimension() == Level.NETHER) ? 8 : 16;

        if (height >= 1) {
            if (blockstate.is(MNDBlocks.POWDERY_CANNON.get()) && blockstate.getValue(LEAVES) != BambooLeaves.NONE) {
                if (blockstate.is(MNDBlocks.POWDERY_CANNON.get()) && blockstate.getValue(LEAVES) != BambooLeaves.NONE) {
                    leaves = BambooLeaves.LARGE;
                    if (blockstate1.is(MNDBlocks.POWDERY_CANNON.get())) {
                        level.setBlock(pos.below(), blockstate.setValue(LEAVES, BambooLeaves.SMALL), 3);
                        level.setBlock(blockpos, blockstate1.setValue(LEAVES, BambooLeaves.NONE), 3);
                    }
                }
            } else {
                leaves = BambooLeaves.SMALL;
            }
        }

        int i = state.getValue(AGE) != 1 && !blockstate1.is(MNDBlocks.POWDERY_CANNON.get()) ? 0 : 1;
        int MH = 1 + level.random.nextInt(3);
        int j = (height < maxHeight || !(random.nextFloat() < 0.25F)) && height != (maxHeight - MH) ? 0 : 1;
        level.setBlock(pos.above(), this.defaultBlockState().setValue(AGE, i).setValue(LEAVES, leaves).setValue(STAGE, j), 3);
        if ((height >= maxHeight - MH || height == maxHeight) && level.isEmptyBlock(pos.above(2))) {
            level.setBlock(pos.above(2), MNDBlocks.BULLET_PEPPER.get().defaultBlockState(), 3);
        }
    }

    protected int getHeightAboveUpToMax(BlockGetter level, BlockPos pos) {
        int maxHeight = (level instanceof Level && ((Level) level).dimension() == Level.NETHER) ? 8 : 16;
        int i;
        for (i = 0; i < maxHeight && level.getBlockState(pos.above(i + 1)).is(MNDBlocks.POWDERY_CANNON.get()); ++i) {
        }

        return i;
    }

    protected int getHeightBelowUpToMax(BlockGetter level, BlockPos pos) {
        int maxHeight = (level instanceof Level && ((Level) level).dimension() == Level.NETHER) ? 8 : 16;
        int i;
        for (i = 0; i < maxHeight && level.getBlockState(pos.below(i + 1)).is(MNDBlocks.POWDERY_CANNON.get()); ++i) {
        }

        return i;
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && state.getValue(PRESSURE) < 2 && !player.isCrouching()) {
            int pressure = state.getValue(PRESSURE);
            level.setBlock(pos, state.setValue(PRESSURE, pressure + 1), 2);
        }
        if (state.getValue(LIT)) {
            ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
            if (!heldItem.is(CommonTags.TOOLS_KNIFE) || !heldItem.is(net.neoforged.neoforge.common.Tags.Items.TOOLS_SHEAR)) {
                explodeAndReset(level, pos, state);
            }
        }
        super.playerWillDestroy(level, pos, state, player);
        return state;
    }

    @Override
    @SuppressWarnings("deprecation")
    protected ItemInteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (state.getValue(LIT)) {
            ItemStack heldItem = player.getItemInHand(hand);

            if (heldItem.is(CommonTags.TOOLS_KNIFE) || heldItem.is(net.neoforged.neoforge.common.Tags.Items.TOOLS_SHEAR)) {
                heldItem.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                int j = 3 + level.random.nextInt(6);
                popResource(level, pos, new ItemStack(MNDItems.BULLET_PEPPER.get(), j));
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

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return false;
    }
}