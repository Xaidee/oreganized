package galena.oreganized.data;

import galena.oreganized.Oreganized;
import galena.oreganized.index.OBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static galena.oreganized.index.OTags.Blocks.BLOWS_LEAD_CLOUD;
import static galena.oreganized.index.OTags.Blocks.CRYSTAL_GLASS;
import static galena.oreganized.index.OTags.Blocks.CRYSTAL_GLASS_PANES;
import static galena.oreganized.index.OTags.Blocks.FIRE_SOURCE;
import static galena.oreganized.index.OTags.Blocks.MELTS_LEAD;
import static galena.oreganized.index.OTags.Blocks.MINEABLE_WITH_SCRIBE;
import static galena.oreganized.index.OTags.Blocks.ORES_LEAD;
import static galena.oreganized.index.OTags.Blocks.ORES_SILVER;
import static galena.oreganized.index.OTags.Blocks.PREVENTS_LEAD_CLOUD;
import static galena.oreganized.index.OTags.Blocks.SILKTOUCH_WITH_SCRIBE;
import static galena.oreganized.index.OTags.Blocks.SILKTOUCH_WITH_SCRIBE_BLACKLIST;
import static galena.oreganized.index.OTags.Blocks.STONE_TYPES_GLANCE;
import static galena.oreganized.index.OTags.Blocks.STORAGE_BLOCKS_ELECTRUM;
import static galena.oreganized.index.OTags.Blocks.STORAGE_BLOCKS_LEAD;
import static galena.oreganized.index.OTags.Blocks.STORAGE_BLOCKS_RAW_LEAD;
import static galena.oreganized.index.OTags.Blocks.STORAGE_BLOCKS_RAW_SILVER;
import static galena.oreganized.index.OTags.Blocks.STORAGE_BLOCKS_SILVER;

public class OBlockTags extends IntrinsicHolderTagsProvider<Block> {

