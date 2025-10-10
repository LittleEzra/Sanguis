package com.feliscape.sanguis.client.hud;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public abstract class HudLayer implements LayeredDraw.Layer {
    protected final Minecraft minecraft = Minecraft.getInstance();

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (canRenderAnyOverlay() && canRenderOverlay(player()) && (!player().isSpectator() || canRenderInSpectator())){
            renderOverlay(guiGraphics, deltaTracker, player());
        }
    }

    protected abstract void renderOverlay(GuiGraphics guiGraphics, DeltaTracker deltaTracker, LocalPlayer player);

    public boolean canRenderOverlay(LocalPlayer player){
        return true;
    }
    protected boolean canRenderAnyOverlay(){
        return this.minecraft.player != null && this.minecraft.player.isAlive() && !this.minecraft.options.hideGui;
    }
    protected boolean canRenderInSpectator(){
        return false;
    }

    public LocalPlayer player(){
        return this.minecraft.player;
    }
}
