package galena.oreganized.index;

import com.google.common.collect.ImmutableList;
import galena.oreganized.Oreganized;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
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

    public static Holder<PlacedFeature> SILVER_ORE_LOW;
    public static Holder<PlacedFeature> SILVER_ORE_HIGH;
    public static Holder<PlacedFeature> LEAD_ORE;
    public static Holder<PlacedFeature> LEAD_ORE_SAVANNAH;

    public static void registerOreFeatures() {
        List<OreConfiguration.TargetBlockState> silverOre = List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, OBlocks.SILVER_ORE.get().defaultBlockState()),
                OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, OBlocks.DEEPSLATE_SILVER_ORE.get().defaultBlockState()));
        List<OreConfiguration.TargetBlockState> leadOre = List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, OBlocks.LEAD_ORE.get().defaultBlockState()),
                OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, OBlocks.DEEPSLATE_LEAD_ORE.get().defaultBlockState()));

        SILVER_ORE_LOW = registerPlacedOreFeature("silver_ore", new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(silverOre, 3, 0.6F)),
                CountPlacement.of(2),
                InSquarePlacement.spread(),
                BiomeFilter.biome(),
                HeightRangePlacement.triangle(VerticalAnchor.absolute(-15), VerticalAnchor.absolute(5)));
        SILVER_ORE_HIGH = registerPlacedOreFeature("silver_ore_high", new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(silverOre, 2, 0.8F)),
                CountPlacement.of(2),
                InSquarePlacement.spread(),
                BiomeFilter.biome(),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(140), VerticalAnchor.absolute(160)));
        LEAD_ORE = registerPlacedOreFeature("lead_ore", new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(leadOre, 8)),
                CountPlacement.of(10),
                InSquarePlacement.spread(),
                BiomeFilter.biome(),
                HeightRangePlacement.triangle(VerticalAnchor.absolute(-40), VerticalAnchor.absolute(-20)));
        LEAD_ORE_SAVANNAH = registerPlacedOreFeature("lead_ore_savannah", new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(leadOre, 13)), // Vein size of 13
                CountPlacement.of(12),
                InSquarePlacement.spread(),
                BiomeFilter.biome(),
                HeightRangePlacement.triangle(VerticalAnchor.absolute(50), VerticalAnchor.absolute(80)));
    }

    private static <C extends FeatureConfiguration, F extends Feature<C>> Holder<PlacedFeature> registerPlacedOreFeature(String regName, ConfiguredFeature<C, F> feature, PlacementModifier ... placementModifiers) {
        return PlacementUtils.register(regName, Holder.direct(feature), placementModifiers);
    }

    public static void onBiomeLoadingEvent(BiomeLoadingEvent event) {
        // Check that we're not in the nether or end to save on resources.
        if (event.getCategory() == Biome.BiomeCategory.NETHER || event.getCategory() == Biome.BiomeCategory.THEEND) return;

        // Check if we're in a savannah to add extra lead generation. Notice that there is no else statement meaning other ore placements
        // will still generate in the biome.
        if (event.getCategory() == Biome.BiomeCategory.SAVANNA) {
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, LEAD_ORE_SAVANNAH);
        }
        event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, SILVER_ORE_LOW);
        event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, SILVER_ORE_HIGH);
        event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, LEAD_ORE);
    }
}
