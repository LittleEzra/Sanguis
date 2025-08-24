package com.feliscape.sanguis.data.datagen.loot;

import com.feliscape.sanguis.data.loot.SanguisChestLootTables;
import com.feliscape.sanguis.data.loot.SanguisModifierLootTables;
import com.feliscape.sanguis.registry.SanguisItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.function.BiConsumer;

import static net.minecraft.world.level.storage.loot.LootPool.lootPool;
import static net.minecraft.world.level.storage.loot.entries.LootItem.lootTableItem;
import static net.minecraft.world.level.storage.loot.functions.SetItemCountFunction.setCount;
import static net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition.randomChance;
import static net.minecraft.world.level.storage.loot.providers.number.UniformGenerator.between;

public class SanguisAdditionalEntityLootTableProvider implements LootTableSubProvider {
    HolderLookup.Provider provider;

    public SanguisAdditionalEntityLootTableProvider(HolderLookup.Provider lookupProvider) {
        provider = lookupProvider;
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        output.accept(SanguisModifierLootTables.ADDITIONAL_BAT_LOOT, LootTable.lootTable()
                .withPool(lootPool()
                        .when(randomChance(0.5F))
                        .add(lootTableItem(SanguisItems.BAT_WING)).apply(setCount(between(1.0F, 2.0F)))
                )
        );
    }
}
