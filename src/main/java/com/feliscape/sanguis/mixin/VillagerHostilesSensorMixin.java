package com.feliscape.sanguis.mixin;

import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.VillagerHostilesSensor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerHostilesSensor.class)
public class VillagerHostilesSensorMixin {

    @Inject(method = "isMatchingEntity", at = @At("HEAD"), cancellable = true)
    protected void makeVampiresMatch(LivingEntity attacker, LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        if (VampireUtil.isVampire(target) && target.distanceToSqr(attacker) <= (double)(8.0F * 8.0F)){
            cir.setReturnValue(true);
        }
    }
}
