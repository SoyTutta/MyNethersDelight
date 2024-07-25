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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.BambooSaplingBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ForgeHooks;
import vectorwing.farmersdelight.common.tag.ForgeTags;

import java.util.Random;

import static com.soytutta.mynethersdelight.common.block.PowderyCaneBlock.LEAVE;

public class PowderyFlowerBlock extends BambooSaplingBlock {
    public static final BooleanProperty LIT = BooleanProperty.create("lit");
    public static final IntegerProperty PRESSURE = IntegerProperty.create("pressure", 0, 2);
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 2);

    public PowderyFlowerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false).setValue(PRESSURE, 0).setValue(AGE, 0));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIT,AGE,PRESSURE);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState blockBelow = level.getBlockState(pos.below());
        return blockBelow.is(MNDTags.POWDERY_CANNON_PLANTABLE_ON) || blockBelow.is(MNDTags.POWDERY_CANE);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState offsetState, LevelAccessor level, BlockPos pos, BlockPos offsetPos) {
        int age = state.getValue(AGE);
        boolean isLit = state.getValue(LIT);
        if (!state.canSurvive(level, pos) || state.getValue(PRESSURE) > 0) {
            level.scheduleTick(pos, this, 1);
        }
        if (direction == Direction.UP && offsetState.is(MNDBlocks.BULLET_PEPPER.get())) {
            level.setBlock(pos, MNDBlocks.POWDERY_CANE.get().defaultBlockState(), 2);
        }
        if (isLit && age < 2) {
            level.setBlock(pos, state.setValue(LIT, false), 2);
        }
        return super.updateShape(state, direction, offsetState, level, pos, offsetPos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.isClientSide) return;
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
        if (pressure == 2 && isLit) {
            explodeAndReset(level, pos, state, age);
        }
    }
    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        int age = state.getValue(AGE);
        BlockState blockBelow = world.getBlockState(pos.below());
        boolean isBlockBelowPowderyCane = blockBelow.is(MNDTags.POWDERY_CANE);
        boolean isBlockBelowPerfectSoil = blockBelow.is(MNDBlocks.RESURGENT_SOIL.get())
                || blockBelow.is(MNDBlocks.POWDERY_CANNON.get());
        boolean isBlockBelowPowderySoil = blockBelow.is(Blocks.CRIMSON_NYLIUM)
                || blockBelow.is(Blocks.GRAVEL);
        boolean isBlockBelowLeave = blockBelow.hasProperty(LEAVE) && !blockBelow.getValue(LEAVE);
        boolean maxHeight = true;
        for (int i = 1; i <= 4; i++) {
            if (!world.getBlockState(pos.below(i)).is(MNDBlocks.POWDERY_CANE.get())) {
                maxHeight = false;
                break;
            }
        }

        if (age == 2 && random.nextInt(2) == 0) {
            world.setBlock(pos, state.setValue(LIT, true), 2);
        } else if (age < 2 && ForgeHooks.onCropsGrowPre(world, pos, state, random.nextInt(3) == 0)) {
            world.setBlock(pos, state.setValue(AGE, age + 1), 2);
            ForgeHooks.onCropsGrowPost(world, pos, state);
        }

        if (!maxHeight) {
            if (age != 2 && random.nextInt(8) == 0 && world.isEmptyBlock(pos.above()) && isBlockBelowLeave) {
            this.growBamboo(world, pos);
            } else if (age <= 2 && (isBlockBelowPerfectSoil || isBlockBelowPowderyCane || isBlockBelowPowderySoil) && world.isEmptyBlock(pos.above())) {
                if (isBlockBelowPerfectSoil) {
                this.growBamboo(world, pos);
                } else if (isBlockBelowPowderyCane && isBlockBelowLeave && random.nextInt(30) == 0) {
                this.growBamboo(world, pos);
                } else if (!isBlockBelowLeave && random.nextInt(300) == 0) {
                this.growBamboo(world, pos);
                } else if (!isBlockBelowPowderySoil && random.nextInt(1200) == 0) {
                this.growBamboo(world, pos);
                }
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!(entity instanceof LivingEntity) || entity.getType() == EntityType.PANDA || entity.getType() == EntityType.BEE || entity.isCrouching())
            return;
        if (!level.isClientSide && state.getValue(PRESSURE) < 2) {
            level.setBlock(pos, state.setValue(PRESSURE, state.getValue(PRESSURE) + 1), 2);
        }
        if (state.getValue(LIT)) {
            int age = state.hasProperty(AGE) ? state.getValue(AGE) : 0;
            level.playSound(null, pos, SoundEvents.GRASS_BREAK, SoundSource.BLOCKS, 0.25F, 0.1F);
            explodeAndReset(level, pos, state, age);
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
        if (state.hasProperty(AGE) && age > 0) {
            level.setBlock(pos, state.setValue(AGE, age - 1), 3);
        }
        level.setBlock(pos, state.setValue(LIT, false), 2);
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult context) {
        int age = state.getValue(AGE);

        if ( age == 2 && state.getValue(LIT)) {
            ItemStack heldItem = player.getItemInHand(hand);
            if (heldItem.is(ForgeTags.TOOLS_KNIVES) ||heldItem.is(net.minecraftforge.common.Tags.Items.SHEARS)) {
                heldItem.hurtAndBreak(1, player, (action) -> { action.broadcastBreakEvent(hand); });
                level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
                level.destroyBlock(pos, true);
                Random random = new Random();
                popResource(level, pos, new ItemStack(MNDItems.BULLET_PEPPER.get(), random.nextInt(100) < 25 ? 1 : 0));
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return super.use(state, level, pos, player, hand, context);
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return false;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return new ItemStack(MNDItems.BULLET_PEPPER.get());
    }

    @Override
    protected void growBamboo(Level level, BlockPos pos) {
        BlockState currentBlockState = level.getBlockState(pos);
        boolean isLit = currentBlockState.getValue(PowderyFlowerBlock.LIT);
        BlockState newBlockState = defaultBlockState();
        if (isLit) {
            newBlockState = MNDBlocks.BULLET_PEPPER.get().defaultBlockState().setValue(PowderyFlowerBlock.AGE, 2).setValue(PowderyFlowerBlock.LIT, true);
        }
        level.setBlock(pos.above(), newBlockState, 3);
    }
}
