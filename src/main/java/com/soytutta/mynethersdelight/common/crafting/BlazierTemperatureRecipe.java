package com.soytutta.mynethersdelight.common.crafting;

import com.soytutta.mynethersdelight.common.block.BlazierBlock;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import com.soytutta.mynethersdelight.common.registry.MNDRecipeSerializers;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class BlazierTemperatureRecipe extends CustomRecipe {
    private final boolean heating;

    private BlazierTemperatureRecipe(ResourceLocation id, CraftingBookCategory category, boolean heating) {
        super(id, category);
        this.heating = heating;
    }

    public static BlazierTemperatureRecipe heating(ResourceLocation id, CraftingBookCategory category) {
        return new BlazierTemperatureRecipe(id, category, true);
    }

    public static BlazierTemperatureRecipe cooling(ResourceLocation id, CraftingBookCategory category) {
        return new BlazierTemperatureRecipe(id, category, false);
    }

    public boolean isHeating() {
        return heating;
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        ItemStack blazier = ItemStack.EMPTY;
        int blazeRods = 0;

        for (int index = 0; index < container.getContainerSize(); index++) {
            ItemStack stack = container.getItem(index);
            if (stack.isEmpty()) {
                continue;
            }
            if (stack.is(MNDItems.BLAZIER.get()) && blazier.isEmpty()) {
                blazier = stack;
            } else if (heating && stack.is(Items.BLAZE_ROD)) {
                blazeRods++;
            } else {
                return false;
            }
        }

        if (blazier.isEmpty()) {
            return false;
        }
        if (!heating) {
            return blazeRods == 0;
        }
        return blazeRods == 2 && BlazierBlock.canIncrease(blazier);
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        ItemStack blazier = findBlazier(container);
        if (blazier.isEmpty()) {
            return ItemStack.EMPTY;
        }

        boolean lit = BlazierBlock.isLit(blazier);
        BlazierBlock.HeatLevel currentHeat = BlazierBlock.getHeat(blazier);

        if (!heating && !lit) {
            return new ItemStack(Items.NETHER_BRICK);
        }

        BlazierBlock.HeatLevel resultHeat;
        boolean resultLit;
        if (heating) {
            if (!lit) {
                resultHeat = BlazierBlock.HeatLevel.SMOKING;
            } else {
                resultHeat = currentHeat.increase().orElse(null);
                if (resultHeat == null) {
                    return ItemStack.EMPTY;
                }
            }
            resultLit = true;
        } else {
            resultHeat = currentHeat.decrease().orElse(BlazierBlock.HeatLevel.SMOKING);
            resultLit = currentHeat != BlazierBlock.HeatLevel.SMOKING;
        }

        ItemStack result = blazier.copy();
        result.setCount(1);
        BlazierBlock.setStoredState(result, resultHeat, resultLit);
        return result;
    }

    private static ItemStack findBlazier(CraftingContainer container) {
        for (int index = 0; index < container.getContainerSize(); index++) {
            ItemStack stack = container.getItem(index);
            if (stack.is(MNDItems.BLAZIER.get())) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= (heating ? 3 : 1);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return heating
                ? MNDRecipeSerializers.BLAZIER_HEATING.get()
                : MNDRecipeSerializers.BLAZIER_COOLING.get();
    }
}
