package com.feliscape.sanguis.client.book.chapter;

import com.feliscape.sanguis.content.quest.HunterQuest;
import com.feliscape.sanguis.content.quest.registry.QuestType;
import com.feliscape.sanguis.registry.custom.SanguisRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

public record ChapterEntryType<T extends ChapterEntry>(MapCodec<T> codec) {
}
