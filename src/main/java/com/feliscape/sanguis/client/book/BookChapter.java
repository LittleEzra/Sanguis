package com.feliscape.sanguis.client.book;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.client.book.chapter.ChapterEntry;
import com.feliscape.sanguis.client.book.chapter.ChapterEntryType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

public class BookChapter {
    public static final ResourceLocation EMPTY = Sanguis.location("empty");
    public static final Codec<BookChapter> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ComponentSerialization.CODEC.fieldOf("title").forGetter(BookChapter::getTitle),
            ItemStack.CODEC.optionalFieldOf("icon", ItemStack.EMPTY).forGetter(BookChapter::getIcon),
            ResourceLocation.CODEC.lenientOptionalFieldOf("art").forGetter(BookChapter::getArt),
            ChapterEntry.TYPED_CODEC.listOf().fieldOf("entries").forGetter(BookChapter::getEntries)
    ).apply(inst, BookChapter::new));

    private Component title;
    private ItemStack icon;
    private Optional<ResourceLocation> art;
    private List<ChapterEntry> entries;

    public BookChapter(Component title, ItemStack icon, Optional<ResourceLocation> art, List<ChapterEntry> entries) {
        this.title = title;
        this.icon = icon;
        this.art = art;
        this.entries = entries;
    }

    public Component getTitle() {
        return title;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public Optional<ResourceLocation> getArt() {
        return art;
    }

    public List<ChapterEntry> getEntries(){
        return entries;
    }
}
