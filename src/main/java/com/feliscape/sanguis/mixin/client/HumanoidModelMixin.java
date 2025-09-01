package com.feliscape.sanguis.mixin.client;

import com.feliscape.sanguis.registry.SanguisItems;
import com.feliscape.sanguis.util.MixinUtil;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends LivingEntity> extends AgeableListModel<T> {

    @Shadow protected abstract ModelPart getArm(HumanoidArm side);

    @Unique
    private static final List<Pose> VALID_POSES = List.of(
            Pose.STANDING,
            Pose.CROUCHING,
            Pose.DYING,
            Pose.FALL_FLYING
    );

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V",at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/model/geom/ModelPart;copyFrom(Lnet/minecraft/client/model/geom/ModelPart;)V", ordinal = 0))
    private void overrideArmRotation(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci){
        if (!VALID_POSES.contains(entity.getPose())) return;

        float staticRotation = MixinUtil.getParasolArmRotation();
        boolean isAttacking = attackTime > 0.0f;

        if (entity.getMainHandItem().is(SanguisItems.PARASOL)){
            ModelPart arm = getArm(entity.getMainArm());
            arm.xRot = isAttacking ? arm.xRot * 0.5F : staticRotation;
            arm.zRot *= 0.2F;
        }
        if (entity.getOffhandItem().is(SanguisItems.PARASOL)){
            ModelPart arm = getArm(entity.getMainArm().getOpposite());
            arm.xRot = isAttacking ? arm.xRot * 0.5F : staticRotation;
            arm.zRot *= 0.2F;
        }
    }
}
