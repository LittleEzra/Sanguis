package com.feliscape.sanguis.client.hud;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.SanguisServerConfig;
import com.feliscape.sanguis.content.attachment.EntityBloodData;
import com.feliscape.sanguis.util.VampireUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class DrainBarHudLayer extends HudLayer{
    public static final ResourceLocation LOCATION = Sanguis.location("drain_bar");

    private static final ResourceLocation DRAIN_BAR_BACKGROUND = Sanguis.location("hud/crosshair/drain_bar");
    private static final ResourceLocation DRAIN_BAR_FILL = Sanguis.location("hud/crosshair/drain_bar_fill");

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (!canRenderOverlay() || player().isSpectator()) return;

        if (VampireUtil.isVampire(player())){
            EntityBloodData blood = getEntityBlood();
            if (blood == null){
                return;
            }
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(
                    GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO
            );

            int x = (guiGraphics.guiWidth() - 9) / 2;
            int y = (guiGraphics.guiHeight() - 6) / 2 + 10;
            int fill = (int) (((float) blood.getRemainingBlood() / blood.getMaxBlood()) * 6.0F);

            guiGraphics.blitSprite(DRAIN_BAR_BACKGROUND,
                    9, 6,
                    0, 0,
                    x, y,
                    9, (6 - fill));
            guiGraphics.blitSprite(DRAIN_BAR_FILL,
                    9, 6,
                    0, (6 - fill),
                    x, y + (6 - fill),
                    9, fill);

            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
        }
    }

    private EntityBloodData getEntityBlood() {
        if (this.minecraft.hitResult == null) return null;

        if (this.minecraft.hitResult.getType() == HitResult.Type.ENTITY){
            var result = ((EntityHitResult) this.minecraft.hitResult);
            if (player().distanceTo(result.getEntity()) <= SanguisServerConfig.CONFIG.vampireDrainDistance.getAsDouble()
                    && EntityBloodData.canHaveBlood(result.getEntity())){
                return result.getEntity().getData(EntityBloodData.type());
            }
        }

        return null;
    }
}
