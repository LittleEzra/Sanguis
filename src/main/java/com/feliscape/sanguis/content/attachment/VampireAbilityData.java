package com.feliscape.sanguis.content.attachment;

import com.feliscape.sanguis.data.ability.VampireAbility;
import com.feliscape.sanguis.data.ability.VampireAbilityHolder;
import com.feliscape.sanguis.data.ability.VampireAbilityWrapper;
import com.feliscape.sanguis.registry.SanguisDataAttachmentTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VampireAbilityData{
    public static final Codec<VampireAbilityData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.INT.fieldOf("skill_points").forGetter(VampireAbilityData::getSkillPoints),
            ResourceLocation.CODEC.listOf().fieldOf("obtained_abilities").forGetter(VampireAbilityData::getObtainedAbilities)
    ).apply(inst, VampireAbilityData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, VampireAbilityData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            VampireAbilityData::getSkillPoints,
            ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()),
            VampireAbilityData::getObtainedAbilities,
            VampireAbilityData::new
    );

    private int skillPoints = 0;
    private ArrayList<ResourceLocation> obtainedAbilities = new ArrayList<>();

    public VampireAbilityData() {}

    public VampireAbilityData(int skillPoints, List<ResourceLocation> obtainedAbilities) {
        this.skillPoints = skillPoints;
        this.obtainedAbilities = new ArrayList<>(obtainedAbilities);
    }

    public int getSkillPoints() {
        return skillPoints;
    }
    public List<ResourceLocation> getObtainedAbilities(){
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

    public boolean hasAbility(ResourceLocation id){
        return obtainedAbilities.contains(id);
    }
    public boolean hasAbility(VampireAbilityWrapper wrapper){
        return obtainedAbilities.contains(wrapper.getId());
    }
    public boolean hasAbility(VampireAbilityHolder holder){
        return obtainedAbilities.contains(holder.id());
    }

    public boolean unlockAbility(VampireAbilityWrapper wrapper){
        if (skillPoints < wrapper.getAbility().cost() || !obtainedAbilities.add(wrapper.getId())){
            return false;
        }
        skillPoints -= wrapper.getAbility().cost();
        return true;
    }
    public boolean unlockAbility(VampireAbilityHolder holder){
        if (skillPoints < holder.value().cost() || !obtainedAbilities.add(holder.id())){
            return false;
        }
        skillPoints -= holder.value().cost();
        return true;
    }
}
