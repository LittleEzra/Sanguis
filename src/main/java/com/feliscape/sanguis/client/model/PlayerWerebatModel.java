package com.feliscape.sanguis.client.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class PlayerWerebatModel extends PlayerModel<AbstractClientPlayer> {
    ModelPart leftWing;
    ModelPart rightWing;
    ModelPart leftEar;
    ModelPart rightEar;

    public PlayerWerebatModel(ModelPart root, boolean slim) {
        super(root, slim);
        this.leftWing = this.body.getChild("left_wing");
        this.rightWing = this.body.getChild("right_wing");
        this.leftEar = this.head.getChild("left_ear");
        this.rightEar = this.head.getChild("right_ear");
    }

    public static LayerDefinition createBodyLayer(boolean slim) {
        return LayerDefinition.create(createMesh(slim), 128, 64);
    }
    public static MeshDefinition createMesh(boolean slim){
        MeshDefinition baseMesh = PlayerModel.createMesh(CubeDeformation.NONE, slim);
        PartDefinition root = baseMesh.getRoot();
        PartDefinition body = root.getChild("body");
        PartDefinition head = root.getChild("head");


        head.addOrReplaceChild("right_ear", CubeListBuilder.create()
                        .texOffs(64, 0)
                        .addBox(-2.0F, -4.5F, 0.0F, 4.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-4.0F, -6.5F, 0.0F));

        head.addOrReplaceChild("left_ear", CubeListBuilder.create()
                        .texOffs(64, 0).mirror()
                        .addBox(-2.0F, -4.5F, 0.0F, 4.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offset(4.0F, -6.5F, 0.0F));


        body.addOrReplaceChild("right_wing", CubeListBuilder.create()
                        .texOffs(56, 16).mirror()
                        .addBox(-18.0F, -2.0F, 0.0F, 18.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offset(0.0F, 2.0F, 2.1F));

        body.addOrReplaceChild("left_wing", CubeListBuilder.create()
                        .texOffs(56, 16)
                        .addBox(0.0F, -2.0F, 0.0F, 18.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 2.0F, 2.1F));

        return baseMesh;
    }

    @Override
    public void setupAnim(AbstractClientPlayer entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        leftWing.zRot = -Mth.sin(ageInTicks * 0.1F) * 0.1F - 0.5F;
        leftWing.yRot = -0.2F;
        rightWing.zRot = -leftWing.zRot;
        rightWing.yRot = -leftWing.yRot;
    }
}
