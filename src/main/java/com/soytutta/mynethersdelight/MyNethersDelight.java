package com.soytutta.mynethersdelight;

import com.soytutta.mynethersdelight.client.event.ClientSetupEvents;
import com.soytutta.mynethersdelight.common.MNDConfiguration;
import com.soytutta.mynethersdelight.common.MNDCommonSetup;
import com.soytutta.mynethersdelight.common.events.CommonEvent;
import com.soytutta.mynethersdelight.common.data.MNDDataMapTypes;
import com.soytutta.mynethersdelight.common.registry.*;
import com.soytutta.mynethersdelight.core.data.MNDDataGenerators;
import com.soytutta.mynethersdelight.integration.MinerDelight.MDItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(MyNethersDelight.MODID)
public class MyNethersDelight
{
    public static final String MODID = "mynethersdelight";
    public static final Logger LOGGER = LogManager.getLogger();

    public MyNethersDelight(IEventBus modEventBus, ModContainer modContainer) {
        MNDConfiguration.register(modContainer);
        modEventBus.addListener(MNDCommonSetup::init);
        modEventBus.addListener(MNDBlockEntityTypes::addCabinetsBlockEntities);
        modEventBus.addListener(MNDDataMapTypes::register);
        modEventBus.addListener(MNDDataGenerators::gatherData);
        NeoForge.EVENT_BUS.addListener(CommonEvent::livingDie);
        NeoForge.EVENT_BUS.addListener(CommonEvent::onMobDrop);
        NeoForge.EVENT_BUS.addListener(CommonEvent::onFrogMagmaCakeInteraction);
        if (FMLEnvironment.dist.isClient()) {
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
            modEventBus.addListener(ClientSetupEvents::init);
            modEventBus.addListener(ClientSetupEvents::onRegisterRenderers);
        }

        MNDItems.ITEMS.register(modEventBus);

        if (ModList.get().isLoaded("minersdelight")) {
            MDItems.register();
        }

        MNDBlocks.BLOCKS.register(modEventBus);
        MNDEffects.EFFECTS.register(modEventBus);
        MNDBlockEntityTypes.TILES.register(modEventBus);
        MNDEntityTypes.ENTITIES.register(modEventBus);
        MNDCreativeTab.TABS.register(modEventBus);
        MNDBiomeFeatures.FEATURES.register(modEventBus);
        MNDLootModifiers.LOOT_MODIFIERS.register(modEventBus);
        MNDConditionCodecs.CONDITION_CODECS.register(modEventBus);
        MNDRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
        MNDEnchantmentComponents.ENCHANTMENT_EFFECT_COMPONENTS.register(modEventBus);
    }
}
