package com.feliscape.sanguis.content.quest.requirement;

import com.feliscape.sanguis.content.quest.HunterQuest;
import com.feliscape.sanguis.registry.custom.SanguisRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Map;
import java.util.Objects;

public record QuestType<T extends HunterQuest>(
        MapCodec<T> codec,
        StreamCodec<RegistryFriendlyByteBuf, T> streamCodec,
        QuestRequirementFactory<T> factory) {
}
