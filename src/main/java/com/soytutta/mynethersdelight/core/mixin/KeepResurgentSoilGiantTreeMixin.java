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
    /**
     * Due to how Trees generate, this mixin is needed to prevent Resurgent Soil from becoming Podzol under a Giant Spruce Tree growth.
     */
    @Inject(at = @At(value = "HEAD"), method = "isGrassOrDirt", cancellable = true)
    private static void keepResurgentSoil(LevelSimulatedReader world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (world.isStateAtPosition(pos, state -> state.is(MNDBlocks.RESURGENT_SOIL.get()))) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
