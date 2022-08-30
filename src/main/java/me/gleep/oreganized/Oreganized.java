package me.gleep.oreganized;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.redlimerl.detailab.DetailArmorBar;
import com.redlimerl.detailab.api.DetailArmorBarAPI;
import com.redlimerl.detailab.api.render.ArmorBarRenderManager;
import com.redlimerl.detailab.api.render.TextureOffset;
import me.gleep.oreganized.blocks.client.ShrapnelBombRenderer;
import me.gleep.oreganized.capabilities.CapabilityHandler;
import me.gleep.oreganized.client.OreganizedClient;
import me.gleep.oreganized.compat.FarmersDelightCompat;
import me.gleep.oreganized.data.OBlockTags;
import me.gleep.oreganized.data.OItemTags;
import me.gleep.oreganized.data.OSoundDefinitions;
import me.gleep.oreganized.entities.PrimedShrapnelBomb;
import me.gleep.oreganized.events.ModEvents;
import me.gleep.oreganized.registry.*;
import me.gleep.oreganized.util.RegistryHandler;
import me.gleep.oreganized.util.SimpleNetwork;
import me.gleep.oreganized.world.gen.OreganizedFeatures;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

@Mod(Oreganized.MOD_ID)
public class Oreganized {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "oreganized";

    public static final List<Block> ENGRAVEABLES = ForgeRegistries.BLOCKS.tags().getTag(OTags.Blocks.ENGRAVABLE).stream().toList();

    public Oreganized() {

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);
        bus.addListener(this::doClientStuff);
        bus.addListener(this::registerRenderers);
        bus.addListener(this::gatherData);

        MinecraftForge.EVENT_BUS.addListener(OreganizedFeatures::onBiomeLoadingEvent);

        if (ModList.get().isLoaded("farmersdelight")) {
            FarmersDelightCompat.init();
        }

        DeferredRegister<?>[] registers = {
                OBlockEntities.BLOCK_ENTITIES,
                OBlocks.BLOCKS,
                OEffects.EFFECTS,
                OEntityTypes.ENTITIES,
                //OFluids.FLUIDS,
                OItems.ITEMS,
                OParticleTypes.PARTICLES,
                OPotions.POTIONS,
                OSoundEvents.SOUNDS,
                OStructures.STRUCTURES,
        };

        for (DeferredRegister<?> register : registers) {
            register.register(bus);
        }

        // TODO: Divide into individual files in registry package
        RegistryHandler.init();

