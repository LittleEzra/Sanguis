package com.feliscape.sanguis.client.render.quest;

import com.feliscape.sanguis.content.quest.HunterQuest;
import net.minecraft.client.Minecraft;

@FunctionalInterface
public interface QuestRendererProvider<T extends HunterQuest> {
    QuestRenderer<? super T> create(Minecraft minecraft);
}
