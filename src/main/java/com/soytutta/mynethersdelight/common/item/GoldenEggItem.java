package com.soytutta.mynethersdelight.common.item;

import com.soytutta.mynethersdelight.common.registry.MNDItems;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.item.ConsumableItem;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



public class GoldenEggItem extends ConsumableItem {

    public GoldenEggItem(Item.Properties properties) {
        super(properties, false, true);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.is(MNDItems.ENCHANTED_GOLDEN_EGG.get());
    }

    @Override
    public void affectConsumer(ItemStack stack, Level level, LivingEntity consumer) {
        Iterator<MobEffectInstance> iterator = consumer.getActiveEffects().iterator();
        List<MobEffectInstance> effectsToRemove = new ArrayList<>();

        while (iterator.hasNext()) {
            MobEffectInstance effectInstance = iterator.next();
            if (!effectInstance.getEffect().isBeneficial()) {
                effectsToRemove.add(effectInstance);
            }
        }

        boolean removedEffects = false;

        for (MobEffectInstance effectInstance : effectsToRemove) {
            int remainingDuration = effectInstance.getDuration();

            int nourishmentDuration = remainingDuration / 2;

            int nourishmentMaxDuration = 0;
            if (stack.is(MNDItems.GOLDEN_EGG.get())) {
                nourishmentMaxDuration = 3000;
            } else if (stack.is(MNDItems.ENCHANTED_GOLDEN_EGG.get())) {
                nourishmentMaxDuration = 6000;
            }
            nourishmentDuration = Math.min(Math.max(nourishmentDuration, 100), nourishmentMaxDuration);

            consumer.addEffect(new MobEffectInstance(ModEffects.NOURISHMENT.get(), nourishmentDuration, 0, false, false));
            consumer.removeEffect(effectInstance.getEffect());
            removedEffects = true;
        }

        if (removedEffects) {
            level.playSound(null, consumer.blockPosition(), SoundEvents.CHISELED_BOOKSHELF_INSERT_ENCHANTED, consumer.getSoundSource(), 1.0F, 1.0F);
        }
    }
}
