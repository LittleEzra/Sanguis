package com.feliscape.sanguis.content.attachment;

import com.feliscape.sanguis.Sanguis;
import com.feliscape.sanguis.registry.*;
import com.feliscape.sanguis.util.VampireUtil;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.jetbrains.annotations.Nullable;

public class VampireData extends DataAttachment {
    private LivingEntity holder;
    private int infectionTime = -1;
    private int startInfectionTime = -1;
    private boolean isVampire;

    private boolean wasBat;
    private boolean isBat;
    private float storedHumanoidHealth;
    @Nullable
    private Bat bat;
    private static final Multimap<Holder<Attribute>, AttributeModifier> BAT_ATTRIBUTES = ImmutableMultimap.of(
            NeoForgeMod.CREATIVE_FLIGHT, new AttributeModifier(Sanguis.location("vampire.bat_form.flight"),
                    1.0D, AttributeModifier.Operation.ADD_VALUE),
            Attributes.ATTACK_DAMAGE, new AttributeModifier(Sanguis.location("vampire.bat_form.damage"),
                    -1.0D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
            Attributes.MAX_HEALTH, new AttributeModifier(Sanguis.location("vampire.bat_form.health"),
                    -0.5D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    );

    private int tier;

    private VampireBloodData bloodData = new VampireBloodData();

    public static final StreamCodec<RegistryFriendlyByteBuf, VampireData> FULL_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            VampireData::getInfectionTime,
            ByteBufCodecs.INT,
            VampireData::getStartInfectionTime,
            ByteBufCodecs.BOOL,
            VampireData::isVampire,
            ByteBufCodecs.BOOL,
            VampireData::isBat,
            ByteBufCodecs.VAR_INT,
            VampireData::getTier,
            VampireBloodData.FULL_STREAM_CODEC,
            VampireData::getBloodData,
            VampireData::new
    );

    public VampireData(LivingEntity holder){
        this.setHolder(holder);
        if (holder.level().isClientSide() && this.bat == null)
            this.bat = EntityType.BAT.create(holder.level());
    }
    public VampireData(){

    }

    public VampireData(boolean isVampire, int tier){
        this.isVampire = isVampire;
        this.infectionTime = isVampire ? 0 : -1;
        this.tier = tier;
    }
    public VampireData(int infectionTime, boolean isVampire, int tier){
        this.infectionTime = infectionTime;
        this.isVampire = isVampire;
        this.tier = tier;
    }

    public VampireData(int infectionTime, int startInfectionTime, boolean isVampire, boolean isBat, int tier, VampireBloodData bloodData){
        this.infectionTime = infectionTime;
        this.startInfectionTime = startInfectionTime;
        this.isVampire = isVampire;
        this.isBat = isBat;
        this.tier = tier;
        this.bloodData = bloodData;
    }

    @Override
    protected void save(CompoundTag tag) {
        tag.putInt("infectionTime", infectionTime);
        tag.putInt("startInfectionTime", startInfectionTime);
        tag.putBoolean("isVampire", isVampire);
        tag.putBoolean("isBat", isBat);
        tag.putInt("tier", tier);
        tag.putFloat("storedHumanoidHealth", storedHumanoidHealth);
        tag.put("bloodData", VampireBloodData.save(new CompoundTag(), this.bloodData));
    }

    @Override
    protected void load(CompoundTag tag) {
        this.infectionTime = tag.getInt("infectionTime");
        this.startInfectionTime = tag.getInt("startInfectionTime");
        this.isVampire = tag.getBoolean("isVampire");
        this.isBat = tag.getBoolean("isBat");
        this.tier = tag.getInt("tier");
        this.storedHumanoidHealth = tag.getFloat("storedHumanoidHealth");
        this.bloodData = VampireBloodData.load(tag.getCompound("bloodData"));
    }

    public void toggleBatForm(){
        if (!this.canTransformIntoBat()) return;
        this.bloodData.addExhaustion(6.0F);
        this.isBat = !this.isBat;

        float health = this.holder.getHealth();
        updateBatAttributes();

        playSoundServer(isBat() ? SanguisSoundEvents.BAT_TRANSFORM.get() : SanguisSoundEvents.VAMPIRE_TRANSFORM.get());
        this.holder.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20));

