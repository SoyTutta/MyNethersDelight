package com.soytutta.mynethersdelight.integration;

import com.soytutta.mynethersdelight.MyNethersDelight;
import net.mehvahdjukaar.moonlight.api.set.BlockSetAPI;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;

public class MNDEveryCompat {
    public static void registerCompat() {
        WoodType.Finder finder = WoodType.Finder.simple(MyNethersDelight.MODID,
                "powdery", "powdery_planks", "powdery_block");
        finder.addChild("stripped_log", "stripped_powdery_block");

        BlockSetAPI.addBlockTypeFinder(WoodType.class, finder);
    }
}