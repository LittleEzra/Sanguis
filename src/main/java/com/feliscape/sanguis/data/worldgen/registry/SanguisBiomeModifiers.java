package com.feliscape.sanguis.data.worldgen.registry;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.registry.SanguisEntityTypes;
import com.feliscape.sanguis.registry.SanguisTags;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;

public class SanguisBiomeModifiers {
    public static final ResourceKey<BiomeModifier> ADD_VAMPIRES = createKey("add_vampires");

    public static void bootstrap(BootstrapContext<BiomeModifier> context){
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);

        context.register(ADD_VAMPIRES,
                new BiomeModifiers.AddSpawnsBiomeModifier(
                        biomes.getOrThrow(SanguisTags.Biomes.SPAWNS_VAMPIRES),
                        List.of(new MobSpawnSettings.SpawnerData(
                                SanguisEntityTypes.VAMPIRE.get(),
                                70, 1, 2
                        ))
                ));
    }
    static ResourceKey<BiomeModifier> createKey(String name){
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Sanguis.location(name));
    }
}
