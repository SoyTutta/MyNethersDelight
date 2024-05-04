package com.soytutta.mynethersdelight.common.registry;

import com.soytutta.mynethersdelight.MyNethersDelight;
import net.minecraft.core.registries.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.*;

public class MNDCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MyNethersDelight.MODID);

    public static final RegistryObject<CreativeModeTab> TAB_MY_NETHERS_DELIGHT = CREATIVE_TABS.register(MyNethersDelight.MODID,
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.my_nethers_delight"))
                    .icon(() -> new ItemStack(MNDBlocks.NETHER_STOVE.get()))
                    .build());
}