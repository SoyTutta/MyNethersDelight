package com.soytutta.mynethersdelight.common.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.soytutta.mynethersdelight.common.MNDConfiguration;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

import static net.minecraft.world.level.storage.loot.LootTable.createStackSplitter;

public class PiglinFoodLootModifier extends AddTableLootModifier
{
    public static final Supplier<MapCodec<PiglinFoodLootModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.mapCodec(inst -> codecStart(inst)
                    .and(ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("lootTable").forGetter(modifier -> modifier.lootTable))
                    .and(ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("lootTableWithoutFood").forGetter(modifier -> modifier.lootTableWithoutFood))
                    .apply(inst, PiglinFoodLootModifier::new)));

    private final ResourceKey<LootTable> lootTable;
    private final ResourceKey<LootTable> lootTableWithoutFood;

    protected PiglinFoodLootModifier(LootItemCondition[] conditions, ResourceKey<LootTable> lootTable,
                                     ResourceKey<LootTable> lootTableWithoutFood) {
        super(conditions, lootTable);
        this.lootTable = lootTable;
        this.lootTableWithoutFood = lootTableWithoutFood;
    }

    @Nonnull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        boolean giveFood = MNDConfiguration.ENABLE_PIGLIN_FOOD_TRADES.get()
                && context.getRandom().nextDouble() < MNDConfiguration.PIGLIN_FOOD_TRADE_CHANCE.get();
        ResourceKey<LootTable> selectedTable = giveFood ? this.lootTable : this.lootTableWithoutFood;
        ObjectArrayList<ItemStack> replacementLoot = new ObjectArrayList<>();
        context.getResolver().get(Registries.LOOT_TABLE, selectedTable).ifPresent(extraTable ->
                extraTable.value().getRandomItemsRaw(context, createStackSplitter(context.getLevel(), replacementLoot::add)));
        if (!replacementLoot.isEmpty()) {
            if (!generatedLoot.isEmpty()) {
                generatedLoot.remove(0);
            }
            generatedLoot.addAll(0, replacementLoot);
        }
        return generatedLoot;
    }
}
