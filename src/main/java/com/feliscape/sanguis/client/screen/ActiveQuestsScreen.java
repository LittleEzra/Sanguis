package com.feliscape.sanguis.client.screen;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.menu.ActiveQuestsMenu;
import com.feliscape.sanguis.content.quest.HunterQuest;
import com.feliscape.sanguis.util.QuestUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.Nullable;

public class ActiveQuestsScreen extends AbstractContainerScreen<ActiveQuestsMenu> {
    private static final ResourceLocation BACKGROUND_LOCATION = Sanguis.location("textures/gui/container/active_quests.png");

    private static final ResourceLocation QUEST_BACKGROUND_UNSELECTED = Sanguis.location("container/quests/quest_background_unselected");
    private static final ResourceLocation QUEST_BACKGROUND_SELECTED = Sanguis.location("container/quests/quest_background_selected");
    int scrollOffset;
    int selectedIndex = -1;
    private final QuestInformationComponent questInformation = new QuestInformationComponent();
    private boolean widthTooNarrow;

    public ActiveQuestsScreen(ActiveQuestsMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        questInformation.setVisible(false);
    }

    @Override
    protected void init() {
        this.imageWidth = 176;
        this.imageHeight = 212;
        super.init();
        this.titleLabelX = 10000;
        this.inventoryLabelY = this.imageHeight - 94;
        this.leftPos = this.questInformation.updateScreenPosition(this.width, this.imageWidth);

        this.widthTooNarrow = this.width < 379;
        this.questInformation.init(this.width, this.height, this.minecraft, widthTooNarrow, this);
        this.addWidget(this.questInformation);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(BACKGROUND_LOCATION, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (menu.data == null) return false;
        scrollY = Mth.clamp(scrollY, -1.0, 1.0);
        if (scrollY != 0.0D){
            scrollOffset = Mth.clamp(scrollOffset + Mth.ceil(Math.abs(scrollY)) * -Mth.sign(scrollY),
                    0, this.menu.data.getActiveQuests().size() - 4);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.menu.data == null)
            return super.mouseClicked(mouseX, mouseY, button);

        if (this.questInformation.mouseClicked(mouseX, mouseY, button)){
            this.questInformation.setFocused(true);
            return true;
        } /*else if (isHoveringAbsolute(this.questInformation.getX(), this.questInformation.getY(), 147, 166, mouseX, mouseY)){
            return false;
        }*/

        selectedIndex = -1;

        for (int i = 0; i < 3; i++){
            if (isHovering(16, 13 + (i * 25), 144, 25, mouseX, mouseY)){
                int index = i + scrollOffset;
                if (index < this.menu.data.getActiveQuests().size()){
                    selectedIndex = index;
                    break;
                }
            }
        }
        questInformation.setVisible(selectedIndex >= 0);
        this.leftPos = questInformation.updateScreenPosition(this.width, this.imageWidth);

        return super.mouseClicked(mouseX, mouseY, button);
    }

    protected boolean isHoveringAbsolute(int x, int y, int width, int height, double mouseX, double mouseY) {
        return mouseX >= (double)(x - 1)
                && mouseX < (double)(x + width + 1)
                && mouseY >= (double)(y - 1)
                && mouseY < (double)(y + height + 1);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        if (menu.data == null) return;
        var data = menu.getData();

        questInformation.render(guiGraphics, mouseX, mouseY, partialTick);
        if (selectedIndex >= 0 && selectedIndex < data.getActiveQuests().size()){
            questInformation.renderContents(guiGraphics, mouseX, mouseY, partialTick, this.menu.getCarried().isEmpty(), data.getActiveQuests().get(selectedIndex));

        }

        renderQuest(guiGraphics, scrollOffset, mouseX, mouseY, data.getQuestOrNull(scrollOffset));
        renderQuest(guiGraphics, scrollOffset + 1, mouseX, mouseY, data.getQuestOrNull(scrollOffset + 1));
        renderQuest(guiGraphics, scrollOffset + 2, mouseX, mouseY, data.getQuestOrNull(scrollOffset + 2));
        renderQuest(guiGraphics, scrollOffset + 3, mouseX, mouseY, data.getQuestOrNull(scrollOffset + 3));
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private void renderQuest(GuiGraphics guiGraphics, int index, int mouseX, int mouseY, @Nullable HunterQuest quest) {
        if (quest == null) return;

        int x = this.leftPos + 16;
        int y = this.topPos + 13 + ((index - scrollOffset) * 25);
        guiGraphics.blitSprite(index == selectedIndex ? QUEST_BACKGROUND_SELECTED : QUEST_BACKGROUND_UNSELECTED,
                x, y, 144, 25);

        guiGraphics.drawString(this.font, quest.getTitle(), x + 2, y + 2, -1);

        @SuppressWarnings("DataFlowIssue")
        var time = QuestUtil.formatDuration(quest, 1.0F, this.minecraft.level.tickRateManager().tickrate());

        guiGraphics.drawString(this.font, time, x + 2, y + 2 + 9, 8355711);

    }
}
