package com.soytutta.mynethersdelight.common.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public record PlantRuleSet(List<PlantRule> rules) {
    public static final Codec<PlantRuleSet> CODEC = PlantRule.CODEC.listOf()
            .fieldOf("rules")
            .xmap(PlantRuleSet::new, PlantRuleSet::rules)
            .codec();

    public record PlantRule(double chance, ChanceMode chanceMode, RuleDirection direction, SoilType soil,
                            Optional<StatePropertiesPredicate> state, List<OffsetCondition> conditions,
                            PlantAction action) {
        public static final Codec<PlantRule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.doubleRange(0.0, 1.0).optionalFieldOf("chance", 1.0).forGetter(PlantRule::chance),
                ChanceMode.CODEC.optionalFieldOf("chance_mode", ChanceMode.RICH_SOIL_MULTIPLIER).forGetter(PlantRule::chanceMode),
                RuleDirection.CODEC.optionalFieldOf("direction", RuleDirection.ANY).forGetter(PlantRule::direction),
                SoilType.CODEC.optionalFieldOf("soil", SoilType.ANY).forGetter(PlantRule::soil),
                StatePropertiesPredicate.CODEC.optionalFieldOf("state").forGetter(PlantRule::state),
                OffsetCondition.CODEC.listOf().optionalFieldOf("conditions", List.of()).forGetter(PlantRule::conditions),
                PlantAction.CODEC.fieldOf("action").forGetter(PlantRule::action)
        ).apply(instance, PlantRule::new));
    }

    public record OffsetCondition(OffsetConditionType type, BlockPos offset, Optional<ResourceLocation> value) {
        public static final Codec<OffsetCondition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                OffsetConditionType.CODEC.fieldOf("type").forGetter(OffsetCondition::type),
                BlockPos.CODEC.optionalFieldOf("offset", BlockPos.ZERO).forGetter(OffsetCondition::offset),
                ResourceLocation.CODEC.optionalFieldOf("value").forGetter(OffsetCondition::value)
        ).apply(instance, OffsetCondition::new));
    }

    public record PlantAction(ActionType type, Optional<PlantBlockState> result, List<PlantPlacement> blocks) {
        public static final Codec<PlantAction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ActionType.CODEC.fieldOf("type").forGetter(PlantAction::type),
                PlantBlockState.CODEC.optionalFieldOf("result").forGetter(PlantAction::result),
                PlantPlacement.CODEC.listOf().optionalFieldOf("blocks", List.of()).forGetter(PlantAction::blocks)
        ).apply(instance, PlantAction::new));
    }

    public record PlantPlacement(BlockPos offset, PlantBlockState state) {
        public static final Codec<PlantPlacement> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BlockPos.CODEC.optionalFieldOf("offset", BlockPos.ZERO).forGetter(PlantPlacement::offset),
                PlantBlockState.CODEC.fieldOf("state").forGetter(PlantPlacement::state)
        ).apply(instance, PlantPlacement::new));
    }

    public record PlantBlockState(Block block, Map<String, String> properties) {
        public static final Codec<PlantBlockState> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(PlantBlockState::block),
                Codec.unboundedMap(Codec.STRING, Codec.STRING).optionalFieldOf("properties", Map.of()).forGetter(PlantBlockState::properties)
        ).apply(instance, PlantBlockState::new));
    }

    public enum ChanceMode implements StringRepresentable {
        ABSOLUTE("absolute"),
        RICH_SOIL_MULTIPLIER("rich_soil_multiplier");

        public static final Codec<ChanceMode> CODEC = enumCodec(ChanceMode.values());
        private final String name;

        ChanceMode(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }

    public enum RuleDirection implements StringRepresentable {
        ANY("any"),
        ABOVE("above"),
        BELOW("below");

        public static final Codec<RuleDirection> CODEC = enumCodec(RuleDirection.values());
        private final String name;

        RuleDirection(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }

    public enum SoilType implements StringRepresentable {
        ANY("any"),
        RESURGENT_SOIL("resurgent_soil"),
        RESURGENT_FARMLAND("resurgent_farmland"),
        LETIOS_COMPOST("letios_compost");

        public static final Codec<SoilType> CODEC = enumCodec(SoilType.values());
        private final String name;

        SoilType(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }

    public enum OffsetConditionType implements StringRepresentable {
        AIR("air"),
        BLOCK("block"),
        BLOCK_TAG("block_tag"),
        FLUID_TAG("fluid_tag");

        public static final Codec<OffsetConditionType> CODEC = enumCodec(OffsetConditionType.values());
        private final String name;

        OffsetConditionType(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }

    public enum ActionType implements StringRepresentable {
        PROPAGATE("propagate"),
        REPLACE("replace"),
        PLACE_BLOCKS("place_blocks"),
        BONEMEAL("bonemeal");

        public static final Codec<ActionType> CODEC = enumCodec(ActionType.values());
        private final String name;

        ActionType(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }

    private static <T extends Enum<T> & StringRepresentable> Codec<T> enumCodec(T[] values) {
        return Codec.STRING.comapFlatMap(name -> Arrays.stream(values)
                        .filter(value -> value.getSerializedName().equals(name))
                        .findFirst()
                        .map(DataResult::success)
                        .orElseGet(() -> DataResult.error(() -> "Unknown value: " + name)),
                StringRepresentable::getSerializedName);
    }
}
