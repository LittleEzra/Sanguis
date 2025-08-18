package com.feliscape.sanguis.mixin;

import com.feliscape.sanguis.content.attachment.EntityBloodData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Brain.class)
public abstract class BrainMixin<E extends LivingEntity> {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void cancelTick(ServerLevel level, E entity, CallbackInfo ci){
        if (entity.hasData(EntityBloodData.type()) && entity.getData(EntityBloodData.type()).isFrozen()){
            ci.cancel();
        }
    }
}
