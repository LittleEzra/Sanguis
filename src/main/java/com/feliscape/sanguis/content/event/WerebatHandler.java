package com.feliscape.sanguis.content.event;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.registry.SanguisDataAttachmentTypes;
import com.feliscape.sanguis.registry.SanguisMobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = Sanguis.MOD_ID)
public class WerebatHandler {
    @SubscribeEvent
    public static void tick(PlayerTickEvent.Post event){

    }

    @SubscribeEvent
    public static void jump(LivingEvent.LivingJumpEvent event){

    }

    public static boolean isWerebat(LivingEntity living){
        return living instanceof Player player && isWerebat(player);
    }
    public static boolean isWerebat(Player player){
        return player.hasEffect(SanguisMobEffects.WEREBAT_CURSE) || player.hasData(SanguisDataAttachmentTypes.WEREBAT_CURSE);
    }
}
