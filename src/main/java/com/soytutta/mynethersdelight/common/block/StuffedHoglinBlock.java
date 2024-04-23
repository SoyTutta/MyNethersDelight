//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.common.block;

import javax.annotation.Nullable;

import com.soytutta.mynethersdelight.common.utility.MNDTextUtils;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.DoubleBlockCombiner.BlockType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.TextUtils;

// thanks Umpaz for letting me use this code <3
public class StuffedHoglinBlock extends HorizontalDirectionalBlock {
    public static final EnumProperty<BedPart> PART;
    public static final IntegerProperty SERVINGS;
    protected static final VoxelShape[] SHAPES_NORTH_HEAD;
    protected static final VoxelShape[] SHAPES_NORTH_FOOT;
    protected static final VoxelShape[] SHAPES_SOUTH_HEAD;
    protected static final VoxelShape[] SHAPES_SOUTH_FOOT;
    protected static final VoxelShape[] SHAPES_WEST_HEAD;
    protected static final VoxelShape[] SHAPES_WEST_FOOT;
    protected static final VoxelShape[] SHAPES_EAST_HEAD;
    protected static final VoxelShape[] SHAPES_EAST_FOOT;

    public StuffedHoglinBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.getStateDefinition().any()).setValue(FACING, Direction.NORTH)).setValue(SERVINGS, 11)).setValue(PART, BedPart.HEAD));
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(PART) == BedPart.HEAD) {
            switch ((Direction)state.getValue(FACING)) {
                case NORTH:
                    return SHAPES_NORTH_HEAD[(Integer)state.getValue(SERVINGS)];
                case SOUTH:
                    return SHAPES_SOUTH_HEAD[(Integer)state.getValue(SERVINGS)];
                case WEST:
                    return SHAPES_WEST_HEAD[(Integer)state.getValue(SERVINGS)];
                case EAST:
                    return SHAPES_EAST_HEAD[(Integer)state.getValue(SERVINGS)];
            }
        }

        if (state.getValue(PART) == BedPart.FOOT) {
            switch ((Direction)state.getValue(FACING)) {
                case NORTH:
                    return SHAPES_NORTH_FOOT[(Integer)state.getValue(SERVINGS)];
                case SOUTH:
                    return SHAPES_SOUTH_FOOT[(Integer)state.getValue(SERVINGS)];
                case WEST:
                    return SHAPES_WEST_FOOT[(Integer)state.getValue(SERVINGS)];
                case EAST:
                    return SHAPES_EAST_FOOT[(Integer)state.getValue(SERVINGS)];
            }
        }

        return SHAPES_NORTH_HEAD[(Integer)state.getValue(SERVINGS)];
    }

    private static Direction getDirectionToOther(BedPart part, Direction direction) {
        return part == BedPart.HEAD ? direction : direction.getOpposite();
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, SERVINGS, PART});
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (facing != getDirectionToOther((BedPart)stateIn.getValue(PART), (Direction)stateIn.getValue(FACING))) {
            return !stateIn.canSurvive(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        } else {
            return stateIn.canSurvive(worldIn, currentPos) && facingState.is(this) && facingState.getValue(PART) != stateIn.getValue(PART) ? stateIn : Blocks.AIR.defaultBlockState();
        }
    }
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).getMaterial().isSolid();
    }

    public void playerWillDestroy(Level p_49505_, BlockPos p_49506_, BlockState p_49507_, Player p_49508_) {
        if (!p_49505_.isClientSide && p_49508_.isCreative()) {
            BedPart bedpart = (BedPart)p_49507_.getValue(PART);
            if (bedpart == BedPart.FOOT) {
                BlockPos blockpos = p_49506_.relative(getDirectionToOther(bedpart, (Direction)p_49507_.getValue(FACING)));
                BlockState blockstate = p_49505_.getBlockState(blockpos);
                if (blockstate.is(this) && blockstate.getValue(PART) == BedPart.HEAD) {
                    p_49505_.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 35);
                    p_49505_.levelEvent(p_49508_, 2001, blockpos, Block.getId(blockstate));
                }
            }
        }

        super.playerWillDestroy(p_49505_, p_49506_, p_49507_, p_49508_);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext p_49479_) {
        Direction direction = p_49479_.getHorizontalDirection();
        BlockPos blockpos = p_49479_.getClickedPos();
        BlockPos blockpos1 = blockpos.relative(direction);
        Level level = p_49479_.getLevel();
        return level.getBlockState(blockpos1).canBeReplaced(p_49479_) && level.getWorldBorder().isWithinBounds(blockpos1) ? (BlockState)this.defaultBlockState().setValue(FACING, direction) : null;
    }

    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        if (!worldIn.isClientSide) {
            BlockPos facingPos = pos.relative((Direction)state.getValue(FACING));
            worldIn.setBlock(facingPos, (BlockState)state.setValue(PART, BedPart.FOOT), 3);
            worldIn.blockUpdated(pos, Blocks.AIR);
            state.updateNeighbourShapes(worldIn, pos, 3);
        }

    }

    public static DoubleBlockCombiner.BlockType getBlockType(BlockState state) {
        BedPart bedpart = (BedPart)state.getValue(PART);
        return bedpart == BedPart.FOOT ? BlockType.FIRST : BlockType.SECOND;
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        int servings = (Integer)state.getValue(SERVINGS);
        ItemStack heldStack = player.getItemInHand(handIn);
        if (servings > 9) {
            if (heldStack.is(ModTags.KNIVES)) {
                return this.cutEar(level, pos, state);
            }

            player.displayClientMessage(MNDTextUtils.getTranslation("block.feast.use_knife", new Object[0]), true);
        }

        if (servings < 10) {
            if (heldStack.is(Items.BOWL)) {
                if (servings == 9) {
                    return this.takeServing(level, pos, state, player, handIn, (Item)MNDItems.PLATE_OF_STUFFED_HOGLIN_SNOUT.get());
                }

                if (servings > 4 && servings < 9) {
                    return this.takeServing(level, pos, state, player, handIn, (Item)MNDItems.PLATE_OF_STUFFED_HOGLIN_HAM.get());
                }

                if (servings > 0 && servings < 5) {
                    return this.takeServing(level, pos, state, player, handIn, (Item)MNDItems.PLATE_OF_STUFFED_HOGLIN.get());
                }
            }

            if (servings == 0) {
                level.playSound((Player)null, pos, SoundEvents.WOOD_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
                level.destroyBlock(pos, true);
            } else {
                player.displayClientMessage(TextUtils.getTranslation("block.feast.use_container", new Object[]{(new ItemStack(Items.BOWL)).getHoverName()}), true);
            }
        }

        return InteractionResult.SUCCESS;
    }

    protected InteractionResult cutEar(Level level, BlockPos pos, BlockState state) {
        int servings = (Integer)state.getValue(SERVINGS);
        BedPart part = (BedPart)state.getValue(PART);
        BlockPos pairPos = pos.relative(getDirectionToOther(part, (Direction)state.getValue(FACING)));
        BlockState pairState = level.getBlockState(pairPos);
        level.setBlock(pairPos, (BlockState)pairState.setValue(SERVINGS, servings - 1), 3);
        level.setBlock(pos, (BlockState)state.setValue(SERVINGS, servings - 1), 3);
        Containers.dropItemStack(level, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), new ItemStack((ItemLike)MNDItems.ROAST_EAR.get()));
        level.playSound((Player)null, pos, (SoundEvent)ModSounds.BLOCK_CUTTING_BOARD_KNIFE.get(), SoundSource.PLAYERS, 0.8F, 0.8F);
        return InteractionResult.SUCCESS;
    }

    protected InteractionResult takeServing(Level level, BlockPos pos, BlockState state, Player player, InteractionHand handIn, Item serving) {
        int servings = (Integer)state.getValue(SERVINGS);
        BedPart part = (BedPart)state.getValue(PART);
        BlockPos pairPos = pos.relative(getDirectionToOther(part, (Direction)state.getValue(FACING)));
        BlockState pairState = level.getBlockState(pairPos);
        ItemStack heldItem = player.getItemInHand(handIn);
        level.setBlock(pairPos, (BlockState)pairState.setValue(SERVINGS, servings - 1), 3);
        level.setBlock(pos, (BlockState)state.setValue(SERVINGS, servings - 1), 3);
        if (!player.isCreative()) {
            heldItem.shrink(1);
        }

        if (!player.getInventory().add(new ItemStack(serving))) {
            player.drop(new ItemStack(serving), false);
        }

        level.playSound((Player)null, pos, SoundEvents.ARMOR_EQUIP_GENERIC, SoundSource.PLAYERS, 1.0F, 1.0F);
        return InteractionResult.SUCCESS;
    }

    static {
        PART = BlockStateProperties.BED_PART;
        SERVINGS = IntegerProperty.create("servings", 0, 11);
        SHAPES_NORTH_HEAD = new VoxelShape[]{Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(1.0, 2.0, 0.0, 15.0, 9.0, 7.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(1.0, 2.0, 0.0, 15.0, 9.0, 7.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(1.0, 2.0, 0.0, 15.0, 9.0, 7.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(1.0, 2.0, 0.0, 15.0, 9.0, 7.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 0.0, 15.0, 9.0, 7.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 0.0, 16.0, 9.0, 7.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 0.0, 16.0, 10.0, 7.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 0.0, 16.0, 10.0, 7.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 0.0, 16.0, 10.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 0.0, 16.0, 10.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 0.0, 16.0, 10.0, 16.0))};
        SHAPES_NORTH_FOOT = new VoxelShape[]{Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(1.0, 2.0, 2.0, 15.0, 9.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(1.0, 2.0, 2.0, 15.0, 9.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(1.0, 2.0, 2.0, 15.0, 9.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(1.0, 2.0, 2.0, 15.0, 9.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 1.0, 15.0, 9.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 1.0, 16.0, 9.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 1.0, 16.0, 10.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 1.0, 16.0, 10.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 1.0, 16.0, 10.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 1.0, 16.0, 10.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 1.0, 16.0, 10.0, 16.0))};
        SHAPES_SOUTH_HEAD = new VoxelShape[]{Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(1.0, 2.0, 9.0, 15.0, 9.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(1.0, 2.0, 9.0, 15.0, 9.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(1.0, 2.0, 9.0, 15.0, 9.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(1.0, 2.0, 9.0, 15.0, 9.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(1.0, 2.0, 9.0, 16.0, 9.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 9.0, 16.0, 9.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 9.0, 16.0, 10.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 9.0, 16.0, 10.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 0.0, 16.0, 10.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 0.0, 16.0, 10.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 0.0, 16.0, 10.0, 16.0))};
        SHAPES_SOUTH_FOOT = new VoxelShape[]{Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(1.0, 2.0, 0.0, 15.0, 9.0, 14.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(1.0, 2.0, 0.0, 15.0, 9.0, 14.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(1.0, 2.0, 0.0, 15.0, 9.0, 14.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(1.0, 2.0, 0.0, 15.0, 9.0, 14.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(1.0, 2.0, 0.0, 16.0, 9.0, 15.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 0.0, 16.0, 9.0, 15.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 0.0, 16.0, 10.0, 15.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 0.0, 16.0, 10.0, 15.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 0.0, 16.0, 10.0, 15.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 0.0, 16.0, 10.0, 15.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 0.0, 16.0, 10.0, 15.0))};
        SHAPES_WEST_HEAD = new VoxelShape[]{Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 1.0, 7.0, 9.0, 15.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 1.0, 7.0, 9.0, 15.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 1.0, 7.0, 9.0, 15.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 1.0, 7.0, 9.0, 15.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 1.0, 7.0, 9.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 0.0, 7.0, 9.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 0.0, 7.0, 10.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 0.0, 7.0, 10.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 0.0, 16.0, 10.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 0.0, 16.0, 10.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 0.0, 16.0, 10.0, 16.0))};
        SHAPES_WEST_FOOT = new VoxelShape[]{Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(2.0, 2.0, 1.0, 16.0, 9.0, 15.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(2.0, 2.0, 1.0, 16.0, 9.0, 15.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(2.0, 2.0, 1.0, 16.0, 9.0, 15.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(2.0, 2.0, 1.0, 16.0, 9.0, 15.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(1.0, 2.0, 1.0, 16.0, 9.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(1.0, 2.0, 0.0, 16.0, 9.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(1.0, 2.0, 0.0, 16.0, 10.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(1.0, 2.0, 0.0, 16.0, 10.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(1.0, 2.0, 0.0, 16.0, 10.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(1.0, 2.0, 0.0, 16.0, 10.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(1.0, 2.0, 0.0, 16.0, 10.0, 16.0))};
        SHAPES_EAST_HEAD = new VoxelShape[]{Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(9.0, 2.0, 1.0, 16.0, 9.0, 15.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(9.0, 2.0, 1.0, 16.0, 9.0, 15.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(9.0, 2.0, 1.0, 16.0, 9.0, 15.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(9.0, 2.0, 1.0, 16.0, 9.0, 15.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(9.0, 2.0, 0.0, 16.0, 9.0, 15.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(9.0, 2.0, 0.0, 16.0, 9.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(9.0, 2.0, 0.0, 16.0, 10.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(9.0, 2.0, 0.0, 16.0, 10.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 0.0, 16.0, 10.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 0.0, 16.0, 10.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 0.0, 16.0, 10.0, 16.0))};
        SHAPES_EAST_FOOT = new VoxelShape[]{Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 1.0, 14.0, 9.0, 15.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 1.0, 14.0, 9.0, 15.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 1.0, 14.0, 9.0, 15.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 1.0, 14.0, 9.0, 15.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 0.0, 15.0, 9.0, 15.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 0.0, 15.0, 9.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 0.0, 15.0, 10.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 0.0, 15.0, 10.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 0.0, 15.0, 10.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 0.0, 15.0, 10.0, 16.0)), Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 0.0, 15.0, 10.0, 16.0))};
    }
}
