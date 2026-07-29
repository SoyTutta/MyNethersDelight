package com.soytutta.mynethersdelight.common.block;

import com.soytutta.mynethersdelight.common.registry.MNDBlocks;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import com.soytutta.mynethersdelight.common.tag.MNDTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.Tags;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import javax.annotation.Nullable;

public class PowderyFlowerBlock extends BushBlock implements BonemealableBlock {
    public static final int MAX_AGE = 3;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final IntegerProperty PRESSURE = IntegerProperty.create("pressure", 0, 2);
    protected static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 5.0, 11.0);

    public PowderyFlowerBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(AGE, 0)
                .setValue(LIT, false)
                .setValue(PRESSURE, 0));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState blockBelow = level.getBlockState(pos.below());
        return blockBelow.is(MNDTags.POWDERY_CANNON_PLANTABLE_ON)
                || blockBelow.is(MNDTags.POWDERY_CANE);
    }

    @Override
    @SuppressWarnings("deprecation")
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(MNDItems.BULLET_PEPPER.get());
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter level,
                               BlockPos pos, CollisionContext context) {
        Vec3 offset = state.getOffset(level, pos);
        return SHAPE.move(offset.x, offset.y, offset.z);
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
            level.scheduleTick(pos, this, 2);
            return state;
        }
        if (direction == Direction.UP && (neighborState.is(MNDBlocks.POWDERY_CANE.get())
                || neighborState.is(MNDBlocks.BULLET_PEPPER.get()))) {
            return MNDBlocks.POWDERY_CANE.get().defaultBlockState()
                    .setValue(PowderyCaneBlock.LIT, state.getValue(LIT))
                    .setValue(PowderyCaneBlock.PRESSURE, state.getValue(PRESSURE))
                    .setValue(PowderyCaneBlock.AGE, 0);
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, LIT, PRESSURE);
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return false;
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(AGE) < MAX_AGE;
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
        if (pressure == 2 && isLit) {
            explodeAndReset(level, pos, state);
            return;
        }
        if (pressure > 0) {
            level.setBlock(pos, state.setValue(PRESSURE, pressure - 1), 2);
            level.scheduleTick(pos, this, 20);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.is(this)) {
            return;
        }

        int age = state.getValue(AGE);
        if (age < MAX_AGE
                && ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt(5) == 0)) {
            int newAge = age + 1;
            BlockState newState = state.setValue(AGE, newAge);
            if (newAge == MAX_AGE) {
                newState = newState.setValue(LIT, true);
            }
            level.setBlock(pos, newState, 2);
            ForgeHooks.onCropsGrowPost(level, pos, newState);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(newState));
            return;
        }

        BlockState blockBelow = level.getBlockState(pos.below());
        if (blockBelow.is(MNDBlocks.POWDERY_CANNON.get())
                || blockBelow.is(MNDBlocks.RESURGENT_SOIL.get()) && random.nextFloat() < 0.5F
                || blockBelow.is(Tags.Blocks.GRAVEL) && random.nextFloat() < 0.25F
                || blockBelow.is(BlockTags.NYLIUM) && random.nextFloat() < 0.05F) {
            placePepperAbove(level, pos, state);
        }
        level.scheduleTick(pos, this, 1);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide || !(entity instanceof LivingEntity)
                || entity.getType() == EntityType.PANDA || entity.getType() == EntityType.BEE) {
            return;
        }

        entity.makeStuckInBlock(state, new Vec3(0.8F, 0.75F, 0.8F));
        if (entity.xOld != entity.getX() || entity.zOld != entity.getZ()) {
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
            BlockState newState = state.setValue(AGE, 0).setValue(LIT, false).setValue(PRESSURE, 0);
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
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos,
                                         BlockState state, boolean isClientSide) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random,
                                     BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random,
                                BlockPos pos, BlockState state) {
        if (random.nextFloat() < 0.75F && state.getValue(AGE) < MAX_AGE) {
            int newAge = state.getValue(AGE) + 1;
            BlockState newState = state.setValue(AGE, newAge);
            if (newAge == MAX_AGE) {
                newState = newState.setValue(LIT, true);
            }
            level.setBlock(pos, newState, 2);
        } else {
            placePepperAbove(level, pos, state);
        }
    }

    private void placePepperAbove(Level level, BlockPos pos, BlockState state) {
        BlockPos abovePos = pos.above();
        if (level.isEmptyBlock(abovePos) || level.getBlockState(abovePos).canBeReplaced()) {
            BlockState pepperState = MNDBlocks.BULLET_PEPPER.get().defaultBlockState();
            if (pepperState.hasProperty(LIT)) {
                pepperState = pepperState.setValue(LIT, state.getValue(LIT));
            }
            if (pepperState.hasProperty(PRESSURE)) {
                pepperState = pepperState.setValue(PRESSURE, state.getValue(PRESSURE));
            }
            level.setBlock(abovePos, pepperState, 3);
        }
    }

    private void explodeAndReset(Level level, BlockPos pos, BlockState state) {
        if (!level.isClientSide && state.getValue(LIT)) {
            level.playSound(null, pos, SoundEvents.CREEPER_PRIMED,
                    SoundSource.BLOCKS, 0.5F, 0.25F);
            level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    0.55F, false, Level.ExplosionInteraction.NONE);
            level.setBlock(pos, state.setValue(AGE, 0)
                    .setValue(LIT, false).setValue(PRESSURE, 0), 2);
        }
    }
}
