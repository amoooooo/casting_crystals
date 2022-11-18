package coffee.amo.casting_crystals.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CastingCrystalsConfig {
    public static final ForgeConfigSpec GENERAL_SPEC;
    public static ForgeConfigSpec.BooleanValue enableCooldowns;
    public static ForgeConfigSpec.DoubleValue baseCooldown;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }

    private static void setupConfig(ForgeConfigSpec.Builder builder){
        enableCooldowns = builder.comment("Enable cooldowns for casting crystals").translation("casting_crystals.enablecooldowns").define("enableCooldowns", true);
        baseCooldown = builder.comment("Base cooldown for casting crystals. Supports range 0 - 100000 ticks.").translation("casting_crystals.basecooldown").defineInRange("baseCooldown", 100f, 0, 100000f);
    }
}
