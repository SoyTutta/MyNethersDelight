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
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        switchEffect(entity);
        if (isInFireCondition(entity) || entity.isInLava() || entity.isOnFire()) {
            if (entity.getHealth() < entity.getMaxHealth()) {
                entity.heal(2.0F);
                if (!entity.level().isClientSide) {
                    double x = entity.getX() + (entity.getRandom().nextDouble() - 0.5) * entity.getBbWidth();
                    double y = entity.getY() + entity.getRandom().nextDouble() * entity.getBbHeight();
                    double z = entity.getZ() + (entity.getRandom().nextDouble() - 0.5) * entity.getBbWidth();
                    ((ServerLevel) entity.level()).sendParticles(ParticleTypes.FLAME, x, y, z, 1, 0, 0, 0, 0);
                }
            } else {
                entity.setRemainingFireTicks(0);
                entity.clearFire();
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        int interval = 40 >> amplifier;
        return interval <= 0 || duration % interval == 0;
    }
}
