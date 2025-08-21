package com.feliscape.sanguis.content.attachment;

import com.feliscape.sanguis.content.quest.HunterQuest;
import com.feliscape.sanguis.registry.SanguisCriteriaTriggers;
import com.feliscape.sanguis.registry.SanguisDataAttachmentTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jetbrains.annotations.Nullable;

public class HunterData extends DataAttachment{
    private LivingEntity holder;

    private boolean hasInjection;
    private HunterQuestData quests = new HunterQuestData();

    public static final StreamCodec<RegistryFriendlyByteBuf, HunterData> FULL_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            HunterData::hasInjection,
            HunterQuestData.STREAM_CODEC,
            HunterData::getQuests,
            HunterData::new
    );

    public HunterData() {

    }
    public HunterData(boolean hasInjection, HunterQuestData questData) {
        this.hasInjection = hasInjection;
        this.quests = questData;
    }

    @Override
    protected void save(CompoundTag tag) {
        tag.putBoolean("hasInjection", hasInjection);
        tag.put("questData", HunterQuestData.save(this.quests));
    }

    @Override
    protected void load(CompoundTag tag) {
        this.hasInjection = tag.getBoolean("hasInjection");
        this.quests = HunterQuestData.load(tag.getCompound("questData"));
        this.quests.setPlayer(this.holder);
    }

    public void removeGarlic(){
        hasInjection = false;
        this.quests.getActiveQuests().clear();
        this.holder.syncData(type());
    }
    public void injectGarlic(){
        hasInjection = true;
        if (this.holder instanceof ServerPlayer player){
            SanguisCriteriaTriggers.HUNTER_INJECT.get().trigger(player);
        }
        this.holder.syncData(type());
    }

    public boolean hasInjection(){
        return this.hasInjection;
    }

    public HunterQuestData getQuests() {
        return quests;
    }

    public void addQuest(HunterQuest quest){
        this.quests.add(quest);
        this.holder.syncData(type());
    }

    protected void setHolder(IAttachmentHolder holder){
        if (!(holder instanceof LivingEntity living)){
            throw new IllegalArgumentException("Trying to set HunterData holder to non-LivingEntity");
        }
        this.holder = living;
        if (living instanceof Player player){
            this.quests.setPlayer(player);
        } else{
            this.quests.setPlayer(null);
        }
    }
    protected void setHolder(LivingEntity holder){
        this.holder = holder;
        if (holder instanceof Player player){
            this.quests.setPlayer(player);
        } else{
            this.quests.setPlayer(null);
        }
    }

    public static HunterData copyDeathPersistent(HunterData oldData, IAttachmentHolder holder, HolderLookup.Provider provider){
        HunterData newData = new HunterData(oldData.hasInjection, oldData.quests);
        newData.setHolder(holder);
        return newData;
    }

    public static HunterData getInstance(IAttachmentHolder iAttachmentHolder) {
        if (!(iAttachmentHolder instanceof LivingEntity living)){
            throw new IllegalArgumentException("Trying to attach HunterData to non-LivingEntity");
        }

        HunterData data = new HunterData();
        data.setHolder(living);
        return data;
    }

    public static AttachmentType<HunterData> type(){
        return SanguisDataAttachmentTypes.HUNTER.get();
    }

    public HunterData update(IAttachmentHolder holder, RegistryFriendlyByteBuf buffer){
        if (!(holder instanceof LivingEntity living)){
            throw new IllegalArgumentException("Trying to read HunterData for non-LivingEntity");
        }

        this.hasInjection = buffer.readBoolean();
        this.quests = HunterQuestData.STREAM_CODEC.decode(buffer);
        this.setHolder(living);
        return this;
    }

    public static class SyncHandler implements AttachmentSyncHandler<HunterData> {

        @Override
        public void write(RegistryFriendlyByteBuf buf, HunterData attachment, boolean initialSync) {
            if (initialSync){
                FULL_STREAM_CODEC.encode(buf, attachment);
            } else{
                buf.writeBoolean(attachment.hasInjection);
                HunterQuestData.STREAM_CODEC.encode(buf, attachment.quests);
            }
        }

        @Override
        public @Nullable HunterData read(IAttachmentHolder holder, RegistryFriendlyByteBuf buf, @Nullable HunterData previousValue) {
            if (previousValue == null) {
                HunterData newData = FULL_STREAM_CODEC.decode(buf);
                newData.setHolder(holder);
                return newData;
            } else {
                return previousValue.update(holder, buf);
            }
        }
    }
}
