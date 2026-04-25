package com.soytutta.mynethersdelight.common.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import javax.annotation.Nullable;

import com.soytutta.mynethersdelight.common.block.entity.BlazierBlockEntity;
import com.soytutta.mynethersdelight.common.registry.MNDBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlazierBlock extends BaseEntityBlock {

    public static final MapCodec<BlazierBlock> CODEC = RecordCodecBuilder.mapCodec((p) ->
            p.group(propertiesCodec()).apply(p, BlazierBlock::new));

    protected static final VoxelShape PLATE_SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 2.0, 15.0);
    protected static final VoxelShape SHAPE_BLAZEFIRE = Shapes.joinUnoptimized(PLATE_SHAPE, Block.box(2.0F, 0.0F, 2.0F, 14.0F, 6.0F, 14.0F), BooleanOp.OR);
    protected static final VoxelShape SHAPE_BLAZEFIRE_SMOKING = Shapes.joinUnoptimized(PLATE_SHAPE, Block.box(2.0F, 0.0F, 2.0F, 14.0F, 4.0F, 14.0F), BooleanOp.OR);

    public enum HeatLevel implements StringRepresentable {
        SMELTING("smelting"),
        BAKING("baking"),
        CAMPFIRE("campfire"),
        SMOKING("smoking");

        private final String name;
        HeatLevel(String name) { this.name = name; }

        @Override
        public String getSerializedName() { return this.name; }

        public Optional<HeatLevel> decrease() {
            return switch (this) {
                case SMELTING -> Optional.of(BAKING);
                case BAKING   -> Optional.of(CAMPFIRE);
                case CAMPFIRE -> Optional.of(SMOKING);
                case SMOKING  -> Optional.empty();
            };
        }

        public int getBlazepowderDrop(RandomSource random) {
            return switch (this) {
                case SMELTING -> 1 + random.nextInt(5);
                case BAKING   -> 1 + random.nextInt(4);
                case CAMPFIRE -> 1 + random.nextInt(2);
                case SMOKING  -> random.nextInt(2);
            };
        }
    }

    public static final EnumProperty<HeatLevel> HEAT = EnumProperty.create("heat", HeatLevel.class);
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    @Override
    public MapCodec<BlazierBlock> codec() { return CODEC; }

    public BlazierBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(LIT, true)
                .setValue(HEAT, HeatLevel.SMELTING)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level,
                                              BlockPos pos, Player player, InteractionHand hand,
                                              BlockHitResult hitResult) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof BlazierBlockEntity blazeEntity)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        ItemStack heldItem = player.getItemInHand(hand);

        if (state.getValue(LIT)) {
            Optional<RecipeHolder<?>> recipeOpt = blazeEntity.getCookableRecipe(heldItem);
            if (recipeOpt.isPresent()) {
                if (!level.isClientSide &&
                        blazeEntity.placeFood(player, heldItem, blazeEntity.getCookTimeForRecipe(recipeOpt.get(), state.getValue(HEAT)))) {
                    player.awardStat(Stats.INTERACT_WITH_CAMPFIRE);
                    return ItemInteractionResult.SUCCESS;
                }
                return ItemInteractionResult.CONSUME;
            }

            if (!level.isClientSide && blazeEntity.hasFreeSlot()) {
                if (blazeEntity.hasRecipeInLowerHeat(heldItem)) {
                    player.displayClientMessage(
                            Component.translatable("block.mynethersdelight.blazier.too_hot"),
                            true);
                } else if (blazeEntity.hasRecipeInHigherHeat(heldItem)) {
                    player.displayClientMessage(
                            Component.translatable("block.mynethersdelight.blazier.too_cold"),
                            true);
                }
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hitResult) {
        if (!player.isShiftKeyDown()) return InteractionResult.PASS;
        if (!level.isClientSide) {
            boolean isLit = state.getValue(LIT);

            if (!isLit) {
                level.destroyBlock(pos, true, player);
                return InteractionResult.sidedSuccess(false);
            }

            HeatLevel current = state.getValue(HEAT);
            Optional<HeatLevel> next = current.decrease();

            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof BlazierBlockEntity blazeEntity) {
                Containers.dropContents(level, pos, blazeEntity.getItems());
                blazeEntity.clearContent();
            }

            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(),
                    new ItemStack(Items.BLAZE_POWDER, level.random.nextInt(2)));

            if (next.isPresent()) {
                boolean keepLit = current != HeatLevel.SMOKING;
                level.setBlock(pos, state
                        .setValue(HEAT, next.get())
                        .setValue(LIT, keepLit), 11);
            } else {
                level.setBlock(pos, state.setValue(LIT, false), 11);
            }

            level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (state.getValue(LIT) && entity instanceof LivingEntity) {
            int damage = switch (state.getValue(HEAT)) {
                case SMELTING -> 4;
                case BAKING   -> 3;
                case CAMPFIRE -> 2;
                case SMOKING  -> 1;
            };
            entity.hurt(level.damageSources().campfire(), damage);
        }
        super.entityInside(state, level, pos, entity);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos,
                                   Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide) {
            FluidState fluid = level.getFluidState(pos);
            if (fluid.is(FluidTags.WATER)) {
                dropResources(state, level, pos);
                level.removeBlock(pos, false);
                level.levelEvent(LevelEvent.SOUND_EXTINGUISH_FIRE, pos, 0);
                return;
            }
        }
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (level.getFluidState(pos).is(FluidTags.WATER)) return null;
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(LIT, true)
                .setValue(HEAT, HeatLevel.SMELTING);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos,
                            BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof BlazierBlockEntity blazeEntity) {
                Containers.dropContents(level, pos, blazeEntity.getItems());
            }

            int powderToDrop = state.getValue(LIT)
                    ? state.getValue(HEAT).getBlazepowderDrop(level.random)
                    : 0;

            if (powderToDrop > 0) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(),
                        new ItemStack(Items.BLAZE_POWDER, powderToDrop));
            }

            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        if (state.getValue(LIT)) {
            return state.getValue(HEAT) == HeatLevel.SMOKING ? SHAPE_BLAZEFIRE_SMOKING : SHAPE_BLAZEFIRE;
        } else {
            return PLATE_SHAPE;
        }
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlazierBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                  BlockEntityType<T> type) {
        if (!state.getValue(LIT)) {
            return createTickerHelper(type, MNDBlockEntityTypes.BLAZIER.get(),
                    BlazierBlockEntity::cooldownTick);
        }
        if (level.isClientSide) {
            return createTickerHelper(type, MNDBlockEntityTypes.BLAZIER.get(),
                    BlazierBlockEntity::particleTick);
        } else {
            return createTickerHelper(type, MNDBlockEntityTypes.BLAZIER.get(),
                    BlazierBlockEntity::cookTick);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT)) return;

        HeatLevel heat = state.getValue(HEAT);

        if (random.nextInt(10) == 0) {
            level.playLocalSound(
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    SoundEvents.BLAZE_BURN, SoundSource.BLOCKS,
                    0.4F + random.nextFloat() * 0.4F,
                    0.8F + random.nextFloat() * 0.4F,
                    false
            );
        }

        if (random.nextInt(5) != 0) return;

        spawnSmokePair(level, pos, random, 0.3);

        if (heat.ordinal() >= HeatLevel.CAMPFIRE.ordinal()) {
            spawnSmokePair(level, pos, random, 0.3);
        }

        if (heat.ordinal() >= HeatLevel.BAKING.ordinal()) {
            spawnRisingSmoke(level, pos, random, 2, 0.4, 0.1);
        }

        if (heat == HeatLevel.SMELTING) {
            spawnRisingSmoke(level, pos, random, 3, 0.4, 0.08);
        }
    }

    private static void spawnSmokePair(Level level, BlockPos pos, RandomSource random, double spread) {
        level.addParticle(ParticleTypes.SMOKE,
                randX(pos, random, spread),
                pos.getY() + 0.3,
                randZ(pos, random, spread),
                0, 0.02, 0);

        level.addParticle(ParticleTypes.LARGE_SMOKE,
                randX(pos, random, spread * 0.75),
                pos.getY() + 0.2,
                randZ(pos, random, spread * 0.75),
                0, 0.01, 0);
    }

    private static void spawnRisingSmoke(Level level, BlockPos pos, RandomSource random, int count, double spread, double speed) {
        for (int i = 0; i < count; i++) {
            level.addParticle(ParticleTypes.SMOKE,
                    randX(pos, random, spread),
                    pos.getY() + 0.4,
                    randZ(pos, random, spread),
                    (random.nextDouble() - 0.5) * speed,
                    random.nextDouble() * speed,
                    (random.nextDouble() - 0.5) * speed);

            level.addParticle(ParticleTypes.LARGE_SMOKE,
                    randX(pos, random, spread * 0.75),
                    pos.getY() + 0.2,
                    randZ(pos, random, spread * 0.75),
                    0, 0.01, 0);
        }
    }

    private static double randX(BlockPos pos, RandomSource random, double spread) {
        return pos.getX() + 0.5 + (random.nextDouble() - 0.5) * spread;
    }

    private static double randZ(BlockPos pos, RandomSource random, double spread) {
        return pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * spread;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, HEAT, FACING);
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }
}