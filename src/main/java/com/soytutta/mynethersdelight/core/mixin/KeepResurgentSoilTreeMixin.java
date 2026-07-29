package com.soytutta.mynethersdelight.core.mixin;

import com.soytutta.mynethersdelight.common.registry.MNDBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiConsumer;

@Mixin(TrunkPlacer.class)
public class KeepResurgentSoilTreeMixin {
    @Inject(method = "setDirtAt", at = @At("HEAD"), cancellable = true)
    private static void mynethersdelight$keepResurgentSoil(LevelSimulatedReader level,
                                                           BiConsumer<BlockPos, BlockState> blockSetter,
                                                           RandomSource random, BlockPos pos,
                                                           TreeConfiguration configuration, CallbackInfo ci) {
        if (level.isStateAtPosition(pos, state -> state.is(MNDBlocks.RESURGENT_SOIL.get()))) {
            ci.cancel();
        }
    }
}
