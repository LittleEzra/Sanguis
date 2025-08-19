package com.feliscape.sanguis.client.render.entity;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.client.SanguisModelLayers;
import com.feliscape.sanguis.client.model.VampireHunterModel;
import com.feliscape.sanguis.content.entity.living.VampireHunter;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class VampireHunterRenderer extends MobRenderer<VampireHunter, VampireHunterModel> {
    private static final ResourceLocation TEXTURE = Sanguis.location("textures/entity/hunter/vampire_hunter.png");

    public VampireHunterRenderer(EntityRendererProvider.Context context) {
        super(context, new VampireHunterModel(context.bakeLayer(SanguisModelLayers.VAMPIRE_HUNTER)), 0.5F);
        this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    protected void scale(VampireHunter entity, PoseStack poseStack, float partialTickTime) {
        float f = 0.9375F;
        poseStack.scale(0.9375F, 0.9375F, 0.9375F);
    }

    @Override
    public ResourceLocation getTextureLocation(VampireHunter entity) {
        return TEXTURE;
    }
}
