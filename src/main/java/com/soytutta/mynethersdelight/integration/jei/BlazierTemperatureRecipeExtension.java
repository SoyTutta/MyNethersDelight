package com.soytutta.mynethersdelight.integration.jei;

import com.soytutta.mynethersdelight.common.block.BlazierBlock;
import com.soytutta.mynethersdelight.common.crafting.BlazierTemperatureRecipe;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class BlazierTemperatureRecipeExtension implements ICraftingCategoryExtension {
    private final BlazierTemperatureRecipe recipe;

    public BlazierTemperatureRecipeExtension(BlazierTemperatureRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper,
                          IFocusGroup focuses) {
        List<ItemStack> blazierInputs = new ArrayList<>();
        List<ItemStack> outputs = new ArrayList<>();
        List<List<ItemStack>> inputs = new ArrayList<>();

        if (recipe.isHeating()) {
            blazierInputs.add(createBlazierStack(BlazierBlock.HeatLevel.SMOKING, false));
            blazierInputs.add(createBlazierStack(BlazierBlock.HeatLevel.SMOKING, true));
            blazierInputs.add(createBlazierStack(BlazierBlock.HeatLevel.CAMPFIRE, true));
            blazierInputs.add(createBlazierStack(BlazierBlock.HeatLevel.BAKING, true));

            outputs.add(createBlazierStack(BlazierBlock.HeatLevel.SMOKING, true));
            outputs.add(createBlazierStack(BlazierBlock.HeatLevel.CAMPFIRE, true));
            outputs.add(createBlazierStack(BlazierBlock.HeatLevel.BAKING, true));
            outputs.add(createBlazierStack(BlazierBlock.HeatLevel.SMELTING, true));

            inputs.add(blazierInputs);
            inputs.add(List.of(new ItemStack(Items.BLAZE_ROD)));
            inputs.add(List.of(new ItemStack(Items.BLAZE_ROD)));
        } else {
            blazierInputs.add(createBlazierStack(BlazierBlock.HeatLevel.SMELTING, true));
            blazierInputs.add(createBlazierStack(BlazierBlock.HeatLevel.BAKING, true));
            blazierInputs.add(createBlazierStack(BlazierBlock.HeatLevel.CAMPFIRE, true));
            blazierInputs.add(createBlazierStack(BlazierBlock.HeatLevel.SMOKING, true));
            blazierInputs.add(createBlazierStack(BlazierBlock.HeatLevel.SMOKING, false));

            outputs.add(createBlazierStack(BlazierBlock.HeatLevel.BAKING, true));
            outputs.add(createBlazierStack(BlazierBlock.HeatLevel.CAMPFIRE, true));
            outputs.add(createBlazierStack(BlazierBlock.HeatLevel.SMOKING, true));
            outputs.add(createBlazierStack(BlazierBlock.HeatLevel.SMOKING, false));
            outputs.add(new ItemStack(Items.NETHER_BRICK));
            inputs.add(blazierInputs);
        }

        List<IRecipeSlotBuilder> inputSlots =
                craftingGridHelper.createAndSetInputs(builder, inputs, 0, 0);
        IRecipeSlotBuilder outputSlot =
                craftingGridHelper.createAndSetOutputs(builder, outputs);
        builder.createFocusLink(inputSlots.get(0), outputSlot);
        builder.setShapeless();
    }

    static ItemStack createBlazierStack(BlazierBlock.HeatLevel heat, boolean lit) {
        ItemStack stack = new ItemStack(MNDItems.BLAZIER.get());
        BlazierBlock.setStoredState(stack, heat, lit);
        return stack;
    }
}
