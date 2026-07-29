//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.core.data;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.soytutta.mynethersdelight.common.registry.MNDBlocks;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;

public class MNDLang extends LanguageProvider {
    private final Set<String> addedKeys = new HashSet<>();

    public MNDLang(PackOutput output) {
        super(output, "mynethersdelight", "en_us");
    }

    @Override
    protected void addTranslations() {
        Set<Supplier<Block>> blocks = MNDBlocks.BLOCKS.getEntries().stream()
                .map(entry -> (Supplier<Block>) entry::get)
                .collect(Collectors.toSet());
        Set<Supplier<Item>> items = MNDItems.ITEMS.getEntries().stream()
                .map(entry -> (Supplier<Item>) entry::get)
                .collect(Collectors.toSet());

        blocks.removeIf(block -> Set.of(
                MNDBlocks.LETIOS_COMPOST.get(),
                MNDBlocks.BLOCK_OF_POWDERY_CANNON.get(),
                MNDBlocks.BLOCK_OF_STRIPPED_POWDERY_CANNON.get(),
                MNDBlocks.WALL_POWDERY_TORCH.get(),
                MNDBlocks.POWDERY_WALL_SIGN.get(),
                MNDBlocks.POWDERY_WALL_HANGING_SIGN.get()
        ).contains(block.get()));

        blocks.forEach((b) -> {
            String descriptionId = b.get().getDescriptionId();
            if (addedKeys.add(descriptionId)) {
                String name = descriptionId.replaceFirst("block.mynethersdelight.", "");
                name = toTitleCase(this.correctBlockItemName(name), "_").replaceAll("Of", "of");
                name = toTitleCase(this.correctBlockItemName(name), "_").replaceAll(" Block", "");
                this.add(descriptionId, name);
            }
        });

        items.removeIf((i) -> i.get() instanceof BlockItem);
        items.forEach((i) -> {
            String descriptionId = i.get().getDescriptionId();
            if (addedKeys.add(descriptionId)) {
                String name = descriptionId.replaceFirst("item.mynethersdelight.", "");
                name = toTitleCase(this.correctBlockItemName(name), "_").replaceAll("A", "a");
                name = toTitleCase(this.correctBlockItemName(name), "_").replaceAll("On", "on");
                name = toTitleCase(this.correctBlockItemName(name), "_").replaceAll("Of", "of");
                name = toTitleCase(this.correctBlockItemName(name), "_").replaceAll("And", "and");
                name = toTitleCase(this.correctBlockItemName(name), "_").replaceAll("With", "with");
                this.add(descriptionId, name);
            }
        });

        addCustomTranslations();
    }

