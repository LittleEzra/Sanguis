package com.feliscape.sanguis.content.quest.registry;

import com.feliscape.sanguis.content.quest.HunterQuest;
import com.feliscape.sanguis.registry.custom.SanguisRegistries;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public final class QuestType<T extends HunterQuest> {
    private final MapCodec<T> codec;
    private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;
    private final QuestFactory<T> factory;
    private final Holder<QuestType<?>> holder = SanguisRegistries.QUEST_TYPES.wrapAsHolder(this);

    public QuestType(
            MapCodec<T> codec,
            StreamCodec<RegistryFriendlyByteBuf, T> streamCodec,
            QuestFactory<T> factory) {
        this.codec = codec;
        this.streamCodec = streamCodec;
        this.factory = factory;
    }

    public MapCodec<T> codec() {
        return codec;
    }

    public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
        return streamCodec;
    }

    public QuestFactory<T> factory() {
        return factory;
    }

    public Holder<QuestType<?>> builtInRegistryHolder() {
        return holder;
    }

    @Override
    public String toString() {
        return "QuestType{" + SanguisRegistries.QUEST_TYPES.wrapAsHolder(this).getRegisteredName() + "}";
    }

}
