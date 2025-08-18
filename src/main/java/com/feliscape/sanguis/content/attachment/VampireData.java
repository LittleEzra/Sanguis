package com.feliscape.sanguis.content.attachment;

import com.feliscape.sanguis.registry.SanguisCriteriaTriggers;
import com.feliscape.sanguis.registry.SanguisDataAttachmentTypes;
import com.feliscape.sanguis.registry.SanguisTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jetbrains.annotations.Nullable;

public class VampireData extends DataAttachment {
    private int infectionTime = -1;
    private boolean isVampire;
    private LivingEntity holder;

    private VampireBloodData bloodData = new VampireBloodData();

    public static final StreamCodec<RegistryFriendlyByteBuf, VampireData> FULL_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            VampireData::getInfectionTime,
            ByteBufCodecs.BOOL,
            VampireData::isVampire,
            VampireBloodData.FULL_STREAM_CODEC,
            VampireData::getBloodData,
            VampireData::new
    );

    public VampireData(){

    }

    public VampireData(boolean isVampire){
        this.isVampire = isVampire;
        this.infectionTime = isVampire ? 0 : -1;
    }
    public VampireData(int infectionTime, boolean isVampire){
        this.infectionTime = infectionTime;
        this.isVampire = isVampire;
    }

    public VampireData(int infectionTime, boolean isVampire, VampireBloodData bloodData){
        this.infectionTime = infectionTime;
        this.isVampire = isVampire;
        this.bloodData = bloodData;
    }

    @Override
    protected void save(CompoundTag tag) {
        tag.putInt("infectionTime", infectionTime);
        tag.putBoolean("isVampire", isVampire);
        tag.put("bloodData", VampireBloodData.save(new CompoundTag(), this.bloodData));
    }

    @Override
    protected void load(CompoundTag tag) {
        this.infectionTime = tag.getInt("infectionTime");
        this.isVampire = tag.getBoolean("isVampire");
        this.bloodData = VampireBloodData.load(tag.getCompound("bloodData"));
    }

    public void tick(){
        if (infectionTime > 0){
            infectionTime--;
            checkVampireStatus();
        }

        if (isVampire){
            if (!this.holder.level().isClientSide()) {
                if (this.holder.tickCount % 5 == 0 && inSunlight(this.holder) && canBurn(this.holder)){
                    this.holder.igniteForSeconds(2.0F);
                }

                if (this.holder instanceof Player player) {
                    this.bloodData.tick(player);

                    if (this.holder.tickCount % 20 == 0){
                        BlockPos blockPos = this.holder.blockPosition();
                        if (BlockPos.findClosestMatch(blockPos, 4, 4, pos ->
                                this.holder.level().getBlockState(pos).is(SanguisTags.Blocks.VAMPIRE_REPELLENTS)).isPresent()){
                            this.holder.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 1, true, true));
                            this.holder.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0, true, true));
                        }
                    }
                }
            }
        }
    }

    private boolean canBurn(LivingEntity entity) {
        return !entity.fireImmune() && !entity.isInvulnerableTo(entity.damageSources().onFire());
    }

    private boolean inSunlight(LivingEntity entity) {
        Level level = entity.level();
        if (level.isClientSide()){
            return level.canSeeSky(entity.blockPosition()) && (level.getDayTime() < 13000L || level.getDayTime() > 23500);
        }
        return level.canSeeSky(entity.blockPosition()) && level.isDay();
    }

    /**
     * Sets isVampire based on the infectionTime
     */
    private void checkVampireStatus() {
        boolean wasVampire = isVampire;
        if (infectionTime == 0){
            isVampire = true;
            if (!wasVampire){
                transformToVampire();
            }
        } else if (infectionTime < 0){
            isVampire = false;
            if (wasVampire && this.holder instanceof ServerPlayer serverPlayer){
                SanguisCriteriaTriggers.VAMPIRE_CURE.get().trigger(serverPlayer);
            }
        }
    }

    private void transformToVampire(){
        bloodData.setBlood(bloodData.maxBlood());
        bloodData.setDrinkDelay(0);
        bloodData.setExhaustion(0.0F);
        if (this.holder instanceof ServerPlayer serverPlayer){
            SanguisCriteriaTriggers.VAMPIRE_TRANSFORMATION.get().trigger(serverPlayer);
        }
    }

    public int getInfectionTime(){
        return infectionTime;
    }

    public boolean isVampire(){
        return isVampire && infectionTime >= 0;
    }

    /**
     * Makes this entity no longer vampiric.
     * @return Whether the vampire state changed
     */
    public boolean cure(){
        if (!this.isVampire) return false;

        this.infectionTime = -1;
        this.checkVampireStatus();
        return true;
    }

    /**
     * Makes this entity no longer vampiric.
     * @return Whether the vampire state changed
     */
    public boolean cure(boolean causeEffects){
        if (this.cure()){
            if (causeEffects) {
                this.holder.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 240, 2));
                this.holder.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 2));
                this.holder.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1));
            }
            return true;
        } else{
            return false;
        }
    }

    public void drink(LivingEntity target){
        this.bloodData.drink(this.holder, target);
    }

    public void infect(LivingEntity entity) {
        entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 1));
        this.infectionTime = 5 * 60 * 20; // 5 Minutes
    }
    public void infectImmediately() {
        this.infectionTime = 0;
        this.checkVampireStatus();
    }

    public static AttachmentType<VampireData> type(){
        return SanguisDataAttachmentTypes.VAMPIRISM.get();
    }

    public boolean isInfected() {
        return isVampire || infectionTime >= 0;
    }

    public VampireBloodData getBloodData() {
        return bloodData;
    }

    protected void setHolder(IAttachmentHolder holder){
        if (!(holder instanceof LivingEntity living)){
            throw new IllegalArgumentException("Trying to set VampireData holder to non-LivingEntity");
        }
        this.holder = living;
    }

    public static VampireData copyDeathPersistent(VampireData oldData, IAttachmentHolder holder, HolderLookup.Provider provider){
        VampireData newData;
        if (oldData.infectionTime != 0){
            newData = new VampireData(oldData.isVampire);
        } else{
            newData = new VampireData(oldData.infectionTime, oldData.isVampire);
        }
        newData.setHolder(holder);
        return newData;
    }

    public static VampireData getInstance(IAttachmentHolder iAttachmentHolder) {
        if (!(iAttachmentHolder instanceof LivingEntity living)){
            throw new IllegalArgumentException("Trying to attach VampireData to non-LivingEntity");
        }

        VampireData data = new VampireData();
        data.holder = living;
        return data;
    }

    public VampireData update(IAttachmentHolder holder, RegistryFriendlyByteBuf buffer){
        if (!(holder instanceof LivingEntity living)){
            throw new IllegalArgumentException("Trying to read VampireData for non-LivingEntity");
        }

        this.infectionTime = buffer.readInt();
        this.isVampire = buffer.readBoolean();
        this.bloodData.update(buffer);
        this.setHolder(living);
        return this;
    }

    public static class SyncHandler implements AttachmentSyncHandler<VampireData> {

        @Override
        public void write(RegistryFriendlyByteBuf buf, VampireData attachment, boolean initialSync) {
            if (initialSync){
                FULL_STREAM_CODEC.encode(buf, attachment);
            } else{
                buf.writeInt(attachment.infectionTime);
                buf.writeBoolean(attachment.isVampire);
                VampireBloodData.FULL_STREAM_CODEC.encode(buf, attachment.bloodData);
            }
        }

        @Override
        public @Nullable VampireData read(IAttachmentHolder holder, RegistryFriendlyByteBuf buf, @Nullable VampireData previousValue) {
            if (previousValue == null) {
                VampireData newData = FULL_STREAM_CODEC.decode(buf);
                newData.setHolder(holder);
                return newData;
            } else {
                return previousValue.update(holder, buf);
            }
        }
    }
}
