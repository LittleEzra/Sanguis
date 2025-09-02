package com.feliscape.sanguis.client.registry;

import com.feliscape.sanguis.client.book.widget.WidgetType;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.concurrent.Immutable;
import javax.swing.text.html.Option;
import java.util.Map;
import java.util.Optional;

public class WidgetTypeRegistry {
    public static final Codec<WidgetType<?>> CODEC = ResourceLocation.CODEC.comapFlatMap(
            location -> getType(location)
                    .map(DataResult::success)
                    .orElseGet(() -> DataResult.error(() -> "Unknown registry key in WidgetTypeRegistry: " + location)),
            t -> t.location()
    );

    private static final Map<ResourceLocation, WidgetType<?>> registry = Maps.newHashMap();

    public static void register(ResourceLocation location, WidgetType<?> type){
        if (registry.containsKey(location)){
            throw new IllegalStateException("Duplicate widget type registration: " + location);
        }
        registry.put(location, type);
    }

    public static Optional<WidgetType<?>> getType(ResourceLocation location){
        return Optional.ofNullable(registry.get(location));
    }
}
