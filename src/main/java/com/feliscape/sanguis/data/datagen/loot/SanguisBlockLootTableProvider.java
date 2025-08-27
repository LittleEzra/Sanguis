package com.feliscape.sanguis.data.datagen.loot;

import com.feliscape.sanguis.content.block.CoffinBlock;
import com.feliscape.sanguis.content.block.GarlicCropBlock;
import com.feliscape.sanguis.registry.SanguisBlocks;
import com.feliscape.sanguis.registry.SanguisItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarrotBlock;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Set;

import static net.minecraft.world.level.storage.loot.functions.ApplyBonusCount.addBonusBinomialDistributionCount;

public class SanguisBlockLootTableProvider extends BlockLootSubProvider {

    protected SanguisBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        HolderLookup.RegistryLookup<Enchantment> enchantmentLookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

        this.dropOther(SanguisBlocks.QUEST_BOARD.get(), Blocks.STRIPPED_OAK_LOG);
        this.dropSelf(SanguisBlocks.GARLIC_STRING.get());

        this.add(SanguisBlocks.WHITE_COFFIN.get(), block -> this.createSinglePropConditionTable(block, CoffinBlock.PART, BedPart.HEAD));
        this.add(SanguisBlocks.LIGHT_GRAY_COFFIN.get(), block -> this.createSinglePropConditionTable(block, CoffinBlock.PART, BedPart.HEAD));
        this.add(SanguisBlocks.GRAY_COFFIN.get(), block -> this.createSinglePropConditionTable(block, CoffinBlock.PART, BedPart.HEAD));
        this.add(SanguisBlocks.BLACK_COFFIN.get(), block -> this.createSinglePropConditionTable(block, CoffinBlock.PART, BedPart.HEAD));
        this.add(SanguisBlocks.BROWN_COFFIN.get(), block -> this.createSinglePropConditionTable(block, CoffinBlock.PART, BedPart.HEAD));
        this.add(SanguisBlocks.RED_COFFIN.get(), block -> this.createSinglePropConditionTable(block, CoffinBlock.PART, BedPart.HEAD));
        this.add(SanguisBlocks.ORANGE_COFFIN.get(), block -> this.createSinglePropConditionTable(block, CoffinBlock.PART, BedPart.HEAD));
        this.add(SanguisBlocks.YELLOW_COFFIN.get(), block -> this.createSinglePropConditionTable(block, CoffinBlock.PART, BedPart.HEAD));
        this.add(SanguisBlocks.LIME_COFFIN.get(), block -> this.createSinglePropConditionTable(block, CoffinBlock.PART, BedPart.HEAD));
        this.add(SanguisBlocks.GREEN_COFFIN.get(), block -> this.createSinglePropConditionTable(block, CoffinBlock.PART, BedPart.HEAD));
        this.add(SanguisBlocks.CYAN_COFFIN.get(), block -> this.createSinglePropConditionTable(block, CoffinBlock.PART, BedPart.HEAD));
        this.add(SanguisBlocks.LIGHT_BLUE_COFFIN.get(), block -> this.createSinglePropConditionTable(block, CoffinBlock.PART, BedPart.HEAD));
        this.add(SanguisBlocks.BLUE_COFFIN.get(), block -> this.createSinglePropConditionTable(block, CoffinBlock.PART, BedPart.HEAD));
        this.add(SanguisBlocks.PURPLE_COFFIN.get(), block -> this.createSinglePropConditionTable(block, CoffinBlock.PART, BedPart.HEAD));
        this.add(SanguisBlocks.MAGENTA_COFFIN.get(), block -> this.createSinglePropConditionTable(block, CoffinBlock.PART, BedPart.HEAD));
        this.add(SanguisBlocks.PINK_COFFIN.get(), block -> this.createSinglePropConditionTable(block, CoffinBlock.PART, BedPart.HEAD));

        LootItemCondition.Builder garlicAgeCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(SanguisBlocks.GARLIC.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(GarlicCropBlock.AGE, 4));
        this.add(
                SanguisBlocks.GARLIC.get(),
                block -> this.applyExplosionDecay(
                        block,
                        LootTable.lootTable()
                                .withPool(LootPool.lootPool().add(LootItem.lootTableItem(SanguisItems.GARLIC)))
                                .withPool(LootPool.lootPool().when(garlicAgeCondition).add(
                                        LootItem.lootTableItem(SanguisItems.GARLIC)
                                                .apply(addBonusBinomialDistributionCount(enchantmentLookup.getOrThrow(Enchantments.FORTUNE),
                                                        0.5714286F, 3))
                                        )
                                )
                                .withPool(LootPool.lootPool().when(garlicAgeCondition).add(
                                        LootItem.lootTableItem(SanguisItems.GARLIC_FLOWER)
                                                .apply(addBonusBinomialDistributionCount(enchantmentLookup.getOrThrow(Enchantments.FORTUNE),
                                                        0.4F, 2))
                                        )
                                )
                )
        );
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return SanguisBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
