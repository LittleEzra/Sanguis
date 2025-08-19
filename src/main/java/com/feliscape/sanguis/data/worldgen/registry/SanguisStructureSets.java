package com.feliscape.sanguis.data.worldgen.registry;

import com.feliscape.sanguis.Sanguis;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

import java.util.List;

public class SanguisStructureSets {
    public static final ResourceKey<StructureSet> HUNTER_CAMP = createKey("hunter_camp");

    public static void bootstrap(BootstrapContext<StructureSet> context){
        HolderGetter<Structure> structureGetter = context.lookup(Registries.STRUCTURE);

        context.register(
                HUNTER_CAMP,
                new StructureSet(
                        List.of(
                                StructureSet.entry(structureGetter.getOrThrow(SanguisStructures.HUNTER_CAMP))
                        ),
                        new RandomSpreadStructurePlacement(28, 10, RandomSpreadType.LINEAR, 14356925)
                )
        );
    }

    private static ResourceKey<StructureSet> createKey(String name) {
        return ResourceKey.create(Registries.STRUCTURE_SET, Sanguis.location(name));
    }
}
