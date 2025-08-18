package com.feliscape.sanguis.client.render.entity;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.client.SanguisModelLayers;
import com.feliscape.sanguis.client.model.VampireModel;
import com.feliscape.sanguis.content.entity.VampireEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

public class VampireRenderer extends HumanoidMobRenderer<VampireEntity, VampireModel> {
    private static final ResourceLocation TEXTURE = Sanguis.location("textures/entity/vampire/vampire.png");

    public VampireRenderer(EntityRendererProvider.Context context) {
        super(context, new VampireModel(context.bakeLayer(SanguisModelLayers.VAMPIRE)), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this,
                new VampireModel(context.bakeLayer(SanguisModelLayers.VAMPIRE_ARMOR_OUTER)),
                new VampireModel(context.bakeLayer(SanguisModelLayers.VAMPIRE_ARMOR_INNER)),
                context.getModelManager()));
    }

    @Override
    public ResourceLocation getTextureLocation(VampireEntity entity) {
        return TEXTURE;
    }

    @Override
    protected boolean isShaking(VampireEntity entity) {
        return entity.isShaking();
    }
}
