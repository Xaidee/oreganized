package galena.oreganized.data;

import galena.oreganized.Oreganized;
import galena.oreganized.index.OTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class OEntityTags extends EntityTypeTagsProvider {

    public OEntityTags(PackOutput output, CompletableFuture<HolderLookup.Provider> future, @Nullable ExistingFileHelper help) {
        super(output, future, Oreganized.MOD_ID, help);
    }

    @Override
    public String getName() {
        return "Oreganized Entity Type Tags";
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Oreganized
        tag(OTags.Entities.LIGHTER_THAN_LEAD).add(EntityType.IRON_GOLEM);
        tag(OTags.Entities.BOLT_RESISTANT)
                .addOptional(new ResourceLocation("alexsmobs:mimicube"))
                .addOptional(new ResourceLocation("caverns_and_chasms:mime"));

        // Vanilla
        //tag(EntityTypeTags.IMPACT_PROJECTILES).add(OEntityTypes.LEAD_BOLT.get());
    }
}