    private void addCustomTranslations() {
        this.add("block.mynethersdelight.letios_compost", "Leteos Compost");
        this.add("block.mynethersdelight.powdery_block", "Block of Powdery Cannon");
        this.add("block.mynethersdelight.stripped_powdery_block", "Block of Stripped Powdery Cannon");

        this.add("mynethersdelight.itemGroup.main", "My Nether's Delight");
        this.add("effect.mynethersdelight.b_pungent", "Caustic Pungency");
        this.add("effect.mynethersdelight.b_pungent.desc", "Induces combustion upon heat exposure; flames are automatically doused when at a safe distance.");
        this.add("effect.mynethersdelight.g_pungent", "Invigorating Pungency");
        this.add("effect.mynethersdelight.g_pungent.desc", "Provides natural regeneration while exposed to heat.");
        this.add("mynethersdelight.jei.forgoting", "Forgetting");
        this.add("mynethersdelight.jei.forgoting.nether", "Will only forget in the nether");
        this.add("mynethersdelight.jei.forgoting.accelerators", "Sped up by adjacent activators (see below)");
        this.add("mynethersdelight.jei.forgoting.light", "Sped up by adjacent flames (see below)");
        this.add("mynethersdelight.jei.forgoting.fluid", "Sped up by adjacent lava");

        this.add("block.mynethersdelight.blazier.too_hot", "The fire is too intense.");
        this.add("block.mynethersdelight.blazier.too_cold", "The fire isn't burning hot enough.");
        this.add("tooltip.mynethersdelight.blazier.heat", "Heat: %s");
        this.add("tooltip.mynethersdelight.blazier.heat.smelting", "White-hot");
        this.add("tooltip.mynethersdelight.blazier.heat.baking", "Blazing");
        this.add("tooltip.mynethersdelight.blazier.heat.campfire", "Live flame");
        this.add("tooltip.mynethersdelight.blazier.heat.smoking", "Embers");
        this.add("tooltip.mynethersdelight.blazier.heat.extinguished", "Blazier Base");

        this.add("mynethersdelight.configuration.title", "My Nether's Delight — Settings");
        this.add("mynethersdelight.configuration.section.mynethersdelight.common.toml", "Common settings");
        this.add("mynethersdelight.configuration.section.mynethersdelight.common.toml.title", "Common settings");
        this.add("mynethersdelight.configuration.settings", "General");
        this.add("mynethersdelight.configuration.settings.button", "General");
        this.add("mynethersdelight.configuration.settings.tooltip", "Configure general gameplay settings.");
        this.add("mynethersdelight.configuration.enablePiglinFoodTrades", "Enable Piglin Food Trades");
        this.add("mynethersdelight.configuration.enablePiglinFoodTrades.tooltip", "Allows Piglins to barter food items and Strider Rocks from this mod. Special hunting-related trades are not affected.");
        this.add("mynethersdelight.configuration.piglinFoodTradeChance", "Piglin Food Trade Chance");
        this.add("mynethersdelight.configuration.piglinFoodTradeChance.tooltip", "The chance that a Piglin barter gives food or Strider Rocks from this mod. Uses a decimal percentage: 0.25 means 25%.");
        this.add("mynethersdelight.configuration.enableFrogMagmaCakeBehavior", "Enable Frog Magma Cake Behavior");
        this.add("mynethersdelight.configuration.enableFrogMagmaCakeBehavior.tooltip", "Allows Frogs to seek out Magma Cake and Magma Cake Slices, and lets players feed slices directly to a Frog.");
        this.add("mynethersdelight.configuration.farming", "Farming");
        this.add("mynethersdelight.configuration.farming.button", "Farming");
        this.add("mynethersdelight.configuration.farming.tooltip", "Configure Resurgent Soil and Resurgent Soil Farmland behavior.");
        this.add("mynethersdelight.configuration.enableResurgentSoilPropagation", "Enable Resurgent Soil Propagation");
        this.add("mynethersdelight.configuration.enableResurgentSoilPropagation.tooltip", "Allows Resurgent Soil and Resurgent Soil Farmland to propagate nearby plants.");
        this.add("mynethersdelight.configuration.resurgentSoilTickMultiplier", "Resurgent Soil Tick Multiplier");
        this.add("mynethersdelight.configuration.resurgentSoilTickMultiplier.tooltip", "Multiplies the number of growth, transformation and propagation attempts made by Resurgent Soil and Resurgent Soil Farmland. Farmland hydration and drying are not affected.");
        this.add("mynethersdelight.configuration.resurgentSoilGrowthRange", "Resurgent Soil Growth Range");
        this.add("mynethersdelight.configuration.resurgentSoilGrowthRange.tooltip", "The maximum vertical distance that Resurgent Soil and Resurgent Soil Farmland follow connected plants while applying growth.");
        this.add("mynethersdelight.configuration.resurgentFarmlandHeatSearchRadius", "Farmland Heat Search Radius");
        this.add("mynethersdelight.configuration.resurgentFarmlandHeatSearchRadius.tooltip", "The horizontal radius used to search for valid heat sources. Vertical range is half this value. Set to 0 to disable heat hydration.");
        this.add("mynethersdelight.configuration.crafting", "Crafting");
        this.add("mynethersdelight.configuration.crafting.button", "Crafting");
        this.add("mynethersdelight.configuration.crafting.tooltip", "Configure craftable features and Blazier behavior.");
        this.add("mynethersdelight.configuration.enableBlazier", "Enable Blazier");
        this.add("mynethersdelight.configuration.enableBlazier.tooltip", "Master switch for the Blazier. Disabling hides its creative-tab item and recipes, and stops its behavior.");
        this.add("mynethersdelight.configuration.blazierCookingTimeMultiplier", "Blazier Cooking Time Multiplier");
        this.add("mynethersdelight.configuration.blazierCookingTimeMultiplier.tooltip", "Multiplies cooking time in every Blazier mode while preserving their original 6 / 3 / 1 / 6 time proportions. Higher values take longer.");
        this.add("mynethersdelight.configuration.enableStoneCabinets", "Enable Stone Cabinets");
        this.add("mynethersdelight.configuration.enableStoneCabinets.tooltip", "Enables Nether Brick, Red Nether Brick and Blackstone Brick Cabinets. Disabling hides their creative-tab items and recipes.");
        this.add("mynethersdelight.configuration.world", "World");
        this.add("mynethersdelight.configuration.world.button", "World");
        this.add("mynethersdelight.configuration.world.tooltip", "Configure world generation.");
        this.add("mynethersdelight.configuration.generatePowderyCane", "Generate Powdery Cane");
        this.add("mynethersdelight.configuration.generatePowderyCane.tooltip", "Controls whether Powdery Cane patches generate naturally in Crimson Forests.");
        this.add("mynethersdelight.block.feast.space_required", "You need more space to serve this.");
        this.add("mynethersdelight.block.feast.use_knife", "You need a Knife to cut this.");
        this.add("tooltip.farmersdelight.strider_egg", "Nourished by 1 Harmful Effect");
        this.add("tooltip.farmersdelight.golden_egg", "Nourished by all harmful effects");
        this.add("tooltip.farmersdelight.enchanted_golden_egg", "Nourished by all harmful effects");
        this.add("tooltip.farmersdelight.hot_cream", "Burning Effects");
        this.add("farmersdelight.tooltip.strider_feed.when_feeding", "When fed to a Strider:");
        this.add("tooltip.farmersdelight.magma_cake_slice", "Spicy frog Snack");

        this.add("mynethersdelight.jei.info.strider_egg", "A peculiar find, suggesting Striders may be ovoviviparous.\n\nObtainable only through careful hunting.\nhard shell, it can only be eaten starry...");
        this.add("mynethersdelight.jei.info.hoglin_hide", "A great source of leather which also serves as a nice hunting trophy.\n\nTo avoid damaging the hide, using something lighter than a Sword...");
        this.add("mynethersdelight.jei.info.mushroom_colony", "Some Mushrooms form colonies when they are on top of Rich Soil or Resurgent Soil.");
        this.add("mynethersdelight.jei.info.fungus_colony", "Some Nethers Fungus form colonies when they are on top of Resurgent Soil.");
        this.add("mynethersdelight.jei.info.wild_powdery", "An invasive crop found in Crimson Forests. Its Berries are explosive on Contact, Try Cutting the red stem...");

        this.add("enchantment.mynethersdelight.poaching", "Curse of Poaching");
        this.add("enchantment.mynethersdelight.poaching.desc", "Responsible hunting can provide you with extra ingredients!!!\nBut be careful about choosing your target...\n\nThe weapon will be penalty for each hunting attempt");

        this.add("item.mynethersdelight.bullet_pepper", "Bullet Pepper");

        this.add("item.miners_delight.strider_stew_cup", "Strider Stew Cup");
        this.add("item.miners_delight.spicy_noodle_soup_cup", "Spicy Noodle Soup");
        this.add("item.miners_delight.spicy_hoglin_stew_cup", "Spicy Hoglin Stew Cup");
        this.add("item.miners_delight.rock_soup_cup", "Rock Soup Cup");
        this.add("item.miners_delight.egg_soup_cup", "Egg Soup Cup");
    }

    @Override
    public String getName() {
        return "Lang Entries";
    }

    public static String toTitleCase(String givenString, String regex) {
        String[] stringArray = givenString.split(regex);
        StringBuilder stringBuilder = new StringBuilder();
        String[] var4 = stringArray;
        int var5 = stringArray.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            String string = var4[var6];
            stringBuilder.append(Character.toUpperCase(string.charAt(0))).append(string.substring(1)).append(regex);
        }

        return stringBuilder.toString().trim().replaceAll(regex, " ").substring(0, stringBuilder.length() - 1);
    }

    public String correctBlockItemName(String name) {
        if (!name.endsWith("_bricks") && name.contains("bricks")) {
            name = name.replaceFirst("bricks", "brick");
        }

        if ((name.contains("_fence") || name.contains("_button")) && name.contains("planks")) {
            name = name.replaceFirst("_planks", "");
        }

        return name;
    }
}
