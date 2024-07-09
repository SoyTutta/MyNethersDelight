package com.soytutta.mynethersdelight;

import com.mojang.serialization.Codec;
import com.soytutta.mynethersdelight.common.MNDCommonSetup;
import com.soytutta.mynethersdelight.common.loot.RemplaceLootModifier;
import com.soytutta.mynethersdelight.common.registry.*;
import com.soytutta.mynethersdelight.integration.MNDEveryCompat;
import com.soytutta.mynethersdelight.integration.addonsdelight.MNDItemsMD;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(MyNethersDelight.MODID)
public class MyNethersDelight
{
    public static final String MODID = "mynethersdelight";
    public static final Logger LOGGER = LogManager.getLogger();

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
        registerLootSerializers(modEventBus);
        if (ModList.get().isLoaded("miners_delight")) {
            MNDItemsMD.ITEMS.register(modEventBus);
        }
        if (ModList.get().isLoaded("moonlight")) {
            MNDEveryCompat.registerCompat();
        }
    }
    void registerLootSerializers(IEventBus bus) {
        DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MyNethersDelight.MODID);
        RegistryObject<Codec<RemplaceLootModifier>> REMPLACE_ITEM = LOOT.register("remplace_item", RemplaceLootModifier.CODEC);

        LOOT.register(bus);
    }
}