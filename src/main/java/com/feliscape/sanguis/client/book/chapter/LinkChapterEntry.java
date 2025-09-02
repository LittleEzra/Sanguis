package com.feliscape.sanguis.client.book.chapter;

import com.feliscape.sanguis.client.book.BookChapter;
import com.feliscape.sanguis.registry.SanguisChapterEntryTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class LinkChapterEntry extends ChapterEntry{
    public static final MapCodec<LinkChapterEntry> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ComponentSerialization.CODEC.fieldOf("title").forGetter(LinkChapterEntry::getTitle),
            ItemStack.CODEC.optionalFieldOf("icon", ItemStack.EMPTY).forGetter(LinkChapterEntry::getIcon),
            ResourceLocation.CODEC.fieldOf("location").forGetter(LinkChapterEntry::getLocation),
            Codec.INT.optionalFieldOf("indent", 0).forGetter(LinkChapterEntry::getIndent)
    ).apply(inst, LinkChapterEntry::new));

    protected Component title;
    protected ItemStack icon;
    protected ResourceLocation location;
    protected int indent;

    public LinkChapterEntry(Component title, ItemStack icon, ResourceLocation location, int indent) {
        this.title = title;
        this.icon = icon;
        this.location = location;
        this.indent = indent;
    }

    public Component getTitle() {
        return title;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public ResourceLocation getLocation() {
        return location;
    }

    public int getIndent() {
        return indent;
    }

    @Override
    public ChapterEntryType<?> type() {
        return SanguisChapterEntryTypes.LINK.get();
    }

    @Override
    public int render(int x, int y, int i, GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        return y;
    }
}
