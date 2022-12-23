package galena.oreganized.index;

import galena.oreganized.Oreganized;
import galena.oreganized.world.gen.structure.BoulderStructure;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;



public class OStructures {

    public static final DeferredRegister<StructureFeature<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, Oreganized.MOD_ID);

    public static final RegistryObject<StructureFeature<JigsawConfiguration>> BOULDER = STRUCTURES.register("boulder", () -> new BoulderStructure(JigsawConfiguration.CODEC));

    public static final ResourceKey<ConfiguredStructureFeature<?, ?>> BOULDER_KEY = ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, new ResourceLocation(Oreganized.MOD_ID, "boulder"));
}