package com.soytutta.mynethersdelight.common.entity.ia;

import com.soytutta.mynethersdelight.common.MNDConfiguration;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import com.soytutta.mynethersdelight.common.block.feasts.MagmaCakeBlock;

import java.util.EnumSet;

public class EatMagmaCakeGoal extends Goal {
    private static final int COOLDOWN_TICKS = 5;
    private static final int TARGET_SEARCH_COOLDOWN = 20;
    private static final int CAKE_SEARCH_RADIUS = 10;
    private static final double PLAYER_SEARCH_RANGE = 6.0;
    private static final int TONGUE_ANIMATION_DURATION = 10;
    private static final SoundEvent TONGUE_SOUND = SoundEvents.FROG_EAT;

    private final Frog frog;
    private final Level level;
    private Object targetPos;
    private int eatAnimationTick;
    private int tongueAnimationTick;
    private int cooldownTick;
    private int targetSearchCooldown;
    private InteractionHand offeredHand;

    public EatMagmaCakeGoal(Frog frog) {
        this.frog = frog;
        this.level = frog.level();
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (!MNDConfiguration.ENABLE_FROG_MAGMA_CAKE_BEHAVIOR.get()) {
            return false;
        }

        if (this.offeredHand != null) {
            if (isTargetValid()) {
                return true;
            }
            this.targetPos = null;
            this.offeredHand = null;
        }

        if (this.targetSearchCooldown > 0) {
            this.targetSearchCooldown--;
            return false;
        }

        this.targetSearchCooldown = TARGET_SEARCH_COOLDOWN + this.frog.getRandom().nextInt(TARGET_SEARCH_COOLDOWN);
        updateTarget();
        return this.targetPos != null;
    }

    @Override
    public void start() {
        moveToTarget();
    }

    @Override
    public void stop() {
        this.targetPos = null;
        this.eatAnimationTick = 0;
        this.tongueAnimationTick = 0;
        this.cooldownTick = COOLDOWN_TICKS;
        this.targetSearchCooldown = TARGET_SEARCH_COOLDOWN;
        this.offeredHand = null;
        this.frog.setPose(Pose.STANDING);
    }

    @Override
    public boolean canContinueToUse() {
        return MNDConfiguration.ENABLE_FROG_MAGMA_CAKE_BEHAVIOR.get()
                && (this.tongueAnimationTick > 0 && this.targetPos != null || isTargetValid());
    }

    @Override
    public void tick() {
        if (this.cooldownTick > 0) {
            this.cooldownTick--;
            return;
        }

        moveToTarget();

        if (this.targetPos instanceof BlockPos) {
            BlockPos pos = (BlockPos) this.targetPos;
            double distanceSq = this.frog.distanceToSqr(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            if (distanceSq <= 1.75 * 1.75) {
                handleBlockInteraction(pos);
            }
        } else if (this.targetPos instanceof Entity) {
            Entity entity = (Entity) this.targetPos;
            double distanceSq = this.frog.distanceToSqr(entity.getX(), entity.getY(), entity.getZ());
            if (distanceSq <= 1.75 * 1.75) {
                handleEntityInteraction(entity);
            }
        }
    }

    private void updateTarget() {
        this.offeredHand = null;
        double minDistanceSq = Double.MAX_VALUE;
        Object closestTarget = null;
        BlockPos frogPos = this.frog.blockPosition();

        BlockPos cakePos = findNearbyCake();
        if (cakePos != null) {
            double distanceSq = frogPos.distSqr(cakePos);
            minDistanceSq = distanceSq;
            closestTarget = cakePos;
        }

        Player player = this.level.getNearestPlayer(frog, PLAYER_SEARCH_RANGE);
        if (player != null && isOfferingMagmaCakeSlice(player)) {
            double playerDistanceSq = frogPos.distSqr(player.blockPosition());
            if (playerDistanceSq < minDistanceSq) {
                closestTarget = player;
            }
        }

        this.targetPos = closestTarget;
    }

    private boolean isTargetValid() {
        if (this.targetPos instanceof BlockPos pos) {
            return this.level.getBlockState(pos).getBlock() instanceof MagmaCakeBlock;
        }
        if (this.targetPos instanceof Player player) {
            return player.isAlive()
                    && player.distanceToSqr(this.frog) <= PLAYER_SEARCH_RANGE * PLAYER_SEARCH_RANGE
                    && (this.offeredHand == null
                    ? isOfferingMagmaCakeSlice(player)
                    : player.getItemInHand(this.offeredHand).is(MNDItems.MAGMA_CAKE_SLICE.get()));
        }
        return false;
    }

    private boolean isOfferingMagmaCakeSlice(Player player) {
        return player.getMainHandItem().is(MNDItems.MAGMA_CAKE_SLICE.get())
                || player.getOffhandItem().is(MNDItems.MAGMA_CAKE_SLICE.get());
    }

    private void moveToTarget() {
        if (targetPos instanceof BlockPos) {
            BlockPos pos = (BlockPos) targetPos;
            this.frog.getNavigation().moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 1.5);
        } else if (targetPos instanceof Entity) {
            Entity entity = (Entity) targetPos;
            this.frog.getNavigation().moveTo(entity.getX(), entity.getY(), entity.getZ(), 1.5);
        }
    }

