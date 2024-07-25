//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.common.block;

import java.util.Optional;
import javax.annotation.Nullable;

import com.soytutta.mynethersdelight.common.block.entity.NetherStoveBlockEntity;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import com.soytutta.mynethersdelight.common.tag.MNDTags;
import com.soytutta.mynethersdelight.common.registry.MNDBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
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
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ToolActions;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.MathUtils;

import static vectorwing.farmersdelight.common.block.StoveBlock.STOVE_DAMAGE;

public class NetherStoveBlock extends BaseEntityBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;;
    public static final BooleanProperty SOUL = BooleanProperty.create("soul");
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public NetherStoveBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, false));
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldStack = player.getItemInHand(hand);
        Item heldItem = heldStack.getItem();

        if (state.getValue(LIT)) {
            if (heldStack.canPerformAction(ToolActions.SHOVEL_DIG)) {
                this.extinguish(state, level, pos);
                heldStack.hurtAndBreak(1, player, (action) -> {
                    action.broadcastBreakEvent(hand);
                });
                return InteractionResult.SUCCESS;
            }

            if (heldItem == Items.WATER_BUCKET) {
                if (!level.isClientSide()) {
                    level.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }

                this.extinguish(state, level, pos);
                if (!player.isCreative()) {
                    player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                }

                return InteractionResult.SUCCESS;
            }
        } else {
            if (heldItem instanceof FlintAndSteelItem) {
                level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, MathUtils.RAND.nextFloat() * 0.4F + 0.8F);
                level.setBlock(pos, state.setValue(BlockStateProperties.LIT, Boolean.TRUE), 11);
                heldStack.hurtAndBreak(1, player, (action) -> {
                    action.broadcastBreakEvent(hand);
                });
                return InteractionResult.SUCCESS;
            }

            if (heldItem instanceof FireChargeItem) {
                level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, (MathUtils.RAND.nextFloat() - MathUtils.RAND.nextFloat()) * 0.2F + 1.0F);
                level.setBlock(pos, state.setValue(BlockStateProperties.LIT, Boolean.TRUE), 11);
                if (!player.isCreative()) {
                    heldStack.shrink(1);
                }

                return InteractionResult.SUCCESS;
            }
        }
        if (state.getValue(LIT)) {
            BlockPos frontPos = pos.relative(state.getValue(FACING));
            boolean isNetherStove = !state.getValue(SOUL);
            boolean isSoulNetherStove = state.getValue(SOUL);

            if ((isNetherStove && heldStack.is(MNDTags.STOVE_SOUL_FUEL)) || (isSoulNetherStove && heldStack.is(MNDTags.STOVE_FIRE_FUEL))) {
                if (!player.isCreative()) {
                    player.getItemInHand(hand).shrink(1);
                }
                SoundEvent sound1, sound2;
                ParticleType<?> particle1, particle2;

                if (isNetherStove) {
                    sound1 = SoundEvents.SOUL_ESCAPE;
                    sound2 = SoundEvents.GHAST_HURT;
                    particle1 = ParticleTypes.SOUL;
                    particle2 = ParticleTypes.SOUL_FIRE_FLAME;
                    level.setBlock(pos, state.setValue(SOUL, Boolean.TRUE), 11);
                } else {
                    sound1 = SoundEvents.GENERIC_EXPLODE;
                    sound2 = SoundEvents.BLAZE_SHOOT;
                    particle1 = ParticleTypes.LAVA;
                    particle2 = ParticleTypes.FLAME;
                    level.setBlock(pos, state.setValue(SOUL, Boolean.FALSE), 11);
                }

                level.playSound(null, pos, sound1, SoundSource.BLOCKS, isNetherStove ? 1.5F : 1.0F, isNetherStove ? 0.5F : 1.5F);
                level.playSound(null, pos, sound2, SoundSource.BLOCKS, isNetherStove ? 1.0F : 1.5F, isNetherStove ? 0.5F : 1.0F);

                for (int i = 0; i < 3; ++i)
                    level.addParticle((ParticleOptions) particle1, frontPos.getX() + 0.5, frontPos.getY() + 0.5, frontPos.getZ() + 0.5, MathUtils.RAND.nextGaussian() * 0.05, MathUtils.RAND.nextGaussian() * 0.05, MathUtils.RAND.nextGaussian() * 0.05);

                for (int i = 0; i < 4; ++i)
                    level.addParticle((ParticleOptions) particle2, frontPos.getX() + 0.5, frontPos.getY() + 0.5, frontPos.getZ() + 0.5, MathUtils.RAND.nextGaussian() * 0.02, MathUtils.RAND.nextGaussian() * 0.02, MathUtils.RAND.nextGaussian() * 0.02);

                return InteractionResult.SUCCESS;
            }
        }

        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof NetherStoveBlockEntity stoveEntity) {
            int stoveSlot = stoveEntity.getNextEmptySlot();
            if (stoveSlot < 0 || stoveEntity.isStoveBlockedAbove()) {
                return InteractionResult.PASS;
            }

            Optional<CampfireCookingRecipe> recipe = stoveEntity.getMatchingRecipe(new SimpleContainer(heldStack), stoveSlot);
            if (recipe.isPresent()) {
                if (!level.isClientSide && stoveEntity.addItem(player.getAbilities().instabuild ? heldStack.copy() : heldStack, recipe.get(), stoveSlot)) {
                    return InteractionResult.SUCCESS;
                }

                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        boolean isNetherStove = !state.getValue(SOUL);
        if (isNetherStove) {
            return new ItemStack(MNDItems.NETHER_STOVE.get());
        } else { return new ItemStack(MNDItems.SOUL_NETHER_STOVE.get()); }
    }

    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    public void extinguish(BlockState state, Level level, BlockPos pos) {
        level.setBlock(pos, state.setValue(LIT, false), 2);
        double x = (double)pos.getX() + 0.5;
        double y = (double)pos.getY();
        double z = (double)pos.getZ() + 0.5;
        level.playLocalSound(x, y, z, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F, false);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(LIT, true);
    }

    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        boolean isLit = level.getBlockState(pos).getValue(LIT);
        boolean isSoul = level.getBlockState(pos).getValue(SOUL);

        if (isLit && !entity.fireImmune() && entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)entity)) {
            if (isSoul){ entity.hurt(STOVE_DAMAGE, 2.0F); } else { entity.hurt(STOVE_DAMAGE, 1.0F); }
        }
        super.stepOn(level, pos, state, entity);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity tileEntity = worldIn.getBlockEntity(pos);
            if (tileEntity instanceof NetherStoveBlockEntity) {
                ItemUtils.dropItems(worldIn, pos, ((NetherStoveBlockEntity)tileEntity).getInventory());
            }
            super.onRemove(state, worldIn, pos, newState, isMoving);
        }

    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIT,SOUL, FACING);
    }

    public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand) {
        if (stateIn.getValue(CampfireBlock.LIT)) {
            double x = (double)pos.getX() + 0.5;
            double y = (double)pos.getY();
            double z = (double)pos.getZ() + 0.5;
            if (rand.nextInt(10) == 0) {
                level.playLocalSound(x, y, z, ModSounds.BLOCK_STOVE_CRACKLE.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }

            Direction direction = stateIn.getValue(HorizontalDirectionalBlock.FACING);
            Direction.Axis direction$axis = direction.getAxis();
            double horizontalOffset = rand.nextDouble() * 0.6 - 0.3;
            double xOffset = direction$axis == Axis.X ? (double)direction.getStepX() * 0.52 : horizontalOffset;
            double yOffset = rand.nextDouble() * 6.0 / 16.0;
            double zOffset = direction$axis == Axis.Z ? (double)direction.getStepZ() * 0.52 : horizontalOffset;

            if (stateIn.getValue(SOUL)) {
                level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, x + xOffset, y + yOffset, z + zOffset, 0.0, 0.0, 0.0);
            } else {
                level.addParticle(ParticleTypes.FLAME, x + xOffset, y + yOffset, z + zOffset, 0.0, 0.0, 0.0);
            }
            level.addParticle(ParticleTypes.SMOKE, x + xOffset, y + yOffset, z + zOffset, 0.0, 0.0, 0.0);
        }
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return (MNDBlockEntityTypes.NETHER_STOVE.get()).create(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return state.getValue(LIT)
                ? createTickerHelper(blockEntityType, MNDBlockEntityTypes.NETHER_STOVE.get(), level.isClientSide
                    ? NetherStoveBlockEntity::animationTick
                    : NetherStoveBlockEntity::cookingTick)
                : null;
    }

    @Nullable
    public BlockPathTypes getBlockPathType(BlockState state, BlockGetter world, BlockPos pos, @Nullable Mob entity) {
        return state.getValue(LIT) ? BlockPathTypes.DAMAGE_FIRE : null;
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState rotate(BlockState pState, Rotation pRot) {
        return pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }
}
