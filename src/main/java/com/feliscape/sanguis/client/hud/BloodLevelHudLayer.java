package com.feliscape.sanguis.client.hud;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.attachment.VampireData;
import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public class BloodLevelHudLayer extends HudLayer{
    public static final ResourceLocation LOCATION = Sanguis.location("blood_level");

    private static final ResourceLocation BLOOD_EMPTY_SPRITE = Sanguis.location("hud/blood_empty");
    private static final ResourceLocation BLOOD_HALF_SPRITE = Sanguis.location("hud/blood_half");
    private static final ResourceLocation BLOOD_FULL_SPRITE = Sanguis.location("hud/blood_full");

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (!canRenderOverlay() || player().isSpectator()) return;

        if (VampireUtil.isVampire(player()) && this.minecraft.gameMode.hasExperience()){
            LivingEntity livingentity = this.getPlayerVehicleWithHealth();
            int vehicleMaxHearts = this.getVehicleMaxHearts(livingentity);
            if (vehicleMaxHearts > 0) return;

            int left = guiGraphics.guiWidth() / 2 + 91;
            int top = guiGraphics.guiHeight() - this.minecraft.gui.rightHeight;
            this.minecraft.gui.rightHeight += 10;

            int blood = player().getData(VampireData.type()).getBloodData().getBlood();

            for (int i = 0; i < 10; i++) {
                int x = left - i * 8 - 9;
                guiGraphics.blitSprite(BLOOD_EMPTY_SPRITE, x, top, 9, 9);
                if (i * 2 + 1 < blood) {
                    guiGraphics.blitSprite(BLOOD_FULL_SPRITE, x, top, 9, 9);
                }

                if (i * 2 + 1 == blood) {
                    guiGraphics.blitSprite(BLOOD_HALF_SPRITE, x, top, 9, 9);
                }
            }
        }
    }

    @Nullable
    private Player getCameraPlayer() {
        return this.minecraft.getCameraEntity() instanceof Player player ? player : null;
    }

    @Nullable
    private LivingEntity getPlayerVehicleWithHealth() {
        Player player = this.getCameraPlayer();
        if (player != null) {
            Entity entity = player.getVehicle();
            if (entity == null) {
                return null;
            }

            if (entity instanceof LivingEntity) {
                return (LivingEntity)entity;
            }
        }

        return null;
    }

    /**
     * Retrieves the maximum number of hearts representing the vehicle's health for the given mount entity.
     * <p>
     * @return the maximum number of hearts representing the vehicle's health, or 0 if the mount entity is null or does not show vehicle health.
     *
     * @param vehicle the living entity representing the vehicle.
     */
    private int getVehicleMaxHearts(@Nullable LivingEntity vehicle) {
        if (vehicle != null && vehicle.showVehicleHealth()) {
            float f = vehicle.getMaxHealth();
            int i = (int)(f + 0.5F) / 2;
            if (i > 30) {
                i = 30;
            }

            return i;
        } else {
            return 0;
        }
    }
}
