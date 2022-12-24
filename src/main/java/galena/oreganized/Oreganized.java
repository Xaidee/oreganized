package galena.oreganized;

import com.google.common.collect.ImmutableBiMap;
import com.redlimerl.detailab.api.DetailArmorBarAPI;
import com.redlimerl.detailab.api.render.ArmorBarRenderManager;
import com.redlimerl.detailab.api.render.TextureOffset;
import galena.oreganized.client.OreganizedClient;
import galena.oreganized.client.render.gui.StunningOverlay;
import galena.oreganized.content.block.MoltenLeadCauldronBlock;
import galena.oreganized.data.*;
import galena.oreganized.index.*;
import galena.oreganized.integration.CompatHandler;
import galena.oreganized.integration.CompatHandlerClient;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

@Mod(Oreganized.MOD_ID)
public class Oreganized {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "oreganized";

    public static final List<DyeColor> DYE_COLORS = List.of();

    public Oreganized() {

        final IEventBus bus = Bus.MOD.bus().get();
        final ModLoadingContext context = ModLoadingContext.get();
        bus.addListener(this::setup);
        bus.addListener(this::clientSetup);
        bus.addListener(this::gatherData);

        MinecraftForge.EVENT_BUS.addListener(OFeatures::onBiomeLoadingEvent);

        DeferredRegister<?>[] registers = {
                OBlockEntities.BLOCK_ENTITIES,
                OBlocks.BLOCKS,
                OEffects.EFFECTS,
                OEntityTypes.ENTITIES,
                OFluids.FLUIDS,
                OItems.ITEMS,
                OParticleTypes.PARTICLES,
                OPotions.POTIONS,
                OSoundEvents.SOUNDS,
                OStructures.STRUCTURES,
        };

        for (DeferredRegister<?> register : registers) {
            register.register(bus);
        }

        CompatHandler.register();

        //context.registerConfig(ModConfig.Type.COMMON, OreganizedConfig.COMMON_SPEC);
        //context.registerConfig(ModConfig.Type.CLIENT, OreganizedConfig.CLIENT_SPEC);
    }

    private void setup(FMLCommonSetupEvent event) {
        /*FluidInteractionRegistry.addInteraction(OFluids.MOLTEN_LEAD_TYPE.get(), new FluidInteractionRegistry.InteractionInformation(
                ForgeMod.WATER_TYPE.get(),
                fluidState -> OBlocks.LEAD_BLOCK.get().defaultBlockState()
        ));*/

        event.enqueueWork(() -> {
            OFeatures.registerOreFeatures();

            Map<Item, CauldronInteraction> EMPTY = CauldronInteraction.EMPTY;
            Map<Item, CauldronInteraction> WATER = CauldronInteraction.WATER;
            Map<Item, CauldronInteraction> LAVA = CauldronInteraction.LAVA;
            Map<Item, CauldronInteraction> POWDER_SNOW = CauldronInteraction.POWDER_SNOW;
            Map<Item, CauldronInteraction> LEAD = MoltenLeadCauldronBlock.INTERACTION_MAP;

            EMPTY.put(OItems.MOLTEN_LEAD_BUCKET.get(), MoltenLeadCauldronBlock.FILL_MOLTEN_LEAD);
            WATER.put(OItems.MOLTEN_LEAD_BUCKET.get(), MoltenLeadCauldronBlock.FILL_MOLTEN_LEAD);
            LAVA.put(OItems.MOLTEN_LEAD_BUCKET.get(), MoltenLeadCauldronBlock.FILL_MOLTEN_LEAD);
            POWDER_SNOW.put(OItems.MOLTEN_LEAD_BUCKET.get(), MoltenLeadCauldronBlock.FILL_MOLTEN_LEAD);
            LEAD.put(OItems.MOLTEN_LEAD_BUCKET.get(), MoltenLeadCauldronBlock.FILL_MOLTEN_LEAD);

            EMPTY.put(OBlocks.LEAD_BLOCK.get().asItem(), MoltenLeadCauldronBlock.FILL_LEAD_BLOCK);
            WATER.put(OBlocks.LEAD_BLOCK.get().asItem(), MoltenLeadCauldronBlock.FILL_LEAD_BLOCK);
            LAVA.put(OBlocks.LEAD_BLOCK.get().asItem(), MoltenLeadCauldronBlock.FILL_LEAD_BLOCK);
            POWDER_SNOW.put(OBlocks.LEAD_BLOCK.get().asItem(), MoltenLeadCauldronBlock.FILL_LEAD_BLOCK);

            LEAD.put(Items.AIR, MoltenLeadCauldronBlock.EMPTY_LEAD_BLOCK);
            LEAD.put(Items.BUCKET, MoltenLeadCauldronBlock.EMPTY_MOLTEN_LEAD);

            CauldronInteraction.addDefaultInteractions(MoltenLeadCauldronBlock.INTERACTION_MAP);

            PotionBrewing.addMix(Potions.WATER, OItems.LEAD_INGOT.get(), OPotions.STUNNING.get());
            PotionBrewing.addMix(OPotions.STUNNING.get(), Items.REDSTONE, OPotions.LONG_STUNNING.get());
            PotionBrewing.addMix(OPotions.STUNNING.get(), Items.GLOWSTONE_DUST, OPotions.STRONG_STUNNING.get());

            FireBlock fire = (FireBlock) Blocks.FIRE;
            fire.setFlammable(OBlocks.SHRAPNEL_BOMB.get(), 15, 100);
        });

        OBlocks.WAXED_BLOCKS = new ImmutableBiMap.Builder<Block, Block>()
                .put(OBlocks.WAXED_SPOTTED_GLANCE.get(), OBlocks.SPOTTED_GLANCE.get())
                .put(OBlocks.WAXED_WHITE_CONCRETE_POWDER.get(), Blocks.WHITE_CONCRETE_POWDER)
                .put(OBlocks.WAXED_ORANGE_CONCRETE_POWDER.get(), Blocks.ORANGE_CONCRETE_POWDER)
                .put(OBlocks.WAXED_MAGENTA_CONCRETE_POWDER.get(), Blocks.MAGENTA_CONCRETE_POWDER)
                .put(OBlocks.WAXED_LIGHT_BLUE_CONCRETE_POWDER.get(), Blocks.LIGHT_BLUE_CONCRETE_POWDER)
                .put(OBlocks.WAXED_YELLOW_CONCRETE_POWDER.get(), Blocks.YELLOW_CONCRETE_POWDER)
                .put(OBlocks.WAXED_LIME_CONCRETE_POWDER.get(), Blocks.LIME_CONCRETE_POWDER)
                .put(OBlocks.WAXED_PINK_CONCRETE_POWDER.get(), Blocks.PINK_CONCRETE_POWDER)
                .put(OBlocks.WAXED_GRAY_CONCRETE_POWDER.get(), Blocks.GRAY_CONCRETE_POWDER)
                .put(OBlocks.WAXED_LIGHT_GRAY_CONCRETE_POWDER.get(), Blocks.LIGHT_GRAY_CONCRETE_POWDER)
                .put(OBlocks.WAXED_CYAN_CONCRETE_POWDER.get(), Blocks.CYAN_CONCRETE_POWDER)
                .put(OBlocks.WAXED_PURPLE_CONCRETE_POWDER.get(), Blocks.PURPLE_CONCRETE_POWDER)
                .put(OBlocks.WAXED_BLUE_CONCRETE_POWDER.get(), Blocks.BLUE_CONCRETE_POWDER)
                .put(OBlocks.WAXED_BROWN_CONCRETE_POWDER.get(), Blocks.BROWN_CONCRETE_POWDER)
                .put(OBlocks.WAXED_GREEN_CONCRETE_POWDER.get(), Blocks.GREEN_CONCRETE_POWDER)
                .put(OBlocks.WAXED_RED_CONCRETE_POWDER.get(), Blocks.RED_CONCRETE_POWDER)
                .put(OBlocks.WAXED_BLACK_CONCRETE_POWDER.get(), Blocks.BLACK_CONCRETE_POWDER)
        .build();
    }

