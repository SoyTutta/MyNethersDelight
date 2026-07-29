package com.soytutta.mynethersdelight.common.block.entity;

import com.soytutta.mynethersdelight.common.MNDConfiguration;
import com.soytutta.mynethersdelight.common.block.BlazierBlock;
import com.soytutta.mynethersdelight.common.registry.MNDBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.Nullable;
import java.util.Optional;

public class BlazierBlockEntity extends BlockEntity implements Clearable {
    private static final int BURN_COOL_SPEED = 2;
    private static final int NUM_SLOTS = 4;
    private static final float[][] FOUR_SLOT_OFFSETS = {
            {-0.25F, -0.25F},
            {0.25F, -0.25F},
            {-0.25F, 0.25F},
            {0.25F, 0.25F}
    };
    private static final float TWO_SLOT_OFFSET = 0.22F;

    private final NonNullList<ItemStack> items = NonNullList.withSize(NUM_SLOTS, ItemStack.EMPTY);
    private final int[] cookingProgress = new int[NUM_SLOTS];
    private final int[] cookingTime = new int[NUM_SLOTS];

    private final RecipeManager.CachedCheck<Container, BlastingRecipe> blastingCheck =
            RecipeManager.createCheck(RecipeType.BLASTING);
    private final RecipeManager.CachedCheck<Container, SmeltingRecipe> smeltingCheck =
            RecipeManager.createCheck(RecipeType.SMELTING);
    private final RecipeManager.CachedCheck<Container, CampfireCookingRecipe> campfireCheck =
            RecipeManager.createCheck(RecipeType.CAMPFIRE_COOKING);
    private final RecipeManager.CachedCheck<Container, SmokingRecipe> smokingCheck =
            RecipeManager.createCheck(RecipeType.SMOKING);

    public BlazierBlockEntity(BlockPos pos, BlockState state) {
        super(MNDBlockEntityTypes.BLAZIER.get(), pos, state);
    }

    public static int getMaxSlots(BlazierBlock.HeatLevel heat) {
        return switch (heat) {
            case SMELTING, SMOKING -> 2;
            case BAKING, CAMPFIRE -> 4;
        };
    }

