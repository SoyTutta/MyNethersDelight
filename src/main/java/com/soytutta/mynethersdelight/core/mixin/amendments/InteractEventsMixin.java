package com.soytutta.mynethersdelight.core.mixin.amendments;

import com.soytutta.mynethersdelight.common.registry.MNDBlocks;
import net.mehvahdjukaar.amendments.events.behaviors.InteractEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = InteractEvents.class, remap = false)
public class InteractEventsMixin {

    @Inject(method = "onItemUsedOnBlock", at = @At("HEAD"), cancellable = true)
    private static void onItemUsedOnBlock(Player player, Level level, ItemStack stack, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        BlockPos pos = hitResult.getBlockPos();
        BlockState state = level.getBlockState(pos);

        if (state.is(MNDBlocks.MAGMA_CAKE.get()) && stack.is(MNDBlocks.MAGMA_CAKE.get().asItem())) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}