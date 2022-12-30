package galena.oreganized.data;

import galena.oreganized.Oreganized;
import galena.oreganized.data.provider.OItemModelProvider;
import galena.oreganized.index.OBlocks;
import galena.oreganized.index.OItems;
import galena.oreganized.integration.farmersdelight.FDCompatRegistry;
import galena.oreganized.integration.nethersdelight.NDCompatRegistry;
import galena.oreganized.integration.quark.QCompatRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class OItemModels extends OItemModelProvider {

    public OItemModels(DataGenerator gen, ExistingFileHelper help) {
        super(gen, help);
    }

    @Override
    public String getName() {
        return Oreganized.MOD_ID + " Item Models";
    }

    @Override
    protected void registerModels() {
        normalItem(OItems.MUSIC_DISC_STRUCTURE);
        normalItem(OItems.RAW_SILVER);
        normalItem(OItems.SILVER_INGOT);
        normalItem(OItems.SILVER_NUGGET);
        normalItem(OItems.RAW_LEAD);
        normalItem(OItems.LEAD_INGOT);
        normalItem(OItems.LEAD_NUGGET);
        normalItem(OItems.ELECTRUM_INGOT);
        normalItem(OItems.ELECTRUM_NUGGET);
        normalItem(OItems.NETHERITE_NUGGET);
        normalItem(OItems.MOLTEN_LEAD_BUCKET);
        normalItem(OItems.ELECTRUM_HELMET);
        normalItem(OItems.ELECTRUM_CHESTPLATE);
        normalItem(OItems.ELECTRUM_LEGGINGS);
        normalItem(OItems.ELECTRUM_BOOTS);
        normalItem(OItems.SHRAPNEL_BOMB_MINECART);

        toolItem(OItems.BUSH_HAMMER);
        toolItem(OItems.ELECTRUM_SWORD);
        toolItem(OItems.ELECTRUM_SHOVEL);
        toolItem(OItems.ELECTRUM_PICKAXE);
        toolItem(OItems.ELECTRUM_AXE);
        toolItem(OItems.ELECTRUM_HOE);
        toolItem(FDCompatRegistry.ELECTRUM_KNIFE);
        toolItem(NDCompatRegistry.ELECTRUM_MACHETE);

        block(OBlocks.GLANCE);
        block(OBlocks.POLISHED_GLANCE);
        block(OBlocks.GLANCE_BRICKS);
        block(OBlocks.CHISELED_GLANCE);
        block(OBlocks.GLANCE_SLAB);
        block(OBlocks.GLANCE_BRICK_SLAB);
        block(OBlocks.GLANCE_STAIRS);
        block(OBlocks.GLANCE_BRICK_STAIRS);
        wall(OBlocks.GLANCE_WALL, OBlocks.GLANCE);
        wall(OBlocks.GLANCE_BRICK_WALL, OBlocks.GLANCE_BRICKS);
        block(OBlocks.SPOTTED_GLANCE);
        block(OBlocks.WAXED_SPOTTED_GLANCE, blockName(OBlocks.SPOTTED_GLANCE));
        block(OBlocks.SILVER_ORE);
        block(OBlocks.DEEPSLATE_SILVER_ORE);
        block(OBlocks.LEAD_ORE);
        block(OBlocks.DEEPSLATE_LEAD_ORE);
        block(OBlocks.RAW_SILVER_BLOCK);
        block(OBlocks.RAW_LEAD_BLOCK);
        block(OBlocks.LEAD_BLOCK);
        block(OBlocks.ELECTRUM_BLOCK);
        block(OBlocks.EXPOSER, "exposer_level_0_south");
        block(OBlocks.SHRAPNEL_BOMB);
        block(OBlocks.CUT_LEAD);

        block(OBlocks.WHITE_CRYSTAL_GLASS);
        blockFlat(OBlocks.WHITE_CRYSTAL_GLASS_PANE, OBlocks.WHITE_CRYSTAL_GLASS);
        block(OBlocks.ORANGE_CRYSTAL_GLASS);
        blockFlat(OBlocks.ORANGE_CRYSTAL_GLASS_PANE, OBlocks.ORANGE_CRYSTAL_GLASS);
        block(OBlocks.MAGENTA_CRYSTAL_GLASS);
        blockFlat(OBlocks.MAGENTA_CRYSTAL_GLASS_PANE, OBlocks.MAGENTA_CRYSTAL_GLASS);
        block(OBlocks.LIGHT_BLUE_CRYSTAL_GLASS);
        blockFlat(OBlocks.LIGHT_BLUE_CRYSTAL_GLASS_PANE, OBlocks.LIGHT_BLUE_CRYSTAL_GLASS);
        block(OBlocks.YELLOW_CRYSTAL_GLASS);
        blockFlat(OBlocks.YELLOW_CRYSTAL_GLASS_PANE, OBlocks.YELLOW_CRYSTAL_GLASS);
        block(OBlocks.LIME_CRYSTAL_GLASS);
        blockFlat(OBlocks.LIME_CRYSTAL_GLASS_PANE, OBlocks.LIME_CRYSTAL_GLASS);
        block(OBlocks.PINK_CRYSTAL_GLASS);
        blockFlat(OBlocks.PINK_CRYSTAL_GLASS_PANE, OBlocks.PINK_CRYSTAL_GLASS);
        block(OBlocks.GRAY_CRYSTAL_GLASS);
        blockFlat(OBlocks.GRAY_CRYSTAL_GLASS_PANE, OBlocks.GRAY_CRYSTAL_GLASS);
        block(OBlocks.LIGHT_GRAY_CRYSTAL_GLASS);
        blockFlat(OBlocks.LIGHT_GRAY_CRYSTAL_GLASS_PANE, OBlocks.LIGHT_GRAY_CRYSTAL_GLASS);
        block(OBlocks.CYAN_CRYSTAL_GLASS);
        blockFlat(OBlocks.CYAN_CRYSTAL_GLASS_PANE, OBlocks.CYAN_CRYSTAL_GLASS);
        block(OBlocks.PURPLE_CRYSTAL_GLASS);
        blockFlat(OBlocks.PURPLE_CRYSTAL_GLASS_PANE, OBlocks.PURPLE_CRYSTAL_GLASS);
        block(OBlocks.BLUE_CRYSTAL_GLASS);
        blockFlat(OBlocks.BLUE_CRYSTAL_GLASS_PANE, OBlocks.BLUE_CRYSTAL_GLASS);
        block(OBlocks.BROWN_CRYSTAL_GLASS);
        blockFlat(OBlocks.BROWN_CRYSTAL_GLASS_PANE, OBlocks.BROWN_CRYSTAL_GLASS);
        block(OBlocks.GREEN_CRYSTAL_GLASS);
        blockFlat(OBlocks.GREEN_CRYSTAL_GLASS_PANE, OBlocks.GREEN_CRYSTAL_GLASS);
        block(OBlocks.RED_CRYSTAL_GLASS);
        blockFlat(OBlocks.RED_CRYSTAL_GLASS_PANE, OBlocks.RED_CRYSTAL_GLASS);
        block(OBlocks.BLACK_CRYSTAL_GLASS);
        blockFlat(OBlocks.BLACK_CRYSTAL_GLASS_PANE, OBlocks.BLACK_CRYSTAL_GLASS);

        for (Supplier<? extends Block> blocks : OBlocks.WAXED_CONRETE_POWDER) {
            withExistingParent(blockName(blocks), "minecraft:block/" + blockName(blocks).replace("waxed_", ""));
        }
    }
}
