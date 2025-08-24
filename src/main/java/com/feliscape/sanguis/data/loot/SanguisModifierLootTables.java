package com.feliscape.sanguis.data.loot;

import com.feliscape.sanguis.Sanguis;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public class SanguisModifierLootTables {
    public static final ResourceKey<LootTable> ADDITIONAL_VILLAGE_LOOT = key("glm/chests/village");
    public static final ResourceKey<LootTable> ADDITIONAL_BAT_LOOT = key("additional_bat_loot");

    private static ResourceKey<LootTable> key(String path) {
        return ResourceKey.create(Registries.LOOT_TABLE, Sanguis.location(path));
    }
}
