//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.common.item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.soytutta.mynethersdelight.common.registry.MNDEffects;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
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

    @Override
    public void affectConsumer(ItemStack stack, Level level, LivingEntity consumer) {
        List<MobEffectInstance> harmfulEffects = new ArrayList<>();

        for (MobEffectInstance effectInstance : consumer.getActiveEffects()) {
            if ((effectInstance.getEffect().value()).getCategory().equals(MobEffectCategory.HARMFUL)
                    && effectInstance.getCures().contains(EffectCures.MILK)) {
            harmfulEffects.add(effectInstance);
            }
        }

        if (!harmfulEffects.isEmpty()) {
            MobEffectInstance selectedEffect = harmfulEffects.get(level.random.nextInt(harmfulEffects.size()));
            int remainingDuration = selectedEffect.getDuration();
            int nourishDuration = remainingDuration / 2;

            if (nourishDuration > 0) {
                consumer.addEffect(new MobEffectInstance(ModEffects.NOURISHMENT, nourishDuration, 0));
                level.playSound(null, consumer.blockPosition(), SoundEvents.CHISELED_BOOKSHELF_INSERT_ENCHANTED, consumer.getSoundSource(), 1.0F, 1.0F);
            }

            consumer.removeEffect(selectedEffect.getEffect());
        }
    }

    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 45;
    }

    public SoundEvent getDrinkingSound() {
        return SoundEvents.HONEY_DRINK;
    }
}