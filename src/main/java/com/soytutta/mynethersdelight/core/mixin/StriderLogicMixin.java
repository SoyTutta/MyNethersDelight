package com.soytutta.mynethersdelight.core.mixin;

import com.soytutta.mynethersdelight.common.registry.MNDEffects;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Strider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Strider.class)
public abstract class StriderLogicMixin extends LivingEntity {

    @Shadow @Final private static EntityDataAccessor<Boolean> mynetherdelight$DATA_IS_PUNGENT;

    protected StriderLogicMixin() { super(null, null); }

    @Inject(method = "tick()V", at = @At("TAIL"))
    private void updatePungentState(CallbackInfo ci) {
        if (!this.level().isClientSide()) {
            boolean hasEffect = this.getActiveEffectsMap().containsKey(MNDEffects.GPUNGENT);

            this.getEntityData().set(mynetherdelight$DATA_IS_PUNGENT, hasEffect);
        }
    }
}