package com.feliscape.sanguis.content.component.namegen;

import com.feliscape.sanguis.content.quest.registry.QuestType;
import net.minecraft.util.RandomSource;

import java.util.List;

public class EnUsQuestNameGenerator extends QuestNameGenerator{
    @Override
    public String language() {
        return "en_us";
    }

    private final List<String> prepositions = List.of(
            "Under",
            "Above",
            "Through",
            "Beneath",
            "On",
            "Below",
            "Within"
    );
    private final List<String> adjectives = List.of(
            "Red",
            "Blue",
            "Dark",
            "Sunburnt",
            "Feared",
            "Horrid",
            "Terrible",
            "Hidden",
            "Ominous",
            "Tall"
    );
    private final List<String> objects = List.of(
            "Moon",
            "Sky",
            "Stars",
            "Night",
            "Fire",
            "Forest",
            "Cavern",
            "Tower",
            "Castle",
            "Camp"
    );

    @Override
    public String generateName(QuestType<?> type, long seed) {
        RandomSource random = RandomSource.create(seed);

        String start = prepositions.get(random.nextInt(prepositions.size()));
        String adjective = adjectives.get(random.nextInt(prepositions.size()));
        String noun = objects.get(random.nextInt(prepositions.size()));

        return "%s the %s %s".formatted(start, adjective, noun);
    }
}
