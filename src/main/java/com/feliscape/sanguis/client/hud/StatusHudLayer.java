package com.feliscape.sanguis.client.hud;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.attachment.VampireData;
import com.feliscape.sanguis.util.HunterUtil;
import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;

public class StatusHudLayer extends HudLayer {
    public static final ResourceLocation LOCATION = Sanguis.location("vampire_status");
    private static final ResourceLocation INFECTED_SPRITE = Sanguis.location("hud/status/infected");
    private static final ResourceLocation VAMPIRE_SPRITE = Sanguis.location("hud/status/vampire");
    private static final ResourceLocation BAT_SPRITE = Sanguis.location("hud/status/bat");
    private static final ResourceLocation HUNTER_SPRITE = Sanguis.location("hud/status/hunter");

    @Override
    public void renderOverlay(GuiGraphics guiGraphics, DeltaTracker deltaTracker, LocalPlayer player) {
        ResourceLocation location = null;

        if (VampireUtil.isInfected(player())) location = INFECTED_SPRITE;
        else if (VampireUtil.isBat(player())) location = BAT_SPRITE;
        else if (VampireUtil.isVampire(player())) location = VAMPIRE_SPRITE;
        else if (HunterUtil.isHunter(player())) location = HUNTER_SPRITE;

        if (location == null) return;

        int x = (guiGraphics.guiWidth() - 19) / 2;
        int y = guiGraphics.guiHeight() - 36 - getHeight(player());

        guiGraphics.blitSprite(location, x , y, 19, 15);

        if (VampireUtil.isVampire(player())){
            int tier = player().getData(VampireData.type()).getTier();
            if (tier > 0) {
                String s = (tier) + "";
                int j = (guiGraphics.guiWidth() - this.minecraft.font.width(s)) / 2;
                int k = guiGraphics.guiHeight() - 31 - getLevelHeight(player());
                guiGraphics.drawString(this.minecraft.font, s, j + 1, k, 0, false);
                guiGraphics.drawString(this.minecraft.font, s, j - 1, k, 0, false);
                guiGraphics.drawString(this.minecraft.font, s, j, k + 1, 0, false);
                guiGraphics.drawString(this.minecraft.font, s, j, k - 1, 0, false);
                guiGraphics.drawString(this.minecraft.font, s, j, k, 0xb9214d, false);
            }
        }
    }

    private int getHeight(LocalPlayer player) {
        if (!isAnyBarVisible()) return -2;

        int i = player.experienceLevel;
        if (i == 0){
            return 4;
        }
        int y = 7;

        if (Integer.toString(i).length() > 2){
            y += 2;
        }
        return y;
    }
    private int getLevelHeight(LocalPlayer player) {
        if (!isAnyBarVisible()) return -2;

        int i = player.experienceLevel;
        if (i == 0){
            return 4;
        }
        return 14;
    }

    @SuppressWarnings("DataFlowIssue")
    private boolean isAnyBarVisible() {
        return this.minecraft.gameMode.hasExperience();
    }
    private boolean isExperienceBarVisible() {
        return this.minecraft.player.jumpableVehicle() == null && this.minecraft.gameMode.hasExperience();
    }
}
