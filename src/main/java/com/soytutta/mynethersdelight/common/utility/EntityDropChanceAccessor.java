package com.soytutta.mynethersdelight.common.utility;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface EntityDropChanceAccessor {
    List<ItemStack> callGenerateLoot(DamageSource damageSource);
}
