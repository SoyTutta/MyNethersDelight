package com.soytutta.mynethersdelight.core.mixin;

import com.soytutta.mynethersdelight.common.registry.MNDItems;
import com.soytutta.mynethersdelight.common.tag.MyCommonTags;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Hoglin.class, Strider.class, Cat.class, Wolf.class})
public class AnimalFoodMixin {
    @Inject(method = "isFood", at = @At("HEAD"), cancellable = true)
    private void mynethersdelight$acceptPortedFoods(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        Object self = this;
        if (self instanceof Hoglin && stack.is(MNDItems.BULLET_PEPPER.get())) {
            cir.setReturnValue(true);
        } else if (self instanceof Strider
                && (stack.is(MNDItems.HOT_CREAM_CONE.get()) || stack.is(MNDItems.BULLET_PEPPER.get()))) {
            cir.setReturnValue(true);
        } else if (self instanceof Cat
                && (stack.is(MyCommonTags.FOODS_RAW_STRIDER) || stack.is(MNDItems.GHASMATI.get()))) {
            cir.setReturnValue(true);
        } else if (self instanceof Wolf && stack.is(MNDItems.HOTDOG.get())) {
            cir.setReturnValue(true);
        }
    }
}
