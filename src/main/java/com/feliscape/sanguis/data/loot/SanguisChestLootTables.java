package com.feliscape.sanguis.data.loot;

import com.feliscape.sanguis.Sanguis;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public class SanguisChestLootTables {
    public static final ResourceKey<LootTable> HUNTER_CAMP_FOOD = key("chests/hunter_camp/food");
    public static final ResourceKey<LootTable> HUNTER_CAMP_TOWER = key("chests/hunter_camp/tower");
    public static final ResourceKey<LootTable> HUNTER_CAMP_SHRINE = key("chests/hunter_camp/shrine");
    public static final ResourceKey<LootTable> HUNTER_CAMP_TENT = key("chests/hunter_camp/tent");
    public static final ResourceKey<LootTable> HUNTER_CAMP_STORAGE = key("chests/hunter_camp/storage");

    private static ResourceKey<LootTable> key(String path) {
        return ResourceKey.create(Registries.LOOT_TABLE, Sanguis.location(path));
    }
}
