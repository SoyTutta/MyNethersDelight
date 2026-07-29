package com.soytutta.mynethersdelight.common.effect;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class GoodPungentEffect extends AbstractPungentEffect {

    public GoodPungentEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFD700);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        switchEffect(entity);

        if (isInFireCondition(entity) || entity.isInLava() || entity.isOnFire()) {
            if (entity.getHealth() < entity.getMaxHealth()) {
                entity.heal(2.0F);

                if (!entity.level().isClientSide) {
                    double width = entity.getBbWidth();
                    double height = entity.getBbHeight();
                    double x = entity.getX() + (entity.getRandom().nextDouble() - 0.5) * width;
                    double y = entity.getY() + entity.getRandom().nextDouble() * height;
                    double z = entity.getZ() + (entity.getRandom().nextDouble() - 0.5) * width;
                    ((ServerLevel) entity.level()).sendParticles(ParticleTypes.FLAME, x, y, z, 1, 0, 0, 0, 0);
                }
            } else {
                entity.setRemainingFireTicks(0);
                entity.clearFire();
            }
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        int i = 40 >> amplifier;
        if (i > 0) {
            return duration % i == 0;
        } else {
            return true;
        }
    }
}
