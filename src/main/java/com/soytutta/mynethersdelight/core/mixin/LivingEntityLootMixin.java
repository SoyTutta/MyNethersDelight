package com.soytutta.mynethersdelight.core.mixin;

import com.soytutta.mynethersdelight.common.utility.EntityDropChanceAccessor;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityLootMixin implements EntityDropChanceAccessor {
    @Shadow
    protected abstract void dropAllDeathLoot(DamageSource damageSource);

    @Override
    public List<ItemStack> callGenerateLoot(DamageSource damageSource) {
        LivingEntity self = (LivingEntity) (Object) this;
        Collection<ItemEntity> previousCapture = self.captureDrops(new ArrayList<>());
        Collection<ItemEntity> capturedDrops;
        try {
            dropAllDeathLoot(damageSource);
        } finally {
            capturedDrops = self.captureDrops(previousCapture);
        }

        if (capturedDrops == null) {
            return List.of();
        }
        return capturedDrops.stream().map(ItemEntity::getItem).toList();
    }
}
