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
                .withPool(lootPool()
                        .setRolls(between(1.0F, 2.0F))
                        .add(lootTableItem(Items.DIAMOND).setWeight(2).apply(setCount(between(1.0F, 2.7F))))
                        .add(lootTableItem(Items.EMERALD).setWeight(6).apply(setCount(between(2.0F, 5.3F))))
                ).withPool(lootPool()
                        .add(lootTableItem(Items.GOLD_INGOT).when(randomChance(0.6F)).apply(setCount(between(2.0F, 4.3F))))
                        .add(lootTableItem(Items.IRON_INGOT).when(randomChance(0.9F)).apply(setCount(between(3.0F, 6.0F))))
                ).withPool(lootPool().when(randomChance(0.35F))
                        .setRolls(between(2.0F, 3.0F))
                        .add(lootTableItem(SanguisItems.GARLIC).setWeight(4).when(randomChance(0.7F)).apply(setCount(between(2.0F, 4.3F))))
                        .add(lootTableItem(Items.POTATO).setWeight(8).apply(setCount(between(3.0F, 6.0F))))
                        .add(lootTableItem(Items.CARROT).setWeight(5).apply(setCount(between(2.0F, 5.0F))))
                )
        );

        output.accept(SanguisQuestLootTables.ItemRequirements.HUNT_VAMPIRE, LootTable.lootTable().withPool(lootPool()
                        .add(lootTableItem(SanguisItems.VAMPIRE_BLOOD).apply(setCount(between(7.0F, 16.0F))))
                        .add(lootTableItem(SanguisItems.BLOODY_FANG).apply(setCount(between(14.0F, 20.0F))))
                        .add(lootTableItem(SanguisItems.GARLIC).apply(setCount(between(14.0F, 24.0F))))
                ));
    }
}
