package com.feliscape.sanguis.client.render.quest;

import com.feliscape.sanguis.content.quest.ItemQuest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ItemQuestRenderer extends QuestRenderer<ItemQuest> {
    public ItemQuestRenderer(Minecraft minecraft) {
        super(minecraft);
    }

    @Override
    public void render(ItemQuest quest, GuiGraphics guiGraphics, int x, int y, int mouseX, int mouseY, boolean canRenderTooltip) {
        List<ItemStack> items = quest.getItems();
        for (int i = 0; i < items.size(); i++){
            int itemX = x + (i * 18);
            ItemStack itemStack = items.get(i);
            guiGraphics.renderItem(itemStack, itemX, y);
            guiGraphics.renderItemDecorations(minecraft.font, itemStack, itemX, y);
            if (isHovering(itemX, y, 16, 16, mouseX, mouseY)) {
                guiGraphics.renderTooltip(minecraft.font, itemStack, mouseX, mouseY);
            }
        }
    }
}
