package com.soytutta.mynethersdelight.common.effect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class PungentEffect extends AbstractPungentEffect {
    public PungentEffect() {
        super(MobEffectCategory.HARMFUL, 0x8B4513);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        switchEffect(entity);
        if (isInFireCondition(entity) || entity.isInLava() || entity.isOnFire()) {
            float minHealth = amplifier >= 2 ? 2.0F
                    : amplifier == 1 ? entity.getMaxHealth() / 2
                    : entity.getMaxHealth() - entity.getMaxHealth() / 4;
            if (entity.getHealth() > minHealth) {
                entity.hurt(entity.damageSources().magic(), 1.0F);
                entity.setRemainingFireTicks(10);
            } else if (entity.isOnFire()) {
                entity.setRemainingFireTicks(0);
                entity.clearFire();
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        int interval = 25 >> amplifier;
        return interval <= 0 || duration % interval == 0;
    }
}
