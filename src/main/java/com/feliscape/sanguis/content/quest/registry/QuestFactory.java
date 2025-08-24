package com.feliscape.sanguis.content.quest.registry;

import com.feliscape.sanguis.content.quest.HunterQuest;
import net.minecraft.server.level.ServerLevel;

public interface QuestFactory<T extends HunterQuest> {
    T create(ServerLevel level);
}
