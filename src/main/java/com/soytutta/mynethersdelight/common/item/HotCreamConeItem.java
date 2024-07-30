package com.soytutta.mynethersdelight.common.item;

import java.util.Iterator;

import com.soytutta.mynethersdelight.common.registry.MNDEffects;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.EffectCures;
import vectorwing.farmersdelight.common.item.ConsumableItem;

public class HotCreamConeItem extends ConsumableItem {

    public HotCreamConeItem(Properties properties) {
        super(properties, false, true);
    }

    public SoundEvent getEatingSound() {
        return SoundEvents.HONEY_DRINK;
    }

    public void affectConsumer(ItemStack stack, Level level, LivingEntity consumer) {
        boolean removedEffect = false;

        if (!consumer.fireImmune()) {
            consumer.setRemainingFireTicks(consumer.getRemainingFireTicks() + 1);
            consumer.setRemainingFireTicks(10);
        }

        Iterator<MobEffectInstance> iterator = consumer.getActiveEffects().iterator();

        while (iterator.hasNext() && !removedEffect) {
            MobEffectInstance effectInstance = iterator.next();
            if (effectInstance.getCures().contains(EffectCures.MILK)) {
                int remainingDuration = effectInstance.getDuration();
                int fireResistanceDuration = (remainingDuration / 5);
                int pungentDuration = (fireResistanceDuration / 2);

                if (fireResistanceDuration > 200 ) {
                    consumer.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, fireResistanceDuration * 3));
                } else {
                    consumer.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 400));
                } if (pungentDuration > 200) {
                    consumer.addEffect(new MobEffectInstance(MNDEffects.GPUNGENT, pungentDuration * 3, 2));
                } else {
                    consumer.addEffect(new MobEffectInstance(MNDEffects.GPUNGENT, 600, 2));
                }

                consumer.removeEffect(effectInstance.getEffect());
                removedEffect = true;
            }
        }

        if (removedEffect) {
            level.playSound(null, consumer.blockPosition(), SoundEvents.LAVA_EXTINGUISH, consumer.getSoundSource(), 1.0F, 1.0F);
        }
    }
}
