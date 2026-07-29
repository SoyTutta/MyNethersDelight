package com.soytutta.mynethersdelight.core.mixin;

import com.soytutta.mynethersdelight.common.registry.MNDBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.Feature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Feature.class)
public class KeepResurgentSoilGiantTreeMixin {
    @Inject(method = "isGrassOrDirt", at = @At("HEAD"), cancellable = true)
    private static void mynethersdelight$keepResurgentSoil(LevelSimulatedReader level, BlockPos pos,
                                                           CallbackInfoReturnable<Boolean> cir) {
        if (level.isStateAtPosition(pos, state -> state.is(MNDBlocks.RESURGENT_SOIL.get()))) {
            cir.setReturnValue(false);
        }
    }
}
