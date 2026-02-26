package com.feliscape.sanguis.client.screen.ability;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.SanguisClient;
import com.feliscape.sanguis.content.ability.VampireAbility;
import com.feliscape.sanguis.content.menu.VampireAbilitiesMenu;
import com.feliscape.sanguis.registry.custom.SanguisRegistries;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

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

        var obtainedAbilities = this.menu.getAbilities().getObtainedAbilities();
        renderUnobtainedAbilities(guiGraphics, mouseX, mouseY, obtainedAbilities);
        renderObtainedAbilities(guiGraphics, mouseX, mouseY, obtainedAbilities);

        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private void renderUnobtainedAbilities(GuiGraphics guiGraphics, int mouseX, int mouseY, List<VampireAbility> obtainedAbilities) {
        if (this.minecraft == null || this.minecraft.getConnection() == null) return;
        var allAbilityHolders = this.minecraft.getConnection().registryAccess()
                .registryOrThrow(SanguisRegistries.Keys.VAMPIRE_ABILITIES).holders();
        var allAbilities = allAbilityHolders.map(Holder::value).filter(a -> !obtainedAbilities.contains(a)).toList();

        int startX = this.leftPos + 8;
        int startY = this.topPos + 9;
        for (int i = 0; i < allAbilities.size(); i++){
            var ability = allAbilities.get(i);
            int x = startX + (i % 9) * (16 + 2);
            int y = startY + (i / 9) * (16 + 2);
            if (mouseX >= x && mouseY >= y && mouseX < x + 16 && mouseY < y + 16){ // When hovered
                y -= 1;
            }

            TextureAtlasSprite sprite = SanguisClient.reloadListeners().getVampireAbilityTextureManager().get(ability);
            guiGraphics.blit(x, y, 0, 16, 16, sprite);
        }
    }
    private void renderObtainedAbilities(GuiGraphics guiGraphics, int mouseX, int mouseY, List<VampireAbility> obtainedAbilities) {
        int startX = this.leftPos + 8;
        int startY = this.topPos + 58;
        for (int i = 0; i < obtainedAbilities.size(); i++){
            var ability = obtainedAbilities.get(i);
            int x = startX + i * (16 + 2);

            TextureAtlasSprite sprite = SanguisClient.reloadListeners().getVampireAbilityTextureManager().get(ability);
            guiGraphics.blit(x, startY, 0, 16, 16, sprite);
        }
    }
}
