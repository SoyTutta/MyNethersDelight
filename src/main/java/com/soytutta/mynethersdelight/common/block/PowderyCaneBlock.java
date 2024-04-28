package com.soytutta.mynethersdelight.common.block;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import vectorwing.farmersdelight.common.tag.ForgeTags;

import java.util.Random;

public class PowderyCaneBlock extends BushBlock implements IPlantable, BonemealableBlock {
    public static final BooleanProperty LIT = BooleanProperty.create("lit");
    public static final BooleanProperty BASE = BooleanProperty.create("base");
    public static final BooleanProperty LEAVE = BooleanProperty.create("leave");
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 2);
    public static final IntegerProperty PRESSURE = IntegerProperty.create("pressure", 0, 2);
    private static final VoxelShape SHAPE = Block.box(6.5, 0.0, 6.5, 10.5, 16.0, 10.5);

    public PowderyCaneBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false).setValue(LEAVE, false).setValue(BASE, false).setValue(PRESSURE, 0).setValue(AGE, 0));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIT, BASE, LEAVE, AGE, PRESSURE);
    }

    @Override
    @SuppressWarnings("deprecation")
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(MNDItems.BULLET_PEPPER.get());
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        Vec3 vec3 = state.getOffset(worldIn, pos);
        return SHAPE.move(vec3.x, vec3.y, vec3.z);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState blockBelow = level.getBlockState(pos.below());
        return blockBelow.is(MNDTags.POWDERY_CANNON_PLANTABLE_ON) || blockBelow.is(MNDTags.POWDERY_CANE);
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState offsetState, LevelAccessor level, BlockPos pos, BlockPos offsetPos) {
        if (state.getValue(PRESSURE) > 0) {
            level.scheduleTick(pos, this, 1);
        }
        BlockState blockAbove = level.getBlockState(pos.above());
        BlockState blockBelow = level.getBlockState(pos.below());

        if (blockBelow.is(MNDTags.POWDERY_CANNON_PLANTABLE_ON)) {
            state = state.setValue(BASE, true);
        }
        else if (!blockAbove.is(MNDBlocks.BULLET_PEPPER.get()) && !blockAbove.is(Blocks.AIR)) {
            state = state.setValue(LEAVE, false);
        }
        else if (blockBelow.is(MNDBlocks.POWDERY_CANE.get()) && !state.getValue(BASE) && !state.getValue(LEAVE)) {
            state = state.setValue(LEAVE, new Random().nextInt(100) < 85);
        }
        return super.updateShape(state, direction, offsetState, level, pos, offsetPos);
    }


    @Override
    @SuppressWarnings("deprecation")
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.isClientSide) return;
        BlockPos posAbove = pos.above();
        BlockState stateAbove = level.getBlockState(posAbove);
        int age = state.getValue(AGE);
        int pressure = state.getValue(PRESSURE);
        boolean isLit = state.getValue(LIT);
        if (!state.canSurvive(level, pos)) {
            if (isLit) {
                explodeAndReset(level, pos, state, age);
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
            explodeAndReset(level, pos, state, age);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        int age = state.getValue(AGE);
        BlockState blockBelow = world.getBlockState(pos.below());

        if (age < 2 && ForgeHooks.onCropsGrowPre(world, pos, state, random.nextInt(3) == 0) && (blockBelow.is(MNDBlocks.RESURGENT_SOIL.get()) || blockBelow.is(MNDBlocks.POWDERY_CANNON.get()))) {
            world.setBlock(pos, state.setValue(AGE, age + 1), 2);
            ForgeHooks.onCropsGrowPost(world, pos, state);
        }
        else if (world.isEmptyBlock(pos.above())) {
            world.setBlockAndUpdate(pos.above(), MNDBlocks.BULLET_PEPPER.get().defaultBlockState());
        }
        else if (age == 2) {
            world.setBlock(pos, state.setValue(LIT, true), 2);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!(entity instanceof LivingEntity) || entity.getType() == EntityType.PANDA || entity.getType() == EntityType.BEE || entity.isCrouching())
            return;
        entity.hurt(DamageSource.CACTUS, 1);
        entity.makeStuckInBlock(state, new Vec3(0.8, 0.75, 0.8));
        if (!level.isClientSide && state.getValue(PRESSURE) < 2) {
            level.setBlock(pos, state.setValue(PRESSURE, state.getValue(PRESSURE) + 1), 2);
        }
        if (state.getValue(LIT)) {
            plantPepper(level, pos);
        }
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && state.getValue(PRESSURE) < 2 && !player.isCrouching()) {
            level.setBlock(pos, state.setValue(PRESSURE, state.getValue(PRESSURE) + 1), 2);
        }
        if (state.getValue(LIT)) {
            ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
            if (!heldItem.is(ForgeTags.TOOLS_KNIVES) || !heldItem.is(net.minecraftforge.common.Tags.Items.SHEARS)) {
                int age = state.hasProperty(AGE) ? state.getValue(AGE) : 0;
                explodeAndReset(level, pos, state, age);
            }
        }
        super.playerWillDestroy(level, pos, state, player);
    }


    @Override
    public void wasExploded(Level level, BlockPos pos, Explosion explosion) {
        BlockState state = level.getBlockState(pos);
        if (!level.isClientSide && state.hasProperty(LIT) && state.getValue(LIT)) {
            int age = state.hasProperty(AGE) ? state.getValue(AGE) : 0;
            explodeAndReset(level, pos, state, age);
        }
    }

    private void plantPepper(Level level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.relative(direction);
            BlockState neighborState = level.getBlockState(neighborPos);
            BlockState belowNeighborState = level.getBlockState(neighborPos.below());
            if (neighborState.isAir() && belowNeighborState.is(MNDTags.POWDERY_CANNON_PLANTABLE_ON) && level.random.nextFloat() < 0.25) {
                level.setBlock(neighborPos, MNDBlocks.BULLET_PEPPER.get().defaultBlockState(), 3);
                break;
            }
        }
    }

    private void explodeAndReset(Level level, BlockPos pos, BlockState state, int age) {
        level.playSound(null, pos, SoundEvents.CREEPER_PRIMED, SoundSource.BLOCKS, 0.5F, 0.25F);
        level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1.25F, false, Explosion.BlockInteraction.NONE);
        level.setBlock(pos, state.setValue(LIT, false), 2);
        if (state.hasProperty(AGE) && age > 0) {
            level.setBlock(pos, state.setValue(AGE, age - 1), 3);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult context) {
        ItemStack heldItem = player.getItemInHand(hand);

         if (heldItem.is(ForgeTags.TOOLS_KNIVES) || heldItem.is(net.minecraftforge.common.Tags.Items.SHEARS)) {
             int age = state.getValue(AGE);
             if (state.getValue(LIT)) {
                 if (age > 0) {
                    level.setBlock(pos, state.setValue(AGE, age - 1), 3);
                }
                 int j = 2 + level.random.nextInt(3);
                 popResource(level, pos, new ItemStack(MNDItems.BULLET_PEPPER.get(), j));
                 level.setBlock(pos, state.setValue(LIT, Boolean.FALSE), 2);
                 heldItem.hurtAndBreak(1, player, (action) -> { action.broadcastBreakEvent(hand); });
                 level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
             }
             else if (state.getValue(LEAVE)) {
                 level.setBlock(pos, state.setValue(LEAVE, Boolean.FALSE), 2);
                 heldItem.hurtAndBreak(1, player, (action) -> { action.broadcastBreakEvent(hand); });
                 level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
             }
             return InteractionResult.sidedSuccess(level.isClientSide);
         }
        return super.use(state, level, pos, player, hand, context);
    }

    @Override
    public boolean isPathfindable(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nonnull PathComputationType path) {
        return false;
    }

    @Override
    public BlockPathTypes getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob) {
        return BlockPathTypes.DAMAGE_OTHER;
    }

    @Override
    public BlockPathTypes getAdjacentBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob, BlockPathTypes originalType) {
        return BlockPathTypes.DANGER_OTHER;
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter level, BlockPos pos, BlockState state, boolean isClientSide) {
        return false;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return false;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
    }
}
