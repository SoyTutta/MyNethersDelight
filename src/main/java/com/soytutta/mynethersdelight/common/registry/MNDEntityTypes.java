//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package com.soytutta.mynethersdelight.common.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import com.soytutta.mynethersdelight.common.entity.StriderRockEntity;
public class MNDEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITIES;
    public static final RegistryObject<EntityType<StriderRockEntity>> STRIDER_ROCK;

    static {
        ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, "mynethersdelight");
        STRIDER_ROCK = ENTITIES.register("strider_rock", () -> {
            return EntityType.Builder.<StriderRockEntity>of(StriderRockEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("strider_rock");
        });
    }
}
