package dev.agnor.passivepregen.config;

import com.mojang.datafixers.util.Pair;
import dev.agnor.passivepregen.Constants;

public class PassiveConfig {
    public static SimpleConfig CONFIG;
    public static int player_distance;
    public static int spawn_distance;

    public static void create() {
        PassiveConfigProvider provider = new PassiveConfigProvider();

        provider.addKeyValuePair(new Pair<>("player_distance", 25), "The radius around a player new Chunks will be generated.");
        provider.addKeyValuePair(new Pair<>("spawn_distance", 100), "The radius around the world spawn new Chunks will be generated.");


        CONFIG = SimpleConfig.of(Constants.MOD_ID).provider(provider).request();

        player_distance = CONFIG.getOrDefault("player_distance", 25);
        spawn_distance = CONFIG.getOrDefault("spawn_distance", 100);
    }
}
