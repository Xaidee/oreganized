package galena.oreganized.data;

import com.google.common.collect.ImmutableList;
import galena.oreganized.data.provider.ORecipeProvider;
import galena.oreganized.index.OBlocks;
import galena.oreganized.index.OItems;
import galena.oreganized.index.OTags;
import galena.oreganized.integration.farmersdelight.FDCompatRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ORecipes extends ORecipeProvider {

    protected static final ImmutableList<ItemLike> LEAD_SMELTABLES = ImmutableList.of(OBlocks.LEAD_ORE.get(), OBlocks.DEEPSLATE_LEAD_ORE.get(), OItems.RAW_LEAD.get());
    protected static final ImmutableList<ItemLike> SILVER_SMELTABLES = ImmutableList.of(OBlocks.SILVER_ORE.get(), OBlocks.DEEPSLATE_SILVER_ORE.get(), OItems.RAW_SILVER.get());

    public ORecipes(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        oreSmelting(consumer, LEAD_SMELTABLES, OItems.LEAD_INGOT.get(), 0.7F, 200, "oreganized/lead_ingot");
        oreBlasting(consumer, LEAD_SMELTABLES, OItems.LEAD_INGOT.get(), 0.7F, 100, "oreganized/lead_ingot");
        oreSmelting(consumer, SILVER_SMELTABLES, OItems.SILVER_INGOT.get(), 1.0F, 200, "oreganized/silver_ingot");
        oreBlasting(consumer, SILVER_SMELTABLES, OItems.SILVER_INGOT.get(), 1.0F, 100, "oreganized/silver_ingot");

        smeltingRecipe(OItems.LEAD_NUGGET.get(), OItems.BUSH_HAMMER.get(), 0.1F).save(consumer, "oreganized/lead_nugget_from_smelting");
        blastingRecipe(OItems.LEAD_NUGGET.get(), OItems.BUSH_HAMMER.get(), 0.1F).save(consumer, "oreganized/lead_nugget_from_blasting");

        quadTransform(OBlocks.POLISHED_GLANCE, OBlocks.GLANCE).save(consumer);
        quadTransform(OBlocks.GLANCE_BRICKS, OBlocks.POLISHED_GLANCE).save(consumer);

        compact(OBlocks.SILVER_BLOCK.get().asItem(), OItems.SILVER_INGOT.get()).save(consumer);
        compact(OBlocks.LEAD_BLOCK.get().asItem(), OItems.LEAD_INGOT.get()).save(consumer);
        compact(OBlocks.ELECTRUM_BLOCK.get().asItem(), OItems.ELECTRUM_INGOT.get()).save(consumer);

        compact(OBlocks.RAW_SILVER_BLOCK.get().asItem(), OItems.RAW_SILVER.get()).save(consumer);
        compact(OBlocks.RAW_LEAD_BLOCK.get().asItem(), OItems.RAW_LEAD.get()).save(consumer);

        compact(OItems.SILVER_INGOT.get(), OItems.SILVER_NUGGET.get()).save(consumer);
        compact(OItems.LEAD_INGOT.get(), OItems.LEAD_NUGGET.get()).save(consumer);
        compact(OItems.ELECTRUM_INGOT.get(), OItems.ELECTRUM_NUGGET.get()).save(consumer, "oreganized/electrum_ingot_from_nuggets");
        compact(Items.NETHERITE_INGOT, OItems.NETHERITE_NUGGET.get()).save(consumer, "oreganized/netherite_ingot_from_nuggets");

        unCompact(OItems.SILVER_INGOT.get(), OBlocks.SILVER_BLOCK.get().asItem()).save(consumer, "oreganized/silver_ingot_from_block");
        unCompact(OItems.LEAD_INGOT.get(), OBlocks.LEAD_BLOCK.get().asItem()).save(consumer, "oreganized/lead_ingot_from_block");
        unCompact(OItems.ELECTRUM_INGOT.get(), OBlocks.ELECTRUM_BLOCK.get().asItem()).save(consumer, "oreganized/electrum_ingot_from_block");

        unCompact(OItems.RAW_SILVER.get(), OBlocks.RAW_SILVER_BLOCK.get().asItem()).save(consumer, "oreganized/raw_silver_from_block");
        unCompact(OItems.RAW_LEAD.get(), OBlocks.RAW_LEAD_BLOCK.get().asItem()).save(consumer, "oreganized/raw_lead_from_block");

        unCompact(OItems.SILVER_NUGGET.get(), OItems.SILVER_INGOT.get()).save(consumer);
        unCompact(OItems.LEAD_NUGGET.get(), OItems.LEAD_INGOT.get()).save(consumer);
        unCompact(OItems.ELECTRUM_NUGGET.get(), OItems.ELECTRUM_INGOT.get()).save(consumer);
        unCompact(OItems.NETHERITE_NUGGET.get(), Items.NETHERITE_INGOT).save(consumer);

        makeSlabStonecutting(OBlocks.GLANCE_SLAB, OBlocks.GLANCE, consumer);
        makeSlabStonecutting(OBlocks.GLANCE_BRICK_SLAB, OBlocks.GLANCE_BRICKS, consumer);

        makeStairsStonecutting(OBlocks.GLANCE_STAIRS, OBlocks.GLANCE, consumer);
        makeStairsStonecutting(OBlocks.GLANCE_BRICK_STAIRS, OBlocks.GLANCE_BRICKS, consumer);

        makeWallStonecutting(OBlocks.GLANCE_WALL, OBlocks.GLANCE, consumer);
        makeWallStonecutting(OBlocks.GLANCE_BRICK_WALL, OBlocks.GLANCE_BRICKS, consumer);

        makeChiseledStonecutting(OBlocks.CHISELED_GLANCE, OBlocks.GLANCE, OBlocks.GLANCE_SLAB, consumer);

        stonecutting(OBlocks.GLANCE, OBlocks.POLISHED_GLANCE.get()).save(consumer, "oreganized/stonecutting/polished_glance");
        stonecutting(OBlocks.GLANCE, OBlocks.GLANCE_BRICKS.get()).save(consumer, "oreganized/stonecutting/glance_bricks_from_glance");
        stonecutting(OBlocks.GLANCE, OBlocks.GLANCE_BRICK_STAIRS.get()).save(consumer, "oreganized/stonecutting/glance_brick_stairs_from_glance");
        stonecutting(OBlocks.GLANCE, OBlocks.GLANCE_BRICK_SLAB.get(), 2).save(consumer, "stonecutting/glance_brick_slab_from_glance");
        stonecutting(OBlocks.GLANCE, OBlocks.GLANCE_BRICK_WALL.get()).save(consumer, "oreganized/stonecutting/glance_brick_wall_from_glance");

        stonecutting(OBlocks.POLISHED_GLANCE, OBlocks.GLANCE_BRICKS.get()).save(consumer, "oreganized/stonecutting/glance_bricks_from_polished");
        stonecutting(OBlocks.POLISHED_GLANCE, OBlocks.GLANCE_BRICK_STAIRS.get()).save(consumer, "oreganized/stonecutting/glance_brick_stairs_from_polished");
        stonecutting(OBlocks.POLISHED_GLANCE, OBlocks.GLANCE_BRICK_SLAB.get(), 2).save(consumer, "oreganized/stonecutting/glance_brick_slab_from_polished");
        stonecutting(OBlocks.POLISHED_GLANCE, OBlocks.GLANCE_BRICK_WALL.get()).save(consumer, "oreganized/stonecutting/glance_brick_wall_from_polished");

        makeWaxed(OBlocks.WAXED_SPOTTED_GLANCE, OBlocks.SPOTTED_GLANCE).save(consumer);

        smithingElectrum(Items.DIAMOND_SWORD, OItems.ELECTRUM_SWORD).save(consumer, "oreganized/electrum_sword");
        smithingElectrum(Items.DIAMOND_SHOVEL, OItems.ELECTRUM_SHOVEL).save(consumer, "oreganized/electrum_shovel");
        smithingElectrum(Items.DIAMOND_PICKAXE, OItems.ELECTRUM_PICKAXE).save(consumer, "oreganized/electrum_pickaxe");
        smithingElectrum(Items.DIAMOND_AXE, OItems.ELECTRUM_AXE).save(consumer, "oreganized/electrum_axe");
        smithingElectrum(Items.DIAMOND_HOE, OItems.ELECTRUM_HOE).save(consumer, "oreganized/electrum_hoe");
        smithingElectrum(Items.DIAMOND_HELMET, OItems.ELECTRUM_HELMET).save(consumer, "oreganized/electrum_helmet");
        smithingElectrum(Items.DIAMOND_CHESTPLATE, OItems.ELECTRUM_CHESTPLATE).save(consumer, "oreganized/electrum_chestplate");
        smithingElectrum(Items.DIAMOND_LEGGINGS, OItems.ELECTRUM_LEGGINGS).save(consumer, "oreganized/electrum_leggings");
        smithingElectrum(Items.DIAMOND_BOOTS, OItems.ELECTRUM_BOOTS).save(consumer, "oreganized/electrum_boots");

        crystalGlass(OBlocks.BLACK_CRYSTAL_GLASS, Blocks.BLACK_STAINED_GLASS).save(consumer);
        crystalGlass(OBlocks.BLUE_CRYSTAL_GLASS, Blocks.BLUE_STAINED_GLASS).save(consumer);
        crystalGlass(OBlocks.BROWN_CRYSTAL_GLASS, Blocks.BROWN_STAINED_GLASS).save(consumer);
        crystalGlass(OBlocks.CYAN_CRYSTAL_GLASS, Blocks.CYAN_STAINED_GLASS).save(consumer);
        crystalGlass(OBlocks.GRAY_CRYSTAL_GLASS, Blocks.GRAY_STAINED_GLASS).save(consumer);
        crystalGlass(OBlocks.GREEN_CRYSTAL_GLASS, Blocks.GREEN_STAINED_GLASS).save(consumer);
        crystalGlass(OBlocks.LIGHT_BLUE_CRYSTAL_GLASS, Blocks.LIGHT_BLUE_STAINED_GLASS).save(consumer);
        crystalGlass(OBlocks.LIGHT_GRAY_CRYSTAL_GLASS, Blocks.LIGHT_GRAY_STAINED_GLASS).save(consumer);
        crystalGlass(OBlocks.LIME_CRYSTAL_GLASS, Blocks.LIME_STAINED_GLASS).save(consumer);
        crystalGlass(OBlocks.MAGENTA_CRYSTAL_GLASS, Blocks.MAGENTA_STAINED_GLASS).save(consumer);
        crystalGlass(OBlocks.ORANGE_CRYSTAL_GLASS, Blocks.ORANGE_STAINED_GLASS).save(consumer);
        crystalGlass(OBlocks.PINK_CRYSTAL_GLASS, Blocks.PINK_STAINED_GLASS).save(consumer);
        crystalGlass(OBlocks.PURPLE_CRYSTAL_GLASS, Blocks.PURPLE_STAINED_GLASS).save(consumer);
        crystalGlass(OBlocks.RED_CRYSTAL_GLASS, Blocks.RED_STAINED_GLASS).save(consumer);
        crystalGlass(OBlocks.WHITE_CRYSTAL_GLASS, Blocks.WHITE_STAINED_GLASS).save(consumer);
        crystalGlass(OBlocks.YELLOW_CRYSTAL_GLASS, Blocks.YELLOW_STAINED_GLASS).save(consumer);

        makeWaxed(OBlocks.WAXED_WHITE_CONCRETE_POWDER, Blocks.WHITE_CONCRETE_POWDER).save(consumer);
        makeWaxed(OBlocks.WAXED_ORANGE_CONCRETE_POWDER, Blocks.ORANGE_CONCRETE_POWDER).save(consumer);
        makeWaxed(OBlocks.WAXED_MAGENTA_CONCRETE_POWDER, Blocks.MAGENTA_CONCRETE_POWDER).save(consumer);
        makeWaxed(OBlocks.WAXED_LIGHT_BLUE_CONCRETE_POWDER, Blocks.LIGHT_BLUE_CONCRETE_POWDER).save(consumer);
        makeWaxed(OBlocks.WAXED_YELLOW_CONCRETE_POWDER, Blocks.YELLOW_CONCRETE_POWDER).save(consumer);
        makeWaxed(OBlocks.WAXED_LIME_CONCRETE_POWDER, Blocks.LIME_CONCRETE_POWDER).save(consumer);
        makeWaxed(OBlocks.WAXED_PINK_CONCRETE_POWDER, Blocks.PINK_CONCRETE_POWDER).save(consumer);
        makeWaxed(OBlocks.WAXED_GRAY_CONCRETE_POWDER, Blocks.GRAY_CONCRETE_POWDER).save(consumer);
        makeWaxed(OBlocks.WAXED_LIGHT_GRAY_CONCRETE_POWDER, Blocks.LIGHT_GRAY_CONCRETE_POWDER).save(consumer);
        makeWaxed(OBlocks.WAXED_CYAN_CONCRETE_POWDER, Blocks.CYAN_CONCRETE_POWDER).save(consumer);
        makeWaxed(OBlocks.WAXED_PURPLE_CONCRETE_POWDER, Blocks.PURPLE_CONCRETE_POWDER).save(consumer);
        makeWaxed(OBlocks.WAXED_BLUE_CONCRETE_POWDER, Blocks.BLUE_CONCRETE_POWDER).save(consumer);
        makeWaxed(OBlocks.WAXED_BROWN_CONCRETE_POWDER, Blocks.BROWN_CONCRETE_POWDER).save(consumer);
        makeWaxed(OBlocks.WAXED_GREEN_CONCRETE_POWDER, Blocks.GREEN_CONCRETE_POWDER).save(consumer);
        makeWaxed(OBlocks.WAXED_RED_CONCRETE_POWDER, Blocks.RED_CONCRETE_POWDER).save(consumer);
        makeWaxed(OBlocks.WAXED_BLACK_CONCRETE_POWDER, Blocks.BLACK_CONCRETE_POWDER).save(consumer);

        for (int i = 0; OBlocks.CRYSTAL_GLASS_PANES.size() > i; i++) {
            makeBars(OBlocks.CRYSTAL_GLASS_PANES.get(i), OBlocks.CRYSTAL_GLASS.get(i)).save(consumer);
        }

        ShapedRecipeBuilder.shaped(OBlocks.GLANCE.get())
                .pattern("AB")
                .pattern("BA")
                .define('A', OTags.Items.INGOTS_LEAD)
                .define('B', Items.COBBLESTONE)
                .unlockedBy("has_lead_ingot", has(OTags.Items.INGOTS_LEAD))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(OItems.ELECTRUM_INGOT.get())
                .requires(OTags.Items.INGOTS_SILVER)
                .requires(OTags.Items.INGOTS_SILVER)
                .requires(OTags.Items.INGOTS_SILVER)
                .requires(OTags.Items.INGOTS_SILVER)
                .requires(OTags.Items.INGOTS_SILVER)
                .requires(Tags.Items.INGOTS_GOLD)
                .requires(Tags.Items.INGOTS_GOLD)
                .requires(Tags.Items.INGOTS_GOLD)
                .unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD))
                .unlockedBy("has_silver_ingot", has(OTags.Items.INGOTS_SILVER))
                .save(consumer);


        ShapedRecipeBuilder.shaped(OItems.BUSH_HAMMER.get())
                .pattern("AA")
                .pattern("B ")
                .define('A', OTags.Items.INGOTS_LEAD)
                .define('B', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_lead_ingot", has(OTags.Items.INGOTS_LEAD))
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .save(consumer);

        ShapedRecipeBuilder.shaped(OItems.SILVER_MIRROR.get())
                .pattern("ABA")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', Tags.Items.INGOTS_GOLD)
                .define('B', OTags.Items.INGOTS_SILVER)
                .unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD))
                .unlockedBy("has_silver_ingot", has(OTags.Items.INGOTS_SILVER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(OBlocks.EXPOSER.get())
                .pattern("AAA")
                .pattern("BBC")
                .pattern("AAA")
                .define('A', ItemTags.STONE_CRAFTING_MATERIALS)
                .define('B', Tags.Items.DUSTS_REDSTONE)
                .define('C', OTags.Items.INGOTS_SILVER)
                .unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
                .unlockedBy("has_silver_ingot", has(OTags.Items.INGOTS_SILVER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(OBlocks.SHRAPNEL_BOMB.get())
                .pattern("ABA")
                .pattern("BAB")
                .pattern("ABA")
                .define('A', Tags.Items.GUNPOWDER)
                .define('B', OTags.Items.NUGGETS_LEAD)
                .unlockedBy("has_gunpowder", has(Tags.Items.GUNPOWDER))
                .unlockedBy("has_lead_nugget", has(OTags.Items.NUGGETS_LEAD))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(OItems.SHRAPNEL_BOMB_MINECART.get())
                .requires(OBlocks.SHRAPNEL_BOMB.get())
                .requires(Items.MINECART)
                .unlockedBy("has_shrapnel_bomb", has(OBlocks.SHRAPNEL_BOMB.get()))
                .save(consumer);
    }
}
