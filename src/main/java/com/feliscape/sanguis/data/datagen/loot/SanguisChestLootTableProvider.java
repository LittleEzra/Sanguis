package com.feliscape.sanguis.data.datagen.loot;

import com.feliscape.sanguis.data.loot.SanguisChestLootTables;
import com.feliscape.sanguis.data.loot.SanguisModifierLootTables;
import com.feliscape.sanguis.registry.SanguisItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
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
        output.accept(SanguisChestLootTables.HUNTER_CAMP_FOOD, LootTable.lootTable()
                .withPool(lootPool()
                        .setRolls(between(1.0F, 1.7F))
                        .add(lootTableItem(SanguisItems.GARLIC)
                                .apply(setCount(between(2.0F, 4.0F)))
                                .when(randomChance(0.75F)))
                        .add(lootTableItem(SanguisItems.GARLIC_FLOWER)
                                .apply(setCount(between(1.2F, 2.6F)))
                                .when(randomChance(0.45F)))
                ).withPool(lootPool()
                        .setRolls(between(3.0F, 5.2F))
                        .add(lootTableItem(Items.CARROT)
                                .apply(setCount(between(1.0F, 3.0F)))
                                .setWeight(3))
                        .add(lootTableItem(Items.POTATO)
                                .apply(setCount(between(2.0F, 3.0F)))
                                .setWeight(3))
                        .add(lootTableItem(Items.BEETROOT)
                                .apply(setCount(between(1.0F, 2.0F)))
                                .setWeight(2))
                        .add(lootTableItem(Items.WHEAT)
                                .apply(setCount(between(2.2F, 5.6F)))
                                .setWeight(3))
                        .add(lootTableItem(Items.BREAD)
                                .apply(setCount(between(1.2F, 2.6F)))
                                .setWeight(1))
                ));
        output.accept(SanguisChestLootTables.HUNTER_CAMP_TOWER, LootTable.lootTable()
                .withPool(lootPool()
                        .setRolls(between(1.0F, 2.0F))
                        .add(lootTableItem(SanguisItems.GARLIC)
                                .apply(setCount(between(3.5F, 4.0F)))
                                .when(randomChance(0.75F)))
                        .add(lootTableItem(SanguisItems.GARLIC_FLOWER)
                                .apply(setCount(between(1.2F, 2.6F)))
                                .when(randomChance(0.6F)))
                )
                .withPool(lootPool()
                        .setRolls(between(1.2F, 4.0F))
                        .add(lootTableItem(Items.IRON_INGOT)
                                .apply(setCount(between(2.0F, 3.0F)))
                                .setWeight(5))
                        .add(lootTableItem(Items.GOLD_INGOT)
                                .apply(setCount(between(1.2F, 2.6F)))
                                .setWeight(2))
                        .add(lootTableItem(Items.OAK_PLANKS)
                                .apply(setCount(between(2.5F, 5.6F)))
                                .setWeight(4))
                )
                .withPool(lootPool()
                        .setRolls(between(1.0F, 1.8F))
                        .when(randomChance(0.8F))
                        .add(lootTableItem(SanguisItems.WOODEN_STAKE)
                                .setWeight(5))
                        .add(lootTableItem(SanguisItems.SYRINGE)
                                .apply(setCount(between(1.0F, 2.3F)))
                                .setWeight(6))
                        .add(lootTableItem(Items.CROSSBOW)
                                .setWeight(4))
                        .add(lootTableItem(SanguisItems.GOLDEN_QUARREL)
                                .apply(setCount(between(1.0F, 4.3F)))
                                .setWeight(4))
                )
        );
        output.accept(SanguisChestLootTables.HUNTER_CAMP_SHRINE, LootTable.lootTable()
                .withPool(lootPool()
                        .add(lootTableItem(SanguisItems.GARLIC)
                                .apply(setCount(between(1.0F, 2.0F)))
                                .when(randomChance(0.6F)))
                        .add(lootTableItem(SanguisItems.GARLIC_FLOWER)
                                .apply(setCount(between(3.0F, 5.7F)))
                                .when(randomChance(0.7F)))
                ).withPool(lootPool()
                                .setRolls(between(2.35F, 4.0F))
                        .add(lootTableItem(Items.GOLD_INGOT)
                                .apply(setCount(between(1.0F, 2.0F)))
                                .setWeight(3)
                                .when(randomChance(0.6F)))
                        .add(lootTableItem(Items.GOLD_NUGGET)
                                .apply(setCount(between(1.0F, 4.0F)))
                                .setWeight(5))
                        .add(lootTableItem(Items.IRON_INGOT)
                                .apply(setCount(between(2.0F, 4.0F)))
                                .setWeight(4))
                )
        );
        output.accept(SanguisChestLootTables.HUNTER_CAMP_STORAGE, LootTable.lootTable()
                .withPool(lootPool()
                                .setRolls(between(3.0F, 5.0F))
                        .add(lootTableItem(Items.GOLD_INGOT)
                                .apply(setCount(between(2.0F, 3.0F)))
                                .setWeight(2)
                                .when(randomChance(0.6F)))
                        .add(lootTableItem(Items.GOLD_NUGGET)
                                .apply(setCount(between(1.0F, 4.0F)))
                                .setWeight(2))
                        .add(lootTableItem(Items.IRON_INGOT)
                                .apply(setCount(between(3.0F, 5.0F)))
                                .setWeight(4))
                        .add(lootTableItem(Items.COAL)
                                .apply(setCount(between(4.0F, 7.0F)))
                                .setWeight(5))
                        .add(lootTableItem(Items.RAW_IRON)
                                .apply(setCount(between(4.0F, 7.0F)))
                                .setWeight(5))
                )
        );
        output.accept(SanguisChestLootTables.HUNTER_CAMP_TENT, LootTable.lootTable()
                .withPool(lootPool()
                                .setRolls(between(1.0F, 2.0F))
                        .add(lootTableItem(Items.GOLD_INGOT)
                                .apply(setCount(between(2.0F, 3.0F)))
                                .setWeight(2)
                                .when(randomChance(0.6F)))
                        .add(lootTableItem(Items.GOLD_NUGGET)
                                .apply(setCount(between(1.0F, 4.0F)))
                                .setWeight(3))
                ).withPool(lootPool()
                        .setRolls(between(2.0F, 4.0F))
                        .add(lootTableItem(Items.BREAD)
                                .apply(setCount(between(4.0F, 5.0F)))
                                .setWeight(2))
                        .add(lootTableItem(Items.POTATO)
                                .apply(setCount(between(2.0F, 3.0F)))
                                .setWeight(1))
                        .add(lootTableItem(Items.CARROT)
                                .apply(setCount(between(1.0F, 3.0F)))
                                .setWeight(1))
                )
        );

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
