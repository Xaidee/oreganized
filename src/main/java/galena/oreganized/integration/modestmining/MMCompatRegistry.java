package galena.oreganized.integration.modestmining;

import galena.oreganized.content.item.OItem;
import galena.oreganized.index.OItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public class MMCompatRegistry {

    public static final RegistryObject<Item> LEAD_PLATING = OItems.ITEMS.register("lead_plating", () -> new OItem(CreativeModeTab.TAB_MISC));
    public static final RegistryObject<Item> SILVER_PLATING = OItems.ITEMS.register("silver_plating", () -> new OItem(CreativeModeTab.TAB_MISC));
    public static final RegistryObject<Item> ELECTRUM_PLATING = OItems.ITEMS.register("electrum_plating", () -> new OItem(CreativeModeTab.TAB_MISC));

    public static final RegistryObject<Item> LEAD_PIECE = OItems.ITEMS.register("lead_piece", () -> new OItem(CreativeModeTab.TAB_MISC));
    public static final RegistryObject<Item> SILVER_PIECE = OItems.ITEMS.register("silver_piece", () -> new OItem(CreativeModeTab.TAB_MISC));
    public static final RegistryObject<Item> ELECTRUM_PIECE = OItems.ITEMS.register("electrum_piece", () -> new OItem(CreativeModeTab.TAB_MISC));

    public static final RegistryObject<Item> LEAD_DUST = OItems.ITEMS.register("lead_dust", () -> new OItem(CreativeModeTab.TAB_MISC));
    public static final RegistryObject<Item> SILVER_DUST = OItems.ITEMS.register("silver_dust", () -> new OItem(CreativeModeTab.TAB_MISC));

    public static void register() {

    }
}
