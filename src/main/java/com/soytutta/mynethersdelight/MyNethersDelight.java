package com.soytutta.mynethersdelight;

import com.soytutta.mynethersdelight.client.event.ClientSetupEvents;
import com.soytutta.mynethersdelight.common.MNDCommonSetup;
import com.soytutta.mynethersdelight.common.registry.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(MyNethersDelight.MODID)
public class MyNethersDelight
{
    public static final String MODID = "mynethersdelight";
    public static final Logger LOGGER = LogManager.getLogger();

    public MyNethersDelight(IEventBus modEventBus) {
        modEventBus.addListener(MNDCommonSetup::init);
        if (FMLEnvironment.dist.isClient()) {
            modEventBus.addListener(ClientSetupEvents::init);
        }

        MNDItems.ITEMS.register(modEventBus);
        MNDBlocks.BLOCKS.register(modEventBus);
        MNDEffects.EFFECTS.register(modEventBus);
        MNDBlockEntityTypes.TILES.register(modEventBus);
        MNDEntityTypes.ENTITIES.register(modEventBus);
        MNDCreativeTab.TABS.register(modEventBus);
        MNDBiomeFeatures.FEATURES.register(modEventBus);
        MNDLootModifiers.LOOT_MODIFIERS.register(modEventBus);
    }
}