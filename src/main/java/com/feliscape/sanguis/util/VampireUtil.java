package com.feliscape.sanguis.util;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.attachment.HunterData;
import com.feliscape.sanguis.content.attachment.VampireData;
import com.feliscape.sanguis.registry.SanguisDataMapTypes;
import com.feliscape.sanguis.registry.SanguisTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class VampireUtil {
    public static boolean isVampire(Entity entity){
        if (!(entity instanceof LivingEntity)) return false;
        if (entity instanceof Player player && player.isSpectator()) return false;

        if (entity.getType().is(SanguisTags.EntityTypes.VAMPIRIC)) return true;
        return entity.hasData(VampireData.type()) && entity.getData(VampireData.type()).isVampire();
    }
    public static boolean isBat(Entity entity){
        if (!(entity instanceof Player) || !isVampire(entity)) return false;

        return entity.getData(VampireData.type()).isBat();
    }

    public static boolean canInfect(Entity entity) {
        return entity instanceof LivingEntity && entity.getType().is(SanguisTags.EntityTypes.INFECTABLE) &&
                !isVampire(entity) && !HunterUtil.isHunter(entity);
    }

    public static boolean shouldBurnInSunlight(LivingEntity entity){
        Level level = entity.level();
        if (!level.canSeeSky(entity.blockPosition())) return false;
        boolean day = level.isClientSide() ? level.getDayTime() < 13000L || level.getDayTime() > 23500L : level.isDay();

        boolean shouldByDefault = false;

        if (day && !level.isClientSide) {
            BlockPos blockpos = BlockPos.containing(entity.getX(), entity.getEyeY(), entity.getZ());
            boolean inColdBlockOrWater = entity.isInWaterRainOrBubble() || entity.isInPowderSnow || entity.wasInPowderSnow;
            shouldByDefault = !inColdBlockOrWater && level.canSeeSky(blockpos);
        }

        boolean hasSunEquipment = entity.getMainHandItem().is(SanguisTags.Items.HELD_SUN_PROTECTION)
                || entity.getOffhandItem().is(SanguisTags.Items.HELD_SUN_PROTECTION);

        return shouldByDefault && !hasSunEquipment;
    }
    public static boolean isDay(Level level){
        return level.isClientSide() ? level.getDayTime() < 13000L || level.getDayTime() > 23500L : level.isDay();
    }

    @SuppressWarnings("deprecation")
    public static boolean canDrink(LivingEntity vampire, LivingEntity target){
        if (!isVampire(vampire)) return false;

        if (vampire.getMainHandItem().is(SanguisTags.Items.HELD_SUN_PROTECTION)
                || vampire.getOffhandItem().is(SanguisTags.Items.HELD_SUN_PROTECTION)) return false;

        return !isVampire(target) && (target.getType().is(SanguisTags.EntityTypes.FOUL_BLOOD) ||
                target.getType().builtInRegistryHolder().getData(SanguisDataMapTypes.ENTITY_BLOOD) != null);
    }

    public static boolean hasFoulBlood(Entity entity){
        if (!(entity instanceof LivingEntity)) return false;

        if (entity.getType().is(SanguisTags.EntityTypes.FOUL_BLOOD)) return true;
        return entity.hasData(HunterData.type()) && entity.getData(HunterData.type()).hasInjection();
    }

    public static boolean isInfected(Entity entity) {
        if (!(entity instanceof LivingEntity)) return false;

        if (entity.getType().is(SanguisTags.EntityTypes.VAMPIRIC)) return false;
        return entity.hasData(VampireData.type()) && !isVampire(entity) && entity.getData(VampireData.type()).isInfected();
    }
}
