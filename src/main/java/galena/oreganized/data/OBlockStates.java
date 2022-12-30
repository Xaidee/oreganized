package galena.oreganized.data;

import galena.oreganized.Oreganized;
import galena.oreganized.data.provider.OBlockStateProvider;
import galena.oreganized.index.OBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class OBlockStates extends OBlockStateProvider {

    public OBlockStates(DataGenerator gen, ExistingFileHelper help) {
        super(gen, help);
    }

    @Override
    public String getName() {
        return Oreganized.MOD_ID + " Block States";
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(OBlocks.GLANCE);
        simpleBlock(OBlocks.POLISHED_GLANCE);
        simpleBlock(OBlocks.GLANCE_BRICKS);
        simpleBlock(OBlocks.CHISELED_GLANCE);
        slabBlock(OBlocks.GLANCE_SLAB, OBlocks.GLANCE);
        slabBlock(OBlocks.GLANCE_BRICK_SLAB, OBlocks.GLANCE_BRICKS);
        stairsBlock(OBlocks.GLANCE_STAIRS, OBlocks.GLANCE);
        stairsBlock(OBlocks.GLANCE_BRICK_STAIRS, OBlocks.GLANCE_BRICKS);
        wallBlock(OBlocks.GLANCE_WALL, OBlocks.GLANCE);
        wallBlock(OBlocks.GLANCE_BRICK_WALL, OBlocks.GLANCE_BRICKS);
        simpleBlock(OBlocks.SPOTTED_GLANCE);
        waxedBlock(OBlocks.WAXED_SPOTTED_GLANCE, OBlocks.SPOTTED_GLANCE.get());
        simpleBlock(OBlocks.SILVER_ORE);
        simpleBlock(OBlocks.DEEPSLATE_SILVER_ORE);
        simpleBlock(OBlocks.LEAD_ORE);
        simpleBlock(OBlocks.DEEPSLATE_LEAD_ORE);
        simpleBlock(OBlocks.RAW_SILVER_BLOCK);
        simpleBlock(OBlocks.RAW_LEAD_BLOCK);
        simpleBlock(OBlocks.LEAD_BLOCK);
        simpleBlock(OBlocks.ELECTRUM_BLOCK);
        simpleBlock(OBlocks.SHRAPNEL_BOMB.get(), cubeBottomTop(OBlocks.SHRAPNEL_BOMB));

        axisBlock(OBlocks.CUT_LEAD);

        waxedBlock(OBlocks.WAXED_WHITE_CONCRETE_POWDER, Blocks.WHITE_CONCRETE_POWDER);
        waxedBlock(OBlocks.WAXED_ORANGE_CONCRETE_POWDER, Blocks.ORANGE_CONCRETE_POWDER);
        waxedBlock(OBlocks.WAXED_MAGENTA_CONCRETE_POWDER, Blocks.MAGENTA_CONCRETE_POWDER);
        waxedBlock(OBlocks.WAXED_LIGHT_BLUE_CONCRETE_POWDER, Blocks.LIGHT_BLUE_CONCRETE_POWDER);
        waxedBlock(OBlocks.WAXED_YELLOW_CONCRETE_POWDER, Blocks.YELLOW_CONCRETE_POWDER);
        waxedBlock(OBlocks.WAXED_LIME_CONCRETE_POWDER, Blocks.LIME_CONCRETE_POWDER);
        waxedBlock(OBlocks.WAXED_PINK_CONCRETE_POWDER, Blocks.PINK_CONCRETE_POWDER);
        waxedBlock(OBlocks.WAXED_GRAY_CONCRETE_POWDER, Blocks.GRAY_CONCRETE_POWDER);
        waxedBlock(OBlocks.WAXED_LIGHT_GRAY_CONCRETE_POWDER, Blocks.LIGHT_GRAY_CONCRETE_POWDER);
        waxedBlock(OBlocks.WAXED_CYAN_CONCRETE_POWDER, Blocks.CYAN_CONCRETE_POWDER);
        waxedBlock(OBlocks.WAXED_PURPLE_CONCRETE_POWDER, Blocks.PURPLE_CONCRETE_POWDER);
        waxedBlock(OBlocks.WAXED_BLUE_CONCRETE_POWDER, Blocks.BLUE_CONCRETE_POWDER);
        waxedBlock(OBlocks.WAXED_BROWN_CONCRETE_POWDER, Blocks.BROWN_CONCRETE_POWDER);
        waxedBlock(OBlocks.WAXED_GREEN_CONCRETE_POWDER, Blocks.GREEN_CONCRETE_POWDER);
        waxedBlock(OBlocks.WAXED_RED_CONCRETE_POWDER, Blocks.RED_CONCRETE_POWDER);
        waxedBlock(OBlocks.WAXED_BLACK_CONCRETE_POWDER, Blocks.BLACK_CONCRETE_POWDER);

        exposer(OBlocks.EXPOSER);

        moltenCauldron(OBlocks.MOLTEN_LEAD_CAULDRON, OBlocks.LEAD_BLOCK);

        crystalGlassBlock(OBlocks.WHITE_CRYSTAL_GLASS);
        crystalGlassPaneBlock(OBlocks.WHITE_CRYSTAL_GLASS_PANE, OBlocks.WHITE_CRYSTAL_GLASS);
        crystalGlassBlock(OBlocks.ORANGE_CRYSTAL_GLASS);
        crystalGlassPaneBlock(OBlocks.ORANGE_CRYSTAL_GLASS_PANE, OBlocks.ORANGE_CRYSTAL_GLASS);
        crystalGlassBlock(OBlocks.MAGENTA_CRYSTAL_GLASS);
        crystalGlassPaneBlock(OBlocks.MAGENTA_CRYSTAL_GLASS_PANE, OBlocks.MAGENTA_CRYSTAL_GLASS);
        crystalGlassBlock(OBlocks.LIGHT_BLUE_CRYSTAL_GLASS);
        crystalGlassPaneBlock(OBlocks.LIGHT_BLUE_CRYSTAL_GLASS_PANE, OBlocks.LIGHT_BLUE_CRYSTAL_GLASS);
        crystalGlassBlock(OBlocks.YELLOW_CRYSTAL_GLASS);
        crystalGlassPaneBlock(OBlocks.YELLOW_CRYSTAL_GLASS_PANE, OBlocks.YELLOW_CRYSTAL_GLASS);
        crystalGlassBlock(OBlocks.LIME_CRYSTAL_GLASS);
        crystalGlassPaneBlock(OBlocks.LIME_CRYSTAL_GLASS_PANE, OBlocks.LIME_CRYSTAL_GLASS);
        crystalGlassBlock(OBlocks.PINK_CRYSTAL_GLASS);
        crystalGlassPaneBlock(OBlocks.PINK_CRYSTAL_GLASS_PANE, OBlocks.PINK_CRYSTAL_GLASS);
        crystalGlassBlock(OBlocks.GRAY_CRYSTAL_GLASS);
        crystalGlassPaneBlock(OBlocks.GRAY_CRYSTAL_GLASS_PANE, OBlocks.GRAY_CRYSTAL_GLASS);
        crystalGlassBlock(OBlocks.LIGHT_GRAY_CRYSTAL_GLASS);
        crystalGlassPaneBlock(OBlocks.LIGHT_GRAY_CRYSTAL_GLASS_PANE, OBlocks.LIGHT_GRAY_CRYSTAL_GLASS);
        crystalGlassBlock(OBlocks.CYAN_CRYSTAL_GLASS);
        crystalGlassPaneBlock(OBlocks.CYAN_CRYSTAL_GLASS_PANE, OBlocks.CYAN_CRYSTAL_GLASS);
        crystalGlassBlock(OBlocks.PURPLE_CRYSTAL_GLASS);
        crystalGlassPaneBlock(OBlocks.PURPLE_CRYSTAL_GLASS_PANE, OBlocks.PURPLE_CRYSTAL_GLASS);
        crystalGlassBlock(OBlocks.BLUE_CRYSTAL_GLASS);
        crystalGlassPaneBlock(OBlocks.BLUE_CRYSTAL_GLASS_PANE, OBlocks.BLUE_CRYSTAL_GLASS);
        crystalGlassBlock(OBlocks.BROWN_CRYSTAL_GLASS);
        crystalGlassPaneBlock(OBlocks.BROWN_CRYSTAL_GLASS_PANE, OBlocks.BROWN_CRYSTAL_GLASS);
        crystalGlassBlock(OBlocks.GREEN_CRYSTAL_GLASS);
        crystalGlassPaneBlock(OBlocks.GREEN_CRYSTAL_GLASS_PANE, OBlocks.GREEN_CRYSTAL_GLASS);
        crystalGlassBlock(OBlocks.RED_CRYSTAL_GLASS);
        crystalGlassPaneBlock(OBlocks.RED_CRYSTAL_GLASS_PANE, OBlocks.RED_CRYSTAL_GLASS);
        crystalGlassBlock(OBlocks.BLACK_CRYSTAL_GLASS);
        crystalGlassPaneBlock(OBlocks.BLACK_CRYSTAL_GLASS_PANE, OBlocks.BLACK_CRYSTAL_GLASS);
    }

}
