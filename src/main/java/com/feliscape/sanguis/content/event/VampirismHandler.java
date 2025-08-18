package com.feliscape.sanguis.content.event;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.attachment.EntityBloodData;
import com.feliscape.sanguis.content.attachment.VampireData;
import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = Sanguis.MOD_ID)
public class VampirismHandler {
    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event){
        if (event.getEntity() instanceof LivingEntity living){
            if (living.hasData(VampireData.type())) {
                VampireData data = living.getData(VampireData.type());
                data.tick();
            }
            if (living.hasData(EntityBloodData.type())) {
                EntityBloodData data = living.getData(EntityBloodData.type());
                data.tick();
            }
        }
    }
    @SubscribeEvent
    public static void onEntityTick(LivingEntityUseItemEvent.Start event){
        LivingEntity entity = event.getEntity();
        if (VampireUtil.isVampire(entity)){
            if (event.getItem().getFoodProperties(entity) != null){
                event.setCanceled(true);
                if (entity instanceof Player player){
                    player.displayClientMessage(Component.translatable("sanguis.cant_eat_message"), true);
                }
            }
        }
    }
}
