package com.soytutta.mynethersdelight.common.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class RemplaceLootModifier extends LootModifier {
    private final Item replacedItem;
    private final Item newItem;
    private final EntityType<?> entity;

    public static final Supplier<Codec<RemplaceLootModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst)
            .and(
                    inst.group(
                            ForgeRegistries.ITEMS.getCodec().fieldOf("replaces").forGetter((m) -> m.replacedItem),
                            ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter((m) -> m.newItem),
                            ForgeRegistries.ENTITY_TYPES.getCodec().fieldOf("entity").forGetter((m) -> m.entity)
                    )
            )
            .apply(inst, RemplaceLootModifier::new)));

    protected RemplaceLootModifier(LootItemCondition[] conditionsIn, Item replacedItem, Item newItem, EntityType<?> entity) {
        super(conditionsIn);
        this.replacedItem = replacedItem;
        this.newItem = newItem;
        this.entity = entity;
    }

    @Nonnull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        Entity t = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (t == null || t.getType() != this.entity) return generatedLoot;
        int amountOfItems = 0;
        for (ItemStack i : generatedLoot) {
            if (i.getItem() == replacedItem) {
                amountOfItems += i.getCount();
            }
        }
        generatedLoot.removeIf(i -> i.getItem() == replacedItem);
        generatedLoot.add(new ItemStack(newItem, amountOfItems));
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec()
    {
        return CODEC.get();
    }
}
