package com.soytutta.mynethersdelight.common.item;

import com.soytutta.mynethersdelight.common.registry.MNDEffects;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.item.DrinkableItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HotCreamItem extends DrinkableItem {

    public HotCreamItem(Item.Properties properties) {
        super(properties, false, true);
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return SoundEvents.HONEY_DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 60;
    }

    @Override
    public void affectConsumer(ItemStack stack, Level level, LivingEntity consumer) {
        boolean removedEffects = false;

        if (!consumer.fireImmune()) {
            consumer.setRemainingFireTicks(consumer.getRemainingFireTicks() + 2);
            consumer.setSecondsOnFire(30);
        }

        Iterator<MobEffectInstance> iterator = consumer.getActiveEffects().iterator();
        List<MobEffectInstance> effectsToRemove = new ArrayList<>();

        while (iterator.hasNext()) {
            MobEffectInstance effectInstance = iterator.next();
            if (effectInstance.isCurativeItem(new ItemStack(Items.MILK_BUCKET))) {
                effectsToRemove.add(effectInstance);
            }
        }

        for (MobEffectInstance effectInstance : effectsToRemove) {
            MobEffect effect = effectInstance.getEffect();
            consumer.removeEffect(effect);
        }

        for (MobEffectInstance effectInstance : effectsToRemove) {
            int remainingDuration = effectInstance.getDuration();
            int fireResistanceSeconds = remainingDuration / 5;
            int purgentSeconds = fireResistanceSeconds / 2;

            if (fireResistanceSeconds > 0) {
                consumer.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, fireResistanceSeconds * 3));
            }
            if (purgentSeconds > 0) {
                consumer.addEffect(new MobEffectInstance(MNDEffects.GPUNGENT.get(), purgentSeconds * 3,2));
            }
            removedEffects = true;
        }

        if (removedEffects) {
            level.playSound(null, consumer.blockPosition(), SoundEvents.LAVA_EXTINGUISH, consumer.getSoundSource(), 1.0F, 1.0F);
        }
    }
}
