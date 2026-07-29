//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.common.item;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
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
            if (selectedEffect.getEffect().getCategory().equals(MobEffectCategory.HARMFUL) && selectedEffect.isCurativeItem(new ItemStack(Items.MILK_BUCKET))) {
                harmfulEffects.add(selectedEffect);
            }
        }

        if (!harmfulEffects.isEmpty()) {
            MobEffectInstance selectedEffect = harmfulEffects.get(level.random.nextInt(harmfulEffects.size()));
            int nourishDuration = selectedEffect.getDuration() / 2;
            MobEffect effect = selectedEffect.getEffect();
            if (consumer.removeEffect(effect)) {
                if (nourishDuration > 0) {
                    consumer.addEffect(new MobEffectInstance(ModEffects.NOURISHMENT.get(), nourishDuration, 0, false, false));
                    level.playSound(null, consumer.blockPosition(), SoundEvents.CHISELED_BOOKSHELF_INSERT_ENCHANTED,
                            consumer.getSoundSource(), 1.0F, 1.0F);
                }
            }
        }
    }


    public int getUseDuration(ItemStack stack) {
        return 45;
    }

    public SoundEvent getDrinkingSound() {
        return SoundEvents.HONEY_DRINK;
    }
}
