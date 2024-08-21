package com.soytutta.mynethersdelight.common.item;

import com.soytutta.mynethersdelight.common.entity.StriderRockEntity;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.item.EggItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;

public class StriderRockItem extends Item implements ProjectileItem {
    public StriderRockItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EGG_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!level.isClientSide) {
            StriderRockEntity thrownEgg = new StriderRockEntity(level, player);
            thrownEgg.setItem(new ItemStack(MNDItems.STRIDER_ROCK.get()));
            thrownEgg.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(thrownEgg);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            itemStack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }

    @Override
    public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        StriderRockEntity thrownEgg = new StriderRockEntity(level, pos.x(), pos.y(), pos.z());
        thrownEgg.setItem(stack);
        return thrownEgg;
    }
}