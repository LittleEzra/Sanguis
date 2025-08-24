package com.feliscape.sanguis;

import net.neoforged.neoforge.common.ModConfigSpec;

public class SanguisClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();


    public static final SanguisClientConfig CONFIG;
    public static final ModConfigSpec SPEC;

    public final ModConfigSpec.DoubleValue vampireNightVisionBrightness;

    public SanguisClientConfig(ModConfigSpec.Builder builder){
        builder.push("vampire_form");
        vampireNightVisionBrightness = builder
                .translation("sanguis.configuration.client.vampire_form.night_vision_brightness")
                .comment("How bright you can see at night as a vampire. 1.0 is equal to the Night Vision effect.")
                .defineInRange("night_vision_brightness", 0.5D, 0.0D, 1.0D);
        builder.pop();
    }

    static {
        var pair = BUILDER.configure(SanguisClientConfig::new);

        CONFIG = pair.getLeft();
        SPEC = pair.getRight();
    }
}
