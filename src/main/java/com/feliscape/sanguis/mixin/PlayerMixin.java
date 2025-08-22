package com.feliscape.sanguis.mixin;

import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    @Unique
    private static final EntityDimensions BAT_DIMENSIONS = EntityDimensions.scalable(0.6F, 0.6F).withEyeHeight(0.4F);

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "getDefaultDimensions", at = @At("HEAD"), cancellable = true)
    public void getDefaultDimensions(Pose pose, CallbackInfoReturnable<EntityDimensions> cir) {
        if (VampireUtil.isVampire(self()) && VampireUtil.isBat(self())){
            cir.setReturnValue(BAT_DIMENSIONS);
        }
    }
    @Inject(method = "getFlyingSpeed", at = @At("HEAD"), cancellable = true)
    public void overrideBatFlyingSpeed(CallbackInfoReturnable<Float> cir) {
        if (VampireUtil.isBat(self())){
            cir.setReturnValue(this.isSprinting() ? 0.051999997f : 0.04f);
        }
    }
}
