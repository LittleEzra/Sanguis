package com.feliscape.sanguis.data.worldgen.registry;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.registry.SanguisEntityTypes;
import com.feliscape.sanguis.registry.SanguisTags;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;

import java.util.Map;

public class SanguisStructures {
    public static final ResourceKey<Structure> HUNTER_CAMP = createKey("hunter_camp");

    private static final Map<MobCategory, StructureSpawnOverride> HUNTER_CAMP_SPAWNS =
            Map.of(
                    MobCategory.MONSTER,
                    new StructureSpawnOverride(
                            StructureSpawnOverride.BoundingBoxType.STRUCTURE,
                            WeightedRandomList.create(new MobSpawnSettings.SpawnerData(SanguisEntityTypes.VAMPIRE_HUNTER.get(),
                                    1, 1, 1))
                    )
            );

    public static void bootstrap(BootstrapContext<Structure> context){
        HolderGetter<Biome> biomeGetter = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> poolGetter = context.lookup(Registries.TEMPLATE_POOL);
        context.register(
                HUNTER_CAMP,
                new JigsawStructure(
                        new Structure.StructureSettings.Builder(biomeGetter.getOrThrow(SanguisTags.Biomes.HAS_HUNTER_CAMPS))
                                .terrainAdapation(TerrainAdjustment.BEARD_THIN)
                                .spawnOverrides(HUNTER_CAMP_SPAWNS)
                                .build(),
                        poolGetter.getOrThrow(SanguisTemplatePools.HUNTER_CAMP_BASE),
                        3,
                        ConstantHeight.of(VerticalAnchor.absolute(0)),
                        true,
                        Heightmap.Types.WORLD_SURFACE_WG
                )
        );
    }

    private static ResourceKey<Structure> createKey(String name) {
        return ResourceKey.create(Registries.STRUCTURE, Sanguis.location(name));
    }
}
