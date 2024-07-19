package com.soytutta.mynethersdelight.core.mixin;

import com.soytutta.mynethersdelight.common.registry.MNDItems;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FireworkRocketRecipe.class)
public abstract class FireworkRocketRecipeMixin {

    @Inject(method = "matches", at = @At("HEAD"), cancellable = true)
    private void onMatches(CraftingContainer container, Level level, CallbackInfoReturnable<Boolean> cir) {
        boolean hasPaper = false;
        int powderCannonCount = 0;
        int GunpowderCount = 0;
        for (int i = 0; i < container.getContainerSize(); ++i) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.is(Items.PAPER)) {
                    if (hasPaper) {
                        cir.setReturnValue(false);
                        return;
                    }
                    hasPaper = true;
                } else if (stack.is(MNDItems.POWDER_CANNON.get())) {
                    ++powderCannonCount;
                    if (powderCannonCount > 3 && GunpowderCount < 1) {
                        cir.setReturnValue(false);
                        return;
                    }
                } else if (stack.is(Items.GUNPOWDER)) {
                    ++GunpowderCount;
                    if (GunpowderCount > 3) {
                        cir.setReturnValue(false);
                        return;
                    }
                } else if (!stack.is(Items.FIREWORK_STAR)) {
                    cir.setReturnValue(false);
                    return;
                }
            }
        }

        cir.setReturnValue(hasPaper && (powderCannonCount >= 1 || GunpowderCount >= 1));
    }

    @Inject(method = "assemble", at = @At("HEAD"), cancellable = true)
    private void onAssemble(CraftingContainer container, RegistryAccess registry, CallbackInfoReturnable<ItemStack> cir) {
        boolean hasGunpowder = false;
        int powderCannonCount = 0;
        int extraCount = 0;
        int flight = 0;

        for (int i = 0; i < container.getContainerSize(); ++i) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.is(Items.GUNPOWDER)) {
                    hasGunpowder = true;
                    ++flight;
                } else if (stack.is(MNDItems.POWDER_CANNON.get())) {
                    ++powderCannonCount;
                    if (flight < 3) {
                        ++flight;
                    }
                }
            }
            if (hasGunpowder) {
                extraCount = powderCannonCount;
            }
        }

        ItemStack result = new ItemStack(Items.FIREWORK_ROCKET, 3 + extraCount);
        CompoundTag fireworksTag = result.getOrCreateTagElement("Fireworks");
        ListTag explosions = new ListTag();

        for (int i = 0; i < container.getContainerSize(); ++i) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.is(Items.FIREWORK_STAR)) {
                    CompoundTag explosionTag = stack.getTagElement("Explosion");
                    if (explosionTag != null) {
                        explosions.add(explosionTag);
                    }
                }
            }
        }

        fireworksTag.putByte("Flight", (byte) flight);
        if (!explosions.isEmpty()) {
            fireworksTag.put("Explosions", explosions);
        }

        cir.setReturnValue(result);
    }
}
