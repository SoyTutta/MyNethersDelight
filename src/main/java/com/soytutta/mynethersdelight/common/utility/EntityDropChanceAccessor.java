package com.soytutta.mynethersdelight.common.utility;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface EntityDropChanceAccessor {

    float callGetEquipmentDropChance(EquipmentSlot equipmentSlot);

    void callSetDropChance(EquipmentSlot equipmentSlot, float chance);

    void callDropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit);

    List<ItemStack> callGenerateLoot(DamageSource damageSource);
}