//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.soytutta.mynethersdelight.common.registry;

import com.soytutta.mynethersdelight.common.effect.GoodPungentEffect;
import com.soytutta.mynethersdelight.common.effect.PungentEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MNDEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, "mynethersdelight");
    public static final Holder<MobEffect> GPUNGENT = EFFECTS.register("g_pungent", GoodPungentEffect::new);
    public static final Holder<MobEffect> BPUNGENT = EFFECTS.register("b_pungent", PungentEffect::new);
}
