package com.soytutta.mynethersdelight.integration.jei;

import com.soytutta.mynethersdelight.common.block.BlazierBlock;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;

public class BlazierSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {

    public static final BlazierSubtypeInterpreter INSTANCE = new BlazierSubtypeInterpreter();

    private BlazierSubtypeInterpreter() {
    }

    @Override
    public Object getSubtypeData(ItemStack ingredient, UidContext context) {
        return getSubtype(ingredient);
    }

    @Override
    public String getLegacyStringSubtypeInfo(ItemStack ingredient, UidContext context) {
        return getSubtype(ingredient);
    }

    private static String getSubtype(ItemStack ingredient) {
        BlockItemStateProperties properties = ingredient.getOrDefault(
                DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY);
        if (Boolean.FALSE.equals(properties.get(BlazierBlock.LIT))) {
            return "extinguished";
        }
        BlazierBlock.HeatLevel heat = properties.get(BlazierBlock.HEAT);
        return heat == null
                ? BlazierBlock.HeatLevel.SMELTING.getSerializedName()
                : heat.getSerializedName();
    }
}
