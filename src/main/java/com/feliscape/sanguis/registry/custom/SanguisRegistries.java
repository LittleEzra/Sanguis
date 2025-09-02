package com.feliscape.sanguis.registry.custom;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.client.book.chapter.ChapterEntryType;
import com.feliscape.sanguis.content.quest.registry.QuestType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

@EventBusSubscriber(modid = Sanguis.MOD_ID)
public class SanguisRegistries {
    public static final Registry<QuestType<?>> QUEST_TYPES = new RegistryBuilder<>(Keys.QUEST_TYPES)
            .sync(true)
            .create();
    public static final Registry<ChapterEntryType<?>> CHAPTER_ENTRY_TYPES = new RegistryBuilder<>(Keys.CHAPTER_ENTRY_TYPES)
            .create();

    @SubscribeEvent
    static void registerRegistries(NewRegistryEvent event){
        event.register(QUEST_TYPES);
        event.register(CHAPTER_ENTRY_TYPES);
    }

    public static class Keys{

        public static final ResourceKey<Registry<QuestType<?>>> QUEST_TYPES = ResourceKey.createRegistryKey(Sanguis.location("quest_types"));
        public static final ResourceKey<Registry<ChapterEntryType<?>>> CHAPTER_ENTRY_TYPES = ResourceKey.createRegistryKey(Sanguis.location("chapter_entry_types"));
    }
}
