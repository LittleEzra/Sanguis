package com.feliscape.sanguis.mixin;

import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public class FoodDataMixin {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void cancelTick(Player player, CallbackInfo ci) {
        if (VampireUtil.isVampire(player)){
            ci.cancel();
        }
    }
}
