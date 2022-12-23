package galena.oreganized.data;

import galena.oreganized.Oreganized;
import galena.oreganized.index.OTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class OBiomeTags extends BiomeTagsProvider {

    public OBiomeTags(DataGenerator generator, @Nullable ExistingFileHelper helper) {
        super(generator, Oreganized.MOD_ID, helper);
    }

    @Override
    public @NotNull String getName() {
        return "Oreganized Biome Tags";
    }

    @Override
    protected void addTags() {
        tag(OTags.Biomes.HAS_BOULDER)
                .addTag(OTags.Biomes.RICH_IN_LEAD_ORE)
                .addTag(Tags.Biomes.IS_PLAINS);
        tag(OTags.Biomes.RICH_IN_LEAD_ORE).addTag(BiomeTags.HAS_VILLAGE_SAVANNA);
    }
}
