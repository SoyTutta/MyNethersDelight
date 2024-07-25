package com.soytutta.mynethersdelight.common.registry;

import com.soytutta.mynethersdelight.MyNethersDelight;
import com.soytutta.mynethersdelight.common.enchantment.HuntingEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MNDEnchantments {
    public static final DeferredRegister<Enchantment> DEF_REG = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, MyNethersDelight.MODID);
    public static final EnchantmentCategory HUNTING_WEAPON = EnchantmentCategory.create("hunting_weapon",
            (item -> item instanceof SwordItem || item instanceof AxeItem));
    public static final RegistryObject<Enchantment> HUNTING = DEF_REG.register("poaching", () -> new HuntingEnchantment( Enchantment.Rarity.VERY_RARE, HUNTING_WEAPON, EquipmentSlot.MAINHAND));
}