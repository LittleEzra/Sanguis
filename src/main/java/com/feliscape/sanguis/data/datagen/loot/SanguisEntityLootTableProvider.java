package com.feliscape.sanguis.data.datagen.loot;

import com.feliscape.sanguis.registry.SanguisEntityTypes;
import com.feliscape.sanguis.registry.SanguisItems;
import com.feliscape.sanguis.registry.SanguisTags;
import com.google.common.collect.ImmutableSet;
import net.minecraft.advancements.critereon.EntityEquipmentPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static net.minecraft.world.level.storage.loot.LootPool.lootPool;
import static net.minecraft.world.level.storage.loot.LootTable.lootTable;
import static net.minecraft.world.level.storage.loot.entries.LootItem.lootTableItem;
import static net.minecraft.world.level.storage.loot.functions.SetItemCountFunction.setCount;
import static net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition.randomChance;
import static net.minecraft.world.level.storage.loot.providers.number.UniformGenerator.between;

public class SanguisEntityLootTableProvider extends EntityLootSubProvider {
    private static final Set<EntityType<?>> SPECIAL_LOOT_TABLE_TYPES = ImmutableSet.of(

    );

    private static final LootItemCondition.Builder KILLED_WITH_STAKE = killedWith(SanguisTags.Items.STAKES);


    public SanguisEntityLootTableProvider(HolderLookup.Provider registries) {
        super(FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    public void generate() {
        this.add(SanguisEntityTypes.VAMPIRE_HUNTER.get(), lootTable()
                .withPool(lootPool()
                        .add(lootTableItem(Items.EMERALD).apply(setCount(between(1.0F, 2.0F)))).when(randomChance(0.7F))
                )
                .withPool(lootPool()
                        .add(lootTableItem(SanguisItems.GARLIC).apply(setCount(between(1.3F, 4.0F)))).when(randomChance(0.5F))
                )
        );
        this.add(SanguisEntityTypes.VAMPIRE.get(), lootTable()
                .withPool(lootPool()
                        .add(lootTableItem(SanguisItems.BLOODY_FANG).apply(setCount(between(1.0F, 2.0F))))
                ).withPool(lootPool()
                        .add(lootTableItem(SanguisItems.VAMPIRE_BLOOD).when(KILLED_WITH_STAKE)))
        );
    }

    @Override
    protected boolean canHaveLootTable(EntityType<?> type) {
        return super.canHaveLootTable(type) || SPECIAL_LOOT_TABLE_TYPES.contains(type);
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return SanguisEntityTypes.ENTITY_TYPES.getEntries().stream().map(Supplier::get);
    }

    private static LootItemCondition.Builder killedWith(ItemLike item){
        return LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.ATTACKER,
                EntityPredicate.Builder.entity()
                        .equipment(EntityEquipmentPredicate.Builder.equipment()
                                .mainhand(ItemPredicate.Builder.item().of(item))));
    }
    private static LootItemCondition.Builder killedWith(TagKey<Item> item){
        return LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.ATTACKER,
                EntityPredicate.Builder.entity()
                        .equipment(EntityEquipmentPredicate.Builder.equipment()
                                .mainhand(ItemPredicate.Builder.item().of(item))));
    }
}
