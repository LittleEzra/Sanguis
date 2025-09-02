package com.feliscape.sanguis.content.quest;

import com.feliscape.sanguis.content.quest.registry.QuestType;
import com.feliscape.sanguis.data.loot.SanguisQuestLootTables;
import com.feliscape.sanguis.registry.SanguisCriteriaTriggers;
import com.feliscape.sanguis.registry.custom.SanguisRegistries;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.HolderSet;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public abstract class HunterQuest {
    public static final Codec<HunterQuest> TYPED_CODEC = SanguisRegistries.QUEST_TYPES
            .byNameCodec()
            .dispatch("requirement", HunterQuest::type, QuestType::codec);
    public static final StreamCodec<RegistryFriendlyByteBuf, HunterQuest> TYPED_STREAM_CODEC = ByteBufCodecs.registry(SanguisRegistries.Keys.QUEST_TYPES)
            .dispatch(HunterQuest::type, QuestType::streamCodec);

    protected HunterQuest(){
    }
    protected HunterQuest(int duration){
        this.duration = duration;
    }
    protected HunterQuest(int duration, boolean isCompleted){
        this.duration = duration;
        this.isCompleted = isCompleted;
    }

    protected boolean isCompleted = false;
    protected boolean isDirty = false;
    protected int duration = 60 * 60 * 20; // 60 Minutes = 3 days

    public static Tag serialize(HunterQuest quest) {
        return TYPED_CODEC.encodeStart(NbtOps.INSTANCE, quest).getOrThrow();
    }

    public void complete(Player player){
        if (!checkCompleted(player)) return;

        onComplete(player);
        if (player.level() instanceof ServerLevel serverLevel){
            LootTable loot = serverLevel.getServer().reloadableRegistries().getLootTable(SanguisQuestLootTables.BASIC_REWARD);
            ObjectArrayList<ItemStack> items = loot.getRandomItems(new LootParams.Builder(serverLevel)
                    .withParameter(LootContextParams.ORIGIN, player.position())
                    .withParameter(LootContextParams.THIS_ENTITY, player)
                    .create(LootContextParamSets.GIFT));
            for (ItemStack itemStack : items){
                if (!player.getInventory().add(itemStack)){
                    player.drop(itemStack, true);
                }
            }
        }
        if (player instanceof ServerPlayer serverPlayer){
            SanguisCriteriaTriggers.QUEST_COMPLETED.get().trigger(serverPlayer, this);
        }
    }

    public void tick(Player player){
        if (duration > 0) {
            this.duration--;
        }

        if (this.isDirty()){
            this.updateIsCompleted(player);
        }
    }

    public abstract QuestType<? extends HunterQuest> type();

    public boolean isCompleted(){
        return isCompleted;
    }

    public void setDirty(boolean dirty){
        this.isDirty = dirty;
    }
    public boolean isDirty(){
        return isDirty;
    }
    public boolean isInfiniteDuration(){
        return duration == -1;
    }
    public void makeInfinite(){
        this.duration = -1;
    }

    public int getDuration() {
        return duration;
    }

    public abstract boolean checkCompleted(Player player);

    public abstract void onComplete(Player player);

    public abstract Component getTypeName();
    public abstract Component getTitle();

    public void updateIsCompleted(Player player) {
        this.isCompleted = checkCompleted(player);
    }

    public boolean is(HolderSet<QuestType<?>> holders) {
        return holders.contains(type().builtInRegistryHolder());
    }
}
