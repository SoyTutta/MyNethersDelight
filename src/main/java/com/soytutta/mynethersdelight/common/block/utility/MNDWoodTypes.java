package com.soytutta.mynethersdelight.common.block.utility;

import com.soytutta.mynethersdelight.MyNethersDelight;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.WoodType;

public class MNDWoodTypes {
    public static final WoodType POWDERY_CANNON =
            WoodType.register(WoodType.create(new ResourceLocation(MyNethersDelight.MODID, "powdery_cannon").toString()));
}
