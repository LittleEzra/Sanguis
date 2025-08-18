package com.feliscape.sanguis.content.attachment;

import com.feliscape.sanguis.registry.SanguisDataComponents;
import com.feliscape.sanguis.registry.SanguisItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class VampireBloodData {
    private int drinkDelay = 0;
    private int blood = maxBlood();
    private float exhaustion;

    public static final StreamCodec<RegistryFriendlyByteBuf, VampireBloodData> FULL_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            VampireBloodData::getDrinkDelay,
            ByteBufCodecs.INT,
            VampireBloodData::getBlood,
            ByteBufCodecs.FLOAT,
            VampireBloodData::getExhaustion,
            VampireBloodData::new
    );

    public VampireBloodData() {

    }

    public VampireBloodData(int drinkDelay, int blood, float exhaustion) {
        this.drinkDelay = drinkDelay;
        this.blood = blood;
        this.exhaustion = exhaustion;
    }

    public void drink(LivingEntity holder, LivingEntity target){
        if (EntityBloodData.canHaveBlood(target) && drinkDelay <= 0){
            increaseBlood(target.getData(EntityBloodData.type()).drain(), holder, true);
            this.drinkDelay = 18;
            holder.syncData(VampireData.type());
        }
    }

    public void drink(LivingEntity holder, int amount){
        increaseBlood(amount);
        holder.syncData(VampireData.type());
    }

    public void tick(Player player){
        if (drinkDelay > 0){
            drinkDelay--;
        }

        Difficulty difficulty = player.level().getDifficulty();
        if (difficulty != Difficulty.PEACEFUL) {
            increaseExhaustion(restingExhaustion());

            if (exhaustion >= 4.0F) {
                exhaustion -= 4.0F;
                decreaseBlood(1);
                player.syncData(VampireData.type());
            }
        }
    }


    public int maxBlood(){
        return 20;
    }

    /**
     * @return The amount that exhaustion is increased by each tick
     */
    public float restingExhaustion(){
        return 0.004F; // Same value as
    }

    public void increaseExhaustion(float amount){
        this.exhaustion += amount;
    }

    public float getExhaustion(){
        return exhaustion;
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
        return tag;
    }

    public static VampireBloodData load(CompoundTag tag) {
        VampireBloodData data = new VampireBloodData();
        data.blood = tag.getInt("blood");
        data.exhaustion = tag.getFloat("exhaustion");
        return data;
    }

    public void update(int drinkDelay, int blood, float exhaustion) {
        this.drinkDelay = drinkDelay;
        this.blood = blood;
        this.exhaustion = exhaustion;
    }
}
