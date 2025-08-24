package com.feliscape.sanguis.registry;

import com.feliscape.sanguis.Sanguis;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class SanguisTags {
    public static class Blocks{
        public static final TagKey<Block> VAMPIRE_REPELLENTS = create("vampire_repellents");

        private static TagKey<Block> create(String name){
            return TagKey.create(Registries.BLOCK, Sanguis.location(name));
        }
    }
    public static class Items{
        public static final TagKey<Item> STAKES = create("stakes");
        public static final TagKey<Item> CLEAVERS = create("cleavers");
        public static final TagKey<Item> QUARRELS = create("quarrels");
        public static final TagKey<Item> GUIDE_BOOK_MATERIALS = create("guide_book_materials");

        private static TagKey<Item> create(String name){
            return TagKey.create(Registries.ITEM, Sanguis.location(name));
        }
    }
    public static class EntityTypes{
        public static final TagKey<EntityType<?>> VAMPIRE_NEUTRAL = create("vampire_neutral");
        public static final TagKey<EntityType<?>> VAMPIRIC = create("vampiric");
        public static final TagKey<EntityType<?>> HUNTER = create("hunter");
        public static final TagKey<EntityType<?>> STAKE_IMMUNE = create("stake_immune");
        public static final TagKey<EntityType<?>> INFECTABLE = create("infectable");
        public static final TagKey<EntityType<?>> FOUL_BLOOD = create("foul_blood");

        public static final TagKey<EntityType<?>> KILL_MOB_QUEST_VALID = create("kill_mob_quest_valid");

        private static TagKey<EntityType<?>> create(String name){
            return TagKey.create(Registries.ENTITY_TYPE, Sanguis.location(name));
        }
    }
    public static class Biomes{
        public static final TagKey<Biome> SPAWNS_VAMPIRES = create("spawns_vampires");
        public static final TagKey<Biome> HAS_HUNTER_CAMPS = create("has_structure/hunter_camp");

        private static TagKey<Biome> create(String name){
            return TagKey.create(Registries.BIOME, Sanguis.location(name));
        }
    }
}
