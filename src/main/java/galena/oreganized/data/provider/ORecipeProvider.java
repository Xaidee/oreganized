package galena.oreganized.data.provider;

import com.simibubi.create.foundation.data.recipe.CreateRecipeProvider;
import galena.oreganized.Oreganized;
import galena.oreganized.index.OTags;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalAdvancement;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ORecipeProvider extends RecipeProvider {

    public ORecipeProvider(DataGenerator gen) {
        super(gen);
    }

    public ShapedRecipeBuilder makeSlab(Supplier<? extends Block> slabOut, Supplier<? extends Block> blockIn) {
        return ShapedRecipeBuilder.shaped(slabOut.get(), 6)
                .pattern("AAA")
                .define('A', blockIn.get())
                .unlockedBy("has_" + ForgeRegistries.BLOCKS.getKey(blockIn.get()).getPath(), has(blockIn.get()));
    }

    public ShapedRecipeBuilder makeStairs(Supplier<? extends Block> stairsOut, Supplier<? extends Block> blockIn) {
        return ShapedRecipeBuilder.shaped(stairsOut.get(), 4)
                .pattern("A  ")
                .pattern("AA ")
                .pattern("AAA")
                .define('A', blockIn.get())
                .unlockedBy("has_" + ForgeRegistries.BLOCKS.getKey(blockIn.get()).getPath(), has(blockIn.get()));
    }

    public ShapedRecipeBuilder makeWall(Supplier<? extends Block> wallOut, Supplier<? extends Block> blockIn) {
        return ShapedRecipeBuilder.shaped(wallOut.get(), 6)
                .pattern("AAA")
                .pattern("AAA")
                .define('A', blockIn.get())
                .unlockedBy("has_" + ForgeRegistries.BLOCKS.getKey(blockIn.get()).getPath(), has(blockIn.get()));
    }

    public ShapedRecipeBuilder makeBars(Supplier<? extends Block> barsOut, Supplier<? extends Block> blockIn) {
        return ShapedRecipeBuilder.shaped(barsOut.get(), 16)
                .pattern("AAA")
                .pattern("AAA")
                .define('A', blockIn.get())
                .unlockedBy("has_" + ForgeRegistries.BLOCKS.getKey(blockIn.get()).getPath(), has(blockIn.get()));
    }

    public ShapedRecipeBuilder quadTransform(Supplier<? extends Block> blockOut, Supplier<? extends Block> blockIn) {
        return ShapedRecipeBuilder.shaped(blockOut.get(), 4)
                .pattern("AA")
                .pattern("AA")
                .define('A', blockIn.get())
                .unlockedBy("has_" + ForgeRegistries.BLOCKS.getKey(blockIn.get()).getPath(), has(blockIn.get()));
    }

    public ShapedRecipeBuilder makeChiseled(Supplier<? extends Block> blockOut, Supplier<? extends SlabBlock> slabIn) {
        return ShapedRecipeBuilder.shaped(blockOut.get())
                .pattern("A")
                .pattern("A")
                .define('A', slabIn.get())
                .unlockedBy("has_" + ForgeRegistries.BLOCKS.getKey(slabIn.get()).getPath(), has(slabIn.get()));
    }

    public ShapedRecipeBuilder compact(Item itemOut, Item itemIn) {
        return ShapedRecipeBuilder.shaped(itemOut)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', itemIn)
                .unlockedBy("has_" + ForgeRegistries.ITEMS.getKey(itemIn).getPath(), has(itemIn));
    }

    public ShapelessRecipeBuilder unCompact(Item itemOut, Item itemIn) {
        return ShapelessRecipeBuilder.shapeless(itemOut, 9)
                .requires(itemIn)
                .unlockedBy("has_" + ForgeRegistries.ITEMS.getKey(itemIn).getPath(), has(itemIn));
    }

    public ShapedRecipeBuilder crystalGlass(Supplier<? extends Block> blockOut, Block blockIn) {
        return ShapedRecipeBuilder.shaped(blockOut.get())
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', blockIn)
                .define('B', OTags.Items.INGOTS_LEAD)
                .unlockedBy("has_lead_ingot", has(OTags.Items.INGOTS_LEAD))
                .unlockedBy("has_any_glass", has(Tags.Items.GLASS));
    }

    public void ore(ItemLike result, List<ItemLike> ingredients, float xp, String group, Consumer<FinishedRecipe> consumer) {
        oreSmeltingRecipe(result, ingredients, xp, group, consumer);
        oreBlastingRecipe(result, ingredients, xp, group, consumer);
    }

    public SimpleCookingRecipeBuilder smeltingRecipe(ItemLike result, ItemLike ingredient, float exp) {
        return smeltingRecipe(result, ingredient, exp, 1);
    }

    private void oreSmeltingRecipe(ItemLike result, List<ItemLike> ingredients, float xp, String group, Consumer<FinishedRecipe> consumer) {
        for (ItemLike ingredient : ingredients) {
            smeltingRecipe(result, ingredient, xp, 1).group(group).save(consumer, new ResourceLocation(Oreganized.MOD_ID, "smelt_" + ForgeRegistries.ITEMS.getKey(ingredient.asItem()).getPath()));
        }
    }

    public SimpleCookingRecipeBuilder smeltingRecipe(ItemLike result, ItemLike ingredient, float exp, int count) {
        return SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemStack(ingredient, count)), result, exp, 200)
                .unlockedBy("has_" + ForgeRegistries.ITEMS.getKey(ingredient.asItem()).getPath(), has(ingredient));
    }

    public SimpleCookingRecipeBuilder smeltingRecipeTag(ItemLike result, TagKey<Item> ingredient, float exp) {
        return smeltingRecipeTag(result, ingredient, exp, 1);
    }

    public SimpleCookingRecipeBuilder smeltingRecipeTag(ItemLike result, TagKey<Item> ingredient, float exp, int count) {
        return SimpleCookingRecipeBuilder.smelting(Ingredient.of(ingredient), result, exp, 200)
                .unlockedBy("has_" + ingredient, has(ingredient));
    }

    public SimpleCookingRecipeBuilder blastingRecipe(ItemLike result, ItemLike ingredient, float exp) {
        return blastingRecipe(result, ingredient, exp, 1);
    }

    private void oreBlastingRecipe(ItemLike result, List<ItemLike> ingredients, float xp, String group, Consumer<FinishedRecipe> consumer) {
        for (ItemLike ingredient : ingredients) {
            blastingRecipe(result, ingredient, xp, 1).group(group).save(consumer, new ResourceLocation(Oreganized.MOD_ID, "blast_" + ForgeRegistries.ITEMS.getKey(ingredient.asItem()).getPath()));
        }
    }

    public SimpleCookingRecipeBuilder blastingRecipe(ItemLike result, ItemLike ingredient, float exp, int count) {
        return SimpleCookingRecipeBuilder.blasting(Ingredient.of(new ItemStack(ingredient, count)), result, exp, 100)
                .unlockedBy("has_" + ForgeRegistries.ITEMS.getKey(ingredient.asItem()).getPath(), has(ingredient));
    }

    public SimpleCookingRecipeBuilder blastingRecipeTag(ItemLike result, TagKey<Item> ingredient, float exp) {
        return blastingRecipeTag(result, ingredient, exp, 1);
    }

    public SimpleCookingRecipeBuilder blastingRecipeTag(ItemLike result, TagKey<Item> ingredient, float exp, int count) {
        return SimpleCookingRecipeBuilder.blasting(Ingredient.of(ingredient), result, exp, 100)
                .unlockedBy("has_" + ingredient, has(ingredient));
    }

    public UpgradeRecipeBuilder smithingRecipe(Supplier<Item> input, Supplier<Item> upgradeItem, Supplier<Item> result) {
        return UpgradeRecipeBuilder.smithing(Ingredient.of(input.get()), Ingredient.of(upgradeItem.get()), result.get())
                .unlocks("has_" + ForgeRegistries.ITEMS.getKey(upgradeItem.get()), has(upgradeItem.get()));
    }

    public UpgradeRecipeBuilder smithingElectrum(Item input, Supplier<Item> result) {
        TagKey<Item> electrum = OTags.Items.INGOTS_ELECTRUM;
        return UpgradeRecipeBuilder.smithing(Ingredient.of(input), Ingredient.of(electrum), result.get())
                .unlocks("has_electrum_ingot", has(electrum));
    }

    public SingleItemRecipeBuilder stonecutting(Supplier<Block> input, ItemLike result) {
        return SingleItemRecipeBuilder.stonecutting(Ingredient.of(input.get()), result)
                .unlockedBy("has_" + ForgeRegistries.BLOCKS.getKey(input.get()).getPath(), has(input.get()));
    }

    public SingleItemRecipeBuilder stonecutting(Supplier<Block> input, ItemLike result, int resultAmount) {
        return SingleItemRecipeBuilder.stonecutting(Ingredient.of(input.get()), result, resultAmount)
                .unlockedBy("has_" + ForgeRegistries.BLOCKS.getKey(input.get()).getPath(), has(input.get()));
    }

    public ShapelessRecipeBuilder makeWaxed(Supplier<Block> blockOut, Block blockIn) {
        return ShapelessRecipeBuilder.shapeless(blockOut.get())
                .requires(blockIn)
                .requires(Items.HONEYCOMB)
                .unlockedBy("has_" + ForgeRegistries.BLOCKS.getKey(blockIn), has(blockIn))
                .unlockedBy("has_honeycomb", has(Items.HONEYCOMB));
    }

    public ShapelessRecipeBuilder makeWaxed(Supplier<Block> blockOut, Supplier<Block> blockIn) {
        return makeWaxed(blockOut, blockIn.get());
    }

    public void makeSlabStonecutting(Supplier<? extends Block> blockOut, Supplier<Block> blockIn, Consumer<FinishedRecipe> consumer) {
        makeSlab(blockOut, blockIn).save(consumer);
        stonecutting(blockIn, blockOut.get(), 2).save(consumer, "oreganized/stonecutting/" + ForgeRegistries.ITEMS.getKey(blockOut.get().asItem()).getPath());
    }

    public void makeStairsStonecutting(Supplier<? extends Block> blockOut, Supplier<Block> blockIn, Consumer<FinishedRecipe> consumer) {
        makeStairs(blockOut, blockIn).save(consumer);
        stonecutting(blockIn, blockOut.get()).save(consumer, "oreganized/stonecutting/" + ForgeRegistries.ITEMS.getKey(blockOut.get().asItem()).getPath());
    }

    public void makeWallStonecutting(Supplier<? extends Block> blockOut, Supplier<Block> blockIn, Consumer<FinishedRecipe> consumer) {
        makeWall(blockOut, blockIn).save(consumer);
        stonecutting(blockIn, blockOut.get()).save(consumer, "oreganized/stonecutting/" + ForgeRegistries.ITEMS.getKey(blockOut.get().asItem()).getPath());
    }

    public void makeChiseledStonecutting(Supplier<? extends Block> blockOut, Supplier<Block> blockIn, Supplier<SlabBlock> slabIn, Consumer<FinishedRecipe> consumer) {
        makeChiseled(blockOut, slabIn).save(consumer);
        stonecutting(blockIn, blockOut.get()).save(consumer, "oreganized/stonecutting/" + ForgeRegistries.ITEMS.getKey(blockOut.get().asItem()).getPath());
    }
}
