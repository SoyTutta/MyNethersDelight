package com.soytutta.mynethersdelight.common.registry;

import com.soytutta.mynethersdelight.common.block.entity.MNDHangingSignBlockEntity;
import com.soytutta.mynethersdelight.common.block.entity.MNDSignBlockEntity;
import com.soytutta.mynethersdelight.common.block.entity.NetherStoveBlockEntity;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MNDBlockEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, "mynethersdelight");
    public static final RegistryObject<BlockEntityType<NetherStoveBlockEntity>> NETHER_STOVE;
    public static final RegistryObject<BlockEntityType<MNDSignBlockEntity>> MND_SIGN;
    public static final RegistryObject<BlockEntityType<MNDHangingSignBlockEntity>> MND_HSIGN;

    public MNDBlockEntityTypes() {
    }

    static {
        NETHER_STOVE = TILES.register("nether_stove", () -> {
            return BlockEntityType.Builder.of(NetherStoveBlockEntity::new, new Block[]{MNDBlocks.NETHER_STOVE.get()}).build(null);
        });
        MND_SIGN = TILES.register("mnd_sign", () -> {
            return BlockEntityType.Builder.of(MNDSignBlockEntity::new, MNDBlocks.POWDERY_SIGN.get(), MNDBlocks.POWDERY_WALL_SIGN.get()).build((null));
        });
        MND_HSIGN = TILES.register("mnd_hsign", () -> {
            return BlockEntityType.Builder.of(MNDHangingSignBlockEntity::new, MNDBlocks.POWDERY_HANGING_SIGN.get(), MNDBlocks.POWDERY_WALL_HANGING_SIGN.get()).build((null));
        });
    }
}
