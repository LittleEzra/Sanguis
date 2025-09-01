package com.feliscape.sanguis.util;

import net.minecraft.util.Mth;
import org.lwjgl.system.MathUtil;

public class MixinUtil {
    public static float getParasolArmRotation(){
        return (float) Math.toRadians(-17.5);
    }
}
