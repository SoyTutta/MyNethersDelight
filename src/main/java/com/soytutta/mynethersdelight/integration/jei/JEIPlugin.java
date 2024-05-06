//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.integration.jei;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.ImmutableList;
import com.soytutta.mynethersdelight.common.registry.MNDBlocks;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import com.soytutta.mynethersdelight.common.utility.MNDTextUtils;
import com.soytutta.mynethersdelight.integration.jei.category.ForgotingRecipeCategory;
import com.soytutta.mynethersdelight.integration.jei.resource.ForgotingDummy;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.integration.jei.FDRecipes;

import java.util.List;

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
        registration.addIngredientInfo(List.of( new ItemStack(MNDItems.BULLET_PEPPER.get()), new ItemStack(MNDItems.POWDER_CANNON.get())), VanillaTypes.ITEM_STACK, MNDTextUtils.getTranslation("jei.info.wild_powdery"));
        registration.addIngredientInfo(List.of( new ItemStack(MNDItems.RESURGENT_SOIL_FARMLAND.get()), new ItemStack(MNDItems.RESURGENT_SOIL.get())), VanillaTypes.ITEM_STACK, MNDTextUtils.getTranslation("jei.info.r_soil"));
        registration.addIngredientInfo(List.of( new ItemStack(ModItems.BROWN_MUSHROOM_COLONY.get()), new ItemStack(ModItems.RED_MUSHROOM_COLONY.get())), VanillaTypes.ITEM_STACK, MNDTextUtils.getTranslation("jei.info.mushroom_colony"));
        registration.addIngredientInfo(List.of( new ItemStack(Items.NETHER_WART), new ItemStack(MNDItems.WARPED_FUNGUS_COLONY.get()), new ItemStack(MNDItems.CRIMSON_FUNGUS_COLONY.get())), VanillaTypes.ITEM_STACK, MNDTextUtils.getTranslation("jei.info.fungus_colony"));
        registration.addIngredientInfo(new ItemStack(MNDItems.STRIDER_ROCK.get()), VanillaTypes.ITEM_STACK, MNDTextUtils.getTranslation("jei.info.strider_egg"));
        registration.addIngredientInfo(new ItemStack(MNDItems.HOGLIN_HIDE.get()), VanillaTypes.ITEM_STACK, MNDTextUtils.getTranslation("jei.info.hoglin_hide"));
        registration.addIngredientInfo(List.of( new ItemStack(MNDItems.ROAST_STUFFED_HOGLIN.get()), new ItemStack(MNDItems.ROAST_EAR.get()), new ItemStack(MNDItems.PLATE_OF_STUFFED_HOGLIN_SNOUT.get()), new ItemStack(MNDItems.PLATE_OF_STUFFED_HOGLIN_HAM.get()), new ItemStack(MNDItems.PLATE_OF_STUFFED_HOGLIN.get())), VanillaTypes.ITEM_STACK, MNDTextUtils.getTranslation("jei.info.plate_of_stuffed_hoglin"));
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
