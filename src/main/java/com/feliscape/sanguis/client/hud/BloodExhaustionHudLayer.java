package com.feliscape.sanguis.client.hud;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.attachment.VampireData;
import com.feliscape.sanguis.util.VampireUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;

public class BloodExhaustionHudLayer extends HudLayer{
    public static final ResourceLocation LOCATION = Sanguis.location("blood_exhaustion");

    private static final ResourceLocation EXHAUSTION_SPRITE = Sanguis.location("hud/blood/exhaustion");

    @Override
    public void renderOverlay(GuiGraphics guiGraphics, DeltaTracker deltaTracker, LocalPlayer player) {
        float exhaustion = Math.min(player.getData(VampireData.type()).getBloodData().getExhaustion(), 4.0F);
        float ratio = Math.min(1, Math.max(0, exhaustion / 4.0F));
        int right = guiGraphics.guiWidth() / 2 + 91;
        int top = guiGraphics.guiHeight() - this.minecraft.gui.rightHeight;
        int width = (int)(ratio * 81);
        int height = 9;

        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.75F);
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        guiGraphics.blitSprite(EXHAUSTION_SPRITE, 81, 9, 81 - width, 0, right - width, top, width, height);

        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

    }

    @Override
    public boolean canRenderOverlay(LocalPlayer player) {
        return VampireUtil.isVampire(player) && this.minecraft.gameMode.hasExperience() && ModList.get().isLoaded("appleskin");
    }
}
