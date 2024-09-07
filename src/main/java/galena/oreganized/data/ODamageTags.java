package galena.oreganized.data;

import galena.oreganized.Oreganized;
import galena.oreganized.index.ODamageSources;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ODamageTags extends TagsProvider<DamageType> {

    public ODamageTags(PackOutput output, CompletableFuture<HolderLookup.Provider> future, @Nullable ExistingFileHelper helper) {
        super(output, Registries.DAMAGE_TYPE, future, Oreganized.MOD_ID, helper);
    }

    @Override
    public @NotNull String getName() {
        return "Oreganized DamageType Tags";
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
      // tag(DamageTypeTags.BYPASSES_ARMOR).add(ODamageSources.LEAD_BOLT);
    }
}
