//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.integration.jei;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.ImmutableList;
import com.soytutta.mynethersdelight.common.registry.MNDBlocks;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import com.soytutta.mynethersdelight.integration.jei.category.ForgotingRecipeCategory;
import com.soytutta.mynethersdelight.integration.jei.resource.ForgotingDummy;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import vectorwing.farmersdelight.integration.jei.FDRecipes;

@ParametersAreNonnullByDefault
@JeiPlugin
public class JEIPlugin implements IModPlugin {
    private static final ResourceLocation ID = new ResourceLocation("mynethersdelight", "jei_plugin");

    public JEIPlugin() {
    }

    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new ForgotingRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    public void registerRecipes(IRecipeRegistration registration) {
        new FDRecipes();
        registration.addRecipes(MNDRecipeTypes.FORGOTING, ImmutableList.of(new ForgotingDummy()));
    }


    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(MNDItems.NETHER_STOVE.get()), RecipeTypes.CAMPFIRE_COOKING);
        registration.addRecipeCatalyst(new ItemStack(MNDItems.SOUL_NETHER_STOVE.get()), RecipeTypes.CAMPFIRE_COOKING);
        registration.addRecipeCatalyst(new ItemStack((ItemLike) MNDBlocks.LETIOS_COMPOST.get()), MNDRecipeTypes.FORGOTING);

    }

    public ResourceLocation getPluginUid() {
        return ID;
    }
}