    private void handleBlockInteraction(BlockPos pos) {
        this.frog.getNavigation().stop();
        this.frog.getLookControl().setLookAt(pos.getX(), pos.getY(), pos.getZ());
        this.eatAnimationTick++;

        if (this.eatAnimationTick == 1) {
            this.level.playSound(null, this.frog, TONGUE_SOUND, SoundSource.NEUTRAL, 2.0F, 1.0F);
            this.frog.setPose(Pose.USING_TONGUE);
            this.frog.getLookControl().setLookAt(pos.getX(), pos.getY(), pos.getZ());

            BlockState state = this.level.getBlockState(pos);
            if (state.getBlock() instanceof MagmaCakeBlock) {
                simulateMagmaCubeMeal();

                int bites = state.getValue(MagmaCakeBlock.BITES);
                if (bites < 6) {
                    this.level.setBlockAndUpdate(pos, state.setValue(MagmaCakeBlock.BITES, bites + 1));
                } else if (state.getValue(MagmaCakeBlock.SECOND_CAKE)){
                    this.level.setBlockAndUpdate(pos, state.setValue(MagmaCakeBlock.BITES, 0)
                            .setValue(MagmaCakeBlock.SECOND_CAKE, false));
                } else {
                    this.level.destroyBlock(pos, false);
                }
                this.cooldownTick = COOLDOWN_TICKS;
            }
        }

        if (this.tongueAnimationTick < TONGUE_ANIMATION_DURATION) {
            this.frog.getLookControl().setLookAt(pos.getX(), pos.getY(), pos.getZ());
            this.tongueAnimationTick++;
        } else {
            this.stop();
        }
    }

    private void handleEntityInteraction(Entity entity) {
        this.frog.getNavigation().stop();
        this.frog.getLookControl().setLookAt(entity.getX(), entity.getY(), entity.getZ());

        if (entity instanceof Player) {
            Player player = (Player) entity;
            ItemStack offeredSlice = getOfferedSlice(player);
            if (offeredSlice.is(MNDItems.MAGMA_CAKE_SLICE.get())) {
                this.eatAnimationTick++;
                if (this.eatAnimationTick == 1) {
                    this.level.playSound(null, this.frog, TONGUE_SOUND, SoundSource.NEUTRAL, 2.0F, 1.0F);
                    this.frog.setPose(Pose.USING_TONGUE);
                    this.frog.getLookControl().setLookAt(entity.getX(), entity.getY(), entity.getZ());

                    if (!player.isCreative()) {
                        offeredSlice.shrink(1);
                    }

                    simulateMagmaCubeMeal();
                }
            }
        }
        if (this.tongueAnimationTick < TONGUE_ANIMATION_DURATION) {
            this.frog.getLookControl().setLookAt(entity.getX(), entity.getY(), entity.getZ());
            this.tongueAnimationTick++;
        } else {
            this.stop();
        }
    }

    private BlockPos findNearbyCake() {
        return BlockPos.findClosestMatch(this.frog.blockPosition(), CAKE_SEARCH_RADIUS, CAKE_SEARCH_RADIUS,
                        pos -> this.level.getBlockState(pos).getBlock() instanceof MagmaCakeBlock)
                .map(BlockPos::immutable)
                .orElse(null);
    }

    public boolean requestFeeding(Player player, InteractionHand hand) {
        if (!MNDConfiguration.ENABLE_FROG_MAGMA_CAKE_BEHAVIOR.get()
                || !player.getItemInHand(hand).is(MNDItems.MAGMA_CAKE_SLICE.get())) {
            return false;
        }

        this.targetPos = player;
        this.offeredHand = hand;
        this.eatAnimationTick = 0;
        this.tongueAnimationTick = 0;
        this.cooldownTick = 0;
        this.targetSearchCooldown = 0;
        return true;
    }

    private ItemStack getOfferedSlice(Player player) {
        if (this.offeredHand != null) {
            return player.getItemInHand(this.offeredHand);
        }
        return player.getMainHandItem().is(MNDItems.MAGMA_CAKE_SLICE.get())
                ? player.getMainHandItem()
                : player.getOffhandItem();
    }

    private void simulateMagmaCubeMeal() {
        for (int i = 0; i < 3; i++) {
            MagmaCube magmaCube = EntityType.MAGMA_CUBE.create(this.level);
            if (magmaCube != null) {
                magmaCube.setSize(1, true);
                magmaCube.setHealth(1);
                magmaCube.setInvisible(true);
                magmaCube.setPos(this.frog.getX(), this.frog.getY(), this.frog.getZ());
                if (!this.level.addFreshEntity(magmaCube)) {
                    magmaCube.discard();
                    continue;
                }

                this.frog.doHurtTarget(magmaCube);
                if (magmaCube.isAlive()) {
                    magmaCube.discard();
                } else {
                    magmaCube.remove(Entity.RemovalReason.KILLED);
                }
            }
        }

        this.frog.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0));
    }
}
