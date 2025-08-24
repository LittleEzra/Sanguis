package com.feliscape.sanguis.mixin;

import com.feliscape.sanguis.registry.SanguisTags;
import com.feliscape.sanguis.util.VampireUtil;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow public abstract EntityType<?> getType();

    @ModifyReturnValue(method = "isAlliedTo(Lnet/minecraft/world/entity/Entity;)Z", at = @At("TAIL"))
    public boolean makeAlliedToVampires(boolean original, Entity entity){
        if (this.getType().is(SanguisTags.EntityTypes.VAMPIRE_NEUTRAL)) {
            return original || VampireUtil.isVampire(entity);
        }
        return original;
    }
}
