package galena.oreganized.integration;

import galena.oreganized.integration.farmersdelight.FDCompatRegistry;
import galena.oreganized.integration.modestmining.MMCompatRegistry;
import galena.oreganized.integration.shieldexp.SECompatRegistry;
import galena.oreganized.integration.tconstruct.TCCompatRegistry;
import net.minecraftforge.fml.ModList;

public class CompatHandler {

    public static final boolean farmersDelight;
    public static final boolean shieldexp;
    public static final boolean modestmining;
    public static final boolean nethersdelight;
    public static final boolean quark;
    public static final boolean tinkersConstruct;

    static {
        ModList mods = ModList.get();

        farmersDelight = mods.isLoaded("farmersdelight");
        shieldexp = mods.isLoaded("shieldexp");
        modestmining = mods.isLoaded("modestmining");
        nethersdelight = mods.isLoaded("nethersdelight");
        quark = mods.isLoaded("quark");
        tinkersConstruct = mods.isLoaded("tconstruct");
    }

    public static void register() {
        if (farmersDelight) FDCompatRegistry.register();
        if (shieldexp) SECompatRegistry.register();
        if (modestmining) MMCompatRegistry.register();
        //if (quark) QCompatRegistry.register();
        if (tinkersConstruct) TCCompatRegistry.register();
    }
}
