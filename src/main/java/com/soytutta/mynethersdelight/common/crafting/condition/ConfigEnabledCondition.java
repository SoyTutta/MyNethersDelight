package com.soytutta.mynethersdelight.common.crafting.condition;

import com.google.gson.JsonObject;
import com.soytutta.mynethersdelight.MyNethersDelight;
import com.soytutta.mynethersdelight.common.MNDConfiguration;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

import java.util.Locale;
import java.util.function.BooleanSupplier;

public record ConfigEnabledCondition(Setting setting) implements ICondition {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(MyNethersDelight.MODID, "config_enabled");
    public static final Serializer SERIALIZER = new Serializer();

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public boolean test(IContext context) {
        return setting.enabled.getAsBoolean();
    }

    public enum Setting {
        BLAZIER(() -> MNDConfiguration.ENABLE_BLAZIER.get()),
        STONE_CABINETS(() -> MNDConfiguration.ENABLE_STONE_CABINETS.get()),
        POWDERY_CANE_GENERATION(() -> MNDConfiguration.GENERATE_POWDERY_CANE.get());

        private final BooleanSupplier enabled;

        Setting(BooleanSupplier enabled) {
            this.enabled = enabled;
        }
    }

    public static class Serializer implements IConditionSerializer<ConfigEnabledCondition> {
        @Override
        public void write(JsonObject json, ConfigEnabledCondition value) {
            json.addProperty("setting", value.setting.name().toLowerCase(Locale.ROOT));
        }

        @Override
        public ConfigEnabledCondition read(JsonObject json) {
            String setting = json.getAsJsonPrimitive("setting").getAsString();
            return new ConfigEnabledCondition(Setting.valueOf(setting.toUpperCase(Locale.ROOT)));
        }

        @Override
        public ResourceLocation getID() {
            return ID;
        }
    }
}
