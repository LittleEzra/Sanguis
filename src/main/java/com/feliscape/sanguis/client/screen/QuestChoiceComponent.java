package com.feliscape.sanguis.client.screen;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.SanguisClient;
import com.feliscape.sanguis.content.quest.HunterQuest;
import com.feliscape.sanguis.networking.payload.ServerboundCancelQuestPayload;
import com.feliscape.sanguis.networking.payload.ServerboundChooseQuestPayload;
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

public class QuestChoiceComponent implements Renderable, GuiEventListener, NarratableEntry {
    protected static final ResourceLocation QUEST_INFORMATION_LOCATION = Sanguis.location("textures/gui/container/quest_information.png");

    protected Minecraft minecraft;
    protected QuestBoardScreen screen;
    private int width;
    private int height;
    private boolean focused;
    private boolean active;
    private int index;
    private AcceptButton acceptButton;
    private HunterQuest quest;

    public boolean isActive() {
        return this.active;
    }

    public void init(int width, int height, Minecraft minecraft, QuestBoardScreen screen, int index, HunterQuest quest) {
        this.minecraft = minecraft;
        this.width = width;
        this.height = height;
        this.screen = screen;
        this.active = true;
        this.index = index;
        this.quest = quest;
        initVisuals();
    }

    public void initVisuals() {
        int x = this.getX();
        int y = this.getY();
        acceptButton = new AcceptButton(x + 58, y + 130, 31, 22);
    }

    public int getX(){
        int offsetFromCenter = this.index - 1;
        return (this.width - 147) / 2 + (147 + 10) * offsetFromCenter;
    }
    public int getY(){
        return (this.height - 166) / 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int i = this.getX();
        int j = this.getY();
        if (!active) RenderSystem.setShaderColor(0.5F, 0.5F, 0.5F, 1.0F);
        guiGraphics.blit(QUEST_INFORMATION_LOCATION, i, j, 0, 0, 147, 166);
        renderContents(guiGraphics, mouseX, mouseY, partialTick, true);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, boolean canRenderTooltip) {
        int x = this.getX() + 10;
        int y = this.getY() + 10;
        guiGraphics.drawString(this.minecraft.font, quest.getTitle(), x, y, -1);
        y += 11;
        guiGraphics.drawString(this.minecraft.font, quest.getTypeName(), x, y, -1);
        y += 13;
        SanguisClient.reloadListeners().getQuestRenderDispatcher().
                render(quest, x, y, mouseX, mouseY, canRenderTooltip, guiGraphics);

        if (active)
            acceptButton.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.isActive() || this.minecraft.player.isSpectator()) {
            return false;
        }

        return this.acceptButton.mouseClicked(mouseX, mouseY, button);
    }

    protected void setActive(boolean active) {
        if (active) {
            this.initVisuals();
        }

        this.active = active;
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
    public NarrationPriority narrationPriority() {
        return NarrationPriority.HOVERED;
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {

    }

    public class AcceptButton extends AbstractButton {
        private static final ResourceLocation BUTTON_HIGHLIGHTED_SPRITE = Sanguis.location("container/quests/accept_button_highlighted");
        private static final ResourceLocation BUTTON_SPRITE = Sanguis.location("container/quests/accept_button");

        public AcceptButton(int x, int y, int width, int height) {
            this(x, y, width, height, Component.translatable("container.sanguis.active_quests.complete"));
        }
        public AcceptButton(int x, int y, int width, int height, Component message) {
            super(x, y, width, height, message);
            this.setTooltip(Tooltip.create(message));
        }

        @Override
        public void onPress() {
            if (!QuestChoiceComponent.this.isActive()) return;

            PacketDistributor.sendToServer(new ServerboundChooseQuestPayload(
                    QuestChoiceComponent.this.screen.getMenu().containerId,
                    QuestChoiceComponent.this.index));
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            ResourceLocation resourcelocation;
            if (this.isHoveredOrFocused()) {
                resourcelocation = AcceptButton.BUTTON_HIGHLIGHTED_SPRITE;
            } else {
                resourcelocation = AcceptButton.BUTTON_SPRITE;
            }

            guiGraphics.blitSprite(resourcelocation, this.getX(), this.getY(), this.width, this.height);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            this.defaultButtonNarrationText(narrationElementOutput);
        }
    }
}
