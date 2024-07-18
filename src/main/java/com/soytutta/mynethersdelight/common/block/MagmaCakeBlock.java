package com.soytutta.mynethersdelight.common.block;


import com.mojang.datafixers.util.Pair;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fml.ModList;
import vectorwing.farmersdelight.common.registry.ModDamageTypes;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import java.util.function.Supplier;

public class MagmaCakeBlock extends Block {
    public static final BooleanProperty SECOND_CAKE = BooleanProperty.create("second_cake");
    public static final DirectionProperty SECOND_CAKE_FACING = DirectionProperty.create("second_facing", Direction.Plane.HORIZONTAL);;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 6);

    protected static final VoxelShape[][] SHAPE_BY_BITE = new VoxelShape[][]{
         {
            Block.box(1.0, 0.0, 1.0, 15.0, 8.0, 15.0),
            Block.box(1.0, 0.0, 1.0, 15.0, 8.0, 13.0),
            Block.box(1.0, 0.0, 1.0, 15.0, 8.0, 11.0),
            Block.box(1.0, 0.0, 1.0, 15.0, 8.0, 9.0),
            Block.box(1.0, 0.0, 1.0, 15.0, 8.0, 7.0),
            Block.box(1.0, 0.0, 1.0, 15.0, 8.0, 5.0),
            Block.box(1.0, 0.0, 1.0, 15.0, 8.0, 3.0)
         },{
            Block.box(1.0, 0.0, 1.0, 15.0, 8.0, 15.0),
            Block.box(3.0, 0.0, 1.0, 15.0, 8.0, 15.0),
            Block.box(5.0, 0.0, 1.0, 15.0, 8.0, 15.0),
            Block.box(7.0, 0.0, 1.0, 15.0, 8.0, 15.0),
            Block.box(9.0, 0.0, 1.0, 15.0, 8.0, 15.0),
            Block.box(11.0, 0.0, 1.0, 15.0, 8.0, 15.0),
            Block.box(13.0, 0.0, 1.0, 15.0, 8.0, 15.0)
         },{
            Block.box(1.0, 0.0, 1.0, 15.0, 8.0, 15.0),
            Block.box(1.0, 0.0, 3.0, 15.0, 8.0, 15.0),
            Block.box(1.0, 0.0, 5.0, 15.0, 8.0, 15.0),
            Block.box(1.0, 0.0, 7.0, 15.0, 8.0, 15.0),
            Block.box(1.0, 0.0, 9.0, 15.0, 8.0, 15.0),
            Block.box(1.0, 0.0, 11.0, 15.0, 8.0, 15.0),
            Block.box(1.0, 0.0, 13.0, 15.0, 8.0, 15.0)
         },{
            Block.box(1.0, 0.0, 1.0, 15.0, 8.0, 15.0),
            Block.box(1.0, 0.0, 1.0, 13.0, 8.0, 15.0),
            Block.box(1.0, 0.0, 1.0, 11.0, 8.0, 15.0),
            Block.box(1.0, 0.0, 1.0, 9.0, 8.0, 15.0),
            Block.box(1.0, 0.0, 1.0, 7.0, 8.0, 15.0),
            Block.box(1.0, 0.0, 1.0, 5.0, 8.0, 15.0),
            Block.box(1.0, 0.0, 1.0, 3.0, 8.0, 15.0)
         }
    };
    
    protected static final VoxelShape[][] SECOND_SHAPE_BY_BITE = new VoxelShape[][]{
         {
            Shapes.join(Block.box(1, 0, 1, 15, 8, 15), Block.box(2, 8, 2, 14, 16, 14), BooleanOp.OR),
            Shapes.join(Block.box(1, 0, 1, 15, 8, 15), Block.box(2, 8, 2, 14, 16, 13), BooleanOp.OR),
            Shapes.join(Block.box(1, 0, 1, 15, 8, 15), Block.box(2, 8, 2, 14, 16, 11), BooleanOp.OR),
            Shapes.join(Block.box(1, 0, 1, 15, 8, 15), Block.box(2, 8, 2, 14, 16, 9 ), BooleanOp.OR),
            Shapes.join(Block.box(1, 0, 1, 15, 8, 15), Block.box(2, 8, 2, 14, 16, 7 ), BooleanOp.OR),
            Shapes.join(Block.box(1, 0, 1, 15, 8, 15), Block.box(2, 8, 2, 14, 16, 5 ), BooleanOp.OR),
            Shapes.join(Block.box(1, 0, 1, 15, 8, 15), Block.box(2, 8, 2, 14, 16, 3 ), BooleanOp.OR)
         },{
            Shapes.join(Block.box(1, 0, 1, 15, 8, 15), Block.box(2, 8, 2, 14, 16, 14), BooleanOp.OR),
            Shapes.join(Block.box(1, 0, 1, 15, 8, 15), Block.box(3, 8, 2, 14, 16, 14), BooleanOp.OR),
            Shapes.join(Block.box(1, 0, 1, 15, 8, 15), Block.box(5, 8, 2, 14, 16, 14), BooleanOp.OR),
            Shapes.join(Block.box(1, 0, 1, 15, 8, 15), Block.box(7, 8, 2, 14, 16, 14), BooleanOp.OR),
            Shapes.join(Block.box(1, 0, 1, 15, 8, 15), Block.box(9, 8, 2, 14, 16, 14), BooleanOp.OR),
            Shapes.join(Block.box(1, 0, 1, 15, 8, 15), Block.box(11, 8, 2, 14, 16, 14), BooleanOp.OR),
            Shapes.join(Block.box(1, 0, 1, 15, 8, 15), Block.box(13, 8, 2, 14, 16, 14), BooleanOp.OR)
         },{
            Shapes.join(Block.box(1, 0, 1, 15, 8, 15), Block.box(2, 8, 2, 14, 16, 14), BooleanOp.OR),
            Shapes.join(Block.box(1, 0, 1, 15, 8, 15), Block.box(2, 8, 3, 14, 16, 14), BooleanOp.OR),
            Shapes.join(Block.box(1, 0, 1, 15, 8, 15), Block.box(2, 8, 5, 14, 16, 14), BooleanOp.OR),
            Shapes.join(Block.box(1, 0, 1, 15, 8, 15), Block.box(2, 8, 7, 14, 16, 14), BooleanOp.OR),
            Shapes.join(Block.box(1, 0, 1, 15, 8, 15), Block.box(2, 8, 9, 14, 16, 14), BooleanOp.OR),
            Shapes.join(Block.box(1, 0, 1, 15, 8, 15), Block.box(2, 8, 11, 14, 16, 14), BooleanOp.OR),
            Shapes.join(Block.box(1, 0, 1, 15, 8, 15), Block.box(2, 8, 13, 14, 16, 14), BooleanOp.OR)
         },{
            Shapes.join(Block.box(1, 0, 1, 15, 8, 15), Block.box(2, 8, 2, 14, 16, 14), BooleanOp.OR),
            Shapes.join(Block.box(1, 0, 1, 15, 8, 15), Block.box(2, 8, 2, 13, 16, 14), BooleanOp.OR),
            Shapes.join(Block.box(1, 0, 1, 15, 8, 15), Block.box(2, 8, 2, 11, 16, 14), BooleanOp.OR),
            Shapes.join(Block.box(1, 0, 1, 15, 8, 15), Block.box(2, 8, 2, 9, 16, 14), BooleanOp.OR),
            Shapes.join(Block.box(1, 0, 1, 15, 8, 15), Block.box(2, 8, 2, 7, 16, 14), BooleanOp.OR),
            Shapes.join(Block.box(1, 0, 1, 15, 8, 15), Block.box(2, 8, 2, 5, 16, 14), BooleanOp.OR),
            Shapes.join(Block.box(1, 0, 1, 15, 8, 15), Block.box(2, 8, 2, 3, 16, 14), BooleanOp.OR)
         }
    };
    
    public final Supplier<Item> pieSlice;

    public MagmaCakeBlock(Properties properties, Supplier<Item> pieSlice) {
        super(properties);
        this.pieSlice = pieSlice;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(BITES, 0)
                .setValue(SECOND_CAKE, false).setValue(SECOND_CAKE_FACING, Direction.NORTH));
    }

    public ItemStack getPieSliceItem() {
        return new ItemStack(this.pieSlice.get());
    }

    public int getMaxBites() {
        return 7;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        boolean isSecondCake = state.getValue(SECOND_CAKE);
        int second_direction = state.getValue(SECOND_CAKE_FACING).get2DDataValue();
        int direction = state.getValue(FACING).get2DDataValue();
        int bites = state.getValue(BITES);
        if (isSecondCake) {
            return SECOND_SHAPE_BY_BITE[second_direction][bites];
        } else {
            return SHAPE_BY_BITE[direction][bites];
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldStack = player.getItemInHand(hand);
        if (level.isClientSide) {
            if (heldStack.is(MNDItems.MAGMA_CAKE.get())) {
                return secondCake(level, pos, state, player);
            }

            if (heldStack.is(ModTags.KNIVES)) {
                return cutSlice(level, pos, state, player);
            }

            if (this.consumeBite(level, pos, state, player) == InteractionResult.SUCCESS) {
                return InteractionResult.SUCCESS;
            }

            if (heldStack.isEmpty()) {
                return InteractionResult.CONSUME;
            }
        }

        if (heldStack.is(MNDItems.MAGMA_CAKE.get())) {
            return secondCake(level, pos, state, player);
        }

        if (heldStack.is(ModTags.KNIVES)) {
            return cutSlice(level, pos, state, player);
        }
        return this.consumeBite(level, pos, state, player);
    }
    protected InteractionResult secondCake(Level level, BlockPos pos, BlockState state, Player player) {
        Direction direction = player.getDirection().getOpposite();
        ItemStack heldStack = player.getMainHandItem();
        if (ModList.get().isLoaded("amendments")) {
            if (state.getValue(BITES) == 0 && !state.getValue(SECOND_CAKE)) {
                if (!player.isCreative()) {
                    heldStack.shrink(1);
                }
                level.setBlock(pos, state.setValue(SECOND_CAKE_FACING, direction)
                        .setValue(SECOND_CAKE, true), 3);
            }
        } else { return InteractionResult.PASS; }
        level.playSound(null, pos, SoundEvents.MAGMA_CUBE_SQUISH_SMALL, SoundSource.PLAYERS, 0.8F, 0.8F);
        return InteractionResult.SUCCESS;
    }

    protected InteractionResult consumeBite(Level level, BlockPos pos, BlockState state, Player playerIn) {
        if (!playerIn.canEat(false)) {
            return InteractionResult.PASS;
        } else {
            ItemStack sliceStack = this.getPieSliceItem();
            FoodProperties sliceFood = sliceStack.getItem().getFoodProperties();

            playerIn.getFoodData().eat(sliceStack.getItem(), sliceStack);
            if (this.getPieSliceItem().getItem().isEdible() && sliceFood != null) {
                for (Pair<MobEffectInstance, Float> pair : sliceFood.getEffects()) {
                    if (!level.isClientSide && pair.getFirst() != null && level.random.nextFloat() < pair.getSecond()) {
                        playerIn.addEffect(new MobEffectInstance(pair.getFirst()));
                    }
                }
            }

            int bites = state.getValue(BITES);
            if (bites < getMaxBites() - 1) {
                level.setBlock(pos, state.setValue(BITES, bites + 1), 3);
            } else if (state.getValue(SECOND_CAKE)){
                level.setBlock(pos, state.setValue(BITES, 0)
                        .setValue(SECOND_CAKE, false), 3);
            } else {
                level.removeBlock(pos, false);
            }
            level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.8F, 0.8F);
            return InteractionResult.SUCCESS;
        }
    }

    protected InteractionResult cutSlice(Level level, BlockPos pos, BlockState state, Player player) {
        int bites = state.getValue(BITES);
        if (bites < getMaxBites() - 1) {
            level.setBlock(pos, state.setValue(BITES, bites + 1), 3);
        } else if (state.getValue(SECOND_CAKE)){
            level.setBlock(pos, state.setValue(BITES, 0)
                    .setValue(SECOND_CAKE, false), 3);
        } else {
            level.removeBlock(pos, false);
        }

        Direction direction = player.getDirection().getOpposite();
        ItemUtils.spawnItemEntity(level, this.getPieSliceItem(), pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5,
                direction.getStepX() * 0.15, 0.05, direction.getStepZ() * 0.15);
        level.playSound(null, pos, SoundEvents.MAGMA_CUBE_SQUISH, SoundSource.PLAYERS, 0.8F, 0.8F);
        return InteractionResult.SUCCESS;
    }

    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!entity.fireImmune() && entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)entity)) {
            entity.hurt(ModDamageTypes.getSimpleDamageSource(level, DamageTypes.HOT_FLOOR), 1.0F);
        }
        super.stepOn(level, pos, state, entity);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return facing == Direction.DOWN && !stateIn.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isSolid();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, BITES, SECOND_CAKE, SECOND_CAKE_FACING);
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
        super.animateTick(state, level, pos, rand);
        VoxelShape shape = this.getShape(state, level, pos, CollisionContext.empty());
        shape.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
            for (int i = 0; i < 0.025; ++i) {
                double particleX = minX + (maxX - minX) * rand.nextDouble() + pos.getX();
                double particleY = minY + (maxY - minY) * rand.nextDouble() + pos.getY();
                double particleZ = minZ + (maxZ - minZ) * rand.nextDouble() + pos.getZ();
                double offsetX = (rand.nextDouble() - 0.5) * 0.45;
                double offsetY = (rand.nextDouble() - 0.5) * 0.45;
                double offsetZ = (rand.nextDouble() - 0.5) * 0.45;

                level.addParticle(ParticleTypes.FLAME, particleX + offsetX, particleY + offsetY, particleZ + offsetZ, 0.0D, 0.0D, 0.0D);
            }
        });
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        return getMaxBites() - blockState.getValue(BITES);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }
}