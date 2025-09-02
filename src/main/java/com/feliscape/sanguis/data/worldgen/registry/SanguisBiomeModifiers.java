package com.feliscape.sanguis.data.worldgen.registry;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.registry.SanguisEntityTypes;
import com.feliscape.sanguis.registry.SanguisTags;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;

public class SanguisBiomeModifiers {
    public static final ResourceKey<BiomeModifier> ADD_VAMPIRES = createKey("add_vampires");
    public static final ResourceKey<BiomeModifier> ADD_WILD_GARLIC = createKey("add_wild_garlic");
    public static final ResourceKey<BiomeModifier> ADD_COMMON_WILD_GARLIC = createKey("add_common_wild_garlic");

    public static void bootstrap(BootstrapContext<BiomeModifier> context){
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);

        context.register(ADD_VAMPIRES,
                new BiomeModifiers.AddSpawnsBiomeModifier(
                        biomes.getOrThrow(SanguisTags.Biomes.SPAWNS_VAMPIRES),
                        List.of(new MobSpawnSettings.SpawnerData(
                                SanguisEntityTypes.VAMPIRE.get(),
                                70, 1, 2
                        ))
                ));
        context.register(ADD_WILD_GARLIC,
                new BiomeModifiers.AddFeaturesBiomeModifier(
                        biomes.getOrThrow(SanguisTags.Biomes.HAS_WILD_GARLIC),
                        HolderSet.direct(placedFeatures.getOrThrow(SanguisPlacedFeatures.WILD_GARLIC_PATCH)),
                        GenerationStep.Decoration.VEGETAL_DECORATION
                ));
        context.register(ADD_COMMON_WILD_GARLIC,
                new BiomeModifiers.AddFeaturesBiomeModifier(
                        biomes.getOrThrow(SanguisTags.Biomes.HAS_COMMON_WILD_GARLIC),
                        HolderSet.direct(placedFeatures.getOrThrow(SanguisPlacedFeatures.WILD_GARLIC_PATCH_COMMON)),
                        GenerationStep.Decoration.VEGETAL_DECORATION
                ));
    }
    static ResourceKey<BiomeModifier> createKey(String name){
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Sanguis.location(name));
    }
}
