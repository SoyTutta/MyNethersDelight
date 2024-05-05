package com.soytutta.mynethersdelight;

import com.soytutta.mynethersdelight.common.MNDCommonSetup;
import com.soytutta.mynethersdelight.common.registry.*;
import com.soytutta.mynethersdelight.core.data.worldgen.MNDConfiguredFeatures;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MyNethersDelight.MODID)
public class MyNethersDelight
{
    public static final String MODID = "mynethersdelight";

    public MyNethersDelight() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(MNDCommonSetup::init);
        MNDItems.ITEMS.register(modEventBus);
        MNDBlocks.BLOCKS.register(modEventBus);
        MNDEffects.EFFECTS.register(modEventBus);
        MNDBlockEntityTypes.TILES.register(modEventBus);
        MNDEntityTypes.ENTITIES.register(modEventBus);
        MNDCreativeTab.TABS.register(modEventBus);
        MNDBiomeFeatures.FEATURES.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }
}