package com.feliscape.sanguis.content.attachment;

import com.feliscape.sanguis.content.ability.VampireAbility;
import com.feliscape.sanguis.registry.SanguisDataAttachmentTypes;
import com.feliscape.sanguis.registry.custom.SanguisRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

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

    /*@Override
    protected void load(CompoundTag tag) {
        obtainedAbilities = new ArrayList<>();
        ListTag list = tag.getList("0", Tag.TAG_STRING);
        for (int i = 0; i < list.size(); i++){
            obtainedAbilities.add(ResourceLocation.parse(list.getString(i)));
        }
        this.skillPoints = tag.getInt("skill_points");
    }

    @Override
    protected void save(CompoundTag tag) {
        ListTag list = new ListTag();
        for (int i = 0; i < obtainedAbilities.size(); i++){
            list.add(StringTag.valueOf(obtainedAbilities.get(i).toString()));
        }
        tag.put("obtained_abilities", list);
        tag.putInt("skill_points", skillPoints);
    }*/

    public boolean hasAbility(VampireAbility ability){
        return obtainedAbilities.contains(ability);
    }
    public boolean hasAbility(Supplier<VampireAbility> ability){
        return hasAbility(ability.get());
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
