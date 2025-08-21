package com.feliscape.sanguis.content.quest;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.quest.requirement.QuestType;
import com.feliscape.sanguis.data.loot.SanguisQuestLootTables;
import com.feliscape.sanguis.registry.SanguisQuestTypes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.ArrayList;
import java.util.List;

public class ItemQuest extends HunterQuest{
    public static final MapCodec<ItemQuest> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ItemStack.CODEC.listOf().fieldOf("items").forGetter(ItemQuest::getItems)
    ).apply(inst, ItemQuest::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemQuest> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()),
            ItemQuest::getItems,
            ItemQuest::new
    );

    private final List<ItemStack> items;

    public ItemQuest(List<ItemStack> items) {
        this.items = items;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    @Override
    public QuestType<ItemQuest> type(){
        return SanguisQuestTypes.FETCH_ITEMS.get();
    }

    @Override
    public boolean checkCompleted(Player player) {
        if (player == null) return false;

        var inventory = player.getInventory();
        ArrayList<ItemStack> itemsLeft = new ArrayList<>(items.stream().map(ItemStack::copy).toList());


        for (int j = 0; j < inventory.getContainerSize(); j++){
            ItemStack inventoryItem = inventory.getItem(j).copy();

            for (int i = 0; i < itemsLeft.size(); i++){
                ItemStack requiredItem = itemsLeft.get(i);
                if (inventoryItem.isEmpty() || !ItemStack.isSameItemSameComponents(inventoryItem, requiredItem)) {
                    continue;
                }

                if (inventoryItem.getCount() < requiredItem.getCount()){
                    requiredItem.shrink(inventoryItem.getCount());
                    break;
                }

                itemsLeft.set(i, ItemStack.EMPTY);
                if (inventoryItem.getCount() > requiredItem.getCount()){
                    inventoryItem.shrink(requiredItem.getCount());
                } else{
                    break;
                }
            }
        }

        return itemsLeft.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public void onComplete(Player player) {
        var inventory = player.getInventory();
        ArrayList<ItemStack> itemsLeft = new ArrayList<>(items.stream().map(ItemStack::copy).toList());

        for (ItemStack requiredItem : itemsLeft){
            Sanguis.LOGGER.debug("Trying to find {}", requiredItem.toString());
            for (int i = 0; i < inventory.getContainerSize(); i++){
                ItemStack inventoryItem = inventory.getItem(i);
                Sanguis.LOGGER.debug("Checking {} from Inventory", inventoryItem);
                if (inventoryItem.isEmpty() || !ItemStack.isSameItemSameComponents(inventoryItem, requiredItem)) {
                    Sanguis.LOGGER.debug("Not the same Item: will be skipped");
                    continue;
                }

                if (inventoryItem.getCount() <= requiredItem.getCount()){
                    Sanguis.LOGGER.debug("Item smaller than or equal to the required stack ({} <= {})", inventoryItem.getCount(), requiredItem.getCount());
                    requiredItem.shrink(inventoryItem.getCount());
                    inventory.removeItem(inventoryItem);
                } else{
                    Sanguis.LOGGER.debug("Item larger required stack ({} > {}), breaking out of loop", inventoryItem.getCount(), requiredItem.getCount());
                    inventoryItem.shrink(requiredItem.getCount());
                    break;
                }

                if (requiredItem.getCount() <= 0) {
                    Sanguis.LOGGER.debug("Item was found, breaking out of loop");
                    break;
                }
            }
        }
    }

    @Override
    public Component getTitle() {
        return Component.translatable("quest.sanguis.fetch_items.title")
                .withStyle(ChatFormatting.GRAY);
    }

    @Override
    public Component getName() {
        return Component.translatable("quest.sanguis.fetch_items.name",
                        Component.translatable(items.getFirst().getDescriptionId()))
                .withStyle(ChatFormatting.WHITE);
    }

    public static ItemQuest create(ServerLevel level){
        LootTable table = level.getServer().reloadableRegistries().getLootTable(SanguisQuestLootTables.ItemRequirements.HUNT_VAMPIRE);
        if (table == LootTable.EMPTY){
            Sanguis.LOGGER.warn("No loot table found");
        }

        var params = new LootParams.Builder(level).create(LootContextParamSets.EMPTY);
        return new ItemQuest(table.getRandomItems(params));
    }
}
