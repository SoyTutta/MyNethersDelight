package com.soytutta.mynethersdelight.common.block.crops;

import com.mojang.serialization.MapCodec;
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
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.Tags;
import vectorwing.farmersdelight.common.tag.CommonTags;

import static com.soytutta.mynethersdelight.common.block.utility.MNDBlockStateProperties.PRESSURE;

public class PowderyFlowerBlock extends BushBlock implements BonemealableBlock {

    public static final MapCodec<PowderyCaneBlock> CODEC = simpleCodec(PowderyCaneBlock::new);
    public static final int MAX_AGE = 3;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    protected static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 5.0, 11.0);

    public PowderyFlowerBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(AGE, 0)
                .setValue(LIT, false)
                .setValue(PRESSURE, 0));
    }

    public MapCodec<PowderyCaneBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState blockBelow = level.getBlockState(pos.below());
        return blockBelow.is(MNDTags.POWDERY_CANNON_PLANTABLE_ON) || blockBelow.is(MNDTags.POWDERY_CANE);
    }

    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(MNDItems.BULLET_PEPPER.get());
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        Vec3 vec3 = state.getOffset(worldIn, pos);
        return SHAPE.move(vec3.x, vec3.y, vec3.z);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState offsetState, LevelAccessor level, BlockPos pos, BlockPos offsetPos) {
        if (!state.canSurvive(level, pos)) {
            level.scheduleTick(pos, this, 2);
            return state;
        }

        if (direction == Direction.UP && (offsetState.is(MNDBlocks.POWDERY_CANE.get()) || offsetState.is(MNDBlocks.BULLET_PEPPER.get()))) {
            return MNDBlocks.POWDERY_CANE.get().defaultBlockState()
                    .setValue(PowderyCaneBlock.LIT, state.getValue(LIT))
                    .setValue(PRESSURE, state.getValue(PRESSURE))
                    .setValue(PowderyCaneBlock.AGE, 0);
        }
        return super.updateShape(state, direction, offsetState, level, pos, offsetPos);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, LIT, PRESSURE);
    }

    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return false;
    }

    protected boolean isRandomlyTicking(BlockState state) {
        return state.getValue(AGE) < MAX_AGE;
    }

    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (world.isClientSide || !state.is(this)) return;

        int pressure = state.getValue(PRESSURE);
        boolean isLit = state.getValue(LIT);

        if (!state.canSurvive(world, pos)) {
            if (isLit) explodeAndReset(world, pos, state);
            world.destroyBlock(pos, true);
            return;
        }

        if (pressure == 2 && isLit) {
            explodeAndReset(world, pos, state);
            return;
        }

        if (pressure > 0) {
            world.setBlock(pos, state.setValue(PRESSURE, pressure - 1), 2);
            world.scheduleTick(pos, this, 20);
        }
    }

    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.is(this)) return;

        int i = state.getValue(AGE);
        if (i < MAX_AGE && CommonHooks.canCropGrow(level, pos, state, random.nextInt(5) == 0)) {
            int newAge = i + 1;
            BlockState blockstate = state.setValue(AGE, newAge);
            if (newAge == MAX_AGE) {
                blockstate = blockstate.setValue(LIT, true);
            }
            level.setBlock(pos, blockstate, 2);
            CommonHooks.fireCropGrowPost(level, pos, blockstate);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(blockstate));
            return;
        }
        BlockState blockBelow = level.getBlockState(pos.below());
        if ((blockBelow.is(MNDBlocks.POWDERY_CANNON.get()))
                || (blockBelow.is(MNDBlocks.RESURGENT_SOIL.get()) && random.nextFloat() < 0.5F)
                || (blockBelow.is(Tags.Blocks.GRAVELS) && random.nextFloat() < 0.25F)
                || (blockBelow.is(BlockTags.NYLIUM) && random.nextFloat() < 0.05F)) {
            BlockPos posAbove = pos.above();
            if (level.isEmptyBlock(posAbove) || level.getBlockState(posAbove).canBeReplaced()) {
                BlockState newBulletPepperState = MNDBlocks.BULLET_PEPPER.get().defaultBlockState();

                if (newBulletPepperState.hasProperty(LIT)) {
                    newBulletPepperState = newBulletPepperState.setValue(LIT, state.getValue(LIT));
                }
                if (newBulletPepperState.hasProperty(PRESSURE)) {
                    newBulletPepperState = newBulletPepperState.setValue(PRESSURE, state.getValue(PRESSURE));
                }
                level.setBlock(posAbove, newBulletPepperState, 3);
            }
        }
        level.scheduleTick(pos, this, 1);
    }

    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && entity instanceof LivingEntity && entity.getType() != EntityType.PANDA && entity.getType() != EntityType.BEE) {
            entity.makeStuckInBlock(state, new Vec3(0.8F, 0.75F, 0.8F));

            if (entity.xOld != entity.getX() || entity.zOld != entity.getZ()) {
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
            if (heldItem.is(CommonTags.TOOLS_KNIFE) || heldItem.is(Tags.Items.TOOLS_SHEAR)) {
                int j = 1 + level.random.nextInt(2);
                popResource(level, pos, new ItemStack(MNDItems.BULLET_PEPPER.get(), j + (flag ? 1 : 0)));
                level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
                BlockState blockstate = state.setValue(AGE, 0).setValue(LIT, false).setValue(PRESSURE, 0);
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
            if (!heldItem.is(CommonTags.TOOLS_KNIFE) && !heldItem.is(Tags.Items.TOOLS_SHEAR)) {
                explodeAndReset(level, pos, state);
                return Blocks.AIR.defaultBlockState();
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return true;
    }

    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        if (random.nextFloat() < 0.75F && state.getValue(AGE) < MAX_AGE) {
            int newAge = state.getValue(AGE) + 1;
            BlockState updatedState = state.setValue(AGE, newAge);

            if (newAge == MAX_AGE) {
                updatedState = updatedState.setValue(LIT, true);
            }
            level.setBlock(pos, updatedState, 2);
        } else {
            BlockPos posAbove = pos.above();
            if (level.isEmptyBlock(posAbove) || level.getBlockState(posAbove).canBeReplaced()) {
                BlockState newBulletPepperState = MNDBlocks.BULLET_PEPPER.get().defaultBlockState();

                if (newBulletPepperState.hasProperty(LIT)) {
                    newBulletPepperState = newBulletPepperState.setValue(LIT, state.getValue(LIT));
                }
                if (newBulletPepperState.hasProperty(PRESSURE)) {
                    newBulletPepperState = newBulletPepperState.setValue(PRESSURE, state.getValue(PRESSURE));
                }
                level.setBlock(posAbove, newBulletPepperState, 3);
            }
        }
    }

    private void explodeAndReset(Level level, BlockPos pos, BlockState state) {
        if (!level.isClientSide && state.getValue(LIT)) {
            level.playSound(null, pos, SoundEvents.CREEPER_PRIMED, SoundSource.BLOCKS, 0.5F, 0.25F);
            level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3.0F, false, Level.ExplosionInteraction.NONE);
            level.setBlock(pos, state.setValue(AGE, 0).setValue(LIT, false).setValue(PRESSURE, 0), 2);
        }
    }
}