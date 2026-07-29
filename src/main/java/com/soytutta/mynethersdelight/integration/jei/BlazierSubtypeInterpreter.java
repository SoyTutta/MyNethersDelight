package com.soytutta.mynethersdelight.integration.jei;

import com.soytutta.mynethersdelight.common.block.BlazierBlock;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;

public class BlazierSubtypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {
    public static final BlazierSubtypeInterpreter INSTANCE = new BlazierSubtypeInterpreter();

    private BlazierSubtypeInterpreter() {
    }

    @Override
    public String apply(ItemStack ingredient, UidContext context) {
        return BlazierBlock.isLit(ingredient)
                ? BlazierBlock.getHeat(ingredient).getSerializedName()
                : "extinguished";
    }
}
