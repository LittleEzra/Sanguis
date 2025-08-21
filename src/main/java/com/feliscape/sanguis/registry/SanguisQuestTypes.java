package com.feliscape.sanguis.registry;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.content.quest.ItemQuest;
import com.feliscape.sanguis.content.quest.KillMobsQuest;
import com.feliscape.sanguis.content.quest.requirement.QuestType;
import com.feliscape.sanguis.registry.custom.SanguisRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SanguisQuestTypes {
    public static final DeferredRegister<QuestType<?>> QUEST_TYPES =
            DeferredRegister.create(SanguisRegistries.QUEST_REQUIREMENTS, Sanguis.MOD_ID);

    public static final Supplier<QuestType<ItemQuest>> FETCH_ITEMS = QUEST_TYPES.register(
            "fetch_items", () -> new QuestType<>(ItemQuest.CODEC, ItemQuest.STREAM_CODEC, ItemQuest::create)
    );
    public static final Supplier<QuestType<KillMobsQuest>> KILL_MOBS = QUEST_TYPES.register(
            "kill_mobs", () -> new QuestType<>(KillMobsQuest.CODEC, KillMobsQuest.STREAM_CODEC, KillMobsQuest::create)
    );

    public static void register(IEventBus eventBus){
        QUEST_TYPES.register(eventBus);
    }
}
