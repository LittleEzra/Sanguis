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
    public static final Codec<HunterQuest> TYPED_CODEC = SanguisRegistries.QUEST_REQUIREMENTS
            .byNameCodec()
            .dispatch("requirement", HunterQuest::type, QuestType::codec);
    public static final StreamCodec<RegistryFriendlyByteBuf, HunterQuest> TYPED_STREAM_CODEC = ByteBufCodecs.registry(SanguisRegistries.Keys.QUEST_REQUIREMENTS)
            .dispatch(HunterQuest::type, QuestType::streamCodec);
    protected boolean isCompleted = false;

    public void complete(Player player){
        if (!checkCompleted(player)) return;

        onComplete(player);
        // TODO: add rewards
    }

    public abstract QuestType<? extends HunterQuest> type();

    public boolean isCompleted(){
        return isCompleted;
    }

    public abstract boolean checkCompleted(Player player);

    public abstract void onComplete(Player player);

    public abstract Component getTitle();
    public abstract Component getName();

    public void updateIsCompleted(Player player) {
        this.isCompleted = checkCompleted(player);
    }
}
