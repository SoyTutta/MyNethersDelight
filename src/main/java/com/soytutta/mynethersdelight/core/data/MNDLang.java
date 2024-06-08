//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.core.data;

import java.util.HashSet;
import java.util.Set;

import com.soytutta.mynethersdelight.common.registry.MNDBlocks;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;

public class MNDLang extends LanguageProvider {
    public MNDLang(PackOutput output) {
        super(output, "mynethersdelight", "en_us");
    }

    protected void addTranslations() {
        Set<RegistryObject<Block>> blocks = new HashSet<>(MNDBlocks.BLOCKS.getEntries());
        Set<RegistryObject<Item>> items = new HashSet<>(MNDItems.ITEMS.getEntries());
        blocks.remove(MNDBlocks.LETIOS_COMPOST);
        blocks.remove(MNDBlocks.POWDERY_BLOCK);
        blocks.remove(MNDBlocks.STRIPPED_POWDERY_BLOCK);
        blocks.remove(MNDBlocks.WALL_POWDERY_TORCH);
        blocks.remove(MNDBlocks.POWDERY_WALL_SIGN);
        blocks.remove(MNDBlocks.POWDERY_WALL_HANGING_SIGN);
        blocks.remove(MNDBlocks.STRIDERLOAF_BLOCK);
        blocks.remove(MNDBlocks.COLD_STRIDERLOAF_BLOCK);
        blocks.remove(MNDBlocks.GHASTA_WITH_CREAM_BLOCK);
        blocks.forEach((b) -> {
            String name = (b.get()).getDescriptionId().replaceFirst("block.mynethersdelight.", "");
            name = toTitleCase(this.correctBlockItemName(name), "_").replaceAll("Of", "of");
            this.add((b.get()).getDescriptionId(), name);
        });
        items.removeIf((i) -> i.get() instanceof BlockItem);
        items.forEach((i) -> {
            String name = (i.get()).getDescriptionId().replaceFirst("item.mynethersdelight.", "");
            name = toTitleCase(this.correctBlockItemName(name), "_").replaceAll("Of", "of");
            name = toTitleCase(this.correctBlockItemName(name), "_").replaceAll("And", "and");
            name = toTitleCase(this.correctBlockItemName(name), "_").replaceAll("With", "with");
            this.add(i.get().getDescriptionId(), name);
        });
        this.add("block.mynethersdelight.letios_compost", "Leteos Compost");
        this.add("block.mynethersdelight.powdery_block", "Block of Powdery Cannon");
        this.add("block.mynethersdelight.stripped_powdery_block", "Block of Stripped Powdery Cannon");
        this.add("block.mynethersdelight.striderloaf_block", "Striderloaf");
        this.add("block.mynethersdelight.cold_striderloaf_block", "Cold Striderloaf");
        this.add("block.mynethersdelight.ghasta_with_cream_block", "Ghasta with Cream");

        this.add("mynethersdelight.itemGroup.main", "My Nether's Delight");
        this.add("effect.mynethersdelight.g_pungent", "Pungent");
        this.add("effect.mynethersdelight.b_pungent", "Pungent");
        this.add("mynethersdelight.jei.forgoting", "Forgoting");
        this.add("mynethersdelight.jei.forgoting.nether", "Will only forget in the nether");
        this.add("mynethersdelight.jei.forgoting.accelerators", "Sped up by adjacent activators (see below)");
        this.add("mynethersdelight.jei.forgoting.light", "Sped up by adjacent flames (see below)");
        this.add("mynethersdelight.jei.forgoting.fluid", "Sped up by adjacent lava");
        this.add("mynethersdelight.block.feast.use_knife", "You need a Knife to cut this.");
        this.add("farmersdelight.tooltip.strider_egg", "Nourished by 1 Harmful Effect");
        this.add("farmersdelight.tooltip.hot_cream", "Burning Effects");
        this.add("farmersdelight.tooltip.strider_feed.when_feeding", "When fed to a Strider");

        this.add("mynethersdelight.jei.info.hot_cream", "It burns all active Effects converting them into Fire Resistance and Pungent.\n\nIf you serve it in a Cone it will only Burn one Effect!! Striders also prefer it served this way...");
        this.add("mynethersdelight.jei.info.strider_egg", "Wait... Are Striders Ovoviviparous? that's weird...\n\nAnyway, it's only obtainable by careful hunting.\nhard shell, it can only be eaten starry...");
        this.add("mynethersdelight.jei.info.hoglin_hide", "A great source of leather, it could also serve as a nice hunting trophy.\n\nIf you do not want to damage the leather, try to use something lighter than a Sword...");
        this.add("mynethersdelight.jei.info.r_soil", "Almost every plant or crop feels comfortable in this soil, if you want to moisten need fire instead of water.");
        this.add("mynethersdelight.jei.info.mushroom_colony", "Some Mushrooms form colonies when they are on top of Rich Soil or Resurgent Soil.");
        this.add("mynethersdelight.jei.info.fungus_colony", "Some Nethers Fungus form colonies when they are on top of Resurgent Soil.");
        this.add("mynethersdelight.jei.info.wild_powdery", "Powdery Canes are an invasive crop in Crimson Forests.\n\nIts Berries are explosive on Contact, try Cutting the red stem...");
        this.add("mynethersdelight.jei.info.plate_of_stuffed_hoglin", "It's astonishing how much Dishes can be obtained from such a Primitive Feast...");
        this.add("mynethersdelight.jei.info.plate_of_ghasta", "Is the ghast still alive?\nI took a portion recently, but it grew back.");
        this.add("mynethersdelight.jei.info.plate_of_striderloaf", "Once cooled, it's not so delicious...\nBut it's quite satiating.");

    }

    public String getName() {
        return "Lang Entries";
    }

    public static String toTitleCase(String givenString, String regex) {
        String[] stringArray = givenString.split(regex);
        StringBuilder stringBuilder = new StringBuilder();
        String[] var4 = stringArray;
        int var5 = stringArray.length;

        for(int var6 = 0; var6 < var5; ++var6) {
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