    public OBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> future, @Nullable ExistingFileHelper helper) {
        super(output, Registries.BLOCK, future, block -> block.builtInRegistryHolder().key(), Oreganized.MOD_ID, helper);
    }

    @Override
    public @NotNull String getName() {
        return "Oreganized Block Tags";
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Oreganized
        OBlocks.CRYSTAL_GLASS.forEach((c, b) -> tag(CRYSTAL_GLASS).addOptional(BuiltInRegistries.BLOCK.getKey(b.get())));
        OBlocks.CRYSTAL_GLASS_PANES.forEach((c, b) -> tag(CRYSTAL_GLASS_PANES).addOptional(BuiltInRegistries.BLOCK.getKey(b.get())));

        tag(FIRE_SOURCE).addTag(BlockTags.FIRE).addTag(BlockTags.CAMPFIRES);
        tag(STONE_TYPES_GLANCE).add(
                OBlocks.POLISHED_GLANCE.get(), OBlocks.GLANCE_BRICKS.get(), OBlocks.CHISELED_GLANCE.get(),
                OBlocks.GLANCE_BRICK_STAIRS.get(), OBlocks.GLANCE_BRICK_WALL.get()
        );

        // Oreganized Forge
        tag(ORES_SILVER).add(OBlocks.SILVER_ORE.get(), OBlocks.DEEPSLATE_SILVER_ORE.get());
        tag(ORES_LEAD).add(OBlocks.LEAD_ORE.get(), OBlocks.DEEPSLATE_LEAD_ORE.get());

        tag(STORAGE_BLOCKS_SILVER).add(OBlocks.SILVER_BLOCK.get());
        tag(STORAGE_BLOCKS_LEAD).add(OBlocks.LEAD_BLOCK.get());
        tag(STORAGE_BLOCKS_ELECTRUM).add(OBlocks.ELECTRUM_BLOCK.get());

        tag(STORAGE_BLOCKS_RAW_SILVER).add(OBlocks.RAW_SILVER_BLOCK.get());
        tag(STORAGE_BLOCKS_RAW_LEAD).add(OBlocks.RAW_LEAD_BLOCK.get());

        // Vanilla
        tag(BlockTags.WALLS).add(OBlocks.GLANCE_WALL.get(), OBlocks.GLANCE_BRICK_WALL.get());
        tag(BlockTags.STAIRS).add(OBlocks.GLANCE_STAIRS.get(), OBlocks.POLISHED_GLANCE_STAIRS.get(), OBlocks.GLANCE_BRICK_STAIRS.get());
        tag(BlockTags.SLABS).add(OBlocks.GLANCE_SLAB.get(), OBlocks.POLISHED_GLANCE_SLAB.get(), OBlocks.GLANCE_BRICK_SLAB.get());
        tag(BlockTags.BEACON_BASE_BLOCKS).add(OBlocks.ELECTRUM_BLOCK.get());
        tag(BlockTags.IMPERMEABLE).addTag(CRYSTAL_GLASS);
        tag(BlockTags.CAULDRONS).add(OBlocks.MOLTEN_LEAD_CAULDRON.get());
        tag(BlockTags.DOORS).add(OBlocks.LEAD_DOOR.get());
        tag(BlockTags.TRAPDOORS).add(OBlocks.LEAD_TRAPDOOR.get());

        // Forge
        tag(Tags.Blocks.ORES).addTags(ORES_SILVER, ORES_LEAD);
        tag(Tags.Blocks.ORE_RATES_SINGULAR).addTags(ORES_SILVER, ORES_LEAD);
        tag(Tags.Blocks.STORAGE_BLOCKS).addTags(STORAGE_BLOCKS_SILVER, STORAGE_BLOCKS_LEAD, STORAGE_BLOCKS_ELECTRUM, STORAGE_BLOCKS_RAW_SILVER, STORAGE_BLOCKS_RAW_LEAD);
        tag(Tags.Blocks.GLASS).addTag(CRYSTAL_GLASS);
        tag(Tags.Blocks.GLASS_PANES).addTag(CRYSTAL_GLASS_PANES);
        tag(Tags.Blocks.ORES_IN_GROUND_STONE).add(OBlocks.LEAD_ORE.get(), OBlocks.SILVER_ORE.get());
        tag(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE).add(OBlocks.DEEPSLATE_LEAD_ORE.get(), OBlocks.DEEPSLATE_SILVER_ORE.get());

        // Mineables!
        /*tag(MINEABLE_WITH_BUSH_HAMMER).add(

        );*/
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                OBlocks.LEAD_ORE.get(),
                OBlocks.DEEPSLATE_LEAD_ORE.get(),
                OBlocks.RAW_LEAD_BLOCK.get(),

                OBlocks.SILVER_ORE.get(),
                OBlocks.DEEPSLATE_SILVER_ORE.get(),
                OBlocks.RAW_SILVER_BLOCK.get(),

                OBlocks.LEAD_BLOCK.get(),
                OBlocks.LEAD_BRICKS.get(),
                OBlocks.LEAD_PILLAR.get(),
                OBlocks.LEAD_BULB.get(),
                OBlocks.CUT_LEAD.get(),
                OBlocks.SILVER_BLOCK.get(),
                OBlocks.ELECTRUM_BLOCK.get(),

                OBlocks.GLANCE.get(),
                OBlocks.GLANCE_STAIRS.get(),
                OBlocks.GLANCE_SLAB.get(),
                OBlocks.POLISHED_GLANCE.get(),
                OBlocks.POLISHED_GLANCE_STAIRS.get(),
                OBlocks.POLISHED_GLANCE_SLAB.get(),
                OBlocks.GLANCE_WALL.get(),
                OBlocks.GLANCE_BRICKS.get(),
                OBlocks.GLANCE_BRICK_STAIRS.get(),
                OBlocks.GLANCE_BRICK_SLAB.get(),
                OBlocks.GLANCE_BRICK_WALL.get(),
                OBlocks.CHISELED_GLANCE.get(),
                OBlocks.SPOTTED_GLANCE.get(),
                OBlocks.WAXED_SPOTTED_GLANCE.get(),

                OBlocks.GARGOYLE.get(),

                OBlocks.MOLTEN_LEAD_CAULDRON.get(),

                OBlocks.LEAD_DOOR.get(),
                OBlocks.LEAD_TRAPDOOR.get(),
                OBlocks.LEAD_BARS.get(),

                OBlocks.GROOVED_ICE.get(),
                OBlocks.GROOVED_PACKED_ICE.get(),
                OBlocks.GROOVED_BLUE_ICE.get()
        );

        OBlocks.WAXED_CONCRETE_POWDER.forEach((c, b) -> tag(BlockTags.MINEABLE_WITH_SHOVEL).addOptional(b.getId()));

        tag(BlockTags.NEEDS_STONE_TOOL).add(
                OBlocks.LEAD_ORE.get(),
                OBlocks.DEEPSLATE_LEAD_ORE.get()
        );

        tag(BlockTags.NEEDS_IRON_TOOL).add(
                OBlocks.SILVER_ORE.get(),
                OBlocks.DEEPSLATE_SILVER_ORE.get()
        );

        tag(BlockTags.MINEABLE_WITH_AXE).add(OBlocks.LEAD_BOLT_CRATE.get());

        tag(MELTS_LEAD)
                .add(Blocks.LAVA)
                .add(Blocks.MAGMA_BLOCK)
                .addTags(BlockTags.CAMPFIRES)
                .addTags(BlockTags.FIRE);

        tag(BlockTags.ICE)
                .add(OBlocks.GROOVED_ICE.get())
                .add(OBlocks.GROOVED_PACKED_ICE.get())
                .add(OBlocks.GROOVED_BLUE_ICE.get());

        var scribeMineable = tag(MINEABLE_WITH_SCRIBE)
                .addTags(Tags.Blocks.GLASS)
                .addTags(Tags.Blocks.GLASS_PANES)
                .addTags(Tags.Blocks.OBSIDIAN)
                .addTags(BlockTags.ICE)
                .addTags(BlockTags.CRYSTAL_SOUND_BLOCKS)
                .addTags(Tags.Blocks.STORAGE_BLOCKS_AMETHYST)
                .add(Blocks.AMETHYST_CLUSTER)
                .add(Blocks.LARGE_AMETHYST_BUD)
                .add(Blocks.MEDIUM_AMETHYST_BUD)
                .add(Blocks.SMALL_AMETHYST_BUD);

        scribeMineable
                .addTags(Tags.Blocks.STORAGE_BLOCKS_QUARTZ)
                .add(Blocks.QUARTZ_BRICKS)
                .add(Blocks.QUARTZ_PILLAR)
                .add(Blocks.QUARTZ_SLAB)
                .add(Blocks.QUARTZ_STAIRS)
                .add(Blocks.CHISELED_QUARTZ_BLOCK)
                .add(Blocks.SMOOTH_QUARTZ)
                .add(Blocks.SMOOTH_QUARTZ_SLAB)
                .add(Blocks.SMOOTH_QUARTZ_STAIRS);

        Stream.of("%s", "waxed_%s", "%s_cluster", "%s_pane").forEach(pattern -> {
            scribeMineable
                    .addOptional(new ResourceLocation("quark", pattern.formatted("red_corundum")))
                    .addOptional(new ResourceLocation("quark", pattern.formatted("orange_corundum")))
                    .addOptional(new ResourceLocation("quark", pattern.formatted("yellow_corundum")))
                    .addOptional(new ResourceLocation("quark", pattern.formatted("green_corundum")))
                    .addOptional(new ResourceLocation("quark", pattern.formatted("blue_corundum")))
                    .addOptional(new ResourceLocation("quark", pattern.formatted("indigo_corundum")))
                    .addOptional(new ResourceLocation("quark", pattern.formatted("violet_corundum")))
                    .addOptional(new ResourceLocation("quark", pattern.formatted("white_corundum")))
                    .addOptional(new ResourceLocation("quark", pattern.formatted("black_corundum")))
            ;
        });

        tag(SILKTOUCH_WITH_SCRIBE_BLACKLIST)
                .add(OBlocks.GROOVED_ICE.get())
                .add(OBlocks.GROOVED_PACKED_ICE.get())
                .add(OBlocks.GROOVED_BLUE_ICE.get());

        tag(BlockTags.SNOW_LAYER_CANNOT_SURVIVE_ON)
                .add(OBlocks.GROOVED_ICE.get())
                .add(OBlocks.GROOVED_PACKED_ICE.get());

        tag(SILKTOUCH_WITH_SCRIBE)
                .addTags(MINEABLE_WITH_SCRIBE)
                .addTags(BlockTags.MINEABLE_WITH_PICKAXE);

        tag(PREVENTS_LEAD_CLOUD)
                .add(Blocks.WATER);

        tag(BLOWS_LEAD_CLOUD)
                .addTags(ORES_LEAD)
                .addTags(STORAGE_BLOCKS_RAW_LEAD);
    }
}
