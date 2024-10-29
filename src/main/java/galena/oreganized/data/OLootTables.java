package galena.oreganized.data;

import galena.oreganized.Oreganized;
import galena.oreganized.content.block.IMeltableBlock;
import galena.oreganized.data.provider.OBlockLootProvider;
import galena.oreganized.index.OBlocks;
import galena.oreganized.index.OEntityTypes;
import galena.oreganized.index.OItems;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OLootTables extends LootTableProvider {

    public OLootTables(PackOutput output) {
        super(output, Set.of(), List.of(
                new SubProviderEntry(BlockLoot::new, LootContextParamSets.BLOCK),
                new SubProviderEntry(EntityLoot::new, LootContextParamSets.ENTITY)
        ));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext tracker) {
    }

    public static class BlockLoot extends OBlockLootProvider {

        protected void generate() {
            //dropNothing(OBlocks.MOLTEN_LEAD);
            cauldron(OBlocks.MOLTEN_LEAD_CAULDRON);

            dropSelf(OBlocks.GLANCE);
            dropSelf(OBlocks.POLISHED_GLANCE);
            dropSelf(OBlocks.GLANCE_BRICKS);
            dropSelf(OBlocks.CHISELED_GLANCE);
            slab(OBlocks.GLANCE_SLAB);
            slab(OBlocks.POLISHED_GLANCE_SLAB);
            slab(OBlocks.GLANCE_BRICK_SLAB);
            dropSelf(OBlocks.GLANCE_STAIRS);
            dropSelf(OBlocks.POLISHED_GLANCE_STAIRS);
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
            dropSelf(OBlocks.LEAD_BRICKS);
            dropSelf(OBlocks.LEAD_PILLAR);
            dropSelf(OBlocks.LEAD_BULB);
            dropSelf(OBlocks.CUT_LEAD);
            dropSelf(OBlocks.ELECTRUM_BLOCK);
            dropSelf(OBlocks.GARGOYLE);
            dropSelf(OBlocks.SHRAPNEL_BOMB);
            dropSelf(OBlocks.LEAD_BOLT_CRATE);

            grooved(OBlocks.GROOVED_ICE, Blocks.ICE);
            grooved(OBlocks.GROOVED_BLUE_ICE, Blocks.BLUE_ICE);
            grooved(OBlocks.GROOVED_PACKED_ICE, Blocks.PACKED_ICE);

            add(OBlocks.LEAD_DOOR.get(), LootTable.lootTable()
                    .withPool(applyExplosionCondition(OBlocks.LEAD_DOOR.get(), LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0F))
                            .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(OBlocks.LEAD_DOOR.get())
                                    .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoorBlock.HALF, DoubleBlockHalf.LOWER)))
                            .add(LootItem.lootTableItem(OBlocks.LEAD_DOOR.get())))));
            dropSelf(OBlocks.LEAD_TRAPDOOR);

            add(OBlocks.LEAD_BARS.get(), LootTable.lootTable()
                    .withPool(applyExplosionCondition(OBlocks.LEAD_BARS.get(), LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0F))
                            .add(AlternativesEntry.alternatives(
                                    LootItem.lootTableItem(OBlocks.LEAD_BARS.get()).when(HAS_SILK_TOUCH),
                                    LootItem.lootTableItem(OItems.LEAD_NUGGET.get())
                                            .apply(SetItemCountFunction.setCount((UniformGenerator.between(2F, 3F))))
                                            .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(OBlocks.LEAD_BARS.get())
                                                    .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(IMeltableBlock.GOOPYNESS_3, 2))
                                            ),
                                    LootItem.lootTableItem(OBlocks.LEAD_BARS.get())
                            )))));

            OBlocks.CRYSTAL_GLASS.forEach((c, b) -> dropAsSilk(b));
            OBlocks.CRYSTAL_GLASS_PANES.forEach((c, b) -> dropAsSilk(b));
            OBlocks.WAXED_CONCRETE_POWDER.forEach((c, b) -> dropSelf(b));
        }

        private void grooved(Supplier<Block> block, Block other) {
            var hasScribe = MatchTool.toolMatches(ItemPredicate.Builder.item().of(OItems.SCRIBE.get()));
            add(block.get(), LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0F))
                            .add(AlternativesEntry.alternatives(
                                    LootItem.lootTableItem(block.get().asItem())
                                            .when(HAS_SILK_TOUCH),
                                    LootItem.lootTableItem(other.asItem())
                                            .when(hasScribe)
                            ))
                    ));
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return Oreganized.REGISTRY_HELPER.getBlockSubHelper().getDeferredRegister().getEntries().stream().map(Supplier::get).collect(Collectors.toList());
        }
    }

    public static class EntityLoot extends EntityLootSubProvider {

        public EntityLoot() {
            super(FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        public void generate() {

        }

        @Override
        protected Stream<EntityType<?>> getKnownEntityTypes() {
            return OEntityTypes.ENTITIES.getEntries().stream().map(Supplier::get);
        }
    }
}
