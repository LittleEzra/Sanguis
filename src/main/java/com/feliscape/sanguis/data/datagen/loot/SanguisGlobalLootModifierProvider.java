package com.feliscape.sanguis.data.datagen.loot;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.data.loot.SanguisModifierLootTables;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.concurrent.CompletableFuture;

public class SanguisGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public SanguisGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Sanguis.MOD_ID);
    }

    @Override
    protected void start() {
        add("add_to_village_plains",
                new AddTableLootModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.withDefaultNamespace("chests/village/village_plains_house")).build()
                },
                        SanguisModifierLootTables.ADDITIONAL_VILLAGE_LOOT
                ));
        add("add_to_village_taiga",
                new AddTableLootModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.withDefaultNamespace("chests/village/village_taiga_house")).build()
                },
                        SanguisModifierLootTables.ADDITIONAL_VILLAGE_LOOT
                ));
        add("add_to_village_savanna",
                new AddTableLootModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.withDefaultNamespace("chests/village/village_savanna_house")).build()
                },
                        SanguisModifierLootTables.ADDITIONAL_VILLAGE_LOOT
                ));
        add("add_to_pillager_outpost",
                new AddTableLootModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.withDefaultNamespace("chests/pillager_outpost")).build()
                },
                        SanguisModifierLootTables.ADDITIONAL_VILLAGE_LOOT
                ));
        add("add_to_bat",
                new AddTableLootModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.withDefaultNamespace("entities/bat")).build()
                },
                        SanguisModifierLootTables.ADDITIONAL_BAT_LOOT
                ));
    }
}
