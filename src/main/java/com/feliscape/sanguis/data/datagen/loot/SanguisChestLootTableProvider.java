package com.feliscape.sanguis.data.datagen.loot;

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

public class SanguisChestLootTableProvider implements LootTableSubProvider {
    HolderLookup.Provider provider;

    public SanguisChestLootTableProvider(HolderLookup.Provider lookupProvider) {
        provider = lookupProvider;
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        output.accept(SanguisModifierLootTables.ADDITIONAL_VILLAGE_LOOT, LootTable.lootTable()
                .withPool(lootPool()
                        .add(lootTableItem(SanguisItems.GARLIC)
                                .apply(setCount(between(1.4F, 4.0F)))
                                .when(randomChance(0.75F)))
                        .add(lootTableItem(SanguisItems.GARLIC_FLOWER)
                                .apply(setCount(between(1.2F, 2.6F)))
                                .when(randomChance(0.45F)))
                ));
    }
}
