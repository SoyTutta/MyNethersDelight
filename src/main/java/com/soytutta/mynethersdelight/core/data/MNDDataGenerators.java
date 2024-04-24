//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.core.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
@Mod.EventBusSubscriber(
        modid = "mynethersdelight",
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class MNDDataGenerators {

    public MNDDataGenerators() {
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        MNDBlockTags blockTags = new MNDBlockTags(generator, "mynethersdelight", helper);
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new MNDItemTags(generator, blockTags, "mynethersdelight", helper));
        generator.addProvider(event.includeServer(), new MNDRecipes(generator));
        MNDBlockStates blockStates = new MNDBlockStates(generator, helper);
        generator.addProvider(event.includeClient(), blockStates);
        generator.addProvider(event.includeClient(), new MNDLang(generator));
    }
}
