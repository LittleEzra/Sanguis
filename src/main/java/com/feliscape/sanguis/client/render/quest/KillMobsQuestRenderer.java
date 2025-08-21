package com.feliscape.sanguis.client.render.quest;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.quest.KillMobsQuest;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;

public class KillMobsQuestRenderer extends QuestRenderer<KillMobsQuest> {
    private static final ResourceLocation PROGRESS_BACKGROUND = Sanguis.location("container/quests/progress_bar_background");
    private static final ResourceLocation PROGRESS_FILL = Sanguis.location("container/quests/progress_bar_fill");

    public KillMobsQuestRenderer(Minecraft minecraft) {
        super(minecraft);
    }

    @Override
    public void render(KillMobsQuest quest, GuiGraphics guiGraphics, int x, int y, int mouseX, int mouseY, boolean canRenderTooltip) {
        var requiredKills = new ArrayList<>(quest.getRequiredKills().entrySet());

        for (int i = 0; i < requiredKills.size(); i++){
            var entry = requiredKills.get(i);
            int offsetY = y + i * (5 + 9 + 3);

            Component component = Component.translatable(entry.getKey().getDescriptionId()).withStyle(ChatFormatting.WHITE)
                    .append(Component.literal(" x%s".formatted(entry.getValue()))).withStyle(ChatFormatting.GRAY);

            guiGraphics.drawString(this.minecraft.font, component, x, offsetY, -1);
            guiGraphics.blitSprite(PROGRESS_BACKGROUND, x, offsetY + 12, 121, 5);
            if (quest.getKills().containsKey(entry.getKey())){
                int fill = (int) (((float) quest.getKills().get(entry.getKey()) / entry.getValue()) * 121.0F);
                guiGraphics.blitSprite(PROGRESS_FILL,
                        121, 5,
                        0, 0,
                        x, offsetY + 12,
                        fill, 5);
            }
        }
    }
}
