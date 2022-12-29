package galena.oreganized.data;

import galena.oreganized.index.OBlocks;
import galena.oreganized.index.OEffects;
import galena.oreganized.index.OItems;
import galena.oreganized.index.OTags;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Set;
import java.util.function.Consumer;

public class OAdvancements extends AdvancementProvider {

    public OAdvancements(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, helper);
    }

    @Override
    public String getName() {
        return "Oreganized Advancements";
    }

    @Override
    protected void registerAdvancements(Consumer<Advancement> consumer, ExistingFileHelper helper) {
        new Advancements().accept(consumer);
    }

    static class Advancements implements Consumer<Consumer<Advancement>> {

        @Override
        public void accept(Consumer<Advancement> consumer) {
            Advancement mirror_mirror = Advancement.Builder.advancement()
                    .parent(getAdv("minecraft:adventure/root"))
                    .display(
                            OItems.SILVER_MIRROR.get(),
                            new TranslatableComponent("advancements.adventure.mirror_mirror.title"),
                            new TranslatableComponent("advancements.adventure.mirror_mirror.description"),
                            null,
                            FrameType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("has_silver_mirror", InventoryChangeTrigger.TriggerInstance.hasItems(OItems.SILVER_MIRROR.get()))
                    .save(consumer, "oreganized:adventure/mirror_mirror");

            Advancement eat_with_lead = Advancement.Builder.advancement()
                    .parent(getAdv("minecraft:story/upgrade_tools"))
                    .display(
                            OItems.LEAD_INGOT.get(),
                            new TranslatableComponent("advancements.story.eat_with_lead.title"),
                            new TranslatableComponent("advancements.story.eat_with_lead.description"),
                            null,
                            FrameType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("stunned", EffectsChangedTrigger.TriggerInstance.hasEffects(MobEffectsPredicate.effects().and(OEffects.STUNNING.get())))
                    .addCriterion("has_lead", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(OTags.Items.INGOTS_LEAD).build()))
                    .addCriterion("eaten", ConsumeItemTrigger.TriggerInstance.usedItem(ItemPredicate.Builder.item().of(OTags.Items.CONSUMABLE).build()))
                    .save(consumer, "oreganized:story/eat_with_lead");

            Advancement obtain_silver = Advancement.Builder.advancement()
                    .parent(getAdv("minecraft:story/iron_tools"))
                    .display(
                            OItems.SILVER_INGOT.get(),
                            new TranslatableComponent("advancements.story.obtain_silver.title"),
                            new TranslatableComponent("advancements.story.obtain_silver.description"),
                            null,
                            FrameType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("has_silver_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(OTags.Items.INGOTS_SILVER).build()))
                    .save(consumer, "oreganized:story/obtain_silver");

            Advancement electrum_gear = Advancement.Builder.advancement()
                    .parent(obtain_silver)
                    .display(
                            OItems.ELECTRUM_CHESTPLATE.get(),
                            new TranslatableComponent("advancements.story.electrum_gear.title"),
                            new TranslatableComponent("advancements.story.electrum_gear.description"),
                            null,
                            FrameType.CHALLENGE,
                            true,
                            true,
                            false
                    )
                    .addCriterion("has_all_electrum_armor", InventoryChangeTrigger.TriggerInstance.hasItems(
                        OItems.ELECTRUM_HELMET.get(), OItems.ELECTRUM_CHESTPLATE.get(), OItems.ELECTRUM_LEGGINGS.get(), OItems.ELECTRUM_BOOTS.get()
                    ))
                    .save(consumer, "oreganized:story/electrum_gear");

            Advancement melting_point = Advancement.Builder.advancement()
                    .parent(getAdv("minecraft:story/upgrade_tools"))
                    .display(
                            OItems.MOLTEN_LEAD_BUCKET.get(),
                            new TranslatableComponent("advancements.story.melting_point.title"),
                            new TranslatableComponent("advancements.story.melting_point.description"),
                            null,
                            FrameType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("has_molten_lead_bucket", InventoryChangeTrigger.TriggerInstance.hasItems(OItems.MOLTEN_LEAD_BUCKET.get()))
                    //.addCriterion("item_used_on_cauldron", ItemInteractWithBlockTrigger.TriggerInstance.itemUsedOnBlock()))
                    .save(consumer, "oreganized:story/melting_point");

            Advancement disc_smith = Advancement.Builder.advancement()
                    .parent(melting_point)
                    .display(
                            OItems.MUSIC_DISC_STRUCTURE.get(),
                            new TranslatableComponent("advancements.story.disc_smith.title"),
                            new TranslatableComponent("advancements.story.disc_smith.description"),
                            null,
                            FrameType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("use_disc_on_lead_cauldron", ItemUsedOnBlockTrigger.TriggerInstance.itemUsedOnBlock(new LocationPredicate.Builder().setBlock(new BlockPredicate(null, Set.of(OBlocks.MOLTEN_LEAD_CAULDRON.get()), StatePropertiesPredicate.ANY, NbtPredicate.ANY)), ItemPredicate.Builder.item().of(Items.MUSIC_DISC_11)))
                    .addCriterion("has_structure_disc", InventoryChangeTrigger.TriggerInstance.hasItems(OItems.MUSIC_DISC_STRUCTURE.get()))
                    .save(consumer, "oreganized:story/disc_smith");
        }

        protected Advancement getAdv(String loc) {
            return Advancement.Builder.advancement().build(new ResourceLocation(loc));
        }
    }
}
