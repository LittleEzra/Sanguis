package com.feliscape.sanguis.client.book.widget;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;

public record WidgetType<T extends BookWidget>(ResourceLocation location, MapCodec<T> codec) {
}
