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
import java.util.List;
import java.util.Map;

public class GuideBookManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    public static final Logger LOGGER = LogManager.getLogger();

    public static final ResourceLocation ROOT_LOCATION = Sanguis.location("root");

    private Map<ResourceLocation, BookEntry> entries = ImmutableMap.of();

    public GuideBookManager(String directory) {
        super(GSON, directory);
    }

    @Nullable
    public BookEntry getEntry(ResourceLocation location){
        return entries.getOrDefault(location, null);
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
                LOGGER.error("Failed to load animation {}", location, e);
            }
        }
        entries = builder.build();
        LOGGER.debug("Loaded {} book entries", entries.size());
    }
}
