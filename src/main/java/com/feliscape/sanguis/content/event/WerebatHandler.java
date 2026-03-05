package com.feliscape.sanguis.content.event;

import com.feliscape.sanguis.Sanguis;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = Sanguis.MOD_ID)
public class WerebatHandler {
    @SubscribeEvent
    public static void tick(PlayerTickEvent.Post event){

    }
}
