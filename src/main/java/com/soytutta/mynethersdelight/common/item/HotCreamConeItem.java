package com.soytutta.mynethersdelight.common.item;

import java.util.Iterator;

import com.soytutta.mynethersdelight.MyNethersDelight;
import com.soytutta.mynethersdelight.common.registry.MNDEffects;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.EffectCures;
import vectorwing.farmersdelight.common.item.ConsumableItem;

import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.ModParticleTypes;
import vectorwing.farmersdelight.common.utility.MathUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.List;

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

    public static final List<MobEffectInstance> EFFECTS = Lists.newArrayList(
            new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 3000, 1),
            new MobEffectInstance(MNDEffects.GPUNGENT, 3000, 0));

    @EventBusSubscriber(modid = MyNethersDelight.MODID, bus = EventBusSubscriber.Bus.GAME)
    public static class StriderFoodEvent
    {
        @SubscribeEvent
        @SuppressWarnings("unused")
        public static void onStriderFoodApplied(PlayerInteractEvent.EntityInteract event) {
            Player player = event.getEntity();
            Entity target = event.getTarget();
            ItemStack itemStack = event.getItemStack();

            if (target instanceof Strider strider) {
                if (strider.isAlive() && itemStack.getItem().equals(MNDItems.HOT_CREAM_CONE.get())) {
                    strider.setHealth(strider.getMaxHealth());
                    for (MobEffectInstance effect : EFFECTS) {
                        strider.addEffect(new MobEffectInstance(effect));
                    }
                    strider.level().playSound(null, target.blockPosition(), SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.8F, 0.8F);

                    for (int i = 0; i < 5; ++i) {
                        double xSpeed = MathUtils.RAND.nextGaussian() * 0.02D;
                        double ySpeed = MathUtils.RAND.nextGaussian() * 0.02D;
                        double zSpeed = MathUtils.RAND.nextGaussian() * 0.02D;
                        strider.level().addParticle(ModParticleTypes.STAR.get(), strider.getRandomX(1.0D), strider.getRandomY() + 0.5D, strider.getRandomZ(1.0D), xSpeed, ySpeed, zSpeed);
                    }

                    if (itemStack.getCraftingRemainingItem() != ItemStack.EMPTY && !player.isCreative()) {
                        player.addItem(itemStack.getCraftingRemainingItem());
                        itemStack.shrink(1);
                    }

                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag isAdvanced) {
        if (!Configuration.FOOD_EFFECT_TOOLTIP.get()) {
            return;
        }

        MutableComponent textWhenFeeding = TextUtils.getTranslation("tooltip.strider_feed.when_feeding");
        tooltip.add(textWhenFeeding.withStyle(ChatFormatting.GRAY));

        for (MobEffectInstance effectInstance : EFFECTS) {
            MutableComponent effectDescription = Component.literal(" ");
            MutableComponent effectName = Component.translatable(effectInstance.getDescriptionId());
            effectDescription.append(effectName);
            MobEffect effect = effectInstance.getEffect().value();

            if (effectInstance.getAmplifier() > 0) {
                effectDescription.append(" ").append(Component.translatable("potion.potency." + effectInstance.getAmplifier()));
            }

            if (effectInstance.getDuration() > 20) {
                effectDescription.append(" (").append(MobEffectUtil.formatDuration(effectInstance, 1.0F, context.tickRate())).append(")");
            }

            tooltip.add(effectDescription.withStyle(effect.getCategory().getTooltipFormatting()));
        }
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand) {
        if (target instanceof Strider strider) {
            if (strider.isAlive()) {
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}
