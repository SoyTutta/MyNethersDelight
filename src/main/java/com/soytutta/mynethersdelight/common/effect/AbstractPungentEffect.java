package com.soytutta.mynethersdelight.common.effect;

import com.soytutta.mynethersdelight.common.registry.MNDEffects;
import com.soytutta.mynethersdelight.common.tag.MNDTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.Optional;

public abstract class AbstractPungentEffect extends MobEffect {

    public AbstractPungentEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    private boolean hasFireProtectionArmor(LivingEntity entity) {
        Registry<Enchantment> enchantmentRegistry = entity.level().registryAccess().registryOrThrow(Registries.ENCHANTMENT);

        Optional<Holder.Reference<Enchantment>> fireProtectionHolderOpt = enchantmentRegistry.getHolder(Enchantments.FIRE_PROTECTION);

        if (fireProtectionHolderOpt.isEmpty()) {
            return false;
        }
        Holder<Enchantment> fireProtectionHolder = fireProtectionHolderOpt.get();

        for (ItemStack armorPiece : entity.getArmorSlots()) {
            if (EnchantmentHelper.getItemEnchantmentLevel(fireProtectionHolder, armorPiece) > 0) {
                return true;
            }
        }
        return false;
    }

    protected void switchEffect(LivingEntity entity) {
        boolean isImmuneToFire = entity.fireImmune()
                || entity.hasEffect(MobEffects.FIRE_RESISTANCE)
                || hasFireProtectionArmor(entity);

        Holder<MobEffect> thisHolder = BuiltInRegistries.MOB_EFFECT.wrapAsHolder(this);
        MobEffectInstance currentEffectInstance = entity.getEffect(thisHolder);
        if (currentEffectInstance == null) {
            return;
        }

        MobEffect targetEffect = isImmuneToFire ? MNDEffects.GPUNGENT.value() : MNDEffects.BPUNGENT.value();
        if (this != targetEffect) {
            transformEffect(entity, currentEffectInstance, targetEffect);
        }
    }

    private void transformEffect(LivingEntity entity, MobEffectInstance oldEffect, MobEffect newEffect) {
        Holder<MobEffect> newEffectHolder = BuiltInRegistries.MOB_EFFECT.wrapAsHolder(newEffect);

        int duration = oldEffect.getDuration();
        int amplifier = oldEffect.getAmplifier();
        boolean ambient = oldEffect.isAmbient();
        boolean visible = oldEffect.isVisible();
        boolean icon = oldEffect.showIcon();

        entity.removeEffect(oldEffect.getEffect());
        entity.addEffect(new MobEffectInstance(newEffectHolder, duration, amplifier, ambient, visible, icon));
    }

    protected boolean isInFireCondition(LivingEntity entity) {
        Level world = entity.level();
        BlockPos entityPos = entity.blockPosition();
        int areaSize = 1;

        for (BlockPos pos : BlockPos.betweenClosed(entityPos.offset(-areaSize, -areaSize, -areaSize), entityPos.offset(areaSize, areaSize, areaSize))) {
            BlockState blockState = world.getBlockState(pos);
            if (blockState.is(MNDTags.LETIOS_FLAMES)) {
                if (!blockState.hasProperty(BlockStateProperties.LIT) || blockState.getValue(BlockStateProperties.LIT)) {
                    return true;
                }
            }
        }
        return false;
    }
}