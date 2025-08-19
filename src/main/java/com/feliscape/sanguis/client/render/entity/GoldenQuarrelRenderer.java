package com.feliscape.sanguis.client.render.entity;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.entity.projectile.GoldenQuarrel;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class GoldenQuarrelRenderer extends ArrowRenderer<GoldenQuarrel> {
    private static final ResourceLocation TEXTURE = Sanguis.location("textures/entity/projectile/golden_quarrel.png");

    public GoldenQuarrelRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(GoldenQuarrel pEntity) {
        return TEXTURE;
    }
}
