package com.feliscape.sanguis.client.hud;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.SanguisServerConfig;
import com.feliscape.sanguis.content.attachment.EntityBloodData;
import com.feliscape.sanguis.registry.SanguisTags;
import com.feliscape.sanguis.util.VampireUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class DrainBarHudLayer extends HudLayer{
    public static final ResourceLocation LOCATION = Sanguis.location("drain_bar");

    private static final ResourceLocation DRAIN_BAR_BACKGROUND = Sanguis.location("hud/crosshair/drain_bar");
    private static final ResourceLocation DRAIN_BAR_FILL = Sanguis.location("hud/crosshair/drain_bar_fill");
    private static final ResourceLocation DRAIN_BAR_FOUL = Sanguis.location("hud/crosshair/drain_bar_foul");

    @Override
    public void renderOverlay(GuiGraphics guiGraphics, DeltaTracker deltaTracker, LocalPlayer player) {
        int x = (guiGraphics.guiWidth() - 9) / 2;
        int y = (guiGraphics.guiHeight() - 6) / 2 + 10;

        if (this.minecraft.options.attackIndicator().get() == AttackIndicatorStatus.CROSSHAIR){
            float f = player().getAttackStrengthScale(0.0F);
            boolean flag = false;
            if (this.minecraft.crosshairPickEntity != null && this.minecraft.crosshairPickEntity instanceof LivingEntity && f >= 1.0F) {
                flag = player().getCurrentItemAttackStrengthDelay() > 5.0F;
                flag &= this.minecraft.crosshairPickEntity.isAlive();
            }
            if (flag || f < 1.0F){
                y += 12;
            }
        }

        if (isFoul()){
            guiGraphics.blitSprite(DRAIN_BAR_FOUL,
                    9, 6,
                    0, 0,
                    x, y,
                    9, 6);
            return;
        }

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

    @Override
    public boolean canRenderOverlay(LocalPlayer player) {
        return VampireUtil.isVampire(player);
    }

    private EntityBloodData getEntityBlood() {
        if (this.minecraft.crosshairPickEntity == null) return null;

        if (player().distanceTo(this.minecraft.crosshairPickEntity) <= SanguisServerConfig.CONFIG.vampireDrainDistance.getAsDouble()
                && EntityBloodData.canHaveBlood(this.minecraft.crosshairPickEntity)) {
            return this.minecraft.crosshairPickEntity.getData(EntityBloodData.type());
        }

        return null;
    }
    private boolean isFoul() {
        if (this.minecraft.crosshairPickEntity == null) return false;

        return player().distanceTo(this.minecraft.crosshairPickEntity) <= SanguisServerConfig.CONFIG.vampireDrainDistance.getAsDouble()
                && VampireUtil.hasFoulBlood(this.minecraft.crosshairPickEntity);
    }
}
