package com.feliscape.sanguis.client.book.chapter;

import com.feliscape.sanguis.client.screen.GuideBookScreen;
import com.feliscape.sanguis.registry.SanguisChapterEntryTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import static com.feliscape.sanguis.client.screen.GuideBookScreen.ITEM_SCALE;

public class PlainChapterEntry extends ChapterEntry{
    public static final Component DASH = Component.literal("- ");

    public static final MapCodec<PlainChapterEntry> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ComponentSerialization.CODEC.fieldOf("title").forGetter(PlainChapterEntry::getTitle),
            ItemStack.CODEC.optionalFieldOf("icon", ItemStack.EMPTY).forGetter(PlainChapterEntry::getIcon),
            Codec.INT.optionalFieldOf("indent", 0).forGetter(PlainChapterEntry::getIndent)
    ).apply(inst, PlainChapterEntry::new));

    protected Component title;
    protected ItemStack icon;
    protected int indent;

    public PlainChapterEntry(Component title, ItemStack icon, int indent) {
        this.title = title;
        this.icon = icon;
        this.indent = indent;
    }

    public Component getTitle() {
        return title;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public int getIndent() {
        return indent;
    }

    @Override
    public ChapterEntryType<?> type() {
        return SanguisChapterEntryTypes.PLAIN.get();
    }

    @Override
    public int render(int x, int y, int i, GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        Font font = Minecraft.getInstance().font;
        ItemStack icon = this.getIcon();

        x += this.getIndent() * GuideBookScreen.INDENT_SIZE;

        if (icon.isEmpty()) {
            guiGraphics.drawString(font, DASH, x, y, 0x3f3f3f, false);

            guiGraphics.drawString(font, this.getTitle(), x + 14, y, 0x3f3f3f, false);
        } else{
            guiGraphics.drawString(font, DASH, x, y, 0x3f3f3f, false);
            x += font.width(DASH);

            PoseStack poseStack = guiGraphics.pose();

            poseStack.pushPose();
            poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
            poseStack.translate(x / ITEM_SCALE, (y - 2) / ITEM_SCALE, 0.0F);
            guiGraphics.renderItem(icon, 0, 0);
            poseStack.popPose();

            guiGraphics.drawString(font, this.getTitle(), x + 14, y, 0x3f3f3f, false);
        }
        y += 12;
        return y;
    }
}
