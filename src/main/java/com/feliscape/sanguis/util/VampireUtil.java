package com.feliscape.sanguis.util;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.attachment.VampireData;
import com.feliscape.sanguis.registry.SanguisDataMapTypes;
import com.feliscape.sanguis.registry.SanguisTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class VampireUtil {
    public static boolean isVampire(Entity entity){
        if (!(entity instanceof LivingEntity)) return false;

        if (entity.getType().is(SanguisTags.EntityTypes.VAMPIRIC)) return true;
        return entity.hasData(VampireData.type()) && entity.getData(VampireData.type()).isVampire();
    }

    public static boolean canInfect(Entity entity) {
        return entity instanceof LivingEntity && entity.getType().is(SanguisTags.EntityTypes.INFECTABLE) &&
                !isVampire(entity);
    }

    public static boolean shouldBurnInSunlight(LivingEntity entity){
        Level level = entity.level();
        boolean day = level.isClientSide() ? level.getDayTime() < 13000L || level.getDayTime() > 23500L : level.isDay();

        if (day && !level.isClientSide) {
            BlockPos blockpos = BlockPos.containing(entity.getX(), entity.getEyeY(), entity.getZ());
            boolean inColdBlockOrWater = entity.isInWaterRainOrBubble() || entity.isInPowderSnow || entity.wasInPowderSnow;
            return !inColdBlockOrWater && level.canSeeSky(blockpos);
        }

        return false;
    }

    @SuppressWarnings("deprecation")
    public static boolean canDrink(LivingEntity vampire, LivingEntity target){
        if (!isVampire(vampire)) return false;

        return !isVampire(target) && target.getType().builtInRegistryHolder().getData(SanguisDataMapTypes.ENTITY_BLOOD) != null;
    }
}
