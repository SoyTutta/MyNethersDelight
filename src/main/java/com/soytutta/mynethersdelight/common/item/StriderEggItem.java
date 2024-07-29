//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.common.item;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.EffectCures;
import vectorwing.farmersdelight.common.item.DrinkableItem;
import vectorwing.farmersdelight.common.registry.ModEffects;

public class StriderEggItem extends DrinkableItem {

    public StriderEggItem(Item.Properties properties) {
        super(properties, false, true);
    }

    public void affectConsumer(ItemStack stack, Level level, LivingEntity consumer) {
        Iterator<MobEffectInstance> itr = consumer.getActiveEffects().iterator();
        ArrayList<MobEffectInstance> harmfulEffects = new ArrayList<>();

        while (itr.hasNext()) {
            MobEffectInstance selectedEffect = itr.next();
            if ((selectedEffect.getEffect().value()).getCategory().equals(MobEffectCategory.HARMFUL) && selectedEffect.getCures().contains(EffectCures.MILK)) {
                harmfulEffects.add(selectedEffect);
            }
        }

        if (!harmfulEffects.isEmpty()) {
            MobEffectInstance selectedEffect = harmfulEffects.get(level.random.nextInt(harmfulEffects.size()));
            Holder<MobEffect> effect = selectedEffect.getEffect();
            consumer.removeEffect(effect);
            int remainingDuration = selectedEffect.getDuration();
            int nourishDuration = remainingDuration / 10;
            if (nourishDuration > 0) {
                MobEffectInstance regenerationEffect = new MobEffectInstance(ModEffects.NOURISHMENT, nourishDuration * 3, 0);
                consumer.addEffect(regenerationEffect);
            }
        }
    }

    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 45;
    }

    public SoundEvent getDrinkingSound() {
        return SoundEvents.HONEY_DRINK;
    }
}