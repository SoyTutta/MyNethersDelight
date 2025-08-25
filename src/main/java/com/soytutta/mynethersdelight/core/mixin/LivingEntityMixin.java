package com.soytutta.mynethersdelight.core.mixin;

import com.soytutta.mynethersdelight.common.utility.EntityDropChanceAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements EntityDropChanceAccessor {

    @Shadow protected abstract void dropAllDeathLoot(ServerLevel pLevel, DamageSource pDamageSource);

    @Override
    public List<ItemStack> callGenerateLoot(DamageSource damageSource) {
        LivingEntity self = (LivingEntity)(Object)this;
        if (!(self.level() instanceof ServerLevel serverLevel)) {
            return new ArrayList<>();
        }

        Collection<ItemEntity> capturedDrops = self.captureDrops(new ArrayList<>());

        try {
            this.dropAllDeathLoot(serverLevel, damageSource);
        } finally {
            capturedDrops = self.captureDrops(capturedDrops);
        }

        if (capturedDrops != null) {
            return capturedDrops.stream()
                    .map(ItemEntity::getItem)
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }
}