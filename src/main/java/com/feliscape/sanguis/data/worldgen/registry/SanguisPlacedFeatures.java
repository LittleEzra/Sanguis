package com.feliscape.sanguis.data.worldgen.registry;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.registry.SanguisBlocks;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

import java.util.List;

public class SanguisPlacedFeatures {
    public static final ResourceKey<PlacedFeature> WILD_GARLIC_PATCH = createKey("wild_garlic_patch");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        Holder<ConfiguredFeature<?, ?>> wildGarlicPatchFeature = configuredFeatures.getOrThrow(SanguisConfiguredFeatures.WILD_GARLIC_PATCH);
        PlacementUtils.register(
                context, WILD_GARLIC_PATCH, wildGarlicPatchFeature, RarityFilter.onAverageOnceEvery(50),
                InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()
        );
    }

    public static ResourceKey<PlacedFeature> createKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Sanguis.location(name));
    }
}
