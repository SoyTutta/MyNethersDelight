package com.soytutta.mynethersdelight.common.registry;

import com.soytutta.mynethersdelight.MyNethersDelight;
import com.soytutta.mynethersdelight.common.crafting.BlazierTemperatureRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MNDRecipeSerializers {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, MyNethersDelight.MODID);

    public static final Supplier<SimpleCraftingRecipeSerializer<?>> BLAZIER_HEATING =
            RECIPE_SERIALIZERS.register("blazier_heating",
                    () -> new SimpleCraftingRecipeSerializer<>(BlazierTemperatureRecipe::heating));
    public static final Supplier<SimpleCraftingRecipeSerializer<?>> BLAZIER_COOLING =
            RECIPE_SERIALIZERS.register("blazier_cooling",
                    () -> new SimpleCraftingRecipeSerializer<>(BlazierTemperatureRecipe::cooling));
}