        MinecraftForge.EVENT_BUS.register( new CapabilityHandler() );
        //MinecraftForge.EVENT_BUS.register( new StunnedOverlayRenderer() );
    }

    private void setup(final FMLCommonSetupEvent event) {

        event.enqueueWork(() -> {
            OreganizedFeatures.registerOreFeatures();
            SimpleNetwork.register();

            PotionBrewing.addMix(Potions.WATER, OItems.LEAD_INGOT.get(), OPotions.STUNNING.get());
            PotionBrewing.addMix(OPotions.STUNNING.get(), Items.REDSTONE, OPotions.LONG_STUNNING.get());
            PotionBrewing.addMix(OPotions.STUNNING.get(), Items.GLOWSTONE_DUST, OPotions.STRONG_STUNNING.get());

            DispenserBlock.registerBehavior( OItems.MOLTEN_LEAD_BUCKET.get(), new DefaultDispenseItemBehavior() {
                private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

                /**
                 * Dispense the specified stack, play the dispense sound and spawn particles.
                 */
                public ItemStack execute(BlockSource p_123561_, ItemStack p_123562_) {
                    DispensibleContainerItem dispensiblecontaineritem = (DispensibleContainerItem)p_123562_.getItem();
                    BlockPos blockpos = p_123561_.getPos().relative(p_123561_.getBlockState().getValue(DispenserBlock.FACING));
                    Level level = p_123561_.getLevel();
                    if (dispensiblecontaineritem.emptyContents((Player)null, level, blockpos, (BlockHitResult)null)) {
                        dispensiblecontaineritem.checkExtraContent((Player)null, level, p_123562_, blockpos);
                        return new ItemStack( Items.BUCKET);
                    } else {
                        return this.defaultDispenseItemBehavior.dispense(p_123561_, p_123562_);
                    }
                }
            } );

            DispenserBlock.registerBehavior(OBlocks.SHRAPNEL_BOMB.get(), new DefaultDispenseItemBehavior() {

                private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

                public ItemStack execute(BlockSource pSource, ItemStack pStack) {
                    Direction direction = pSource.getBlockState().getValue(DispenserBlock.FACING);
                    Level level = pSource.getLevel();
                    double d0 = pSource.x() + (double)(direction.getStepX());
                    double d1 = pSource.y() + (double)(direction.getStepY());
                    double d2 = pSource.z() + (double)(direction.getStepZ());
                    BlockPos blockpos = pSource.getPos().relative(direction);

                    PrimedShrapnelBomb primedbomb = new PrimedShrapnelBomb(level, d0, d1, d2, null);
                    primedbomb.setDeltaMovement(0.0D, 0.0D, 0.0D);
                    level.addFreshEntity(primedbomb);
                    pStack.shrink(1);
                    return pStack;
                }

                /**
                 * Play the dispense sound from the specified block.
                 */
                protected void playSound(BlockSource pSource) {
                    pSource.getLevel().levelEvent(1000, pSource.getPos(), 0);
                }
            });
        });

        ModEvents.ENGRAVED_COPPER_BLOCKS = ImmutableList.of(RegistryHandler.ENGRAVED_CUT_COPPER.get(),
                RegistryHandler.ENGRAVED_EXPOSED_CUT_COPPER.get(), RegistryHandler.ENGRAVED_WEATHERED_CUT_COPPER.get(),
                RegistryHandler.ENGRAVED_OXIDIZED_CUT_COPPER.get());

        ModEvents.ENGRAVED_WAXED_COPPER_BLOCKS = ImmutableBiMap.of(
                RegistryHandler.ENGRAVED_WAXED_CUT_COPPER.get(), RegistryHandler.ENGRAVED_CUT_COPPER.get(),
                RegistryHandler.ENGRAVED_WAXED_EXPOSED_CUT_COPPER.get(), RegistryHandler.ENGRAVED_EXPOSED_CUT_COPPER.get(),
                RegistryHandler.ENGRAVED_WAXED_WEATHERED_CUT_COPPER.get(),RegistryHandler.ENGRAVED_WEATHERED_CUT_COPPER.get(),
                RegistryHandler.ENGRAVED_WAXED_OXIDIZED_CUT_COPPER.get(), RegistryHandler.ENGRAVED_OXIDIZED_CUT_COPPER.get());

        ModEvents.WAXED_BLOCKS= ImmutableBiMap.ofEntries(
                Map.entry(OBlocks.WAXED_WHITE_CONCRETE_POWDER.get(), Blocks.WHITE_CONCRETE_POWDER),
                Map.entry(OBlocks.WAXED_ORANGE_CONCRETE_POWDER.get(), Blocks.ORANGE_CONCRETE_POWDER),
                Map.entry(OBlocks.WAXED_MAGENTA_CONCRETE_POWDER.get(), Blocks.MAGENTA_CONCRETE_POWDER),
                Map.entry(OBlocks.WAXED_LIGHT_BLUE_CONCRETE_POWDER.get(), Blocks.LIGHT_BLUE_CONCRETE_POWDER),
                Map.entry(OBlocks.WAXED_YELLOW_CONCRETE_POWDER.get(), Blocks.YELLOW_CONCRETE_POWDER),
                Map.entry(OBlocks.WAXED_LIME_CONCRETE_POWDER.get(), Blocks.LIME_CONCRETE_POWDER),
                Map.entry(OBlocks.WAXED_PINK_CONCRETE_POWDER.get(), Blocks.PINK_CONCRETE_POWDER),
                Map.entry(OBlocks.WAXED_GRAY_CONCRETE_POWDER.get(), Blocks.GRAY_CONCRETE_POWDER),
                Map.entry(OBlocks.WAXED_LIGHT_GRAY_CONCRETE_POWDER.get(), Blocks.LIGHT_GRAY_CONCRETE_POWDER),
                Map.entry(OBlocks.WAXED_CYAN_CONCRETE_POWDER.get(), Blocks.CYAN_CONCRETE_POWDER),
                Map.entry(OBlocks.WAXED_PURPLE_CONCRETE_POWDER.get(), Blocks.PURPLE_CONCRETE_POWDER),
                Map.entry(OBlocks.WAXED_BLUE_CONCRETE_POWDER.get(), Blocks.BLUE_CONCRETE_POWDER),
                Map.entry(OBlocks.WAXED_BROWN_CONCRETE_POWDER.get(), Blocks.BROWN_CONCRETE_POWDER),
                Map.entry(OBlocks.WAXED_GREEN_CONCRETE_POWDER.get(), Blocks.GREEN_CONCRETE_POWDER),
                Map.entry(OBlocks.WAXED_RED_CONCRETE_POWDER.get(), Blocks.RED_CONCRETE_POWDER),
                Map.entry(OBlocks.WAXED_BLACK_CONCRETE_POWDER.get(), Blocks.BLACK_CONCRETE_POWDER),
                Map.entry(OBlocks.WAXED_SPOTTED_GLANCE.get(), OBlocks.SPOTTED_GLANCE.get())
        );
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        OreganizedClient.registerBlockRenderers();

        if (ModList.get().isLoaded("detailab")) {
            ResourceLocation texture = new ResourceLocation(MOD_ID, "textures/gui/armor_bar.png");
            DetailArmorBarAPI.customArmorBarBuilder().armor((ArmorItem) OItems.ELECTRUM_CHESTPLATE.get(), (ArmorItem) OItems.ELECTRUM_HELMET.get(), (ArmorItem) OItems.ELECTRUM_LEGGINGS.get(), (ArmorItem) OItems.ELECTRUM_BOOTS.get())
                    .render((ItemStack itemStack) ->
                            new ArmorBarRenderManager(texture, 18, 18,
                                    new TextureOffset(9, 9), new TextureOffset(0, 9), new TextureOffset(9, 0), new TextureOffset(0, 0))
                    ).register();
        }

        event.enqueueWork(() -> ItemProperties.register(OItems.SILVER_INGOT.get(),
                new ResourceLocation(Oreganized.MOD_ID + ":shine"),
                (ItemStack p_174676_, @Nullable ClientLevel p_174677_, @Nullable LivingEntity p_174678_, int p_174679_) ->
                        p_174676_.getTag() != null ? (p_174676_.getTag().getBoolean("Shine") ? 1 : 0) : 0)
        );
        event.enqueueWork(() -> ItemProperties.register(OItems.SILVER_MIRROR.get(),
                new ResourceLocation(Oreganized.MOD_ID + ":dist"),
                (ItemStack p_174676_, @Nullable ClientLevel p_174677_, @Nullable LivingEntity p_174678_, int p_174679_) ->
                        p_174676_.getTag() != null ? p_174676_.getTag().getInt("Dist") : 8)
        );
    }

    private void registerRenderers( EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer( OEntityTypes.SHRAPNEL_BOMB.get(), ShrapnelBombRenderer::new);
    }

    public void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();

        if(event.includeClient()) {
            // TODO
            //generator.addProvider(new OBlockStates(generator, helper));
            //generator.addProvider(new OItemModels(generator, helper));
            //generator.addProvider(new OLang(generator));
            generator.addProvider(new OSoundDefinitions(generator, helper));
        }
        if(event.includeServer()) {
            // TODO
            //generator.addProvider(new ORecipes(generator));
            //generator.addProvider(new OLootTables(generator));
            OBlockTags blockTags = new OBlockTags(generator, helper);
            generator.addProvider(blockTags);
            generator.addProvider(new OItemTags(generator, blockTags, helper));
            //generator.addProvider(new OEntityTags(generator, helper));
            //generator.addProvider(new OAdvancements(generator, helper));
            //generator.addProvider(new OFluidTags(generator, helper));
        }
    }
}
