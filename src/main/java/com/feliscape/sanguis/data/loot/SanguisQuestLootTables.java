package com.feliscape.sanguis.data.loot;

import com.feliscape.sanguis.Sanguis;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public class SanguisQuestLootTables {
    public static final ResourceKey<LootTable> BASIC_REWARD = key("quest/reward/basic");

    protected static ResourceKey<LootTable> key(String path) {
        return ResourceKey.create(Registries.LOOT_TABLE, Sanguis.location(path));
    }

    public static class ItemRequirements{
        public static final ResourceKey<LootTable> HUNT_VAMPIRE = key("quest/item_requirement/hunt_vampire");
    }
}
