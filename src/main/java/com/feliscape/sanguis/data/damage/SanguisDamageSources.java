package com.feliscape.sanguis.data.damage;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class SanguisDamageSources {
    public static DamageSource draining(Level level, @Nullable Entity drainer){
        return new DamageSource(
                level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(SanguisDamageTypes.DRAINING), drainer
        );
    }
    public static DamageSource draining(RegistryAccess access, @Nullable Entity drainer){
        return new DamageSource(
                access.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(SanguisDamageTypes.DRAINING), drainer
        );
    }
}
