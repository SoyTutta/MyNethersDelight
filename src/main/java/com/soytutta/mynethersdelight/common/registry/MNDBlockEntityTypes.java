package com.soytutta.mynethersdelight.common.registry;

import com.soytutta.mynethersdelight.common.block.entity.BlazierBlockEntity;
import com.soytutta.mynethersdelight.common.block.entity.MNDHangingSignBlockEntity;
import com.soytutta.mynethersdelight.common.block.entity.MNDSignBlockEntity;
import com.soytutta.mynethersdelight.common.block.entity.NetherStoveBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;

import java.util.function.Supplier;

public class MNDBlockEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, "mynethersdelight");

    public static final Supplier<BlockEntityType<NetherStoveBlockEntity>> NETHER_STOVE = TILES.register("nether_stove", () ->
            BlockEntityType.Builder.of(NetherStoveBlockEntity::new,
                    new Block[]{MNDBlocks.NETHER_STOVE.get()}).build(null)
    );
    public static final Supplier<BlockEntityType<MNDSignBlockEntity>> MND_SIGN = TILES.register("mnd_sign", () ->
            BlockEntityType.Builder.of(MNDSignBlockEntity::new,
                    MNDBlocks.POWDERY_SIGN.get(), MNDBlocks.POWDERY_WALL_SIGN.get()).build((null))
    );
    public static final Supplier<BlockEntityType<MNDHangingSignBlockEntity>> MND_HSIGN = TILES.register("mnd_hsign", () ->
            BlockEntityType.Builder.of(MNDHangingSignBlockEntity::new,
                    MNDBlocks.POWDERY_HANGING_SIGN.get(), MNDBlocks.POWDERY_WALL_HANGING_SIGN.get()).build((null))
    );
    public static final Supplier<BlockEntityType<BlazierBlockEntity>> BLAZIER = TILES.register("blazier", () ->
            BlockEntityType.Builder.of(BlazierBlockEntity::new,
                    new Block[]{MNDBlocks.BLAZIER_BLOCK.get()}).build(null)
    );

    public static void addCabinetsBlockEntities(BlockEntityTypeAddBlocksEvent event) {
        event.modify(ModBlockEntityTypes.CABINET.get(),
                MNDBlocks.NETHER_BRICKS_CABINET.get(),
                MNDBlocks.RED_NETHER_BRICKS_CABINET.get(),
                MNDBlocks.BLACKSTONE_BRICKS_CABINET.get(),
                MNDBlocks.POWDERY_CABINET.get()
        );
    }
}
