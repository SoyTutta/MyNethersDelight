package com.soytutta.mynethersdelight.common.block;

import com.soytutta.mynethersdelight.common.MNDConfiguration;
import com.soytutta.mynethersdelight.common.block.entity.BlazierBlockEntity;
import com.soytutta.mynethersdelight.common.registry.MNDBlockEntityTypes;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
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
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class BlazierBlock extends BaseEntityBlock {
    private static final String BLOCK_STATE_TAG = "BlockStateTag";
    private static final String HEAT_TAG = "heat";
    private static final String LIT_TAG = "lit";
    private static final int EXTINGUISHED_TOOLTIP_COLOR = 0x8B2635;

    protected static final VoxelShape PLATE_SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 2.0, 15.0);
    protected static final VoxelShape SHAPE_BLAZEFIRE =
            Shapes.joinUnoptimized(PLATE_SHAPE, Block.box(2.0, 0.0, 2.0, 14.0, 6.0, 14.0), BooleanOp.OR);
    protected static final VoxelShape SHAPE_BLAZEFIRE_SMOKING =
            Shapes.joinUnoptimized(PLATE_SHAPE, Block.box(2.0, 0.0, 2.0, 14.0, 4.0, 14.0), BooleanOp.OR);

    public enum HeatLevel implements StringRepresentable {
        SMELTING("smelting", 0xFFB52A),
        BAKING("baking", 0xF08324),
        CAMPFIRE("campfire", 0xD65A2B),
        SMOKING("smoking", 0xB23A33);

        private final String name;
        private final int tooltipColor;

        HeatLevel(String name, int tooltipColor) {
            this.name = name;
            this.tooltipColor = tooltipColor;
        }

        @Override
        public String getSerializedName() {
            return name;
        }

        public int getTooltipColor() {
            return tooltipColor;
        }

        public static Optional<HeatLevel> byName(String name) {
            for (HeatLevel heat : values()) {
                if (heat.name.equals(name)) {
                    return Optional.of(heat);
                }
            }
            return Optional.empty();
        }

        public Optional<HeatLevel> decrease() {
            return switch (this) {
                case SMELTING -> Optional.of(BAKING);
                case BAKING -> Optional.of(CAMPFIRE);
                case CAMPFIRE -> Optional.of(SMOKING);
                case SMOKING -> Optional.empty();
            };
        }

        public Optional<HeatLevel> increase() {
            return switch (this) {
                case SMELTING -> Optional.empty();
                case BAKING -> Optional.of(SMELTING);
                case CAMPFIRE -> Optional.of(BAKING);
                case SMOKING -> Optional.of(CAMPFIRE);
            };
        }

        public int getBlazePowderDrop(RandomSource random) {
            return switch (this) {
                case SMELTING -> 1 + random.nextInt(5);
                case BAKING -> 1 + random.nextInt(4);
                case CAMPFIRE -> 1 + random.nextInt(2);
                case SMOKING -> random.nextInt(2);
            };
        }
    }

    public static final EnumProperty<HeatLevel> HEAT = EnumProperty.create("heat", HeatLevel.class);
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BlazierBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(LIT, true)
                .setValue(HEAT, HeatLevel.SMELTING)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip,
                                TooltipFlag tooltipFlag) {
        boolean lit = isLit(stack);
        HeatLevel heat = getHeat(stack);
        String heatName = lit ? heat.getSerializedName() : "extinguished";
        int heatColor = lit ? heat.getTooltipColor() : EXTINGUISHED_TOOLTIP_COLOR;
        Component heatComponent = Component.translatable("tooltip.mynethersdelight.blazier.heat." + heatName)
                .withStyle(style -> style.withColor(TextColor.fromRgb(heatColor)));
        tooltip.add(lit
                ? Component.translatable("tooltip.mynethersdelight.blazier.heat", heatComponent)
                        .withStyle(ChatFormatting.GRAY)
                : heatComponent);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hitResult) {
        if (!MNDConfiguration.ENABLE_BLAZIER.get()) {
            return InteractionResult.PASS;
        }

        ItemStack heldItem = player.getItemInHand(hand);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof BlazierBlockEntity blazier)) {
            return InteractionResult.PASS;
        }

        if (heldItem.isEmpty() && player.isShiftKeyDown()) {
            if (!level.isClientSide) {
                coolBlazier(state, level, pos, player, blazier);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        if (state.getValue(LIT)) {
            Optional<? extends net.minecraft.world.item.crafting.AbstractCookingRecipe> recipe =
                    blazier.getCookableRecipe(heldItem);
            if (recipe.isPresent()) {
                if (!level.isClientSide
                        && blazier.placeFood(player, heldItem,
                        blazier.getCookTimeForRecipe(recipe.get(), state.getValue(HEAT)))) {
                    player.awardStat(Stats.INTERACT_WITH_CAMPFIRE);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }

            if (!level.isClientSide && blazier.hasFreeSlot()) {
                if (blazier.hasRecipeInLowerHeat(heldItem)) {
                    player.displayClientMessage(Component.translatable("block.mynethersdelight.blazier.too_hot"), true);
                } else if (blazier.hasRecipeInHigherHeat(heldItem)) {
                    player.displayClientMessage(Component.translatable("block.mynethersdelight.blazier.too_cold"), true);
                }
            }
        }

        return InteractionResult.PASS;
    }

    private void coolBlazier(BlockState state, Level level, BlockPos pos, Player player,
                             BlazierBlockEntity blazier) {
        if (!state.getValue(LIT)) {
            level.destroyBlock(pos, true, player);
            return;
        }

        HeatLevel current = state.getValue(HEAT);
        Optional<HeatLevel> next = current.decrease();

        Containers.dropContents(level, pos, blazier.getItems());
        blazier.clearContent();
        Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(),
                new ItemStack(Items.BLAZE_POWDER, level.random.nextInt(2)));

        if (next.isPresent()) {
            level.setBlock(pos, state.setValue(HEAT, next.get()).setValue(LIT, true), 11);
            level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.35F, 1.3F);
        } else {
            level.setBlock(pos, state.setValue(LIT, false), 11);
            level.levelEvent(1009, pos, 0);
        }
        level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (MNDConfiguration.ENABLE_BLAZIER.get() && state.getValue(LIT) && entity instanceof LivingEntity) {
            int damage = switch (state.getValue(HEAT)) {
                case SMELTING -> 4;
                case BAKING -> 3;
                case CAMPFIRE -> 2;
                case SMOKING -> 1;
            };
            entity.hurt(level.damageSources().inFire(), damage);
        }
        super.entityInside(state, level, pos, entity);
    }

    @Nullable
    @Override
    public BlockPathTypes getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob) {
        return MNDConfiguration.ENABLE_BLAZIER.get() && state.getValue(LIT)
                ? BlockPathTypes.DAMAGE_FIRE
                : null;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block,
                                BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide) {
            FluidState fluid = level.getFluidState(pos);
            if (fluid.is(FluidTags.WATER)) {
                dropResources(state, level, pos);
                level.removeBlock(pos, false);
                level.levelEvent(1009, pos, 0);
                return;
            }
        }
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (!MNDConfiguration.ENABLE_BLAZIER.get()) {
            return null;
        }
        LevelAccessor level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (level.getFluidState(pos).is(FluidTags.WATER)) {
            return null;
        }
        ItemStack stack = context.getItemInHand();
        return defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(LIT, isLit(stack))
                .setValue(HEAT, getHeat(stack));
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        ItemStack stack = new ItemStack(MNDItems.BLAZIER.get());
        setStoredState(stack, state.getValue(HEAT), state.getValue(LIT));
        return stack;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof BlazierBlockEntity blazier) {
                Containers.dropContents(level, pos, blazier.getItems());
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (!state.getValue(LIT)) {
            return PLATE_SHAPE;
        }
        return state.getValue(HEAT) == HeatLevel.SMOKING
                ? SHAPE_BLAZEFIRE_SMOKING
                : SHAPE_BLAZEFIRE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
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
        if (!MNDConfiguration.ENABLE_BLAZIER.get()) {
            return null;
        }
        if (!state.getValue(LIT)) {
            return createTickerHelper(type, MNDBlockEntityTypes.BLAZIER.get(), BlazierBlockEntity::cooldownTick);
        }
        return level.isClientSide
                ? createTickerHelper(type, MNDBlockEntityTypes.BLAZIER.get(), BlazierBlockEntity::particleTick)
                : createTickerHelper(type, MNDBlockEntityTypes.BLAZIER.get(), BlazierBlockEntity::cookTick);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!MNDConfiguration.ENABLE_BLAZIER.get() || !state.getValue(LIT)) {
            return;
        }

        HeatLevel heat = state.getValue(HEAT);
        if (random.nextInt(10) == 0) {
            level.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    SoundEvents.BLAZE_BURN, SoundSource.BLOCKS,
                    0.4F + random.nextFloat() * 0.4F,
                    0.8F + random.nextFloat() * 0.4F, false);
        }
        if (random.nextInt(5) != 0) {
            return;
        }

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
        level.addParticle(ParticleTypes.SMOKE, randX(pos, random, spread), pos.getY() + 0.3,
                randZ(pos, random, spread), 0, 0.02, 0);
        level.addParticle(ParticleTypes.LARGE_SMOKE, randX(pos, random, spread * 0.75),
                pos.getY() + 0.2, randZ(pos, random, spread * 0.75), 0, 0.01, 0);
    }

    private static void spawnRisingSmoke(Level level, BlockPos pos, RandomSource random,
                                         int count, double spread, double speed) {
        for (int i = 0; i < count; i++) {
            level.addParticle(ParticleTypes.SMOKE, randX(pos, random, spread), pos.getY() + 0.4,
                    randZ(pos, random, spread), (random.nextDouble() - 0.5) * speed,
                    random.nextDouble() * speed, (random.nextDouble() - 0.5) * speed);
            level.addParticle(ParticleTypes.LARGE_SMOKE, randX(pos, random, spread * 0.75),
                    pos.getY() + 0.2, randZ(pos, random, spread * 0.75), 0, 0.01, 0);
        }
    }

    private static double randX(BlockPos pos, RandomSource random, double spread) {
        return pos.getX() + 0.5 + (random.nextDouble() - 0.5) * spread;
    }

    private static double randZ(BlockPos pos, RandomSource random, double spread) {
        return pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * spread;
    }

    public static HeatLevel getHeat(ItemStack stack) {
        CompoundTag stateTag = stack.getTagElement(BLOCK_STATE_TAG);
        if (stateTag == null) {
            return HeatLevel.SMELTING;
        }
        return HeatLevel.byName(stateTag.getString(HEAT_TAG)).orElse(HeatLevel.SMELTING);
    }

    public static boolean isLit(ItemStack stack) {
        CompoundTag stateTag = stack.getTagElement(BLOCK_STATE_TAG);
        return stateTag == null || !stateTag.contains(LIT_TAG) || Boolean.parseBoolean(stateTag.getString(LIT_TAG));
    }

    public static boolean canIncrease(ItemStack stack) {
        return !isLit(stack) || getHeat(stack) != HeatLevel.SMELTING;
    }

    public static void setStoredState(ItemStack stack, HeatLevel heat, boolean lit) {
        CompoundTag stateTag = stack.getOrCreateTagElement(BLOCK_STATE_TAG);
        stateTag.putString(HEAT_TAG, heat.getSerializedName());
        stateTag.putString(LIT_TAG, Boolean.toString(lit));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, HEAT, FACING);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }
}
