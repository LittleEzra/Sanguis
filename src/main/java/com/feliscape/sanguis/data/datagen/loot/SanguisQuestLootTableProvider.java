package com.feliscape.sanguis.data.datagen.loot;

import com.feliscape.sanguis.data.loot.SanguisQuestLootTables;
import com.feliscape.sanguis.registry.SanguisItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootTable;

import static net.minecraft.world.level.storage.loot.LootPool.lootPool;
import static net.minecraft.world.level.storage.loot.entries.LootItem.lootTableItem;
import static net.minecraft.world.level.storage.loot.functions.SetItemCountFunction.setCount;
import static net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition.randomChance;
import static net.minecraft.world.level.storage.loot.providers.number.UniformGenerator.between;

import java.util.function.BiConsumer;

public class SanguisQuestLootTableProvider implements LootTableSubProvider {
    HolderLookup.Provider provider;

    public SanguisQuestLootTableProvider(HolderLookup.Provider lookupProvider) {
        provider = lookupProvider;
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        output.accept(SanguisQuestLootTables.BASIC_REWARD, LootTable.lootTable()
                .withPool(lootPool().add(lootTableItem(Items.DIAMOND)))); // TODO: replace with worthwhile loot

        output.accept(SanguisQuestLootTables.ItemRequirements.HUNT_VAMPIRE, LootTable.lootTable().withPool(lootPool()
                        .add(lootTableItem(SanguisItems.VAMPIRE_BLOOD).apply(setCount(between(7.0F, 16.0F))))
                        .add(lootTableItem(SanguisItems.BLOODY_FANG).apply(setCount(between(14.0F, 20.0F))))
                        .add(lootTableItem(SanguisItems.GARLIC).apply(setCount(between(14.0F, 24.0F))))
                ));
    }
}
