package com.feliscape.sanguis.client.screen.ability;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.attachment.VampireAbilityData;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public class VampireAbilitiesScreen extends Screen {
    private static final ResourceLocation WINDOW_LOCATION = Sanguis.location("textures/gui/abilities/window.png");
    private static final Component TITLE = Component.translatable("gui.sanguis.vampire_abilities");

    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    @Nullable
    private final Screen lastScreen;
    private final VampireAbilityData abilities;

    protected VampireAbilitiesScreen(VampireAbilityData data) {
        this(data, null);
    }
    protected VampireAbilitiesScreen(VampireAbilityData data, @Nullable Screen lastScreen) {
        super(TITLE);
        this.abilities = data;
        this.lastScreen = lastScreen;
    }

    @Override
    protected void init() {
        this.layout.addTitleHeader(TITLE, this.font);
        this.layout.addToFooter(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose()).width(200).build());
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.lastScreen);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        int i = (this.width - 252) / 2;
        int j = (this.height - 140) / 2;
        this.renderInside(guiGraphics, mouseX, mouseY, i, j);
        this.renderWindow(guiGraphics, i, j);
    }
    private void renderInside(GuiGraphics guiGraphics, int mouseX, int mouseY, int offsetX, int offsetY) {
        guiGraphics.fill(offsetX + 9, offsetY + 18, offsetX + 9 + 174, offsetY + 18 + 146, 0xff575757);
        guiGraphics.enableScissor(offsetX + 9, offsetY + 18, offsetX + 9 + 174, offsetY + 18 + 146);
        guiGraphics.disableScissor();
    }
    public void renderWindow(GuiGraphics guiGraphics, int offsetX, int offsetY) {
        RenderSystem.enableBlend();
        guiGraphics.blit(WINDOW_LOCATION, offsetX, offsetY, 0, 0, 188, 183);


        //guiGraphics.drawString(this.font, this.selectedTab != null ? this.selectedTab.getTitle() : TITLE, offsetX + 8, offsetY + 6, 4210752, false);
    }
}
