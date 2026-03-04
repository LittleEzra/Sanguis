package com.feliscape.sanguis.client.model;

import com.feliscape.sanguis.client.render.entity.PlayerWerebatRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public class SanguisUniqueEntityRenderers implements ResourceManagerReloadListener {

    private PlayerWerebatRenderer playerWerebatRendererWide;
    private PlayerWerebatRenderer playerWerebatRendererSlim;

    public PlayerWerebatRenderer getWerebatRenderer(boolean slim){
        return slim ? playerWerebatRendererSlim : playerWerebatRendererWide;
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        Minecraft minecraft = Minecraft.getInstance();
        var entityRenderDispatcher = minecraft.getEntityRenderDispatcher();
        EntityRendererProvider.Context context = new EntityRendererProvider.Context(
                entityRenderDispatcher,
                minecraft.getItemRenderer(),
                minecraft.getBlockRenderer(),
                entityRenderDispatcher.getItemInHandRenderer(),
                resourceManager,
                minecraft.getEntityModels(),
                minecraft.font);
        playerWerebatRendererWide = new PlayerWerebatRenderer(context, false);
        playerWerebatRendererSlim = new PlayerWerebatRenderer(context, true);
    }
}
