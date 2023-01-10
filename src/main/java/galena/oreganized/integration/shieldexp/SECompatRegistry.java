package galena.oreganized.integration.shieldexp;

import galena.oreganized.index.OItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShieldItem;
import net.minecraftforge.registries.RegistryObject;
import org.infernalstudios.shieldexp.init.ItemsInit;

public class SECompatRegistry {

    public static final RegistryObject<ShieldItem> ELECTRUM_SHIELD = registerShield("electrum_shield", 363);

    public static RegistryObject<ShieldItem> registerShield(String id, int durability) {
        Item.Properties properties = (new Item.Properties()).durability(durability).tab(CreativeModeTab.TAB_COMBAT);

        RegistryObject<ShieldItem> shield = OItems.ITEMS.register(id, () -> new ShieldItem(properties));
        ItemsInit.SHIELDS.add(shield);
        return shield;
    }
    public static void register() {

    }
}