package com.feliscape.sanguis.client.screen;

import com.feliscape.sanguis.content.menu.QuestBoardMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

public class QuestBoardScreen extends AbstractContainerScreen<QuestBoardMenu> {
    private List<QuestChoiceComponent> choices = List.of(
            new QuestChoiceComponent(),
            new QuestChoiceComponent(),
            new QuestChoiceComponent()
    );

    @Override
    protected void init() {
        this.imageWidth = 0;
        this.imageHeight = 0;
        super.init();
        this.titleLabelX = 10000;
        this.inventoryLabelY = 10000;

        for (int i = 0; i < choices.size(); i++){
            choices.get(i).init(this.width, this.height, this.minecraft, this, i, this.menu.blockEntity.getQuest(i));
            this.addWidget(choices.get(i));
        }
    }

    public QuestBoardScreen(QuestBoardMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (QuestChoiceComponent choice : choices){
            if (choice.mouseClicked(mouseX, mouseY, button)){
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        for (QuestChoiceComponent choice : choices){
            choice.setActive(!this.menu.blockEntity.hasChosen());
            choice.render(guiGraphics, mouseX, mouseY, partialTick);
        }
    }
}
