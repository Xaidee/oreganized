package me.gleep.oreganized.compat;

import me.gleep.oreganized.items.tiers.OreganizedTiers;
import me.gleep.oreganized.registry.OItems;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.item.KnifeItem;

public class FarmersDelightCompat {
    public static final RegistryObject<Item> ELECTRUM_KNIFE = OItems.ITEMS.register("electrum_knife",
            () -> new KnifeItem(OreganizedTiers.ELECTRUM, 0.5F, -1.8F, (new Item.Properties()).tab(FarmersDelight.CREATIVE_TAB)));

    public static void init() {}
}
