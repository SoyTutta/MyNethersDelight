package com.soytutta.mynethersdelight.core.mixin;

import com.soytutta.mynethersdelight.common.utility.EntityDropChanceAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity implements EntityDropChanceAccessor {

    public MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow protected abstract float getEquipmentDropChance(EquipmentSlot p_21520_);
    @Shadow public abstract void setDropChance(EquipmentSlot p_21410_, float p_21411_);
    @Shadow protected abstract void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit);

    @Override
    public float callGetEquipmentDropChance(EquipmentSlot equipmentSlot){
        return this.getEquipmentDropChance(equipmentSlot);
    }

    @Override
    public void callSetDropChance(EquipmentSlot equipmentSlot, float chance){
        this.setDropChance(equipmentSlot, chance);
    }

    @Override
    public void callDropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit){
        this.dropCustomDeathLoot(level, damageSource, recentlyHit);
    }
}