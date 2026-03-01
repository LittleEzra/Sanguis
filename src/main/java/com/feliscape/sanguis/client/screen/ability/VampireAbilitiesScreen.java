package com.feliscape.sanguis.client.screen.ability;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.SanguisClient;
import com.feliscape.sanguis.content.ability.VampireAbility;
import com.feliscape.sanguis.content.menu.VampireAbilitiesMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;
import java.util.Optional;

public class VampireAbilitiesScreen extends AbstractContainerScreen<VampireAbilitiesMenu> {
    private static final ResourceLocation BACKGROUND_LOCATION = Sanguis.location("textures/gui/container/vampire_abilities.png");

    public VampireAbilitiesScreen(VampireAbilitiesMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {

    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(BACKGROUND_LOCATION, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        selectedAbility = -1;
        renderUnobtainedAbilities(guiGraphics, mouseX, mouseY, this.menu.getUnobtainedAbilities());
        renderObtainedAbilities(guiGraphics, mouseX, mouseY, this.menu.getObtainedAbilities());

        int cost = this.menu.getCurrentCost();
        int maxCost = this.menu.getMaxCost();
        String costString = "Cost: " + cost + "/" + maxCost;

        guiGraphics.drawString(this.font, costString,
                this.leftPos + (this.imageWidth / 2) - this.font.width(costString) / 2,
                this.topPos + 48, (cost > maxCost) ? 0xff0a0a : 0x404040, false);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);
        if (this.menu.getCarried().isEmpty() && selectedAbility > -1) {
            guiGraphics.renderTooltip(font,
                    getAbilityTooltip(getSelectedAbility(selectedAbility, !hasSelectedUnobtainedAbility)),
                    Optional.empty(),
                    x, y);
        }
    }

    private VampireAbility getSelectedAbility(int index, boolean obtained){
        var list = (obtained ? menu.getObtainedAbilities() : menu.getUnobtainedAbilities());
        return list.get(index);
    }

    public static List<Component> getAbilityTooltip(VampireAbility ability){
        return List.of(
                ability.getTranslation(),
                ability.getDescription()
        );
    }

    public static void renderAbility(VampireAbility ability, GuiGraphics guiGraphics, int x, int y){
        TextureAtlasSprite sprite = SanguisClient.reloadListeners().getVampireAbilityTextureManager().get(ability);
        guiGraphics.blit(x, y, 0, 16, 16, sprite);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0){
            if (selectedAbility > -1){
                this.menu.moveAbility(selectedAbility, hasSelectedUnobtainedAbility);
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    int selectedAbility = -1;
    boolean hasSelectedUnobtainedAbility;

    private void renderUnobtainedAbilities(GuiGraphics guiGraphics, int mouseX, int mouseY, List<VampireAbility> abilities) {
        int startX = this.leftPos + 8;
        int startY = this.topPos + 9;
        for (int i = 0; i < abilities.size(); i++){
            var ability = abilities.get(i);
            int x = startX + (i % 9) * (16 + 2);
            int y = startY + (i / 9) * (16 + 2);

            if (mouseX >= x - 1 && mouseY >= y - 1 && mouseX < x + 17 && mouseY < y + 17){ // When hovered
                y -= 1;
                selectedAbility = i;
                hasSelectedUnobtainedAbility = true;
            }

            renderAbility(ability, guiGraphics, x, y);
        }
    }
    private void renderObtainedAbilities(GuiGraphics guiGraphics, int mouseX, int mouseY, List<VampireAbility> abilities) {
        int startX = this.leftPos + 8;
        int startY = this.topPos + 60;
        for (int i = 0; i < abilities.size(); i++){
            var ability = abilities.get(i);
            int x = startX + i * (16 + 2);
            int y = startY;

            if (mouseX >= x - 1 && mouseY >= y - 1 && mouseX < x + 17 && mouseY < y + 17){ // When hovered
                y -= 1;
                selectedAbility = i;
                hasSelectedUnobtainedAbility = false;
            }

            renderAbility(ability, guiGraphics, x, y);
        }
    }
}
