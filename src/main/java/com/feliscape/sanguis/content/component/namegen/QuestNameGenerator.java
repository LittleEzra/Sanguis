package com.feliscape.sanguis.content.component.namegen;

import com.feliscape.sanguis.content.quest.registry.QuestType;

public abstract class QuestNameGenerator {
    public abstract String language();

    public abstract String generateName(QuestType<?> type, long seed);
}
