package com.feliscape.sanguis.client.render.entity;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.client.SanguisModelLayers;
import com.feliscape.sanguis.client.model.PlayerWerebatModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.neoforged.neoforge.client.ClientHooks;

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

    public void renderRightHand(PoseStack poseStack, MultiBufferSource buffer, int combinedLight, AbstractClientPlayer player) {
        if (!ClientHooks.renderSpecificFirstPersonArm(poseStack, buffer, combinedLight, player, HumanoidArm.RIGHT)) {
            this.renderHand(poseStack, buffer, combinedLight, player, this.model.rightArm, this.model.rightSleeve);
        }

    }

    public void renderLeftHand(PoseStack poseStack, MultiBufferSource buffer, int combinedLight, AbstractClientPlayer player) {
        if (!ClientHooks.renderSpecificFirstPersonArm(poseStack, buffer, combinedLight, player, HumanoidArm.LEFT)) {
            this.renderHand(poseStack, buffer, combinedLight, player, this.model.leftArm, this.model.leftSleeve);
        }

    }

    private void renderHand(PoseStack poseStack, MultiBufferSource buffer, int combinedLight, AbstractClientPlayer player, ModelPart rendererArm, ModelPart rendererArmwear) {
        PlayerModel<AbstractClientPlayer> playermodel = this.getModel();
        this.setModelProperties(player);
        playermodel.attackTime = 0.0F;
        playermodel.crouching = false;
        playermodel.swimAmount = 0.0F;
        playermodel.setupAnim(player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        rendererArm.xRot = 0.0F;
        ResourceLocation texture = getTextureLocation(player);
        rendererArm.render(poseStack, buffer.getBuffer(RenderType.entitySolid(texture)), combinedLight, OverlayTexture.NO_OVERLAY);
        rendererArmwear.xRot = 0.0F;
        rendererArmwear.render(poseStack, buffer.getBuffer(RenderType.entityTranslucent(texture)), combinedLight, OverlayTexture.NO_OVERLAY);
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractClientPlayer entity) {
        return this.slim ? SLIM_TEXTURE : WIDE_TEXTURE;
    }
}
