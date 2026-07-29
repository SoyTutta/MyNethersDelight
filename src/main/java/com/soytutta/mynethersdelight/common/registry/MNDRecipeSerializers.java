package com.soytutta.mynethersdelight.common.registry;

import com.soytutta.mynethersdelight.MyNethersDelight;
import com.soytutta.mynethersdelight.common.crafting.BlazierTemperatureRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MNDRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MyNethersDelight.MODID);

    public static final RegistryObject<SimpleCraftingRecipeSerializer<BlazierTemperatureRecipe>> BLAZIER_HEATING =
            RECIPE_SERIALIZERS.register("blazier_heating",
                    () -> new SimpleCraftingRecipeSerializer<>(BlazierTemperatureRecipe::heating));
    public static final RegistryObject<SimpleCraftingRecipeSerializer<BlazierTemperatureRecipe>> BLAZIER_COOLING =
            RECIPE_SERIALIZERS.register("blazier_cooling",
                    () -> new SimpleCraftingRecipeSerializer<>(BlazierTemperatureRecipe::cooling));
}
