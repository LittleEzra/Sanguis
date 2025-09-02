package com.feliscape.sanguis.client.book.chapter;

import com.feliscape.sanguis.content.quest.HunterQuest;
import com.feliscape.sanguis.content.quest.registry.QuestType;
import com.feliscape.sanguis.registry.custom.SanguisRegistries;
import com.mojang.serialization.Codec;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.Supplier;

public abstract class ChapterEntry {
    public static final Codec<ChapterEntry> TYPED_CODEC = SanguisRegistries.CHAPTER_ENTRY_TYPES
            .byNameCodec()
            .dispatch("type", ChapterEntry::type, ChapterEntryType::codec);

    public abstract ChapterEntryType<?> type();

    /**
     *
     * @return The new y coordinate
     */
    public abstract int render(int x, int y, int i, GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY);
}
