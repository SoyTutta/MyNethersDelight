//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.common.registry;

import com.soytutta.mynethersdelight.common.effect.GoodPungentEffect;
import com.soytutta.mynethersdelight.common.effect.PungentEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MNDEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, "mynethersdelight");
    public static final RegistryObject<MobEffect> GPUNGENT;
    public static final RegistryObject<MobEffect> BPUNGENT;

    public MNDEffects() {
    }

    static {
        GPUNGENT = EFFECTS.register("g_pungent", GoodPungentEffect::new);
        BPUNGENT = EFFECTS.register("b_pungent", PungentEffect::new);
    }
}
