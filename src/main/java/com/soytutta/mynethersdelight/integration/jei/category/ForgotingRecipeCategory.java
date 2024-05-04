//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.integration.jei.category;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.soytutta.mynethersdelight.common.utility.MNDTextUtils;
import com.soytutta.mynethersdelight.common.registry.MNDBlocks;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import com.soytutta.mynethersdelight.common.tag.MNDTags;
import com.soytutta.mynethersdelight.integration.jei.MNDRecipeTypes;
import com.soytutta.mynethersdelight.integration.jei.resource.ForgotingDummy;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
// thanks Umpaz for letting me use this code <3
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ForgotingRecipeCategory implements IRecipeCategory<ForgotingDummy> {
    public static final ResourceLocation UID = new ResourceLocation("farmersdelight", "composition");
    private static final int slotSize = 22;
    private final Component title = MNDTextUtils.getTranslation("jei.forgoting");

    private final IDrawable background;
    private final IDrawable slotIcon;
    private final IDrawable icon;
    private final ItemStack letiosCompost;
    private final ItemStack resurgentSoil;

    public ForgotingRecipeCategory(IGuiHelper helper) {
        ResourceLocation backgroundImage = new ResourceLocation("mynethersdelight", "textures/gui/jei/composition.png");
        this.background = helper.createDrawable(backgroundImage, 0, 0, 118, 80);
        this.letiosCompost = new ItemStack(MNDBlocks.LETIOS_COMPOST.get());
        this.resurgentSoil = new ItemStack(MNDItems.RESURGENT_SOIL.get());
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, this.resurgentSoil);
        this.slotIcon = helper.createDrawable(backgroundImage, 119, 0, 22, 22);
    }

    public RecipeType<ForgotingDummy> getRecipeType() {
        return MNDRecipeTypes.FORGOTING;
    }

    public Component getTitle() {
        return this.title;
    }

    public IDrawable getBackground() {
        return this.background;
    }

    public IDrawable getIcon() {
        return this.icon;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, ForgotingDummy recipe, IFocusGroup focusGroup) {
        List<ItemStack> accelerators = ForgeRegistries.BLOCKS.tags().getTag(MNDTags.SHOWCASE_ACTIVATORS).stream().map(ItemStack::new).collect(Collectors.toList());
        List<ItemStack> flames = ForgeRegistries.BLOCKS.tags().getTag(MNDTags.SHOWCASE_FLAMES).stream().map(ItemStack::new).collect(Collectors.toList());
        builder.addSlot(RecipeIngredientRole.INPUT, 9, 26).addItemStack(this.letiosCompost);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 93, 26).addItemStack(this.resurgentSoil);
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 64, 54).addItemStacks(accelerators);
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 38, 54).addItemStacks(flames);
    }

    public void draw(ForgotingDummy recipe, IRecipeSlotsView recipeSlotsView, PoseStack ms, double mouseX, double mouseY) {
        this.slotIcon.draw(ms, 63, 53);
        this.slotIcon.draw(ms, 37, 53);
    }

    public List<Component> getTooltipStrings(ForgotingDummy recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (inIconAt(40, 38, mouseX, mouseY)) {
            return ImmutableList.of(translateKey(".light"));
        } else if (inIconAt(53, 38, mouseX, mouseY)) {
            return ImmutableList.of(translateKey(".fluid"));
        } else if (inIconAt(67, 38, mouseX, mouseY)) {
            return ImmutableList.of(translateKey(".accelerators"));
        } else {
            return inIconOn(49, 9, mouseX, mouseY) ? ImmutableList.of(translateKey(".nether")) : Collections.emptyList();
        }
    }

    private static boolean inIconAt(int iconX, int iconY, double mouseX, double mouseY) {
        boolean icon_size = true;
        return (double)iconX <= mouseX && mouseX < (double)(iconX + 11) && (double)iconY <= mouseY && mouseY < (double)(iconY + 11);
    }

    private static boolean inIconOn(int iconX, int iconY, double mouseX, double mouseY) {
        return (double)iconX <= mouseX && mouseX < (double)(iconX + 16) && (double)iconY <= mouseY && mouseY < (double)(iconY + 19);
    }

    private static MutableComponent translateKey(@Nonnull String suffix) {
        return Component.translatable("mynethersdelight.jei.forgoting" + suffix);
    }
}
