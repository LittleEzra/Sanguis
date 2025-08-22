package com.feliscape.sanguis.content.quest;

import com.feliscape.sanguis.content.quest.requirement.QuestType;
import com.feliscape.sanguis.registry.custom.SanguisRegistries;
import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;

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

    public void complete(Player player){
        if (!checkCompleted(player)) return;

        onComplete(player);
        // TODO: add rewards
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
}
