package com.feliscape.sanguis.util;

import com.feliscape.sanguis.content.quest.HunterQuest;
import com.feliscape.sanguis.content.quest.requirement.QuestType;
import com.feliscape.sanguis.registry.custom.SanguisRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringUtil;

import java.util.List;

public class QuestUtil {

    public static Component formatDuration(HunterQuest quest, float durationFactor, float ticksPerSecond) {
        if (quest.isInfiniteDuration()) {
            return Component.translatable("effect.duration.infinite");
        } else {
            int i = Mth.floor((float)quest.getDuration() * durationFactor);
            return Component.literal(StringUtil.formatTickDuration(i, ticksPerSecond));
        }
    }

    public static HunterQuest createRandom(ServerLevel serverLevel){
        List<Holder.Reference<QuestType<?>>> all = SanguisRegistries.QUEST_TYPES.holders().toList();
        QuestType<?> type = all.get(serverLevel.random.nextInt(all.size())).value();

        return type.factory().create(serverLevel);
    }
}
