package com.soytutta.mynethersdelight.common.entity.ia;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import com.soytutta.mynethersdelight.common.block.MagmaCakeBlock;
import com.soytutta.mynethersdelight.common.registry.MNDItems;

import java.util.EnumSet;
import java.util.List;


public class EatMagmaCakeGoal extends Goal {
    private static final int COOLDOWN_TICKS = 100;
    private static final int TONGUE_ANIMATION_DURATION = 10;
    private static final SoundEvent TONGUE_SOUND = SoundEvents.FROG_EAT;

    private final Frog frog;
    private final Level level;
    private Object targetPos;
    private int eatAnimationTick;
    private int tongueAnimationTick;
    private int cooldownTick;

    public EatMagmaCakeGoal(Frog frog) {
        this.frog = frog;
        this.level = frog.level();
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
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
        this.frog.setPose(Pose.STANDING);
    }

    @Override
    public boolean canContinueToUse() {
        return this.targetPos != null;
    }

    @Override
    public void tick() {
        if (this.cooldownTick > 0) {
            this.cooldownTick--;
            return;
        }

        updateTarget();
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
        List<BlockPos> nearbyCakes = findNearbyCakes();
        double minDistanceSq = Double.MAX_VALUE;
        Object closestTarget = null;
        BlockPos frogPos = this.frog.blockPosition();

        for (BlockPos cakePos : nearbyCakes) {
            double distanceSq = frogPos.distSqr(cakePos);
            if (distanceSq < minDistanceSq) {
                minDistanceSq = distanceSq;
                closestTarget = cakePos;
            }
        }

        Player player = this.level.getNearestPlayer(frog, 6.0);
        if (player != null && player.distanceToSqr(this.frog) <= 6.0 * 6.0) {
            ItemStack mainHand = player.getMainHandItem();
            ItemStack offHand = player.getOffhandItem();
            if (mainHand.getItem() == MNDItems.MAGMA_CAKE.get() ||
                    mainHand.getItem() == MNDItems.MAGMA_CAKE_SLICE.get() ||
                    offHand.getItem() == MNDItems.MAGMA_CAKE.get() ||
                    offHand.getItem() == MNDItems.MAGMA_CAKE_SLICE.get()) {
                double playerDistanceSq = frogPos.distSqr(player.blockPosition());
                if (playerDistanceSq < minDistanceSq) {
                    minDistanceSq = playerDistanceSq;
                    closestTarget = player;
                }
            }
        }

        this.targetPos = closestTarget;
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
                for (int i = 0; i < 3; i++) {
                    MagmaCube magmacube = EntityType.MAGMA_CUBE.create(this.level);
                    if (magmacube != null) {
                        magmacube.setSize(1, true);
                        magmacube.setHealth(1);
                        magmacube.setInvisible(true);
                        magmacube.setPos(this.frog.getX(), this.frog.getY(), this.frog.getZ());
                        this.level.addFreshEntity(magmacube);

                        this.frog.doHurtTarget(magmacube);
                        if (!magmacube.isAlive()) {
                            magmacube.remove(Entity.RemovalReason.KILLED);
                        }
                    }
                }

                this.frog.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0));

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
            ItemStack mainHand = player.getMainHandItem();
            ItemStack offHand = player.getOffhandItem();
            if (mainHand.getItem() == MNDItems.MAGMA_CAKE_SLICE.get()
                    || offHand.getItem() == MNDItems.MAGMA_CAKE_SLICE.get()) {
                this.eatAnimationTick++;
                if (this.eatAnimationTick == 1) {
                    this.level.playSound(null, this.frog, TONGUE_SOUND, SoundSource.NEUTRAL, 2.0F, 1.0F);
                    this.frog.setPose(Pose.USING_TONGUE);
                    this.frog.getLookControl().setLookAt(entity.getX(), entity.getY(), entity.getZ());

                    if (mainHand.getItem() == MNDItems.MAGMA_CAKE_SLICE.get()) {
                        mainHand.shrink(1);
                    } else if (offHand.getItem() == MNDItems.MAGMA_CAKE_SLICE.get()) {
                        offHand.shrink(1);
                    }

                    for (int i = 0; i < 3; i++) {
                        MagmaCube magmacube = EntityType.MAGMA_CUBE.create(this.level);
                        if (magmacube != null) {
                            magmacube.setSize(1, true);
                            magmacube.setHealth(1);
                            magmacube.setInvisible(true);
                            magmacube.setPos(this.frog.getX(), this.frog.getY(), this.frog.getZ());
                            this.level.addFreshEntity(magmacube);

                            this.frog.doHurtTarget(magmacube);
                            if (!magmacube.isAlive()) {
                                magmacube.remove(Entity.RemovalReason.KILLED);
                            }
                        }
                    }

                    this.frog.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0));
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

    private List<BlockPos> findNearbyCakes() {
        List<BlockPos> cakePositions = Lists.newArrayList();
        BlockPos frogPos = this.frog.blockPosition();
        int searchRadius = 10;
        for (int x = -searchRadius; x <= searchRadius; ++x) {
            for (int y = -searchRadius; y <= searchRadius; ++y) {
                for (int z = -searchRadius; z <= searchRadius; ++z) {
                    BlockPos pos = frogPos.offset(x, y, z);
                    if (this.level.getBlockState(pos).getBlock() instanceof MagmaCakeBlock) {
                        cakePositions.add(pos);
                    }
                }
            }
        }
        return cakePositions;
    }
}
