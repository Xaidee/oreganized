package galena.oreganized.index;

import com.google.common.collect.ImmutableList;
import galena.oreganized.Oreganized;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

public class OFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Oreganized.MOD_ID);

    public static final RegistryObject<Feature<OreConfiguration>> SILVER_ORE_LOW = FEATURES.register("silver_ore", () -> new OreFeature(OreConfiguration.CODEC));
    public static final RegistryObject<Feature<OreConfiguration>> SILVER_ORE_HIGH = FEATURES.register("silver_ore_high", () -> new OreFeature(OreConfiguration.CODEC));
    public static final RegistryObject<Feature<OreConfiguration>> LEAD_ORE = FEATURES.register("lead_ore", () -> new OreFeature(OreConfiguration.CODEC));
    public static final RegistryObject<Feature<OreConfiguration>> LEAD_ORE_EXTRA = FEATURES.register("lead_ore_extra", () -> new OreFeature(OreConfiguration.CODEC));

    public static final class Configured {

        public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, Oreganized.MOD_ID);

        public static final RegistryObject<ConfiguredFeature<OreConfiguration, ?>> SILVER_ORE_LOW = registerOre("silver_ore", OBlocks.SILVER_ORE, OBlocks.DEEPSLATE_SILVER_ORE, 4);
        public static final RegistryObject<ConfiguredFeature<OreConfiguration, ?>> SILVER_ORE_HIGH = registerOre("silver_ore_high", OBlocks.SILVER_ORE, OBlocks.DEEPSLATE_SILVER_ORE, 2);
        public static final RegistryObject<ConfiguredFeature<OreConfiguration, ?>> LEAD_ORE = registerOre("lead_ore", OBlocks.LEAD_ORE, OBlocks.DEEPSLATE_LEAD_ORE, 8);
        public static final RegistryObject<ConfiguredFeature<OreConfiguration, ?>> LEAD_ORE_EXTRA = registerOre("lead_ore_extra", OBlocks.LEAD_ORE, OBlocks.DEEPSLATE_LEAD_ORE, 13);

        private static <FC extends FeatureConfiguration, F extends Feature<FC>> RegistryObject<ConfiguredFeature<FC, ?>> register(String name, Supplier<ConfiguredFeature<FC, F>> feature) {
            return CONFIGURED_FEATURES.register(name, feature);
        }

        private static RegistryObject<ConfiguredFeature<OreConfiguration, ?>> registerOre(String name, Supplier<? extends Block> stoneOre, Supplier<? extends Block> deepslateOre, int size) {
            return register(name, () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, stoneOre.get().defaultBlockState()),
                    OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, deepslateOre.get().defaultBlockState())), size)));
        }
    }

    public static final class Placed {
        public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, Oreganized.MOD_ID);

        public static final RegistryObject<PlacedFeature> SILVER_ORE_LOW = register("silver_ore", Configured.SILVER_ORE_LOW,
                CountPlacement.of(4),
                InSquarePlacement.spread(),
                BiomeFilter.biome(),
                HeightRangePlacement.triangle(VerticalAnchor.absolute(-15), VerticalAnchor.absolute(5))
        );
        public static final RegistryObject<PlacedFeature> SILVER_ORE_HIGH = register("silver_ore_high", Configured.SILVER_ORE_HIGH,
                CountPlacement.of(4),
                InSquarePlacement.spread(),
                BiomeFilter.biome(),
                HeightRangePlacement.triangle(VerticalAnchor.absolute(140), VerticalAnchor.absolute(160))
        );
        public static final RegistryObject<PlacedFeature> LEAD_ORE = register("lead_ore", Configured.LEAD_ORE,
                CountPlacement.of(10),
                InSquarePlacement.spread(),
                BiomeFilter.biome(),
                HeightRangePlacement.triangle(VerticalAnchor.absolute(-40), VerticalAnchor.absolute(-20))
        );
        public static final RegistryObject<PlacedFeature> LEAD_ORE_EXTRA = register("lead_ore_extra", Configured.LEAD_ORE_EXTRA,
                CountPlacement.of(12),
                InSquarePlacement.spread(),
                BiomeFilter.biome(),
                HeightRangePlacement.triangle(VerticalAnchor.absolute(50), VerticalAnchor.absolute(80))
        );

        private static RegistryObject<PlacedFeature> register(String name, RegistryObject<? extends ConfiguredFeature<?, ?>> feature, PlacementModifier... placementModifiers) {
            return register(name, feature, List.of(placementModifiers));
        }

        @SuppressWarnings("unchecked")
        private static RegistryObject<PlacedFeature> register(String name, RegistryObject<? extends ConfiguredFeature<?, ?>> feature, List<PlacementModifier> placementModifiers) {
            return PLACED_FEATURES.register(name, () -> new PlacedFeature((Holder<ConfiguredFeature<?, ?>>) feature.getHolder().get(), ImmutableList.copyOf(placementModifiers)));
        }
    }

    @SubscribeEvent
    public static void onBiomeLoadingEvent(BiomeLoadingEvent event) {
        // Check that we're not in the nether or end to save on resources.
        if (event.getCategory() == Biome.BiomeCategory.NETHER || event.getCategory() == Biome.BiomeCategory.THEEND) return;

        // Check if we're in a savannah to add extra lead generation. Notice that there is no else statement meaning other ore placements
        // will still generate in the biome.
        if (event.getCategory() == Biome.BiomeCategory.SAVANNA) {
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Holder.direct(Placed.LEAD_ORE_EXTRA.get()));
        }
        event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Holder.direct(Placed.SILVER_ORE_LOW.get()));
        event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Holder.direct(Placed.SILVER_ORE_HIGH.get()));
        event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Holder.direct(Placed.LEAD_ORE.get()));
    }
}
