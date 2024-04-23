//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package com.soytutta.mynethersdelight.common.block;

import javax.annotation.Nullable;

import com.soytutta.mynethersdelight.common.registry.MNDBlocks;
import com.soytutta.mynethersdelight.common.tag.MNDTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolActions;
import vectorwing.farmersdelight.common.tag.ForgeTags;
import vectorwing.farmersdelight.common.utility.MathUtils;

import java.util.Random;
import java.util.stream.Stream;


public class TrophyBlock extends Block implements SimpleWaterloggedBlock {
    public static IntegerProperty ROTTING = IntegerProperty.create("rotting", 0, 2);
    public static final DirectionProperty FACING;
    public static final BooleanProperty WATERLOGGED;
    protected static final VoxelShape EAST_AABB;
    protected static final VoxelShape WEST_AABB;
    protected static final VoxelShape SOUTH_AABB;
    protected static final VoxelShape NORTH_AABB;

    static {
        FACING = HorizontalDirectionalBlock.FACING;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        NORTH_AABB = Stream.of(
                Block.box(1, 0, 15, 15, 3, 16),
                Block.box(0, 3, 15, 16, 15, 16),
                Block.box(1, 4, 13, 15, 13, 15),
                Block.box(2, 8, 9, 14, 12, 13),
                Block.box(2, 6, 7, 14, 10, 11),
                Block.box(2, 4, 5, 14, 8, 9),
                Block.box(2, 2, 3, 14, 6, 7)
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
        EAST_AABB = Stream.of(
                Block.box(0, 0, 1, 1, 3, 15),
                Block.box(0, 3, 0, 1, 15, 16),
                Block.box(1, 4, 1, 3, 13, 15),
                Block.box(3, 8, 2, 7, 12, 14),
                Block.box(5, 6, 2, 9, 10, 14),
                Block.box(7, 4, 2, 11, 8, 14),
                Block.box(9, 2, 2, 13, 6, 14)
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
        SOUTH_AABB = Stream.of(
                Block.box(1, 0, 0, 15, 3, 1),
                Block.box(0, 3, 0, 16, 15, 1),
                Block.box(1, 4, 1, 15, 13, 3),
                Block.box(2, 8, 3, 14, 12, 7),
                Block.box(2, 6, 5, 14, 10, 9),
                Block.box(2, 4, 7, 14, 8, 11),
                Block.box(2, 2, 9, 14, 6, 13)
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
        WEST_AABB = Stream.of(
                Block.box(15, 0, 1, 16, 3, 15),
                Block.box(15, 3, 0, 16, 15, 16),
                Block.box(13, 4, 1, 15, 13, 15),
                Block.box(9, 8, 2, 13, 12, 14),
                Block.box(7, 6, 2, 11, 10, 14),
                Block.box(5, 4, 2, 9, 8, 14),
                Block.box(3, 2, 2, 7, 6, 14)
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext hit) {
        switch (state.getValue(FACING)) {
            case NORTH:
                return NORTH_AABB;
            case SOUTH:
                return SOUTH_AABB;
            case WEST:
                return WEST_AABB;
            case EAST:
            default:
                return EAST_AABB;
        }
    }

    public int getMaxRottingStage() {
        return 2;
    }
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    public TrophyBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false).setValue(ROTTING, 0));
    }

    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        if (worldIn.isClientSide || state.getBlock() != MNDBlocks.HOGLIN_TROPHY.get()) return;

        if (worldIn.getBiome(pos) == BuiltinDimensionTypes.NETHER || worldIn.dimensionType().ultraWarm()) return;

        int currentRotting = state.getValue(ROTTING);
        int maxRotting = this.getMaxRottingStage();

        if (currentRotting >= maxRotting) return;

        int nextRotting = Math.min(currentRotting + 1, maxRotting);
        worldIn.setBlock(pos, state.setValue(ROTTING, nextRotting), 3);

        if (nextRotting == maxRotting) {
            convertToZoglin(worldIn, pos, random);
        } else if (nextRotting == maxRotting - 1) {
            worldIn.playSound(null, pos, SoundEvents.HOGLIN_HURT, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    private void convertToZoglin(ServerLevel worldIn, BlockPos pos, RandomSource random) {
        BlockState currentState = worldIn.getBlockState(pos);
        worldIn.playSound(null, pos, SoundEvents.HOGLIN_CONVERTED_TO_ZOMBIFIED, SoundSource.BLOCKS, 1.0F, 1.0F);
        Direction facing = currentState.getValue(FACING);
        worldIn.setBlock(pos, MNDBlocks.ZOGLIN_TROPHY.get().defaultBlockState().setValue(FACING, facing), 3);

        for (int i = 0; i < 10; i++) {
            double d0 = (double) pos.getX() + random.nextDouble();
            double d1 = (double) pos.getY() + random.nextDouble();
            double d2 = (double) pos.getZ() + random.nextDouble();
            worldIn.sendParticles(ParticleTypes.ENTITY_EFFECT, d0, d1, d2, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        Block block = state.getBlock();
        ItemStack heldItem = player.getItemInHand(hand);
        ParticleOptions secondParticle = null;
        SoundEvent secondSoundEvent = null;
        boolean useSecondEffects = false;

        if (block == MNDBlocks.HOGLIN_TROPHY.get() && (heldItem.is(MNDTags.HOGLIN_WAXED))) {
            processTrophyInteraction(level, pos, player, hand, MNDBlocks.WAXED_HOGLIN_TROPHY.get(), SoundEvents.HONEYCOMB_WAX_ON, ParticleTypes.WAX_ON, secondParticle, secondSoundEvent, useSecondEffects);
            return InteractionResult.SUCCESS;
        } else if (block == MNDBlocks.WAXED_HOGLIN_TROPHY.get() && heldItem.is(ForgeTags.TOOLS_AXES)) {
            processTrophyInteraction(level, pos, player, hand, MNDBlocks.HOGLIN_TROPHY.get(), SoundEvents.HONEYCOMB_WAX_ON, ParticleTypes.WAX_OFF, secondParticle, secondSoundEvent, useSecondEffects);
            return InteractionResult.SUCCESS;
        } else if (block == MNDBlocks.ZOGLIN_TROPHY.get() && heldItem.is(MNDTags.HOGLIN_CURE)) {
            secondParticle = ParticleTypes.ENCHANT;
            secondSoundEvent = SoundEvents.ENCHANTMENT_TABLE_USE;
            useSecondEffects = true;
            processTrophyInteraction(level, pos, player, hand, MNDBlocks.HOGLIN_TROPHY.get(), SoundEvents.ZOMBIE_VILLAGER_CURE, ParticleTypes.CLOUD, secondParticle, secondSoundEvent, useSecondEffects);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    private void processTrophyInteraction(Level level, BlockPos pos, Player player, InteractionHand hand, Block trophyBlock, SoundEvent soundEvent, ParticleOptions particle, ParticleOptions secondParticle, SoundEvent secondSoundEvent, boolean useSecondEffects) {
        if (level.isClientSide()) return;

        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem.is(ForgeTags.TOOLS)) {
            heldItem.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
        } else if (!player.getAbilities().instabuild) {
            heldItem.shrink(1);
        }

        level.playSound(null, pos, soundEvent, SoundSource.BLOCKS, 0.8F, 0.8F);
        level.setBlockAndUpdate(pos, trophyBlock.defaultBlockState().setValue(FACING, level.getBlockState(pos).getValue(FACING)));

        if (!level.isClientSide && level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel) level;
            for (int i = 0; i < 6; i++) {
                double d0 = (double) pos.getX() + level.getRandom().nextDouble();
                double d1 = (double) pos.getY() + level.getRandom().nextDouble();
                double d2 = (double) pos.getZ() + level.getRandom().nextDouble();
                serverLevel.sendParticles(particle, d0, d1, d2, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                if (useSecondEffects) {
                    serverLevel.sendParticles(secondParticle, d0, d1, d2, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                    level.playSound(null, pos, secondSoundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            }
        }
    }

    private boolean canAttachTo(BlockGetter level, BlockPos pos, Direction facing) {
        return level.getBlockState(pos).isFaceSturdy(level, pos, facing);
    }

    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        return this.canAttachTo(level, pos.relative(direction.getOpposite()), direction);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockState = this.defaultBlockState();
        LevelReader levelReader = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        for (Direction direction : context.getNearestLookingDirections()) {
            if (direction.getAxis().isHorizontal()) {
                blockState = blockState.setValue(FACING, direction.getOpposite());
                if (blockState.canSurvive(levelReader, blockPos)) {
                    return blockState.setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
                }
            }
        }
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        if (!level.dimensionType().ultraWarm() && state.getBlock() == MNDBlocks.HOGLIN_TROPHY.get()) {
            for (int i = 0; i < 0.5; i++) {
                double offsetX = random.nextGaussian() * 0.2; double offsetY = random.nextGaussian() * 0.2; double offsetZ = random.nextGaussian() * 0.2;
                double x = pos.getX() + random.nextDouble(); double y = pos.getY() + random.nextDouble(); double z = pos.getZ() + random.nextDouble();
                level.addParticle(ParticleTypes.ENCHANTED_HIT, x, y, z, offsetX, offsetY, offsetZ);
            }
        }
    }

    public BlockState rotate(BlockState state, Rotation facing) {
        return state.setValue(FACING, facing.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror facing) {
        return state.rotate(facing.getRotation(state.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, ROTTING);
        super.createBlockStateDefinition(builder);
    }

    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
}