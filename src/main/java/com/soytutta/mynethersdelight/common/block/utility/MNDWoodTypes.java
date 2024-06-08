package com.soytutta.mynethersdelight.common.block.utility;

import com.soytutta.mynethersdelight.MyNethersDelight;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public class MNDWoodTypes {
    public static final WoodType POWDERY =
            WoodType.register(new WoodType(MyNethersDelight.MODID + ":powdery", BlockSetType.BAMBOO, SoundType.BAMBOO_WOOD, SoundType.BAMBOO_WOOD_HANGING_SIGN, SoundEvents.BAMBOO_WOOD_FENCE_GATE_CLOSE, SoundEvents.BAMBOO_WOOD_FENCE_GATE_OPEN));
}
