package com.soytutta.mynethersdelight.core.mixin;

import com.soytutta.mynethersdelight.common.registry.MNDEffects;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Strider.class)
public class StriderMixin {
    @Unique
    private static final EntityDataAccessor<Boolean> mynethersdelight$DATA_IS_PUNGENT =
            SynchedEntityData.defineId(Strider.class, EntityDataSerializers.BOOLEAN);

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void mynethersdelight$definePungentData(CallbackInfo ci) {
        Strider self = (Strider) (Object) this;
        self.getEntityData().define(mynethersdelight$DATA_IS_PUNGENT, false);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void mynethersdelight$addHotCreamTemptGoal(CallbackInfo ci) {
        Strider self = (Strider) (Object) this;
        self.goalSelector.addGoal(3, new TemptGoal(self, 1.2D, Ingredient.of(MNDItems.HOT_CREAM_CONE.get()), false));
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void mynethersdelight$updatePungentState(CallbackInfo ci) {
        Strider self = (Strider) (Object) this;
        if (!self.level().isClientSide) {
            boolean pungent = self.hasEffect(MNDEffects.GPUNGENT.get());
            if (self.getEntityData().get(mynethersdelight$DATA_IS_PUNGENT) != pungent) {
                self.getEntityData().set(mynethersdelight$DATA_IS_PUNGENT, pungent);
            }
        }
    }

    @Inject(method = "isSuffocating", at = @At("HEAD"), cancellable = true)
    private void mynethersdelight$disableColdVisuals(CallbackInfoReturnable<Boolean> cir) {
        Strider self = (Strider) (Object) this;
        if (self.getEntityData().get(mynethersdelight$DATA_IS_PUNGENT)) {
            cir.setReturnValue(false);
        }
    }
}