        if (this.isBat){
            if (this.holder instanceof ServerPlayer serverPlayer)
                SanguisCriteriaTriggers.TRANSFORM_TO_BAT.get().trigger(serverPlayer);

            if (!this.holder.level().isClientSide())
                storedHumanoidHealth = Math.max(0.0F, health - this.holder.getMaxHealth());
        } else{
            if (!this.holder.level().isClientSide() && storedHumanoidHealth > 0.0F){
                this.holder.heal(storedHumanoidHealth);
                storedHumanoidHealth = 0.0F;
            }
        }


        this.holder.syncData(type());
        this.holder.refreshDimensions();
    }
    public void disableBatForm(){
        if (!this.isBat) return;

        this.isBat = false;
        updateBatAttributes();

        playSoundServer(SanguisSoundEvents.VAMPIRE_TRANSFORM.get());
        this.holder.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20));

        this.holder.syncData(type());
        this.holder.refreshDimensions();
    }

    private void updateBatAttributes(){
        if (this.isBat()) {
            this.holder.getAttributes().addTransientAttributeModifiers(BAT_ATTRIBUTES);
        }
        else {
            this.holder.getAttributes().removeAttributeModifiers(BAT_ATTRIBUTES);
        }
    }

    private boolean canTransformIntoBat() {
        if ((!isBat && tier == 0) || !this.holder.getData(VampireAbilityData.type()).hasAbility(SanguisVampireAbilities.BAT_TRANSFORMATION)){
            return false;
        }

        return (!VampireUtil.shouldBurnInSunlight(this.holder) && this.holder.getHealth() > 2.0F
                && this.bloodData.getBlood() >= (isBat ? 4 : 6)) || (this.holder instanceof Player player && player.getAbilities().invulnerable);
    }

    public Bat getBat() {
        return bat;
    }
    public void clientTick(){
        if (!isVampire || holder == null || !holder.level().isClientSide()) return;

        if (this.bat != null){
            this.bat.tick();
            this.bat.tickCount++;
            this.setBatRotation(false);
        }
    }

    public void setBatRotation(boolean includeX) {
        if (this.bat == null) return;

        this.bat.setYHeadRot(this.holder.getYHeadRot());
        this.bat.setYRot(this.holder.getYRot());
        this.bat.setYBodyRot(this.holder.getYRot());
        this.bat.setXRot(includeX ? this.holder.getXRot() : 0.0F);
    }

    public void tick(){
        if (infectionTime > 0){
            infectionTime--;
            checkVampireStatus();
        }

        if (wasBat != isBat){
            this.holder.refreshDimensions();
        }

        if (isVampire){
            if (!this.holder.level().isClientSide()) {
                if (this.holder.tickCount % 5 == 0 && VampireUtil.shouldBurnInSunlight(this.holder) && canBurn(this.holder)){
                    this.holder.igniteForSeconds(2.0F);
                }

                if (this.holder instanceof Player player) {
                    this.bloodData.tick(player);

                    if (this.holder.tickCount % 20 == 0){
                        BlockPos blockPos = this.holder.blockPosition();
                        if (BlockPos.findClosestMatch(blockPos, 4, 4, pos ->
                                this.holder.level().getBlockState(pos).is(SanguisTags.Blocks.VAMPIRE_REPELLENTS)).isPresent()){
                            this.holder.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 2, true, true));
                            this.holder.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0, true, true));
                        }
                    }
                }
            }
            if (this.isBat() && this.holder instanceof Player player){
                if (bloodData.getBlood() < 4 && !player.getAbilities().invulnerable){
                    this.disableBatForm();
                } else{
                    player.getAbilities().flying = true;
                }
            }
        }

        wasBat = isBat;
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
            if (wasVampire){
                revertFromVampire();
            }
        }
        if (wasVampire && !isVampire)
            this.disableBatForm();
    }

    private void transformToVampire(){
        this.disableBatForm();
        this.holder.refreshDimensions();
        bloodData.setBlood(bloodData.maxBlood());
        bloodData.setDrinkDelay(0);
        bloodData.setExhaustion(0.0F);
        this.holder.playSound(SanguisSoundEvents.VAMPIRE_TRANSFORM.get());
        if (this.holder instanceof ServerPlayer serverPlayer){
            SanguisCriteriaTriggers.VAMPIRE_TRANSFORMATION.get().trigger(serverPlayer);
        }
    }

    private void playSoundServer(SoundEvent soundEvent){
        this.holder.level().playSound(null, this.holder.getX(), this.holder.getY(), this.holder.getZ(), soundEvent, this.holder.getSoundSource(), 1.0F, 1.0F);
    }
    private void playSoundServer(SoundEvent soundEvent, float volume, float pitch){
        this.holder.level().playSound(null, this.holder.getX(), this.holder.getY(), this.holder.getZ(), soundEvent, this.holder.getSoundSource(), volume, pitch);
    }

    private void revertFromVampire(){
        this.disableBatForm();
        this.holder.refreshDimensions();
        this.tier = 0;
        if (this.holder instanceof Player player){
            player.getFoodData().setFoodLevel(2);
        }

        if (this.holder instanceof ServerPlayer serverPlayer){
            SanguisCriteriaTriggers.VAMPIRE_CURE.get().trigger(serverPlayer);
        }
    }

    public int getInfectionTime(){
        return infectionTime;
    }
    public int getStartInfectionTime(){
        return startInfectionTime;
    }

    public boolean isVampire(){
        return isVampire && infectionTime >= 0;
    }
    public boolean isBat(){
        return isVampire && isBat;
    }

    public int getTier() {
        return tier;
    }

    public boolean canUpgrade(){
        return tier == 0;
    }
    public void upgradeTier(){
        tier++;
    }

    /**
     * Makes this entity no longer vampiric.
     * @return Whether the vampire state changed
     */
    public boolean cure(){
        if (!this.isVampire && !this.isInfected()) return false;

        this.infectionTime = -1;
        this.checkVampireStatus();
        return true;
    }

    /**
     * Makes this entity no longer vampiric.
     * @return Whether the vampire state changed
     */
    public boolean cure(boolean causeEffects){
        boolean wasVampire = this.isVampire;
        if (this.cure()){
            if (causeEffects && wasVampire) {
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

    public void infect() {
        this.infect(10 * 60 * 20);
    }
    public void infect(int duration) {
        this.holder.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 1));
        this.infectionTime = duration;
        this.startInfectionTime = duration;
        this.holder.syncData(type());
    }
    public void infectImmediately() {
        this.infectionTime = 0;
        this.checkVampireStatus();
        this.holder.syncData(type());
    }

    public void reduceInfection(int amount) {
        this.infectionTime += amount;
        if (infectionTime > startInfectionTime + 5000){
            cure(false);
            this.holder.syncData(type());
        }
    }

    public static AttachmentType<VampireData> type(){
        return SanguisDataAttachmentTypes.VAMPIRISM.get();
    }

    public boolean isInfected() {
        return infectionTime > 0;
    }

    public VampireBloodData getBloodData() {
        return bloodData;
    }

    protected void setHolder(IAttachmentHolder holder){
        if (!(holder instanceof LivingEntity living)){
            throw new IllegalArgumentException("Trying to set VampireData holder to non-LivingEntity");
        }
        this.holder = living;
        if (living.level().isClientSide() && this.bat == null)
            this.bat = EntityType.BAT.create(living.level());
    }
    protected void setHolder(LivingEntity living){
        this.holder = living;
    }

    public static VampireData copyDeathPersistent(VampireData oldData, IAttachmentHolder holder, HolderLookup.Provider provider){
        VampireData newData;
        if (oldData.infectionTime != 0){
            newData = new VampireData(oldData.isVampire, oldData.tier);
        } else{
            newData = new VampireData(oldData.infectionTime, oldData.isVampire, oldData.tier);
        }
        newData.setHolder(holder);
        return newData;
    }

    public static VampireData getInstance(IAttachmentHolder iAttachmentHolder) {
        if (!(iAttachmentHolder instanceof LivingEntity living)){
            throw new IllegalArgumentException("Trying to attach VampireData to non-LivingEntity");
        }

        return new VampireData(living);
    }

    public VampireData update(IAttachmentHolder holder, RegistryFriendlyByteBuf buffer){
        if (!(holder instanceof LivingEntity living)){
            throw new IllegalArgumentException("Trying to read VampireData for non-LivingEntity");
        }

        this.infectionTime = buffer.readInt();
        this.startInfectionTime = buffer.readInt();
        this.isVampire = buffer.readBoolean();
        this.isBat = buffer.readBoolean();
        this.tier = buffer.readVarInt();
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
                buf.writeInt(attachment.startInfectionTime);
                buf.writeBoolean(attachment.isVampire);
                buf.writeBoolean(attachment.isBat);
                buf.writeVarInt(attachment.tier);
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
