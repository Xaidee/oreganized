package galena.oreganized;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

@EventBusSubscriber(modid = Oreganized.MOD_ID)
public class OreganizedConfig {
    public static final Common COMMON;
    public static final Client CLIENT;
    private static final ForgeConfigSpec COMMON_SPEC;
    private static final ForgeConfigSpec CLIENT_SPEC;

    public static class Common {
        public final ConfigValue<Boolean> poisonInsteadOfStunning;
        public final ConfigValue<Boolean> leadDustCloud;
        public final ConfigValue<Boolean> pillagerSpawnWithBolts;
        public final ConfigValue<Boolean> scribeSilkTouchStone;
        public final ConfigValue<Integer> moltenLeadDelay;
        public final ConfigValue<Boolean> cauldronLeadMelting;

        private Common(ForgeConfigSpec.Builder builder) {
            builder.comment("Common");
            builder.push("common");

            poisonInsteadOfStunning = builder.comment("Should lead poisoning events give just Poison instead of Brain Damage?").define("poisonInsteadOfBrainDamage", false);
            leadDustCloud = builder.comment("Should lead ore spawn dust clouds when broken without adjacent water?").define("leadDustCloud", true);
            pillagerSpawnWithBolts = builder.comment("Pillagers have a chance to spawn with a lead bolt in their offhand").define("pillagerSpawnWithBolts", true);
            scribeSilkTouchStone = builder.comment("The scribe is able to silk-touch pickaxe-related blocks").define("scribeSilkTouchStone", true);
            moltenLeadDelay = builder.comment("Time in ticks molten lead waits until flowing downwards").defineInRange("moltenLeadDelay", 20 * 10, 0, 20 * 100);
            cauldronLeadMelting = builder.comment("Can lead blocks be placed into a cauldron to melt?").define("cauldronLeadMelting", true);

            builder.pop();
        }
    }

    public static class Client {

        public final ConfigValue<Boolean> renderStunningOverlay;

        public Client(ForgeConfigSpec.Builder builder) {
            builder.comment("Client");
            builder.push("client");

            renderStunningOverlay = builder.comment("Should the custom overlay for the brain damage effect be rendered?").define("renderBrainDamageOverlay", true);

            builder.pop();
        }
    }

    static {
        final Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
        final Pair<Client, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(Client::new);

        COMMON = commonSpecPair.getLeft();
        CLIENT = clientSpecPair.getLeft();
        COMMON_SPEC = commonSpecPair.getRight();
        CLIENT_SPEC = clientSpecPair.getRight();
    }

    public static void register() {
        var context = ModLoadingContext.get();
        context.registerConfig(ModConfig.Type.COMMON, COMMON_SPEC);
        context.registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC);
    }

}
