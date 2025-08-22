package com.feliscape.sanguis.data.worldgen.registry;

import com.feliscape.sanguis.Sanguis;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

public class SanguisTemplatePools {
    public static final ResourceKey<StructureTemplatePool> HUNTER_CAMP_BASE = createKey("hunter_camp/base");
    public static final ResourceKey<StructureTemplatePool> HUNTER_CAMP_QUEST_BOARD = createKey("hunter_camp/quest_board");
    public static final ResourceKey<StructureTemplatePool> HUNTER_CAMP_TOWER = createKey("hunter_camp/tower");
    public static final ResourceKey<StructureTemplatePool> HUNTER_CAMP_FEATURES = createKey("hunter_camp/features");

    public static void bootstrap(BootstrapContext<StructureTemplatePool> context){
        HolderGetter<StructureProcessorList> processorListGetter = context.lookup(Registries.PROCESSOR_LIST);
        Holder<StructureProcessorList> mossify20Percent = processorListGetter.getOrThrow(ProcessorLists.MOSSIFY_20_PERCENT);

        HolderGetter<StructureTemplatePool> poolGetter = context.lookup(Registries.TEMPLATE_POOL);
        Holder<StructureTemplatePool> empty = poolGetter.getOrThrow(Pools.EMPTY);

        context.register(
                HUNTER_CAMP_BASE,
                new StructureTemplatePool(
                        empty,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.legacy(Sanguis.stringLocation("hunter_camp/base")), 1)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );
        context.register(
                HUNTER_CAMP_QUEST_BOARD,
                new StructureTemplatePool(
                        empty,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.legacy(Sanguis.stringLocation("hunter_camp/quest_board/quest_board_0")), 1),
                                Pair.of(StructurePoolElement.legacy(Sanguis.stringLocation("hunter_camp/quest_board/quest_board_1")), 1),
                                Pair.of(StructurePoolElement.legacy(Sanguis.stringLocation("hunter_camp/quest_board/quest_board_2")), 1)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );
        context.register(
                HUNTER_CAMP_TOWER,
                new StructureTemplatePool(
                        empty,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.legacy(Sanguis.stringLocation("hunter_camp/tower_0"), mossify20Percent), 1),
                                Pair.of(StructurePoolElement.legacy(Sanguis.stringLocation("hunter_camp/tower_1"), mossify20Percent), 1)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );
        context.register(
                HUNTER_CAMP_FEATURES,
                new StructureTemplatePool(
                        empty,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.empty(), 10),
                                Pair.of(StructurePoolElement.legacy(Sanguis.stringLocation("hunter_camp/feature/food_tents")), 4),
                                Pair.of(StructurePoolElement.legacy(Sanguis.stringLocation("hunter_camp/feature/tents")), 4),
                                Pair.of(StructurePoolElement.legacy(Sanguis.stringLocation("hunter_camp/feature/garlic_farm")), 5),
                                Pair.of(StructurePoolElement.legacy(Sanguis.stringLocation("hunter_camp/feature/garlic_farm_large")), 4),
                                Pair.of(StructurePoolElement.legacy(Sanguis.stringLocation("hunter_camp/feature/storage")), 4),
                                Pair.of(StructurePoolElement.legacy(Sanguis.stringLocation("hunter_camp/feature/shrine"), mossify20Percent), 2),
                                Pair.of(StructurePoolElement.legacy(Sanguis.stringLocation("hunter_camp/feature/target_dummies")), 4)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );
    }

    private static ResourceKey<StructureTemplatePool> createKey(String name) {
        return ResourceKey.create(Registries.TEMPLATE_POOL, Sanguis.location(name));
    }
}
