package com.feliscape.sanguis.content.attachment;

import com.feliscape.sanguis.content.quest.HunterQuest;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HunterQuestData {
    private final List<HunterQuest> activeQuests;
    @Nullable
    protected Player player;

    public static final StreamCodec<RegistryFriendlyByteBuf, HunterQuestData> STREAM_CODEC = StreamCodec.composite(
            HunterQuest.TYPED_STREAM_CODEC.apply(ByteBufCodecs.list()),
            HunterQuestData::getActiveQuests,
            HunterQuestData::new
    );

    public HunterQuestData(){
        activeQuests = new ArrayList<>();
    }
    public HunterQuestData(List<HunterQuest> quests){
        activeQuests = quests;
    }

    public void add(HunterQuest hunterQuest) {
        this.activeQuests.add(hunterQuest);
    }

    public void complete(int index) {
        if (player == null) return;

        HunterQuest quest = this.getQuest(index);
        quest.complete(player);
        this.activeQuests.remove(index);
        player.syncData(HunterData.type());
    }
    public void cancel(int index){
        if (player == null) return;

        this.activeQuests.remove(index);
        player.syncData(HunterData.type());
    }
    public void tick(){
        if (this.player == null){
            return;
        }

        if (this.player.tickCount % 5 == 0){
            for (HunterQuest quest : activeQuests){
                quest.updateIsCompleted(player);
            }
            this.player.syncData(HunterData.type());
        }
    }

    public List<HunterQuest> getActiveQuests() {
        return activeQuests;
    }

    public HunterQuest getQuest(int index) {
        return this.activeQuests.get(index);
    }

    public static CompoundTag save(HunterQuestData data){
        CompoundTag compound = new CompoundTag();

        ListTag list = new ListTag();
        for (HunterQuest quest : data.activeQuests){
            list.add(HunterQuest.TYPED_CODEC.encodeStart(NbtOps.INSTANCE, quest).getOrThrow());
        }
        compound.put("quests", list);

        return compound;
    }

    public static HunterQuestData load(CompoundTag tag){
        ArrayList<HunterQuest> quests = new ArrayList<>();
        ListTag list = tag.getList("quests", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++){
            quests.add(HunterQuest.TYPED_CODEC.decode(NbtOps.INSTANCE, list.get(i)).getOrThrow().getFirst());
        }
        return new HunterQuestData(quests);
    }
    @Nullable
    public HunterQuest getQuestOrNull(int index) {
        if (index < 0 || index >= this.activeQuests.size()) return null;
        return this.activeQuests.get(index);
    }

    public void setPlayer(@Nullable Player player) {
        this.player = player;
    }
    public void setPlayer(@Nullable Entity entity) {
        if (entity instanceof Player player1) {
            this.setPlayer(player1);
        }
        else{
            this.player = null;
        }
    }
}
