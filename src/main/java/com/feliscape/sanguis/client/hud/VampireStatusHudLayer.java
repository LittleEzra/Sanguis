package com.feliscape.sanguis.client.hud;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;

public class VampireStatusHudLayer extends HudLayer {
    public static final ResourceLocation LOCATION = Sanguis.location("vampire_status");
    private static final ResourceLocation FANGS_SPRITE = Sanguis.location("hud/fangs");

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (!canRenderOverlay() || player().isSpectator()) return;

        if (VampireUtil.isVampire(player())){
            int x = (guiGraphics.guiWidth() - 15) / 2;
            int y = guiGraphics.guiHeight() - 31 - getHeight(player());

            guiGraphics.blitSprite(FANGS_SPRITE, x , y, 15, 10);
        }
    }

    private int getHeight(LocalPlayer player) {
        if (!isExperienceBarVisible()) return -2;

        int i = player.experienceLevel;
        if (i == 0){
            return  4;
        }
        int y = 7;

        if (Integer.toString(i).length() > 2){
            y += 2;
        }
        return y;
    }

    @SuppressWarnings("DataFlowIssue")
    private boolean isExperienceBarVisible() {
        return this.minecraft.player.jumpableVehicle() == null && this.minecraft.gameMode.hasExperience();
    }
}