    public static void cookTick(Level level, BlockPos pos, BlockState state, BlazierBlockEntity blockEntity) {
        BlazierBlock.HeatLevel heat = state.getValue(BlazierBlock.HEAT);
        int maxSlots = getMaxSlots(heat);
        boolean dirty = false;

        for (int i = maxSlots; i < NUM_SLOTS; i++) {
            if (!blockEntity.items.get(i).isEmpty()) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), blockEntity.items.get(i));
                blockEntity.items.set(i, ItemStack.EMPTY);
                blockEntity.cookingProgress[i] = 0;
                dirty = true;
            }
        }

        for (int i = 0; i < maxSlots; i++) {
            ItemStack stack = blockEntity.items.get(i);
            if (stack.isEmpty()) {
                continue;
            }
            dirty = true;

            SimpleContainer input = new SimpleContainer(stack);
            Optional<? extends AbstractCookingRecipe> recipe = blockEntity.getRecipeFor(input, heat, level);
            if (recipe.isPresent()) {
                blockEntity.cookingProgress[i]++;
                if (blockEntity.cookingProgress[i] >= blockEntity.cookingTime[i]) {
                    ItemStack result = recipe.get().assemble(input, level.registryAccess());
                    if (result.isItemEnabled(level.enabledFeatures())) {
                        Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), result);
                        blockEntity.items.set(i, ItemStack.EMPTY);
                        blockEntity.cookingProgress[i] = 0;
                        level.sendBlockUpdated(pos, state, state, 3);
                        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
                    }
                }
            }
        }

        if (dirty) {
            setChanged(level, pos, state);
        }
    }

    public static void cooldownTick(Level level, BlockPos pos, BlockState state, BlazierBlockEntity blockEntity) {
        boolean dirty = false;
        for (int i = 0; i < blockEntity.items.size(); i++) {
            if (blockEntity.cookingProgress[i] > 0) {
                dirty = true;
                blockEntity.cookingProgress[i] =
                        Mth.clamp(blockEntity.cookingProgress[i] - BURN_COOL_SPEED, 0, blockEntity.cookingTime[i]);
            }
        }
        if (dirty) {
            setChanged(level, pos, state);
        }
    }

    public static void particleTick(Level level, BlockPos pos, BlockState state, BlazierBlockEntity blockEntity) {
        RandomSource random = level.random;
        BlazierBlock.HeatLevel heat = state.getValue(BlazierBlock.HEAT);
        Direction facing = state.getValue(BlazierBlock.FACING);
        int maxSlots = getMaxSlots(heat);

        if (random.nextFloat() < 0.11F) {
            spawnByHeat(level, pos, random, heat);
        }
        spawnItemSmoke(level, pos, facing, heat, blockEntity, random, maxSlots);
    }

    private static void spawnByHeat(Level level, BlockPos pos, RandomSource random,
                                    BlazierBlock.HeatLevel heat) {
        spawnAmbientParticle(level, pos, random, ParticleTypes.SMOKE);
        if (heat.ordinal() >= BlazierBlock.HeatLevel.CAMPFIRE.ordinal()) {
            spawnAmbientParticle(level, pos, random, ParticleTypes.SMOKE);
            spawnAmbientParticle(level, pos, random, ParticleTypes.LARGE_SMOKE);
        }
        if (heat.ordinal() >= BlazierBlock.HeatLevel.BAKING.ordinal()) {
            spawnAmbientParticle(level, pos, random, ParticleTypes.SMOKE);
            spawnAmbientParticle(level, pos, random, ParticleTypes.LARGE_SMOKE);
        }
        if (heat == BlazierBlock.HeatLevel.SMELTING) {
            spawnAmbientParticle(level, pos, random, ParticleTypes.LARGE_SMOKE);
            spawnAmbientParticle(level, pos, random, ParticleTypes.LARGE_SMOKE);
        }
    }

    private static void spawnItemSmoke(Level level, BlockPos pos, Direction facing,
                                       BlazierBlock.HeatLevel heat, BlazierBlockEntity blockEntity,
                                       RandomSource random, int maxSlots) {
        if (maxSlots == 2) {
            Direction perpendicular = facing.getClockWise();
            float y = pos.getY() + (heat == BlazierBlock.HeatLevel.SMOKING ? 0.15F : 0.386F);
            float[] offsets = {-TWO_SLOT_OFFSET, TWO_SLOT_OFFSET};
            for (int index = 0; index < 2; index++) {
                if (blockEntity.items.get(index).isEmpty() || random.nextFloat() >= 0.2F) {
                    continue;
                }
                spawnSmoke(level,
                        pos.getX() + 0.5 + perpendicular.getStepX() * offsets[index],
                        y,
                        pos.getZ() + 0.5 + perpendicular.getStepZ() * offsets[index]);
            }
        } else {
            float y = pos.getY() + 0.386F;
            for (int index = 0; index < 4; index++) {
                if (blockEntity.items.get(index).isEmpty() || random.nextFloat() >= 0.2F) {
                    continue;
                }
                spawnSmoke(level,
                        pos.getX() + 0.5 + FOUR_SLOT_OFFSETS[index][0],
                        y,
                        pos.getZ() + 0.5 + FOUR_SLOT_OFFSETS[index][1]);
            }
        }
    }

    private static void spawnSmoke(Level level, double x, double y, double z) {
        for (int index = 0; index < 4; index++) {
            level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 5.0E-4, 0.0);
        }
    }

    private static void spawnAmbientParticle(Level level, BlockPos pos, RandomSource random,
                                             SimpleParticleType type) {
        level.addAlwaysVisibleParticle(type, true,
                pos.getX() + 0.5 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
                pos.getY() + random.nextDouble() + random.nextDouble(),
                pos.getZ() + 0.5 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
                0.0, 0.07, 0.0);
    }

    private Optional<? extends AbstractCookingRecipe> getRecipeFor(
            Container input, BlazierBlock.HeatLevel heat, Level level) {
        return switch (heat) {
            case SMELTING -> blastingCheck.getRecipeFor(input, level);
            case BAKING -> smeltingCheck.getRecipeFor(input, level);
            case CAMPFIRE -> campfireCheck.getRecipeFor(input, level);
            case SMOKING -> smokingCheck.getRecipeFor(input, level);
        };
    }

    public boolean hasFreeSlot() {
        BlazierBlock.HeatLevel heat = getBlockState().getValue(BlazierBlock.HEAT);
        int maxSlots = getMaxSlots(heat);
        for (int index = 0; index < maxSlots; index++) {
            if (items.get(index).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public Optional<? extends AbstractCookingRecipe> getCookableRecipe(ItemStack stack) {
        if (level == null || stack.isEmpty() || !hasFreeSlot()) {
            return Optional.empty();
        }
        return getRecipeFor(new SimpleContainer(stack),
                getBlockState().getValue(BlazierBlock.HEAT), level);
    }

    public boolean hasRecipeInLowerHeat(ItemStack stack) {
        if (level == null || stack.isEmpty()) {
            return false;
        }
        BlazierBlock.HeatLevel current = getBlockState().getValue(BlazierBlock.HEAT);
        SimpleContainer input = new SimpleContainer(stack);
        boolean foundCurrent = false;
        for (BlazierBlock.HeatLevel heat : BlazierBlock.HeatLevel.values()) {
            if (heat == current) {
                foundCurrent = true;
                continue;
            }
            if (foundCurrent && getRecipeFor(input, heat, level).isPresent()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasRecipeInHigherHeat(ItemStack stack) {
        if (level == null || stack.isEmpty()) {
            return false;
        }
        BlazierBlock.HeatLevel current = getBlockState().getValue(BlazierBlock.HEAT);
        SimpleContainer input = new SimpleContainer(stack);
        for (BlazierBlock.HeatLevel heat : BlazierBlock.HeatLevel.values()) {
            if (heat == current) {
                break;
            }
            if (getRecipeFor(input, heat, level).isPresent()) {
                return true;
            }
        }
        return false;
    }

    public int getCookTimeForRecipe(AbstractCookingRecipe recipe, BlazierBlock.HeatLevel heat) {
        double modeMultiplier = switch (heat) {
            case SMELTING, SMOKING -> 6.0;
            case BAKING -> 3.0;
            case CAMPFIRE -> 1.0;
        };
        return (int) Math.max(1, recipe.getCookingTime()
                * modeMultiplier
                * MNDConfiguration.BLAZIER_COOKING_TIME_MULTIPLIER.get());
    }

    public boolean placeFood(@Nullable LivingEntity entity, ItemStack food, int cookTime) {
        BlazierBlock.HeatLevel heat = getBlockState().getValue(BlazierBlock.HEAT);
        int maxSlots = getMaxSlots(heat);
        for (int index = 0; index < maxSlots; index++) {
            if (items.get(index).isEmpty()) {
                cookingTime[index] = cookTime;
                cookingProgress[index] = 0;
                items.set(index, food.copyWithCount(1));
                if (!(entity instanceof Player player) || !player.getAbilities().instabuild) {
                    food.shrink(1);
                }
                level.gameEvent(GameEvent.BLOCK_CHANGE, getBlockPos(),
                        GameEvent.Context.of(entity, getBlockState()));
                markUpdated();
                return true;
            }
        }
        return false;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        items.clear();
        ContainerHelper.loadAllItems(tag, items);
        if (tag.contains("CookingTimes", 11)) {
            int[] times = tag.getIntArray("CookingTimes");
            System.arraycopy(times, 0, cookingProgress, 0, Math.min(cookingProgress.length, times.length));
        }
        if (tag.contains("CookingTotalTimes", 11)) {
            int[] totals = tag.getIntArray("CookingTotalTimes");
            System.arraycopy(totals, 0, cookingTime, 0, Math.min(cookingTime.length, totals.length));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, items, true);
        tag.putIntArray("CookingTimes", cookingProgress);
        tag.putIntArray("CookingTotalTimes", cookingTime);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        ContainerHelper.saveAllItems(tag, items, true);
        tag.putIntArray("CookingTimes", cookingProgress);
        tag.putIntArray("CookingTotalTimes", cookingTime);
        return tag;
    }

    private void markUpdated() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    public NonNullList<ItemStack> getItems() {
        return items;
    }
}
