package com.feliscape.sanguis.content.quest;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.component.GeneratedName;
import com.feliscape.sanguis.content.component.NameGeneratorContents;
import com.feliscape.sanguis.content.quest.requirement.QuestType;
import com.feliscape.sanguis.data.loot.SanguisQuestLootTables;
import com.feliscape.sanguis.registry.SanguisQuestTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemQuest extends HunterQuest{
    public static final MapCodec<ItemQuest> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(
                    Codec.INT.fieldOf("duration").forGetter(HunterQuest::getDuration),
                    Codec.BOOL.fieldOf("completed").forGetter(HunterQuest::isCompleted),
                    ItemStack.CODEC.listOf().fieldOf("items").forGetter(ItemQuest::getItems)
    ).apply(inst, ItemQuest::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemQuest> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ItemQuest::getDuration,
            ByteBufCodecs.BOOL,
            ItemQuest::isCompleted,
            ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()),
            ItemQuest::getItems,
            ItemQuest::new
    );

    private final List<ItemStack> items;

    public ItemQuest(List<ItemStack> items) {
        this.items = items;
    }
    public ItemQuest(int duration, boolean isCompleted, List<ItemStack> items) {
        super(duration, isCompleted);
        this.items = items;
    }

    @Override
    public void tick(Player player) {
        if (player.tickCount % 5 == 0){
            this.setDirty(true);
        }
        super.tick(player);
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
            for (int i = 0; i < inventory.getContainerSize(); i++){
                ItemStack inventoryItem = inventory.getItem(i);
                if (inventoryItem.isEmpty() || !ItemStack.isSameItemSameComponents(inventoryItem, requiredItem)) {
                    continue;
                }

                if (inventoryItem.getCount() <= requiredItem.getCount()){
                    requiredItem.shrink(inventoryItem.getCount());
                    inventory.removeItem(inventoryItem);
                } else{
                    inventoryItem.shrink(requiredItem.getCount());
                    break;
                }

                if (requiredItem.getCount() <= 0) {
                    break;
                }
            }
        }
    }

    @Override
    public Component getTypeName() {
        return Component.translatable("quest.sanguis.fetch_items.name")
                .withStyle(ChatFormatting.GRAY);
    }

    @Override
    public Component getTitle() {
        return Component.translatable("quest.sanguis.fetch_items.title",
                        items.getFirst().getDisplayName())
                .withStyle(ChatFormatting.WHITE);
    }

    public static ItemQuest create(ServerLevel level){
        LootTable table = level.getServer().reloadableRegistries().getLootTable(SanguisQuestLootTables.ItemRequirements.HUNT_VAMPIRE);
        if (table == LootTable.EMPTY){
            Sanguis.LOGGER.warn("ItemQuest.create couldn't find the appropriate loot table");
        }

        var params = new LootParams.Builder(level).create(LootContextParamSets.EMPTY);
        ItemQuest itemQuest = new ItemQuest(table.getRandomItems(params));
        return itemQuest;
    }
}
