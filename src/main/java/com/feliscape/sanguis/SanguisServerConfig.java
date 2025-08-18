package com.feliscape.sanguis;

import net.neoforged.neoforge.common.ModConfigSpec;

public class SanguisServerConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();


    public static final SanguisServerConfig CONFIG;
    public static final ModConfigSpec SPEC;

    public final ModConfigSpec.DoubleValue vampireDrainDistance;

    public SanguisServerConfig(ModConfigSpec.Builder builder){
        builder.push("vampire_form");
        vampireDrainDistance = builder
                .translation("sanguis.configuration.server.vampire_form.drain_distance")
                .comment("How close you have to be to an entity to drain its blood")
                .defineInRange("drain_distance", 2.0D, 1.0D, 16.0D);
        builder.pop();
    }

    static {
        var pair = BUILDER.configure(SanguisServerConfig::new);

        CONFIG = pair.getLeft();
        SPEC = pair.getRight();
    }
}
