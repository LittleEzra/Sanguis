package com.feliscape.sanguis.registry;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.client.screen.ActiveQuestsScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = Sanguis.MOD_ID, value = Dist.CLIENT)
public class SanguisScreens {

    @SubscribeEvent
    public static void registerMenuScreens(RegisterMenuScreensEvent event)
    {
        event.register(SanguisMenuTypes.ACTIVE_QUESTS.get(), ActiveQuestsScreen::new);
    }
}
