package dev.agnor.passivepregen;

import net.minecraftforge.common.ForgeConfigSpec;

public class ForgePregenConfig {
    public static final ForgeConfigSpec SERVER_SPEC;

    public static final ForgeConfigSpec.IntValue player_distance;
    public static final ForgeConfigSpec.IntValue spawn_distance;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        configBuilder.comment("values are defines as the radius. There will be (2r-1)^2 chunks generated");
        player_distance = configBuilder.comment("The radius around a player new Chunks will be generated.").defineInRange("player_distance", 25, 0, 300);
        spawn_distance = configBuilder.comment("The radius around the world spawn new Chunks will be generated.").defineInRange("spawn_distance", 100, 0, 500);
        SERVER_SPEC = configBuilder.build();
    }
}
