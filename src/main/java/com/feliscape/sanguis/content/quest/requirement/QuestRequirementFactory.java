package com.feliscape.sanguis.content.quest.requirement;

import com.feliscape.sanguis.content.quest.HunterQuest;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface QuestRequirementFactory<T extends HunterQuest> {
    T create(ServerLevel level);
}
