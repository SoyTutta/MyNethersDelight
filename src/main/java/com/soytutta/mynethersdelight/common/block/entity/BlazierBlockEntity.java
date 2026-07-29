package com.soytutta.mynethersdelight.common.block.entity;

import java.util.Optional;
import javax.annotation.Nullable;

import com.soytutta.mynethersdelight.common.MNDConfiguration;
import com.soytutta.mynethersdelight.common.block.BlazierBlock;
import com.soytutta.mynethersdelight.common.registry.MNDBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Clearable;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent.Context;

public class BlazierBlockEntity extends BlockEntity implements Clearable {

    private static final int BURN_COOL_SPEED = 2;
    private static final int NUM_SLOTS = 4;

    private static final float[][] FOUR_SLOT_OFFSETS = {
            { -0.25F, -0.25F },
            {  0.25F, -0.25F },
            { -0.25F,  0.25F },
            {  0.25F,  0.25F }
    };

    private static final float TWO_SLOT_OFFSET = 0.22F;

    private final NonNullList<ItemStack> items;
    private final int[] cookingProgress;
    private final int[] cookingTime;
    @Nullable
    private BlazierBlock.HeatLevel storedHeat;
    @Nullable
    private Boolean storedLit;

    private final RecipeManager.CachedCheck<SingleRecipeInput, BlastingRecipe>        blastingCheck;
    private final RecipeManager.CachedCheck<SingleRecipeInput, SmeltingRecipe>        smeltingCheck;
    private final RecipeManager.CachedCheck<SingleRecipeInput, CampfireCookingRecipe> campfireCheck;
    private final RecipeManager.CachedCheck<SingleRecipeInput, SmokingRecipe>         smokingCheck;

    public BlazierBlockEntity(BlockPos pos, BlockState state) {
        super(MNDBlockEntityTypes.BLAZIER.get(), pos, state);
        this.items           = NonNullList.withSize(NUM_SLOTS, ItemStack.EMPTY);
        this.cookingProgress = new int[NUM_SLOTS];
        this.cookingTime     = new int[NUM_SLOTS];
        this.blastingCheck   = RecipeManager.createCheck(RecipeType.BLASTING);
        this.smeltingCheck   = RecipeManager.createCheck(RecipeType.SMELTING);
        this.campfireCheck   = RecipeManager.createCheck(RecipeType.CAMPFIRE_COOKING);
        this.smokingCheck    = RecipeManager.createCheck(RecipeType.SMOKING);
    }

    public static int getMaxSlots(BlazierBlock.HeatLevel heat) {
        return switch (heat) {
            case SMELTING, SMOKING -> 2;
            case BAKING, CAMPFIRE  -> 4;
        };
    }

