package galena.oreganized.data;

import galena.oreganized.Oreganized;
import galena.oreganized.compat.ColorCompat;
import galena.oreganized.data.provider.OItemModelProvider;
import galena.oreganized.index.OBlocks;
import galena.oreganized.index.OItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;

public class OItemModels extends OItemModelProvider {

    public OItemModels(PackOutput output, ExistingFileHelper helper) {
        super(output, helper);
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
        normalItem(OItems.ELECTRUM_INGOT);
        normalItem(OItems.ELECTRUM_NUGGET);
        normalItem(OItems.NETHERITE_NUGGET);
        normalItem(OItems.MOLTEN_LEAD_BUCKET);
        trimmableArmorItem(OItems.ELECTRUM_HELMET);
        trimmableArmorItem(OItems.ELECTRUM_CHESTPLATE);
        trimmableArmorItem(OItems.ELECTRUM_LEGGINGS);
        trimmableArmorItem(OItems.ELECTRUM_BOOTS);
        normalItem(OItems.SHRAPNEL_BOMB_MINECART);
        normalItem(OItems.SHRAPNEL_BOMB_MINECART);
        normalItem(OItems.ELECTRUM_UPGRADE_SMITHING_TEMPLATE);
        normalItem(OItems.LEAD_BOLT);
        normalItem(OItems.FLINT_AND_PEWTER);

        toolItem(OItems.BUSH_HAMMER);
        toolItem(OItems.SCRIBE);
        toolItem(OItems.ELECTRUM_SWORD);
        toolItem(OItems.ELECTRUM_SHOVEL);
        toolItem(OItems.ELECTRUM_PICKAXE);
        toolItem(OItems.ELECTRUM_AXE);
        toolItem(OItems.ELECTRUM_HOE);
        toolItem(OItems.ELECTRUM_KNIFE);
        toolItem(OItems.ELECTRUM_MACHETE);
        shieldItem(OItems.ELECTRUM_SHIELD);

        crossbowOverwrite("crossbow_lead_bolt");

        block(OBlocks.GLANCE);
        block(OBlocks.POLISHED_GLANCE);
        block(OBlocks.GLANCE_BRICKS);
        block(OBlocks.CHISELED_GLANCE);
        block(OBlocks.GLANCE_SLAB);
        block(OBlocks.POLISHED_GLANCE_SLAB);
        block(OBlocks.GLANCE_BRICK_SLAB);
        block(OBlocks.GLANCE_STAIRS);
        block(OBlocks.POLISHED_GLANCE_STAIRS);
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
        block(OBlocks.SILVER_BLOCK);
        block(OBlocks.RAW_LEAD_BLOCK);
        block(OBlocks.LEAD_BLOCK);
        block(OBlocks.LEAD_BRICKS);
        block(OBlocks.LEAD_PILLAR);
        block(OBlocks.LEAD_BULB);
        block(OBlocks.CUT_LEAD);
        block(OBlocks.ELECTRUM_BLOCK);
        normalItem(OBlocks.GARGOYLE.get()::asItem);
        block(OBlocks.SHRAPNEL_BOMB);
        block(OBlocks.LEAD_BOLT_CRATE);

        normalItem(OBlocks.LEAD_DOOR.get()::asItem);
        block(OBlocks.LEAD_TRAPDOOR, blockName(OBlocks.LEAD_TRAPDOOR) + "_bottom");
        blockFlat(OBlocks.LEAD_BARS);

        OBlocks.CRYSTAL_GLASS.forEach((color, block) -> block(block));
        OBlocks.CRYSTAL_GLASS_PANES.forEach((color, block) -> blockFlat(block, OBlocks.CRYSTAL_GLASS.get(color)));

        block(OBlocks.GROOVED_ICE);
        block(OBlocks.GROOVED_PACKED_ICE);
        block(OBlocks.GROOVED_BLUE_ICE);

        OBlocks.WAXED_CONCRETE_POWDER.forEach((color, waxed) -> {
            var unwaxed = ColorCompat.createId("concrete_powder", color);
            withExistingParent(blockName(waxed), unwaxed.withPrefix("block/"));
        });
    }

}
