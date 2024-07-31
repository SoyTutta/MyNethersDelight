//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package com.soytutta.mynethersdelight.client.event;

import com.soytutta.mynethersdelight.client.renderer.NetherStoveRenderer;
import com.soytutta.mynethersdelight.common.block.utility.MNDWoodTypes;
import com.soytutta.mynethersdelight.common.registry.MNDBlockEntityTypes;
import com.soytutta.mynethersdelight.common.registry.MNDBlocks;
import com.soytutta.mynethersdelight.common.registry.MNDEntityTypes;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(
        modid = "mynethersdelight",
        bus = EventBusSubscriber.Bus.MOD,
        value = {Dist.CLIENT}
)
public class ClientSetupEvents {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(MNDEntityTypes.STRIDER_ROCK.get(), ThrownItemRenderer::new);
        event.registerBlockEntityRenderer(MNDBlockEntityTypes.NETHER_STOVE.get(), NetherStoveRenderer::new);
    }

    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        BlockEntityRenderers.register(MNDBlockEntityTypes.MND_SIGN.get(), SignRenderer::new);
        BlockEntityRenderers.register(MNDBlockEntityTypes.MND_HSIGN.get(), HangingSignRenderer::new);
        event.enqueueWork(() -> {
            FlowerPotBlock pot = (FlowerPotBlock) Blocks.FLOWER_POT;
            pot.addPlant(BuiltInRegistries.BLOCK.getKey(MNDBlocks.POWDERY_CANNON.get()), MNDBlocks.POTTED_POWDERY_CANNON::get);
            pot.addPlant(BuiltInRegistries.BLOCK.getKey(MNDBlocks.BULLET_PEPPER.get()), MNDBlocks.POTTED_BULLET_PEPPER::get);
            Sheets.addWoodType(MNDWoodTypes.POWDERY);
        });
    }
}
