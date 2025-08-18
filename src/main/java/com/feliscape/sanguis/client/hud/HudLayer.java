package com.feliscape.sanguis.client.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.NotNull;

public abstract class HudLayer implements LayeredDraw.Layer {
    protected final Minecraft minecraft = Minecraft.getInstance();

    public boolean canRenderOverlay(){
        return this.minecraft.player != null && this.minecraft.player.isAlive() && !this.minecraft.options.hideGui;
    }

    public LocalPlayer player(){
        return this.minecraft.player;
    }
}
