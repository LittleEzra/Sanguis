package com.feliscape.sanguis.registry.custom;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.quest.requirement.QuestType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

@EventBusSubscriber(modid = Sanguis.MOD_ID)
public class SanguisRegistries {
    public static final Registry<QuestType<?>> QUEST_REQUIREMENTS = new RegistryBuilder<>(Keys.QUEST_REQUIREMENTS)
            .sync(true)
            .create();

    @SubscribeEvent
    static void registerRegistries(NewRegistryEvent event){
        event.register(QUEST_REQUIREMENTS);
    }

    public static class Keys{

        public static final ResourceKey<Registry<QuestType<?>>> QUEST_REQUIREMENTS = ResourceKey.createRegistryKey(Sanguis.location("quest_requirements"));
    }
}
