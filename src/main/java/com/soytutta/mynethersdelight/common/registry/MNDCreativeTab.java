package com.soytutta.mynethersdelight.common.registry;

import com.soytutta.mynethersdelight.MyNethersDelight;
import com.soytutta.mynethersdelight.common.utility.MNDTextUtils;
import net.minecraft.core.registries.*;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MNDCreativeTab {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MyNethersDelight.MODID);

    public static final DeferredHolder<CreativeModeTab,
            CreativeModeTab> MY_NETHERS_DELIGHT_TAB = TABS.register("main",
            () -> CreativeModeTab.builder()
                .title(MNDTextUtils.getTranslation("itemGroup.main"))
                .icon(MNDItems.NETHER_STOVE.get()::getDefaultInstance)
                .displayItems((parameters, output) -> {
                    output.accept(MNDItems.NETHER_BRICKS_CABINET.get());
                    output.accept(MNDItems.NETHER_STOVE.get());
                    output.accept(MNDItems.BULLET_PEPPER_CRATE.get());
                    output.accept(MNDItems.LETIOS_COMPOST.get());
                    output.accept(MNDItems.RESURGENT_SOIL.get());
                    output.accept(MNDItems.RESURGENT_SOIL_FARMLAND.get());

                    output.accept(MNDItems.POWDER_CANNON.get());
                    output.accept(MNDItems.POWDERY_TORCH.get());
                    output.accept(MNDItems.BLOCK_OF_POWDERY_CANNON.get());
                    output.accept(MNDItems.BLOCK_OF_STRIPPED_POWDERY_CANNON.get());
                    output.accept(MNDItems.POWDERY_CABINET.get());
                    output.accept(MNDItems.POWDERY_PLANKS.get());
                    output.accept(MNDItems.POWDERY_PLANKS_STAIRS.get());
                    output.accept(MNDItems.POWDERY_PLANKS_SLAB.get());
                    output.accept(MNDItems.POWDERY_MOSAIC.get());
                    output.accept(MNDItems.POWDERY_MOSAIC_STAIRS.get());
                    output.accept(MNDItems.POWDERY_MOSAIC_SLAB.get());

                    output.accept(MNDItems.POWDERY_FENCE.get());
                    output.accept(MNDItems.POWDERY_FENCE_GATE.get());

                    output.accept(MNDItems.POWDERY_DOOR.get());
                    output.accept(MNDItems.POWDERY_TRAPDOOR.get());
                    output.accept(MNDItems.POWDERY_PRESSURE_PLATE.get());
                    output.accept(MNDItems.POWDERY_BUTTON.get());
                    output.accept(MNDItems.POWDERY_SIGN.get());
                    output.accept(MNDItems.POWDERY_HANGING_SIGN.get());

                    output.accept(MNDItems.CRIMSON_FUNGUS_COLONY.get());
                    output.accept(MNDItems.WARPED_FUNGUS_COLONY.get());

                    output.accept(MNDItems.HOGLIN_TROPHY.get());
                    output.accept(MNDItems.WAXED_HOGLIN_TROPHY.get());
                    output.accept(MNDItems.ZOGLIN_TROPHY.get());
                    output.accept(MNDItems.SKOGLIN_TROPHY.get());

                    output.accept(MNDItems.HOGLIN_HIDE.get());

                    output.accept(MNDItems.HOGLIN_LOIN.get());
                    output.accept(MNDItems.HOGLIN_SAUSAGE.get());
                    output.accept(MNDItems.ROASTED_SAUSAGE.get());
                    output.accept(MNDItems.HOTDOG.get());
                    output.accept(MNDItems.SAUSAGE_AND_POTATOES.get());
                    output.accept(MNDItems.BREAKFAST_SAMPLER.get());
                    output.accept(MNDItems.COOKED_LOIN.get());
                    output.accept(MNDItems.BLUE_TENDERLOIN_STEAK.get());
                    output.accept(MNDItems.FRIED_HOGLIN_CHOP.get());

                    output.accept(MNDItems.RAW_STUFFED_HOGLIN.get());
                    output.accept(MNDItems.ROAST_STUFFED_HOGLIN.get());
                    output.accept(MNDItems.ROAST_EAR.get());
                    output.accept(MNDItems.PLATE_OF_STUFFED_HOGLIN_SNOUT.get());
                    output.accept(MNDItems.PLATE_OF_STUFFED_HOGLIN_HAM.get());
                    output.accept(MNDItems.PLATE_OF_STUFFED_HOGLIN.get());

                    output.accept(MNDItems.STRIDER_ROCK.get());
                    output.accept(MNDItems.STRIDER_EGG.get());
                    output.accept(MNDItems.BOILED_EGG.get());
                    output.accept(MNDItems.DEVILED_EGG.get());
                    output.accept(MNDItems.STRIDER_SLICE.get());
                    output.accept(MNDItems.MINCED_STRIDER.get());
                    output.accept(MNDItems.BLEEDING_TARTAR.get());
                    output.accept(MNDItems.STRIDER_WITH_GRILLED_FUNGUS.get());
                    output.accept(MNDItems.STRIDER_STEW.get());
                    output.accept(MNDItems.CRIMSON_STROGANOFF.get());
                    output.accept(MNDItems.STRIDERLOAF_BLOCK.get());
                    output.accept(MNDItems.STRIDERLOAF.get());
                    output.accept(MNDItems.COLD_STRIDERLOAF.get());

                    output.accept(MNDItems.GHASTA.get());
                    output.accept(MNDItems.GHASMATI.get());
                    output.accept(MNDItems.GHAST_DOUGH.get());
                    output.accept(MNDItems.GHAST_SALAD.get());
                    output.accept(MNDItems.SPICY_NOODLE_SOUP.get());
                    output.accept(MNDItems.SPICY_COTTON.get());
                    output.accept(MNDItems.GHASTA_WITH_CREAM_BLOCK.get());
                    output.accept(MNDItems.GHASTA_WITH_CREAM.get());

                    output.accept(MNDItems.BULLET_PEPPER.get());
                    output.accept(MNDItems.SPICY_SKEWER.get());
                    output.accept(MNDItems.CHILIDOG.get());
                    output.accept(MNDItems.SPICY_HOGLIN_STEW.get());
                    output.accept(MNDItems.HOT_WINGS.get());
                    output.accept(MNDItems.HOT_WINGS_BUCKET.get());
                    output.accept(MNDItems.SPICY_CURRY.get());

                    output.accept(MNDItems.ROCK_SOUP.get());
                    output.accept(MNDItems.BURNT_ROLL.get());
                    output.accept(MNDItems.MAGMA_CAKE.get());
                    output.accept(MNDItems.MAGMA_CAKE_SLICE.get());

                    output.accept(MNDItems.HOT_CREAM.get());
                    output.accept(MNDItems.HOT_CREAM_CONE.get());
                }
        ).build()
    );
}