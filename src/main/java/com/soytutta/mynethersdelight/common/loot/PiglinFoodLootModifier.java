package com.soytutta.mynethersdelight.common.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.soytutta.mynethersdelight.common.MNDConfiguration;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class PiglinFoodLootModifier extends LootModifier {
    public static final Supplier<Codec<PiglinFoodLootModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.create(instance -> codecStart(instance)
                    .and(ResourceLocation.CODEC.fieldOf("lootTable").forGetter(modifier -> modifier.lootTable))
                    .and(ResourceLocation.CODEC.fieldOf("lootTableWithoutFood").forGetter(modifier -> modifier.lootTableWithoutFood))
                    .apply(instance, PiglinFoodLootModifier::new)));

    private final ResourceLocation lootTable;
    private final ResourceLocation lootTableWithoutFood;

    protected PiglinFoodLootModifier(LootItemCondition[] conditions, ResourceLocation lootTable,
                                     ResourceLocation lootTableWithoutFood) {
        super(conditions);
        this.lootTable = lootTable;
        this.lootTableWithoutFood = lootTableWithoutFood;
    }

    @Nonnull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        boolean giveFood = MNDConfiguration.ENABLE_PIGLIN_FOOD_TRADES.get()
                && context.getRandom().nextDouble() < MNDConfiguration.PIGLIN_FOOD_TRADE_CHANCE.get();
        ResourceLocation selectedTable = giveFood ? this.lootTable : this.lootTableWithoutFood;
        LootTable table = context.getLevel().getServer().getLootData().getLootTable(selectedTable);
        ObjectArrayList<ItemStack> replacementLoot = new ObjectArrayList<>();
        table.getRandomItemsRaw(context, replacementLoot::add);
        if (!replacementLoot.isEmpty()) {
            if (!generatedLoot.isEmpty()) {
                generatedLoot.remove(0);
            }
            generatedLoot.addAll(0, replacementLoot);
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
