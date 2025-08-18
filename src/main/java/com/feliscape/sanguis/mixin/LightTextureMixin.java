package com.feliscape.sanguis.mixin;

import com.feliscape.sanguis.SanguisClientConfig;
import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LightTexture.class)
public class LightTextureMixin {

    @Shadow @Final private Minecraft minecraft;

    @ModifyVariable(method = "updateLightTexture", ordinal = 7, at = @At(value = "INVOKE", target = "Lorg/joml/Vector3f;lerp(Lorg/joml/Vector3fc;F)Lorg/joml/Vector3f;", ordinal = 0))
    public float overrideLight(float value){
        if (VampireUtil.isVampire(this.minecraft.player)){
            float brightness = (float) SanguisClientConfig.CONFIG.vampireNightVisionBrightness.getAsDouble();
            return Math.max(value, brightness);
        }
        return value;
    }
}
