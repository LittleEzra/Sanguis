package com.feliscape.sanguis.data.damage;

import com.feliscape.sanguis.Sanguis;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageType;

public class SanguisDamageTypes {
    public static final ResourceKey<DamageType> DRAINING = ResourceKey.create(Registries.DAMAGE_TYPE,
            Sanguis.location("draining"));

    public static void bootstrap(BootstrapContext<DamageType> context){
        context.register(DRAINING, new DamageType(DRAINING.location().toString(),
                0.2f,
                DamageEffects.HURT)
        );
    }
}
