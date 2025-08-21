package com.feliscape.sanguis.client.render.quest;

import com.feliscape.sanguis.content.quest.HunterQuest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public abstract class QuestRenderer<T extends HunterQuest> {
    protected final Minecraft minecraft;

    public QuestRenderer(Minecraft minecraft){
        this.minecraft = minecraft;
    }

    public abstract void render(T quest, GuiGraphics guiGraphics, int x, int y, int mouseX, int mouseY, boolean canRenderTooltip);

    protected boolean isHovering(int x, int y, int width, int height, int mouseX, int mouseY) {
        return (mouseX > x && x + width > mouseX) && (mouseY > y && y + height > mouseY);
    }
}
