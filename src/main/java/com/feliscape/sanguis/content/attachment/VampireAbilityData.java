package com.feliscape.sanguis.content.attachment;

import com.feliscape.sanguis.content.ability.ActiveVampireAbility;
import com.feliscape.sanguis.content.ability.VampireAbility;
import com.feliscape.sanguis.registry.SanguisDataAttachmentTypes;
import com.feliscape.sanguis.registry.custom.SanguisRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class VampireAbilityData{
    private static final StreamCodec<RegistryFriendlyByteBuf, VampireAbility> ABILITY_STREAM_CODEC =
            ByteBufCodecs.registry(SanguisRegistries.Keys.VAMPIRE_ABILITIES);
    private static final StreamCodec<RegistryFriendlyByteBuf, List<VampireAbility>> ABILITY_LIST_STREAM_CODEC =
            ByteBufCodecs.registry(SanguisRegistries.Keys.VAMPIRE_ABILITIES).apply(ByteBufCodecs.list());
    private static final Codec<VampireAbility> ABILITY_CODEC = SanguisRegistries.VAMPIRE_ABILITIES.byNameCodec();

    public static final Codec<VampireAbilityData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.INT.fieldOf("skill_points").forGetter(VampireAbilityData::getSkillPoints),
            ABILITY_CODEC.optionalFieldOf("active_ability", null).forGetter(VampireAbilityData::getActiveAbility),
            ABILITY_CODEC.listOf().fieldOf("obtained_abilities").forGetter(VampireAbilityData::getObtainedAbilities)
    ).apply(inst, VampireAbilityData::new));

    private LivingEntity holder;

    public static final StreamCodec<RegistryFriendlyByteBuf, VampireAbilityData> STREAM_CODEC = StreamCodec.of(
            VampireAbilityData::encode,
            VampireAbilityData::decode
    );
    public static void encode(RegistryFriendlyByteBuf buf, VampireAbilityData data){
        buf.writeInt(data.skillPoints);
        buf.writeBoolean(data.activeAbility != null);
        if (data.activeAbility != null)
            ABILITY_STREAM_CODEC.encode(buf, data.activeAbility);
        ABILITY_LIST_STREAM_CODEC.encode(buf, data.obtainedAbilities);
    }
    public static VampireAbilityData decode(RegistryFriendlyByteBuf buf){
        return new VampireAbilityData().update(buf);
    }

    private int skillPoints = 0;
    private ArrayList<VampireAbility> obtainedAbilities = new ArrayList<>();
    @Nullable
    private ActiveVampireAbility activeAbility = null;

    public VampireAbilityData() {}

    public VampireAbilityData(int skillPoints, VampireAbility activeAbility, List<VampireAbility> obtainedAbilities) {
        this.skillPoints = skillPoints;
        this.setActiveAbility(activeAbility);
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

    public List<ActiveVampireAbility> getActiveAbilities(){
        var registry = holder.level().registryAccess().registryOrThrow(SanguisRegistries.Keys.VAMPIRE_ABILITIES);
        return registry.holders()
                .filter(ability -> hasAbility(ability) && ability.value() instanceof ActiveVampireAbility)
                .map(holder -> (ActiveVampireAbility) holder.value())
                .collect(Collectors.toList());
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
        return !obtainedAbilities.contains(ability) && obtainedAbilities.add(ability);
    }

    public boolean removeAbility(VampireAbility ability){
        if (!obtainedAbilities.contains(ability)) return false;
        obtainedAbilities.remove(ability);
        return true;
    }

    public void resolveUpdatedAbilities(List<VampireAbility> newAbilities) {
        for (VampireAbility a : newAbilities){
            if (!hasAbility(a)){
                unlockAbility(a);
            }
        }

        obtainedAbilities.removeIf(a -> !newAbilities.contains(a));
        this.holder.syncData(type());
    }

    protected void setHolder(IAttachmentHolder holder){
        if (!(holder instanceof LivingEntity living)){
            throw new IllegalArgumentException("Trying to set VampireData holder to non-LivingEntity");
        }
        this.holder = living;
    }
    protected void setHolder(LivingEntity living){
        this.holder = living;
    }

    public static VampireAbilityData getInstance(IAttachmentHolder iAttachmentHolder) {
        if (!(iAttachmentHolder instanceof LivingEntity living)){
            throw new IllegalArgumentException("Trying to attach VampireAbilityData to non-LivingEntity");
        }

        var data = new VampireAbilityData();
        data.setHolder(living);
        return data;
    }

    public ActiveVampireAbility getActiveAbility(){
        return activeAbility;
    }

    public void setActiveAbility(VampireAbility pickedAbility) {
        if (pickedAbility instanceof ActiveVampireAbility active){
            this.activeAbility = active;
        } else if (pickedAbility == null){
            this.activeAbility = null;
        }
    }

    public void useActiveAbility() {
        if (this.activeAbility != null){
            this.activeAbility.use(this.holder);
        }
    }

    public static class Serializer implements IAttachmentSerializer<Tag, VampireAbilityData> {
        public VampireAbilityData read(IAttachmentHolder holder, Tag tag, HolderLookup.Provider provider) {
            DataResult<VampireAbilityData> parsingResult = CODEC.parse(provider.createSerializationContext(NbtOps.INSTANCE), tag);

            VampireAbilityData data = parsingResult.getOrThrow((msg) -> this.buildException("read", msg));
            data.setHolder(holder);
            return data;
        }

        public @Nullable Tag write(VampireAbilityData attachment, HolderLookup.Provider provider) {
            DataResult<Tag> encodingResult = CODEC.encodeStart(provider.createSerializationContext(NbtOps.INSTANCE), attachment);
            return encodingResult.getOrThrow((msg) -> this.buildException("write", msg));
        }

        private RuntimeException buildException(String operation, String error) {
            return new IllegalStateException("Unable to " + operation + " attachment due to an internal codec error: " + error);
        }
    }

    public VampireAbilityData update(RegistryFriendlyByteBuf buffer){
        this.skillPoints = buffer.readInt();
        if (buffer.readBoolean())
            this.setActiveAbility(ABILITY_STREAM_CODEC.decode(buffer));
        this.obtainedAbilities = new ArrayList<>(ABILITY_LIST_STREAM_CODEC.decode(buffer));
        return this;
    }
    public VampireAbilityData update(IAttachmentHolder holder, RegistryFriendlyByteBuf buffer){
        if (!(holder instanceof LivingEntity living)){
            throw new IllegalArgumentException("Trying to read VampireAbilityData for non-LivingEntity");
        }

        this.update(buffer);
        this.setHolder(living);
        return this;
    }

    public static class SyncHandler implements AttachmentSyncHandler<VampireAbilityData> {
        public void write(RegistryFriendlyByteBuf buf, VampireAbilityData attachment, boolean initialSync) {
            STREAM_CODEC.encode(buf, attachment);
        }

        public VampireAbilityData read(IAttachmentHolder holder, RegistryFriendlyByteBuf buf, @Nullable VampireAbilityData previousValue) {
            VampireAbilityData data = new VampireAbilityData();
            return data.update(holder, buf);
        }
    }
}
