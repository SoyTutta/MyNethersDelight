package com.soytutta.mynethersdelight.common.item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;

import com.soytutta.mynethersdelight.common.registry.MNDEffects;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.item.ConsumableItem;
import vectorwing.farmersdelight.common.registry.ModParticleTypes;
import vectorwing.farmersdelight.common.utility.MathUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;


@EventBusSubscriber(modid = "mynethersdelight", bus = Bus.FORGE)
public class HotCreamConeItem extends ConsumableItem {

    public HotCreamConeItem(Properties properties) {
        super(properties, false, true);
    }

    public SoundEvent getEatingSound() {
        return SoundEvents.HONEY_DRINK;
    }

    public void affectConsumer(ItemStack stack, Level level, LivingEntity consumer) {
        Iterator<MobEffectInstance> itr = consumer.getActiveEffects().iterator();
        ArrayList<MobEffect> compatibleEffects = new ArrayList<>();

        if (!consumer.fireImmune()) {
            consumer.setRemainingFireTicks(consumer.getRemainingFireTicks() + 1);
            consumer.setSecondsOnFire(10);
        }

        MobEffectInstance selectedEffect;
        while(itr.hasNext()) {
            selectedEffect = itr.next();
            if (selectedEffect.isCurativeItem(new ItemStack(Items.MILK_BUCKET))) {
                compatibleEffects.add(selectedEffect.getEffect());
            }
        }

        if (!compatibleEffects.isEmpty()) {
            MobEffect effectToRemove = compatibleEffects.get(level.random.nextInt(compatibleEffects.size()));
            selectedEffect = consumer.getEffect(effectToRemove);
            int purgentSeconds = selectedEffect.getDuration() / 15;
            int fireResistanceSeconds = purgentSeconds / 2;
            consumer.removeEffect(selectedEffect.getEffect());

            if (fireResistanceSeconds > 0) {
                consumer.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, fireResistanceSeconds * 3));
            }
            if (purgentSeconds > 0) {
                consumer.addEffect(new MobEffectInstance(MNDEffects.GPUNGENT.get(), purgentSeconds * 3));
            }
        }
            level.playSound(null, consumer.blockPosition(), SoundEvents.LAVA_EXTINGUISH, consumer.getSoundSource(), 1.0F, 1.0F);
    }

    public static final List<MobEffectInstance> EFFECTS;

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        if ((Boolean) Configuration.FOOD_EFFECT_TOOLTIP.get()) {
            MutableComponent textWhenFeeding = TextUtils.getTranslation("tooltip.strider_feed.when_feeding", new Object[0]);
            tooltip.add(textWhenFeeding.withStyle(ChatFormatting.GRAY));

            MutableComponent effectDescription;
            MobEffect effect;
            for (Iterator var6 = EFFECTS.iterator(); var6.hasNext(); tooltip.add(effectDescription.withStyle(effect.getCategory().getTooltipFormatting()))) {
                MobEffectInstance effectInstance = (MobEffectInstance) var6.next();
                effectDescription = Component.literal(" ");
                MutableComponent effectName = Component.translatable(effectInstance.getDescriptionId());
                effectDescription.append(effectName);
                effect = effectInstance.getEffect();
                if (effectInstance.getAmplifier() > 0) {
                    effectDescription.append(" ").append(Component.translatable("potion.potency." + effectInstance.getAmplifier()));
                }

                if (effectInstance.getDuration() > 20) {
                    effectDescription.append(" (").append(MobEffectUtil.formatDuration(effectInstance, 1.0F)).append(")");
                }
            }
        }
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target,
                                                  InteractionHand hand) {
        if (target instanceof Strider strider) {
            strider.setHealth(strider.getMaxHealth());
            for (MobEffectInstance effect : EFFECTS) {
                strider.addEffect(new MobEffectInstance(effect));
            }

            strider.level.playSound(null, target.blockPosition(), SoundEvents.STRIDER_HAPPY, SoundSource.PLAYERS,
                    0.8F, 0.8F);

            for (int i = 0; i < 5; ++i) {
                double d0 = MathUtils.RAND.nextGaussian() * 0.02;
                double d1 = MathUtils.RAND.nextGaussian() * 0.02;
                double d2 = MathUtils.RAND.nextGaussian() * 0.02;
                strider.level.addParticle((ParticleOptions) ModParticleTypes.STAR.get(), strider.getRandomX(1.0),
                        strider.getRandomY() + 0.5, strider.getRandomZ(1.0), d0, d1, d2);
            }

            if (!playerIn.isCreative()) {
                stack.shrink(1);
            }

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    static {
        EFFECTS = Lists.newArrayList(new MobEffectInstance[] {
                new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 6000, 1)});
    }

    @EventBusSubscriber(modid = "mynethersdelight", bus = Bus.FORGE)
    public static class StriderFeedEvent {
        public StriderFeedEvent() {
        }

        @SubscribeEvent
        public static void onStriderFeedApplied(PlayerInteractEvent.EntityInteract event) {
            Player player = event.getEntity();
            Entity target = event.getTarget();
            ItemStack heldStack = event.getItemStack();
            if (target instanceof LivingEntity entity) {
                if (entity instanceof Strider) {
                    if (entity.isAlive() && heldStack.getItem().equals(MNDItems.HOT_CREAM_CONE.get())) {
                        entity.setHealth(entity.getMaxHealth());
                        Iterator var6 = HotCreamConeItem.EFFECTS.iterator();
                        while (var6.hasNext()) {
                            MobEffectInstance effect = (MobEffectInstance) var6.next();
                            entity.addEffect(new MobEffectInstance(effect));
                        }

                        entity.level.playSound((Player) null, target.blockPosition(), SoundEvents.STRIDER_HAPPY,
                                SoundSource.PLAYERS, 0.8F, 0.8F);

                        for (int i = 0; i < 5; ++i) {
                            double d0 = MathUtils.RAND.nextGaussian() * 0.02;
                            double d1 = MathUtils.RAND.nextGaussian() * 0.02;
                            double d2 = MathUtils.RAND.nextGaussian() * 0.02;
                            entity.level.addParticle((ParticleOptions) ModParticleTypes.STAR.get(),
                                    entity.getRandomX(1.0), entity.getRandomY() + 0.5, entity.getRandomZ(1.0), d0,
                                    d1, d2);
                        }

                        if (!player.isCreative()) {
                            heldStack.shrink(1);
                        }

                        event.setCancellationResult(InteractionResult.SUCCESS);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
}
