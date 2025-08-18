package com.feliscape.sanguis.mixin;

import com.feliscape.sanguis.content.attachment.EntityBloodData;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity implements EquipmentUser, Leashable, Targeting {

    @Shadow @Final public GoalSelector goalSelector;

    @Shadow @Final public GoalSelector targetSelector;

    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "updateControlFlags", at = @At(value = "TAIL"))
    public void disableAI(CallbackInfo ci){
        if (this.hasData(EntityBloodData.type())){
            if (this.getData(EntityBloodData.type()).isFrozen()) {
                this.targetSelector.setControlFlag(Goal.Flag.TARGET, false);
                this.goalSelector.setControlFlag(Goal.Flag.MOVE, false);
                this.goalSelector.setControlFlag(Goal.Flag.LOOK, false);
                this.goalSelector.setControlFlag(Goal.Flag.JUMP, false);
            }
        }
    }
}
