package com.soytutta.mynethersdelight.common.item;

import java.util.ArrayList;
import java.util.Iterator;

import com.soytutta.mynethersdelight.common.registry.MNDEffects;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
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
        Iterator<MobEffectInstance> itr = consumer.getActiveEffects().iterator();
        ArrayList<Holder<MobEffect>> compatibleEffects = new ArrayList<>();

        if (!consumer.fireImmune()) {
            consumer.setRemainingFireTicks(consumer.getRemainingFireTicks() + 1);
            consumer.setRemainingFireTicks(10);
        }

        MobEffectInstance selectedEffect;
        while(itr.hasNext()) {
            selectedEffect = itr.next();
            if (selectedEffect.getCures().contains(EffectCures.MILK)) {
                compatibleEffects.add(selectedEffect.getEffect());
            }
        }

        if (!compatibleEffects.isEmpty()) {
            Holder<MobEffect> effectToRemove = compatibleEffects.get(level.random.nextInt(compatibleEffects.size()));
            selectedEffect = consumer.getEffect(effectToRemove);
            int purgentSeconds = selectedEffect.getDuration() / 15;
            int fireResistanceSeconds = purgentSeconds / 2;
            consumer.removeEffect(selectedEffect.getEffect());

            if (fireResistanceSeconds > 0) {
                consumer.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, fireResistanceSeconds * 3));
            }
            if (purgentSeconds > 0) {
                consumer.addEffect(new MobEffectInstance(MNDEffects.GPUNGENT, purgentSeconds * 3));
            }
        }
            level.playSound(null, consumer.blockPosition(), SoundEvents.LAVA_EXTINGUISH, consumer.getSoundSource(), 1.0F, 1.0F);
    }
}
