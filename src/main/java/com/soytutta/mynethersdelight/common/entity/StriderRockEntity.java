
package com.soytutta.mynethersdelight.common.entity;

import com.soytutta.mynethersdelight.common.registry.MNDEntityTypes;
import com.soytutta.mynethersdelight.common.registry.MNDItems;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class StriderRockEntity extends ThrowableItemProjectile {
    public StriderRockEntity(EntityType<StriderRockEntity> StriderRockEntityType, Level level) {
        super(StriderRockEntityType, level);
    }

    public StriderRockEntity(Level level, LivingEntity livingEntity) {
        super(MNDEntityTypes.STRIDER_ROCK.get(), livingEntity, level);
    }

    public StriderRockEntity(Level level, double d, double e, double f) {
        super(MNDEntityTypes.STRIDER_ROCK.get(), d, e, f, level);
    }

    public void handleEntityEvent(byte id) {
        ItemStack entityStack = new ItemStack(this.getDefaultItem());
        if (id == 3) {
            ParticleOptions iparticledata = new ItemParticleOption(ParticleTypes.ITEM, entityStack);

            for(int i = 0; i < 12; ++i) {
                this.level.addParticle(iparticledata, this.getX(), this.getY(), this.getZ(), ((double)this.random.nextFloat() * 2.0 - 1.0) * 0.10000000149011612, ((double)this.random.nextFloat() * 2.0 - 1.0) * 0.10000000149011612 + 0.10000000149011612, ((double)this.random.nextFloat() * 2.0 - 1.0) * 0.10000000149011612);
            }
        }

    }

    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        result.getEntity().hurt(DamageSource.thrown(this, this.getOwner()), 1.0F);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level.isClientSide) {
            this.playSound(SoundEvents.STONE_BREAK, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            boolean striderSpawned = false;
            if (this.random.nextInt(16) == 0) {
                int i = 1;
                if (this.random.nextInt(64) == 0) {
                    i = 4;
                }

                for (int j = 0; j < i; ++j) {
                    Strider strider = EntityType.STRIDER.create(this.level);
                    if (strider != null) {
                        strider.setAge(-24000);
                        strider.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                        this.level.addFreshEntity(strider);
                        striderSpawned = true;
                    }
                }
            }

            if (!striderSpawned) {
                if (this.random.nextInt(5) == 0) {
                    int i = 1;
                    for (int j = 0; j < i; ++j) {
                        ItemEntity stickItem = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), new ItemStack(MNDItems.STRIDER_EGG.get()));
                        this.level.addFreshEntity(stickItem);
                    }
                }
            }

            this.level.broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }

    @Override
    protected Item getDefaultItem() {
        return MNDItems.STRIDER_ROCK.get();
    }
}