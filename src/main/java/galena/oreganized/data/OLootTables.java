package galena.oreganized.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import galena.oreganized.data.provider.OBlockLootProvider;
import galena.oreganized.index.OBlocks;
import galena.oreganized.index.OEntityTypes;
import galena.oreganized.index.OItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.EntityLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class OLootTables extends LootTableProvider {

    public OLootTables(DataGenerator gen) {
        super(gen);
    }

    public String getName() {
        return "Oreganized Loot Tables";
    }

    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return ImmutableList.of(Pair.of(Blocks::new, LootContextParamSets.BLOCK), Pair.of(Entities::new, LootContextParamSets.ENTITY));
    }

    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext tracker) {

    }

    public static class Blocks extends OBlockLootProvider {

        protected void addTables() {
            //dropNothing(OBlocks.MOLTEN_LEAD);
            cauldron(OBlocks.MOLTEN_LEAD_CAULDRON);

            dropSelf(OBlocks.GLANCE);
            dropSelf(OBlocks.POLISHED_GLANCE);
            dropSelf(OBlocks.GLANCE_BRICKS);
            dropSelf(OBlocks.CHISELED_GLANCE);
            slab(OBlocks.GLANCE_SLAB);
            slab(OBlocks.GLANCE_BRICK_SLAB);
            dropSelf(OBlocks.GLANCE_STAIRS);
            dropSelf(OBlocks.GLANCE_BRICK_STAIRS);
            dropSelf(OBlocks.GLANCE_WALL);
            dropSelf(OBlocks.GLANCE_BRICK_WALL);
            dropSelf(OBlocks.SPOTTED_GLANCE);
            dropSelf(OBlocks.WAXED_SPOTTED_GLANCE);
            ore(OBlocks.SILVER_ORE, OItems.RAW_SILVER);
            ore(OBlocks.DEEPSLATE_SILVER_ORE, OItems.RAW_SILVER);
            ore(OBlocks.LEAD_ORE, OItems.RAW_LEAD);
            ore(OBlocks.DEEPSLATE_LEAD_ORE, OItems.RAW_LEAD);
            dropSelf(OBlocks.RAW_SILVER_BLOCK);
            dropSelf(OBlocks.RAW_LEAD_BLOCK);
            dropSelf(OBlocks.SILVER_BLOCK);
            dropSelf(OBlocks.LEAD_BLOCK);
            dropSelf(OBlocks.ELECTRUM_BLOCK);
            dropSelf(OBlocks.EXPOSER);
            dropSelf(OBlocks.SHRAPNEL_BOMB);

            for (int i = 0; OBlocks.CRYSTAL_GLASS.size() > i; i++) {
                dropAsSilk(OBlocks.CRYSTAL_GLASS.get(i));
                dropAsSilk(OBlocks.CRYSTAL_GLASS_PANES.get(i));
            }

            for (Supplier<? extends Block> blocks : OBlocks.WAXED_CONRETE_POWDER) {
                dropSelf(blocks);
            }
        }

        protected Iterable<Block> getKnownBlocks() {
            return OBlocks.BLOCKS.getEntries().stream().map(Supplier::get).collect(Collectors.toList());
        }
    }

    public static class Entities extends EntityLoot {

        protected void addTables() {

        }

        protected Iterable<EntityType<?>> getKnownEntities() {
            return OEntityTypes.ENTITIES.getEntries().stream().map(Supplier::get).collect(Collectors.toList());
        }
    }
}
