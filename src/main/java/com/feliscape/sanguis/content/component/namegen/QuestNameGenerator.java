package com.feliscape.sanguis.content.component.namegen;

import com.feliscape.sanguis.content.quest.HunterQuest;
import com.feliscape.sanguis.content.quest.requirement.QuestType;
import net.minecraft.network.chat.Component;

public abstract class QuestNameGenerator {
    public abstract String language();

    public abstract String generateName(QuestType<?> type, long seed);
}
