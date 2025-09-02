package com.feliscape.sanguis.client.book;

import com.feliscape.sanguis.Sanguis;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GuideBookManager {
    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    public static final Logger LOGGER = LogManager.getLogger();

    public static final ResourceLocation ROOT_LOCATION = Sanguis.location("root");

    private ChapterCollector chapterCollector;
    private EntryCollector entryCollector;

    public GuideBookManager(String directory) {
        chapterCollector = new ChapterCollector(directory + "/chapter");
        entryCollector = new EntryCollector(directory + "/entry");
    }

    public ChapterCollector getChapterCollector() {
        return chapterCollector;
    }

    public EntryCollector getEntryCollector() {
        return entryCollector;
    }

    @Nullable
    public BookEntry getEntry(ResourceLocation location){
        return entryCollector.entries.getOrDefault(location, null);
    }
    @Nullable
    public BookChapter getChapter(ResourceLocation location){
        return chapterCollector.chapters.getOrDefault(location, null);
    }
    @Nullable
    public BookChapter getChapter(int index){
        if (index < 0 || index >= chapterCollector.chapterList.size()) return null;
        return chapterCollector.chapterList.get(index);
    }
    public List<BookChapter> getAllChapters(){
        return chapterCollector.chapterList;
    }

    public static class EntryCollector extends SimpleJsonResourceReloadListener{
        protected Map<ResourceLocation, BookEntry> entries = ImmutableMap.of();

        public EntryCollector(String directory) {
            super(GSON, directory);
        }

        @Override
        protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
            ImmutableMap.Builder<ResourceLocation, BookEntry> builder = ImmutableMap.builder();
            for (Map.Entry<ResourceLocation, JsonElement> jsonEntry : object.entrySet()){
                ResourceLocation location = jsonEntry.getKey();
                JsonElement json = jsonEntry.getValue();
                try {
                    final var entry = BookEntry.CODEC.parse(JsonOps.INSTANCE, json)
                            .getOrThrow(JsonParseException::new);
                    builder.put(location, entry);
                } catch (Exception e) {
                    LOGGER.error("Failed to load book entry {}", location, e);
                }
            }
            entries = builder.build();
            LOGGER.debug("Loaded {} book entries", entries.size());
        }
    }
    public static class ChapterCollector extends SimpleJsonResourceReloadListener{
        protected Map<ResourceLocation, BookChapter> chapters = ImmutableMap.of();
        protected List<BookChapter> chapterList = List.of();

        public ChapterCollector(String directory) {
            super(GSON, directory);
        }

        @Override
        protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
            ImmutableMap.Builder<ResourceLocation, BookChapter> builder = ImmutableMap.builder();
            for (Map.Entry<ResourceLocation, JsonElement> jsonEntry : object.entrySet()){
                ResourceLocation location = jsonEntry.getKey();
                JsonElement json = jsonEntry.getValue();
                try {
                    final var entry = BookChapter.CODEC.parse(JsonOps.INSTANCE, json)
                            .getOrThrow(JsonParseException::new);
                    builder.put(location, entry);
                } catch (Exception e) {
                    LOGGER.error("Failed to load book chapter {}", location, e);
                }
            }
            chapters = builder.build();
            chapterList = new ArrayList<>(chapters.values());
            LOGGER.debug("Loaded {} book chapters", chapters.size());
        }
    }
}
