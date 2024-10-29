package galena.oreganized.data;

import galena.oreganized.Oreganized;
import galena.oreganized.compat.ColorCompat;
import galena.oreganized.data.provider.OBlockStateProvider;
import galena.oreganized.index.OBlocks;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;

public class OBlockStates extends OBlockStateProvider {

    public OBlockStates(PackOutput output, ExistingFileHelper helper) {
        super(output, helper);
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
        slabBlock(OBlocks.POLISHED_GLANCE_SLAB, OBlocks.POLISHED_GLANCE);
        slabBlock(OBlocks.GLANCE_BRICK_SLAB, OBlocks.GLANCE_BRICKS);
        stairsBlock(OBlocks.GLANCE_STAIRS, OBlocks.GLANCE);
        stairsBlock(OBlocks.POLISHED_GLANCE_STAIRS, OBlocks.POLISHED_GLANCE);
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
        simpleBlock(OBlocks.SILVER_BLOCK);
        meltableBlock(OBlocks.LEAD_BLOCK, (n, t) -> models().cubeAll(n, t));
        meltableBlock(OBlocks.LEAD_BRICKS, (n, t) -> models().cubeAll(n, t));
        meltablePillar(OBlocks.LEAD_PILLAR);
        meltablePillar(OBlocks.CUT_LEAD);
        bulb(OBlocks.LEAD_BULB);
        simpleBlock(OBlocks.ELECTRUM_BLOCK);
        simpleBlock(OBlocks.SHRAPNEL_BOMB.get(), cubeBottomTop(OBlocks.SHRAPNEL_BOMB));

        //doorBlock(OBlocks.LEAD_DOOR.get(), blockTexture(OBlocks.LEAD_DOOR.get()).withSuffix("_bottom"), blockTexture(OBlocks.LEAD_DOOR.get()).withSuffix("_top"));
        meltableDoor(OBlocks.LEAD_DOOR);
        meltableTrapdoor(OBlocks.LEAD_TRAPDOOR);
        meltableBars(OBlocks.LEAD_BARS);

        OBlocks.WAXED_CONCRETE_POWDER.forEach((color, block) -> {
            var unwaxed = ColorCompat.getColoredBlock("concrete_powder", color);
            waxedBlock(block, unwaxed);
        });

        crate(OBlocks.LEAD_BOLT_CRATE);

        moltenCauldron(OBlocks.MOLTEN_LEAD_CAULDRON, OBlocks.LEAD_BLOCK);

        OBlocks.CRYSTAL_GLASS.forEach((color, block) -> crystalGlassBlock(block));
        OBlocks.CRYSTAL_GLASS_PANES.forEach((color, block) -> crystalGlassPaneBlock(color, block, OBlocks.CRYSTAL_GLASS.get(color)));

        simpleBlock(OBlocks.GROOVED_ICE);
        simpleBlock(OBlocks.GROOVED_PACKED_ICE);
        simpleBlock(OBlocks.GROOVED_BLUE_ICE);

        gargoyleBlock(OBlocks.GARGOYLE);
    }

}
