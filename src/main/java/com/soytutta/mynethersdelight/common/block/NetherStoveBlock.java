package com.soytutta.mynethersdelight.common.block;

import com.soytutta.mynethersdelight.common.block.entity.NetherStoveBlockEntity;
import com.soytutta.mynethersdelight.common.registry.MNDBlockEntityTypes;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import com.soytutta.mynethersdelight.common.tag.MNDTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import vectorwing.farmersdelight.common.block.AbstractStoveBlock;
import vectorwing.farmersdelight.common.block.entity.AbstractStoveBlockEntity;
import vectorwing.farmersdelight.common.registry.ModSounds;

public class NetherStoveBlock extends AbstractStoveBlock {
    public static final BooleanProperty SOUL = BooleanProperty.create("soul");

    public NetherStoveBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(LIT, false)
                .setValue(SOUL, false));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
                                 BlockHitResult hit) {
        InteractionResult base = super.use(state, level, pos, player, hand, hit);
        if (base.consumesAction()) {
            return base;
        }

        ItemStack heldStack = player.getItemInHand(hand);
        if (state.getValue(LIT)) {
            boolean soul = state.getValue(SOUL);
            if ((!soul && heldStack.is(MNDTags.STOVE_SOUL_FUEL))
                    || (soul && heldStack.is(MNDTags.STOVE_FIRE_FUEL))) {
                if (!player.getAbilities().instabuild) {
                    heldStack.shrink(1);
                }

                level.setBlock(pos, state.setValue(SOUL, !soul), 11);
                BlockPos front = pos.relative(state.getValue(FACING));
                if (!level.isClientSide) {
                    level.playSound(null, pos, soul ? SoundEvents.GENERIC_EXPLODE : SoundEvents.SOUL_ESCAPE,
                            SoundSource.BLOCKS, soul ? 1.0F : 1.5F, soul ? 1.5F : 0.5F);
                }

                ParticleOptions particle = soul ? ParticleTypes.FLAME : ParticleTypes.SOUL_FIRE_FLAME;
                for (int i = 0; i < 5; i++) {
                    level.addParticle(particle, front.getX() + 0.5, front.getY() + 0.5,
                            front.getZ() + 0.5, 0, 0.02, 0);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return new ItemStack(state.getValue(SOUL) ? MNDItems.SOUL_NETHER_STOVE.get() : MNDItems.NETHER_STOVE.get());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NetherStoveBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide
                ? createTickerHelper(type, MNDBlockEntityTypes.NETHER_STOVE.get(), NetherStoveBlockEntity::particleTick)
                : createTickerHelper(type, MNDBlockEntityTypes.NETHER_STOVE.get(), AbstractStoveBlockEntity::serverTick);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT)) {
            return;
        }

        double x = pos.getX() + 0.5;
        double y = pos.getY();
        double z = pos.getZ() + 0.5;
        if (random.nextInt(10) == 0) {
            level.playLocalSound(x, y, z, ModSounds.BLOCK_STOVE_CRACKLE.get(), SoundSource.BLOCKS,
                    1.0F, 1.0F, false);
        }

        ParticleOptions flame = state.getValue(SOUL) ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME;
        level.addParticle(flame, x, y + 0.2, z, 0, 0, 0);
        level.addParticle(ParticleTypes.SMOKE, x, y + 0.3, z, 0, 0, 0);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(SOUL);
    }
}