    private void clientSetup(FMLClientSetupEvent event) {
        CompatHandlerClient.setup(event);
        OreganizedClient.registerBlockRenderers();

        MinecraftForge.EVENT_BUS.register(new StunningOverlay());
        OverlayRegistry.registerOverlayAbove(ForgeIngameGui.FROSTBITE_ELEMENT, "stunning", new StunningOverlay());

        ItemProperties.register(OItems.SILVER_MIRROR.get(), new ResourceLocation("level"), (stack, world, entity, seed) -> {
            if(entity == null) {
                return 8;
            } else {
                return stack.getOrCreateTag().getInt("Level");
            }
        });

        if (ModList.get().isLoaded("detailab")) {
            ResourceLocation texture = new ResourceLocation(MOD_ID, "textures/gui/armor_bar.png");
            DetailArmorBarAPI.customArmorBarBuilder().armor((ArmorItem) OItems.ELECTRUM_CHESTPLATE.get(), (ArmorItem) OItems.ELECTRUM_HELMET.get(), (ArmorItem) OItems.ELECTRUM_LEGGINGS.get(), (ArmorItem) OItems.ELECTRUM_BOOTS.get())
                    .render((ItemStack itemStack) ->
                            new ArmorBarRenderManager(texture, 18, 18,
                                    new TextureOffset(9, 9), new TextureOffset(0, 9), new TextureOffset(9, 0), new TextureOffset(0, 0))
                    ).register();
        }
    }

    public void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        boolean client = event.includeClient();
        boolean server = event.includeServer();

        if (client) {
            generator.addProvider(new OBlockStates(generator, helper));
            generator.addProvider(new OItemModels(generator, helper));
            generator.addProvider(new OLang(generator));
            generator.addProvider(new OSoundDefinitions(generator, helper));
        }
        if (server) {
            generator.addProvider(new ORecipes(generator));
            generator.addProvider(new OLootTables(generator));
            OBlockTags blockTags = new OBlockTags(generator, helper);
            generator.addProvider(blockTags);
            generator.addProvider(new OItemTags(generator, blockTags, helper));
            generator.addProvider(new OEntityTags(generator, helper));
            generator.addProvider(new OAdvancements(generator, helper));
            generator.addProvider(new OFluidTags(generator, helper));
            generator.addProvider(new OBiomeTags(generator, helper));
        }
    }
}
