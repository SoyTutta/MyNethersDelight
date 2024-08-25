package com.soytutta.mynethersdelight.common.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.soytutta.mynethersdelight.common.block.MagmaCakeBlock;
import com.soytutta.mynethersdelight.common.block.StuffedHoglinBlock;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import java.util.function.Supplier;

public class MNDEspecialLootModifier extends LootModifier
{
    public static final Supplier<MapCodec<MNDEspecialLootModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.mapCodec(inst -> codecStart(inst)
                    .and(BuiltInRegistries.ITEM.byNameCodec().fieldOf("drop").forGetter((m) -> m.especialDrop))
                    .apply(inst, MNDEspecialLootModifier::new)));

    private final Item especialDrop;

    protected MNDEspecialLootModifier(LootItemCondition[] conditionsIn, Item especialDropIn) {
        super(conditionsIn);
        this.especialDrop = especialDropIn;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        BlockState state = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        if (state != null) {
            Block targetBlock = state.getBlock();

            if (targetBlock instanceof MagmaCakeBlock) {
                int bites = state.getValue(MagmaCakeBlock.BITES);
                int count = state.getValue(MagmaCakeBlock.SECOND_CAKE) ? 14 - bites : 7 - bites;
                generatedLoot.add(new ItemStack(this.especialDrop, count));
            }
        }
        return generatedLoot;
    }


    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}