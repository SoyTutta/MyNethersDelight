package com.soytutta.mynethersdelight.common.data;

import com.soytutta.mynethersdelight.MyNethersDelight;
import com.soytutta.mynethersdelight.common.MNDConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import vectorwing.farmersdelight.common.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class PlantRuleEngine {
    private static final double RESURGENT_SOIL_BOOST_MULTIPLIER = 1.05;
    private static final Set<String> WARNED_RULES = ConcurrentHashMap.newKeySet();
    private static final Consumer<BlockState> NO_PROPAGATION = state -> {};
    private static final Runnable NO_BONEMEAL = () -> {};

    public static double getResurgentSoilBoostChance() {
        return Configuration.RICH_SOIL_BOOST_CHANCE.get() * RESURGENT_SOIL_BOOST_MULTIPLIER;
    }

    public static boolean applyPropagationRules(ServerLevel level, BlockPos sourcePos, BlockState sourceState,
                                                PlantRuleSet.RuleDirection direction, PlantRuleSet.SoilType soil,
                                                RandomSource random, Consumer<BlockState> propagation, Runnable bonemeal) {
        return applyRules(MNDDataMapTypes.PROPAGATION_TRANSFORMATIONS, level, sourcePos, sourceState,
                direction, soil, random, actionType -> actionType != PlantRuleSet.ActionType.REPLACE, true,
                propagation, bonemeal);
    }

    public static void applySoilTransformRules(ServerLevel level, BlockPos sourcePos, BlockState sourceState,
                                               PlantRuleSet.SoilType soil, RandomSource random) {
        applyRules(MNDDataMapTypes.PROPAGATION_TRANSFORMATIONS, level, sourcePos, sourceState,
                PlantRuleSet.RuleDirection.ABOVE, soil, random,
                actionType -> actionType == PlantRuleSet.ActionType.REPLACE, false, NO_PROPAGATION, NO_BONEMEAL);
    }

    public static boolean applyLetiosCompostRules(ServerLevel level, BlockPos sourcePos, BlockState sourceState,
                                                  RandomSource random) {
        return applyRules(MNDDataMapTypes.LETIOS_COMPOST_TRANSFORMATIONS, level, sourcePos, sourceState,
                PlantRuleSet.RuleDirection.ABOVE, PlantRuleSet.SoilType.LETIOS_COMPOST, random,
                actionType -> true, false, NO_PROPAGATION, NO_BONEMEAL);
    }

    public static boolean rollPropagation(RandomSource random, double chance) {
        if (!MNDConfiguration.ENABLE_RESURGENT_SOIL_PROPAGATION.get()) {
            return false;
        }
        return random.nextDouble() < Math.min(1.0, Math.max(0.0, chance));
    }

    public static int getTickAttempts(RandomSource random, double multiplier) {
        int attempts = (int) Math.floor(multiplier);
        double remainder = multiplier - attempts;
        if (remainder > 0.0 && random.nextDouble() < remainder) {
            attempts++;
        }
        return attempts;
    }

    private static boolean applyRules(DataMapType<net.minecraft.world.level.block.Block, PlantRuleSet> type,
                                      ServerLevel level, BlockPos sourcePos, BlockState sourceState,
                                      PlantRuleSet.RuleDirection direction, PlantRuleSet.SoilType soil,
                                      RandomSource random, Predicate<PlantRuleSet.ActionType> actionFilter,
                                      boolean propagationPhase, Consumer<BlockState> propagation,
                                      Runnable bonemeal) {
        PlantRuleSet ruleSet = sourceState.getBlock().builtInRegistryHolder().getData(type);
        if (ruleSet == null) {
            return false;
        }

        boolean hasApplicableRules = false;
        boolean propagationEnabled = !propagationPhase || MNDConfiguration.ENABLE_RESURGENT_SOIL_PROPAGATION.get();
        for (PlantRuleSet.PlantRule rule : ruleSet.rules()) {
            if (!actionFilter.test(rule.action().type()) || !matches(rule.direction(), direction) || !matches(rule.soil(), soil)) {
                continue;
            }
            if (!propagationEnabled
                    && (rule.action().type() == PlantRuleSet.ActionType.PROPAGATE
                    || rule.action().type() == PlantRuleSet.ActionType.PLACE_BLOCKS)) {
                continue;
            }

            hasApplicableRules = true;
            if (!matchesState(sourceState, rule) || !matchesConditions(level, sourcePos, rule.conditions())) {
                continue;
            }

            double chance = rule.chanceMode() == PlantRuleSet.ChanceMode.ABSOLUTE
                    ? rule.chance()
                    : getResurgentSoilBoostChance() * rule.chance();
            if (random.nextDouble() >= Math.min(1.0, Math.max(0.0, chance))) {
                continue;
            }

            executeAction(level, sourcePos, sourceState, rule.action(), propagation, bonemeal);
        }
        return hasApplicableRules;
    }

    private static boolean matches(PlantRuleSet.RuleDirection expected, PlantRuleSet.RuleDirection actual) {
        return expected == PlantRuleSet.RuleDirection.ANY || expected == actual;
    }

    private static boolean matches(PlantRuleSet.SoilType expected, PlantRuleSet.SoilType actual) {
        return expected == PlantRuleSet.SoilType.ANY || expected == actual;
    }

    private static boolean matchesState(BlockState sourceState, PlantRuleSet.PlantRule rule) {
        if (rule.state().isEmpty()) {
            return true;
        }

        Optional<String> missingProperty = rule.state().get().checkState(sourceState.getBlock().getStateDefinition());
        if (missingProperty.isPresent()) {
            warnOnce(sourceState, "Unknown state property '" + missingProperty.get() + "'");
            return false;
        }
        return rule.state().get().matches(sourceState);
    }

    private static boolean matchesConditions(ServerLevel level, BlockPos sourcePos, List<PlantRuleSet.OffsetCondition> conditions) {
        for (PlantRuleSet.OffsetCondition condition : conditions) {
            BlockPos targetPos = sourcePos.offset(condition.offset());
            BlockState targetState = level.getBlockState(targetPos);
            boolean matches = switch (condition.type()) {
                case AIR -> targetState.isAir();
                case BLOCK -> condition.value()
                        .flatMap(BuiltInRegistries.BLOCK::getOptional)
                        .map(targetState::is)
                        .orElse(false);
                case BLOCK_TAG -> condition.value()
                        .map(id -> targetState.is(TagKey.create(Registries.BLOCK, id)))
                        .orElse(false);
                case FLUID_TAG -> condition.value()
                        .map(id -> level.getFluidState(targetPos).is(TagKey.create(Registries.FLUID, id)))
                        .orElse(false);
            };
            if (!matches) {
                return false;
            }
        }
        return true;
    }

    private static void executeAction(ServerLevel level, BlockPos sourcePos, BlockState sourceState,
                                      PlantRuleSet.PlantAction action, Consumer<BlockState> propagation,
                                      Runnable bonemeal) {
        switch (action.type()) {
            case PROPAGATE -> resolveResult(sourceState, action.result()).ifPresent(propagation);
            case REPLACE -> resolveResult(sourceState, action.result())
                    .ifPresent(result -> level.setBlockAndUpdate(sourcePos, result));
            case BONEMEAL -> bonemeal.run();
            case PLACE_BLOCKS -> placeBlocks(level, sourcePos, sourceState, action.blocks());
        }
    }

    private static void placeBlocks(ServerLevel level, BlockPos sourcePos, BlockState sourceState,
                                    List<PlantRuleSet.PlantPlacement> placements) {
        List<ResolvedPlacement> resolved = new ArrayList<>();
        for (PlantRuleSet.PlantPlacement placement : placements) {
            Optional<BlockState> state = resolveState(sourceState, placement.state());
            if (state.isEmpty()) {
                return;
            }
            resolved.add(new ResolvedPlacement(sourcePos.offset(placement.offset()), state.get()));
        }
        resolved.forEach(placement -> level.setBlockAndUpdate(placement.pos(), placement.state()));
    }

    private static Optional<BlockState> resolveResult(BlockState sourceState, Optional<PlantRuleSet.PlantBlockState> result) {
        if (result.isEmpty()) {
            warnOnce(sourceState, "Missing result for plant rule action");
            return Optional.empty();
        }
        return resolveState(sourceState, result.get());
    }

    private static Optional<BlockState> resolveState(BlockState sourceState, PlantRuleSet.PlantBlockState result) {
        BlockState state = result.block().defaultBlockState();
        for (var entry : result.properties().entrySet()) {
            Property<?> property = state.getBlock().getStateDefinition().getProperty(entry.getKey());
            if (property == null) {
                warnOnce(sourceState, "Unknown result property '" + entry.getKey() + "' for " + BuiltInRegistries.BLOCK.getKey(result.block()));
                return Optional.empty();
            }
            Optional<BlockState> updatedState = setProperty(state, property, entry.getValue());
            if (updatedState.isEmpty()) {
                warnOnce(sourceState, "Invalid value '" + entry.getValue() + "' for result property '" + entry.getKey() + "'");
                return Optional.empty();
            }
            state = updatedState.get();
        }
        return Optional.of(state);
    }

    private static <T extends Comparable<T>> Optional<BlockState> setProperty(BlockState state, Property<T> property, String value) {
        return property.getValue(value).map(parsed -> state.setValue(property, parsed));
    }

    private static void warnOnce(BlockState sourceState, String message) {
        ResourceLocation sourceId = BuiltInRegistries.BLOCK.getKey(sourceState.getBlock());
        String key = sourceId + ":" + message;
        if (WARNED_RULES.add(key)) {
            MyNethersDelight.LOGGER.warn("Skipping plant rule for {}: {}", sourceId, message);
        }
    }

    private record ResolvedPlacement(BlockPos pos, BlockState state) {}
}
