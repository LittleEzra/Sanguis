package com.feliscape.sanguis.client.render.entity;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.client.SanguisModelLayers;
import com.feliscape.sanguis.client.model.PlayerWerebatModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;

public class PlayerWerebatRenderer extends PlayerRenderer {
    private static final ResourceLocation WIDE_TEXTURE = Sanguis.location("textures/entity/werebat/wide.png");
    private static final ResourceLocation SLIM_TEXTURE = Sanguis.location("textures/entity/werebat/slim.png");

    private final boolean slim;

    public PlayerWerebatRenderer(EntityRendererProvider.Context context, boolean useSlimModel) {
        super(context, useSlimModel);
        this.slim = useSlimModel;
        this.model = new PlayerWerebatModel(
                context.bakeLayer(useSlimModel ? SanguisModelLayers.WEREBAT_SLIM : SanguisModelLayers.WEREBAT_WIDE),
                useSlimModel
        );
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractClientPlayer entity) {
        return this.slim ? SLIM_TEXTURE : WIDE_TEXTURE;
    }
}
