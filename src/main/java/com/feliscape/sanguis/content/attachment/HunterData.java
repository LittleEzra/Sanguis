package com.feliscape.sanguis.content.attachment;

import com.feliscape.sanguis.registry.SanguisCriteriaTriggers;
import com.feliscape.sanguis.registry.SanguisDataAttachmentTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jetbrains.annotations.Nullable;

public class HunterData extends DataAttachment{
    private LivingEntity holder;

    private boolean hasInjection;

    public static final StreamCodec<RegistryFriendlyByteBuf, HunterData> FULL_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            HunterData::hasInjection,
            HunterData::new
    );

    public HunterData() {

    }
    public HunterData(boolean hasInjection) {
        this.hasInjection = hasInjection;
    }

    @Override
    protected void save(CompoundTag tag) {
        tag.putBoolean("hasInjection", hasInjection);
    }

    @Override
    protected void load(CompoundTag tag) {
        this.hasInjection = tag.getBoolean("hasInjection");
    }

    public void removeGarlic(){
        hasInjection = false;
    }
    public void injectGarlic(){
        hasInjection = true;
        if (this.holder instanceof ServerPlayer player){
            SanguisCriteriaTriggers.HUNTER_INJECT.get().trigger(player);
        }
    }

    public boolean hasInjection(){
        return this.hasInjection;
    }

    protected void setHolder(IAttachmentHolder holder){
        if (!(holder instanceof LivingEntity living)){
            throw new IllegalArgumentException("Trying to set HunterData holder to non-LivingEntity");
        }
        this.holder = living;
    }

    public static HunterData copyDeathPersistent(HunterData oldData, IAttachmentHolder holder, HolderLookup.Provider provider){
        HunterData newData = new HunterData(oldData.hasInjection);
        newData.setHolder(holder);
        return newData;
    }

    public static HunterData getInstance(IAttachmentHolder iAttachmentHolder) {
        if (!(iAttachmentHolder instanceof LivingEntity living)){
            throw new IllegalArgumentException("Trying to attach HunterData to non-LivingEntity");
        }

        HunterData data = new HunterData();
        data.holder = living;
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
