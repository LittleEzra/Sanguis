package com.feliscape.sanguis.util;

import com.feliscape.sanguis.content.attachment.HunterData;
import com.feliscape.sanguis.content.quest.HunterQuest;
import com.feliscape.sanguis.registry.SanguisTags;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class HunterUtil {
    public static boolean isHunter(Entity entity){
        if (!(entity instanceof LivingEntity)) return false;

        if (entity.getType().is(SanguisTags.EntityTypes.HUNTER)) return true;
        return entity.hasData(HunterData.type()) && entity.getData(HunterData.type()).hasInjection();
    }

    public static boolean canInject(Entity entity) {
        return entity instanceof LivingEntity && !isHunter(entity) && !VampireUtil.isVampire(entity);
    }
}
