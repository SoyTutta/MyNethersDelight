//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package com.soytutta.mynethersdelight.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import com.soytutta.mynethersdelight.common.entity.StriderRockEntity;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MNDEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, "mynethersdelight");

    public static final Supplier<EntityType<StriderRockEntity>> STRIDER_ROCK =
            ENTITIES.register("strider_rock", () ->
                    EntityType.Builder.<StriderRockEntity>of(StriderRockEntity::new, MobCategory.MISC)
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("strider_rock"));
}