package com.soytutta.mynethersdelight.common;

import com.soytutta.mynethersdelight.common.entity.StriderRockEntity;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import com.soytutta.mynethersdelight.common.world.MNDWildCropGeneration;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class MNDCommonSetup {
    public MNDCommonSetup() {
    }

    public static void init(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            registerDispenserBehaviors();
            registerCompostables();
            MNDWildCropGeneration.addMNDGeneration();
        });
    }
    public static void registerDispenserBehaviors() {
        DispenserBlock.registerBehavior(MNDItems.STRIDER_ROCK.get(), new AbstractProjectileDispenseBehavior() {
            protected Projectile getProjectile(Level pLevel, Position pPosition, ItemStack pStack) {
                return new StriderRockEntity(pLevel, pPosition.x(), pPosition.y(), pPosition.z());
            }
        });
    }
    public static void registerCompostables() {
        ComposterBlock.COMPOSTABLES.put(MNDItems.WARPED_FUNGUS_COLONY.get(), 1.0F);
        ComposterBlock.COMPOSTABLES.put(MNDItems.CRIMSON_FUNGUS_COLONY.get(), 1.0F);
        ComposterBlock.COMPOSTABLES.put(MNDItems.BULLET_PEPPER.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(MNDItems.STRIDER_EGG.get(), 0.4F);
    }
}

