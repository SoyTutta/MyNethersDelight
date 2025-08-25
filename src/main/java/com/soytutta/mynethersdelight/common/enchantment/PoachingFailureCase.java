package com.soytutta.mynethersdelight.common.enchantment;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public class PoachingFailureCase {
    private final EntityType<? extends Mob> sourceType;
    private final EntityType<? extends Mob> targetType;
    private final SoundEvent transformSound;
    private final BiPredicate<Mob, ItemStack> condition;
    private final BiConsumer<Mob, Mob> setupAction;

    public PoachingFailureCase(EntityType<? extends Mob> sourceType, EntityType<? extends Mob> targetType, SoundEvent transformSound, BiPredicate<Mob, ItemStack> condition, BiConsumer<Mob, Mob> setupAction) {
        this.sourceType = sourceType;
        this.targetType = targetType;
        this.transformSound = transformSound;
        this.condition = condition;
        this.setupAction = setupAction;
    }

    public EntityType<? extends Mob> getTargetType() { return targetType; }
    public SoundEvent getTransformSound() { return transformSound; }
    public BiConsumer<Mob, Mob> getSetupAction() { return setupAction; }

    public boolean matches(Mob mob, ItemStack weapon) {
        return mob.getType() == this.sourceType && this.condition.test(mob, weapon);
    }
}