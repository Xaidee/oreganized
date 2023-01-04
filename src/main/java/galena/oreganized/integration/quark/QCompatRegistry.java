package galena.oreganized.integration.quark;

import galena.oreganized.index.OBlocks;
import galena.oreganized.integration.quark.block.VerticalSlabBlock;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.RegistryObject;

public class QCompatRegistry {

    // Glance
    public static final RegistryObject<RotatedPillarBlock> GLANCE_PILLAR = OBlocks.register("glance_pillar", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(OBlocks.POLISHED_GLANCE.get())), CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final RegistryObject<VerticalSlabBlock> GLANCE_VERTICAL_SLAB = OBlocks.register("glance_vertical_slab", () -> new VerticalSlabBlock(BlockBehaviour.Properties.copy(OBlocks.GLANCE_SLAB.get())), CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final RegistryObject<VerticalSlabBlock> GLANCE_BRICK_VERTICAL_SLAB = OBlocks.register("glance_brick_vertical_slab", () -> new VerticalSlabBlock(BlockBehaviour.Properties.copy(OBlocks.GLANCE_BRICK_SLAB.get())), CreativeModeTab.TAB_BUILDING_BLOCKS);

    // Raw Ore deco blocks
    public static final RegistryObject<Block> RAW_LEAD_BRICKS = OBlocks.register("raw_lead_bricks", () -> new Block(BlockBehaviour.Properties.copy(OBlocks.RAW_LEAD_BLOCK.get())), CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final RegistryObject<Block> RAW_SILVER_BRICKS = OBlocks.register("raw_silver_bricks", () -> new Block(BlockBehaviour.Properties.copy(OBlocks.RAW_SILVER_BLOCK.get())), CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final RegistryObject<SlabBlock> RAW_LEAD_BRICK_SLAB = OBlocks.register("raw_lead_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(OBlocks.RAW_LEAD_BLOCK.get())), CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final RegistryObject<SlabBlock> RAW_SILVER_BRICK_SLAB = OBlocks.register("raw_silver_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(OBlocks.RAW_SILVER_BLOCK.get())), CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final RegistryObject<StairBlock> RAW_LEAD_BRICK_STAIRS = OBlocks.register("raw_lead_brick_stairs", () -> new StairBlock(() -> RAW_LEAD_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(OBlocks.RAW_LEAD_BLOCK.get())), CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final RegistryObject<StairBlock> RAW_SILVER_BRICK_STAIRS = OBlocks.register("raw_silver_brick_stairs", () -> new StairBlock(() -> RAW_SILVER_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(OBlocks.RAW_SILVER_BLOCK.get())), CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final RegistryObject<VerticalSlabBlock> RAW_LEAD_BRICK_VERTICAL_SLAB = OBlocks.register("raw_lead_brick_vertical_slab", () -> new VerticalSlabBlock(BlockBehaviour.Properties.copy(OBlocks.RAW_LEAD_BLOCK.get())), CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final RegistryObject<VerticalSlabBlock> RAW_SILVER_BRICK_VERTICAL_SLAB = OBlocks.register("raw_silver_brick_vertical_slab", () -> new VerticalSlabBlock(BlockBehaviour.Properties.copy(OBlocks.RAW_SILVER_BLOCK.get())), CreativeModeTab.TAB_BUILDING_BLOCKS);

    //public static final RegistryObject<EntityType<Boltarang>> BOLTARANG = ENTITIES.register("boltarang", () -> EntityType.Builder.<Boltarang>of(Boltarang::new, MobCategory.MISC).sized(0.4F, 0.4F).clientTrackingRange(4).updateInterval(10).build("bolterang"));

    //public static final RegistryObject<BoltarangItem> BOLTARANG_ITEM = ITEMS.register("boltarang", () -> new BoltarangItem(PickarangModule.flamerang));

    public static void register() {

    }

    @OnlyIn(Dist.CLIENT)
    public static void clientSetup() {
        //EntityRenderers.register(BOLTARANG.get(), PickarangRenderer::new);
    }
}
