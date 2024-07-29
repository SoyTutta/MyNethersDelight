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
import net.neoforged.neoforge.common.data.LanguageProvider;

public class MNDLang extends LanguageProvider {
    private final Set<String> addedKeys = new HashSet<>();

    public MNDLang(PackOutput output) {
        super(output, "mynethersdelight", "en_us");
    }

    protected void addTranslations() {
        Set<Supplier<Block>> blocks = MNDBlocks.BLOCKS.getEntries().stream()
                .map(entry -> (Supplier<Block>) entry::get)
                .collect(Collectors.toSet());
        Set<Supplier<Item>> items = MNDItems.ITEMS.getEntries().stream()
                .map(entry -> (Supplier<Item>) entry::get)
                .collect(Collectors.toSet());

        blocks.remove(MNDBlocks.LETIOS_COMPOST);
        blocks.remove(MNDBlocks.BLOCK_OF_POWDERY_CANNON);
        blocks.remove(MNDBlocks.BLOCK_OF_STRIPPED_POWDERY_CANNON);
        blocks.remove(MNDBlocks.WALL_POWDERY_TORCH);
        blocks.remove(MNDBlocks.POWDERY_WALL_SIGN);
        blocks.remove(MNDBlocks.POWDERY_WALL_HANGING_SIGN);
        blocks.remove(MNDBlocks.STRIDERLOAF_BLOCK);
        blocks.remove(MNDBlocks.COLD_STRIDERLOAF_BLOCK);
        blocks.remove(MNDBlocks.GHASTA_WITH_CREAM_BLOCK);

        blocks.forEach((b) -> {
            String descriptionId = (b.get()).getDescriptionId();
            if (addedKeys.add(descriptionId)) {
                String name = descriptionId.replaceFirst("block.mynethersdelight.", "");
                name = toTitleCase(this.correctBlockItemName(name), "_").replaceAll("Of", "of");
                this.add(descriptionId, name);
            }
        });

        items.removeIf((i) -> i.get() instanceof BlockItem);
        items.forEach((i) -> {
            String descriptionId = i.get().getDescriptionId();
            if (addedKeys.add(descriptionId)) {
                String name = descriptionId.replaceFirst("item.mynethersdelight.", "");
                name = toTitleCase(this.correctBlockItemName(name), "_").replaceAll("Of", "of");
                name = toTitleCase(this.correctBlockItemName(name), "_").replaceAll("And", "and");
                name = toTitleCase(this.correctBlockItemName(name), "_").replaceAll("With", "with");
                this.add(descriptionId, name);
            }
        });

        addCustomTranslations();
    }

    private void addCustomTranslations() {
        addNotPresent("block.mynethersdelight.letios_compost", "Leteos Compost");
        addNotPresent("block.mynethersdelight.powdery_block", "Block of Powdery Cannon");
        addNotPresent("block.mynethersdelight.stripped_powdery_block", "Block of Stripped Powdery Cannon");
        addNotPresent("block.mynethersdelight.striderloaf_block", "Striderloaf");
        addNotPresent("block.mynethersdelight.cold_striderloaf_block", "Cold Striderloaf");
        addNotPresent("block.mynethersdelight.ghasta_with_cream_block", "Ghasta with Cream");

        addNotPresent("mynethersdelight.itemGroup.main", "My Nether's Delight");
        addNotPresent("effect.mynethersdelight.g_pungent", "Pungent");
        addNotPresent("effect.mynethersdelight.g_pungent.desc", "Regenerate with heat");
        addNotPresent("effect.mynethersdelight.b_pungent", "Pungent");
        addNotPresent("effect.mynethersdelight.b_pungent.desc", "Increases heat sensitivity and prevents burns when leaving the heat zone");
        addNotPresent("mynethersdelight.jei.forgoting", "Forgoting");
        addNotPresent("mynethersdelight.jei.forgoting.nether", "Will only forget in the nether");
        addNotPresent("mynethersdelight.jei.forgoting.accelerators", "Sped up by adjacent activators (see below)");
        addNotPresent("mynethersdelight.jei.forgoting.light", "Sped up by adjacent flames (see below)");
        addNotPresent("mynethersdelight.jei.forgoting.fluid", "Sped up by adjacent lava");
        addNotPresent("mynethersdelight.block.feast.use_knife", "You need a Knife to cut this.");
        addNotPresent("farmersdelight.tooltip.strider_egg", "Nourished by 1 Harmful Effect");
        addNotPresent("farmersdelight.tooltip.hot_cream", "Burning Effects");
        addNotPresent("farmersdelight.tooltip.strider_feed.when_feeding", "When fed to a Strider");
        addNotPresent("farmersdelight.tooltip.magma_cake_slice", "Spicy frog Snack");

        addNotPresent("mynethersdelight.jei.info.hot_cream", "It burns all active Effects converting them into Fire Resistance and Pungent.\n\nIf you serve it in a Cone it will only Burn one Effect!! Striders also prefer it served this way...");
        addNotPresent("mynethersdelight.jei.info.strider_egg", "Wait... Are Striders Ovoviviparous? that's weird...\n\nAnyway, it's only obtainable by careful hunting.\nhard shell, it can only be eaten starry...");
        addNotPresent("mynethersdelight.jei.info.hoglin_hide", "A great source of leather, it could also serve as a nice hunting trophy.\n\nIf you do not want to damage the leather, try to use something lighter than a Sword...");
        addNotPresent("mynethersdelight.jei.info.r_soil", "Almost every plant or crop feels comfortable in this soil, if you want to moisten need fire instead of water.");
        addNotPresent("mynethersdelight.jei.info.mushroom_colony", "Some Mushrooms form colonies when they are on top of Rich Soil or Resurgent Soil.");
        addNotPresent("mynethersdelight.jei.info.fungus_colony", "Some Nethers Fungus form colonies when they are on top of Resurgent Soil.");
        addNotPresent("mynethersdelight.jei.info.wild_powdery", "Powdery Canes are an invasive crop in Crimson Forests.\n\nIts Berries are explosive on Contact, try Cutting the red stem...");
        addNotPresent("mynethersdelight.jei.info.plate_of_stuffed_hoglin", "It's astonishing how much Dishes can be obtained from such a Primitive Feast...");
        addNotPresent("mynethersdelight.jei.info.plate_of_ghasta", "Is the ghast still alive?\nI took a portion recently, but it grew back.");
        addNotPresent("mynethersdelight.jei.info.plate_of_striderloaf", "Once cooled, it takes away your appetite.");

        addNotPresent("enchantment.mynethersdelight.poaching", "Poaching");
        addNotPresent("enchantment.mynethersdelight.poaching.desc", "Responsible hunting can provide you with extra ingredients!!!\nBut be careful about choosing your target...");

        addNotPresent("item.miners_delight.strider_stew_cup", "Strider Stew Cup");
        addNotPresent("item.miners_delight.spicy_noodle_soup_cup", "Spicy Noodle Soup");
        addNotPresent("item.miners_delight.spicy_hoglin_stew_cup", "Spicy Hoglin Stew Cup");
        addNotPresent("item.miners_delight.rock_soup_cup", "Rock Soup Cup");
    }

    private void addNotPresent(String key, String translation) {
        if (addedKeys.add(key)) {
            this.add(key, translation);
        }
    }

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