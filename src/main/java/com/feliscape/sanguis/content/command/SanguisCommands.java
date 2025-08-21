package com.feliscape.sanguis.content.command;

import com.feliscape.sanguis.Sanguis;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = Sanguis.MOD_ID)
public class SanguisCommands {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event){
        QuestCommand.register(event.getDispatcher());
    }
}
