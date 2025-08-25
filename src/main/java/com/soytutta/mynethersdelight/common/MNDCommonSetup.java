package com.soytutta.mynethersdelight.common;

import com.soytutta.mynethersdelight.common.enchantment.PoachingFailureRegistry;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

public class MNDCommonSetup {

    public static void init(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            PoachingFailureRegistry.registerAll();
            registerDispenserBehaviors();
        });
    }

    public static void registerDispenserBehaviors() {
        DispenserBlock.registerProjectileBehavior(MNDItems.STRIDER_ROCK.get());
    }
}