package com.feliscape.sanguis.registry;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.client.book.chapter.ChapterEntryType;
import com.feliscape.sanguis.client.book.chapter.LinkChapterEntry;
import com.feliscape.sanguis.client.book.chapter.PlainChapterEntry;
import com.feliscape.sanguis.registry.custom.SanguisRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SanguisChapterEntryTypes {
    public static final DeferredRegister<ChapterEntryType<?>> CHAPTER_ENTRY_TYPES =
            DeferredRegister.create(SanguisRegistries.CHAPTER_ENTRY_TYPES, Sanguis.MOD_ID);

    public static final Supplier<ChapterEntryType<LinkChapterEntry>> LINK = CHAPTER_ENTRY_TYPES.register("link",
            () -> new ChapterEntryType<>(LinkChapterEntry.CODEC));
    public static final Supplier<ChapterEntryType<PlainChapterEntry>> PLAIN = CHAPTER_ENTRY_TYPES.register("plain",
            () -> new ChapterEntryType<>(PlainChapterEntry.CODEC));

    public static void register(IEventBus eventBus){
        CHAPTER_ENTRY_TYPES.register(eventBus);
    }
}
