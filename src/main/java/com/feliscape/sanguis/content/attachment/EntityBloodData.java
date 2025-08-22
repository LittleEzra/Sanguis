package com.feliscape.sanguis.content.attachment;

import com.feliscape.sanguis.data.damage.SanguisDamageSources;
import com.feliscape.sanguis.registry.SanguisDataAttachmentTypes;
import com.feliscape.sanguis.registry.SanguisDataMapTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jetbrains.annotations.Nullable;

public class EntityBloodData extends DataAttachment{
    private LivingEntity holder;
    int remainingBlood = 1;
    float saturation;
    int maxBlood = 1;
    int frozenTicks = 0;

    public EntityBloodData() {

    }

    @Override
    protected void save(CompoundTag tag) {
        tag.putInt("remainingBlood", this.getRemainingBlood());
        tag.putFloat("saturation", this.getSaturation());
        tag.putInt("maxBlood", this.getMaxBlood());
        tag.putInt("frozenTicks", this.getFrozenTicks());
    }

    @Override
    protected void load(CompoundTag tag) {
        this.remainingBlood = tag.getInt("remainingBlood");
        this.saturation = tag.getInt("saturation");
        this.maxBlood = tag.getInt("maxBlood");
        this.frozenTicks = tag.getInt("frozenTicks");
    }

    public EntityBloodData(int remainingBlood, float saturation, int maxBlood) {
        this.remainingBlood = remainingBlood;
        this.saturation = saturation;
        this.maxBlood = maxBlood;
    }
    public EntityBloodData(int remainingBlood, float saturation, int maxBlood, int frozenTicks) {
        this.remainingBlood = remainingBlood;
        this.saturation = saturation;
        this.maxBlood = maxBlood;
        this.frozenTicks = frozenTicks;
    }

    public int getRemainingBlood(){
        return remainingBlood;
    }
    private void setRemainingBlood(int remainingBlood){
        this.remainingBlood = remainingBlood;
    }

    public int getMaxBlood() {
        return maxBlood;
    }

    private void setMaxBlood(int maxBlood) {
        this.maxBlood = maxBlood;
    }

    public int getFrozenTicks() {
        return this.frozenTicks;
    }

    private void setFrozenTicks(int frozenTicks) {
        this.frozenTicks = frozenTicks;
    }

    public float getSaturation() {
        return saturation;
    }

    private void setSaturation(float saturation) {
        this.saturation = saturation;
    }

    public boolean isFrozen() {
        return getFrozenTicks() > 0;
    }

    public void tick(){
        if (this.frozenTicks > 0){
            this.frozenTicks--;
            this.holder.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 2));
            if (this.holder instanceof Mob mob){
                if (mob.getNavigation().isInProgress())
                    mob.getNavigation().stop();
                // Disable all flags
                /*mob.goalSelector.setControlFlag(Goal.Flag.MOVE, false);
                mob.goalSelector.setControlFlag(Goal.Flag.JUMP, false);
                mob.goalSelector.setControlFlag(Goal.Flag.LOOK, false);*/
                this.holder.setXRot(20.0F);
            }
        }
    }

    public int drain(){
        if (this.remainingBlood == 0) return 0;

        this.remainingBlood--;
        this.holder.hurt(SanguisDamageSources.draining(this.holder.level(), this.holder),
                remainingBlood == 0 ? this.holder.getMaxHealth() * 10.0F : holder.getMaxHealth() / (maxBlood + 1.0F));
        this.holder.syncData(type());
        this.frozenTicks = 20;
        return 1;
    }

    @SuppressWarnings("deprecation")
    public static EntityBloodData getInstance(IAttachmentHolder iAttachmentHolder){
        if (!(iAttachmentHolder instanceof LivingEntity entity)){
            throw new IllegalArgumentException("Trying to attach EntityBloodData to non-LivingEntity");
        }

        var bloodContent = entity.getType().builtInRegistryHolder().getData(SanguisDataMapTypes.ENTITY_BLOOD);

        if (bloodContent == null){
            throw new IllegalArgumentException("Entity %s does not have blood".formatted(entity.getDisplayName().getString()));
        }

        EntityBloodData data = new EntityBloodData(bloodContent.amount(), bloodContent.saturationModifier(), bloodContent.amount());
        data.setHolder(entity);
        return data;
    }

    @SuppressWarnings("deprecation")
    protected void setHolder(IAttachmentHolder holder){
        if (!(holder instanceof LivingEntity living)){
            throw new IllegalArgumentException("Trying to set BloodEntityData holder to non-LivingEntity");
        }

        var bloodContent = living.getType().builtInRegistryHolder().getData(SanguisDataMapTypes.ENTITY_BLOOD);

        if (bloodContent == null){
            throw new IllegalArgumentException("Entity %s does not have blood".formatted(living.getDisplayName().getString()));
        }

        this.holder = living;
    }
    @SuppressWarnings("deprecation")
    protected void setHolder(LivingEntity living){
        var bloodContent = living.getType().builtInRegistryHolder().getData(SanguisDataMapTypes.ENTITY_BLOOD);

        if (bloodContent == null){
            throw new IllegalArgumentException("Entity %s does not have blood".formatted(living.getDisplayName().getString()));
        }

        this.holder = living;
    }

    @SuppressWarnings("deprecation")
    public static boolean canHaveBlood(Entity entity){
        if (!(entity instanceof LivingEntity living)) return false;

        var bloodContent = living.getType().builtInRegistryHolder().getData(SanguisDataMapTypes.ENTITY_BLOOD);
        return bloodContent != null;
    }

    public static AttachmentType<EntityBloodData> type(){
        return SanguisDataAttachmentTypes.ENTITY_BLOOD.get();
    }

    public static class SyncHandler implements AttachmentSyncHandler<EntityBloodData> {

        @Override
        public void write(RegistryFriendlyByteBuf buf, EntityBloodData attachment, boolean initialSync) {
            buf.writeInt(attachment.getRemainingBlood());
            buf.writeFloat(attachment.getSaturation());
            buf.writeInt(attachment.getMaxBlood());
            buf.writeInt(attachment.getFrozenTicks());
        }

        @Override
        public @Nullable EntityBloodData read(IAttachmentHolder holder, RegistryFriendlyByteBuf buf, @Nullable EntityBloodData previousValue) {
            if (previousValue == null) {
                EntityBloodData newData = new EntityBloodData(buf.readInt(), buf.readInt(), buf.readInt());
                newData.setHolder(holder);
                return newData;
            } else {
                previousValue.setHolder(holder);
                previousValue.setRemainingBlood(buf.readInt());
                previousValue.setSaturation(buf.readFloat());
                previousValue.setMaxBlood(buf.readInt());
                previousValue.setFrozenTicks(buf.readInt());
                return previousValue;
            }
        }
    }
}
