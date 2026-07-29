package com.soytutta.mynethersdelight.common.effect;

import com.soytutta.mynethersdelight.common.registry.MNDEffects;
import com.soytutta.mynethersdelight.common.tag.MNDTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public abstract class AbstractPungentEffect extends MobEffect {
    protected AbstractPungentEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    protected void switchEffect(LivingEntity entity) {
        boolean fireProtected = entity.fireImmune() || entity.hasEffect(MobEffects.FIRE_RESISTANCE)
                || hasFireProtectionArmor(entity);
        MobEffectInstance current = entity.getEffect(this);
        if (current == null) {
            return;
        }

        MobEffect target = fireProtected ? MNDEffects.GPUNGENT.get() : MNDEffects.BPUNGENT.get();
        if (target != this) {
            int duration = current.getDuration();
            int amplifier = current.getAmplifier();
            boolean ambient = current.isAmbient();
            boolean visible = current.isVisible();
            boolean icon = current.showIcon();
            entity.removeEffect(this);
            entity.addEffect(new MobEffectInstance(target, duration, amplifier, ambient, visible, icon));
        }
    }

    private boolean hasFireProtectionArmor(LivingEntity entity) {
        for (ItemStack armorPiece : entity.getArmorSlots()) {
            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_PROTECTION, armorPiece) > 0) {
                return true;
            }
        }
        return false;
    }

    protected boolean isInFireCondition(LivingEntity entity) {
        BlockPos entityPos = entity.blockPosition();
        for (BlockPos pos : BlockPos.betweenClosed(entityPos.offset(-1, -1, -1), entityPos.offset(1, 1, 1))) {
            BlockState state = entity.level().getBlockState(pos);
            if (state.is(MNDTags.LETIOS_FLAMES)
                    && (!state.hasProperty(BlockStateProperties.LIT) || state.getValue(BlockStateProperties.LIT))) {
                return true;
            }
        }
        return false;
    }
}
