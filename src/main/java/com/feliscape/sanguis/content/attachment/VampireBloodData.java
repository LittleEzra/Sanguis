package com.feliscape.sanguis.content.attachment;

import com.feliscape.sanguis.data.damage.SanguisDamageSources;
import com.feliscape.sanguis.registry.SanguisDataComponents;
import com.feliscape.sanguis.registry.SanguisSoundEvents;
import com.feliscape.sanguis.util.VampireUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;

public class VampireBloodData {
    private int drinkDelay = 0;
    private int blood = maxBlood();
    private float exhaustion;
    private float saturation;
    private int tickTimer;

    public static final StreamCodec<RegistryFriendlyByteBuf, VampireBloodData> FULL_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            VampireBloodData::getDrinkDelay,
            ByteBufCodecs.INT,
            VampireBloodData::getBlood,
            ByteBufCodecs.FLOAT,
            VampireBloodData::getExhaustion,
            ByteBufCodecs.FLOAT,
            VampireBloodData::getSaturation,
            ByteBufCodecs.INT,
            data -> data.tickTimer,
            VampireBloodData::new
    );

    public VampireBloodData() {

    }

    public VampireBloodData(int drinkDelay, int blood, float exhaustion, float saturation, int tickTimer) {
        this.drinkDelay = drinkDelay;
        this.blood = blood;
        this.exhaustion = exhaustion;
        this.saturation = saturation;
        this.tickTimer = tickTimer;
    }

    public void drink(LivingEntity holder, LivingEntity target){
        if (drinkDelay > 0 || !target.isAlive()) return;

        if (VampireUtil.hasFoulBlood(target)){
            target.hurt(SanguisDamageSources.draining(target.level(), target),
                    2.0F);
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 1));
            holder.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 2));
        }
        else if (target instanceof Player player){
            if (!VampireUtil.isVampire(player)){
                target.hurt(SanguisDamageSources.draining(target.level(), target),
                        2.0F);
                increaseBlood(1, holder, true);
                this.saturation = Math.min(this.saturation + 1.0F, blood);
                holder.syncData(VampireData.type());
            }
        }
        else if (EntityBloodData.canHaveBlood(target)){
            var data = target.getData(EntityBloodData.type());
            increaseBlood(data.drain(), holder, true);
            this.saturation = Math.min(this.saturation + data.getSaturation(), blood);
            holder.syncData(VampireData.type());
        }

        float f = holder.getRandom().nextFloat() * 0.2F + 0.9F;
        this.playSoundServer(holder, SanguisSoundEvents.VAMPIRE_DRINK.get(), 1.0F, f);

        this.drinkDelay = 14;
    }

    private void playSoundServer(LivingEntity entity, SoundEvent soundEvent){
        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), soundEvent, entity.getSoundSource(), 1.0F, 1.0F);
    }
    private void playSoundServer(LivingEntity entity, SoundEvent soundEvent, float volume, float pitch){
        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), soundEvent, entity.getSoundSource(), volume, pitch);
    }

    public void drink(LivingEntity holder, int amount, float saturation){
        increaseBlood(amount);
        this.saturation = Math.min(this.saturation + saturation, blood);
        holder.syncData(VampireData.type());
    }

    public void tick(Player player){
        if (drinkDelay > 0){
            drinkDelay--;
        }

        if (!player.getAbilities().invulnerable) {
            Difficulty difficulty = player.level().getDifficulty();

            if (this.exhaustion > 4.0F) {
                this.exhaustion -= 4.0F;
                if (this.saturation > 0.0F) {
                    this.saturation = Math.max(this.saturation - 1.0F, 0.0F);
                } else if (difficulty != Difficulty.PEACEFUL) {
                    this.blood = Math.max(this.blood - 1, 0);
                }
            }

            addExhaustion(getTickExhaustion(player));

            if (exhaustion >= 4.0F) {
                exhaustion -= 4.0F;
                decreaseBlood(1);
                player.syncData(VampireData.type());
            }

            boolean canRegenerate = player.level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);
            if (canRegenerate && this.saturation > 0.0F && player.isHurt() && this.blood >= 20) {
                this.tickTimer++;
                if (this.tickTimer >= 10) {
                    float f = Math.min(this.saturation, 6.0F);
                    player.heal(f / 6.0F);
                    this.addExhaustion(f);
                    this.tickTimer = 0;
                }
            } else if (canRegenerate && this.blood >= 18 && player.isHurt()) {
                this.tickTimer++;
                if (this.tickTimer >= 80) {
                    player.heal(1.0F);
                    this.addExhaustion(6.0F);
                    this.tickTimer = 0;
                }
            } else if (this.blood <= 0) {
                this.tickTimer++;
                if (this.tickTimer >= 80) {
                    if (player.getHealth() > 10.0F || difficulty == Difficulty.HARD || player.getHealth() > 1.0F && difficulty == Difficulty.NORMAL) {
                        player.hurt(player.damageSources().starve(), 1.0F);
                    }

                    this.tickTimer = 0;
                }
            } else {
                this.tickTimer = 0;
            }

            // Assign the food level to the blood level for effects such as no sprinting at 3 or less blood drops
            player.getFoodData().setFoodLevel(this.getBlood());
        }
    }


    public int maxBlood(){
        return 20;
    }

    /**
     * @return The amount that exhaustion is increased by each tick
     */
    public float getTickExhaustion(Player player){
        return VampireUtil.isBat(player) ? getBatExhaustion(player) : 0.004F; // Same value as
    }
    public float getBatExhaustion(Player player){
        if (player.isSprinting()) return 0.009F;
        return 0.006F;
    }

    public void addExhaustion(float amount){
        this.exhaustion = Math.min(this.exhaustion + amount, 40.0F);
    }

    public float getExhaustion(){
        return exhaustion;
    }
    public float getSaturation(){
        return saturation;
    }

    public int decreaseBlood(int amount){
        this.blood = Math.max(this.blood - amount, 0);
        return this.blood;
    }
    public int increaseBlood(int amount){
        this.blood = Math.min(this.blood + amount, maxBlood());
        return this.blood;
    }
    public int increaseBlood(int amount, LivingEntity entity, boolean addExcessToBottles){
        if (amount == 0) return this.blood;

        if (addExcessToBottles){
            int excess = this.blood + amount - maxBlood();
            if (excess > 0){
                addToBottles(entity, excess);
            }
        }

        this.blood = Math.min(this.blood + amount, maxBlood());
        return this.blood;
    }

    private void addToBottles(LivingEntity entity, int excess) {
        if (entity instanceof Player player && excess > 0){
            ItemStack offhand = entity.getOffhandItem();
            if (offhand.has(SanguisDataComponents.MAX_BLOOD)){
                int blood = offhand.getOrDefault(SanguisDataComponents.BLOOD, 0);
                int maxBlood = offhand.getOrDefault(SanguisDataComponents.MAX_BLOOD, 0);
                if (maxBlood <= 0) return;

                int amount = Math.min(maxBlood - blood, excess);
                excess -= amount;
                offhand.set(SanguisDataComponents.BLOOD, blood + amount);
                if (excess <= 0){
                    return;
                }
            }
            ItemStack mainHand = entity.getMainHandItem();
            if (mainHand.has(SanguisDataComponents.MAX_BLOOD)){
                int blood = mainHand.getOrDefault(SanguisDataComponents.BLOOD, 0);
                int maxBlood = mainHand.getOrDefault(SanguisDataComponents.MAX_BLOOD, 0);
                if (maxBlood <= 0) return;

                int amount = Math.min(maxBlood - blood, excess);
                excess -= amount;
                mainHand.set(SanguisDataComponents.BLOOD, blood + amount);
                if (excess <= 0){
                    return;
                }
            }

            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack itemStack = player.getInventory().getItem(i);
                if (itemStack.has(SanguisDataComponents.MAX_BLOOD)){
                    int blood = itemStack.getOrDefault(SanguisDataComponents.BLOOD, 0);
                    int maxBlood = itemStack.getOrDefault(SanguisDataComponents.MAX_BLOOD, 0);
                    if (maxBlood <= 0) return;

                    int amount = Math.min(maxBlood - blood, excess);
                    excess -= amount;
                    itemStack.set(SanguisDataComponents.BLOOD, blood + amount);
                    if (excess <= 0){
                        return;
                    }
                }
            }
            player.getInventory().setChanged();
        }
    }

    public int getBlood(){
        return blood;
    }

    public int getDrinkDelay() {
        return drinkDelay;
    }

    public void setDrinkDelay(int drinkDelay) {
        this.drinkDelay = drinkDelay;
    }

    public void setBlood(int blood) {
        this.blood = blood;
    }

    public void setExhaustion(float exhaustion) {
        this.exhaustion = exhaustion;
    }

    public static CompoundTag save(CompoundTag tag, VampireBloodData data) {
        tag.putInt("blood", data.blood);
        tag.putFloat("exhaustion", data.exhaustion);
        tag.putFloat("saturation", data.saturation);
        tag.putInt("foodTickTimer", data.tickTimer);
        return tag;
    }

    public static VampireBloodData load(CompoundTag tag) {
        VampireBloodData data = new VampireBloodData();
        data.blood = tag.getInt("blood");
        data.exhaustion = tag.getFloat("exhaustion");
        data.saturation = tag.getFloat("saturation");
        data.tickTimer = tag.getInt("foodTickTimer");
        return data;
    }

    public void update(RegistryFriendlyByteBuf byteBuf) {
        this.drinkDelay = byteBuf.readInt();
        this.blood = byteBuf.readInt();
        this.exhaustion = byteBuf.readFloat();
        this.saturation = byteBuf.readFloat();
        this.tickTimer = byteBuf.readInt();
    }
}
