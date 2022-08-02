package dev.agnor.passivepregen.config;


import com.mojang.datafixers.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class PassiveConfigProvider implements SimpleConfig.DefaultConfig {

    private String configContents = "";


    public void addKeyValuePair(Pair<String, ?> keyValuePair, String comment) {
        configContents += keyValuePair.getFirst() + "=" + keyValuePair.getSecond() + " #"
                + comment + "\n";
    }

    @Override
    public String get(String namespace) {
        return configContents;
    }
}
