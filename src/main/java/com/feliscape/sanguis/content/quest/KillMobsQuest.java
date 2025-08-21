package com.feliscape.sanguis.content.quest;

import com.feliscape.sanguis.content.quest.requirement.QuestType;
import com.feliscape.sanguis.registry.SanguisQuestTypes;
import com.feliscape.sanguis.registry.SanguisTags;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

public class KillMobsQuest extends HunterQuest {
    public static final MapCodec<KillMobsQuest> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        Codec.unboundedMap(BuiltInRegistries.ENTITY_TYPE.byNameCodec(), Codec.INT).fieldOf("required_kills").forGetter(KillMobsQuest::getRequiredKills),
        Codec.unboundedMap(BuiltInRegistries.ENTITY_TYPE.byNameCodec(), Codec.INT).fieldOf("kills").forGetter(KillMobsQuest::getKills)
    ).apply(inst, KillMobsQuest::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, KillMobsQuest> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(Object2ObjectOpenHashMap::new, ByteBufCodecs.registry(Registries.ENTITY_TYPE), ByteBufCodecs.VAR_INT),
            KillMobsQuest::getRequiredKills,
            ByteBufCodecs.map(Object2ObjectOpenHashMap::new, ByteBufCodecs.registry(Registries.ENTITY_TYPE), ByteBufCodecs.VAR_INT),
            KillMobsQuest::getKills,
            KillMobsQuest::new
    );

    protected Map<EntityType<?>, Integer> requiredKills;
    protected HashMap<EntityType<?>, Integer> kills = new HashMap<>();

    public KillMobsQuest(Map<EntityType<?>, Integer> requiredKills){
        this.requiredKills = requiredKills;
    }
    public KillMobsQuest(Map<EntityType<?>, Integer> requiredKills, Map<EntityType<?>, Integer> kills){
        this.requiredKills = requiredKills;
        this.kills = new HashMap<>(kills);
    }

    @Override
    public QuestType<? extends KillMobsQuest> type() {
        return SanguisQuestTypes.KILL_MOBS.get();
    }

    @Override
    public boolean checkCompleted(Player player) {
        for (Map.Entry<EntityType<?>, Integer> entry : requiredKills.entrySet()){
            if (!kills.containsKey(entry.getKey())) return false;

            if (kills.get(entry.getKey()) < entry.getValue()) return false;
        }
        return true;
    }

    @Override
    public void onComplete(Player player) {

    }

    @Override
    public Component getTitle() {
        return Component.translatable("quest.sanguis.kill_mobs.title")
                .withStyle(ChatFormatting.GRAY);
    }

    @Override
    public Component getName() {
        return Component.translatable("quest.sanguis.kill_mobs.name",
                        Component.translatable(requiredKills.entrySet().stream().findFirst().orElseThrow().getKey().getDescriptionId()))
                .withStyle(ChatFormatting.WHITE);
    }

    public Map<EntityType<?>, Integer> getRequiredKills() {
        return requiredKills;
    }

    public HashMap<EntityType<?>, Integer> getKills() {
        return kills;
    }

    public int getKills(EntityType<?> type) {
        return kills.get(type);
    }
    public void recordKill(EntityType<?> type) {
        this.recordKill(type, 1);
    }
    public void recordKill(EntityType<?> type, int amount) {
        if (!this.requiredKills.containsKey(type)) return;

        if (this.kills.containsKey(type))
            this.kills.put(type, this.kills.get(type) + amount);
        else
            this.kills.put(type, amount);
    }

    public static KillMobsQuest create(ServerLevel level){
        HolderLookup.RegistryLookup<EntityType<?>> typeLookup = level.getServer().registryAccess().lookupOrThrow(Registries.ENTITY_TYPE);
        var availableTypes = typeLookup.getOrThrow(SanguisTags.EntityTypes.KILL_MOB_QUEST_VALID);
        return new KillMobsQuest(Map.of(
                availableTypes.getRandomElement(level.random).orElseThrow().value(), Mth.nextInt(level.random, 10, 20)
        ));
    }
}
