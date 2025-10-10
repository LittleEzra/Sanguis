package com.feliscape.sanguis.client.hud;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.attachment.VampireData;
import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.fml.ModList;

public class BloodSaturationHudLayer extends HudLayer{
    public static final ResourceLocation LOCATION = Sanguis.location("blood_saturation");

    private static final ResourceLocation BLOOD_OUTLINE_SPRITE = Sanguis.location("hud/blood/blood_outline");

    @Override
    public void renderOverlay(GuiGraphics guiGraphics, DeltaTracker deltaTracker, LocalPlayer player) {
        float saturation = player.getData(VampireData.type()).getBloodData().getSaturation();
        int endSaturation = (int) Math.ceil(saturation / 2.0F);
        int right = guiGraphics.guiWidth() / 2 + 91;
        int top = guiGraphics.guiHeight() - this.minecraft.gui.rightHeight + 10;
        for (int i = 0; i < endSaturation; i++){
            int x = right - i * 8 - 9;
            float iconSaturation = (saturation / 2.0F) - i;
            int size = Mth.ceil(9 * Math.clamp(iconSaturation, 0.0F, 1.0F));
            guiGraphics.blitSprite(BLOOD_OUTLINE_SPRITE,
                    9, 9,
                    0, 9 - size,
                    x, top + (9 - size),
                    9, size);
        }
    }

    @Override
    public boolean canRenderOverlay(LocalPlayer player) {
        return VampireUtil.isVampire(player) && this.minecraft.gameMode.hasExperience() && ModList.get().isLoaded("appleskin");
    }
}
