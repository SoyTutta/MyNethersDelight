package com.soytutta.mynethersdelight.core.mixin;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.monster.Strider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Strider.class)
public class StriderEntityDataMixin {

    @Unique // Marca nuestro campo como único para este Mixin
    private static final EntityDataAccessor<Boolean> mynetherdelight$DATA_IS_PUNGENT =
            SynchedEntityData.defineId(Strider.class, EntityDataSerializers.BOOLEAN);

    @Inject(method = "defineSynchedData(Lnet/minecraft/network/syncher/SynchedEntityData$Builder;)V", at = @At("TAIL"))
    private void addPungentSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(mynetherdelight$DATA_IS_PUNGENT, false);
    }
}