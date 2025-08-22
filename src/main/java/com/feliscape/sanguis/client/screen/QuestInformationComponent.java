package com.feliscape.sanguis.client.screen;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.SanguisClient;
import com.feliscape.sanguis.content.quest.HunterQuest;
import com.feliscape.sanguis.networking.payload.ServerboundCancelQuestPayload;
import com.feliscape.sanguis.networking.payload.ServerboundCompleteQuestPayload;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;

public class QuestInformationComponent implements Renderable, GuiEventListener, NarratableEntry {
    protected static final ResourceLocation QUEST_INFORMATION_LOCATION = Sanguis.location("textures/gui/container/quest_information.png");
    private boolean visible;
    private int xOffset;
    private int width;
    private int height;
    private boolean widthTooNarrow;
    protected Minecraft minecraft;
    protected ActiveQuestsScreen screen;
    private boolean focused;

    private CompleteButton completeButton;
    private CancelButton cancelButton;

    public boolean isVisible() {
        return this.visible;
    }

    public void init(int width, int height, Minecraft minecraft, boolean widthTooNarrow, ActiveQuestsScreen screen) {
        this.minecraft = minecraft;
        this.width = width;
        this.height = height;
        this.widthTooNarrow = widthTooNarrow;
        this.visible = false;
        this.screen = screen;
    }

    public void initVisuals() {
        this.xOffset = this.widthTooNarrow ? 0 : 86;
        int x = this.getX();
        int y = this.getY();
        completeButton = new CompleteButton(x + 123, y + 142, 14, 14);
        cancelButton = new CancelButton(x + 104, y + 142, 14, 14);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (this.isVisible()) {
            int i = (this.width - 147) / 2 - this.xOffset;
            int j = (this.height - 166) / 2;
            guiGraphics.blit(QUEST_INFORMATION_LOCATION, i, j, 0, 0, 147, 166);
        }
    }

    public void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, boolean canRenderTooltip, HunterQuest quest) {
        int x = this.getX() + 10;
        int y = this.getY() + 10;
        guiGraphics.drawString(this.minecraft.font, quest.getTitle(), x, y, -1);
        y += 11;
        guiGraphics.drawString(this.minecraft.font, quest.getTypeName(), x, y, -1);
        y += 13;
        SanguisClient.reloadListeners().getQuestRenderDispatcher().
                render(quest, x, y, mouseX, mouseY, canRenderTooltip, guiGraphics);

        completeButton.active = quest.isCompleted();
        completeButton.render(guiGraphics, mouseX, mouseY, partialTick);
        cancelButton.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    public int updateScreenPosition(int width, int imageWidth) {
        int i;
        if (this.isVisible() && !this.widthTooNarrow) {
            i = 177 + (width - imageWidth - 200) / 2;
        } else {
            i = (width - imageWidth) / 2;
        }

        return i;
    }

    public int getX(){
        return (this.width - 147) / 2 - this.xOffset;
    }
    public int getY(){
        return (this.height - 166) / 2;
    }

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    @Override
    public boolean isFocused() {
        return focused;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.isVisible() || this.minecraft.player.isSpectator()) {
            return false;
        }

        if (this.completeButton.mouseClicked(mouseX, mouseY, button)){
            return true;
        } else if (this.cancelButton.mouseClicked(mouseX, mouseY, button)){
            return true;
        }

        return false;
    }

    @Override
    public NarrationPriority narrationPriority() {
        return NarratableEntry.NarrationPriority.HOVERED;
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {

    }

    public void setVisible(boolean visible) {
        if (visible) {
            this.initVisuals();
        }

        this.visible = visible;
    }

    public abstract static class QuestButton extends AbstractButton{
        private static final ResourceLocation BUTTON_DISABLED_SPRITE = Sanguis.location("container/quests/button_disabled");
        private static final ResourceLocation BUTTON_HIGHLIGHTED_SPRITE = Sanguis.location("container/quests/button_highlighted");
        private static final ResourceLocation BUTTON_SPRITE = Sanguis.location("container/quests/button");

        public QuestButton(int x, int y, int width, int height, Component message) {
            super(x, y, width, height, message);
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            ResourceLocation resourcelocation;
            if (!this.active) {
                resourcelocation = QuestButton.BUTTON_DISABLED_SPRITE;
            } else if (this.isHoveredOrFocused()) {
                resourcelocation = QuestButton.BUTTON_HIGHLIGHTED_SPRITE;
            } else {
                resourcelocation = QuestButton.BUTTON_SPRITE;
            }

            guiGraphics.blitSprite(resourcelocation, this.getX(), this.getY(), this.width, this.height);
            this.renderIcon(guiGraphics);
        }

        protected abstract void renderIcon(GuiGraphics guiGraphics);
    }

    public class CompleteButton extends QuestButton {
        private static final ResourceLocation ICON = Sanguis.location("container/quests/complete");

        public CompleteButton(int x, int y, int width, int height) {
            super(x, y, width, height, Component.translatable("container.sanguis.active_quests.complete"));
        }
        public CompleteButton(int x, int y, int width, int height, Component message) {
            super(x, y, width, height, message);
            this.setTooltip(Tooltip.create(message));
        }

        @Override
        public void onPress() {
            PacketDistributor.sendToServer(new ServerboundCompleteQuestPayload(
                    QuestInformationComponent.this.screen.getMenu().containerId,
                    QuestInformationComponent.this.screen.selectedIndex));
            QuestInformationComponent.this.screen.selectedIndex = -1;
        }

        protected void renderIcon(GuiGraphics guiGraphics) {
            guiGraphics.blitSprite(ICON, this.getX(), this.getY(), 14, 14);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            this.defaultButtonNarrationText(narrationElementOutput);
        }
    }
    public class CancelButton extends QuestButton {
        private static final ResourceLocation ICON = Sanguis.location("container/quests/cancel");

        public CancelButton(int x, int y, int width, int height) {
            super(x, y, width, height, Component.translatable("container.sanguis.active_quests.cancel"));
        }
        public CancelButton(int x, int y, int width, int height, Component message) {
            super(x, y, width, height, message);
            this.setTooltip(Tooltip.create(message));
        }

        @Override
        public void onPress() {
            PacketDistributor.sendToServer(new ServerboundCancelQuestPayload(
                    QuestInformationComponent.this.screen.getMenu().containerId,
                    QuestInformationComponent.this.screen.selectedIndex));
        }

        protected void renderIcon(GuiGraphics guiGraphics) {
            guiGraphics.blitSprite(ICON, this.getX(), this.getY(), 14, 14);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            this.defaultButtonNarrationText(narrationElementOutput);
        }
    }
}
