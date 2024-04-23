package com.soytutta.mynethersdelight.common.effect;

import com.soytutta.mynethersdelight.common.registry.MNDEffects;
import com.soytutta.mynethersdelight.common.tag.MNDTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.stream.StreamSupport;

public class PungentEffect extends MobEffect {

    private static final DamageSource PUNGENT_DAMAGE = new DamageSource("mynethersdelight.pungent").bypassArmor();

    public PungentEffect() {
        super(MobEffectCategory.NEUTRAL, 0);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        MobEffectInstance fireResistanceEffect = entity.getEffect(MobEffects.FIRE_RESISTANCE);
        MobEffectInstance BPungentEffect = entity.getEffect(MNDEffects.BPUNGENT.get());
        MobEffectInstance GPungentEffect = entity.getEffect(MNDEffects.GPUNGENT.get());
        boolean hasFireProtectionArmor = StreamSupport.stream(entity.getArmorSlots().spliterator(), false)
                .anyMatch(item -> EnchantmentHelper.getEnchantments(item).containsKey(Enchantments.FIRE_PROTECTION));

        if (entity.fireImmune() || fireResistanceEffect != null || hasFireProtectionArmor) {
            switchEffect(entity, BPungentEffect, MNDEffects.GPUNGENT.get());
        } else {
            switchEffect(entity, GPungentEffect, MNDEffects.BPUNGENT.get());
        }

        if (isInFireCondition(entity) || entity.isInLava() || entity.isOnFire()) {
            if (BPungentEffect != null) {
                entity.hurt(PUNGENT_DAMAGE, 1.5f * (amplifier + 1));
            } else if (GPungentEffect != null) {
                entity.heal(0.10f * (amplifier + 1));
            }
            if (isInFireCondition(entity)) {
                entity.setSecondsOnFire(1);
            }
        }

    }

    private void switchEffect(LivingEntity entity, MobEffectInstance currentEffect, MobEffect newEffect) {
        if (currentEffect != null) {
            int duration = currentEffect.getDuration();
            int level = currentEffect.getAmplifier();
            entity.removeEffect(currentEffect.getEffect());
            entity.addEffect(new MobEffectInstance(newEffect, duration, level));
        }
    }

    private boolean isInFireCondition(LivingEntity entity) {
        DamageSource lastDamageSource = entity.getLastDamageSource();
        Level world = entity.level;
        BlockPos entityPos = entity.blockPosition();
        boolean isOnFlame = false;

        int areaSize = 1;

        for (int x = -areaSize; x <= areaSize; x++) {
            for (int y = -areaSize; y <= areaSize; y++) {
                for (int z = -areaSize; z <= areaSize; z++) {
                    BlockPos pos = entityPos.offset(x, y, z);
                    BlockState blockState = world.getBlockState(pos);

                    if (blockState.is(MNDTags.LETIOS_FLAMES)) {
                        if (!blockState.hasProperty(BlockStateProperties.LIT) || (blockState.hasProperty(BlockStateProperties.LIT) && blockState.getValue(BlockStateProperties.LIT))) {
                            isOnFlame = true;
                            break;
                        }
                    }
                } if (isOnFlame) break;
            } if (isOnFlame) break;
        }

        return isOnFlame || (lastDamageSource != null && lastDamageSource.isFire());
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
