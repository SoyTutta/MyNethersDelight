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
            consumer.setRemainingFireTicks(30);
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
            int remainingDuration = effectInstance.getDuration();
            int fireResistanceDuration = remainingDuration / 5;
            int pungentDuration = fireResistanceDuration / 2;
            MobEffect effect = effectInstance.getEffect();

            if (consumer.removeEffect(effect)) {
                consumer.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE,
                        fireResistanceDuration > 600 ? fireResistanceDuration * 3 : 400));
                consumer.addEffect(new MobEffectInstance(MNDEffects.GPUNGENT.get(),
                        pungentDuration > 400 ? pungentDuration * 3 : 600, 2, false, false, true));
                removedEffects = true;
            }
        }

        if (removedEffects) {
            level.playSound(null, consumer.blockPosition(), SoundEvents.LAVA_EXTINGUISH, consumer.getSoundSource(), 1.0F, 1.0F);
        }
    }
}
