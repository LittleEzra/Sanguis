package com.feliscape.sanguis.data.worldgen.registry;

import com.feliscape.sanguis.Sanguis;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

public class SanguisPlacedFeatures {
    public static final ResourceKey<PlacedFeature> WILD_GARLIC_PATCH = createKey("wild_garlic_patch");
    public static final ResourceKey<PlacedFeature> WILD_GARLIC_PATCH_COMMON = createKey("wild_garlic_patch_common");
    public static final ResourceKey<PlacedFeature> BLOOD_ORANGE_VINE = createKey("blood_orange_vine");
    public static final ResourceKey<PlacedFeature> BLOOD_ORANGE_VINE_PATCH = createKey("blood_orange_vine_patch");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        Holder<ConfiguredFeature<?, ?>> wildGarlicPatchFeature = configuredFeatures.getOrThrow(SanguisConfiguredFeatures.WILD_GARLIC_PATCH);
        Holder<ConfiguredFeature<?, ?>> bloodOrangeVineFeature = configuredFeatures.getOrThrow(SanguisConfiguredFeatures.BLOOD_ORANGE_VINE);
        Holder<ConfiguredFeature<?, ?>> bloodOrangeVinePatchFeature = configuredFeatures.getOrThrow(SanguisConfiguredFeatures.BLOOD_ORANGE_VINE_PATCH);
        PlacementUtils.register(
                context, WILD_GARLIC_PATCH, wildGarlicPatchFeature, RarityFilter.onAverageOnceEvery(120),
                InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()
        );
        PlacementUtils.register(
                context, WILD_GARLIC_PATCH_COMMON, wildGarlicPatchFeature, RarityFilter.onAverageOnceEvery(50),
                InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()
        );
        PlacementUtils.register(
                context, BLOOD_ORANGE_VINE, bloodOrangeVineFeature,
                InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                EnvironmentScanPlacement.scanningFor(
                        Direction.UP,
                        BlockPredicate.hasSturdyFace(Direction.DOWN),
                        BlockPredicate.ONLY_IN_AIR_PREDICATE, 12),
                RandomOffsetPlacement.vertical(ConstantInt.of(-1))
        );
        PlacementUtils.register(
                context, BLOOD_ORANGE_VINE_PATCH, bloodOrangeVinePatchFeature, RarityFilter.onAverageOnceEvery(20),
                InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                RandomOffsetPlacement.vertical(ConstantInt.of(-1)),
                BiomeFilter.biome()
        );
    }

    public static ResourceKey<PlacedFeature> createKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Sanguis.location(name));
    }
}
