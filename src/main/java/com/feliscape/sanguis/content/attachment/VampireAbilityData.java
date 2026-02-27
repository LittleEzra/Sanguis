package com.feliscape.sanguis.content.attachment;

import com.feliscape.sanguis.content.ability.ActiveVampireAbility;
import com.feliscape.sanguis.content.ability.VampireAbility;
import com.feliscape.sanguis.registry.SanguisDataAttachmentTypes;
import com.feliscape.sanguis.registry.custom.SanguisRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.AttachmentType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class VampireAbilityData{
    public static final Codec<VampireAbilityData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.INT.fieldOf("skill_points").forGetter(VampireAbilityData::getSkillPoints),
            SanguisRegistries.VAMPIRE_ABILITIES.byNameCodec().listOf()
                    .fieldOf("obtained_abilities")
                    .forGetter(VampireAbilityData::getObtainedAbilities)
    ).apply(inst, VampireAbilityData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, VampireAbilityData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            VampireAbilityData::getSkillPoints,
            ByteBufCodecs.registry(SanguisRegistries.Keys.VAMPIRE_ABILITIES).apply(ByteBufCodecs.list()),
            VampireAbilityData::getObtainedAbilities,
            VampireAbilityData::new
    );

    private int skillPoints = 0;
    private ArrayList<VampireAbility> obtainedAbilities = new ArrayList<>();

    public VampireAbilityData() {}

    public VampireAbilityData(int skillPoints, List<VampireAbility> obtainedAbilities) {
        this.skillPoints = skillPoints;
        this.obtainedAbilities = new ArrayList<>(obtainedAbilities);
    }

    public int getSkillPoints() {
        return skillPoints;
    }
    public List<VampireAbility> getObtainedAbilities(){
        return obtainedAbilities;
    }

    public static AttachmentType<VampireAbilityData> type(){
        return SanguisDataAttachmentTypes.VAMPIRE_ABILITIES.get();
    }

    public Set<ActiveVampireAbility> getActiveAbilities(Level level){
        var registry = level.registryAccess().registryOrThrow(SanguisRegistries.Keys.VAMPIRE_ABILITIES);
        return registry.holders()
                .filter(ability -> hasAbility(ability) && ability.value() instanceof ActiveVampireAbility)
                .map(holder -> (ActiveVampireAbility) holder.value())
                .collect(Collectors.toSet());
    }

    public void onLevelChange(int newLevel){
        skillPoints = (newLevel * newLevel + newLevel) / 2;
    }

    public boolean hasAbility(VampireAbility ability){
        return obtainedAbilities.contains(ability);
    }
    public boolean hasAbility(Supplier<? extends VampireAbility> ability){
        return hasAbility(ability.get());
    }
    public boolean hasAbility(Holder<VampireAbility> ability){
        return hasAbility(ability.value());
    }

    public boolean unlockAbility(VampireAbility ability){
        if (skillPoints < ability.getCost() || obtainedAbilities.contains(ability) || !obtainedAbilities.add(ability)){
            return false;
        }
        skillPoints -= ability.getCost();
        return true;
    }

    public boolean removeAbility(VampireAbility ability){
        if (!obtainedAbilities.contains(ability)) return false;
        obtainedAbilities.remove(ability);
        return true;
    }
}
