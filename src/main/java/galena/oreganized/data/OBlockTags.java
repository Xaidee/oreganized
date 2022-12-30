package galena.oreganized.data;

import galena.oreganized.Oreganized;
import galena.oreganized.index.OBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static galena.oreganized.index.OTags.Blocks.*;

public class OBlockTags extends BlockTagsProvider {

    public OBlockTags(DataGenerator generator, @Nullable ExistingFileHelper helper) {
        super(generator, Oreganized.MOD_ID, helper);
    }

    @Override
    public @NotNull String getName() {
        return "Oreganized Block Tags";
    }

    @Override
    protected void addTags() {
        // Oreganized
        for (int i = 0; OBlocks.CRYSTAL_GLASS.size() > i; i++) {
            tag(CRYSTAL_GLASS).add(OBlocks.CRYSTAL_GLASS.get(i).get());
            tag(CRYSTAL_GLASS_PANES).add(OBlocks.CRYSTAL_GLASS_PANES.get(i).get());
        }
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
        tag(BlockTags.STAIRS).add(OBlocks.GLANCE_STAIRS.get(), OBlocks.GLANCE_BRICK_STAIRS.get());
        tag(BlockTags.SLABS).add(OBlocks.GLANCE_SLAB.get(), OBlocks.GLANCE_BRICK_SLAB.get());
        tag(BlockTags.BEACON_BASE_BLOCKS).add(OBlocks.ELECTRUM_BLOCK.get());
        tag(BlockTags.IMPERMEABLE).addTag(CRYSTAL_GLASS);
        tag(BlockTags.CAULDRONS).add(OBlocks.MOLTEN_LEAD_CAULDRON.get());

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
                OBlocks.SILVER_BLOCK.get(),
                OBlocks.ELECTRUM_BLOCK.get(),

                OBlocks.GLANCE.get(),
                OBlocks.GLANCE_STAIRS.get(),
                OBlocks.GLANCE_SLAB.get(),
                OBlocks.POLISHED_GLANCE.get(),
                OBlocks.GLANCE_WALL.get(),
                OBlocks.GLANCE_BRICKS.get(),
                OBlocks.GLANCE_BRICK_STAIRS.get(),
                OBlocks.GLANCE_BRICK_SLAB.get(),
                OBlocks.GLANCE_BRICK_WALL.get(),
                OBlocks.CHISELED_GLANCE.get(),
                OBlocks.SPOTTED_GLANCE.get(),
                OBlocks.WAXED_SPOTTED_GLANCE.get(),

                OBlocks.EXPOSER.get(),

                OBlocks.CUT_LEAD.get(),

                OBlocks.MOLTEN_LEAD_CAULDRON.get()
        );
        tag(BlockTags.MINEABLE_WITH_SHOVEL).add(
                OBlocks.WAXED_WHITE_CONCRETE_POWDER.get(),
                OBlocks.WAXED_ORANGE_CONCRETE_POWDER.get(),
                OBlocks.WAXED_MAGENTA_CONCRETE_POWDER.get(),
                OBlocks.WAXED_LIGHT_BLUE_CONCRETE_POWDER.get(),
                OBlocks.WAXED_YELLOW_CONCRETE_POWDER.get(),
                OBlocks.WAXED_LIME_CONCRETE_POWDER.get(),
                OBlocks.WAXED_PINK_CONCRETE_POWDER.get(),
                OBlocks.WAXED_GRAY_CONCRETE_POWDER.get(),
                OBlocks.WAXED_LIGHT_GRAY_CONCRETE_POWDER.get(),
                OBlocks.WAXED_CYAN_CONCRETE_POWDER.get(),
                OBlocks.WAXED_PURPLE_CONCRETE_POWDER.get(),
                OBlocks.WAXED_BLUE_CONCRETE_POWDER.get(),
                OBlocks.WAXED_BROWN_CONCRETE_POWDER.get(),
                OBlocks.WAXED_GREEN_CONCRETE_POWDER.get(),
                OBlocks.WAXED_RED_CONCRETE_POWDER.get(),
                OBlocks.WAXED_BLACK_CONCRETE_POWDER.get()
        );

        tag(BlockTags.NEEDS_STONE_TOOL).add(
                OBlocks.LEAD_ORE.get(),
                OBlocks.DEEPSLATE_LEAD_ORE.get()
        );

        tag(BlockTags.NEEDS_IRON_TOOL).add(
                OBlocks.SILVER_ORE.get(),
                OBlocks.DEEPSLATE_SILVER_ORE.get()
        );
    }
}