    public static void cookTick(Level level, BlockPos pos, BlockState state,
                                BlazierBlockEntity be) {
        BlazierBlock.HeatLevel heat = state.getValue(BlazierBlock.HEAT);
        int maxSlots = getMaxSlots(heat);
        boolean dirty = false;

        for (int i = maxSlots; i < NUM_SLOTS; i++) {
            if (!be.items.get(i).isEmpty()) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), be.items.get(i));
                be.items.set(i, ItemStack.EMPTY);
                be.cookingProgress[i] = 0;
                dirty = true;
            }
        }

        for (int i = 0; i < maxSlots; i++) {
            ItemStack stack = be.items.get(i);
            if (stack.isEmpty()) continue;

            dirty = true;

            SingleRecipeInput input = new SingleRecipeInput(stack);
            Optional<? extends RecipeHolder<? extends AbstractCookingRecipe>> recipe =
                    be.getRecipeFor(input, heat, level);
            if (recipe.isPresent()) {
                be.cookingProgress[i]++;

                if (be.cookingProgress[i] >= be.cookingTime[i]) {
                    ItemStack result = recipe.get().value().assemble(input, level.registryAccess());
                    if (result.isItemEnabled(level.enabledFeatures())) {
                        Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), result);
                        be.items.set(i, ItemStack.EMPTY);
                        be.cookingProgress[i] = 0;
                        level.sendBlockUpdated(pos, state, state, 3);
                        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, Context.of(state));
                    }
                }
            }
        }

        if (dirty) setChanged(level, pos, state);
    }

    public static void cooldownTick(Level level, BlockPos pos, BlockState state,
                                    BlazierBlockEntity be) {
        boolean dirty = false;
        for (int i = 0; i < be.items.size(); i++) {
            if (be.cookingProgress[i] > 0) {
                dirty = true;
                be.cookingProgress[i] = Mth.clamp(be.cookingProgress[i] - BURN_COOL_SPEED, 0, be.cookingTime[i]);
            }
        }
        if (dirty) setChanged(level, pos, state);
    }

    public static void particleTick(Level level, BlockPos pos, BlockState state,
                                    BlazierBlockEntity be) {
        RandomSource random = level.random;
        BlazierBlock.HeatLevel heat = state.getValue(BlazierBlock.HEAT);
        Direction facing = state.getValue(BlazierBlock.FACING);
        int maxSlots = getMaxSlots(heat);

        if (random.nextFloat() < 0.11F) {
            spawnByHeat(level, pos, random, heat);
        }

        spawnItemSmoke(level, pos, facing, heat, be, random, maxSlots);
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
                                       BlazierBlock.HeatLevel heat,
                                       BlazierBlockEntity be,
                                       RandomSource random,
                                       int maxSlots) {
        if (maxSlots == 2) {
            Direction perp = facing.getClockWise();
            float y = pos.getY() + (heat == BlazierBlock.HeatLevel.SMOKING ? 0.15F : 0.386F);
            float[] offsets = { -TWO_SLOT_OFFSET, TWO_SLOT_OFFSET };

            for (int j = 0; j < 2; j++) {
                if (be.items.get(j).isEmpty() || random.nextFloat() >= 0.2F) continue;
                spawnSmoke(level,
                        pos.getX() + 0.5 + perp.getStepX() * offsets[j],
                        y,
                        pos.getZ() + 0.5 + perp.getStepZ() * offsets[j]);
            }
        } else {
            float y = pos.getY() + 0.386F;

            for (int j = 0; j < 4; j++) {
                if (be.items.get(j).isEmpty() || random.nextFloat() >= 0.2F) continue;
                spawnSmoke(level,
                        pos.getX() + 0.5 + FOUR_SLOT_OFFSETS[j][0],
                        y,
                        pos.getZ() + 0.5 + FOUR_SLOT_OFFSETS[j][1]);
            }
        }
    }

    private static void spawnSmoke(Level level, double x, double y, double z) {
        for (int k = 0; k < 4; k++) {
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

    private Optional<? extends RecipeHolder<? extends AbstractCookingRecipe>> getRecipeFor(
            SingleRecipeInput input, BlazierBlock.HeatLevel heat, Level level) {
        return switch (heat) {
            case SMELTING -> blastingCheck.getRecipeFor(input, level);
            case BAKING -> smeltingCheck.getRecipeFor(input, level);
            case CAMPFIRE -> campfireCheck.getRecipeFor(input, level);
            case SMOKING -> smokingCheck.getRecipeFor(input, level);
        };
    }

    public boolean hasFreeSlot() {
        BlazierBlock.HeatLevel heat = this.getBlockState().getValue(BlazierBlock.HEAT);
        int maxSlots = getMaxSlots(heat);
        for (int i = 0; i < maxSlots; i++) {
            if (this.items.get(i).isEmpty()) return true;
        }
        return false;
    }

    public Optional<RecipeHolder<?>> getCookableRecipe(ItemStack stack) {
        if (this.level == null) return Optional.empty();
        BlazierBlock.HeatLevel heat = this.getBlockState().getValue(BlazierBlock.HEAT);

        if (!hasFreeSlot()) return Optional.empty();

        SingleRecipeInput input = new SingleRecipeInput(stack);
        return getRecipeFor(input, heat, this.level).map(recipe -> (RecipeHolder<?>) recipe);
    }

    public boolean hasRecipeInLowerHeat(ItemStack stack) {
        if (this.level == null) return false;
        BlazierBlock.HeatLevel current = this.getBlockState().getValue(BlazierBlock.HEAT);
        SingleRecipeInput input = new SingleRecipeInput(stack);

        boolean foundCurrent = false;
        for (BlazierBlock.HeatLevel h : BlazierBlock.HeatLevel.values()) {
            if (h == current) { foundCurrent = true; continue; }
            if (!foundCurrent) continue;
            if (getRecipeFor(input, h, this.level).isPresent()) return true;
        }
        return false;
    }

    public boolean hasRecipeInHigherHeat(ItemStack stack) {
        if (this.level == null) return false;
        BlazierBlock.HeatLevel current = this.getBlockState().getValue(BlazierBlock.HEAT);
        SingleRecipeInput input = new SingleRecipeInput(stack);

        for (BlazierBlock.HeatLevel h : BlazierBlock.HeatLevel.values()) {
            if (h == current) break;
            if (getRecipeFor(input, h, this.level).isPresent()) return true;
        }
        return false;
    }

    public int getCookTimeForRecipe(RecipeHolder<?> holder, BlazierBlock.HeatLevel heat) {
        if (!(holder.value() instanceof AbstractCookingRecipe recipe)) return 600;
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
        BlazierBlock.HeatLevel heat = this.getBlockState().getValue(BlazierBlock.HEAT);
        int maxSlots = getMaxSlots(heat);
        for (int i = 0; i < maxSlots; i++) {
            if (this.items.get(i).isEmpty()) {
                this.cookingTime[i]     = cookTime;
                this.cookingProgress[i] = 0;
                this.items.set(i, food.consumeAndReturn(1, entity));
                this.level.gameEvent(GameEvent.BLOCK_CHANGE, this.getBlockPos(),
                        Context.of(entity, this.getBlockState()));
                this.markUpdated();
                return true;
            }
        }
        return false;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.items.clear();
        ContainerHelper.loadAllItems(tag, this.items, registries);
        if (tag.contains("CookingTimes", 11)) {
            int[] times = tag.getIntArray("CookingTimes");
            System.arraycopy(times, 0, this.cookingProgress, 0,
                    Math.min(this.cookingProgress.length, times.length));
        }
        if (tag.contains("CookingTotalTimes", 11)) {
            int[] totals = tag.getIntArray("CookingTotalTimes");
            System.arraycopy(totals, 0, this.cookingTime, 0,
                    Math.min(this.cookingTime.length, totals.length));
        }
        this.storedHeat = tag.contains("Heat")
                ? BlazierBlock.HeatLevel.byName(tag.getString("Heat")).orElse(null)
                : null;
        this.storedLit = tag.contains("Lit") ? tag.getBoolean("Lit") : null;
        this.restoreStoredBlockState();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, this.items, true, registries);
        tag.putIntArray("CookingTimes", this.cookingProgress);
        tag.putIntArray("CookingTotalTimes", this.cookingTime);
        BlockState state = this.getBlockState();
        if (state.hasProperty(BlazierBlock.HEAT) && state.hasProperty(BlazierBlock.LIT)) {
            tag.putString("Heat", state.getValue(BlazierBlock.HEAT).getSerializedName());
            tag.putBoolean("Lit", state.getValue(BlazierBlock.LIT));
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.restoreStoredBlockState();
    }

    private void restoreStoredBlockState() {
        if (this.level == null || this.storedHeat == null && this.storedLit == null) {
            return;
        }

        BlockState state = this.level.getBlockState(this.worldPosition);
        if (!(state.getBlock() instanceof BlazierBlock)) {
            return;
        }

        BlockState restoredState = state;
        if (this.storedHeat != null) {
            restoredState = restoredState.setValue(BlazierBlock.HEAT, this.storedHeat);
        }
        if (this.storedLit != null) {
            restoredState = restoredState.setValue(BlazierBlock.LIT, this.storedLit);
        }

        this.storedHeat = null;
        this.storedLit = null;
        if (restoredState != state) {
            this.level.setBlock(this.worldPosition, restoredState, 2);
        }
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        ContainerHelper.saveAllItems(tag, this.items, true, registries);
        tag.putIntArray("CookingTimes", this.cookingProgress);
        tag.putIntArray("CookingTotalTimes", this.cookingTime);
        return tag;
    }

    private void markUpdated() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(),
                this.getBlockState(), 3);
    }

    @Override
    public void clearContent() { this.items.clear(); }

    public NonNullList<ItemStack> getItems() { return this.items; }

    @Override
    protected void applyImplicitComponents(DataComponentInput input) {
        super.applyImplicitComponents(input);
        input.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY)
                .copyInto(this.getItems());
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder) {
        super.collectImplicitComponents(builder);
        builder.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(this.getItems()));
    }

    @Override
    public void removeComponentsFromTag(CompoundTag tag) {
        tag.remove("Items");
    }
}
