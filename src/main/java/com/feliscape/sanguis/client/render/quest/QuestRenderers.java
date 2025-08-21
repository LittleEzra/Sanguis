package com.feliscape.sanguis.client.render.quest;

import com.feliscape.sanguis.content.quest.HunterQuest;
import com.feliscape.sanguis.content.quest.requirement.QuestType;
import com.feliscape.sanguis.registry.custom.SanguisRegistries;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;

import java.util.Map;

public class QuestRenderers {
    private static final Map<QuestType<?>, QuestRendererProvider<?>> PROVIDERS = new Object2ObjectOpenHashMap<>();

    public static <T extends HunterQuest> void register(QuestType<T> type, QuestRendererProvider<T> provider) {
        PROVIDERS.put(type, provider);
    }

    public static Map<QuestType<?>, QuestRenderer<?>> createRenderers(Minecraft minecraft) {
        ImmutableMap.Builder<QuestType<?>, QuestRenderer<?>> builder = ImmutableMap.builder();
        PROVIDERS.forEach((type, factory) -> {
            try {
                builder.put(type, factory.create(minecraft));
            } catch (Exception exception) {
                throw new IllegalArgumentException("Failed to create renderer for " + SanguisRegistries.QUEST_REQUIREMENTS.getKey(type), exception);
            }
        });
        return builder.build();
    }
}
