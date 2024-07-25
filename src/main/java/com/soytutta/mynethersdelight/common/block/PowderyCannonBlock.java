package com.soytutta.mynethersdelight.common.block;

import com.soytutta.mynethersdelight.common.tag.MNDTags;
import com.soytutta.mynethersdelight.common.registry.MNDBlocks;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.BambooBlock;
import net.minecraft.world.level.block.state.properties.BambooLeaves;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import vectorwing.farmersdelight.common.tag.ForgeTags;

import javax.annotation.Nullable;

public class PowderyCannonBlock extends BambooBlock {
    public static final BooleanProperty LIT = BooleanProperty.create("lit");
    public static final IntegerProperty PRESSURE = IntegerProperty.create("pressure", 0, 2);

    public PowderyCannonBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false).setValue(PRESSURE, 0));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIT,PRESSURE);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        if (!fluidstate.isEmpty()) {
            return null;
        }
        else {
            BlockState state = context.getLevel().getBlockState(context.getClickedPos().below());
            if (state.is(MNDTags.POWDERY_CANNON_PLANTABLE_ON)) {
                if (state.is(MNDBlocks.POWDERY_CHUBBY_SAPLING.get())) {
                    return this.defaultBlockState().setValue(AGE, 0);
                }
                else if (state.is(MNDBlocks.POWDERY_CANNON.get())) {
                    int i = state.getValue(AGE) > 0 ? 1 : 0;
                    return this.defaultBlockState().setValue(AGE, i);
                }
                else {
                    BlockState aboveState = context.getLevel().getBlockState(context.getClickedPos().above());
                    return aboveState.is(MNDBlocks.POWDERY_CANNON.get()) ? this.defaultBlockState().setValue(AGE, aboveState.getValue(AGE)) : MNDBlocks.POWDERY_CHUBBY_SAPLING.get().defaultBlockState();
                }
            }
            else {
                return null;
            }
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return new ItemStack(MNDItems.POWDER_CANNON.get());
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).is(MNDTags.POWDERY_CANNON_PLANTABLE_ON) || level.getBlockState(pos.below()).getMaterial().isSolid();
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState offsetState, LevelAccessor level, BlockPos pos, BlockPos offsetPos) {
        if (!state.canSurvive(level, pos)) {
            level.scheduleTick(pos, this, 1);
            if (state.getValue(LIT)) {
                explodeAndDestroy((Level) level, pos, state);
            }
            level.destroyBlock(pos, true);
        }
        if (state.getValue(PRESSURE) > 0) {
            level.scheduleTick(pos, this, 1);
        }
        if (direction == Direction.UP && offsetState.is(MNDBlocks.POWDERY_CANNON.get()) && offsetState.getValue(AGE) > state.getValue(AGE)) {
            level.setBlock(pos, state.cycle(AGE), 2);
        }
        if (state.getValue(LEAVES) == BambooLeaves.NONE) {
            return state.setValue(LIT, false);
        }
        return super.updateShape(state, direction, offsetState, level, pos, offsetPos);
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
                explodeAndDestroy(level, pos, state);
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
            explodeAndDestroy(level, pos, state);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        BlockPos posAbove = pos.above();
        BlockState stateAbove = world.getBlockState(posAbove);

        if (state.getValue(LEAVES) != BambooLeaves.NONE && !state.getValue(LIT) && random.nextInt(900) != 0) {
            world.setBlock(pos, state.setValue(LIT, true), 2);
            world.playSound(null, pos, SoundEvents.CROSSBOW_LOADING_MIDDLE, SoundSource.BLOCKS, 0.5F, 0.25F);
        }
        else if (state.getValue(LIT) && stateAbove.is(MNDBlocks.POWDERY_CANNON.get()) && !stateAbove.getValue(LIT)) {
            world.setBlock(pos, state.setValue(LIT, false), 2);
            world.playSound(null, pos, SoundEvents.CROSSBOW_LOADING_MIDDLE, SoundSource.BLOCKS, 0.5F, 0.25F);
            world.setBlock(posAbove, stateAbove.setValue(LIT, true), 2);
        }
        if (state.getValue(STAGE) == 0 && world.isEmptyBlock(pos.above()) && world.getRawBrightness(pos.above(), 0) >= 9) {
            int height = this.getHeightBelowUpToMax(world, pos) + 1;
            if (height < 8 && ForgeHooks.onCropsGrowPre(world, pos, state, random.nextInt(3) == 0)) {
                this.growBamboo(state, world, pos, random, height);
                ForgeHooks.onCropsGrowPost(world, pos, state);
            }
        }
        else if (world.getBlockState(posAbove).is(MNDTags.POWDERY_CANE)) {
            if (world.getBlockState(posAbove).hasProperty(STAGE) && world.getBlockState(posAbove).getValue(STAGE) > 0) {
                if (world.isEmptyBlock(posAbove.above())) {
                    world.setBlock(posAbove.above(), MNDBlocks.BULLET_PEPPER.get().defaultBlockState(), 3);
                }
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        int pressure = state.getValue(PRESSURE);

        if ((entity instanceof LivingEntity && entity.getType() != EntityType.PANDA && entity.getType() != EntityType.BEE) && !((LivingEntity) entity).isCrouching()) {
            entity.hurt(DamageSource.CACTUS,1);
            entity.makeStuckInBlock(state, new Vec3(0.8, 0.75, 0.8));
            if (!level.isClientSide && state.getValue(PRESSURE) < 2) {
                level.setBlock(pos, state.setValue(PRESSURE, pressure + 1), 2);
            }
        }
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && state.getValue(PRESSURE) < 2 && !player.isCrouching()) {
            int pressure = state.getValue(PRESSURE);
            level.setBlock(pos, state.setValue(PRESSURE, pressure + 1), 2);
        }
        if (state.getValue(LIT)) {
            ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
            if (!heldItem.is(ForgeTags.TOOLS_KNIVES) || !heldItem.is(net.minecraftforge.common.Tags.Items.SHEARS)) {
                explodeAndDestroy(level, pos, state);
            }
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void wasExploded(Level level, BlockPos pos, Explosion explosion) {
        BlockState state = level.getBlockState(pos);
        if (!level.isClientSide && state.hasProperty(LIT) && state.getValue(LIT)) {
            explodeAndDestroy(level, pos, state);
        }
    }

    private void explodeAndDestroy(Level level, BlockPos pos, BlockState state) {
        level.playSound(null, pos, SoundEvents.CREEPER_PRIMED, SoundSource.BLOCKS, 0.5F, 0.25F);
        for (int i = 1; i <= 5; i++) {
            level.explode(null, pos.getX(), pos.getY() + i, pos.getZ(), 1.0F, Explosion.BlockInteraction.BREAK);
        }
        level.setBlock(pos, state.setValue(LIT, false), 2);
        level.destroyBlock(pos, true);
    }

    @Override
    protected void growBamboo(BlockState state, Level level, BlockPos pos, RandomSource random, int height) {
        BlockState blockstate = level.getBlockState(pos.below());
        BlockPos blockpos = pos.below(2);
        BlockState blockstate1 = level.getBlockState(blockpos);
        BambooLeaves leaves = BambooLeaves.NONE;
        int maxHeight = (level.dimension() == Level.NETHER) ? 16 : 8;

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
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult context) {
        if (state.getValue(LIT)) {
            ItemStack heldItem = player.getItemInHand(hand);

            if (heldItem.is(ForgeTags.TOOLS_KNIVES) ||heldItem.is(net.minecraftforge.common.Tags.Items.SHEARS)) {
                heldItem.hurtAndBreak(1, player, (action) -> { action.broadcastBreakEvent(hand); });
                int j = 3 + level.random.nextInt(6);
                popResource(level, pos, new ItemStack(MNDItems.BULLET_PEPPER.get(), j));
                level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
                level.setBlock(pos, state.setValue(LIT, Boolean.FALSE), 3);
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return super.use(state, level, pos, player, hand, context);
    }

    @Override
    protected int getHeightAboveUpToMax(BlockGetter level, BlockPos pos) {
        int maxHeight = (level instanceof Level && ((Level) level).dimension() == Level.NETHER) ? 16 : 8;
        int i;
        for (i = 0; i < maxHeight && level.getBlockState(pos.above(i + 1)).is(MNDBlocks.POWDERY_CANNON.get()); ++i) {
        }

        return i;
    }

    @Override
    protected int getHeightBelowUpToMax(BlockGetter level, BlockPos pos) {
        int maxHeight = (level instanceof Level && ((Level) level).dimension() == Level.NETHER) ? 16 : 8;
        int i;
        for (i = 0; i < maxHeight && level.getBlockState(pos.below(i + 1)).is(MNDBlocks.POWDERY_CANNON.get()); ++i) {
        }

        return i;
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return false;
    }
}