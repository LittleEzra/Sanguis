package com.feliscape.sanguis.data.datagen.loot;

import com.feliscape.sanguis.data.loot.SanguisQuestLootTables;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SanguisLootTableProvider extends LootTableProvider {

    public SanguisLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, Collections.emptySet(), List.of(
                new SubProviderEntry(SanguisBlockLootTableProvider::new, LootContextParamSets.BLOCK),
                new SubProviderEntry(SanguisEntityLootTableProvider::new, LootContextParamSets.ENTITY),
                new SubProviderEntry(SanguisChestLootTableProvider::new, LootContextParamSets.CHEST),
                new SubProviderEntry(SanguisQuestLootTableProvider::new, LootContextParamSets.EMPTY)
                ),
                registries);
    }
}
